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
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import client.gui.ClientFrame;


// Entry point to run the client (peer)
public class MainClient
{
	// Argument 1: IP of the server sharing the main filelist
	// Argument 2: folder we want to use as the local client folder
	public static void main (String[] args)
	{
		InetAddress peerServerIP;
		InetAddress serverIP;
		
		try
		{
			String argServerIP = null;
			File clientFolder = null;
			
			// Reading server IP from argument 1 if it exists
			if ( args.length >= 1 )
			{
				argServerIP = args[0];
			}
			else
			{
				argServerIP = JOptionPane.showInputDialog(
					null,
					"IP of the filelist server:",
					"No server IP provided",
					JOptionPane.QUESTION_MESSAGE
				);
			}
			
			// We listen on the local machine if no IP was provided
			if ( argServerIP == null )
				argServerIP = "127.0.0.1";
			
			// Setting up client local folder (reads argument 2 if it exists)
			if (args.length >= 2)
				clientFolder = new File( args[1] );
			else
				clientFolder = new File( "./share_folder" );
			
			
			if ( !clientFolder.exists() )
				clientFolder.mkdirs();
			
			if ( !clientFolder.exists() || !clientFolder.canRead() || !clientFolder.canWrite() )
			{
				JOptionPane.showMessageDialog(
					null,
					"Unable to read from/write to local folder... Sorry :(",
					"Read/write error",
					JOptionPane.ERROR_MESSAGE
				);
				
				System.exit(0);
			}
			
			
			
			
			// By default, PeerServer will listen on every interface
			peerServerIP = InetAddress.getByName("0.0.0.0");
			serverIP = InetAddress.getByName( argServerIP );
			
			//creating the client 
			Client cl = new Client(clientFolder, peerServerIP, serverIP );
			
			// We launch the the PeerServer in a different thread.
			// If we don't, the accept() method will block the code's execution
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
		
		catch ( UnknownHostException uhe )
		{
			JOptionPane.showMessageDialog(
				null,
				"We couldn't find the server... Sorry :(",
				"Invalid IP",
				JOptionPane.ERROR_MESSAGE
			);
		}
		
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(
				null,
				"We couldn't connect to the server... Sorry :(",
				"Connection error",
				JOptionPane.ERROR_MESSAGE
			);
		}
	}
}
