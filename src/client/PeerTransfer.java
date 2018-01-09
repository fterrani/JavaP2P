/**
Project name: JavaP2P 
File name: PeerTransfer.java
Author: Fabien
Date of creation: 5 janv. 2018
 */
package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import common.ConvenienceObservable;

// File upload and download class
public class PeerTransfer extends ConvenienceObservable implements Runnable
{
	private OutputStream dest;
	private InputStream source;
	
	private double progress;
	private int transferedBytes = 0;
	private boolean error = false;
	private int fileSize;
	
	public void run()
	{
		try
		{
			// File transfer happens here
			byte[] buffer = new byte[1024 * 32]; // 32 KB chunks
			int readBytes = -1;
			
			// We make sure to notify observers that we start
			setProgress(0);
			
			// We keep reading as long as we haven't received all bytes
			while( transferedBytes < fileSize )
			{
				readBytes = source.read(buffer);
				
				if ( readBytes != -1 )
				{
					// Writing and flushing to the destination
					dest.write( buffer, 0, readBytes );
					dest.flush();
					
					// We update PeerTransfer's data
					addTransferedBytes( readBytes );
				}
			}
			
			// We make sure to notify observers that we are done
			setProgress(1);
			
			// We close both streams. If a socket is bound to one of these, it will automatically be closed
			System.out.println( "Closing PeerTransfer streams..." );
			source.close();
			dest.close();
			System.out.println( "Streams closed." );
		}
		catch( IOException ioe )
		{
			System.out.println( "Error while transfering!" );
			setError( true );
		}
	}
	
	public PeerTransfer( InputStream inStream, OutputStream outStream, int _fileSize )
	{
		fileSize = _fileSize;
		source = inStream;
		dest = outStream;
	}
	
	public void addTransferedBytes( int bytes )
	{
		transferedBytes += bytes;
		setProgress( ((float) transferedBytes) / fileSize );
	}
	
	public double getProgress()
	{
		return progress;
	}
	
	public boolean errorOccured()
	{
		return error;
	}
	
	public boolean isFinished()
	{
		return (progress >= 1.0);
	}
	
	private void setError( boolean _error )
	{
		error = _error;
		changeAndNotify();
	}

	private void setProgress( float progress )
	{
		this.progress = progress;
		changeAndNotify();
	}
}

