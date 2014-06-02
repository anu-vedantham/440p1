import java.io.*;

public class TransactionalFileOutputStream extends OutputStream implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private File outputFile;
	private long index;
	private RandomAccessFile writer;
	
	public TransactionalFileOutputStream (File outputFile){
		this.outputFile = outputFile;
		this.index = 0;
	}
	
	public void write(int next) throws IOException{
		if (writer == null){
			writer = new RandomAccessFile(outputFile,"rw");
			writer.seek(index);
		}
		index++;
		writer.write(next);
	}
	
	public void close() throws IOException {
		writer.close();
	}
	
	public void afterMigrating(){
		this.writer = null;
	}

}
