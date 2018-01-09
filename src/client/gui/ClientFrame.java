
/**
Project name: DP_ProjectP2P 
File name: ClientFrame2.java
Author: Célia Ahmad
Date of creation: 2 janv. 2018
 */

package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import client.Client;
import client.ClientModel;
import client.PeerClient;
import client.PeerDownload;
import client.PeerServer;
import client.ShareClient;

public class ClientFrame extends JFrame implements Observer, ActionListener {

	private ClientModel cm;

	private JPanel pnNord = new JPanel();
	private JPanel pnCenter = new JPanel();
	private JPanel pnSud = new JPanel();

	// panel mainClient

	private JPanel mainClient = new JPanel();
	private String[] contenuDossier;
	private JLabel jlMainClient;
	private JList jltMainClient;
	private JScrollPane jsMainClient;
	private JButton jbMainClient = new JButton("Refresh list");

	// panel other Client
	private JPanel otherClient = new JPanel();
	private JLabel jlOtherClients = new JLabel("Files available");

	// titel
	private JLabel connectedTo;
	private JLabel clientID;

	// private JTable jtOhterClients = new JTable();
	private String[] listFiles;
	private JList<String> jtOtherClients;
	private JScrollPane jsOtherClients;
	private JButton jbdownload = new JButton("Download");

	private JList currentDownload;
	private JScrollPane jsCurrentDownload;

	private JLabel jlcurrentDownload = new JLabel("Current downloads");

	private ShareClient sss;
	private PeerServer ps;
	private PeerClient pc;
	private Client cl;

	// panel sud
	private ArrayList<CustomProgressBar> bars ;
	private Map<PeerDownload,CustomProgressBar> downloadComponents = new HashMap<>();
	private JPanel pncustomProgressBar = new JPanel();

	public ClientFrame(Client cl) {
		this.cl = cl;
		sss = cl.getShareClient();
		ps = cl.getPeerServer();
		pc = cl.getPeerClient();
		cm = cl.getModel();
		
		setTitle("Interface principale");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(1000, 500));
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BoxLayout(getContentPane(), 1));
		addAllComponents();

		pack();
		
		cm.addObserver(this);
	}

	private static void biggerFont(JComponent c) {
		c.setFont(c.getFont().deriveFont(24.0f));
	}

	private void addAllComponents() {

		// Panel nord
		clientID = new JLabel();
		updateClientID();
		biggerFont(clientID);
		pnNord.setLayout(new BorderLayout());
		pnNord.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		pnNord.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		pnNord.add(clientID);
		pnNord.setPreferredSize(new Dimension(0, 0));

		// Panel center

		// Panel mainClient à gauche
		mainClient.setLayout(new BorderLayout());

		contenuDossier = sss.getContenuDossier();
		jltMainClient = new JList(contenuDossier);

		jsMainClient = new JScrollPane(jltMainClient);
		jlMainClient = new JLabel("Shared files");
		biggerFont(jlMainClient);

		mainClient.add(jlMainClient, BorderLayout.NORTH);
		mainClient.add(jsMainClient, BorderLayout.CENTER);
		mainClient.add(jbMainClient, BorderLayout.SOUTH);

		// Panel otherclient à droite
		listFiles = sss.getDisplayedList();
		jtOtherClients = new JList<String>(listFiles);
		jsOtherClients = new JScrollPane(jtOtherClients);
		otherClient.setLayout(new BorderLayout());
		otherClient.add(jlOtherClients, BorderLayout.NORTH);
		otherClient.add(jsOtherClients, BorderLayout.CENTER);
		otherClient.add(jbdownload, BorderLayout.SOUTH);
		biggerFont(jlOtherClients);

		pnCenter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		pnCenter.setLayout(new GridLayout(1, 2, 20, 0));
		pnCenter.add(mainClient);
		pnCenter.add(otherClient);

		jbdownload.addActionListener(this);
		jbMainClient.addActionListener(this);

		// panel sud
		jsCurrentDownload = new JScrollPane(currentDownload);
		pnSud.setLayout(new BorderLayout());
		pnSud.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		biggerFont(jlcurrentDownload);
		pnSud.add(jlcurrentDownload, BorderLayout.NORTH);

		pncustomProgressBar.setLayout(new BoxLayout(pncustomProgressBar, BoxLayout.Y_AXIS));
		pncustomProgressBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		pnSud.add(pncustomProgressBar, BorderLayout.CENTER);

		add(pnNord, BorderLayout.NORTH);
		add(pnCenter, BorderLayout.CENTER);
		add(pnSud, BorderLayout.SOUTH);

	}

	// methode from Observer Interface
	public void update( Observable o, Object arg )
	{
		if ( arg instanceof PeerDownload )
		{
			PeerDownload download = (PeerDownload) arg;
			CustomProgressBar bar;
			
			// We get the progress bar linked to that download if it exists
			if ( downloadComponents.containsKey(download) )
			{
				bar = downloadComponents.get( download );
			}
			
			// We create the progress bar if it does not exist
			else
			{
				bar = new CustomProgressBar( download.getFileName(), 0 );
				
				bar.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
				
				// Association de la barre au téléchargement
				downloadComponents.put( download, bar );
				
				// Ajout de la barre à l'interface
				pncustomProgressBar.add( bar );
			}
			
			
			if ( download.errorOccured() )
			{
				bar.setError( true );
			}
			else
			{
				int progress = (int) Math.round( download.getProgress() * 100f );
				bar.setValue( progress );
			}
		}
		
		else if ( arg instanceof String )
		{
			String s = arg.toString();
			
			if ( s.equals("server_filelist") )
			{
				String[] serverList = cl.getModel().getListFileFromServer();
				
				jtOtherClients.setListData( serverList );
			}
		}
		
		updateClientID();
	}

	public void updateClientID()
	{
		String status = "Connecting to server...";
		
		if ( cm.getClientID() > 0 )
		{
			status = "Client " + cm.getClientID() + " connected to " + sss.getServerIP();
		}
		
		clientID.setText( status );
	}

	public void actionPerformed( ActionEvent e )
	{
		if ( e.getSource() == jbdownload )
		{
			try
			{
				String string = jtOtherClients.getSelectedValue();
				System.out.println(jtOtherClients.getSelectedValue());
				String[] parts = string.split("\t");
				int id = Integer.parseInt(parts[0]);
				String filename = parts[1];
				System.out.println(parts[0] + "\t"  + parts[1]);
				
				
				InetAddress ip = InetAddress.getByName( sss.cmdGetIP( id ) );
				
				Thread t = new Thread( new Runnable()
				{
					public void run()
					{
						pc.askForFile(ip, filename);
					}
				});
				
				t.start();
				
			}
			catch (IOException ioe)
			{
				System.err.println( "Error while downloading the file..." );
			}
		}
		
		else if( e.getSource() == jbMainClient )
		{
			try
			{
				cl.getShareClient().cmdGetfilelistFromServer();
			} catch (IOException e1)
			{
				System.err.println( "Error while refreshing the shared files list..." );
			}
		}
	}
}
