package aufgabe2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class UDPWorkerThread extends Thread{
	  private int SERVER_PORT;
	  final static int BUFFER_SIZE = 1024;
	  private DatagramSocket serverSocket; // UDP-Socketklasse
	  private InetAddress receivedIPAddress; // IP-Adresse des Clients
	  private int receivedPort; // Port auf dem Client
	  private boolean serviceRequested = true; // Anzeige, ob der Server-Dienst weiterhin benoetigt wird 
	  
	  private Server server;
	  
	  public UDPWorkerThread(Server server, int port) {
		  /* Konstruktor */
		  this.server = server;
		  this.SERVER_PORT = port;
	  }
	  
	  public void run() {
	    String messageFromClients;

	    try {
	      /* UDP-Socket erzeugen (kein Verbindungsaufbau!)
	       * Socket wird an den ServerPort gebunden */
	      serverSocket = new DatagramSocket(SERVER_PORT);
	      System.out.println("UDP Server: Waiting for connection - listening UDP port " +
	                         SERVER_PORT);

	      while (serviceRequested) {

	        /* String vom Client empfangen und in Gro�buchstaben umwandeln */
	        messageFromClients = readFromClient();

	        /* Modifizierten String an Client senden */
	        server.broadcastMessage(messageFromClients);
			  	
	      }

	      /* Socket schlie�en (freigeben) */
	      serverSocket.close();
	      System.out.println("Server shut down!");
	    } catch (SocketException e) {
	      System.err.println("Connection aborted by client!");
	    } catch (IOException e) {
	      System.err.println("Connection aborted by client!");
	    }

	    System.out.println("UDP Server stopped!");
	  }

	  private String readFromClient() throws IOException {
	    /* Liefere den n�chsten String vom Server */
	    String receiveString = "";

	    /* Paket f�r den Empfang erzeugen */
	    byte[] receiveData = new byte[BUFFER_SIZE];
	    DatagramPacket receivePacket = new DatagramPacket(receiveData,
	                                                      BUFFER_SIZE);

	    /* Warte auf Empfang eines Pakets auf dem eigenen Server-Port */
	    serverSocket.receive(receivePacket);

	    /* Paket erhalten --> auspacken und analysieren */
	    receiveString = new String(receivePacket.getData(), 0,
	                               receivePacket.getLength());

	    System.out.println("UDP Server got from Client: " + receiveString);

	    return receiveString;
	  }
}
