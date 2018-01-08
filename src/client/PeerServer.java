
/**
Project name: DP_ProjectP2P 
File name: PeerServer.java
Author: Célia Ahmad
Date of creation: 5 janv. 2018
 */

package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.regex.Pattern;

import common.AbstractServer;
import server.ClientInfo;
import server.ShareServer;

public class PeerServer extends AbstractServer {
	private ClientModel model;

	// méthode downloadFile( InetAddress ip, String fileToDownload )
	// Créer un PeerClientModel extends Observable

	
	public PeerServer(InetAddress _ip, int _port, ClientModel model) throws IOException
	{
		super(_ip, _port);
		this.model = model;
	}

	protected Runnable initClient( Socket clientSocket )
	{
		try
		{
			return new PeerServerSession( clientSocket );
		}
		catch( IOException ioe )
		{
			return null;
		}
	}
	
	
	public class PeerServerSession implements Runnable
	{
		private Socket socket;
		private BufferedReader reader;
		private BufferedOutputStream bos;
		private File f;
		
		public PeerServerSession( Socket socket ) throws IOException
		{
			reader = new BufferedReader(
				new InputStreamReader(
					socket.getInputStream()
				)
			);
			
			bos = new BufferedOutputStream( socket.getOutputStream() );
			f = null;
		}
		
		private void cmdGetFile( String[] args ) throws IOException
		{
			f = new File( args[0] );
			String reply = "DATA " + f.length() + "\r\n";
			
			bos.write( reply.getBytes() );
			
			PeerUpload upload = new PeerUpload( socket, f, (int) f.length() );
			upload.run();
		}
		
		public void run()
		{
			try
			{
				boolean fileInfoSent = false;
				
				// We keep reading lines as long as we are not in timeout
				while( ! fileInfoSent )
				{
					String line = reader.readLine();
					
					if ( line != null )
					{
						if ( Pattern.matches("^[A-Za-z].*", line) )
						{
							String[] matches = line.split("\\s+");
							String command = matches[0];
							String[] args = new String[0];
							
							if ( matches.length > 1 )
							{
								args = Arrays.copyOfRange( matches, 1, matches.length );
							}
							
							switch( command.toLowerCase() )
							{
								case "getfile": cmdGetFile( args ); break;
							}
						}
					}
				}
			}
			
			catch( IOException ioe )
			{
				ioe.printStackTrace();
			}
			
			
			// If we get here, the client session has ended
			try
			{
				// Closes the socket
				socket.close();
			}
			catch( IOException ioe ) {}
		}
	}
}
