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
	
	/* Je te propose une classe dans ce goût-là.
	 * Elle contiendrait toutes les données du client,
	 * avec éventuellement les IP, ports.
	 * mais pas les trucs "techniques", c'est-à-dire
	 * sockets, writers, readers, ...
	 * 
	 * La classe est observable et elle va donc pouvoir
	 * avertir ClientFrame lorsque les données du modèle
	 * sont mises à jour.
	 * 
	 * Attention par contre : lorsque le téléchargement d'un fichier
	 * est en cours, le pourcentage d'avancement de l'objet
	 * PeerDownload va changer. Or, pour que ClientModel le remarque,
	 * il va devoir lui-même observer les PeerDownload et PeerUpload.
	 * 
	 */
	
	// Données liées au serveur de partage (utilisées par ShareClient)
	private File shareFolder;
	private int clientID = 1;
	private String[] listFileFromServer = new String[0];
	
	// Données liées au client lorsqu'il partage des fichiers
	// (pas obligé d'afficher quelque chose pour ça mais c'est mieux de les mettre
	// ici au cas où)
	private ArrayList<PeerUpload> uploads;
	
	// Données liées au client lorsqu'il télécharge des fichiers d'autres clients
	private ArrayList<PeerDownload> downloads;
	
	// Données liées au PeerServer
	public static final int PORT_DEFAULT = 60000;

	
	// Un petite exemple avec la méthode qui ajoute un nouveau téléchargement à ceux en cours
	public void addNewDownload( PeerDownload download )
	{
		downloads.add( download );
		download.addObserver( this );
		changeAndNotify( "downloads" );
	}
	

	public void update( Observable o, Object arg )
	{
		if ( o instanceof PeerDownload )
		{
			// On passerait la chaîne "downloads" pour avertir
			// ClientFrame qu'il doît mettre à jour la liste des
			// téléchargements (la chaîne se trouvera dans le deuxième
			// paramètre de la méthode update() de ClientFrame)
			changeAndNotify( "downloads" );
		}
		else if ( o instanceof PeerUpload )
		{
			changeAndNotify( "uploads" );
		}
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





	public void setListFileFromServer(String[] listFileFromServer) {
		this.listFileFromServer = listFileFromServer;
	}





	public ArrayList<PeerUpload> getUploads() {
		return uploads;
	}





	public void setUploads(ArrayList<PeerUpload> uploads) {
		this.uploads = uploads;
	}





	public ArrayList<PeerDownload> getDownloads() {
		return downloads;
	}





	public void setDownloads(ArrayList<PeerDownload> downloads) {
		this.downloads = downloads;
	}

}
