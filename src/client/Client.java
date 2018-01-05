
/**
Project name: DP_ProjectP2P 
File name: Client.java
Author: Célia Ahmad
Date of creation: 14 déc. 2017
 */
package client;

import java.io.IOException;
import java.net.InetAddress;
import gui.ClientFrame;


public class Client 
 	
{
	private InetAddress ip;
	private int port;
	private ShareServerSession sss ;
	private PeerServer ps ;
	private PeerClient pc ;
	
	public ShareServerSession getSss() {
		return sss;
	}

	public PeerServer getPs() {
		return ps;
	}


	public PeerClient getPc() {
		return pc;
	}
	
	public Client(InetAddress ip) throws IOException {
		
		this.ip = ip;
		port = PeerServer.PORT_DEFAULT;
		sss = new ShareServerSession();
		ps = new PeerServer(ip, port);
		pc = new PeerClient();
	}

	
	// port for incoming connexions
	public static final int PORT_DEFAULT = 60000;
	
	
	
	public static void main (String[] args) {
		 	
		
			try {
				
			//	Client cl = new Client();
			
				// Creating the frame 
				//ClientFrame c = new ClientFrame(cl);
				InetAddress ia = InetAddress.getByName("127.0.0.1");
				
			
				
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
			
			
		}
		
	}
	
	

	
	// Recieving a file from a client 
	
	

	
	


