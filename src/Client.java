import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;


public class Client {

	/**
	 * @param args
	 * @throws UnknownHostException 
	 * @throws IOException 
	 * @throws JSONException 
	 */
	
	
	public static void main(String[] args) throws UnknownHostException, IOException  {
		
		UpdateStatistics statistics = new UpdateStatistics();
		statistics.run();
		
		ReplyServer reply = new ReplyServer();
		reply.run();
		
		
		Socket serverSocket = new Socket("192.168.40.44",9000);
		OutputStream outputStream = serverSocket.getOutputStream();
		DataOutputStream dout=new DataOutputStream(outputStream);
		dout.writeUTF("TEST MESSAGE");
		dout.flush();  
		outputStream.close();
		dout.close();
		serverSocket.close();
		
		
		
		
		
		
		// TODO Auto-generated method stub
//		UpdateStatistics test = new UpdateStatistics();
//		serverSocket =new Socket("192.168.40.44",9000);
////		new Thread(new UpdateStatistics()).start();
//		
//		test.run();
//		Thread.sleep(5*1000);
//		System.out.println("Waiting for the server to connect ");
//		String relation = null;
//		String attribute= null;
//		String replyString = null;
//		String inputString = null;
//		ServerSocket ss=new ServerSocket(8000); 
//		
//		System.out.println("Waiting for the server to connect ");
//		Socket s=ss.accept();  
//		String string=null;
//		DataInputStream dis=new DataInputStream(s.getInputStream());
//		DataOutputStream dout=new DataOutputStream(s.getOutputStream());
//        while(true){
//        	
//
//			string = dis.readUTF();
//            System.out.println(string);
////			string = "{relation: departments, attribute: dept_no}";
////            JSONObject obj = new JSONObject(inputString);
////            relation = obj.getString("relation");
////            attribute = obj.getString("attribute");
////            
//            
////            ReplyServer reply = new ReplyServer("employees", relation, attribute);
////            replyString = reply.run();
////            dout.writeUTF(reply.run());
//            
//            dout.writeUTF("some text for test");
//            System.out.println("sent the msg");
//
////            dout.writeUTF(replyString);
//            
//            dout.flush();  
////            din.close();
//            } 
////        dout.flush();
////      dis.close();
////      dout.close();
//		
//		//ReplyServer reply = new ReplyServer( "employees", "salaries", "emp_no");
//		//reply.run();
//	
		}

}
