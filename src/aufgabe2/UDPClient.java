package aufgabe2;

import java.net.Socket;
import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread {
	private Controller controller;
	private final int serverPort = 56790;
	private final String hostname;

	private DatagramSocket clientSocket;

	private InetAddress serverAdress;

	public UDPClient(String hostname, Controller controller) {
		this.hostname = hostname;
		this.controller = controller;
	}
	
	public void run() {
		try {
			System.out.println("UDP Server Thread started. Try to connect...");
			clientSocket = new DatagramSocket();
			/* Wait for interruption due to logging out the user */
			while(!isInterrupted()) {
				
			}
			clientSocket.close();
		}catch(IOException e) {
			System.err.println("Connection aborted by server!");
		}
	}

	
	public void writeToServer(String message) throws IOException {
		byte[] sendData = message.getBytes();
		serverAdress = InetAddress.getByName(hostname);
		DatagramPacket sendPacket =  new DatagramPacket(sendData, sendData.length,
                serverAdress, serverPort);
		
		clientSocket.send(sendPacket);
		
	}	
}
