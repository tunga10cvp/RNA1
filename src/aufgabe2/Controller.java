package aufgabe2;

import java.beans.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

public class Controller {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rset = null;
	
	private String output;
	private String hostname;
	private String user;
	
	private TCPClient clientTCPThread;
	private UDPClient clientUDPThread;
	
	public OutputPanel outputP;
	public UserPanel userP;
	
	public Controller(){
		
	}
	/* Connect to Server */
	public void connectToServer(String login, String server) {
		this.user = login;
		this.hostname = server;
		
		clientTCPThread = new TCPClient(hostname, user, this);
		clientTCPThread.start();
		
		clientUDPThread = new UDPClient(hostname, this);
		clientUDPThread.start();
	
	}
	/* Process server shutdown */
	public void disconnectByServer() {
		outputP.setOutput("Disconnected by server!");
		clientTCPThread.interrupt();
	}
	/* Disconnect user */
	public void disconnectByUser() {
		try {
			clientTCPThread.writeToServer("LOGOUT=" + user);
			clientTCPThread.interrupt();
			clientUDPThread.interrupt();
		}catch(IOException e) {
			System.err.println(e);
		}
	}
	/* Send message to user */
	public void sendMessage(String message) {
		try {
			String m = "MESSAGE=" + user.toUpperCase() + "> " + message;
			clientUDPThread.writeToServer(m);
		}catch(IOException e) {
			System.err.println(e);
		}
		
		
	}
	/* Process Message */
	public void receivedMessage(String message) {
		outputP.setOutput(message);
	}
}
