
/**
Project name: DP_ProjectP2P 
File name: ClientFrame2.java
Author: Célia Ahmad
Date of creation: 2 janv. 2018
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;

import client.Client;

public class ClientFrame extends JFrame {

	private JPanel pnNord = new JPanel();
	private JPanel pnSud = new JPanel();

	// panel mainClient
	private JPanel mainClient = new JPanel();
	private JLabel jlMainClient ;
	private JList jltMainClient ;
	private JScrollPane jsMainClient;
	private JButton jbMainClient = new JButton("Refresh list ");

	// panel other Client
	private JPanel otherClient = new JPanel();
	private JLabel jlOtherClients = new JLabel("Files available");
//	private JTable jtOhterClients = new JTable();
	private JList jtOtherClients ;
	private JScrollPane jsOtherClients;
	private JButton jbOtherClients = new JButton("Download");
	
	private JList currentDownload = new JList(new String[] { "a", "b", "c", });
	private JScrollPane jsCurrentDownload;
	private String[] contenuDossier;
	private String[] listFiles;

	// panel Info
	private JPanel pnInfo = new JPanel();
	private JLabel statut = new JLabel("bonjour");

	public ClientFrame(Client c) {

		setTitle("Interface principale");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setPreferredSize(new Dimension(1500, 1000));
		setBackground(Color.WHITE);

		// colors
		// mainClient.setBackground( Color.GREEN );
		// pnNord.setBackground( Color.RED );
		// jlMainClient.setBackground( Color.BLUE );
		// jlMainClient.setOpaque( true );
		// jsCurrentDownload.setBackground(Color.PINK);
		// pnSud.setBackground(Color.PINK);

		setLayout(new BorderLayout());
		addAllComponents(c);

		pack();
		setVisible(true);

	}
	
	private static void biggerFont( JComponent c )
	{
		c.setFont(c.getFont().deriveFont( 24.0f ));
	}

	private void addAllComponents(Client c) {

		// Panel mainClient à gauche dans panel nord
		mainClient.setLayout(new BorderLayout());
		
	    contenuDossier= c.getContenuDossier();
		jltMainClient= new JList(contenuDossier);

		jsMainClient = new JScrollPane(jltMainClient);
		jlMainClient = new JLabel("Client ID "+ c.getClientID() + " shared folder");
		biggerFont(jlMainClient);
		
		
		mainClient.add(jlMainClient, BorderLayout.NORTH);
		mainClient.add(jsMainClient, BorderLayout.CENTER);
		mainClient.add(jbMainClient, BorderLayout.SOUTH);

		// Panel otherclient à droite dans panel nord
		listFiles = c.getDisplayedList();
		jtOtherClients = new JList(listFiles);
		jsOtherClients = new JScrollPane(jtOtherClients);
		otherClient.setLayout(new BorderLayout());
		otherClient.add(jlOtherClients, BorderLayout.NORTH);
		otherClient.add(jsOtherClients, BorderLayout.CENTER);
		otherClient.add(jbOtherClients, BorderLayout.SOUTH);
		biggerFont(jlOtherClients);

		// panel nord
		pnNord.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		pnNord.setLayout(new GridLayout(1, 2, 20, 0));
		pnNord.add(mainClient);
		pnNord.add(otherClient);

		// panel sud info
		pnInfo.add(statut);

		// panel sud
		jsCurrentDownload = new JScrollPane(currentDownload);
		pnSud.setLayout(new BorderLayout());
		pnSud.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		pnSud.add(jsCurrentDownload, BorderLayout.CENTER);

		add(pnNord, BorderLayout.NORTH);
		add(pnSud, BorderLayout.CENTER);
		add(pnInfo, BorderLayout.SOUTH);

	}

	public static void main(String[] args) {
	
	}

}
