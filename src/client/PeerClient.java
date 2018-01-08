
/**
Project name: DP_ProjectP2P 
File name: PeerClient.java
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

public class PeerClient {
	private ClientModel model;
	
	public PeerClient(ClientModel model) {
		this.model= model;
		
	}

	public Socket connectToClient(String clientName, int port) throws IOException
	{
		InetAddress clientAddress = InetAddress.getByName(clientName) ;
		Socket clientDownloadingSocket = new Socket(clientAddress, port) ;
		return clientDownloadingSocket ;
	}
	
	// Petit exemple de méthode. ClientFrame appellerait une méthode de Client pour avertir
	// que l'utilisateur veut télécharger un fichier.
	// Client appelerait ensuite peerClient.askForFile( ... )
	
	public PeerDownload askForFile( InetAddress ip, String filename )
	{
		System.out.println("ask for file");
		
		PeerDownload download = null;
		
		try
		{
			Socket peerSocket = connectToClient(ip.getHostAddress(), model.PORT_DEFAULT);
			download = getFile( peerSocket, filename );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return download;
	}
	
	private PeerDownload getFile(Socket socket, String filename) throws IOException
	{
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(
				socket.getInputStream()
			)
		);
		
		PrintWriter writer = new PrintWriter(
			new BufferedOutputStream(
				socket.getOutputStream()
			),
			true // Auto-flushing enabled
		);
		
		writer.println( "getfile " + filename );
		String line = reader.readLine();
		int taille = Integer.parseInt( line.split(" ")[1] );
		
		PeerDownload download = new PeerDownload( socket, new File(model.getShareFolder(), filename), taille );
		model.addNewDownload( download );
		Thread t = new Thread( download );
		t.start();
		
		return download;
	}
}
