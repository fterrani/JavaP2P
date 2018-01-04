/**
Project name: JavaP2P 
File name: Server.java
Author: Fabien Terrani
Date of creation: 14 déc. 2017
 */

package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Server extends AbstractServer
{
	public static final int PORT_DEFAULT = 50000;
	public static final int DEFAULT_TIMEOUT = 30; // in seconds
	
	private ServerSocket listSocket;
	
	private Set<ClientSession> clientSessions;
	private Set<ClientInfo> clientInfos;
	private int nextClientId;
	
	public Server( InetAddress ip ) throws IOException
	{
		this( ip, PORT_DEFAULT );
	}
	
	public Server( InetAddress ip, int port ) throws IOException
	{
		super(ip, port);

		clientSessions = new HashSet<>();
		clientInfos = new HashSet<>();
		
		nextClientId = 1; // We start client ids at 1
	}
	
	protected void createListeningSocket() throws IOException
	{
		System.out.println( "Creating socket..." );
		super.createListeningSocket();
	}
	
	protected Runnable initClient( Socket clientSocket )
	{
		ClientSession cs = null;
		
		System.out.println("Initializing a new client transaction...");
		
		try
		{
			ClientInfo clientInfo = new ClientInfo( clientSocket.getInetAddress() );
			cs = new ClientSession( this, clientSocket, clientInfo );
			
			clientInfos.add( clientInfo );
			clientSessions.add( cs );
		}
		catch( IOException ioe )
		{
			// TODO Log error
		}
		
		return cs;
	}
	
	public int getNextClientId()
	{
		return nextClientId++;
	}
	
	public InetAddress getClientIp( int clientId )
	{
		InetAddress ip = null;
		
		Iterator<ClientInfo> i = clientInfos.iterator();
		ClientInfo info;
		
		while ( i.hasNext() && ip == null )
		{
			info = i.next();
			
			if ( info.getId() == clientId )
			{
				ip = info.getIp();
			}
		}
		
		return ip;
	}
	
	public String[][] getFilelist()
	{
		String[][] list = new String[ clientInfos.size() ][0];
		
		return null;
	}
}
