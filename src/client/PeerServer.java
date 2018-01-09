
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
import java.io.OutputStream;
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

	
	public PeerServer( ClientModel model, InetAddress _ip, int _port ) throws IOException
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
			System.err.println( "PeerServer: Could not create session for peer client");
			return null;
		}
	}
	
	
	public class PeerServerSession implements Runnable
	{
		private Socket socket;
		private BufferedReader reader;
		private OutputStream os;
		
		public PeerServerSession( Socket _socket ) throws IOException
		{
			socket = _socket;
			reader = new BufferedReader(
				new InputStreamReader(
					socket.getInputStream()
				)
			);
			
			os = new BufferedOutputStream( _socket.getOutputStream() );
		}
		
		private void cmdGetFile( String[] args ) throws IOException
		{
			File fileToSend = new File( model.getShareFolder(), args[0] );
			
			System.out.println("FILE TO SEND: " + fileToSend.getAbsolutePath() );
			
			String reply = "DATA " + fileToSend.length() + "\r\n";
			
			os.write( reply.getBytes() );
			os.flush();
			
			PeerUpload upload = new PeerUpload( socket, fileToSend, (int) fileToSend.length() );
			upload.run();
		}
		
		public void run()
		{
			try
			{
				boolean fileSent = false;
				
				// We keep reading lines as long as we are not in timeout
				while( ! fileSent )
				{
					String line = reader.readLine();
					
					String[] matches = line.split("\\s+");
					String command = matches[0];
					String[] args = new String[0];
					
					if ( matches.length > 1 )
					{
						args = Arrays.copyOfRange( matches, 1, matches.length );
					}
					
					if ( command.toLowerCase().equals("getfile") )
					{
						cmdGetFile( args );
						fileSent = true;
					}
				}
			}
			
			catch( IOException ioe )
			{
				System.out.println( "PeerServer: I/O error while handling PeerClient session." );
			}
			
			
			// If we get here, the client session has ended
			try
			{
				System.out.println( "Closing PeerServer socket..." );
				if (!socket.isClosed())
					socket.close();
				System.out.println( "Socket closed." );
			}
			catch( IOException ioe ) {}
		}
	}
}
