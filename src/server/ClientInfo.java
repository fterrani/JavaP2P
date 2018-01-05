/**
Project name: JavaP2P 
File name: ClientInfo.java
Author: Fabien
Date of creation: 2 janv. 2018
 */

package server;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import common.ConvenienceObservable;

public class ClientInfo extends ConvenienceObservable
{
	private InetAddress ip;
	private int port;
	private int id;
	private Set<String> fileList; // Set of files shared by the client
	
	public ClientInfo( InetAddress ip, int port )
	{
		this.ip = ip;
		this.port = port;
		this.id = -1;
		this.fileList = new HashSet<>();
	}

	public InetAddress getIp()
	{
		return ip;
	}

	public int getId()
	{
		return id;
	}
	
	public void setId( int id )
	{
		this.id = id;
		changeAndNotify();
	}

	public String[] getSharedFiles()
	{
		// We don't want to allow the fileList collection to be directly modified
		return fileList.toArray( new String[0] );
	}

	public void setSharedFiles( String[] files )
	{
		fileList.clear();
		fileList.addAll( Arrays.asList(files) );
		changeAndNotify();
	}
	
	public String toString()
	{
		if ( id > 0 )
			return String.format("Client %s (%s:%s)", id, ip.getHostAddress(), port );
		else
			return String.format("Unregistered client (%s:%s)", ip.getHostAddress(), port );
	}
}
