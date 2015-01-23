package com.automic.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXException;

import com.automic.AECredentials;
import com.automic.ConnectionManager;
import com.automic.factories.FactoryBroker;
import com.automic.objects.Jobs;
import com.automic.objects.ObjectBroker;
import com.uc4.api.FolderListItem;
import com.uc4.api.SearchResultItem;
import com.uc4.api.Template;
import com.uc4.api.VersionControlListItem;
import com.uc4.api.objects.DocuContainer;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.Job;
import com.uc4.api.objects.Login;
import com.uc4.api.objects.LoginDefinition;
import com.uc4.api.objects.UC4Object;
import com.uc4.api.systemoverview.ServerListItem;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.FolderList;
import com.uc4.communication.requests.ServerList;
import com.uc4.communication.requests.VersionControlList;

public class Tests {

	/**   
	 * 
	 * tests can be run here.
	 * If you are adding methods to this framework, please test them here!
	 * @throws SAXException 
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
