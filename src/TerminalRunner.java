import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
private static ProcessServer server;
private static boolean stillRunning = true;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 *  @TODO give processManager information to finishing implementing ps 
		 *  and adding a new process
		 */
		manager = new ProcessManager();
		TerminalRunner runner = new TerminalRunner();
		InputStreamReader ir = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(ir);
		startServer();
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
					server.stop();
					
					break;
				case "ps" :
					/**
					 * @TODO must access ProcessManager's list of processes and
					 * print the classname of the process, arguments, 
					 * as well as the process' id (pid) 
					 */
					System.out.print(manager.listProcesses());
					break;
				default:
					/*
					 * Assuming the format of the input is:
					 * 'ClassName arg[0] arg[1] ... arg[n-1]'
					 *  where n is the number of arguments the 
					 *  class' constructor is taking 
					 *  OR
					 *  mg pid
					 *  where pid is a natural number
					 */
					
					if (input.length() < 1)
						System.out.println("not a valid argument");
					else{
						String[] tokens = input.split(" ");
						if (tokens[0].equalsIgnoreCase("migrate")  && isInteger(tokens[1])){
							String[] migrateArgs = new String[3];
							migrateArgs[0] = tokens[1];
							migrateArgs[1] = "localhost"; //@TODO eeeh might wanna change it
							migrateArgs[2] =   String.valueOf(server.PORT);
							
							try {
								manager.migrateProcess(migrateArgs);
							} catch (Exception e) {
								System.out.println("Migrate error: "+e);
							}
							
						}
						String className = tokens[0];
						if (tokens.length > 1){
								String[] argArray = Arrays.copyOfRange(tokens, 1, tokens.length);
							try{
								manager.addProcess(className, argArray);
							}
							catch(Exception e){
								System.out.println("Process "+ className 
										+ " not available with args given " + e);
							}
						}
						else{
							System.out.println("not valid input. Try again");
						}
						
							
							
					}
					
					break;				
			}

		}
	}
	
	
	public static void startServer(){
		server = new ProcessServer();
        Thread serverThread = new Thread(new ProcessServer());
        serverThread.start();
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	
	private static class Listener implements Runnable {
		Socket listener;
		public Listener(Socket someSocket){
			this.listener = someSocket;
		}
			
		public void run() {
			try {
				ObjectInputStream in = new ObjectInputStream(listener.getInputStream());
				DataOutputStream out = new DataOutputStream(listener.getOutputStream());

				Object object = in.readObject();

	            if(object instanceof MigratableProcess){
	            	MigratableProcess process = (MigratableProcess) object;
	            	process.afterMigrate();
	            	//confirm that process was received
	            	out.writeBoolean(true);
		            manager.startProcess(process);
	            }
	            else 
	            	out.writeBoolean(false);
	            
	            in.close();
	            out.close();
	            listener.close();
			}
			catch (Exception e) {
				System.out.println("Error in data sent: "+ e);
	        } 
		}
			

	}

	private static class ProcessServer implements Runnable{
	    /**
	     * Default port number of process server
	     */
	    public static final int PORT = 131;

	    /**
	     * Server socket of process server
	     */
	    private static ServerSocket serverSocket;

	    /**
	     * Running flag
	     */
	    //private boolean running;

	    /**
	     * Bind  the port, then loop to
	     * accept() migration request.
	     */
	    
	    public void run(){
	        // = true;
	        bind();
	        while(stillRunning){
	            accept();
	        }
	    }

	    /**
	     * Stop the process server.
	     */
	    public static void stop(){
	        if(stillRunning){
	            //stillRunning = false;
	            try {
	            	stillRunning = false;
	            	serverSocket.close();
					System.out.println("exiting...");
	            	System.exit(0);
	            } catch (Exception e) {
	                System.out.println("Error"+ e);
	                System.exit(-1);
	            }
	        }
	    }

	    /**
	     * Bind the port
	     * If bind failed, the program exit with -1.
	     */
	    private void bind(){
	        try {	        	
	            serverSocket = new ServerSocket(PORT);
	        } catch (Exception e) {
	            System.out.println
	            	("ServerSocket failed to bind. Restart program and try again");
	            System.out.println(e);
	            System.exit(-1);
	        }
	    }
	    
	    private void accept() {
	        Socket clientSocket = null;
	        if(stillRunning){
	        try {
	            clientSocket = serverSocket.accept();
	        }
	        catch (Exception e) {
	        	System.out.println("Accept error: "+ e);
	        	server.stop();
	            System.exit(-1);
	        }
	        new Thread(new Listener(clientSocket)).start();
	        }
	    }
	}
}
