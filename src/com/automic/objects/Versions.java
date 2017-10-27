package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.automic.utils.Utils;
import com.uc4.api.QuickSearchItem;
import com.uc4.api.SearchResultItem;
import com.uc4.api.UC4HostName;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.UC4TimezoneName;
import com.uc4.api.UC4UserName;
import com.uc4.api.VersionControlListItem;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.RestoreObjectVersion;
import com.uc4.communication.requests.VersionControlList;

public class Versions extends ObjectTemplate{

private ObjectBroker broker;
	
	public Versions(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public boolean restoreObjectVersions(VersionControlListItem VersionControlObject) throws IOException{
		RestoreObjectVersion req = new RestoreObjectVersion(VersionControlObject);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object: "+VersionControlObject.getSavedName()+" Successfully restored in version: "+VersionControlObject.getVersionNumber()));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public UC4Object openVersionControlObject(VersionControlListItem VersionControlObject) throws IOException{
		UC4Object obj = getBrokerInstance().common.openObject(VersionControlObject.getSavedName().toString(), true);
		return obj;
	}
	
	public boolean restoreSpecificVersion(String ObjectName, int VersionNumber) throws IOException{
		VersionControlList vcl = getObjectVersions(ObjectName);
		if(vcl.size() != 0){
			Iterator<VersionControlListItem> lastVersions = vcl.iterator();
			boolean Restored = false;
			while(lastVersions.hasNext()){
				VersionControlListItem item = lastVersions.next();
				if(item.getVersionNumber() == VersionNumber){
					return restoreObjectVersions(item);
				}
			}
			return Restored;
		}else{
			return false;
		}
	}
	
	public boolean restorePreviousVersion(String ObjectName) throws IOException{
		VersionControlList vcl = getObjectVersions(ObjectName);
		if(vcl.size() != 0){
			VersionControlListItem lastVersion = vcl.iterator().next();
			return restoreObjectVersions(lastVersion);
		}else{
			Say(Utils.getErrorString("No Previous Version Found. Is Versioning Activated?"));
			return false;
		}
	}
	
	public int getLastVersionNumber(String ObjectName) throws IOException{
		VersionControlList vcl = getObjectVersions(ObjectName);
		if(vcl.size() == 0){return 0;}
		VersionControlListItem lastVersion = vcl.iterator().next();
		return lastVersion.getVersionNumber();
	}
	
	public VersionControlList getObjectVersions(UC4ObjectName name) throws TimeoutException, IOException {
		VersionControlList req = new VersionControlList(name);
		return (VersionControlList) getBrokerInstance().common.sendGenericXMLRequestAndWait(req);
	}
	
	public VersionControlList getObjectVersions(String ObjectName) throws IOException{

		List<SearchResultItem> items = getBrokerInstance().searches.searchObject(ObjectName);
		// Result should always contain 1 single answer.
		if(items.size()!=1){return null;}
		String OBJECTTYPE = items.get(0).getObjectType();
		//System.out.println("Debug: Type found:"+OBJECTTYPE);
		if(OBJECTTYPE.equals("USER") || OBJECTTYPE.equals("USRG") ){
			UC4UserName name = new UC4UserName(ObjectName);
			VersionControlList req = new VersionControlList(name);
			return (VersionControlList) getBrokerInstance().common.sendGenericXMLRequestAndWait(req);
		}
		if(OBJECTTYPE.equals("HSTA") || OBJECTTYPE.equals("HOSTG") ){
			UC4HostName name = new UC4HostName(ObjectName);
			VersionControlList req = new VersionControlList(name);
			return (VersionControlList) getBrokerInstance().common.sendGenericXMLRequestAndWait(req);
		}
		if(OBJECTTYPE.equals("TZ")){
			UC4TimezoneName name = new UC4TimezoneName(ObjectName);
			VersionControlList req = new VersionControlList(name);
			return (VersionControlList) getBrokerInstance().common.sendGenericXMLRequestAndWait(req);
		}
		
		UC4ObjectName objName = new UC4ObjectName(ObjectName);
		VersionControlList req = new VersionControlList(objName);
		return (VersionControlList) getBrokerInstance().common.sendGenericXMLRequestAndWait(req);
	}
	
	public ArrayList<VersionControlListItem> getAllVersions(VersionControlList req){
		ArrayList<VersionControlListItem> Arr = new ArrayList<VersionControlListItem>();
		Iterator<VersionControlListItem> it = req.iterator();
		while(it.hasNext()){
			VersionControlListItem item = it.next();
			Arr.add(item);
		}
		return Arr;
	}
}
