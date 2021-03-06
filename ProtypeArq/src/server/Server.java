package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import Controls.FileMessage;
import Models.Client;

public class Server {
	private ServerSocket serverSocket;
	private Socket socket;
	private Map<String, ObjectOutputStream> steamMaps = new HashMap<String, ObjectOutputStream>() ;
	
	public Server() {
		try {
			serverSocket = new ServerSocket(5555);
			System.out.println("Server ONLINE port 5555");
			while (true) {
				socket = serverSocket.accept();
				new Thread( new ListenerSocked( socket ) ).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class ListenerSocked implements Runnable {
		
		private ObjectOutputStream outputStream;
		private ObjectInputStream inputStream;
		
		public ListenerSocked( Socket socket ) throws IOException { 
			this.inputStream  = new ObjectInputStream(  socket.getInputStream()  );
			this.outputStream = new ObjectOutputStream( socket.getOutputStream() );
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			FileMessage fileMessage = null;
			try {
				while ( ( fileMessage = (FileMessage) inputStream.readObject() ) != null ) {
					steamMaps.put( fileMessage.getClient(), outputStream );
					if (fileMessage.getFile() != null) {
						System.out.println("Cliente "+fileMessage.getClient()+" enviando mensagem");
						
						for (Map.Entry<String, ObjectOutputStream> m : steamMaps.entrySet() ) {
							if (!fileMessage.getClient().equals(m.getKey() ) ) {
								m.getValue().writeObject(fileMessage);
							}
						}
						
					}
				}
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Disconect, remove: "+ fileMessage.getClient() );
				steamMaps.remove(fileMessage.getClient());
			}
		}
		
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Helloy word");
		new Server();
	}
}
