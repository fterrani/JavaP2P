/**
Project name: JavaP2P 
File name: CustomProgressBar.java
Author: Fabien
Date of creation: 6 janv. 2018
 */

package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;


// A customized progress bar
public class CustomProgressBar extends JPanel
{
	private BasicProgressBarUI customUI = new BasicProgressBarUI()
	{
		protected Color getSelectionBackground() { return Color.BLACK; }
	    protected Color getSelectionForeground() { return Color.WHITE; }
	};
	
	private static float[] startColor = new float[] {0f, 0.5f, 1.0f};
	private static float[] endColor = new float[] {0f, 0.8f, 0.6f};
	
	private JProgressBar bar;
	private JLabel label;
	private boolean error = false;
	
	public CustomProgressBar( String text, int value )
	{
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
		
		label = new JLabel();
		label.setFont( label.getFont().deriveFont(14f) );
		label.setBorder( BorderFactory.createEmptyBorder(0, 0, 2, 0));
		
		bar = new JProgressBar(0, 100);
		bar.setStringPainted( true );
		bar.setFont( bar.getFont().deriveFont(15f) );
		bar.setBackground( new Color(0.96f, 0.96f, 0.96f) );
		bar.setUI( customUI );
		bar.setBorder (null );

		label.setAlignmentX( 0f );
		bar.setAlignmentX( 0f );
		
		add( label );
		add( bar );
		
		setText( text );
		setValue( value );
	}
	
	public void setText( String text )
	{
		label.setText( text );
	}
	
	public int getValue()
	{
		return bar.getValue();
	}
	
	public void setValue( int v )
	{
		bar.setValue( v );
		bar.setForeground( getProgressColor( 1.0f*v / bar.getMaximum()) );
	}
	
	public static Color getProgressColor( double p )
	{
		if (p<0) p = 0;
		else if (p>1) p = 1;
		
		return new Color(
			(float)( startColor[0] + (endColor[0] - startColor[0]) * p ),
			(float)( startColor[1] + (endColor[1] - startColor[1]) * p ),
			(float)( startColor[2] + (endColor[2] - startColor[2]) * p )
		);
	}
	
	public void setError( boolean _error )
	{
		error = _error;

		if (error)
		{
			bar.setForeground( new Color(0.88f, 0.13f, 0.13f) );
			bar.setString( "Error while transfering" );
		}
		
		else
		{
			bar.setForeground( getProgressColor( 1.0f*bar.getValue() / bar.getMaximum()) );
			bar.setString( null );
		}
	}
}
