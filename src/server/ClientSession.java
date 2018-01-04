/**
Project name: JavaP2P 
File name: ClientHandler.java
Author: Fabien
Date of creation: 14 déc. 2017
 */

package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientSession implements Runnable
{
	// Network-related variables
	private Server server;
	private Socket socket;
	private long timeout;
	
	// Reader and writer
	private BufferedReader client;
	private PrintWriter osw;
	
	// Application layer variables
	private ClientInfo clientInfo;
	
	
	public ClientSession( Server _server, Socket _socket, ClientInfo _clientInfo ) throws IOException
	{
		server = _server;
		socket = _socket;
		clientInfo = _clientInfo;
		
		client = new BufferedReader(
			new InputStreamReader(
				socket.getInputStream()
			)
		);
		
		osw = new PrintWriter(
			new BufferedOutputStream(
				socket.getOutputStream()
			),
			true // Auto-flushing enabled
		);
	}
	
	private void resetTimeout( int seconds )
	{
		if ( seconds <= 0 )
			throw new IllegalArgumentException("Timeout duration must be greater than 0 ("+seconds+" given)");
		
		// Moment when a timeout will occur if no client command was received
		timeout = System.currentTimeMillis() + (1000 * seconds);
	}
	
	private void cmdRegister( String[] args )
	{
		if ( args.length > 1 )
		{
			sendError( "1 argument expected (received "+args.length+")" );
			return;
		}
		
		InetAddress ip = null;
		try
		{
			// We use getByName() to check the validity of the IP
			ip = InetAddress.getByName( args[0] );
		}
		catch (UnknownHostException e) {}

		if ( ip == null )
		{
			sendError( "Invalid IP format ("+args[0]+")" );
			return;
		}

		if ( ! ip.equals( clientInfo.getIp() ) )
		{
			sendError(
				String.format(
					"Provided IP (%s) does not match yours (%s)",
					ip.getHostAddress(),
					clientInfo.getIp().getHostAddress()
				)
			);
			return;
		}
		
		registerClient();
	}
	
	private void registerClient()
	{
		int id = server.getNextClientId();
		
		clientInfo.setId( id );
		
		sendTextData( Integer.toString(id) );
		
		sendMessage(
			String.format(
				"IP (%s) has been registered with the following ID: %s%n",
				clientInfo.getIp().getHostAddress(),
				id
			)
		);
	}

	private void cmdSharelist( String[] args )
	{
		if ( args.length == 0 )
		{
			sendError( "no argument received" );
			return;
		}
		
		for (int i = 0; i < args.length; i++)
		{
			args[i] = args[i].trim();
		}
		
		clientInfo.setSharedFiles( args );
		
		sendMessage( "Filelist of "+clientInfo+" was updated." );
	}
	
	private void cmdGetfilelist( String[] args )
	{
		if ( args.length != 0 )
		{
			sendError( "0 argument expected" );
			return;
		}
		
		String[][] filelist = server.getFilelist();
		
		StringBuffer sb = new StringBuffer("");
		
		for (int i = 0; i < filelist.length; i++)
		{
			if (i>0)
				sb.append(";");
			
			sb.append( filelist[i][0] );
			sb.append( ":" );
			sb.append( filelist[i][1] );
		}
		
		sendTextData( sb.toString() );
		sendMessage( "Current filelist was sent to client " + clientInfo );
	}
	
	private void cmdGetip( String[] args )
	{
		if ( args.length != 1 )
		{
			sendError( "1 argument expected" );
			return;
		}
		
		int clientId = -1;
		
		try
		{
			clientId = Integer.parseInt( args[0] );
		}
		catch(NumberFormatException nfe) {}
		
		if ( clientId <= 0 )
		{
			sendError( "Invalid client ID" );
			return;
		}
		
		InetAddress ip = server.getClientIp(clientId);
		
		if ( ip == null )
		{
			sendError( "client ID not found" );
			return;
		}
		
		sendTextData( ip.getHostAddress() );
	}

	public void sendMessage( String txt )
	{
		osw.println( "MESG " + txt );
	}
	
	public void sendError( String txt )
	{
		osw.println( "EROR " + txt );
	}
	
	public void sendTextData( String txt )
	{
		osw.println( "DATA " + txt );
	}
	
	public void run()
	{
		try
		{
			// We give the client some time to reply
			resetTimeout( Server.DEFAULT_TIMEOUT );
			
			boolean timeoutOccured = false;
			boolean clientQuit = false;
			
			// We keep reading lines as long as we are not in timeout
			while( ! timeoutOccured && ! clientQuit )
			{
				String line = client.readLine();
				
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
							case "register": cmdRegister( args ); break;
							case "sharelist": cmdSharelist( args ); break;
							case "getfilelist": cmdGetfilelist( args ); break;
							case "getip": cmdGetip( args ); break;

							default:
								sendError( "Invalid command: " + matches[0] );
						}
					}
					
					else
					{
						sendError( "Commands must start with a letter (A-Z or a-z)" );
					}
					
					// We give again some seconds to the client to send something
					resetTimeout( Server.DEFAULT_TIMEOUT );
				}
				
				// We check if a timeout occured
				timeoutOccured = System.currentTimeMillis() < timeout;
			}
			
			if (timeoutOccured)
			{
				System.out.printf(
					clientInfo + " timeout: more than 30 seconds without receiving a command!%n",
					socket.getInetAddress().getHostAddress()
				);
			}
			
			if (clientQuit)
			{
				System.out.printf(
					clientInfo + " ended transaction gracefully.%n",
					socket.getInetAddress().getHostAddress()
				);
			}
		}
		
		catch( IOException ioe )
		{
			System.out.println( "I/O error!" );
			ioe.printStackTrace();
		}
		
		// If we get here, the client session has ended
	}

	public ClientInfo getClientInfo()
	{
		return clientInfo;
	}
}
