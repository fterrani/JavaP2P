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
import java.util.Observer;
import java.util.Set;
import java.util.logging.Logger;

public class ShareServer extends AbstractServer
{
	public static final int PORT_DEFAULT = 50000;
	public static final int DEFAULT_TIMEOUT = 30; // in seconds
	
	private ShareServerModel model;
	private Logger logger;
	
	public ShareServer( InetAddress ip ) throws IOException
	{
		this( ip, PORT_DEFAULT );
	}
	
	public ShareServer( InetAddress ip, int port ) throws IOException
	{
		super(ip, port);

		model = new ShareServerModel();
		logger = Logger.getLogger("share_server");
	}

	public ShareServerModel getModel()
	{
		return model;
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	
	public void launch() throws IOException
	{
		logger.info("Server is starting...");
		super.launch();
	}
	
	protected void createListeningSocket() throws IOException
	{
		logger.info("Listening on " + ip.getHostAddress() + ":" + port );
		super.createListeningSocket();
	}
	
	protected Runnable initClient( Socket clientSocket )
	{
		ClientSession cs = null;
		
		logger.info(
			"Initializing a new client session with "
			+ clientSocket.getInetAddress().getHostAddress()
			+ ":" + clientSocket.getPort()
		);
		
		try
		{
			ClientInfo clientInfo = new ClientInfo( clientSocket.getInetAddress(), clientSocket.getPort() );
			cs = new ClientSession( this, clientSocket, clientInfo );
			
			model.addClientInfo( clientInfo );
			model.addClientSession( cs );
		}
		catch( IOException ioe )
		{
			logger.severe(
				"I/O error when preparing socket with "
				+ clientSocket.getInetAddress().getHostAddress()
				+ ":" + clientSocket.getPort()
			);
		}
		
		return cs;
	}
	
	public int getNextClientId()
	{
		return model.getNextClientId();
	}
	
	public InetAddress getClientIp( int clientId )
	{
		InetAddress ip = null;
		
		Iterator<ClientInfo> i = model.getInfosIterator();
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
		ArrayList<String[]> list = new ArrayList<>();
		
		Iterator<ClientInfo> i = model.getInfosIterator();
		ClientInfo ci;
		
		while( i.hasNext() )
		{
			ci = i.next();
			String[] files = ci.getSharedFiles();
			
			for (int j = 0; j < files.length; j++)
			{
				list.add( new String[] { Integer.toString( ci.getId() ), files[j] } );
			}
		}
		
		return list.toArray( new String[0][]);
	}
}
