import java.io.*;

public class TransactionalFileInputStream extends InputStream implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private File inputFile;
	private long index;
	private RandomAccessFile reader;
	
	public TransactionalFileInputStream (File inputFile){
		this.inputFile = inputFile;
		this.index = 0;
	}
	
	public int read() throws IOException{
		if (reader == null){
			reader = new RandomAccessFile(inputFile,"r");
			reader.seek(index);
		}
		index++;
		return reader.read();
	}
	
	public void close() throws IOException {
		reader.close();
	}

}
