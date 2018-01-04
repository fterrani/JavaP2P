/**
Project name: JavaP2P 
File name: ServerFrame.java
Author: Fabien
Date of creation: 4 janv. 2018
 */

package server.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
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
	
	private JTabbedPane tabs;
	private JPanel filesPanel;
	private JPanel logPanel;
	
	private JTable logTable;
	private DefaultTableModel logModel;
	private TableHandler logTableHandler;
	
	private JTable filesTable;
	private DefaultTableModel filesModel;
	
	
	public ShareServerFrame( ShareServer _server )
	{
		server = _server;
		server.getModel().addObserver( this );
		
		logModel = new DefaultTableModel( new String[] {"Date and time", "Type", "Message"}, 0 );
		logTableHandler = new TableHandler( logModel );
		
		// This will display the server's logs in a table
		server.getLogger().addHandler( logTableHandler );
		logTable = new JTable( logModel );
		logTable.setFont( logTable.getFont().deriveFont( 16f ) );
		logTable.setRowHeight( logTable.getFont().getSize() + 6 );
		logTable.setDefaultRenderer( Object.class, new LogTableCellRenderer() );
		
		filesModel = new DefaultTableModel( new String[] {"Client ID", "Client IP", "File name"}, 0 );
		filesTable = new JTable( filesModel );
		
		filesPanel = new JPanel();
		filesPanel.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20) );
		filesPanel.setLayout( new BorderLayout() );
		filesPanel.add( new JScrollPane( filesTable ), BorderLayout.CENTER );
		
		logPanel = new JPanel();
		logPanel.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20) );
		logPanel.setLayout( new BorderLayout() );
		logPanel.add( new JScrollPane( logTable ), BorderLayout.CENTER );
		
		
		tabs = new JTabbedPane();
		tabs.setFont( tabs.getFont().deriveFont(16f) );
		tabs.addTab( "Files shared by clients", filesPanel );
		tabs.addTab( "Logs", logPanel );
		
		add( tabs, BorderLayout.CENTER );
		
		setTitle("Server");
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		setPreferredSize( new Dimension(1000, 600) );
		pack();
		setLocationRelativeTo( null );
	}

	public void update( Observable o, Object arg )
	{
		if ( o instanceof ShareServerModel )
		{
			ShareServerModel model = (ShareServerModel) o;
			
			// update the shared files list !!
		}
	}
	
	private class TableHandler extends Handler
	{
		private SimpleDateFormat logTimeFormat;
		private DefaultTableModel model;
		
		public TableHandler( DefaultTableModel _model )
		{
			logTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			model = _model;
		}
		
		public void close() throws SecurityException
		{
		}

		public void flush()
		{
		}

		public void publish( LogRecord record )
		{
			String type = "????";
			
			if ( record.getLevel() == Level.INFO )
				type = "INFO";
			else if ( record.getLevel() == Level.WARNING )
				type = "WARN";
			else if ( record.getLevel() == Level.SEVERE )
				type = "EROR";
			
			model.addRow( new String[]
			{
				logTimeFormat.format( new Date(record.getMillis()) ),
				type,
				record.getMessage()
			});
		}
	}
}
