/**
Project name: JavaP2P 
File name: ServerFrame.java
Author: Fabien
Date of creation: 4 janv. 2018
 */

package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class ServerFrame extends JFrame
{
	private Server server;
	
	private JTabbedPane tabs;
	private JPanel filesPanel;
	private JPanel logPanel;

	private JTable logTable;
	private DefaultTableModel logModel;
	private TableCellRenderer logRenderer;
	
	private JTable filesTable;
	private DefaultTableModel filesModel;
	
	
	public ServerFrame( Server _server )
	{
		server = _server;
		
		tabs = new JTabbedPane();
		filesPanel = new JPanel();
		logPanel = new JPanel();
		
		
		logModel = new DefaultTableModel( new String[] {"Type", "Message"}, 0 );
		logTable = new JTable( logModel );
		
		logTable.setDefaultRenderer( Object.class, new LogTableCellRenderer() );
		logTable.setFillsViewportHeight( true );
		logTable.setBackground( Color.BLUE );
		
		filesModel = new DefaultTableModel();
		filesTable = new JTable( filesModel );
		filesModel.setDataVector( new String[0][0], new String[] {"Client ID", "Client IP", "File name"});
		
		filesPanel.setLayout( new BorderLayout() );
		filesPanel.add( filesTable, BorderLayout.CENTER );
		
		JScrollPane sp = new JScrollPane( logTable );
		sp.setBackground( Color.RED );
		
		//logTable.setPreferredSize(new Dimension(2000, 2000));
		//logTable.setMaximumSize(new Dimension(2000, 2000));
		
		logPanel.setBackground( Color.GREEN );
		logPanel.add( sp, BorderLayout.CENTER );
		
		
		tabs.addTab( "File sharing", filesPanel );
		tabs.addTab( "Logs", logPanel );
		
		add( tabs, BorderLayout.CENTER );
		
		setTitle("Server");
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		setSize( new Dimension(1000, 600) );
		setLocationRelativeTo( null );
		

		addLogMessage( new LogRecord( Level.INFO, "Information message") );
		addLogMessage( new LogRecord( Level.WARNING, "Warning message") );
		addLogMessage( new LogRecord( Level.SEVERE, "Severe message") );
		
		//logTable.setModel( logModel );
	}
	
	private void addLogMessage( LogRecord rec )
	{
		String type = "????";
		
		if ( rec.getLevel() == Level.INFO )
			type = "INFO";
		else if ( rec.getLevel() == Level.WARNING )
			type = "WARN";
		else if (rec.getLevel() == Level.SEVERE )
			type = "EROR";
		
		logModel.addRow( new String[]
		{
			type,
			rec.getMessage()
		});
	}
}
