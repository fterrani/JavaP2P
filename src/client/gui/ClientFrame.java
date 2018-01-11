
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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import client.Client;
import client.ClientModel;
import client.PeerClient;
import client.PeerDownload;
import client.PeerFile;
import client.PeerServer;
import client.ShareClient;

public class ClientFrame extends JFrame implements Observer, ActionListener {

	private ClientModel cm;

	private JPanel pnNord = new JPanel();
	private JPanel pnCenter = new JPanel();
	private JPanel pnSud = new JPanel();

	// panel mainClient
	private JPanel mainClient = new JPanel();
	private String[] localFolderContent;
	private JLabel jlMainClient;
	private JList<String> jltMainClient;
	private JScrollPane jsMainClient;
	private JButton shareAndRefresh = new JButton("Share and refresh all");

	// panel peers
	private JPanel pnPeers = new JPanel();
	private JPanel jpPeerButtons = new JPanel();
	private JPanel jpClientButtons = new JPanel();
	private JLabel jlOtherClients = new JLabel("Files available");
	private JButton refreshListFromServer = new JButton("Refresh list");
	private JList<PeerFile> filesFromPeer; // A list containing files shared by the selected peer
	private JComboBox<String> peerList; // A list of peers
	private JScrollPane jsOtherClients;
	private JButton jbdownload = new JButton("Download");

	// panel north
	private JLabel clientID;
	private JScrollPane jsCurrentDownload;
	private JLabel jlcurrentDownload = new JLabel("Current downloads");
	private ShareClient sc;
	private PeerServer ps;
	private PeerClient pc;
	private Client cl;

	// panel sud
	private ArrayList<CustomProgressBar> bars;
	private Map<PeerDownload, CustomProgressBar> downloadComponents = new HashMap<>();
	private JPanel pncustomProgressBar = new JPanel();

