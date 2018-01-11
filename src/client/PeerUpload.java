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

//This class represents a file upload from a socket
public class PeerUpload extends PeerTransfer
{
	// ( FileNotFoundException are thrown if the program was not able to WRITE in a file for any reason )
	public PeerUpload( Socket peerSocket, File source, int fileSize ) throws FileNotFoundException, IOException
	{
		super(
			new FileInputStream( source ),
			peerSocket.getOutputStream(),
			fileSize
		);
		
		// If we couldn't connect to the PeerClient, we throw an exception
		if ( !peerSocket.isConnected() )
		{
			System.out.println( "Unconnected to PeerClient! Unable to share " + source.getName() );
			throw new IOException();
		}
	}
}
