package com.automic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.xml.sax.SAXException;

import com.automic.factories.FactoryBroker;
import com.automic.objects.ObjectBroker;
import com.automic.tests.Tests;
import com.uc4.api.objects.UC4Object;
import com.uc4.api.objects.Variable;
import com.uc4.communication.Connection;


public class GoAutomic {

	/**   
	 * this class is only provided as an example!! Do NOT put your code in it...
	 * Your code belongs in a seperate Java App which references the content of this framework only..
	 */

		public static void main(String argv[]) throws IOException {
			
			String AEHostnameOrIP = "172.16.148.35";
			int AECPPrimaryPort = 2217;
			int AEClientNumber = 340; //5; // 330;
			String AEUserLogin = "JSM"; //"ARA"; //"BSP";
			String AEUserDepartment = "AUTOMIC"; //"ARA"; //"AUTOMIC";
			String AEUserPassword = "automic"; //"oneAutomation";
			char AEMessageLanguage = 'E';
			
			AECredentials myClientTarget = new AECredentials(AEHostnameOrIP,AECPPrimaryPort,AEClientNumber,AEUserDepartment,AEUserLogin,AEUserPassword,AEMessageLanguage);
			ConnectionManager mgrTarget = new ConnectionManager();
			Connection connSource = mgrTarget.connectToClient(myClientTarget);
			
			ObjectBroker Objbroker = new ObjectBroker(connSource,false);

			ArrayList<UC4Object> arr = Objbroker.jobs.getAllJobs();
			Iterator<UC4Object> it = arr.iterator();
			while(it.hasNext()){
				System.out.println(it.next().getName());
			}
		
			// example below for Factories (cross client operations)
			//FactoryBroker FactBroker = new FactoryBroker(collection,false);
			//FactBroker.exportImportFactory.CopyFolderContentBetweenClients(connSource, "0330/1_WORKLOAD_AUTOMATION/SALES.REPORTING/WORKFLOWS", connTarget, "0340/1_WORKLOAD_AUTOMATION/SALES.REPORTING/WORKFLOWS");
	
		}
}