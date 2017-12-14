/**
Project name: JavaP2P 
File name: ClientHandler.java
Author: Fabien
Date of creation: 14 déc. 2017
 */

package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable
{
	private Socket socket;
	
	/**
	 * Client socket's output stream
	 */
	private Scanner client;
	private OutputStreamWriter osw;
	
	public ClientHandler( Socket socket ) throws IOException
	{
		this.socket = socket;
		
		client = new Scanner(
			new BufferedInputStream(
				socket.getInputStream()
			)
		);
		
		osw = new OutputStreamWriter(
			new BufferedOutputStream(
				socket.getOutputStream()
			)
		);
	}
	
	public void run()
	{
		while ( true )
		{
			String line = client.nextLine();
			
			System.out.println( "From client: " + line );
		}
	}
}
