package com.automic.tests;

import java.io.IOException;
import org.xml.sax.SAXException;

import com.automic.AECredentials;
import com.automic.ConnectionManager;
import com.automic.factories.FactoryBroker;
import com.uc4.communication.Connection;

public class Tests {

	/**   
	 * 
	 * DO NOT USE THIS
	 * 
	 * 
	 */
	
	public Tests(AECredentials myClientSource,AECredentials myClientTarget) throws IOException, SAXException{
		
		// 1- First, use the static connection object to initiate the connection (see ConnectionManager class for details)
		
		ConnectionManager mgrSource = new ConnectionManager();
		Connection connSource = mgrSource.connectToClient(myClientSource);
		
		ConnectionManager mgrTarget = new ConnectionManager();
		Connection connTarget = mgrTarget.connectToClient(myClientTarget);

		// 2- initialize an Object broker object, it gives you access to all object methods
		Connection[] collection = new Connection[2];
		collection[0] = connSource;
		collection[1] = connTarget;
		
		FactoryBroker FactBroker = new FactoryBroker(collection,false);
		FactBroker.exportImportFactory.CopyFolderContentBetweenClients(connSource, "0330/1_WORKLOAD_AUTOMATION/SALES.REPORTING/WORKFLOWS", connTarget, "0340/1_WORKLOAD_AUTOMATION/SALES.REPORTING/WORKFLOWS");
		
		
		connSource.close();
		connTarget.close();
	}
	
}
