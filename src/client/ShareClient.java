
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ShareClient extends Observable {

	// port for connexion to server
	public static final int PORT_DEFAULT = 50000;
	private InetAddress serverIP ;
	private Socket clientSocket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private String[] response;
	private String givenIp;
	private ClientModel model;
	private File shareFolder;
	private String[] listFileFromServer;
	

	// constructeur sans argument qui crée un dossier au lancement
	public ShareClient(ClientModel model, InetAddress serverIP) {
		this.model = model;
		this.serverIP= serverIP;
		shareFolder= model.getShareFolder();
		listFileFromServer = model.getListFileFromServer();
		initFolder();
		
	}

	// constructeur avec argument qui récupère le dossier déjà existant
	public ShareClient(File file, ClientModel model) {
		this.model = model;
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

	/* Connexion and retrieving file list to/from server */
	public void connectToServer() {

		try {
			System.out.println("Connecting to the server: " + serverIP.getHostName());
			clientSocket = new Socket(serverIP, PORT_DEFAULT);
		} catch (IOException e) {
			System.out.println("Connection impossible, check the server IP or server status");
		}
		System.out.println("Connected on port: " + clientSocket.getPort());

		if (clientSocket.isConnected()) {
			createWriterAndReader();
			sendIP();
			shareFiles();
			getfilelistFromServer();
		
			// close connection to server
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

			System.out.println(response[0] + " >>> '" + response[1] + "'");

			if (response[1].equals("")) {
				listFileFromServer = new String[0];
			}

			else {
				String[] strFiles = response[1].split(";");
				listFileFromServer = new String[strFiles.length];

				for (int i = 0; i < strFiles.length; i++) {
					String[] parts = strFiles[i].split(":");
					listFileFromServer[i] = parts[0] + "\t" + parts[1];
				}
			}

			changeAndNotifyObservers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String[] readMessage() throws IOException {
		return splitMessage(bufferedReader.readLine());
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

	private void sendIP() {
		try {

			printWriter.println("register" + " " + clientSocket.getInetAddress().getHostAddress());
			printWriter.flush();

			response = readMessage();

			int givenID = Integer.parseInt(response[1]);
			System.out.println(givenID);
			model.setClientID((int) givenID);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getIP(int id) {
		try {
			printWriter.println("getIp" + " " + id);
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

	public void addObserver(Observer o) {
		super.addObserver(o);

		// We only warn the observer that has just been added
		o.update(this, null);
	}

	// Marks the model as changed and notifies all observers
	private void changeAndNotifyObservers() {
		setChanged();
		notifyObservers();
	}

	// getter and setter

	public String getServerIP() {
		// TODO Auto-generated method stub
		return serverIP.getHostAddress();
	}


	public String[] getContenuDossier() {
		return shareFolder.list();
	}

	public String[] getDisplayedList() {
		return listFileFromServer;
	}

	public ClientModel getModel() {
		return model;
	}

	public void setModel(ClientModel model) {
		this.model = model;
	}

}
