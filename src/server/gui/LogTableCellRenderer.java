/**
Project name: JavaP2P 
File name: LogTableCellRenderer.java
Author: Fabien
Date of creation: 4 janv. 2018
 */

package server.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class LogTableCellRenderer extends DefaultTableCellRenderer
{
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		Object objValue = table.getModel().getValueAt(row, 1);
		
		if (objValue != null)
		{
			String type = objValue.toString();
			
			switch (type)
			{
				case "INFO": c.setBackground( new Color(0.90f, 0.95f, 1.0f) ); break;
				case "WARN": c.setBackground( new Color(1.0f, 0.95f, 0.55f) ); break;
				case "EROR": c.setBackground( new Color(1.0f, 0.75f, 0.75f) ); break;
			}
		}
		
		return c;
	}
}
