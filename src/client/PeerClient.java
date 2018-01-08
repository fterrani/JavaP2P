
/**
Project name: DP_ProjectP2P 
File name: PeerClient.java
Author: C�lia Ahmad
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
	
	// Petit exemple de m�thode. ClientFrame appellerait une m�thode de Client pour avertir
	// que l'utilisateur veut t�l�charger un fichier.
	// Client appelerait ensuite peerClient.askForFile( ... )
	
	public void askForFile( InetAddress ip, String filename )
	{
		
		try {
			Socket peerSocket = connectToClient(ip.getHostAddress(), model.PORT_DEFAULT);
		
		// Contacte un autre peer avec un socket et envoie getfile nomdufichier.txt
		// R�cup�re la taille du fichier � t�l�charger
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
	
	// Socket doit �tre cr�� avant, dest est connu gr�ce � la liste des fichiers partag�s,
	// et filesize a �t� r�cup�r� de la r�ponse � la commande getfile
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
			// Erreur lors de la cr�ation du fichier de destination
		}
		
		catch (IOException e)
		{
			// Erreur r�seau
		}
		
		return download;
	}
}
