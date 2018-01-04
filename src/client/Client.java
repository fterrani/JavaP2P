
/**
Project name: DP_ProjectP2P 
File name: Client.java
Author: Célia Ahmad
Date of creation: 14 déc. 2017
 */

package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	/*
	 * Entry point for starting the client
	 * 
	 * @param args All arguments are ignored
	 * 
	 */

	private static Socket clientSocket;
	private static BufferedReader bufferedReader; 
	private static PrintWriter printWriter; 
	private static Scanner inputScan = new Scanner(System.in);
	private static String s ;
	private MainFrame frame;
	private String serverIp= "127.0.0.1";;
	private int port =50000;
	private int clientID=1;
	
	public Client(){
	 
		
	}
	
	
	public static void main(String[] args) {

		Client client1 = new Client ();
		client1.connectToServer();
		client1.shareFile();
		

	}

	public void connectToServer(){
		
		try {
			System.out.println("Connecting to the server: " + serverIp);
			InetAddress serverAdress = InetAddress.getByName(serverIp);
			clientSocket = new Socket(serverAdress, port);			
		} catch (IOException e) {
			System.out.println("Connection impossible, check the server IP or server status");
		}
		System.out.println("Connected on port: " + clientSocket.getPort());
		System.out.println("Client registered");
	
	}
	
	public void shareFile() {
		
		try {
			printWriter = new PrintWriter(clientSocket.getOutputStream());
			
			// send command SHARE 
			String output = inputScan.nextLine(); 
			printWriter.println(output);
			printWriter.flush(); 
			printWriter.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public int getClientID() {
		// TODO Auto-generated method stub
		return clientID;
	}


	public String getServerIP() {
		// TODO Auto-generated method stub
		return serverIp;
	}


}
