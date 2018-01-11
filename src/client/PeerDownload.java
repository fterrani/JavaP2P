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

// This class represents a file download from a socket
public class PeerDownload extends PeerTransfer
{
	private String filename;
	
	// ( FileNotFoundException are thrown if the program was not able to WRITE in a file for any reason )
	public PeerDownload( Socket peerSocket, File dest, int fileSize ) throws FileNotFoundException, IOException
	{
		super(
			peerSocket.getInputStream(),
			new FileOutputStream( dest ),
			fileSize
		);
		
		// If we couldn't connect to the PeerServer, we throw an exception
		if ( !peerSocket.isConnected() )
		{
			System.out.println( "Unconnected to PeerServer! Unable to download " + dest.getName() );
			throw new IOException();
		}
		
		filename = dest.getName();
	}
	
	public String getFileName()
	{
		return filename;
	}
}
