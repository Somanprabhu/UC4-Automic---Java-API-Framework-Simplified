package com.automic.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.automic.AECredentials;
import com.automic.ConnectionManager;
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
	 * 
	 */
	
	public Tests(AECredentials myClient) throws IOException{

		
		
		// 1- First, use the static connection object to initiate the connection (see ConnectionManager class for details)
		
		ConnectionManager mgr = new ConnectionManager();
		Connection conn = mgr.connectToClient(myClient);
		System.exit(0);
		// 2- initialize an Object broker object, it gives you access to all object methods
		
		ObjectBroker broker = new ObjectBroker(conn,false);
		IFolder myFolder = broker.folders.getFolderByName("BSP.TESTS");
		

		//broker.users.moveUserToClient("BSP.LALA", "BSP", 330);
		
		
	//	ArrayList<IFolder> folders = broker.folders.getAllFolders(true);
		//for(IFolder folder : folders){
			
		//	System.out.println(folder.getName()+":"+folder.getType());
			
		//}
		
		
		//GetDatabaseInfo info = broker.automationEngine.getCentralDBInfo();
		//String version = broker.automationEngine.returnServerVersion();
		
		// 3- use the Object Broker!
		//IFolder myFolder = broker.folders.getFolderByName("BSP.WORKFLOWS");
		//System.out.println(myFolder.getName());
		
		//broker.jobPlans.createSampleWorkPlan(myFolder);
		
		//UC4Object obj = broker.common.openObject("JOBS.WIN.BSP.DEMO.N1", true);
		
		//Job job = (Job) obj;

	
		//broker.common.getObjectVersions("JOBS.WIN.BSP.DEMO.N1");
		
		
		//IFolder folder = broker.folders.getFolderByName("BSP.JOBS");
		//broker.common.replaceObject("JOBS.WIN.BSP.DAY2.EX1.2", "JOBS.WIN.BSP.DAY2.EX1.1", folder);
		//ArrayList<IFolder> mylist = broker.folders.getAllFolders(true);
		
		/*for(int i=0;i<mylist.size();i++){
			System.out.println("+++ Content of: "+mylist.get(i).getName()+":"+mylist.get(i).fullPath());
			FolderList itemlist = broker.folders.getFolderContent(mylist.get(i));
			Iterator<FolderListItem> it = itemlist.iterator();
			while(it.hasNext()){
				FolderListItem item = it.next();
				System.out.println("    --> "+item.getName());
			}
		}
		*/
		//utils.getServerVersion();
		//utils.getActivityWindowContent();

		//IFolder myFolder = broker.folders.getFolderByName("/BSP.OBJECTS/BSP.JOBS");

		//String jobName = broker.jobs.createRandomJobInFolder(Template.JOBS_WIN,myFolder);
		//UC4Object object = broker.common.openObject(jobName, true);
		//broker.jobs.setJobPriority(object,34);
		//broker.jobs.deleteJob(new UC4ObjectName(object.getName()));
		
		//ArrayList<UC4Object> myJobs = broker.common.getAllObjects(ObjectTypeEnum.JOBS);
		//broker.jobPlans.getAllJobPlansWithFilter(".*");
		//broker.automationEngine.getAgentList();
		// 3 - Do not forget to close your connection when finished
		conn.close();

	}
	
}
