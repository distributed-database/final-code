import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/*
 * the things to be replied to server as per the request
 */
public class ReplyServer {
//	private Connection information = null;
	private String dbName = null;
	private String tableName = null;
	private String columnName = null;
	
	private final String userName = "project";

	private final String password = "project";

	private final String serverName = "localhost";

	private final int portNumber = 3306;
	
	private final String statisticsDb = "statistics"; 
	
	private final String statTable = "stats";
	
	
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
	
	public void getStatistics(Connection statistics){
		
		Statement stmt = null;
		String query = "select domain, cardinality, column_length from "+ statTable +"\';" ;
		ResultSet rs = null;
		long cardinality = 0;
		long domainCount = 0;
		int length =0;
		
		try{
			stmt = statistics.createStatement();
			System.out.println(query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
			    System.out.println(rs.getString(1)); //gets the first column's rows.
			}
			
			
			
//			rs.next();
//			domainCount = rs.getLong(1);
//			cardinality = rs.getLong(2);
//			length = rs.getInt(3);
			rs.close();
			stmt.close();
			
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		double selectivityFactor = 0;
//		if(cardinality != 0){
//			selectivityFactor = (double) domainCount / cardinality ;
//		}
//		String str = "{cardinality: "+ cardinality + ", selectivity:"+ selectivityFactor+ ",length:"+ length +"}";
//		return str;
	}
	
	public void run() {
		// TODO Auto-generated method stub
//		try {
//			this.information = this.getConnection("information_schema");
//			System.out.println("Connected to Database: information_schema ");
//		} catch (SQLException e) {
//			System.out.println("ERROR: Could not connect to the database");
//			e.printStackTrace();
//			return;
//		}
//		
		Connection stat = null;
		try {
			stat = this.getConnection(statisticsDb);
			System.out.println("Connected to database "+ statisticsDb);
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			
		}
		
//		String str= getStatistics(stat);
		getStatistics(stat);
		
		
		try {
//			information.close();
			stat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.out.println(str);
//		return str;
	}
	
	
	

}
