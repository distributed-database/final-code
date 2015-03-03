//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.Socket;
//import java.util.Hashtable;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
//public class GetStatistics {
//	
//	public void getStatistics(String jsonString) throws JSONException, IOException{
//		String jsonResult=null;
//		String jsonMessageString = null;
//		String relation1=null;
//		String relation2=null;
//		String attribute=null;
//		DataOutputStream dout;
//		DataInputStream din;
//		InputStream input;
//		OutputStream output;
//		Socket s = null;
//		
//		
//		JSONObject obj = new JSONObject(jsonString);
//        JSONArray array = obj.getJSONArray("input");
//        for (int i = 0; i < array.length(); i++) {
//        	
//        	relation1=array.getJSONObject(i).getString("relation1");
//        	relation2=array.getJSONObject(i).getString("relation2");
//        	attribute=array.getJSONObject(i).getString("attribute");
//        	
////        	===================================================================
////        	working for relation 1
//        	System.out.println(relation1+" "+relation2+" "+attribute);
//        	System.out.println(Server.hashTable.get(relation1));
//        	s=new Socket(Server.hashTable.get(relation1),8000);
//        	dout=new DataOutputStream(s.getOutputStream());
//        	
//        	
//			
//			jsonMessageString = "{relation: "+relation1+", attribute: "+attribute+" }";
//			dout.writeUTF(jsonMessageString);
//
//			System.out.println("Sent request to "+relation1 + "Now waiting for the reply");
//			dout.flush();
////			dout.close();
//			
//			din = new DataInputStream(s.getInputStream());
//			
//			jsonResult = (String)din.readUTF();
//			Server.resultTable.put(relation1, jsonResult);
//			System.out.println("Got the reply from the"+ relation1);
//			System.out.println(jsonResult);
//			din.close();
//			s.close();
////			==============================================================================
////			working for the relation 2
//			
//        	System.out.println(Server.hashTable.get(relation2));
//        	s=new Socket(Server.hashTable.get(relation2),8000);
//        	dout=new DataOutputStream(s.getOutputStream());
//        	
//        	
//			
//			jsonMessageString = "{relation: "+relation2+", attribute: "+attribute+" }";
//			dout.writeUTF(jsonMessageString);
//
//			System.out.println("Sent request to "+relation2 + "Now waiting for the reply");
//			dout.flush();
//			dout.close();
//			
//			din = new DataInputStream(s.getInputStream());
//			jsonResult = (String)din.readUTF();
//			Server.resultTable.put(relation2, jsonResult);
//			System.out.println("Got the reply from the"+ relation2);
//			din.close();
//			s.close();
//																																													
//		}
//		
//	}
//}
