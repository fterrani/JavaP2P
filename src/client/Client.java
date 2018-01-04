
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
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class Client extends Observable
{
	// port for connexion to server
	private int port1 = 50000 ;
	// port for incoming connexions
	private int port2 = 60000 ;
	
	private String serverIp = "127.0.0.1";
	private Socket clientSocket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	
	private int clientID =1;
	private String[] listFileFromServer = new String[0];
	private String[] response;
	private File shareFolder;
	//private File[] contenuDossier;
	private String givenIp;
	
	// constructeur sans argument qui crée un dossier au lancement
	public Client() {
		
		initFolder();
	}
	
	public void addObserver( Observer o )
	{
		super.addObserver(o);
		
		// We only warn the observer that has just been added
		o.update(this, null);
	}
	
	// Marks the model as changed and notifies all observers
	private void changeAndNotifyObservers()
	{
		setChanged();
		notifyObservers();
	}

	// constructeur avec argument qui récupère le dossier déjà existant
	public Client(File file) {
		if (file.exists())
			shareFolder = file;
		else
			initFolder();
		
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
		
		changeAndNotifyObservers();
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
			shareFiles();
			getfilelistFromServer();
			//givenIp = getIp();
			//connectToClient(givenIp, port2);
			
			
			
			//close connection to server		
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private String[] splitMessage( String message )
	{
		String[] parts = new String[2];
		
		parts[0] = message.substring(0, 4);
		parts[1] = message.substring(5);
		
		return parts;
	}
	
	private void getfilelistFromServer() {
		
		printWriter.println("getFileList");
		printWriter.flush();
		
		try {
			response = readMessage();
			
			System.out.println( response[0] + " >>> '" + response[1] + "'");
			
			if ( response[1].equals("") )
			{
				listFileFromServer = new String[0];
			}
			
			else
			{
				String[] strFiles = response[1].split(";");
				listFileFromServer = new String[strFiles.length];
			
				for (int i = 0; i < strFiles.length; i++)
				{
					String[] parts = strFiles[i].split(":");
					listFileFromServer[i] = parts[0]+ "\t" + parts[1];
				}
			}
			
			changeAndNotifyObservers();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void sendIP() {
		try {
		
			printWriter.println("register" + " " + clientSocket.getInetAddress().getHostAddress() );
			printWriter.flush();
			
			response = readMessage();
			
			int givenID = Integer.parseInt( response[1] );
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
			
			response = readMessage();
			
			givenIp = response[1];
			
			changeAndNotifyObservers();
			return givenIp;
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	private String[] readMessage() throws IOException{
	       return splitMessage( bufferedReader.readLine() );             
	   }
	
	
	public void shareFiles() {
		String listFileToShare = "";
		
		File[] contenuDossier = shareFolder.listFiles();
	 
		for (int j = 0; j < contenuDossier.length; j++) {
			 listFileToShare += contenuDossier[j];
		}
		
		printWriter.println("shareList" + " " + listFileToShare);
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

	public void setClientID(int clientID)
	{
		this.clientID = clientID;
		changeAndNotifyObservers();
	}
	
	
	public String[] getContenuDossier() {
		return shareFolder.list();
	}
	
	public String[] getDisplayedList() {
		//listFileFromServer= new String [20];
		//listFileFromServer =  new String[] {"1 - test.txt","2 - para.txt", "3 - progdistribuee.txt" };
		return listFileFromServer;
	}

	//

}
