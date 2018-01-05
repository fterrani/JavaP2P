
/**
Project name: DP_ProjectP2P 
File name: PeerClient.java
Author: Célia Ahmad
Date of creation: 5 janv. 2018
 */

package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class PeerClient {

	
	
	
	

	public Socket connectToClient(String clientName, int port) throws IOException
	{
		InetAddress clientAddress = InetAddress.getByName(clientName) ;
		Socket clientDownloadingSocket = new Socket(clientAddress, port) ;
		return clientDownloadingSocket ;
	}
	
}
