/**
Project name: JavaP2P 
File name: ServerModel.java
Author: Fabien
Date of creation: 4 janv. 2018
 */

package server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.LogRecord;

public class ShareServerModel extends Observable
{
	private int nextClientId;
	private Set<ClientSession> clientSessions;
	private Set<ClientInfo> clientInfos;
	private ArrayList<LogRecord> logs;
	
	public ShareServerModel()
	{
		nextClientId = 1;
		clientSessions = new HashSet<>();
		clientInfos = new HashSet<>();
		logs = new ArrayList<>();
	}
	
	public void addObserver( Observer o )
	{
		super.addObserver( o );
		o.update( this, null );
	}
	
	public void changeAndNotify()
	{
		setChanged();
		notifyObservers();
	}
	
	public int getNextClientId()
	{
		return nextClientId++;
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
		changeAndNotify();
	}
	
	public void removeClientInfo( ClientInfo ci )
	{
		clientInfos.remove( ci );
		changeAndNotify();
	}
	
	public Iterator<ClientInfo> getInfosIterator()
	{
		return clientInfos.iterator();
	}
}
