import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;


public class ServerDatabase {
	private final String userName = "project";

	private final String password = "project";

	private final String serverName = "localhost";

	private final int portNumber = 3306;
	//this is the original database

	//for database statistics
	private final String statisticsDb = "statistics"; 
	
	private final String statTable = "stats";

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);

		conn = DriverManager.getConnection("jdbc:mysql://"
				+ this.serverName + ":" + this.portNumber + "/statistics",
				connectionProps);

		return conn;
	}
	
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
	
	public void updateDB(JSONArray jsonArray) throws SQLException, JSONException{
		Connection con = getConnection();
		String command= null;
		Statement stmnt=con.createStatement();
		command = "DROP TABLE IF EXISTS stats";
		stmnt.execute(command);
		command = "create table stats ( id INT NOT NULL AUTO_INCREMENT, relation_name VARCHAR(64) NOT NULL, attribute_name VARCHAR(64) NOT NULL,domain BIGINT NOT NULL, cardinality BIGINT NOT NULL, column_length INT NOT NULL, PRIMARY KEY ( id) );";
		stmnt.execute(command);
		for (int i = 0; i < jsonArray.length(); i++) {
        	String relationName=jsonArray.getJSONObject(i).getString("relation_name");
        	String attributeName=jsonArray.getJSONObject(i).getString("attribute_name");
        	Long cardinality=jsonArray.getJSONObject(i).getLong("cardinality");
        	Long domain=jsonArray.getJSONObject(i).getLong("domain");	
        	int columnLength=jsonArray.getJSONObject(i).getInt("column_length");
        	
    		command = "insert into "+ statTable + "(relation_name,attribute_name,domain, cardinality, column_length ) values (\'" + relationName + "\' ,\'" + 
					attributeName + "\',"+domain+ " , "+ cardinality+" ,"+ columnLength + ")";
//			System.out.println(command);
			stmnt.executeUpdate(command);
        	
        	}
		stmnt.close();
		
	}
}
