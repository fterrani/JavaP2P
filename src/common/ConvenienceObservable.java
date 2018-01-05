/**
Project name: JavaP2P 
File name: ConvenienceObserver.java
Author: Fabien
Date of creation: 5 janv. 2018
 */

package common;

import java.util.Observable;
import java.util.Observer;

public class ConvenienceObservable extends Observable
{
	public void addObserver( Observer o )
	{
		super.addObserver( o );
		o.update( this, null );
	}
	
	protected void changeAndNotify()
	{
		setChanged();
		notifyObservers();
	}
}
