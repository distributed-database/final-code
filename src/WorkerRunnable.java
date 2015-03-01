
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

    protected Socket clientSocket = null;
    protected String relationName   = null;

    public WorkerRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;

    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            
            DataInputStream dis=new DataInputStream(input);  
            String string = (String)dis.readUTF();
            System.out.println(string);
            JSONObject obj = new JSONObject(string);
            JSONArray array = obj.getJSONArray("relation");
            for (int i = 0; i < array.length(); i++) {
            	String relationName=array.getJSONObject(i).getString("name");
    			MultiThreadedServer.hashTable.put(relationName,clientSocket);
    			//System.out.println(MultiThreadedServer.hashTable.get(relationName));`																																											
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

