
//import java.util.*;
//import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class ProcessManager {
	
	private AtomicLong currentpid;
	private LinkedBlockingQueue<MigratableProcess> processes;
	
	protected ProcessManager(){
		currentpid = new AtomicLong(0);
	}
	
	private long nextPid(){
		return currentpid.getAndIncrement();
	}
	
	private MigratableProcess processByPid(long pid){
		Iterator<MigratableProcess> processIter = processes.iterator();
		while(processIter.hasNext()){
			MigratableProcess process = processIter.next();
			if (process.getID() == pid)
				return process;
		}
		return null;
	}
	
	public void addProcess(String className, String[] args) throws Exception {
		Class<?> obj = Class.forName(className);
		Constructor contr = obj.getConstructor(Arrays.class);
		MigratableProcess newProcess = (MigratableProcess) contr.newInstance(args);
		newProcess.setID(currentpid.get());
		nextPid();
		startProcess(newProcess);
		processes.add(newProcess);
	}

	public void startProcess(MigratableProcess newProcess){
		Thread process = new Thread(newProcess);
		process.start();
	}

	public MigratableProcess getProcess(long pid){
		for (MigratableProcess process: processes){
			if(process.getID() == pid)
				return process;
		}
		
		return null;
	}
	
	public String listProcesses(){
		if (processes.isEmpty())
			return "No Processes currently running\n";
		String returnedString = "";
		for(MigratableProcess process: processes){
			returnedString+= process.getID()+": "+process.toString()+"\n";
		}
		return returnedString;
					
	}
	
	public void migrateProcess(String[] args) throws Exception {
		long pid = Long.parseLong(args[0]);
		
		String host = args[1];
		/**
		 * @TODO gotta change wherever migrateProcess is called
		 */
		int port = Integer.parseInt(args[2]);
		MigratableProcess process = processByPid(pid);
		
		Socket socket = null;
		socket = new Socket(host, port);
		process.suspend();
		
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		out.writeObject(process);
		
		if (in.readBoolean()){
			System.out.println("Successful migration");
			process.unsuspend();
			process.afterMigrate();
		}
		else{
			System.out.println("Failed migration");
			//process.afterMigrate();
		}
			
		
		in.close();
		out.close();
		socket.close();
	}
}
