//Downbloads the initial statistics from the server and saves it in a hash map
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**

 */
public class WorkerRunnable implements Runnable{

    public Socket clientSocket = null;
    public String relationName   = null;
    

    public WorkerRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;

    }

    public void run() {
        try {
            String ipAddress = clientSocket.getRemoteSocketAddress().toString();
            ipAddress=ipAddress.substring(1,14);
 
        	InputStream input  = clientSocket.getInputStream();
            
            DataInputStream dis=new DataInputStream(input);  
            String string = (String)dis.readUTF();
            JSONObject obj = new JSONObject(string);
            JSONArray array = obj.getJSONArray("relation");
            for (int i = 0; i < array.length(); i++) {
            	String relationName=array.getJSONObject(i).getString("name");
    			Server.hashTable.put(relationName,ipAddress);
    			System.out.println("Got info about "+ relationName);																																										
    		}
            input.close();
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

