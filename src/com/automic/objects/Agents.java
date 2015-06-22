package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;

import com.uc4.api.UC4HostName;
import com.uc4.api.systemoverview.AgentListItem;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.AgentList;
import com.uc4.communication.requests.StartHost;
import com.uc4.communication.requests.TerminateHost;

public class Agents extends ObjectTemplate{

	public Agents(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public void startAgent(String AgentName) throws IOException{
		UC4HostName agent = new UC4HostName(AgentName);
		StartHost req = new StartHost(agent);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(req.getMessageBox());
		}
	}
	
	public void shutdownAgent(String AgentName) throws IOException{
		UC4HostName agent = new UC4HostName(AgentName);
		TerminateHost req = new TerminateHost(agent);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(req.getMessageBox());
		}
	}
	
	public boolean isAgentActive(AgentListItem agent) throws IOException {
		return agent.isActive();
	}
	
	public ArrayList<AgentListItem> getAgentList() throws IOException {
		return getAgentListWithNameAndTypeFilter(".*",".*");
	}
	public ArrayList<AgentListItem> getAgentListWithNameFilter(String NameFilter) throws IOException {
		return getAgentListWithNameAndTypeFilter(NameFilter,".*");
	}
	public ArrayList<AgentListItem> getAgentListWithTypeFilter(String TypeFilter) throws IOException {
		return getAgentListWithNameAndTypeFilter(".*",TypeFilter);
	}
	public ArrayList<AgentListItem> getAgentListWithNameAndTypeFilter(String NameFilter, String TypeFilter) throws IOException {
		ArrayList<AgentListItem> objList = new ArrayList<AgentListItem>();
		AgentList list = new AgentList();
		connection.sendRequestAndWait(list);
		if (list.getMessageBox() != null) {
		}
		for (AgentListItem item : list) {
			if(item.getName().toString().matches(NameFilter) && item.getJclVariant().matches(TypeFilter)){
			objList.add(item);	
			}
		}
		return objList;
	}
}