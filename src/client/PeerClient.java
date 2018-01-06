
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
		// Contacte un autre peer avec un socket et envoie getfile nomdufichier.txt
		// R�cup�re la taille du fichier � t�l�charger
		
		//startNewDownload( ... );
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
