package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;

import com.uc4.api.systemoverview.AgentListItem;
import com.uc4.api.systemoverview.ServerListItem;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.AgentList;
import com.uc4.communication.requests.GetDatabaseInfo;
import com.uc4.communication.requests.ResumeClient;
import com.uc4.communication.requests.ServerList;
import com.uc4.communication.requests.StartServer;
import com.uc4.communication.requests.SuspendClient;

public class AutomationEngine extends ObjectTemplate{

	public AutomationEngine(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public ServerList showWPandCPList() throws IOException{
		ServerList req = new ServerList();
		connection.sendRequestAndWait(req);
		if(req.getMessageBox()!=null){
			System.out.println(req.getMessageBox());
		}
		return req;
		
	}
	// Below: Process name should be the full WP or CP process name, ex: UC4#WP002 (which can be obtained by method showWPandCPList())
	public void startCPorWP(String ProcessName) throws IOException{
		StartServer req = new StartServer(ProcessName);
		connection.sendRequestAndWait(req);
		if(req.getMessageBox()!=null){
			System.out.println(req.getMessageBox());
		}
	}
	
	public void resumeClient() throws IOException{
		ResumeClient req = new ResumeClient();
		connection.sendRequestAndWait(req);
		if(req.getMessageBox()!=null){
			System.out.println(req.getMessageBox());
		}
	}
	
	public void suspendClient() throws IOException{
		SuspendClient req = new SuspendClient();
		connection.sendRequestAndWait(req);
		if(req.getMessageBox()!=null){
			System.out.println(req.getMessageBox());
		}
	}
	
	// Get the version of CPs, WPs etc.
	public void getServerVersion() throws IOException {
		ServerList list = new ServerList();
		connection.sendRequestAndWait(list);
		for (ServerListItem item : list) {
			System.out.println("INFO:"+item.getName()+":"+item.getType()+":"+item.getVersion());	
		}
	}
	public String returnServerVersion() throws IOException{
		ServerList list = new ServerList();
		connection.sendRequestAndWait(list);
		for (ServerListItem item : list) {
			return item.getVersion();	
		}
		return null;
		
	}
	
	public GetDatabaseInfo getCentralDBInfo() throws IOException{
		GetDatabaseInfo info = new GetDatabaseInfo();
		connection.sendRequestAndWait(info);
		if (info.getMessageBox() != null) {
			System.err.println(info.getMessageBox().getText());
		}
		return info;
	}
}