	public ClientFrame(Client cl) {
		this.cl = cl;
		sc = cl.getShareClient();
		ps = cl.getPeerServer();
		pc = cl.getPeerClient();
		cm = cl.getModel();

		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(1000, 500));
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BoxLayout(getContentPane(), 1));

		addAllComponents();
		pack();
		
		// We observe the client's model to update the GUI when needed
		cm.addObserver(this);
	}

	private static void biggerFont(JComponent c, float size) {
		c.setFont(c.getFont().deriveFont(size));
	}

	private void addAllComponents() {

		// Panel nord
		clientID = new JLabel();
		clientID.setOpaque(true);
		updateClientID();
		biggerFont(clientID,16f);
		pnNord.setLayout(new FlowLayout( FlowLayout.LEFT, 0, 0 ));
		pnNord.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		pnNord.add(clientID);
		
		// Panel mainClient left
		mainClient.setLayout(new BorderLayout());
		localFolderContent = sc.getContenuDossier();
		jltMainClient = new JList<String>(localFolderContent);
		jltMainClient.setVisibleRowCount( 5 );
		
		jsMainClient = new JScrollPane(jltMainClient);
		jlMainClient = new JLabel("Shared files");
		biggerFont(jlMainClient, 22f);
		
		
		jpClientButtons.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 5 ) );
		jpClientButtons.add( shareAndRefresh );
		
		mainClient.add(jlMainClient, BorderLayout.NORTH);
		mainClient.add(jsMainClient, BorderLayout.CENTER);
		mainClient.add(jpClientButtons, BorderLayout.SOUTH);

		// Panel peers right
		peerList = new JComboBox<String>( new String[]{"Loading peers..."} );
		filesFromPeer = new JList<PeerFile>();
		filesFromPeer.setVisibleRowCount( 5 );
		jsOtherClients = new JScrollPane(filesFromPeer);
		pnPeers.setLayout(new BorderLayout());
		pnPeers.add(jlOtherClients, BorderLayout.NORTH);
		pnPeers.add(jsOtherClients, BorderLayout.CENTER);
		jpPeerButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpPeerButtons.add(refreshListFromServer);
		jpPeerButtons.add(jbdownload);
		jpPeerButtons.add(peerList);
		pnPeers.add(jpPeerButtons, BorderLayout.SOUTH);
		biggerFont(jlOtherClients, 22f);
		
		// panel center
		pnCenter.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
		pnCenter.setLayout(new GridLayout(1, 2, 20, 0));
		pnCenter.add(mainClient);
		pnCenter.add(pnPeers);
		
		// Listening to button events...
		peerList.addActionListener(this);
		jbdownload.addActionListener(this);
		shareAndRefresh.addActionListener(this);
		refreshListFromServer.addActionListener(this);

		// panel sud
		pnSud.setLayout(new BorderLayout());
		pnSud.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
		biggerFont(jlcurrentDownload, 22f);
		pnSud.add(jlcurrentDownload, BorderLayout.NORTH);
		pncustomProgressBar.setLayout(new BoxLayout(pncustomProgressBar, BoxLayout.Y_AXIS));
		pncustomProgressBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
		jsCurrentDownload = new JScrollPane(pncustomProgressBar);
		pnSud.add(jsCurrentDownload, BorderLayout.CENTER);
		
		
		
		// adding panels to contentPane
		add(pnNord, BorderLayout.NORTH);
		add(pnCenter, BorderLayout.CENTER);
		add(pnSud, BorderLayout.SOUTH);

	}

	// method from Observer Interface
	public void update(Observable o, Object arg)
	{
		// A download started or progressed
		if (arg instanceof PeerDownload)
		{
			PeerDownload download = (PeerDownload) arg;
			CustomProgressBar bar;

			// We get the progress bar linked to that download if it exists
			if (downloadComponents.containsKey(download)) {
				bar = downloadComponents.get(download);
			}

			// We create the progress bar if it does not exist
			else {
				bar = new CustomProgressBar(download.getFileName(), 0);

				bar.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

				// Binding downloadbar with download
				downloadComponents.put(download, bar);

				// Adding bar to the gui
				pncustomProgressBar.add(bar);
			}

			if (download.errorOccured()) {
				bar.setError(true);
			} else {
				int progress = (int) Math.round(download.getProgress() * 100f);
				bar.setValue(progress);
				if (download.isFinished()) {
					jltMainClient.setListData(sc.getContenuDossier());
					try {
						sc.cmdShareFiles();
						sc.cmdGetfilelistFromServer();
					} catch (IOException e) {
						System.err.println("Error while refreshing the shared files list after complete download...");
					}
				}

			}
		}

		else if (arg instanceof String) {
			String s = arg.toString();
			
			// We must update the displayed file list
			if (s.equals("server_filelist"))
			{
				// We update the peer list with the one received from the server
				updatePeerList();
			}
		}

		updateClientID();
	}

	public void updateClientID() {
		String status = "Connecting to server...";

		if (cm.getClientID() > 0) {
			status = "Client " + cm.getClientID() + " connected to server " + sc.getServerIP();
		}

		//updating label 
		clientID.setText(status);
		
		//updating title bar
		setTitle("Client ID : " + cm.getClientID() + " / " + "Folder : " + cm.getShareFolder().getName());
	}
	
	private void updatePeerList()
	{
		peerList.removeAllItems();
		peerList.addItem( "All peers" );
		
		for( int clientId : cl.getModel().getListFileFromServer().keySet() )
		{
			peerList.addItem( "Client " + clientId );
		}
		
		peerList.setSelectedIndex(0);
		
		// We show all files by default
		showFileList( 0 );
	}
	
	// Shows the files for peer with ID clientId.
	// If clientId is 0, shows all files from all peers.
	private void showFileList( int clientId )
	{
		ArrayList<PeerFile> files = new ArrayList<>();
		
		Map<Integer, PeerFile[]> list = cl.getModel().getListFileFromServer();
		
		if ( clientId != 0 )
		{
			// If the client ID exists in the list, we show its files
			if ( list.keySet().contains(clientId) )
				files.addAll( Arrays.asList( list.get(clientId) ) );
			
			// Otherwise, we let the list empty
		}
		
		else
		{
			// If no client ID is selected (id=0), we show all files from all peers
			for( Map.Entry<Integer, PeerFile[]> peer : list.entrySet() )
			{
				files.addAll( Arrays.asList( peer.getValue() ) );
			}
		}
		
		// We sort the filelist before showing it
		files.sort( null );
		
		filesFromPeer.setListData( files.toArray(new PeerFile[0]) );
	}
	
	public void actionPerformed(ActionEvent e) {
		
		// We must download a file for the user
		if (e.getSource() == jbdownload)
		{
			try
			{
				PeerFile peerFile = filesFromPeer.getSelectedValue();
				
				if (peerFile != null)
				{
					System.out.println("File to download: " + filesFromPeer.getSelectedValue() );
	
					InetAddress ip = InetAddress.getByName(sc.cmdGetIP( peerFile.getClientId() ));
	
					Thread t = new Thread(new Runnable() {
						public void run() {
							pc.askForFile( ip, peerFile.getFileName() );
						}
					});
	
					t.start();
				}
			} catch (IOException ioe) {
				System.err.println("Error while downloading the file...");
			}
		}
		
		// We must share the local folder's files and update the file list
		else if (e.getSource() == shareAndRefresh)
		{
			try {

				jltMainClient.setListData(sc.getContenuDossier());
				sc.cmdShareFiles();
				sc.cmdGetfilelistFromServer();
			} catch (IOException e1) {
				System.err.println("Error while refreshing local folder and sharing with server");
			}
		}
		
		// We must refresh the file list from the server
		else if (e.getSource() == refreshListFromServer)
		{
			try {
				sc.cmdGetfilelistFromServer();
			} catch (IOException e1) {
				System.err.println("Error while refreshing the shared files list...");
			}
		}
		
		// We must show the selected peer's files
		else if (e.getSource() == peerList && peerList.getSelectedIndex() > -1)
		{
			// Display the selected peer's files or all files if no peer was selected
			String selection = peerList.getItemAt( peerList.getSelectedIndex() );
			String[] parts = selection.split(" ");
			
			int clientId = 0;
			
			if ( parts.length >= 2 )
			{
				try
				{
					clientId = Integer.parseInt( parts[1] );
				}
				catch(NumberFormatException nfe) {}
			}
			
			showFileList( clientId );
		}
	}
}
