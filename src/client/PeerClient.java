
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

import javax.swing.JOptionPane;


// Client to ask file to other peers

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
	
	// Asks for information about the file to download then starts the download
	public void askForFile( InetAddress ip, String filename )
	{
		System.out.println(
			"Client " + model.getClientID()
			+ " ask for " + filename
			+ " stored at " + ip.getHostAddress() + ". Launching a thread..."
		);
		
		Thread t = new Thread( new Runnable()
		{
			public void run()
			{
				try
				{
					Socket peerSocket = connectToClient(ip.getHostAddress(), model.PORT_PEER_SERVER);
					getFile( peerSocket, filename );
				}
				catch (Exception e)
				{
					JOptionPane.showMessageDialog(null,
						String.format(
							"Problem while connecting to PeerServer %s and retrieving %s.",
							ip.getHostAddress(),
							filename
						)
					);
					
					System.out.println( "Problem while connecting to PeerServer and retrieving " + filename );
				}
			}
		});
		
		t.start();
	}
	
	// Sends a getfile command to a PeerServer to get the file size
	private void getFile(Socket socket, String filename) throws IOException
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
		
		// We read the filesize obtained from the PeerServer
		String line = reader.readLine();
		String[] parts = line.split(" ");
		
		if ( parts[0].equals("EROR") )
		{
			// We throw a FileNotFoundException if the file couldn't be found on the peer's system
			throw new FileNotFoundException();
		}
		
		else
		{
			int taille = Integer.parseInt( parts[1] );
			
			// We start downloading the file
			PeerDownload download = new PeerDownload( socket, new File(model.getShareFolder(), filename), taille );
			model.addNewDownload( download ); // The download is added to the model
			download.run();
		}
		
		
		

	}
}
