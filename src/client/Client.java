
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
	private InetAddress serverIP;
	private ShareClient sc ;
	private PeerServer ps ;
	private PeerClient pc ;
	private ClientModel model;
	private int port ;
	
	public ShareClient getSss() {
		return sc;
	}

	public PeerServer getPs() {
		return ps;
	}


	public PeerClient getPc() {
		return pc;
	}
	
	public ClientModel getModel() {
		return model;
	}

	
	public Client(InetAddress ip, InetAddress serverIP) throws IOException {
		model= new ClientModel();
		
		this.ip = ip;
		this.serverIP = serverIP;
		port = model.PORT_DEFAULT;
		sc = new ShareClient(model, serverIP);
		ps = new PeerServer(ip, port, model);
		pc = new PeerClient(model);
	}

	
	public static void main (String[] args) {
		InetAddress ip;
		InetAddress serverIP;
		try {
			ip = InetAddress.getByName("127.0.0.1");
			serverIP = InetAddress.getByName("127.0.0.1");
		
				//creating the client 
				Client cl = new Client(ip,serverIP);
			
				// Creating the frame 
				ClientFrame c = new ClientFrame(cl);
				
				// Shows the frame and launches the Peerserver
				c.setVisible( true );
				cl.getPs().launch();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
	}
