package aufgabe2;

import java.awt.List;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class TCPWorkerThread extends Thread{
	   private int name;
	   private Socket socket;
	   private Server server;
	   private BufferedReader inFromClient;
	   private DataOutputStream outToClient;
	   boolean workerServiceRequested = true; // Arbeitsthread beenden?
	   
	   public TCPWorkerThread(int num, Socket sock, Server server) {
	      /* Konstruktor */
	      this.name = num;
	      this.socket = sock;
	      this.server = server;
	   }

	   public void run() {
	      String capitalizedSentence;

	      System.out.println("TCP Worker Thread " + name +
	            " is running until QUIT is received!");

	      try {
	         /* Socket-Basisstreams durch spezielle Streams filtern */
	         inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	         outToClient = new DataOutputStream(socket.getOutputStream());
	         while (workerServiceRequested) {
	            /* String vom Client empfangen und in Grossbuchstaben umwandeln */
	            capitalizedSentence = readFromClient().toUpperCase();
	            
	            
	            /* Test, ob Arbeitsthread beendet werden soll */
	            if (capitalizedSentence.startsWith("LOGOUT=")) {
	            	String[] sub = capitalizedSentence.split("=");
	            	server.broadcastMessage("MESSAGE=" + sub[1] + " logged out!");
	            	workerServiceRequested = false;
	            }
	            else if(capitalizedSentence.startsWith("CONNECTIONREQUEST=")) {
	            	String[] sub = capitalizedSentence.split("=");
	            	server.broadcastMessage("MESSAGE=User " + sub[1] + " logged in.");
	            }
	         }

	         /* Socket-Streams schliessen --> Verbindungsabbau */
	         socket.close();
	      } catch (IOException e) {
	         System.err.println("Connection aborted by client!");
	      } finally {
	         System.out.println("TCP Worker Thread " + name + " stopped!");
	         /* Platz fuer neuen Thread freigeben */
				server.workerThreadsSem.release();         
	      }
	   }

	   private String readFromClient() throws IOException {
	      String request = inFromClient.readLine();
	      System.out.println("TCP Worker Thread " + name + " detected job: " + request);

	      return request;
	   }

	   public void writeToClient(String reply) throws IOException {
	      /* Sende den String als Antwortzeile (mit CRLF) zum Client */
	      outToClient.writeBytes(reply + '\r' + '\n');
	      System.out.println("TCP Worker Thread " + name +
	            " has written the message: " + reply);
	   }
}
