/**
Project name: JavaP2P 
File name: ConvenienceObserver.java
Author: Fabien
Date of creation: 5 janv. 2018
 */

package common;

import java.util.Observable;
import java.util.Observer;

// This class extends Observable and makes some convenience changes to it
public class ConvenienceObservable extends Observable
{
	public void addObserver( Observer o )
	{
		super.addObserver( o );
		o.update( this, null );
	}
	
	// Marks the Observable as changed and notifies Observers immediately (no argument)
	protected void changeAndNotify()
	{
		setChanged();
		notifyObservers();
	}
	
	// Marks the Observable as changed and notifies Observers immediately (with argument)
	protected void changeAndNotify( Object obj )
	{
		setChanged();
		notifyObservers( obj );
	}
}
