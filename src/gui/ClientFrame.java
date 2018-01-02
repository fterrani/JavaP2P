
/**
Project name: DP_ProjectP2P 
File name: ClientFrame.java
Author: Célia Ahmad
Date of creation: 24 déc. 2017
 */

package gui;

import java.awt.*;

import javax.swing.*;
import client.Client;

public class ClientFrame extends JFrame {

	private JPanel pnConnexion = new JPanel();

	private JLabel ClientID;
	private JLabel serverIP;

	private JButton jbConnect = new JButton("Connect");
	private JButton jbDisconnect = new JButton("Disconnect");

	private JPanel pnBase= new JPanel();
	private JPanel pnNord= new JPanel();
	private JPanel pnSud= new JPanel();
	private JPanel pnInfo= new JPanel();
	private JPanel pnAllFile = new JPanel();
	private JList<String> jlClientFile;
	private JScrollPane jscClientFile;

	
	final static boolean shouldFill = true;
	final static boolean shouldWeightX = true;
	final static boolean RIGHT_TO_LEFT = false;

	public ClientFrame(Client c) {

		setTitle("Interface principale");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setPreferredSize(new Dimension(1000, 800));
		setBackground(Color.WHITE);
		addAllComponents(c);

		pack();
		setVisible(true);

	}

	private void addAllComponents(Client c) {
		ClientID = new JLabel();
		ClientID.setText("ID du client : " + c.getClientID());

		serverIP = new JLabel();
		serverIP.setText("Adresse IP du server :" + c.getServerIP());

		jbConnect.setBorderPainted(true);
		jbConnect.setFocusPainted(false);
		jbConnect.setContentAreaFilled(false);
		jbDisconnect.setBorderPainted(true);
		jbDisconnect.setFocusPainted(false);
		jbDisconnect.setContentAreaFilled(false);

		pnConnexion.setLayout(new BorderLayout());
		pnInfo.setLayout(new BorderLayout());
		pnConnexion.add(ClientID, BorderLayout.WEST);
		pnConnexion.add(serverIP, BorderLayout.EAST);
		pnInfo.add(jbConnect, BorderLayout.LINE_START);
		pnInfo.add(jbDisconnect, BorderLayout.LINE_END);
		
		jlClientFile = new JList<String>();
		jscClientFile = new JScrollPane(jlClientFile);
		jscClientFile.setCursor(new Cursor(Cursor.HAND_CURSOR));
		jscClientFile.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pnAllFile.add(jscClientFile);

		pnBase.setLayout(new BorderLayout());
		pnBase.add(pnInfo, BorderLayout.NORTH);
		pnBase.add(pnConnexion);
		pnBase.add(pnAllFile, BorderLayout.SOUTH);

		add(pnBase);
		
	}
}
