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

// This class allows to create a server that listens on a defined port and IP.
// Each time someone connects, the server gets a Runnable object with initClient and
// launches the task in a new thread
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
			// We listen for connections
			Socket clientSocket = listSocket.accept();
			
			// We run a task per connection
			Runnable task = initClient( clientSocket );
			Thread t = new Thread( task );
			t.start();
		}
	}
	
	protected void createListeningSocket() throws IOException
	{
		listSocket = new ServerSocket( port, 5, ip ); // Backlog defaults to 5
	}
	
	// The Runnable task executed by the server will be provided by implementing classes
	protected abstract Runnable initClient( Socket clientSocket );
	
	public InetAddress getIp()
	{
		return ip;
	}
	
	public int getPort()
	{
		return port;
	}
}
