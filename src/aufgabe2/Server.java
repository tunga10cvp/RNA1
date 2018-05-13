package aufgabe2;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;


public class Server {
   /* TCP-Server, der Verbindungsanfragen entgegennimmt */
    private UDPWorkerThread udpSocket;
    
   /* Semaphore begrenzt die Anzahl parallel laufender Worker-Threads  */
   public Semaphore workerThreadsSem;

   /* Portnummer */
   public final int tcpPort;
   public final int udpPort = 56790;
   
   final static int BUFFER_SIZE = 1024;
   
   /* Anzeige, ob der Server-Dienst weiterhin benoetigt wird */
   public boolean serviceRequested = true;
   
   private ArrayList<TCPWorkerThread> clientList = new ArrayList<TCPWorkerThread>();
   
   /* Konstruktor mit Parametern: Server-Port, Maximale Anzahl paralleler Worker-Threads*/
   public Server(int serverPort, int maxThreads) {
      this.tcpPort = serverPort;
      this.workerThreadsSem = new Semaphore(maxThreads);
   }

   public void startServer() {
      ServerSocket welcomeSocket; // TCP-Server-Socketklasse
      Socket connectionSocket; // TCP-Standard-Socketklasse
      
      int nextThreadNumber = 0;
      udpSocket = new UDPWorkerThread(this, udpPort);
      udpSocket.start();
      
      try {
         /* Server-Socket erzeugen */
         welcomeSocket = new ServerSocket(tcpPort);

         while (serviceRequested) { 
				workerThreadsSem.acquire();  // Blockieren, wenn max. Anzahl Worker-Threads erreicht
				
            System.out.println("TCP Server is waiting for connection - listening TCP port " + tcpPort);
            /*
             * Blockiert auf Verbindungsanfrage warten --> nach Verbindungsaufbau
             * Standard-Socket erzeugen und an connectionSocket zuweisen
             */
            connectionSocket = welcomeSocket.accept();

            /* Neuen Arbeits-Thread erzeugen und die Nummer, den Socket sowie das Serverobjekt uebergeben */
            TCPWorkerThread newClient = new TCPWorkerThread(++nextThreadNumber, connectionSocket, this);
            newClient.start();
            
            clientList.add(newClient);
          }
      } catch (Exception e) {
         System.err.println(e.toString());
      }
   }
   /* Send message to every Client in Chatroom */
   public void broadcastMessage(String message) {
	   for (TCPWorkerThread client : clientList) {
		   try {
			   client.writeToClient(message);   
		   }catch(IOException e) {
			   System.err.println(e);
		   }
		   
	   }
   }
   public static void main(String[] args) {
      /* Create server and start */
      Server myServer = new Server(56789, 10);
      myServer.startServer();
   }
}
