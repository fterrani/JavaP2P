
/**
Project name: DP_ProjectP2P 
File name: PeerServer.java
Author: Célia Ahmad
Date of creation: 5 janv. 2018
 */

package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import common.AbstractServer;

public class PeerServer extends AbstractServer{
	
	// méthode downloadFile( InetAddress ip, String fileToDownload )
	// Créer un PeerClientModel extends Observable
			

	public static final int PORT_DEFAULT = 60000;
	
	public PeerServer(InetAddress _ip, int _port) throws IOException {
		super(_ip, _port);
		launch();
		
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Runnable initClient(Socket clientSocket) {
		// TODO Auto-generated method stub
		return null;
	}
	

	
	
	
	

}
