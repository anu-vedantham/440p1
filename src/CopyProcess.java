import java.io.File;
import java.io.PrintStream;
import java.io.EOFException;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.Thread;
import java.lang.InterruptedException;

public class CopyProcess implements MigratableProcess
{
	private TransactionalFileInputStream  inFile;
	private TransactionalFileOutputStream outFile;
	private long id;

	private volatile boolean suspending;

	public CopyProcess(String args[]) throws Exception
	{
		if (args.length != 2) {
			System.out.println("usage: CopyProcess <inputFile> <outputFile>");
			throw new Exception("Invalid Arguments");
		}
		inFile = new TransactionalFileInputStream(new File(args[0]));
		outFile = new TransactionalFileOutputStream(new File(args[1]));
	}

	public void run()
	{
		PrintStream out = new PrintStream(outFile);
		DataInputStream in = new DataInputStream(inFile);

		try {
			while (!suspending) {
				String line = in.readLine();

				if (line == null) break;
			    out.println(line);
				
				// Make copy take longer so that we don't require extremely large files for interesting results
				/*
			    try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// ignore it
				}*/
			}
		} catch (EOFException e) {
			//End of File
		} catch (IOException e) {
			System.out.println ("CopyProcess: Error: " + e);
		}


		suspending = false;
	}

	public void suspend()
	{
		suspending = true;
		while (suspending);
	}

	@Override
	public void unsuspend() {
		suspending = false;
		
	}

	@Override
	public void afterMigrate() {
		if (this.inFile != null)
			this.inFile.afterMigrating();
		if (this.outFile != null)
			this.outFile.afterMigrating();
		this.run();
		
	}

	@Override
	public void setID(long newID) {
		this.id = newID;
		
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setInputStream(TransactionalFileInputStream inFile) {
		this.inFile = inFile;
		
	}

	@Override
	public TransactionalFileInputStream getInputStream() {
		return this.inFile;
	}

	@Override
	public void setOutputStream(TransactionalFileOutputStream outFile) {
		this.outFile = outFile;
		
	}

	@Override
	public TransactionalFileOutputStream getOutputStream() {
		return this.outFile;
	}

}