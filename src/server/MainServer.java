/**
Project name: JavaP2P 
File name: MainServer.java
Author: Fabien
Date of creation: 4 janv. 2018
 */

package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainServer
{
	/**
	 * Entry point for starting the server.
	 * @param args All arguments are ignored
	 */
	public static void main( String[] args )
	{
		try
		{
			// Listening IP
			InetAddress ip = InetAddress.getByName("127.0.0.1");
			int port = Server.PORT_DEFAULT;
			
			Server s = new Server( ip, port );
			ServerFrame sf = new ServerFrame( s );
			
			sf.setVisible( true );
			s.launch();
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
