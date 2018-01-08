/**
Project name: JavaP2P 
File name: MainClient.java
Author: Fabien
Date of creation: 8 janv. 2018
 */

package client;

import java.io.IOException;
import java.net.InetAddress;

import gui.ClientFrame;

public class MainClient
{
	
	public static void main (String[] args)
	{
		InetAddress peerServerIP;
		InetAddress serverIP;
		
		try
		{
			peerServerIP = InetAddress.getByName("127.0.0.1");
			serverIP = InetAddress.getByName("127.0.0.1");
			
			//creating the client 
			Client cl = new Client(peerServerIP,serverIP);
			
			Thread t = new Thread( new Runnable()
			{
				public void run()
				{
					try
					{
						cl.getPs().launch();
					} catch (IOException e)
					{
						System.err.println("I/O error with PeerServer !");
					}
				}
			});
			t.start();
			
			
			// Creating the frame 
			ClientFrame c = new ClientFrame(cl);
			
			// Shows the frame and launches the Peerserver
			c.setVisible( true );
			cl.getSss().connectToServer();
		}
		
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
