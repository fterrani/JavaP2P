
/**
Project name: DP_ProjectP2P 
File name: MainFrameTest.java
Author: Célia Ahmad
Date of creation: 24 déc. 2017
 */

package gui;

import client.Client;

public class MainFrameTest {

	public static void main(String[] args) {
		Client cl = new Client();
		ClientFrame c = new ClientFrame(cl);
		cl.addObserver(c);
		cl.connectToServer();
	}

}
