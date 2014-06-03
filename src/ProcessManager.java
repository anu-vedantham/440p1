
//import java.util.*;
//import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
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
	
	public void addProcess(String className, String[] args) throws Exception {
		Class<?> obj = Class.forName(className);
		Constructor contr = obj.getConstructor(Arrays.class);
		MigratableProcess newProcess = (MigratableProcess) contr.newInstance(args);
		newProcess.setID(currentpid.get());
		nextPid();
		processes.add(newProcess);
	}
}
