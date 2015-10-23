package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.uc4.api.Template;
import com.uc4.api.UC4HostName;
import com.uc4.api.objects.AgentAssignment;
import com.uc4.api.objects.Authorizations;
import com.uc4.api.objects.Host;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.UC4Object;
import com.uc4.api.systemoverview.AgentGroupListItem;
import com.uc4.api.systemoverview.AgentListItem;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.AgentGroupList;
import com.uc4.communication.requests.AgentList;
import com.uc4.communication.requests.DisconnectHost;
import com.uc4.communication.requests.SetHostAuthorizations;
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
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" ++ Agent: "+AgentName+" Successfully Started.");
		}
	}
	
	public void disconnectAgent(AgentListItem item) throws IOException{
		DisconnectHost req = new DisconnectHost(item);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" ++ Agent: "+item.getName()+" Successfully Shutdown.");
		}
	}
	
	public void disconnectAgent(String AgentName) throws IOException{
		DisconnectHost req = new DisconnectHost(getAgentListItemByName(AgentName));
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" ++ Agent: "+AgentName+" Successfully Shutdown.");
		}
	}
	
	public void TerminateAgent(String AgentName) throws IOException{
		UC4HostName agent = new UC4HostName(AgentName);
		TerminateHost req = new TerminateHost(agent);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" ++ Agent: "+AgentName+" Successfully Shutdown.");
		}
	}
	
	public void createAgentClientAssignment(String AgentClientAssignmentName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(AgentClientAssignmentName, Template.HSTA, FolderLocation);
	}
	
	public AgentAssignment getAgentAssignmentFromObject(UC4Object object){return (AgentAssignment) object;}
	
	//public void createOpenAgentClientAssignment(String AgentClientAssignmentName, IFolder FolderLocation){
		// Stub method. Needs to be created!!
	//}
	
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
			System.out.println(" -- "+list.getMessageBox().getText().toString().replace("\n", ""));
		}
		for (AgentListItem item : list) {
			if(item.getName().toString().matches(NameFilter) && item.getJclVariant().matches(TypeFilter)){
			objList.add(item);	
			}
		}
		return objList;
	}
	
	public AgentListItem getAgentListItemByName(String AgentName) throws IOException {
		AgentList list = new AgentList();
		connection.sendRequestAndWait(list);
		if (list.getMessageBox() != null) {
			System.out.println(" -- "+list.getMessageBox().getText().toString().replace("\n", ""));
		}
		for (AgentListItem item : list) {
			if(item.getName().toString().equals(AgentName)){
			return item;
			}
		}
		return null;
	}
	
	public ArrayList<AgentGroupListItem> getAgentGroupList() throws TimeoutException, IOException{
		ArrayList<AgentGroupListItem> objList = new ArrayList<AgentGroupListItem>();
		AgentGroupList list = new AgentGroupList();
		connection.sendRequestAndWait(list);
		if (list.getMessageBox() != null) {
			System.out.println(" -- "+list.getMessageBox().getText().toString().replace("\n", ""));
		}
		for (AgentGroupListItem item : list) {
			//if(item.getName().toString().matches(NameFilter) && item.getJclVariant().matches(TypeFilter)){
			objList.add(item);	
			//}
		}
		return objList;
	}
	
	public Authorizations getPermissionsForAgent(String AgentName) throws IOException{
		UC4Object obj = getBrokerInstance().common.openObject(AgentName, false);
		Host host = (Host) obj; 
		return host.authorizations();
	}
	
	public void displayPermissionsForAgent(String AgentName) throws IOException{
		UC4Object obj = getBrokerInstance().common.openObject(AgentName, false);
		Host host = (Host) obj;
		AgentListItem item = getAgentListItemByName(AgentName);
		System.out.format("\n%-40s, %-8s, %-17s, %-17s, %-12s, %-15s, %-24s, %-9s, %-7s, %-8s, %-10s, %-19s, %-16s, %-19s, %-18s\n",
				"[Agent]","[Type]","[Agent Version]","[IP Adress]","[Hardware]","[Software]","[Software Version]",
				"[Client]",
				"[Read]","[Write]","[Execute]",
				"[License Category]","[License Class]","[Max Job Resource]","[Max FT Resource]");
		Iterator<Authorizations.Entry> it = host.authorizations().iterator();
		while (it.hasNext()) {
			Authorizations.Entry entry = it.next();
			System.out.format("%-40s, %-8s, %-17s, %-17s, %-12s, %-15s, %-24s, %-9s, %-7s, %-8s, %-10s, %-19s, %-16s, %-19s, %-18s\n",
					AgentName,item.getJclVariant(),item.getVersion(),item.getIpAddress(),item.getHardware(),
					item.getSoftware(), item.getSoftwareVersion(),entry.getClient(),entry.isRead(),entry.isWrite(),entry.isExecute(),
					item.getLicenseCategory(),item.getLicenseClass(),item.getMaxJobResources(),item.getMaxFileTransferResources());
		
		}
		
	}
	
	public void displayPermissionsForAllAgents(ArrayList<AgentListItem> list) throws IOException{
		System.out.println("\nTotal Number Of Agents: "+list.size());
		System.out.format("\n%-40s, %-8s, %-17s, %-17s, %-12s, %-15s, %-24s, %-9s, %-7s, %-8s, %-10s, %-19s, %-16s, %-19s, %-18s\n",
				"[Agent]","[Type]","[Agent Version]","[IP Adress]","[Hardware]","[Software]","[Software Version]",
				"[Client]",
				"[Read]","[Write]","[Execute]",
				"[License Category]","[License Class]","[Max Job Resource]","[Max FT Resource]");
		for(int i=0;i<list.size();i++){
			AgentListItem item = list.get(i);
			if(item.isAuthenticated()){
				UC4Object obj = getBrokerInstance().common.openObject(list.get(i).getName().toString(), false);
				Host host = (Host) obj;
		 //.getName().toString());
				
				Iterator<Authorizations.Entry> it = host.authorizations().iterator();
				while( it.hasNext()) {
					Authorizations.Entry entry = it.next();
					System.out.format("%-40s, %-8s, %-17s, %-17s, %-12s, %-15s, %-24s, %-9s, %-7s, %-8s, %-10s, %-19s, %-16s, %-19s, %-18s\n",
							list.get(i).getName().toString(),item.getJclVariant(),item.getVersion(),item.getIpAddress(),item.getHardware(),
							item.getSoftware(), item.getSoftwareVersion(),entry.getClient(),entry.isRead(),entry.isWrite(),entry.isExecute(),
							item.getLicenseCategory(),item.getLicenseClass(),item.getMaxJobResources(),item.getMaxFileTransferResources());
				}
			}else{
				System.out.format("%-40s, %-8s, %-17s, %-17s, %-12s, %-15s, %-24s, %-9s, %-7s, %-8s, %-10s, %-19s, %-16s, %-19s, %-18s\n",
						item.getName().toString(),item.getJclVariant(),item.getVersion(),item.getIpAddress(),item.getHardware(),
						item.getSoftware(), item.getSoftwareVersion(),"Not Auth","NA","NA","NA",
						item.getLicenseCategory(),item.getLicenseClass(),item.getMaxJobResources(),item.getMaxFileTransferResources());
			}

		}
	}
	
	public void setAgentAuthorizationsForAllAgents(int Client, boolean Read, boolean Write, boolean Execute) throws TimeoutException, IOException{
		setAgentAuthorizations("*",Client,Read,Write,Execute);
	}
	
	// the following method actually takes a filter as an AgentName (if needed..). '*' and '?' are accepted!
	public void setAgentAuthorizations(String AgentName, int Client, boolean Read, boolean Write, boolean Execute) throws TimeoutException, IOException{
		String CurrentSessionClient = connection.getSessionInfo().getClient();
		if(CurrentSessionClient.equals("0000")){
		SetHostAuthorizations req = new SetHostAuthorizations(AgentName, Client, Read, Write, Execute);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		}else{
			System.out.println("-- Error! Current Client is: " + CurrentSessionClient+", You Need to be connected with Client 0000 for this operation.");
		}
	}
	
	public void removeAgentFromClient0(int Client, String AgentName ) throws IOException{
		String CurrentSessionClient = connection.getSessionInfo().getClient();
		if(CurrentSessionClient.equals("0000")){
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(AgentName, false);
		broker.common.deleteObject(obj.getName(), false);
		}else{
			System.out.println("-- Error! Current Client is: " + CurrentSessionClient+", You Need to be connected with Client 0000 for this operation.");
		}
	}
		

}