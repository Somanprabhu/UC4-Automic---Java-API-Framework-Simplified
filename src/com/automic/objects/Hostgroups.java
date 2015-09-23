package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.objects.HostGroup;
import com.uc4.api.objects.HostGroupItem;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

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
	public void addHostToHostgroup(String hostgroup, String Hostname) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(hostgroup, false);
		HostGroup hg = (HostGroup) obj;
		HostGroupItem newItem = new HostGroupItem(Hostname);
		hg.addHost(newItem);
		broker.common.saveAndCloseObject(hg);
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
}
