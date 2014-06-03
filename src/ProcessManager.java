
import java.util.*;
import java.io.*;
import java.util.concurrent.atomic.*;
import java.net.Socket;

public class ProcessManager {
	
	private AtomicLong currentpid;
	
	private ProcessManager(){
		currentpid = new AtomicLong(0);
	}
	
	private long nextPid(){
		return currentpid.getAndIncrement();
	}
	
	public static void Main(){
		
	}
}
