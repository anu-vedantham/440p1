import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Terminal Runner is used to begin Process Manager and to parse the inputs of the user
 */

/**
 * @author Anu Vedantham
 *
 */
public class TerminalRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean stillRunning = true;
		InputStreamReader ir = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(ir);
		while(stillRunning){
			String input = "";
			try{
				System.out.print(">>");
				input = br.readLine();
			}
			catch(Exception e){
				System.out.println("Error: "+e);
				System.out.println("Check for any issues and retype the command");
			}
			switch(input){
				case "quit":
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					finally{
						stillRunning = false;
						System.out.println("exiting...");
					}
					break;
				case "ps" :
					/**
					 * @TODO must access ProcessManager's list of processes and
					 * print the classname of the process, arguments, 
					 * as well as the process' id (pid) 
					 */
					System.out.println("No Processes currently running");
					break;
				default:
					/*
					 * Assuming the format of the input is:
					 * 'ClassName arg[0] arg[1] ... arg[n-1]'
					 *  where n is the number of arguments the 
					 *  class' constructor is taking 
					 */
					
					if (input.length() < 1)
						System.out.println("not a valid argument");
					else{
						String[] tokens = input.split(" ");
						System.out.println("Class name: "+ tokens[0]);
						if (tokens.length >1){
							for(int i = 1; i<tokens.length; i++){
								System.out.println("arg "+ i +": "+ tokens[i]);
							}
						}
					}
					
					break;				
			}

		}
	}

}
