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
import java.util.Set;

public class ClientInfo
{
	private InetAddress ip;
	private int id;
	private Set<String> fileList; // Set of files shared by the client
	
	public ClientInfo( InetAddress ip )
	{
		this.ip = ip;
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
	}
	
	public String toString()
	{
		if ( id > 0 )
			return String.format("client %s (%s)", id, ip.getHostAddress() );
		else
			return String.format("unregistered client (%s)", ip.getHostAddress() );
	}
}
