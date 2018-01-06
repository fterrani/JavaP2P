/**
Project name: JavaP2P 
File name: PeerTransfer.java
Author: Fabien
Date of creation: 5 janv. 2018
 */
package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import common.ConvenienceObservable;

// Cette classe est la classe de base pour PeerDownload et PeerUpload.
// Peu importe le sens du transfert, elle se charge de le gérer et de mettre
// à jour un pourcentage :) Cool non? Moi j'trouve que ça rocks du poney.

// Pour organiser le transfert, on avait défini une commande "getfile fichier.txt"
// que A envoie à B mais on n'avait pas défini la réponse de B.
// B va répondre soit : "EROR File fichier.txt not found"
// Soit : "DATA 1024" avec 1024 qui est la taille du fichier à transférer en bytes.
// On doit connaître cette information pour pouvoir connaître l'avancement du téléchargement.

public class PeerTransfer extends ConvenienceObservable implements Runnable
{
	private Socket peerSocket;
	
	private OutputStream dest;
	private InputStream source;
	
	private double progress;
	private int transferedBytes;
	private int fileSize;
	
	public void run()
	{
		try
		{
			// C'est ici qu'on va transférer le fichier.
			
			// Le code est un peu technique mais ça va nous permettre d'afficher l'avancement
			// du téléchargement
			
			byte[] buffer = new byte[1024 * 32]; // On va lire par "tranches" de 32 KB
			int readBytes = -1;
			
			// À chaque tour de boucle, on lit une partie de la source
			while( (readBytes = source.read(buffer)) != -1 )
			{
				// Puis on écrit dans la destination
				dest.write( buffer, 0, readBytes );
				
				// Ici on modifie les données pour signaler combien de bytes ont été transférés
				addTransferedBytes( readBytes );
			}
			
			// On ferme le socket lorsque le transfert est terminé
			peerSocket.close();
			
			// On ferme les flux source et destination
			// L'un des deux a automatiquement été fermé lorsque peerSocket a été fermé,
			// mais comme on ne sait pas lequel, on ferme les deux.
			source.close();
			dest.close();
		}
		catch( IOException ioe )
		{
			// Si on arrive ici, c'est qu'il y a eu une erreur lors du transfert !
		}
	}
	
	public PeerTransfer( Socket _peerSocket, InputStream inStream, OutputStream outStream, int _fileSize )
	{
		peerSocket = _peerSocket;
		
		transferedBytes = 0;
		fileSize = _fileSize;
		
		source = inStream;
		dest = outStream;
	}
	
	public void addTransferedBytes( int bytes )
	{
		transferedBytes += bytes;
		setProgress( ((float) transferedBytes) / fileSize );
	}
	
	public double getProgress()
	{
		return progress;
	}
	
	public boolean isFinished()
	{
		return (progress >= 1.0);
	}

	private void setProgress( float progress )
	{
		this.progress = progress;
		
		// C'est grâce à ça qu'on avertit les Observer intéressés par la progression
		// du transfert.
		changeAndNotify();
	}
}

