
/**
Project name: DP_ProjectP2P 
File name: Client.java
Author: Célia Ahmad
Date of creation: 14 déc. 2017
 */
package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	
	// port for connexion to server
	private int port1 = 50000 ;
	// port for incoming connexions
	private int port2 = 60000 ;
	
	private String serverIp = "127.0.0.1";
	private Socket clientSocket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private MainFrame frame;
	
	private int clientID =1;
	private String [] listFileFromServer ;
	private String response;
	private File shareFolder;
	private File[] contenuDossier;
	private int givenID;
	private String givenIp;
	
	// constructeur sans argument qui crée un dossier au lancement
	public Client() {
		initFolder();
		
	}

	// constructeur avec argument qui récupère le dossier déjà existant
	public Client(File file) {
		if (file.exists())
			shareFolder = file;
		else
			initFolder();
		
	}

	public static void main(String[] args) {

		Client client1 = new Client();
		//client1.connectToServer();
		
		//get the available file list from the server
		
	}
	
	
	private void initFolder() {
		shareFolder = new File("./shareFolders/client_" + System.currentTimeMillis());
		if (!shareFolder.exists()) {
			shareFolder.mkdirs();
		}
		File test = new File(shareFolder, "test.txt");
		try {
			test.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(shareFolder.getName());
		System.out.println(shareFolder.listFiles().length);
		
		contenuDossier = shareFolder.listFiles();

	}
	
	private void createWriterAndReader() {
		try {
			printWriter = new PrintWriter(
					new BufferedOutputStream(clientSocket.getOutputStream()), true);
			bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/*Connexion and retrieving file list to/from server*/
	public void connectToServer() {

		try {
			System.out.println("Connecting to the server: " + serverIp);
			InetAddress serverAdress = InetAddress.getByName(serverIp);
			clientSocket = new Socket(serverAdress, port1);
		} catch (IOException e) {
			System.out.println("Connection impossible, check the server IP or server status");
		}
		System.out.println("Connected on port: " + clientSocket.getPort());

		if (clientSocket.isConnected()) {
			createWriterAndReader();
			sendIP();
			getfilelistFromServer();
			shareFiles();
			givenIp = getIp();
			connectToClient(givenIp, port2);
			
			
			
			//close connection to server		
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	private void getfilelistFromServer() {
		
		printWriter.println("getFileList");
		printWriter.flush();
		
		try {
			response = readMessage();
			String [] rawlist= response.split(" ");
			String[] strFiles = rawlist[1].split(";");
		//	listFileFromServer= new String[strFiles.length];
		
			for (int i = 0; i < strFiles.length; i++)
			{
				String[] parts = strFiles[i].split(":");
				String clientId = parts[0];
				String fileName = parts[1];
				listFileFromServer[i] = parts[0]+ "\t " + parts[1];
				
			}		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void sendIP() {
		try {
		
			printWriter.println("register" + " " + clientSocket.getInetAddress());
			printWriter.flush();
			givenID= Integer.parseInt(readMessage());  
			System.out.println(givenID);
			setClientID((int)givenID);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getIP(int id) {
		try {
			printWriter.println("getIp" + " " + 5);
			printWriter.flush();
			return givenIp= readMessage();  
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	private String readMessage() throws IOException{
	       return bufferedReader.readLine();             
	   }
	
	
	public void shareFiles() {
	 String listFileToShare = "";
		
		for (int j = 0; j < contenuDossier.length; j++) {
			 listFileToShare += contenuDossier[j];
		}
		
		printWriter.println("shareFile" + " " + listFileToShare);
		printWriter.flush();
		
	}
	
	
	 // Sending a file to a client
	
	public Socket acceptClientConnection(ServerSocket listeningSocket) throws IOException
	{
		Socket clientSendingSocket = listeningSocket.accept() ;
		return clientSendingSocket ;
	}
	
	
	// Recieving a file from a client 
	
	public Socket connectToClient(String clientName, int port) throws IOException
	{
		InetAddress clientAddress = InetAddress.getByName(clientName) ;
		Socket clientDownloadingSocket = new Socket(clientAddress, port) ;
		return clientDownloadingSocket ;
	}
	
	
	//getter and setter 

	public int getClientID() {
		// TODO Auto-generated method stub
		return clientID;
	}

	public String getServerIP() {
		// TODO Auto-generated method stub
		return serverIp;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	
	
	public String[] getContenuDossier() {
		return shareFolder.list();
	}
	
	public String[] getDisplayedList() {
		listFileFromServer= new String [20];
		listFileFromServer =  new String[] {"1 - test.txt","2 - para.txt", "3 - progdistribuee.txt" };
		return listFileFromServer;
	}

	//

}
