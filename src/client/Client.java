
/**
Project name: DP_ProjectP2P 
File name: Client.java
Author: Célia Ahmad
Date of creation: 14 déc. 2017
 */
package client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import gui.ClientFrame;


public class Client 
{
	private ClientModel model;
	
	private InetAddress peerServerIP;
	private InetAddress serverIP;
	private int port ;
	
	private ShareClient sc ;
	private PeerServer ps ;
	private PeerClient pc ;
	
	
	
	public ShareClient getShareClient() {
		return sc;
	}

	public PeerServer getPeerServer() {
		return ps;
	}


	public PeerClient getPeerClient() {
		return pc;
	}
	
	public ClientModel getModel() {
		return model;
	}

	
	public Client( File shareFolder, InetAddress _peerServerIP, InetAddress _serverIP ) throws IOException
	{
		model= new ClientModel( shareFolder );
		
		peerServerIP = _peerServerIP;
		serverIP = _serverIP;
		
		port = model.PORT_PEER_SERVER;
		
		sc = new ShareClient(model, serverIP);
		ps = new PeerServer(model, peerServerIP, port );
		pc = new PeerClient(model);
	}
}
