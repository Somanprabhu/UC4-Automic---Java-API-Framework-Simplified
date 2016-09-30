package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.HostGroup;
import com.uc4.api.objects.HostGroupItem;
import com.uc4.api.objects.UC4Object;
import com.uc4.api.systemoverview.AgentListItem;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.AgentGroupSimulation;

public class Hostgroups extends ObjectTemplate{

	public Hostgroups(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public HostGroup getHostgroupFromObject(UC4Object object){return (HostGroup) object;}
	public ArrayList<UC4Object> getAllHostgroups() throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.HOSTG);
	}
	public ArrayList<UC4Object> getAllHostgroupsWithFilter(String filter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.HOSTG,filter);
	}
	
	public void setAllMode(String hostgroup, boolean isParallel, boolean isForce, int ParallelLimit) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		hg.setExecuteOnAllHosts();
		hg.setEnforced(isForce);
		//hg.setHostGroupType("WINDOWS");
		hg.setMaxParallelJobs(ParallelLimit);
		broker.common.saveAndCloseObject(hg);
	}
	
	public void setLoadDependentMode(String hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		hg.setExecuteLoadDependent();
		broker.common.saveAndCloseObject(hg);
	}
	
	public void setAnyMode(String hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		hg.setExecuteOnAnyHost();;
		broker.common.saveAndCloseObject(hg);
	}
	
	public void setFirstMode(String hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		hg.setExecuteOnFirstHost();;
		broker.common.saveAndCloseObject(hg);
	}
	
	public void setNextMode(String hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		hg.setExecuteOnNextHost();
		broker.common.saveAndCloseObject(hg);
	}
	
	public void setType(String hostgroup, String HostType) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		hg.setHostGroupType(HostType);
		broker.common.saveAndCloseObject(hg);
	}
	
	public void setAllMode(HostGroup hostgroup, boolean isParallel, boolean isForce, int ParallelLimit) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.setExecuteOnAllHosts();
		hostgroup.setEnforced(isForce);
		//hg.setHostGroupType("WINDOWS");
		hostgroup.setMaxParallelJobs(ParallelLimit);
		broker.common.saveAndCloseObject(hostgroup);
	}
	
	public void setLoadDependentMode(HostGroup hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.setExecuteLoadDependent();
		broker.common.saveAndCloseObject(hostgroup);
	}
	
	public void setAnyMode(HostGroup hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.setExecuteOnAnyHost();
		broker.common.saveAndCloseObject(hostgroup);
	}
	
	public void setFirstMode(HostGroup hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.setExecuteOnFirstHost();
		broker.common.saveAndCloseObject(hostgroup);
	}
	
	public void setNextMode(HostGroup hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.setExecuteOnNextHost();
		broker.common.saveAndCloseObject(hostgroup);
	}
	
	public void setType(HostGroup hostgroup, String HostType) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.setHostGroupType(HostType);
		broker.common.saveAndCloseObject(hostgroup);
	}
	
	public AgentGroupSimulation simulateHostGroup(String hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		AgentGroupSimulation obj = new AgentGroupSimulation(new UC4ObjectName(hostgroup));
		broker.common.sendGenericXMLRequestAndWait(obj);
		return obj;
	}
	
	public void addHostToHostgroup(String hostgroup, String Hostname) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		HostGroupItem newItem = new HostGroupItem(Hostname);
		hg.addHost(newItem);
		broker.common.saveAndCloseObject(hg);
	}
	public void clearHosts(HostGroup hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.clear();
		broker.common.saveAndCloseObject(hostgroup);
	}
	
	public void addHostItemToHostgroup(HostGroup hostgroup, HostGroupItem item) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.addHost(item);
		broker.common.saveAndCloseObject(hostgroup);
	}
	
	public void addHostsToHostgroup(String hostgroup, String HostnameFilter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		addHostsToHostgroup(hg,HostnameFilter);
	}
	
	public void addHostsToHostgroup(HostGroup hostgroup, String HostnameFilter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		ArrayList<AgentListItem> agentList = broker.agents.getAgentListWithNameFilter(HostnameFilter);
		for(int i=0;i<agentList.size();i++){
			HostGroupItem newItem = new HostGroupItem(agentList.get(i).getName().getName());
			hostgroup.addHost(newItem);
		}
		broker.common.saveAndCloseObject(hostgroup);
	}
	
	public void addHostsToHostgroup(HostGroup hostgroup, ArrayList<AgentListItem> agentList) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		for(int i=0;i<agentList.size();i++){
			HostGroupItem newItem = new HostGroupItem(agentList.get(i).getName().getName());
			hostgroup.addHost(newItem);
		}
		broker.common.saveAndCloseObject(hostgroup);
	}
	
	// Method below might seem a bit complex.. however it is necessary because the Java API provides no easy way to remove a host 
	// from a host group.
	// the idea: 
	// 1- extract all existing hosts from hostgroup
	// 2- remove the desired host from the same list
	// 3- clear the content of the hostgroup
	// 4- iterate over the new list and add all hosts to the hostgroup
	
	public void removeHostFromHostgroup(String hostgroup, String Hostname) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		ArrayList<HostGroupItem> finalList = new ArrayList<HostGroupItem>();
		
		Iterator<HostGroupItem> it = hg.hosts();
		
		while (it.hasNext()){
			HostGroupItem item = it.next();
			if(!item.getName().equalsIgnoreCase(Hostname)){
				finalList.add(item);
			}
		}
		hg.clear();
		for(int i=0;i<finalList.size();i++){
			hg.addHost(finalList.get(i));
		}
		broker.common.saveAndCloseObject(hg);
	}
	
	public void removeHostsFromHostgroup(String hostgroup, String HostnameFilter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		removeHostsFromHostgroup(hg,HostnameFilter);
	}
	
	public void removeHostsFromHostgroup(HostGroup hostgroup, String HostnameFilter) throws IOException{
		ObjectBroker broker = getBrokerInstance();

		ArrayList<HostGroupItem> finalList = new ArrayList<HostGroupItem>();
		
		Iterator<HostGroupItem> it = hostgroup.hosts();
		
		while (it.hasNext()){
			HostGroupItem item = it.next();
			if(!item.getName().matches(HostnameFilter)){
				finalList.add(item);
			}
		}
		hostgroup.clear();
		for(int i=0;i<finalList.size();i++){
			hostgroup.addHost(finalList.get(i));
		}
		broker.common.saveAndCloseObject(hostgroup);
	}
	public ArrayList<HostGroupItem> getListOfMatchingHosts(HostGroup hostgroup, String HostnameFilter) throws IOException{

		ArrayList<HostGroupItem> MatchingList = new ArrayList<HostGroupItem>();
		
		Iterator<HostGroupItem> it = hostgroup.hosts();
		
		while (it.hasNext()){
			HostGroupItem item = it.next();
			if(item.getName().matches(HostnameFilter)){
				MatchingList.add(item);
			}
		}
		return MatchingList;
	}
}
