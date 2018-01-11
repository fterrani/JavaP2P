/**
Project name: JavaP2P 
File name: ClientModel.java
Author: Fabien
Date of creation: 5 janv. 2018
 */

package client;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import common.ConvenienceObservable;

public class ClientModel extends ConvenienceObservable implements Observer
{
	// ShareClient data
	private File shareFolder;
	private int clientID = 0; // received from the server
	
	// List of shared files for each client ID
	private Map<Integer, PeerFile[]> listFileFromServer = new HashMap<Integer, PeerFile[]>();
	
	// PeerServer data
	private ArrayList<PeerUpload> uploads = new ArrayList<>();
	
	// PeerClient data
	private ArrayList<PeerDownload> downloads = new ArrayList<>();
	
	public static final int PORT_PEER_SERVER = 60000;
	
	public ClientModel( File shareFolder )
	{
		this.shareFolder = shareFolder;
	}

	public void update( Observable o, Object arg )
	{
		// ClientModel warns its observers (in our case ClientFrame) about downloads and uploads
		changeAndNotify( o );
	}
	
	public File getShareFolder() {
		return shareFolder;
	}

	public void setShareFolder(File shareFolder)
	{
		this.shareFolder = shareFolder;
		changeAndNotify("share_folder");
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID)
	{
		this.clientID = clientID;
		changeAndNotify("client_id");
	}
	
	public Map<Integer, PeerFile[]> getListFileFromServer() {
		return listFileFromServer;
	}
	
	// We create a new list from a set of IDs, and a set of String arrays
	public void setListFileFromServer( int[] clientIds, PeerFile[][] fileNames )
	{
		int n = Math.min( clientIds.length, fileNames.length );
		
		this.listFileFromServer.clear();
		
		for (int i = 0; i < n; i++)
		{
			if (clientIds[i] != clientID)
				this.listFileFromServer.put( clientIds[i], fileNames[i] );
		}
		
		changeAndNotify("server_filelist");
	}

	public ArrayList<PeerUpload> getUploads() {
		return uploads;
	}
	
	public void addNewUpload( PeerUpload upload )
	{
		uploads.add( upload );
		upload.addObserver( this );
		
		// We start observing the upload's progress
		changeAndNotify( "uploads" );
	}

	public ArrayList<PeerDownload> getDownloads() {
		return downloads;
	}

	public void addNewDownload( PeerDownload download )
	{
		downloads.add( download );
		download.addObserver( this );
		
		 // We start observing the download's progress
		changeAndNotify( "downloads" );
	}
}
