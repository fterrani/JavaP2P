/**
Project name: JavaP2P 
File name: PeerDownload.java
Author: Fabien
Date of creation: 5 janv. 2018
 */

package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

public class PeerUpload extends PeerTransfer
{
	// Les "FileNotFoundException" sone jetées lorsque le programe n'arrive pas à LIRE dans le fichier
	// pour une raison quelconque
	// "IOException" représente les erreurs de transfert du réseau
	public PeerUpload( Socket peerSocket, File source, int fileSize ) throws FileNotFoundException, IOException
	{
		super(
			peerSocket,
			new FileInputStream( source ),
			peerSocket.getOutputStream(),
			fileSize
		);
	}
}
