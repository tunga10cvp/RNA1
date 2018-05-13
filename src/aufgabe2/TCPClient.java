package aufgabe2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient extends Thread {
	private Controller controller;
	
	private final int serverPort = 56789;
	private final String hostname;
	private final String username;
	private Socket clientSocket;
	
	private DataOutputStream outToServer;
	private BufferedReader inFromServer;
	
	private boolean serviceRequested = true;
	
	private String CONNECTIONREQUEST;
	private String serverResponse;
	
	
	public TCPClient(String hostname, String username, Controller controller) {
		this.hostname = hostname;
		this.username = username;
		this.controller = controller;
		this.CONNECTIONREQUEST = "CONNECTIONREQUEST=" + username;
	}
	
	public void run() {
		System.out.println("TCP Worker Thread started. Try to connect to server ... ");

		try {
			clientSocket = new Socket(hostname, serverPort);
			
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			
			/*Send connection request to Chatroom*/
			writeToServer(CONNECTIONREQUEST);
			
			while(serviceRequested && !isInterrupted()) {	
				/*Read answer from Server*/				
				serverResponse = readFromServer();
				System.out.println(serverResponse);
				/* Process server response */
				if(serverResponse.startsWith("DISCONNECTED")) {
					serviceRequested = false;
					controller.disconnectByServer();
				}
				else if(serverResponse.startsWith("MESSAGE=")) {
					controller.receivedMessage(serverResponse.split("=")[1]);
				}
			}
			clientSocket.close();
			
		}catch(IOException e) {
			System.err.println("Connection closed by server!");
		}
	}
	
    public synchronized void  writeToServer(String request) throws IOException {
        outToServer.writeBytes(request + '\r' + '\n');
        System.out.println("TCP Client has sent the message: " + request);
    }
    
    private String readFromServer() throws IOException {
        String reply = inFromServer.readLine();
        System.out.println("TCP Client got from Server: " + reply);
        return reply;
    }
	
}
