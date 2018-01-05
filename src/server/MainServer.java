/**
Project name: JavaP2P 
File name: MainServer.java
Author: Fabien
Date of creation: 4 janv. 2018
 */

package server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
		FileHandler fileLog = null;
		
		try
		{
			// Logs event in a file
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			String filename = String.format( "./server_logs/server_log_%s.txt", sdf.format(new Date()) );
			fileLog = new FileHandler( filename, true );
			fileLog.setFormatter( new CustomFormatter() );
		}
		catch (IOException ioe)
		{
			System.err.println( "Cannot use log file!" );
		}
		
		try
		{
			// Listening IP
			InetAddress ip = InetAddress.getByName("127.0.0.1");
			int port = ShareServer.PORT_DEFAULT;
			
			// Creating the server
			ShareServer s = new ShareServer( ip, port );
			
			// Creating the frame
			ShareServerFrame sf = new ShareServerFrame( s );
			
			// Connecting server logs to the filehandler
			if ( fileLog != null )
				s.getLogger().addHandler( fileLog );
			
			// Shows the frame and launches the server
			sf.setVisible( true );
			s.launch();
		}
		
		catch ( UnknownHostException uhe )
		{
			System.err.println( "Listening IP format is invalid" );
			System.exit( 1 );
		}

		catch ( BindException be )
		{
			System.err.println( "Port already in use or IP cannot be used." );
			System.exit( 1 );
		}
		catch ( IOException e )
		{
			System.exit( 1 );
		}
	}
}
