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

public class ShareServerModel extends ConvenienceObservable implements Observer
{
	private boolean started;
	private int nextClientId;
	private Set<ClientSession> clientSessions;
	private Set<ClientInfo> clientInfos;
	
	public ShareServerModel()
	{
		setStarted(false);
		nextClientId = 1;
		clientSessions = new HashSet<>();
		clientInfos = new HashSet<>();
	}
	
	public int getNumberOfClients()
	{
		return clientSessions.size();
	}
	
	public int getNextClientId()
	{
		return nextClientId++;
	}
	
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
		// If clientInfos were updated, we warn the server model observers
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
