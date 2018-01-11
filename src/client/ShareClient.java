
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

// Client to contact the server with the filelist
public class ShareClient {

	// port for connexion to server
	public static final int PORT_DEFAULT = 50000;
	private InetAddress serverIP;
	private Socket clientSocket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private String[] lastResponse; // contains the last response received from the server
	private ClientModel model;
	private File shareFolder;
	

	public ShareClient(ClientModel model, InetAddress serverIP) {
		this.model = model;
		this.serverIP= serverIP;
		shareFolder= model.getShareFolder();
	}

	// Connection and retrieving file list to/from server
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
	
	// All messages start with:
	// - 4 characters specifying the message's nature (INFO,EROR,DATA)
	// - a space character
	// - other textual data (but without any newline)
	private String[] splitMessage( String message )
	{
		String[] parts = new String[2];
		
		// We store INFO, EROR or DATA in [0]
		parts[0] = message.substring(0, 4);
		
		// We store the remaining chars in [1]
		parts[1] = message.substring(5);
		
		return parts;
	}
	
	// getfilelist command (used to get the list of files shared by all peers)
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
			
			// Each entry separated by ; represents a file
			String[] strFiles = lastResponse[1].split(";");
			
			for (int i = 0; i < strFiles.length; i++) {
				
				// An entry consists in "<client_id>:<file_name>"
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
		
		// We generate an int array for client IDs, and a PeerFile[][] array for the files
		for( Map.Entry<Integer, ArrayList<PeerFile>> peer : newList.entrySet() )
		{
			clientIds[i] = peer.getKey();
			fileNames[i] = peer.getValue().toArray( new PeerFile[0] );
			i++;
		}
		
		// We update the client model
		model.setListFileFromServer( clientIds, fileNames );
	}
	
	// Used to parse the server's messages
	private String[] readMessage() throws IOException {
		return splitMessage(bufferedReader.readLine());
	}
	
	// sharelist command (used to create or update a list of shared files on the server)
	public void cmdShareFiles() throws IOException {
		String listFileToShare = "";

		File[] sharedFiles = shareFolder.listFiles();

		for (int j = 0; j < sharedFiles.length; j++)
		{
			// Each file is an argument of the sharelist command
			listFileToShare += (j>0?" ":"") + sharedFiles[j].getName();
		}

		printWriter.println("shareList" + " " + listFileToShare);
		printWriter.flush();
		
		lastResponse = readMessage();
	}
	
	// register command (used to register as a "file sharer" on the server
	private void cmdRegister() throws IOException {

		printWriter.println("register" + " " + clientSocket.getLocalAddress().getHostAddress() );
		printWriter.flush();

		lastResponse = readMessage();

		int givenID = Integer.parseInt(lastResponse[1]);
		System.out.println(givenID);
		model.setClientID((int) givenID);
	}
	
	// getip command (used to retrieve an IP from a client ID)
	public String cmdGetIP(int id) throws IOException {
		printWriter.println("getIp" + " " + id);
		printWriter.flush();

		lastResponse = readMessage();

		return lastResponse[1];
	}


	public String getServerIP() {
		return serverIP.getHostAddress();
	}


	public String[] getContenuDossier()
	{
		return shareFolder.list();
	}

	public ClientModel getModel() {
		return model;
	}

	public void setModel(ClientModel model) {
		this.model = model;
	}

}
