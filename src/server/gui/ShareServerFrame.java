/**
Project name: JavaP2P 
File name: ServerFrame.java
Author: Fabien
Date of creation: 4 janv. 2018
 */

package server.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.DateFormatter;

import server.ShareServer;
import server.ShareServerModel;

public class ShareServerFrame extends JFrame implements Observer
{
	private ShareServer server;
	
	private JLabel status;
	
	private JTabbedPane tabs;
	private JPanel filesPanel;
	private JPanel logPanel;
	
	private JLabel filesTabTitle;
	private JTable filesTable;
	private DefaultTableModel filesModel;
	private String[] filesColumns;
	
	private LogTable logTable;
	
	public ShareServerFrame( ShareServer _server )
	{
		server = _server;
		
		status = new JLabel("Server is starting...");
		status.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		logTable = new LogTable();
		
		// This will display the server's logs in the log table
		server.getLogger().addHandler( logTable.getHandler() );
		
		filesTabTitle = new JLabel("Shared files");
		makeFontBigger( filesTabTitle );
		filesColumns = new String[] {"Client ID", "Client IP", "File name"};
		filesModel = new DefaultTableModel( filesColumns, 0 );
		filesTable = new JTable( filesModel );
		
		filesPanel = new JPanel();
		filesPanel.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20) );
		filesPanel.setLayout( new BorderLayout() );
		filesPanel.add( new JScrollPane( filesTable ), BorderLayout.CENTER );
		
		logPanel = new JPanel();
		logPanel.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20) );
		logPanel.setLayout( new BorderLayout() );
		logPanel.add( new JScrollPane( logTable.getTable() ), BorderLayout.CENTER );
		
		
		tabs = new JTabbedPane();
		tabs.setFont( tabs.getFont().deriveFont(16f) );
		tabs.addTab( null, filesPanel );
		tabs.setTabComponentAt( 0, filesTabTitle );
		tabs.addTab( "Logs", logPanel );
		
		add( tabs, BorderLayout.CENTER );
		add( status, BorderLayout.NORTH );
		
		
		makeFontBigger( logTable.getTable() );
		makeFontBigger( filesTable );
		makeFontBigger( status );
		
		
		// We must observe here to make sure all variables are ready to be used
		server.getModel().addObserver( this );
		
		setTitle("Server");
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		setPreferredSize( new Dimension(1000, 600) );
		pack();
		setLocationRelativeTo( null );
	}
	
	private void makeFontBigger( Component c )
	{
		c.setFont( c.getFont().deriveFont( 16f ) );
		
		if ( c instanceof JTable )
			((JTable) c ).setRowHeight( c.getFont().getSize() + 6 );
	}

	public void update( Observable o, Object arg )
	{
		if ( o == server.getModel() )
		{
			updateStatus();
			refreshFilelist();
			updatePeerNumber();
		}
	}

	public void updateStatus()
	{
		setTitle(
			String.format(
				"Server - Listening on IP %s (port %s)",
				server.getIp().getHostAddress(),
				server.getPort()
			)
		);
	}

	public void updatePeerNumber()
	{
		int n = server.getModel().getNumberOfClientSessions();

		filesTabTitle.setText("Shared files ("+ server.getModel().getFilelist().length +")");
		
		status.setText(
			String.format(
				"%s peer%s connected",
				n,
				(n>1?"s":"")
			)
		);
	}
	
	public void refreshFilelist()
	{
		// update the shared files list !!
		String[][] fileList = server.getModel().getFilelist();
		
		for (int i = 0; i < fileList.length; i++)
		{
			fileList[i] = new String[] {
				fileList[i][0],
				server.getModel().getClientIp( Integer.parseInt(fileList[i][0]) ).getHostAddress(),
				fileList[i][1]
			};
		}
		
		System.out.println( "Updating GUI filelist..." );
		
		Timer t = new Timer(0, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				filesModel.setDataVector( fileList, filesColumns );
				System.out.println( "Updated!" );
			}
		});
		t.setRepeats(false);
		t.start();
		
		//filesModel.setDataVector( fileList, filesColumns );
	}
	
}
