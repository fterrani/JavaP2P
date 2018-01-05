/**
Project name: JavaP2P 
File name: AbstractServer.java
Author: Fabien
Date of creation: 4 janv. 2018
 */

package common;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer
{
	protected InetAddress ip;
	protected int port;
	protected ServerSocket listSocket;
	
	public AbstractServer( InetAddress _ip, int _port ) throws IOException
	{
		ip = _ip;
		port = _port;
	}
	
	public void launch() throws IOException
	{
		createListeningSocket();
		
		while( true )
		{
			Socket clientSocket = listSocket.accept();
			
			Runnable task = initClient( clientSocket );
			Thread t = new Thread( task );
			t.start();
		}
	}
	
	protected void createListeningSocket() throws IOException
	{
		listSocket = new ServerSocket( port, 5, ip ); // Backlog defaults to 5
	}
	
	protected abstract Runnable initClient( Socket clientSocket );
}
