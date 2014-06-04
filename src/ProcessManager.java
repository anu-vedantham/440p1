
//import java.util.*;
//import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Arrays;
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
		foreach(MigratableProcess process in processes){
			//HOW TO ITERATE THROUGH QUEUE
		}
	}
	
	public void addProcess(String className, String[] args) throws Exception {
		Class<?> obj = Class.forName(className);
		Constructor contr = obj.getConstructor(Arrays.class);
		MigratableProcess newProcess = (MigratableProcess) contr.newInstance(args);
		newProcess.setID(currentpid.get());
		nextPid();
		Thread process = new Thread(newProcess);
		process.start();
		processes.add(newProcess);
	}
	
	public void migrateProcess(String[] args){
		long pid = Long.parseLong(args[1]);
		String host = args[2];
		MigratableProcess process = processByPid(pid);
		
		Socket socket = null;
		socket = new Socket(host, PORT);
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
