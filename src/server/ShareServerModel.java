/**
Project name: JavaP2P 
File name: ServerModel.java
Author: Fabien
Date of creation: 4 janv. 2018
 */

package server;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.LogRecord;

import common.ConvenienceObservable;

// Represents all information useful to the ShareServer
// The model is Observable to allow GUI to update itself
public class ShareServerModel extends ConvenienceObservable implements Observer
{
	private boolean started; // Switches to TRUE when the server is started and ready to listen
	private int nextClientId; // Contains the next client ID to provide
	private Set<ClientSession> clientSessions; // Objects dealing with client connections
	private Set<ClientInfo> clientInfos; // Objects dealing with client information
	
	public ShareServerModel()
	{
		setStarted(false);
		nextClientId = 1;
		clientSessions = new HashSet<>();
		clientInfos = new HashSet<>();
	}
	
	public int getNumberOfClientSessions()
	{
		return clientSessions.size();
	}
	
	// Returns a new client ID and increments nextClientId
	public int getNextClientId()
	{
		return nextClientId++;
	}
	
	// Returns a client IP from a client ID
	public InetAddress getClientIp( int clientId )
	{
		InetAddress ip = null;
		
		Iterator<ClientInfo> i = getInfosIterator();
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
	
	// Checks if a client with the provided ID exists
	public boolean clientIdExists( int id )
	{
		boolean found = false;
		Iterator<ClientInfo> i = getInfosIterator();
		ClientInfo ci;
		
		while( !found && i.hasNext() )
		{
			ci = i.next();
			
			if ( ci.getId() == id )
			{
				found = true;
			}
		}
		
		return found;
	}
	
	public String[][] getFilelist()
	{
		// Calling with -1 will create a list containing all files shared by all clients
		return getFilelist( -1 );
	}
	
	// The list follows this pattern: "<client_A>:<filename_a>;<client_A>:<filename_b>;<client_B>:<filename_c>;...."
	public String[][] getFilelist( int clientId )
	{
		ArrayList<String[]> list = new ArrayList<>();
		
		Iterator<ClientInfo> i = getInfosIterator();
		ClientInfo ci;
		
		while( i.hasNext() )
		{
			ci = i.next();
			
			// We add the client's files if every client's files must be returned (-1),
			// or if the clientId we want matches the client's
			if ( clientId == -1 || clientId == ci.getId() )
			{
				String[] files = ci.getSharedFiles();
				
				for (int j = 0; j < files.length; j++)
				{
					list.add( new String[] { Integer.toString( ci.getId() ), files[j] } );
				}
			}
		}
		
		return list.toArray( new String[0][0] );
	}

	public void addClientSession( ClientSession cs )
	{
		clientSessions.add( cs );
		changeAndNotify();
	}
	
	public void removeClientSession( ClientSession cs )
	{
		clientSessions.remove( cs );
		changeAndNotify();
	}
	
	public Iterator<ClientSession> getSessionIterator()
	{
		return clientSessions.iterator();
	}

	public void addClientInfo( ClientInfo ci )
	{
		clientInfos.add( ci );
		ci.addObserver( this );
		changeAndNotify();
	}
	
	public void removeClientInfo( ClientInfo ci )
	{
		clientInfos.remove( ci );
		ci.deleteObserver( this );
		changeAndNotify();
	}
	
	public Iterator<ClientInfo> getInfosIterator()
	{
		return clientInfos.iterator();
	}

	public void update( Observable o, Object args )
	{
		// If clientInfos were updated, we warn the server model observers (in our case, ServerFrame)
		changeAndNotify();
	}

	public boolean isStarted()
	{
		return started;
	}

	public void setStarted( boolean started )
	{
		this.started = started;
		changeAndNotify();
	}
}
