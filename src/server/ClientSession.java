/**
Project name: JavaP2P 
File name: ClientHandler.java
Author: Fabien
Date of creation: 14 déc. 2017
 */

package server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class ClientSession implements Runnable
{
	// Network-related variables
	private ShareServer server;
	private Socket socket;
	private long timeout;
	
	// Reader and writer
	private BufferedReader client;
	private PrintWriter osw;
	
	// Application layer variables
	private ClientInfo clientInfo;
	
	
	public ClientSession( ShareServer _server, Socket _socket, ClientInfo _clientInfo ) throws IOException
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
		
		log( Level.INFO, "Socket ready to be used." );
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
		if ( args.length != 1 )
		{
			log( Level.WARNING, "register", "1 argument expected (received "+args.length+")" );
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
			log( Level.WARNING, "register", "Invalid IP format ("+args[0]+")" );
			return;
		}

		if ( ! ip.equals( clientInfo.getIp() ) )
		{
			log( Level.WARNING,
				"register",
				String.format(
					"Provided IP (%s) does not match yours (%s)",
					ip.getHostAddress(),
					clientInfo.getIp().getHostAddress()
				)
			);
			return;
		}
		
		int id = server.getModel().getNextClientId();
		clientInfo.setId( id );
		
		sendTextData( Integer.toString(id) );
		
		log(
			Level.INFO,
			"register",
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
			log( Level.WARNING, "sharelist", "no argument received" );
			return;
		}
		
		for (int i = 0; i < args.length; i++)
		{
			args[i] = args[i].trim();
		}
		
		clientInfo.setSharedFiles( args );
		
		log( Level.INFO, "sharelist", "Sharelist of the client has been updated." );
	}
	
	private void cmdGetfilelist( String[] args )
	{
		if ( args.length != 0 )
		{
			log( Level.WARNING, "getfilelist", "0 arguments expected ("+args.length+" given)" );
			return;
		}
		
		String[][] filelist = server.getModel().getFilelist();
		
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
		
		log( Level.INFO, "getfilelist", "Current server sharelist was sent to the client." );
	}
	
	private void cmdGetip( String[] args )
	{
		if ( args.length != 1 )
		{
			log( Level.WARNING, "getip", "1 argument expected ("+args.length+" given)" );
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
			log( Level.WARNING, "getip", "Invalid client ID ("+ args[0] +")" );
			return;
		}
		
		InetAddress ip = server.getModel().getClientIp(clientId);
		
		if ( ip == null )
		{
			log( Level.WARNING, "getip", "client ID not found ("+ clientId +")" );
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

	private void log( Level logLevel, String txt )
	{
		log( logLevel, null, txt );
	}
	
	private void log( Level logLevel, String command, String txt )
	{
		String msg = clientInfo + ": ";
		
		if (command != null)
			msg += command + " => ";
		
		msg += txt;
		
		server.getLogger().log( logLevel, msg );
	}
	
	public void run()
	{
		try
		{
			// We give the client some time to reply
			resetTimeout( ShareServer.DEFAULT_TIMEOUT );
			
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
					resetTimeout( ShareServer.DEFAULT_TIMEOUT );
				}
				
				// We check if a timeout occured
				timeoutOccured = System.currentTimeMillis() >= timeout;
			}
			
			if (timeoutOccured)
			{
				log( Level.WARNING, "Timeout occured. Too much time without receiving a command." );
			}
			
			if (clientQuit)
			{
				log( Level.INFO, "Session ending gracefully..." );
			}
		}
		
		catch( IOException ioe )
		{
			log( Level.SEVERE, "I/O error" );
		}
		
		
		// If we get here, the client session has ended
		try
		{
			// Closes the socket
			socket.close();
			log( Level.INFO, "Socket closed." );
		}
		catch( IOException ioe ) {}
		
		// Removes this client session from the list
		server.getModel().removeClientSession( this );
		log( Level.INFO, "Session removed from session list." );
	}

	public ClientInfo getClientInfo()
	{
		return clientInfo;
	}
}
