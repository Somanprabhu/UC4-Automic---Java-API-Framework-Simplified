package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.uc4.api.QuickSearchItem;
import com.uc4.api.SearchResultItem;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.QuickSearch;
import com.uc4.communication.requests.SearchObject;

public class Searches  extends ObjectTemplate{

private ObjectBroker broker;
	
	public Searches(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public List<SearchResultItem> genericSearch(SearchObject ser) throws TimeoutException, IOException{
		connection.sendRequestAndWait(ser);
		Iterator<SearchResultItem> it =  ser.resultIterator();
		List<SearchResultItem> results = new ArrayList<SearchResultItem>();
		while(it.hasNext()){
			SearchResultItem item = it.next();
			results.add(item);
		}
	return results;
		
	}
	
	public ArrayList<QuickSearchItem> quickSearch(QuickSearch ser) throws TimeoutException, IOException{
		connection.sendRequestAndWait(ser);
		Iterator<QuickSearchItem> it =  ser.iterator();
		ArrayList<QuickSearchItem> results = new ArrayList<QuickSearchItem>();
		while(it.hasNext()){
			QuickSearchItem item = it.next();
			results.add(item);
		}
	return results;
		
	}
	
	public int getSearchCount(String namePattern) throws IOException {
		SearchObject s = new SearchObject();		
		s.selectAllObjectTypes();		
		s.setTypeFOLD(true);		
		s.setName(namePattern);
		connection.sendRequestAndWait(s);
		return s.size();
	}
	
	public List<SearchResultItem> searchLogins(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setTypeLOGIN(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchVaras(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		ser.setTypeVARA(true);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchCalls(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		ser.setTypeCALL(true);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchEvents(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		ser.setTypeEVNT(true);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchJobs(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		ser.setTypeJOBS(true);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchSched(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		ser.setTypeJSCH(true);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchExecutableObjects(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setTypeExecuteable();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchJOBF(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setTypeJOBF(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchUsersAndGroups(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.setTypeUSER(true);
		ser.setTypeUSRG(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchScripts(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.setTypeSCRI(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchUsers(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.setTypeUSER(true);
		ser.setTypeUSRG(false);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchGroups(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.setTypeUSER(false);
		ser.setTypeUSRG(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchHostgroups(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.setTypeHOSTG(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchHostAssignments(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.setTypeHSTA(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchStorages(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.setTypeSTORE(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchObject(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.selectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchObjectExcludeFolders(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.selectAllObjectTypes();
		ser.setTypeFOLD(false);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchJobflows(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setTypeJOBP(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchConnections(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setTypeCONN(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	// not sure what the following method is for? seems redundant
	@Deprecated
	public List<SearchResultItem> searchObjectWithFilter(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.selectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchJOBSOrJOBPWithFilter(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setTypeJOBS(true);
		ser.setTypeJSCH(true);
		ser.setTypeJOBP(true);		
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchCalendars(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setTypeCALE(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		return genericSearch(ser);
	}
	
	public List<SearchResultItem> searchObjectForUsage(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.selectAllObjectTypes();
		ser.setSearchUseOfObjects(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		ser.setIncludeUseInScripts(false);
		return genericSearch(ser);
	}
	
	public int searchObjectForUsageCount(String ObjectName) throws IOException{
		SearchObject ser = new SearchObject();
		ser.selectAllObjectTypes();
		ser.setSearchUseOfObjects(true);
		//ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		ser.setIncludeUseInScripts(false);
		connection.sendRequestAndWait(ser);
		Iterator<SearchResultItem> it =  ser.resultIterator();
		int count = 0;
		while(it.hasNext()){
			count++;
			it.next();
		}
		return count;
	}
	
	//  not sure what the purpose of this is?
	@Deprecated 
	private List<SearchResultItem> searchObjectForReplace(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.selectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		return genericSearch(ser);
	}
	
	public ArrayList<QuickSearchItem> quickSearch(String ObjectName) throws IOException{
		QuickSearch exp = new QuickSearch(ObjectName);
		return (ArrayList<QuickSearchItem>) quickSearch(exp);
	}
}
