/**
Project name: JavaP2P 
File name: PeerDownload.java
Author: Fabien
Date of creation: 5 janv. 2018
 */

package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PeerDownload extends PeerTransfer
{
	private String filename;
	
	// Les "FileNotFoundException" sone jetées lorsque le programe n'arrive pas à ÉCRIRE dans le fichier
	// pour une raison quelconque
	// "IOException" représente les erreurs de transfert du réseau
	public PeerDownload( Socket peerSocket, File dest, int fileSize ) throws FileNotFoundException, IOException
	{
		super(
			peerSocket.getInputStream(),
			new FileOutputStream( dest ),
			fileSize
		);
		
		filename = dest.getName();
	}
	
	public String getFileName()
	{
		return filename;
	}
}
