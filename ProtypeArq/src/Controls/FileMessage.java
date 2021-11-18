package Controls;

import java.io.File;
import java.io.Serializable;

public class FileMessage implements Serializable {
	private String client;
	private File file;
	
	
	public FileMessage(String client, File file) {
		super();
		this.client = client;
		this.file = file;
	}
	public FileMessage(String client) {
		super();
		this.client = client;
	}
	public FileMessage() {
		super();
		
	}


	public String getClient() {
		return client;
	}


	public void setClient(String client) {
		this.client = client;
	}


	public File getFile() {
		return file;
	}


	public void setFile(File file) {
		this.file = file;
	}
	
	
	
}
