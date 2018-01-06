/**
Project name: JavaP2P 
File name: Trucs.java
Author: Fabien
Date of creation: 5 janv. 2018
 */

package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DemoJTable
{
	public static String[] columns = new String[] {"Groupe sanguin", "Taille (cm)", "Poids (kg)"};
	
	public static void main( String[] args )
	{
		DefaultTableModel model = new DefaultTableModel( columns, 0 );
		JTable table = new JTable( model );
		
		
		JFrame frame = new JFrame();
		frame.add( new JScrollPane(table), BorderLayout.CENTER );
		frame.setPreferredSize( new Dimension(1200, 1000) );
		frame.pack();
		frame.setLocationRelativeTo( null );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setVisible( true );
		
		Timer t = new Timer();
		t.schedule(new TimerTask()
		{
			public void run()
			{
				randomModel( model );
			}
		}, 0, 1000 );
	}
	
	public static void randomModel( DefaultTableModel m )
	{
		Random r = new Random();
		int rows = r.nextInt( 60 );
		
		String[][] data = new String[ rows ][ columns.length ];
		String gs;
		Integer taille;
		Integer poids;
		
		for (int i = 0; i < data.length; i++)
		{
			gs = "";
			taille = 150 + r.nextInt( 50 );
			poids = 60 + r.nextInt( 30 );
			
			switch(r.nextInt(4))
			{
				case 0: gs = "O"; break;
				case 1: gs = "A"; break;
				case 2: gs = "B"; break;
				case 3: gs = "AB"; break;
			}
			
			data[i] = new String[] { gs, taille.toString(), poids.toString() };
		}
		
		m.setDataVector(data, columns );
	}
}
