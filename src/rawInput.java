import java.io.*;

public class rawInput {
	userInput ip = new userInput();
	public void ReadInput(){
		//Actual reading function
		//reads the input from the user
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter name of relation1");
		try {
	         ip.relation1 = br.readLine();
	      } catch (IOException ioe) {
	         System.out.println("IO error trying to read relation 1!");
	         System.exit(1);
	      }
		System.out.println("Enter name of relation2");
		try {
	         ip.relation2 = br.readLine();
	      } catch (IOException ioe) {
	         System.out.println("IO error trying to read relation 2!");
	         System.exit(1);
	      }
		
		System.out.println("Enter name of Attribute");
		try {
	         ip.attribute = br.readLine();
	      } catch (IOException ioe) {
	         System.out.println("IO error trying to read Attribute name!");
	         System.exit(1);
	      }
	}
	public void PrintInput(){
		//print the input to the console 
		//debugging purposes
		System.out.println(ip.attribute);
		System.out.println(ip.relation1);
		System.out.println(ip.relation2);
		
	}
}
class userInput{
	public String relation1;
	public String relation2;
	public String attribute;
}