/**
Project name: JavaP2P 
File name: MainClient.java
Author: Fabien
Date of creation: 8 janv. 2018
 */

package client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import client.gui.ClientFrame;

public class MainClient
{
	// First parameter is the folder we want to use as the client folder
	public static void main (String[] args)
	{
		InetAddress peerServerIP;
		InetAddress serverIP;
		
		try
		{
			File clientFolder;
			
			if (args.length > 0)
				clientFolder = new File( args[0] );
			else
				clientFolder = initFolder();
			
			peerServerIP = InetAddress.getByName("127.0.0.1");
			serverIP = InetAddress.getByName("127.0.0.1");
			
			//creating the client 
			Client cl = new Client(clientFolder, peerServerIP, serverIP );
			
			Thread t = new Thread( new Runnable()
			{
				public void run()
				{
					try
					{
						cl.getPeerServer().launch();
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
			cl.getShareClient().connectToServer();
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static File initFolder()
	{
		char c = 'A';
		Path p = Paths.get("./shareFolders/client_" + c);
		
		while( Files.exists(p) && Files.isDirectory(p) )
		{
			c += 1;
			p = Paths.get("./shareFolders/client_" + c);
		}
		
		File shareFolder = p.toFile();
		
		if (!shareFolder.exists()) {
			shareFolder.mkdirs();
		}
		File test = new File(shareFolder, "test_client_"+c+".txt");
		try {
			test.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(shareFolder.getName());
		System.out.println(shareFolder.listFiles().length);
		
		return shareFolder;
	}
}
