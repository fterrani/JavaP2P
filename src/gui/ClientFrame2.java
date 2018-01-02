
/**
Project name: DP_ProjectP2P 
File name: ClientFrame2.java
Author: Célia Ahmad
Date of creation: 2 janv. 2018
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;

import client.Client;

public class ClientFrame2 extends JFrame  {
	
	private JPanel pnNord= new JPanel();
	private JPanel pnSud= new JPanel();
	
	// panel mainClient 
	private JPanel mainClient= new JPanel();
	private JLabel jlMainClient= new JLabel("Main client");
	private JList jltMainClient= new JList(  new String[] {"a","b","c","b","c","b","c","a","b","c","b","c","b","c","b","c","b","c","b","c","b","c","b","c","b","c","b","c"});
	private JScrollPane jsMainClient;
	private JButton jbMainClient= new JButton("Connect");
	
	
	// panel other Client 
	private JPanel otherClient= new JPanel();
	private JLabel jlOtherClients= new JLabel("Other client");
	private JTable jtOhterClients = new JTable();
	private JScrollPane jsOtherClients;
	private JButton jbOtherClients= new JButton("Download");
	
	
	
	private JList currentDownload= new JList(  new String[] {"a","b","c","b","c","b","c","b","c","b","c","b","c","b","c","b","c","b","c","b","c"});
	private JScrollPane jsCurrentDownlad;
	
	// panel Info
	private JPanel pnInfo= new JPanel();
	private JLabel statut= new JLabel("bonjour");
	
	public ClientFrame2(Client c) {

		setTitle("Interface principale");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setPreferredSize(new Dimension(1500,1000));
		setBackground(Color.WHITE);

		mainClient.setBackground( Color.GREEN );
		pnNord.setBackground( Color.RED );
		jlMainClient.setBackground( Color.BLUE );
		jlMainClient.setOpaque( true );
		
		setLayout(new BorderLayout());
		addAllComponents(c);

		pack();
		setVisible(true);
		
	}
	
	private void addAllComponents(Client c) {
		
		// Panel mainClient
		mainClient.setLayout(new BorderLayout());
		jsMainClient= new JScrollPane(jltMainClient);
		mainClient.add(jlMainClient, BorderLayout.NORTH);
		mainClient.add(jsMainClient,BorderLayout.CENTER);
		mainClient.add(jbMainClient,BorderLayout.SOUTH);
		
		// Panel otherclient
		
		jsOtherClients = new JScrollPane(jtOhterClients);
		otherClient.setLayout(new BorderLayout());;
		otherClient.add(jlOtherClients,BorderLayout.NORTH);
		otherClient.add(jsOtherClients,BorderLayout.CENTER);
		otherClient.add(jbOtherClients,BorderLayout.SOUTH);
		
		//panel info 
		pnInfo.add(statut);
		
		pnNord.setBorder( BorderFactory.createEmptyBorder(20,20,20,20));
		pnNord.setLayout(new GridLayout(1,2, 20, 0));
		pnNord.add(mainClient);
		pnNord.add(otherClient);
		
		currentDownload.setVisibleRowCount(9);
		jsCurrentDownlad = new JScrollPane(currentDownload);

		pnSud.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		pnSud.setBackground(Color.PINK);
		jsCurrentDownlad.setBackground(Color.PINK);
		pnSud.setLayout(new BorderLayout());
		pnSud.add(jsCurrentDownlad, BorderLayout.CENTER);
		
		add(pnNord, BorderLayout.NORTH);
		add(pnSud,BorderLayout.CENTER);
		add(pnInfo,BorderLayout.SOUTH);
			
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
