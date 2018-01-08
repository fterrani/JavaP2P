/**
Project name: JavaP2P 
File name: ClientModel.java
Author: Fabien
Date of creation: 5 janv. 2018
 */

package client;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import common.ConvenienceObservable;

public class ClientModel extends ConvenienceObservable implements Observer
{
	// ShareClient data
	private File shareFolder;
	private int clientID = 0;
	private String[] listFileFromServer = new String[0];
	
	// PeerServer data
	private ArrayList<PeerUpload> uploads = new ArrayList<>();
	
	// PeerClient data
	private ArrayList<PeerDownload> downloads = new ArrayList<>();
	
	// Données liées au PeerServer
	public static final int PORT_PEER_SERVER = 60000;
	
	public ClientModel( File shareFolder )
	{
		this.shareFolder = shareFolder;
	}

	public void update( Observable o, Object arg )
	{
		// Envoie les PeerDownload et PeerUpload en argument aux observateurs
		changeAndNotify( o );
	}
	
	//getter and setter
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





	public String[] getListFileFromServer() {
		return listFileFromServer;
	}





	public void setListFileFromServer(String[] listFileFromServer)
	{
		this.listFileFromServer = listFileFromServer;
		changeAndNotify("server_filelist");
	}





	public ArrayList<PeerUpload> getUploads() {
		return uploads;
	}



	public void addNewUpload( PeerUpload upload )
	{
		uploads.add( upload );
		upload.addObserver( this );
		changeAndNotify( "uploads" );
	}

	public ArrayList<PeerDownload> getDownloads() {
		return downloads;
	}

	public void addNewDownload( PeerDownload download )
	{
		downloads.add( download );
		download.addObserver( this );
		changeAndNotify( "downloads" );
	}
}
