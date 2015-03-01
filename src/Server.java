import java.util.Hashtable;




public class Server {
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		
		MultiThreadedServer server = new MultiThreadedServer(9000);
		new Thread(server).start();

		
		try {
		    Thread.sleep(20 * 5000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		System.out.println(MultiThreadedServer.hashTable.get("departments"));
		System.out.println("Stopping Server");
		server.stop();
	
	}

}


