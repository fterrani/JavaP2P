/**
Project name: JavaP2P 
File name: ServerModel.java
Author: Fabien
Date of creation: 4 janv. 2018
 */

package server;

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
	private int nextClientId;
	private Set<ClientSession> clientSessions;
	private Set<ClientInfo> clientInfos;
	
	public ShareServerModel()
	{
		nextClientId = 1;
		clientSessions = new HashSet<>();
		clientInfos = new HashSet<>();
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
	
	public String[][] getFilelist()
	{
		ArrayList<String[]> list = new ArrayList<>();
		
		Iterator<ClientInfo> i = getInfosIterator();
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
		changeAndNotify();
	}
}
