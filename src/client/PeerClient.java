
/**
Project name: DP_ProjectP2P 
File name: PeerClient.java
Author: Célia Ahmad
Date of creation: 5 janv. 2018
 */

package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	
	public void askForFile( InetAddress ip, String filename )
	{
		
		try {
			Socket peerSocket = connectToClient(ip.getHostAddress(), model.PORT_DEFAULT);
		
		// Contacte un autre peer avec un socket et envoie getfile nomdufichier.txt
		// Récupère la taille du fichier à télécharger
		File fileToDownload = getFile(filename);
		int size = (int) fileToDownload.length();
		
		startNewDownload(peerSocket,fileToDownload, size );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private File getFile(String filename) {
		File f = null ;
		File [] files = model.getShareFolder().listFiles();
		
		for (int i = 0; i < files.length; i++) {
			if (filename == files[i].getName())
				return files[i];
		}
		
		return f;
		
	}
	
	// Socket doit être créé avant, dest est connu grâce à la liste des fichiers partagés,
	// et filesize a été récupéré de la réponse à la commande getfile
	public PeerDownload startNewDownload( Socket peerSocket, File dest, int fileSize )
	{
		PeerDownload download = null;
		
		try
		{
			download = new PeerDownload( peerSocket, dest, fileSize );
			Thread t = new Thread( download );
			t.start();
			return download;
		}
		
		catch (FileNotFoundException e)
		{
			// Erreur lors de la création du fichier de destination
		}
		
		catch (IOException e)
		{
			// Erreur réseau
		}
		
		return download;
	}
}
