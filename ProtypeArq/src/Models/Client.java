package Models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.util.Scanner;

import javax.swing.JFileChooser;

import Buiders.FileMensageBuilder;
import Controls.FileMessage;



public class Client {
	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	private String nameClient;
	
	public Client() throws UnknownHostException, IOException {
		this.socket = new Socket("localhost", 5555);
		this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		
		new Thread(new ListenerSocket(socket)).start();
		
		menu();
	}
	
	public void menu() throws IOException {
		Scanner sc = new java.util.Scanner(System.in);
		System.out.println("your name: ");
		String name = sc.nextLine();
		nameClient = name;
		 sc.nextLine();
		
		 FileMensageBuilder fileMensageBuilder = new FileMensageBuilder();
			
		 
		//this.objectOutputStream.writeObject(new FileMessage(name));
		this.objectOutputStream.writeObject(fileMensageBuilder.setClient(name).getResult());
		
		int op = 0;
		
		while ( op != 1) {
			System.out.println("1 - Finish Program\n2 - Send");
			op = sc.nextInt();
			if        ( op == 2 ) {
				send(name);
			} else if ( op == 1 ) {
				System.exit(0);
			}
		}
	}
	
	private void send(String name) throws IOException {
		// TODO Auto-generated method stub
		JFileChooser fileChooser = new JFileChooser();
		int op = fileChooser.showOpenDialog(null);
		if (op == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();
			
			FileMensageBuilder fileMensageBuilder = new FileMensageBuilder();
			
			
			//this.objectOutputStream.writeObject(new FileMessage(name, file));
			this.objectOutputStream.writeObject(fileMensageBuilder.
					setClient(name).
					setFile(file).
					getResult()
					);
		}
	}

	private class ListenerSocket implements Runnable {
			
		private ObjectInputStream inputStream;
		
		public ListenerSocket( Socket socket ) throws IOException {
			this.inputStream = new ObjectInputStream(socket.getInputStream());
		}
		
		
		
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			FileMessage fileMessage = null;
			try {
				while ( ( fileMessage = (FileMessage) inputStream.readObject() ) != null ) {
					System.out.println("New file from: "+ fileMessage.getClient());
					System.out.println("Name file : "+ fileMessage.getFile().getName());
					//printer(fileMessage);
					
					saveFile(fileMessage);
					
					System.out.println("1 - Finish Program\n2 - Send");
				}
			} catch (Exception e) {
					// TODO: handle exception
			}
		}




		private void saveFile(FileMessage fileMessage) throws IOException {
			// TODO Auto-generated method stub
			try {
				FileInputStream fileInputStream = new FileInputStream(fileMessage.getFile());
				FileOutputStream fileOutputStream = new FileOutputStream("c:\\fTester\\"+ nameClient+ "_for_"+ fileMessage.getClient()+ "_" + fileMessage.getFile().getName());
				FileChannel finput = fileInputStream.getChannel();
				FileChannel fout   = fileOutputStream.getChannel();
				long size = finput.size();
				finput.transferTo(0, size, fout);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}




		private void printer(FileMessage fileMessage) throws IOException {
			// TODO Auto-generated method stub
			FileReader fileReader = new FileReader(fileMessage.getFile());
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String lane ;
			System.out.println("Content:");
				while( (lane = bufferedReader.readLine()) != null ) {
					System.out.println(lane);
				}
		}
		
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Helloy word");
		new Client();
	}
}
