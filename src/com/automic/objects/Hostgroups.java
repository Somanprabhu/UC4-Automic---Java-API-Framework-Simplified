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
	
	public boolean setAllMode(String hostgroup, boolean isParallel, boolean isForce, int ParallelLimit) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		hg.setExecuteOnAllHosts();
		hg.setEnforced(isForce);
		//hg.setHostGroupType("WINDOWS");
		hg.setMaxParallelJobs(ParallelLimit);
		return broker.common.saveAndCloseObject(hg);
	}
	
	public boolean setLoadDependentMode(String hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		hg.setExecuteLoadDependent();
		return broker.common.saveAndCloseObject(hg);
	}
	
	public boolean setAnyMode(String hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		hg.setExecuteOnAnyHost();;
		return broker.common.saveAndCloseObject(hg);
	}
	
	public boolean setFirstMode(String hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		hg.setExecuteOnFirstHost();;
		return broker.common.saveAndCloseObject(hg);
	}
	
	public boolean setNextMode(String hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		hg.setExecuteOnNextHost();
		return broker.common.saveAndCloseObject(hg);
	}
	
	public boolean setType(String hostgroup, String HostType) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		hg.setHostGroupType(HostType);
		return broker.common.saveAndCloseObject(hg);
	}
	
	public boolean setAllMode(HostGroup hostgroup, boolean isParallel, boolean isForce, int ParallelLimit) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.setExecuteOnAllHosts();
		hostgroup.setEnforced(isForce);
		//hg.setHostGroupType("WINDOWS");
		hostgroup.setMaxParallelJobs(ParallelLimit);
		return broker.common.saveAndCloseObject(hostgroup);
	}
	
	public boolean setLoadDependentMode(HostGroup hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.setExecuteLoadDependent();
		return broker.common.saveAndCloseObject(hostgroup);
	}
	
	public boolean setAnyMode(HostGroup hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.setExecuteOnAnyHost();
		return broker.common.saveAndCloseObject(hostgroup);
	}
	
	public boolean setFirstMode(HostGroup hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.setExecuteOnFirstHost();
		return broker.common.saveAndCloseObject(hostgroup);
	}
	
	public boolean setNextMode(HostGroup hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.setExecuteOnNextHost();
		return broker.common.saveAndCloseObject(hostgroup);
	}
	
	public boolean setType(HostGroup hostgroup, String HostType) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.setHostGroupType(HostType);
		return broker.common.saveAndCloseObject(hostgroup);
	}
	
	public AgentGroupSimulation simulateHostGroup(String hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		AgentGroupSimulation obj = new AgentGroupSimulation(new UC4ObjectName(hostgroup));
		broker.common.sendGenericXMLRequestAndWait(obj);
		return obj;
	}
	
	public boolean addHostToHostgroup(String hostgroup, String Hostname) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		HostGroupItem newItem = new HostGroupItem(Hostname);
		hg.addHost(newItem);
		return broker.common.saveAndCloseObject(hg);
	}
	 
	public boolean clearHosts(HostGroup hostgroup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.clear();
		return broker.common.saveAndCloseObject(hostgroup);
	}
	
	public boolean addHostItemToHostgroup(HostGroup hostgroup, HostGroupItem item) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		hostgroup.addHost(item);
		
		return broker.common.saveAndCloseObject(hostgroup);
	}
	
	public ArrayList<HostGroupItem> identifyHostItemInHostgroup(HostGroup hostgroup, HostGroupItem HGItem) throws IOException{
		ArrayList<HostGroupItem> ItemsThatMatch = new ArrayList<HostGroupItem>();
		
		Iterator<HostGroupItem> it = hostgroup.hosts();
		while(it.hasNext()){
			HostGroupItem item = it.next();
//			System.out.println("\nDEBUG:" + item.getName() +":"+HGItem.getName()+":");
//			System.out.println("DEBUG:" + item.getProductVersion() +":"+HGItem.getProductVersion()+":");
//			System.out.println("DEBUG:" + item.getHardware() +":"+HGItem.getHardware()+":");
//			System.out.println("DEBUG:" + item.getSoftware() +":"+HGItem.getSoftware()+":");
//			System.out.println("DEBUG:" + item.getSoftwareVersion() +":"+HGItem.getSoftwareVersion()+":");
//			System.out.println("DEBUG:" + item.getArchive1() +":"+HGItem.getArchive1()+":");
//			System.out.println("DEBUG:" + item.getArchive2() +":"+HGItem.getArchive2()+":");
//			System.out.println("DEBUG:" + item.getRole() +":"+HGItem.getRole()+":");
			if(
					item.getName().equals(HGItem.getName()) &&
					item.getProductVersion().equals(HGItem.getProductVersion()) &&	
					item.getHardware().equals(HGItem.getHardware()) &&
					item.getSoftware().equals(HGItem.getSoftware()) &&
					item.getSoftwareVersion().equals(HGItem.getSoftwareVersion()) &&
					item.getArchive1().equals(HGItem.getArchive1()) &&
					item.getArchive2().equals(HGItem.getArchive2()) &&
					item.getRole().equals(HGItem.getRole()) 
					){
				ItemsThatMatch.add(item);
			}

		}
		
		return ItemsThatMatch;
	}
	
	public boolean removeHostItemFromHostgroup(HostGroup hostgroup, HostGroupItem HGItem) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		Iterator<HostGroupItem> it = hostgroup.hosts();
		while(it.hasNext()){
			HostGroupItem item = it.next();
			if(
					item.getName().equals(HGItem.getName()) &&
					item.getProductVersion().equals(HGItem.getProductVersion()) &&	
					item.getHardware().equals(HGItem.getHardware()) &&
					item.getSoftware().equals(HGItem.getSoftware()) &&
					item.getSoftwareVersion().equals(HGItem.getSoftwareVersion()) &&
					item.getArchive1().equals(HGItem.getArchive1()) &&
					item.getArchive2().equals(HGItem.getArchive2()) &&
					item.getRole().equals(HGItem.getRole()) 
					){
				it.remove();
				}

			}
		return broker.common.saveAndCloseObject(hostgroup);
	}
	
	
	public boolean addHostsToHostgroup(String hostgroup, String HostnameFilter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		return addHostsToHostgroup(hg,HostnameFilter);
	}
	
	public boolean addHostsToHostgroup(HostGroup hostgroup, String HostnameFilter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		ArrayList<AgentListItem> agentList = broker.agents.getAgentListWithNameFilter(HostnameFilter);
		for(int i=0;i<agentList.size();i++){
			HostGroupItem newItem = new HostGroupItem(agentList.get(i).getName().getName());
			hostgroup.addHost(newItem);
		}
		return broker.common.saveAndCloseObject(hostgroup);
	}
	
	public boolean addHostsToHostgroup(HostGroup hostgroup, ArrayList<AgentListItem> agentList) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		for(int i=0;i<agentList.size();i++){
			HostGroupItem newItem = new HostGroupItem(agentList.get(i).getName().getName());
			hostgroup.addHost(newItem);
		}
		return broker.common.saveAndCloseObject(hostgroup);
	}
	
	// Method below might seem a bit complex.. however it is necessary because the Java API provides no easy way to remove a host 
	// from a host group.
	// the idea: 
	// 1- extract all existing hosts from hostgroup
	// 2- remove the desired host from the same list
	// 3- clear the content of the hostgroup
	// 4- iterate over the new list and add all hosts to the hostgroup
	
	public boolean removeHostFromHostgroup(String hostgroup, String Hostname) throws IOException{
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
		return broker.common.saveAndCloseObject(hg);
	}
	
	public boolean removeHostsFromHostgroup(String hostgroup, String HostnameFilter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		return removeHostsFromHostgroup(hg,HostnameFilter);
	}
	
	public boolean removeHostsFromHostgroup(HostGroup hostgroup, String HostnameFilter) throws IOException{
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
		return broker.common.saveAndCloseObject(hostgroup);
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
