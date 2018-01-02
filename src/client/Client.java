
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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	/*
	 * Entry point for starting the client
	 * 
	 * @param args All arguments are ignored
	 * 
	 */
	private Socket clientSocket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private Scanner inputScan = new Scanner(System.in);
	private MainFrame frame;
	private String serverIp = "127.0.0.1";;
	private int port = 50000;
	private int clientID = 1;
	private File shareFolder;
	private File[] contenuDossier;
	private String message;

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
		client1.connectToServer();

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
		System.out.println(shareFolder.listFiles());
		contenuDossier = shareFolder.listFiles();

	}

	public void connectToServer() {

		try {
			System.out.println("Connecting to the server: " + serverIp);
			InetAddress serverAdress = InetAddress.getByName(serverIp);
			clientSocket = new Socket(serverAdress, port);
		} catch (IOException e) {
			System.out.println("Connection impossible, check the server IP or server status");
		}
		System.out.println("Connected on port: " + clientSocket.getPort());

		if (clientSocket.isConnected()) {
			createWriterAndReader();
			sendIP();
			getfilelist();
			
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

	private void getfilelist() {

		printWriter.println("getFileList");
		printWriter.flush();
		try {
			message = readMessage(clientSocket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		System.out.println(message);
		
		
	}

	private void sendIP() {
		try {
			printWriter = new PrintWriter(clientSocket.getOutputStream());
			printWriter.println("register" + " " + clientSocket.getInetAddress());
			printWriter.flush();
			message = readMessage(clientSocket);  
			System.out.println(message);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	private String readMessage(Socket socket) throws IOException{
	       return bufferedReader.readLine();               
	   }
	
	
	public void shareFile() {

		try {
			printWriter = new PrintWriter(clientSocket.getOutputStream());

			// send command SHARE
			String output = inputScan.nextLine();
			printWriter.println(output);
			printWriter.flush();
			printWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getClientID() {
		// TODO Auto-generated method stub
		return clientID;
	}

	public String getServerIP() {
		// TODO Auto-generated method stub
		return serverIp;
	}

	public String[] getContenuDossier() {
		String[] nomsfichier = new String[contenuDossier.length];

		for (int i = 0; i < contenuDossier.length; i++) {
			nomsfichier[i] = contenuDossier[i].getName();
		}
		return nomsfichier;
	}
	//

}
