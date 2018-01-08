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
	
	/* Je te propose une classe dans ce go�t-l�.
	 * Elle contiendrait toutes les donn�es du client,
	 * avec �ventuellement les IP, ports.
	 * mais pas les trucs "techniques", c'est-�-dire
	 * sockets, writers, readers, ...
	 * 
	 * La classe est observable et elle va donc pouvoir
	 * avertir ClientFrame lorsque les donn�es du mod�le
	 * sont mises � jour.
	 * 
	 * Attention par contre : lorsque le t�l�chargement d'un fichier
	 * est en cours, le pourcentage d'avancement de l'objet
	 * PeerDownload va changer. Or, pour que ClientModel le remarque,
	 * il va devoir lui-m�me observer les PeerDownload et PeerUpload.
	 * 
	 */
	
	// Donn�es li�es au serveur de partage (utilis�es par ShareClient)
	private File shareFolder;
	private int clientID = 0;
	private String[] listFileFromServer = new String[0];
	
	// Donn�es li�es au client lorsqu'il partage des fichiers
	// (pas oblig� d'afficher quelque chose pour �a mais c'est mieux de les mettre
	// ici au cas o�)
	private ArrayList<PeerUpload> uploads;
	
	// Donn�es li�es au client lorsqu'il t�l�charge des fichiers d'autres clients
	private ArrayList<PeerDownload> downloads;
	
	// Donn�es li�es au PeerServer
	public static final int PORT_DEFAULT = 60000;

	public void update( Observable o, Object arg )
	{
		// Envoie les PeerDownload et PeerUpload en argument aux observateurs
		changeAndNotify( o );
	}
	
	//getter and setter
	public File getShareFolder() {
		return shareFolder;
	}



	public void setShareFolder(File shareFolder) {
		this.shareFolder = shareFolder;
	}





	public int getClientID() {
		return clientID;
	}





	public void setClientID(int clientID) {
		this.clientID = clientID;
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

	// Un petite exemple avec la m�thode qui ajoute un nouveau t�l�chargement � ceux en cours
	public void addNewDownload( PeerDownload download )
	{
		downloads.add( download );
		download.addObserver( this );
		changeAndNotify( "downloads" );
	}
}
