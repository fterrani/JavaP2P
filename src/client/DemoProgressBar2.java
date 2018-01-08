/**
Project name: JavaP2P 
File name: DemoProgressBar.java
Author: Fabien
Date of creation: 6 janv. 2018
 */

package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.ProgressBarUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class DemoProgressBar2
{
	public CustomProgressBar[] bars;
	
	public DemoProgressBar2 ()
	{
		bars = new CustomProgressBar[ 8 ];
	
		JPanel p = new JPanel();
		p.setLayout( new BoxLayout(p, BoxLayout.Y_AXIS) );
		p.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20) );
		
		for (int i = 0; i < bars.length; i++)
		{
			bars[i] = new CustomProgressBar( randName(), 0 );
			
			if (i > 0)
				bars[i].setBorder( BorderFactory.createEmptyBorder(12, 0, 0, 0));
			
			p.add( bars[i] );
		}
		
		
		Timer t = new Timer();
		t.schedule(new TimerTask()
		{
			public void run()
			{
				randomModel();
			}
		}, 0, 500 );
	}
	
	public  void randomModel()
	{
		int n;
		Random r = new Random();
		
		for (int i = 0; i < bars.length; i++)
		{
			n = bars[i].getValue() + r.nextInt(10);
			
			System.out.println( i + ": "+ bars[i].getValue() +" => " + n );
			bars[i].setValue( n );
		}
	}
	
	public String randName()
	{
		String[] names = new String[] {
			"a.txt",
			"lolcat.png",
			"cool_super_amazing_music.mp3",
			"great_movie.avi"
		};
		
		return names[ (int)(Math.random()*names.length) ];
	}
}
