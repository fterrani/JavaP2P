
/**
Project name: DP_ProjectP2P 
File name: PeerServer.java
Author: C�lia Ahmad
Date of creation: 5 janv. 2018
 */

package client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;

import common.AbstractServer;
import server.ClientInfo;

public class PeerServer extends AbstractServer {
	private ClientModel model;

	// m�thode downloadFile( InetAddress ip, String fileToDownload )
	// Cr�er un PeerClientModel extends Observable

	
	public PeerServer(InetAddress _ip, int _port, ClientModel model) throws IOException {
		super(_ip, _port);
		this.model = model;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Runnable initClient(Socket clientSocket) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	

}
