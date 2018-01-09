/**
Project name: JavaP2P 
File name: PeerFile.java
Author: Fabien
Date of creation: 9 janv. 2018
 */

package client;

public class PeerFile implements Comparable<PeerFile>
{
	private int clientId;
	private String fileName;
	
	public PeerFile( int _clientId, String _fileName )
	{
		clientId = _clientId;
		fileName = _fileName;
	}

	public int getClientId()
	{
		return clientId;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public String toString()
	{
		return String.format( "%s (client %s)", fileName, clientId );
	}

	public int compareTo(PeerFile pf)
	{
		return fileName.compareTo( pf.getFileName() );
	}
}
