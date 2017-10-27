package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.automic.utils.Utils;
import com.uc4.api.systemoverview.ServerListItem;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.GetDatabaseInfo;
import com.uc4.communication.requests.GetRestEndpoints;
import com.uc4.communication.requests.GetRestEndpoints.RestEndpoint;
import com.uc4.communication.requests.ResumeClient;
import com.uc4.communication.requests.ServerList;
import com.uc4.communication.requests.StartServer;
import com.uc4.communication.requests.SuspendClient;

public class AutomationEngine extends ObjectTemplate{

	public AutomationEngine(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	@SuppressWarnings("unused")
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public ServerList showWPandCPList() throws IOException{
		ServerList req = new ServerList();
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			return req;
		}else{
			Say(Utils.getErrorString("Error: " + req.getMessageBox().getText()));
		}
		return req;
	}
	
	// Below: Process name should be the full WP or CP process name, ex: UC4#WP002 (which can be obtained by method showWPandCPList())
	public boolean startCPorWP(String ProcessName) throws IOException{
		StartServer req = new StartServer(ProcessName);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Process: "+ProcessName+" Successfully Started."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean resumeClient() throws IOException{
		ResumeClient req = new ResumeClient();
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Client: "+connection.getSessionInfo().getClient()+" Successfully Resumed."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean suspendClient() throws IOException{
		SuspendClient req = new SuspendClient();
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Client: "+connection.getSessionInfo().getClient()+" Successfully Stopped."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	// Get the version of CPs, WPs etc.
	public void displayServerVersion() throws IOException {
		ServerList list = new ServerList();
		connection.sendRequestAndWait(list);
		for (ServerListItem item : list) {
			// Bug Fix: if the 1st process of the list is turned off, it returns nothing..
			if(item.getVersion()!= null && !item.getVersion().equals("")){
				System.out.println("INFO:"+item.getName()+":"+item.getType()+":"+item.getVersion());
			}
		}
	}
	public String getServerVersion() throws IOException{
		ServerList list = new ServerList();
		connection.sendRequestAndWait(list);
		for (ServerListItem item : list) {
			// Bug Fix: if the 1st process of the list is turned off, it returns nothing..
			if(item.getVersion()!= null && !item.getVersion().equals("")){
				return item.getVersion();	
			}
		}
		return null;
	}
	
	public GetDatabaseInfo getCentralDBInfo() throws IOException{
		GetDatabaseInfo req = new GetDatabaseInfo();
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			return req;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return req;
		
	}
	
//	public ArrayList<RestEndpoint> GetRestEndpointsInfo() throws TimeoutException, IOException {
//		GetRestEndpoints req = new 	GetRestEndpoints();
//		sendGenericXMLRequestAndWait(req);
//		
//		if (req.getMessageBox() == null) {
//			Iterator<RestEndpoint> it = req.iterator();
//			ArrayList<RestEndpoint> allEndpoints = new ArrayList<RestEndpoint>();
//			while(it.hasNext()) {
//				RestEndpoint re = it.next();
//				
//				allEndpoints.add(re);
//			}
//			return allEndpoints;
//		}else{
//			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
//		}
//		return null;
//		
//	}
}
