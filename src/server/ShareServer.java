/**
Project name: JavaP2P 
File name: Server.java
Author: Fabien Terrani
Date of creation: 14 déc. 2017
 */

package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

import common.AbstractServer;

public class ShareServer extends AbstractServer
{
	public static final int PORT_DEFAULT = 50000;
	public static final int DEFAULT_TIMEOUT = 15 * 60; // in seconds
	
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
		
		// We consider the server started when we are about to listen to client connections
		model.setStarted( true );
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
}
