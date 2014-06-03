import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Terminal Runner is used to begin Process Manager and to parse the inputs of the user
 */
import java.util.Arrays;

/**
 * @author Anu Vedantham
 *
 */
public class TerminalRunner {
	
private static ProcessManager manager;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 *  @TODO give processManager information to finishing implementing ps 
		 *  and adding a new process
		 */
		manager = new ProcessManager();
		
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
						String className = tokens[0];
						String[] argArray = Arrays.copyOfRange(tokens, 1, tokens.length);
						try{
							manager.addProcess(className, argArray);
						}
						catch(Exception e){
							System.out.println("Process "+ className + " not available");	
						}
							
					}
					
					break;				
			}

		}
	}

}
