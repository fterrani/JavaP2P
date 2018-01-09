
/**
Project name: DP_ProjectP2P 
File name: ShareServerSession.java
Author: Célia Ahmad
Date of creation: 5 janv. 2018
 */

package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class ShareClient {

	// port for connexion to server
	public static final int PORT_DEFAULT = 50000;
	private InetAddress serverIP ;
	private Socket clientSocket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private String[] lastResponse;
	private String givenIp;
	private ClientModel model;
	private File shareFolder;
	

	// constructeur sans argument qui crée un dossier au lancement
	public ShareClient(ClientModel model, InetAddress serverIP) {
		this.model = model;
		this.serverIP= serverIP;
		shareFolder= model.getShareFolder();
	}

	/* Connection and retrieving file list to/from server */
	public void connectToServer() throws IOException {

		System.out.println("Connecting to the server: " + serverIP.getHostName());
		clientSocket = new Socket(serverIP, PORT_DEFAULT);
		
		System.out.println("Connected on port: " + clientSocket.getPort());
		
		if (clientSocket.isConnected()) {
			createWriterAndReader();
			cmdRegister();
			cmdShareFiles();
			cmdGetfilelistFromServer();
			
			// We keep the socket open for following commands
			// System.out.println( "Closing client socket..." );
			// socket.close();
		}
	}

	private void createWriterAndReader() throws IOException {
		printWriter = new PrintWriter(
			new BufferedOutputStream(clientSocket.getOutputStream()), true
		);
		
		bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}

	private String[] splitMessage( String message )
	{
		String[] parts = new String[2];
		
		parts[0] = message.substring(0, 4);
		parts[1] = message.substring(5);
		
		return parts;
	}
	
	// getfilelist command
	public void cmdGetfilelistFromServer() throws IOException {

		printWriter.println("getFileList");
		printWriter.flush();

		// Each client ID is bound to a list of strings
		HashMap<Integer, ArrayList<PeerFile>> newList = new HashMap<Integer, ArrayList<PeerFile>>();
		
		lastResponse = readMessage();

		System.out.println(lastResponse[0] + " >>> '" + lastResponse[1] + "'");

		if (!lastResponse[1].equals(""))
		{
			int id;
			String[] strFiles = lastResponse[1].split(";");
			
			for (int i = 0; i < strFiles.length; i++) {
				String[] parts = strFiles[i].split(":");
				
				if ( parts.length == 2 )
				{
					try
					{
						// We get the client ID
						id = Integer.parseInt( parts[0] );
						
						// We create a filelist for client ID, if it doesn't exist
						if ( !newList.containsKey(id) )
							newList.put( id, new ArrayList<PeerFile>() );
						
						// We add the file to the client's list
						newList.get( id ).add( new PeerFile(id, parts[1]) );
					}
					catch(NumberFormatException nfe)
					{
						System.out.println( "Invalid ID received from server!" );
					}
				}
			}
		}
		
		int peerCount = newList.size();
		int[] clientIds = new int[ peerCount ];
		PeerFile[][] fileNames = new PeerFile[ peerCount ][0];
		int i = 0;
		
		for( Map.Entry<Integer, ArrayList<PeerFile>> peer : newList.entrySet() )
		{
			clientIds[i] = peer.getKey();
			fileNames[i] = peer.getValue().toArray( new PeerFile[0] );
			i++;
		}
		
		model.setListFileFromServer( clientIds, fileNames );
	}

	private String[] readMessage() throws IOException {
		return splitMessage(bufferedReader.readLine());
	}
	
	// sharelists command
	public void cmdShareFiles() throws IOException {
		String listFileToShare = "";

		File[] contenuDossier = shareFolder.listFiles();

		for (int j = 0; j < contenuDossier.length; j++)
		{
			listFileToShare += (j>0?" ":"") + contenuDossier[j].getName();
		}

		printWriter.println("shareList" + " " + listFileToShare);
		printWriter.flush();
		
		lastResponse = readMessage();
	}

	private void cmdRegister() throws IOException {

		printWriter.println("register" + " " + clientSocket.getInetAddress().getHostAddress());
		printWriter.flush();

		lastResponse = readMessage();

		int givenID = Integer.parseInt(lastResponse[1]);
		System.out.println(givenID);
		model.setClientID((int) givenID);
	}

	public String cmdGetIP(int id) throws IOException {
		printWriter.println("getIp" + " " + id);
		printWriter.flush();

		lastResponse = readMessage();

		givenIp = lastResponse[1];
		
		return givenIp;
	}

	// getter and setter

	public String getServerIP() {
		// TODO Auto-generated method stub
		return serverIP.getHostAddress();
	}


	public String[] getContenuDossier() {
		return shareFolder.list();
	}

	public ClientModel getModel() {
		return model;
	}

	public void setModel(ClientModel model) {
		this.model = model;
	}

}
