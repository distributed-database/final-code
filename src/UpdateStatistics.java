import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mysql.jdbc.DatabaseMetaData;

public class UpdateStatistics {
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
	
	/*
	 * connection function : with database name 'dbName'
	 */
	public Connection getConnection(String dbName) throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);

		conn = DriverManager.getConnection("jdbc:mysql://"
				+ this.serverName + ":" + this.portNumber + "/" + this.dbName,
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
	        stmt.executeUpdate(command); // This will throw a SQLException if it fails
	        return true;
	    } finally {

	    	// This will run whether we throw an exception or not
	        if (stmt != null) { stmt.close(); }
	    }
	}
	
	
	/*
	 * print table names
	 */
	
	public void printTables(Connection conn){
		// Get the table names
		try {
			java.sql.DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			while (rs.next()) {
//			  System.out.println(rs.getString(3));
				this.printAttributes(conn, rs.getString(3));
			}
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table");
			e.printStackTrace();
			return;
		}
		
	}
	
	/*
	 * print attributes of the given table
	 */
	public void printAttributes(Connection conn, String tableName){
		ResultSet rsColumns = null;
		String typeName = "";
		String columnNameString = "";
		long cardinality = 0;
	    java.sql.DatabaseMetaData meta;
		try {
			meta = conn.getMetaData();
			rsColumns = meta.getColumns(null, null, tableName , null);
			while (rsColumns.next()) {
				typeName = rsColumns.getString("TYPE_NAME");
				columnNameString = rsColumns.getString("COLUMN_NAME");
				cardinality = getCardinality(conn, tableName, columnNameString);
		      System.out.println(tableName+"   "+ typeName + "   " + columnNameString + "   " + cardinality);
		    }
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/*
	 * Get the cardinality of the attribute in a table
	 * connection, table name, attribute
	 */
	public long getCardinality ( Connection conn, String tableName, String attributeName){
		String query = "select count("+ attributeName + ") from "+ tableName + ";" ;
		Statement stmt = null;
		ResultSet rs = null;
		long cardinality = 0;
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
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
			return;
		}
		
		Connection stat = null;
		try {
			stat = this.getConnection(statisticsDb);
			System.out.println("Connected to database "+ statisticsDb);
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		this.printTables( conn );
		
		
		
		/*
		 * close connection
		 */
		try {
			conn.close();
			stat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}


