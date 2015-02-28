import java.io.*;

public class rawInput {
	
	public userInput[] ReadInput(){
		//Actual reading function
		//reads the input from the user
		
		int num=1;
		String temp=null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("number of inputs");
		try {
	         num = Integer.parseInt(br.readLine());
	      } catch (IOException ioe) {
	         System.out.println("IO error trying to read relation 1!");
	         System.exit(1);
	      }
		userInput[] ip = new userInput[4];
		System.out.println("enter relations in order of Relation1 Relation2 Attribute");
		for(int i=0;i<num;i++){
			try {
		         temp = br.readLine();
		      } catch (IOException ioe) {
		         System.out.println("IO error trying to read relation 1!");
		         System.exit(1);
		      }
			String[] arr = temp.split(" ");
			ip[i] = new userInput(arr[0], arr[1], arr[2]);
		}

		return ip;
	}
	public void PrintInput(userInput ip[]){
		//print the input to the console 
		//debugging purposes
		
		System.out.println(ip[1].relation1);
		System.out.println(ip[1].relation2);
		System.out.println(ip[1].attribute);
		
	}
}
class userInput{
	public String relation1;
	public String relation2;
	public String attribute;
	
	userInput(String r1,String r2, String a){
		//constructor for userInputobject
		relation1 = r1;
		relation2 = r2;
		attribute = a;
	}
}