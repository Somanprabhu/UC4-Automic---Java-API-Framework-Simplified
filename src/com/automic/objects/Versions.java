package com.automic.objects;

import java.io.IOException;
import java.util.Iterator;

import com.uc4.api.UC4ObjectName;
import com.uc4.api.VersionControlListItem;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
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
	
	
	public void restoreObjectVersions(VersionControlListItem VersionControlObject, boolean showSuccessMsg, boolean showErrorMsg) throws IOException{
		RestoreObjectVersion req = new RestoreObjectVersion(VersionControlObject);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			if(showSuccessMsg){System.out.println(" \t %% "+req.getMessageBox().getText().toString().replace("\n", "").replace("restored.","restored in version "+VersionControlObject.getVersionNumber()));}
		}else{
			if(showErrorMsg){System.out.println(" \t ++ Object: "+VersionControlObject.getSavedName()+" Successfully restored in version: "+VersionControlObject.getVersionNumber());}
		}
	}
	
	@Deprecated
	public void restoreObjectVersions(VersionControlListItem VersionControlObject) throws IOException{
		RestoreObjectVersion req = new RestoreObjectVersion(VersionControlObject);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" \t %% "+req.getMessageBox().getText().toString().replace("\n", "").replace("restored.","restored in version "+VersionControlObject.getVersionNumber()));
		}else{
			Say(" \t ++ Object: "+VersionControlObject.getSavedName()+" Successfully restored in version: "+VersionControlObject.getVersionNumber());
		}
	}
	
	public UC4Object openVersionControlObject(VersionControlListItem VersionControlObject) throws IOException{
		UC4Object obj = getBrokerInstance().common.openObject(VersionControlObject.getSavedName().toString(), true);
		return obj;
	}
	
	@Deprecated
	public boolean restoreSpecificVersion(String ObjectName, int VersionNumber) throws IOException{
		return restoreSpecificVersion(ObjectName,VersionNumber,true,true);
	
	}
	
	public boolean restoreSpecificVersion(String ObjectName, int VersionNumber, boolean showSuccessMsg, boolean showErrorMsg) throws IOException{
		VersionControlList vcl = getObjectVersions(ObjectName);
		if(vcl.size() != 0){
			Iterator<VersionControlListItem> lastVersions = vcl.iterator();
			boolean Restored = false;
			while(lastVersions.hasNext()){
				VersionControlListItem item = lastVersions.next();
				if(item.getVersionNumber() == VersionNumber){
					restoreObjectVersions(item, showSuccessMsg, showErrorMsg);
					Restored = true;
					return true;
				}
			}
			return Restored;
		}else{
			if(showErrorMsg){System.out.println("\t -- Error: No Version Found. Is Versioning Activated?");}
			return false;
		}
		
	}
	
	@Deprecated
	public void restorePreviousVersion(String ObjectName) throws IOException{
		restorePreviousVersion(ObjectName, true, true);
	}
	
	public void restorePreviousVersion(String ObjectName, boolean showSuccessMsg, boolean showErrorMsg) throws IOException{
		VersionControlList vcl = getObjectVersions(ObjectName);
		if(vcl.size() != 0){
			VersionControlListItem lastVersion = vcl.iterator().next();
			restoreObjectVersions(lastVersion, true, true);
		}else{
			if(showErrorMsg){System.out.println(" \t -- Error: No Previous Version Found. Is Versioning Activated?");}
		}
		
	}
	public int getLastVersionNumber(String ObjectName) throws IOException{
		VersionControlList vcl = getObjectVersions(ObjectName);
		if(vcl.size() == 0){return 0;}
		VersionControlListItem lastVersion = vcl.iterator().next();
		return lastVersion.getVersionNumber();
	}
	
	public VersionControlList getObjectVersions(String ObjectName) throws IOException{
		UC4ObjectName objName = new UC4ObjectName(ObjectName);
		VersionControlList req = new VersionControlList(objName);
		return (VersionControlList) getBrokerInstance().common.sendGenericXMLRequestAndWait(req);
	}
}
