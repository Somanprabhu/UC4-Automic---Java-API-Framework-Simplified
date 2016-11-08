package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.automic.utils.Utils;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.AgentAssignment;
import com.uc4.api.systemoverview.AgentAssignmentListItem;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.AgentAssignmentList;
import com.uc4.communication.requests.AgentGroupSimulation;
import com.uc4.communication.requests.DeleteAgentAssignment;
import com.uc4.communication.requests.ExecuteAgentAssignment;
import com.uc4.communication.requests.MoveAgentAssignment;
import com.uc4.communication.requests.PreviewAssignment;
import com.uc4.communication.requests.SaveAgentAssignment;


public class AgentAssignments extends ObjectTemplate{

	public AgentAssignments(Connection conn, boolean verbose) {
		super(conn, verbose);
	}

	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public boolean executeAssignment(String HSTAName) throws TimeoutException, IOException{
		ObjectBroker broker = getBrokerInstance();
		ExecuteAgentAssignment req = new ExecuteAgentAssignment(new UC4ObjectName(HSTAName));
		broker.common.sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("AgentAssignment Executed."));
			return true;
		}
		return false;
	}
	
	public boolean delAssignment(String HSTAName) throws TimeoutException, IOException{
		ObjectBroker broker = getBrokerInstance();
		//AgentAssignmentListItem item
		ArrayList<AgentAssignmentListItem> list = getInactiveAssignmentListAsArray();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getName().getName().equalsIgnoreCase(HSTAName)){
				DeleteAgentAssignment req = new DeleteAgentAssignment(list.get(i));
				broker.common.sendGenericXMLRequestAndWait(req);
				if (req.getMessageBox() == null) {
					Say(Utils.getSuccessString("AgentAssignment Deleted."));
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean activateAssignment(String HSTAName) throws TimeoutException, IOException{	
		return changeAssignmentStatus(HSTAName, true);
	}
	
	public boolean deactivateAssignment(String HSTAName) throws TimeoutException, IOException{
		return changeAssignmentStatus(HSTAName, false);
	}
	
	private boolean changeAssignmentStatus(String HSTAName,boolean active) throws TimeoutException, IOException{
		ObjectBroker broker = getBrokerInstance();
		ArrayList<AgentAssignmentListItem> list = getAllAssignmentListAsArray();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getName().getName().equalsIgnoreCase(HSTAName)){
				MoveAgentAssignment req = new MoveAgentAssignment(list.get(i),active);
				broker.common.sendGenericXMLRequestAndWait(req);
				if (req.getMessageBox() == null) {
					if(active){Say(Utils.getSuccessString("AgentAssignment Activated."));}
					if(!active){Say(Utils.getSuccessString("AgentAssignment Deactivated."));}
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean saveAgentAssignment(AgentAssignmentListItem[] list) throws TimeoutException, IOException{
		ObjectBroker broker = getBrokerInstance();
		SaveAgentAssignment req = new SaveAgentAssignment(list);
		broker.common.sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("AgentAssignment Saved."));
			return true;
		}
		return false;
	}

	public void addMaskToHSTA(AgentAssignment HSTA, int client, boolean read, boolean write, boolean execute, boolean selection){
		HSTA.authorizations().setHostAuthorization(client, read, write, execute, selection);
	}
	
	public void clearHSTA(AgentAssignment HSTA){
		HSTA.authorizations().clear();
	}
	
	public PreviewAssignment previewAgentAssignment(String HSTAName) throws TimeoutException, IOException{
		ObjectBroker broker = getBrokerInstance();
		AgentAssignment HSTA = (AgentAssignment) broker.common.openObject(HSTAName, true);
		PreviewAssignment req = new PreviewAssignment(HSTA);
		broker.common.sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			//Say(Utils.getSuccessString("AgentAssignment Saved."));
			return req;
		}
		return req;
	}
	
	public AgentAssignmentList getAssignmentList() throws TimeoutException, IOException{
		ObjectBroker broker = getBrokerInstance();
		AgentAssignmentList req = new AgentAssignmentList();
		broker.common.sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("AgentAssignment Executed."));
			return req;
		}
		return req;
	}
	
	public ArrayList<AgentAssignmentListItem> getActiveAssignmentListAsArray() throws TimeoutException, IOException{
		ArrayList<AgentAssignmentListItem> array = new ArrayList<AgentAssignmentListItem>();
		ObjectBroker broker = getBrokerInstance();
		AgentAssignmentList req = new AgentAssignmentList();
		broker.common.sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Iterator<AgentAssignmentListItem> it = req.activeAssignments();
			while(it.hasNext()){
				AgentAssignmentListItem item = it.next();
				array.add(item);
			}
			return array;
		}
		return array;
	}
	
	public ArrayList<AgentAssignmentListItem> getInactiveAssignmentListAsArray() throws TimeoutException, IOException{
		ArrayList<AgentAssignmentListItem> array = new ArrayList<AgentAssignmentListItem>();
		ObjectBroker broker = getBrokerInstance();
		AgentAssignmentList req = new AgentAssignmentList();
		broker.common.sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Iterator<AgentAssignmentListItem> it = req.inactiveAssignments();
			while(it.hasNext()){
				AgentAssignmentListItem item = it.next();
				array.add(item);
			}
			return array;
		}
		return array;
	}
	
	public ArrayList<AgentAssignmentListItem> getAllAssignmentListAsArray() throws TimeoutException, IOException{
		ArrayList<AgentAssignmentListItem> array = new ArrayList<AgentAssignmentListItem>();
		array.addAll(getInactiveAssignmentListAsArray());
		array.addAll(getActiveAssignmentListAsArray());
		return array;
	}
	
}
