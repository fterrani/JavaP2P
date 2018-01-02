/**
Project name: JavaP2P 
File name: ClientHandler.java
Author: Fabien
Date of creation: 14 déc. 2017
 */

package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable
{
	private Socket socket;
	private long timeout;
	
	/**
	 * Client socket's output stream
	 */
	private BufferedReader client;
	private OutputStreamWriter osw;
	
	public ClientHandler( Socket _socket ) throws IOException
	{
		socket = _socket;
		resetTimeout();
		
		client = new BufferedReader(
			new InputStreamReader(
				socket.getInputStream()
			)
		);
		
		osw = new OutputStreamWriter(
			new BufferedOutputStream(
				socket.getOutputStream()
			)
		);
	}
	
	private void resetTimeout()
	{
		// Moment when a timeout will occur if no client command was received
		// (30 seconds in the future)
		timeout = System.currentTimeMillis() + (1000 * 30);
	}
	
	public void run()
	{
		try
		{
			// We keep reading lines as long as we are not in timeout
			while( System.currentTimeMillis() < timeout )
			{
				String line = client.readLine();
				
				if ( line != null )
				{
					
					// We give again 30 seconds to the client to send something
					resetTimeout();
					
					System.out.println( "From client: " + line );
				}
			}
			
			System.out.printf(
				"Client %s timeout: more than 30 seconds without receiving a command!%n",
				socket.getInetAddress().getHostAddress()
			);
		}
		
		catch( IOException ioe )
		{
			ioe.printStackTrace();
		}
	}
}
