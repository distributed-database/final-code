import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class UpdateStatistics implements Runnable{
	/*
	 * details of databases
	 */
	
	private final String userName = "project";

	private final String password = "project";

	private final String serverName = "localhost";

	private final int portNumber = 3306;
	//this is the original database
	private final String dbName = "employees";
	//for database statistics
	private final String statisticsDb = "statistics"; 
	
	private final String statTable = "stats";
	
	/*
	 * connection function : with database name 'dbName'
	 */
	public Connection getConnection(String databaseName) throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);

		conn = DriverManager.getConnection("jdbc:mysql://"
				+ this.serverName + ":" + this.portNumber + "/" + databaseName,
				connectionProps);

		return conn;
	}
	
	/*
	 * Execute SQL Query with connection 'conn' and SQL command 'command'
	 */
	public boolean executeUpdate(Connection conn, String command) throws SQLException {
	    Statement stmt = null;
	    try {
	        stmt = conn.createStatement();
	        stmt.executeUpdate(command); 
	        return true;
	    } finally {

	        if (stmt != null) { stmt.close(); }
	    }
	}
	
	
	/*
	 * get table names from database as resultset object
	 */
	
	public ResultSet getTables(Connection conn) throws SQLException{
		// Get the table names
		ResultSet rs = null;
		try {
			java.sql.DatabaseMetaData md = conn.getMetaData();
			rs = md.getTables(null, null, "%", null);
			return rs;

	    }finally { 
	    	//do nothing
	    }
		
	}
	
	/*
	 * print attributes of the given table
	 */
	public ResultSet getAttributes(Connection conn, String tableName) throws SQLException{
		ResultSet rsColumns = null;

	    java.sql.DatabaseMetaData meta;
		try {
			meta = conn.getMetaData();
			rsColumns = meta.getColumns(null, null, tableName , null);
			return rsColumns;
		} finally {
			//System.out.println(" attributes of " + tableName);
		}
	}
	
	/*
	 * Get the cardinality of the attribute in a table
	 * connection, table name, attribute
	 */
	public long getCardinality ( Connection conn,String dbName, String tableName, String attributeName){
		String query = "select count("+ attributeName + ") from "+ tableName + ";" ;
		//query = "SELECT CARDINALITY FROM STATISTICS WHERE TABLE_SCHEMA = \'" + dbName + "\' AND TABLE_NAME = \'" + tableName + "\' AND COLUMN_NAME = \'" + attributeName + "\';";
		Statement stmt = null;
		ResultSet rs = null;
		long cardinality = 0;
		try{
			stmt = conn.createStatement();
			System.out.println(query);
			rs = stmt.executeQuery(query);
			if (rs == null){
				return 0 ;
			}
			rs.next();
			cardinality = rs.getInt(1);
			rs.close();
			stmt.close();
			
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cardinality;
	}
	
	public long getDomainCount (Connection conn, String tableName, String attributeName ){
		String query = "select count(*) from (select distinct " + attributeName +" from " + tableName + " ) for_domain_count ; " ;
		Statement stmt = null;
		ResultSet rs = null;
		long domain = 0;
		try{
			stmt = conn.createStatement();
//			System.out.println(query);
			rs = stmt.executeQuery(query);
			if (rs == null){
				return 0 ;
			}
			rs.next();
			domain = rs.getInt(1);
			rs.close();
			stmt.close();
			
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return domain;
	}
	
	public int getLength(Connection conn, String tableName, String attributeName){
		String query = "select AVG(LENGTH("+ attributeName+")) from " +tableName ;
		Statement stmt = null;
		ResultSet rs = null;
		int length = 0;
		try{
			stmt = conn.createStatement();
//			System.out.println(query);
			rs = stmt.executeQuery(query);
			if (rs == null){
				return 0 ;
			}
			rs.next();
			length = rs.getInt(1);
			rs.close();
			stmt.close();
			
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return length;
	}
	/*
	 * update statistics database if any change. 
	 * original database connection in conn
	 * statistics database in stat
	 * statistics table in tableName
	 */
	public void statistics(Connection conn, Connection stat, Connection information , String statTable){
		
		// Read table name
		
		
		ResultSet tableSet = null;
		ResultSet attributeSet = null;
		ResultSet cardinalitySet = null;
		String tableName = null;
		String columnName = null;
		int length = 0;
		long cardinality = 0;
		long domain = 0;
		int statCardinality = 0;
		Statement stmt = null;
		String command = null;
		try {
			tableSet = getTables(conn);
			while (tableSet.next()) {
				tableName = tableSet.getString(3);
				attributeSet = this.getAttributes(conn, tableName);
				while (attributeSet.next()) {
//					typeName = rsColumns.getString("TYPE_NAME");
					columnName = attributeSet.getString("COLUMN_NAME");
					cardinality = getCardinality(conn,this.dbName, tableName, columnName);
					domain = getDomainCount(conn, tableName, columnName);
					
//					================================================
					
					System.out.println(tableName+"   "+  columnName + "   " + cardinality  + "     "+ domain);
					
//					================================================
					
					command = "select count(*) from (select * from " + statTable + " where relation_name = \'" + tableName +
							"\' and attribute_name = \'"+ columnName + "\')attribute_only ; ";
					
					stmt = stat.createStatement();
					cardinalitySet = stmt.executeQuery(command);
					if (cardinalitySet == null){
						continue;
					}
					cardinalitySet.next();
					statCardinality = cardinalitySet.getInt(1);
					
					length = getLength(conn, tableName, columnName);
					
					if (statCardinality == 0){
						command = "insert into "+ statTable + "(relation_name,attribute_name,domain, cardinality, column_length ) values (\'" + tableName + "\' ,\'" + 
								columnName + "\',"+domain+ " , "+ cardinality+" ,"+ length + ")";
//						System.out.println(command);
						stmt.executeUpdate(command);
					}
					else{
						command = "select * from " + statTable + " where relation_name = \'" + tableName +"\' and attribute_name = \'"+ columnName + "\'";
//						System.out.println(command);
						ResultSet rs = stmt.executeQuery(command);
						if (rs == null){
							continue;
						}
						
						rs.next();
						long oldCardinality = rs.getLong(4);
						int id = rs.getInt(1);
						if(oldCardinality != cardinality){
							command = "update "+ statTable+ " set cardinality = "+ cardinality +", domain = "+ domain + ", column_length ="+ length + " where id="+ id ;
//							System.out.println(command);
//							stmt.executeUpdate(command);
						}
					}
					
					
			    }
				break;
			}
			stmt.close();
			tableSet.close();
			attributeSet.close();
			cardinalitySet.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}


	/*
	 * function to run these functions
	 * 
	 */
	public void run() {

		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection(dbName);
			System.out.println("Connected to database " + dbName );
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}
		
		Connection stat = null;
		try {
			stat = this.getConnection(statisticsDb);
			System.out.println("Connected to database "+ statisticsDb);
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}
		Connection information = null;
		try {
			information = this.getConnection("information_schema");
			System.out.println("Connected to Database: information_schema ");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}
		

		this.statistics(conn, stat, information, statTable);
		
		/*
		 * close connection
		 */
		try {
			conn.close();
			stat.close();
			information.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

	
	
}


