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
import java.util.logging.FileHandler;

import server.gui.ShareServerFrame;

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
			int port = ShareServer.PORT_DEFAULT;
			
			// Creating the server
			ShareServer s = new ShareServer( ip, port );
			
			// Creating the frame
			ShareServerFrame sf = new ShareServerFrame( s );
			
			// Logs event in a file
			FileHandler fileLog = new FileHandler("./server_log.txt", true);
			fileLog.setFormatter( new CustomFormatter() );
			s.getLogger().addHandler( fileLog );
			
			// Shows the frame and launches the server
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
