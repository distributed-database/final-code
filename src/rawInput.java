//Actual reading function
//reads the input from the user and returns an array of objects

import java.io.*;

public class rawInput {
	
	public String ReadInput(){
		
		int num=1;
		String temp=null;
		String jsonString = "{input : [";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("number of inputs");
		try {
	         num = Integer.parseInt(br.readLine());
	      } catch (IOException ioe) {
	         System.out.println("IO error trying to read relation 1!");
	         System.exit(1);
	      }
		
		System.out.println("enter relations in order of Relation1 Relation2 Attribute");
		for(int i=0;i<num;i++){
			try {
		         temp = br.readLine();
		      } catch (IOException ioe) {
		         System.out.println("IO error trying to read relation 1!");
		         System.exit(1);
		      }
			String[] arr = temp.split(" ");
			if(i!=0){jsonString = jsonString+" , ";}
			jsonString  = jsonString + "{ relation1:" +arr[0]+ ", relation2:"+ arr[1]+", attribute:" +arr[2]+ "}";
		
		}
		jsonString = jsonString + "]}";
		System.out.println("Created an object of input");
		return jsonString;
	}
}
