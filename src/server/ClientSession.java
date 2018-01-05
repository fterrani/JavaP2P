/**
Project name: JavaP2P 
File name: ClientHandler.java
Author: Fabien
Date of creation: 14 d�c. 2017
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
				"IP (%s) has been registered with the following ID: %s",
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
	
	// Returns the client ID if it is a positive number and is associated with a client.
	// returns -1 otherwise.
	private int parseAndCheckClientId( String arg )
	{
		int clientId = -1;
		boolean isNumber = true;
		boolean positiveNumber = true;
		
		try
		{
			// We try to parse the string
			clientId = Integer.parseInt( arg );
			
			// If the parsing succeeded, we check if the number is a potentially existing client ID
			if (clientId <= 0)
			{
				positiveNumber = false;
			}
		}
		catch(NumberFormatException nfe)
		{
			isNumber = false;
		}
		
		if ( !isNumber || !positiveNumber )
		{
			log( Level.WARNING, "Invalid client ID ("+ arg +")" );
			return -1;
		}
		
		if ( server.getModel().clientIdExists( clientId ) )
		{
			log( Level.INFO, "No client with ID "+ clientId );
			return -1;
		}
		
		return clientId;
	}
	
	private void cmdGetfilelist( String[] args )
	{
		if ( args.length > 1 )
		{
			log( Level.WARNING, "getfilelist", "0 or 1 argument expected ("+args.length+" given)" );
			return;
		}
		
		String[][] filelist = new String[0][0];
		
		if (args.length == 0)
		{
			filelist = server.getModel().getFilelist();
		}
		
		else if (args.length == 1)
		{
			int clientId = parseAndCheckClientId( args[0] );
			
			if (clientId <= 0)
			{
				// We send an error and nothing else if the 1 argument version of getfilelist
				// was used and an invalid ID was received
				sendError( "Invalid ID received or no client found with this ID ("+args[0]+")" );
				return;
			}
			
			filelist = server.getModel().getFilelist( clientId );
		}
		
		// In other case (0 argument, or 1 argument with a potentially existing client ID),
		// we try to return the associated list
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
		
		int clientId = parseAndCheckClientId( args[0] );
		
		
		if ( clientId <= 0 )
		{
			// The ID does not exist or is not associated with a client
			sendError( "Invalid ID received ("+args[0]+")" );
			return;
		}
		
		// If clientId > 0, we know a client with this ID exists
		InetAddress ip = server.getModel().getClientIp( clientId );
		
		// No IP found (theoretically, this should never happen)
		if ( ip == null )
		{
			log( Level.WARNING, "getip", "No matching IP found for client with ID "+ clientId );
			sendError("No IP found for client with ID " + clientId );
			return;
		}
		
		// IP found, we send it
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
