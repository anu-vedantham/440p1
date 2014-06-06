import java.io.Serializable;


public interface MigratableProcess extends Runnable, Serializable{

	//private static final long serialVersionUID = 1L;

	/*
	private long id;
	private volatile boolean suspending;
	private TransactionalFileInputStream  inFile;
	private TransactionalFileOutputStream outFile;
	*/

	/*public MigratableProcess(){
		//this.id = generate ID
		//this.suspending = false;
	}*/
	void run();
	
	public void suspend();
		/*
		suspending = true;
		while (suspending);
		*/


	public void unsuspend();
		//suspending = false;


	public void afterMigrate();
	/*	
	  	if (this.inFile != null)
			this.inFile.afterMigrating();
		if (this.outFile != null)
			this.outFile.afterMigrating();
		this.run();
	*/
	
	public boolean isProcessDone();

	public void setID(long newID);

	public abstract long getID();

	public abstract String toString();

	public abstract void setInputStream(TransactionalFileInputStream inFile);

	public abstract TransactionalFileInputStream getInputStream(); 

	public abstract void setOutputStream(TransactionalFileOutputStream outFile);

	public abstract TransactionalFileOutputStream getOutputStream();
}