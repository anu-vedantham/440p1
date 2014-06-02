import java.io.Serializable;


public abstract class MigratableProcess implements Runnable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private double id;
	private volatile boolean suspending;
	private TransactionalFileInputStream  inFile;
	private TransactionalFileOutputStream outFile;
	
	public MigratableProcess(){
		//this.id = generate ID
		this.suspending = false;
	}
	
	public abstract void run();
	
	public void suspend(){
		suspending = true;
		while (suspending);
	};
	
	public void unsuspend(){
		suspending = false;
	};
	
	public abstract void afterMigrate();
	
	public double getID(){
		return id;
	};

}
