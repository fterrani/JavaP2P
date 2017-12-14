/**
Project name: JavaP2P 
File name: Server.java
Author: Fabien Terrani
Date of creation: 14 déc. 2017
 */

package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server
{
	public static final int PORT_DEFAULT = 50000;
	
	private InetAddress ip;
	private int port;
	private ServerSocket listSocket;
	
	public Server( InetAddress _ip, int _port ) throws IOException
	{
		ip = _ip;
		port = _port;
		
		createSocket();
		
		while( true )
		{
			Socket s = listSocket.accept();
			initClient( s );
		}
	}
	
	private void createSocket() throws IOException
	{
		System.out.println( "Creating socket..." );
		listSocket = new ServerSocket( port, 5, ip ); // Backlog defaults to 5
	}
	
	private void initClient( Socket s ) throws IOException
	{
		System.out.println("Initializing a new client transaction...");
		ClientHandler handler = new ClientHandler( s );
		Thread t = new Thread( handler );
		t.start();
	}
	
	/**
	 * Entry point for starting the server.
	 * @param args All arguments are ignored
	 */
	public static void main( String[] args )
	{
		try
		{
			InetAddress ip = InetAddress.getByName("127.0.0.1");
			int port = Server.PORT_DEFAULT;
			
			Server s = new Server( ip, port );
		}
		
		catch ( UnknownHostException uhe )
		{
			System.err.println( "Unknown host! Please make sure the IP address' format is correct." );
		}
		
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}
}
