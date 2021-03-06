/**
Project name: JavaP2P 
File name: LogTable.java
Author: Fabien
Date of creation: 5 janv. 2018
 */

package server.gui;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

// This class stores and manages a JTable containing logs
// It contains all log records added with addRecord().
// It also contains a handler that warns the JTable when a new record is inserted. (see the private class LogTableHandler below)
public class LogTable
{
	private static SimpleDateFormat logTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	private ArrayList<LogRecord> records;
	private DefaultTableModel model;
	private JTable table;
	private LogTableHandler handler;
	
	public LogTable()
	{
		records = new ArrayList<>();
		
		model = new DefaultTableModel( new String[] {"Date and time", "Type", "Message"}, 0 );
		table = new JTable( model );
		table.setDefaultRenderer( Object.class, new LogTableCellRenderer() );
		
		handler = new LogTableHandler();
	}
	
	// Adds a new LogRecord to the table
	public void addRecord( LogRecord record )
	{
		records.add( record );
		
		String type = "????";
		
		if ( record.getLevel() == Level.INFO )
			type = "INFO";
		else if ( record.getLevel() == Level.WARNING )
			type = "WARNING";
		else if ( record.getLevel() == Level.SEVERE )
			type = "ERROR";
		
		model.addRow( new String[]
		{
			logTimeFormat.format( new Date(record.getMillis()) ),
			type,
			record.getMessage()
		});
	}

	public JTable getTable()
	{
		return table;
	}
	
	public Handler getHandler()
	{
		return handler;
	}
	
	// A customized CellRenderer that
	private class LogTableCellRenderer extends DefaultTableCellRenderer
	{
		public LogTableCellRenderer()
		{
			super();
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			LogRecord record = records.get(row);
			
			if ( record != null )
			{
				Level level = record.getLevel();
				
				if (level == Level.INFO) c.setBackground( new Color(0.90f, 0.95f, 1.0f) );
				else if (level == Level.WARNING) c.setBackground( new Color(1.0f, 0.95f, 0.55f) );
				else if (level == Level.SEVERE) c.setBackground( new Color(1.0f, 0.75f, 0.75f) );
			}
			
			return c;
		}
	}
	
	// A custom log handler designed to work with a LogTable instance
	private class LogTableHandler extends Handler
	{
		public LogTableHandler()
		{
			super();
		}
		
		public void close() throws SecurityException {}
		public void flush() {}

		public void publish( LogRecord record )
		{
			LogTable.this.addRecord( record );
		}
	}
}
