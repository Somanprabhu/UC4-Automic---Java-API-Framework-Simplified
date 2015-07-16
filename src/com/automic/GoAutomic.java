package com.automic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.automic.objects.ObjectBroker;
import com.uc4.api.FolderListItem;
import com.uc4.api.objects.Client;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.UC4Object;
import com.uc4.api.systemoverview.AgentGroupListItem;
import com.uc4.api.systemoverview.AgentListItem;
import com.uc4.api.systemoverview.ClientListItem;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.FolderList;


public class GoAutomic {

	/**   
	 * this class is only provided as an example!! Do NOT put your code in it...
	 * Your code belongs in a seperate Java App which references the content of this framework only..
	 */

		public static void main(String argv[]) throws IOException {
			
			String AEHostnameOrIP = "192.168.1.179";
			int AECPPrimaryPort = 2217;
			int AEClientNumber = 0; //5; // 330;
			String AEUserLogin = "UC"; //"ARA"; //"BSP";
			String AEUserDepartment = "UC"; //"ARA"; //"AUTOMIC";
			String AEUserPassword = "UC"; //"oneAutomation";
			char AEMessageLanguage = 'E';
			
			AECredentials myClientTarget = new AECredentials(AEHostnameOrIP,AECPPrimaryPort,AEClientNumber,AEUserDepartment,AEUserLogin,AEUserPassword,AEMessageLanguage);
			ConnectionManager mgrTarget = new ConnectionManager();
			Connection connSource = mgrTarget.connectToClient(myClientTarget);
			
			ObjectBroker Objbroker = new ObjectBroker(connSource,false);

			//ArrayList<UC4Object> arr = Objbroker.jobs.getAllJobs();
			//Iterator<UC4Object> it = arr.iterator();
			//while(it.hasNext()){
				//System.out.println(it.next().getName());
		//	}
			
			Objbroker.clients.displayClientList();;
		
			ArrayList<AgentListItem> agents = Objbroker.agents.getAgentList();
			for(int i =0;i< agents.size();i++){
				AgentListItem agent = agents.get(i);
				Objbroker.agents.displayPermissionsForAgent(agent.toString());
			}
			
		
	
			// example below for Factories (cross client operations)
			//FactoryBroker FactBroker = new FactoryBroker(collection,false);
			//FactBroker.exportImportFactory.CopyFolderContentBetweenClients(connSource, "0330/1_WORKLOAD_AUTOMATION/SALES.REPORTING/WORKFLOWS", connTarget, "0340/1_WORKLOAD_AUTOMATION/SALES.REPORTING/WORKFLOWS");
	
		}
}