import java.io.Serializable;


public abstract class MigratableProcess implements Runnable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private long id;
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

	public void afterMigrate(){
		if (this.inFile != null)
			this.inFile.afterMigrating();
		if (this.outFile != null)
			this.outFile.afterMigrating();
		this.run();
	};
	
	public long getID(){
		return id;
	};
	
	public void setID(long newID){
		id = newID;
	}
	
	public abstract String toString();
	
	public abstract void setInputStream(TransactionalFileInputStream inFile);
	
	public abstract void setOutputStream(TransactionalFileOutputStream outFile);
}
