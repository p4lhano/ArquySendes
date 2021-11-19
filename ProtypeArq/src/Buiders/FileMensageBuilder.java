package Buiders;

import java.io.File;

import Controls.FileMessage;

public class FileMensageBuilder implements Builder {
	private String client;
	private File file;
	
	@Override
	public FileMensageBuilder setClient(String client) {
		// TODO Auto-generated method stub
		this.client = client;
		return this;
	}

	@Override
	public FileMensageBuilder setFile(File file) {
		// TODO Auto-generated method stub
		this.file = file;
		return this;
	}
	
	public FileMessage getResult() {
		return new FileMessage(client,file);
	} 

}
