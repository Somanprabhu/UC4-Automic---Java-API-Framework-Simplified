package com.automic.objects;

import java.io.IOException;

import com.uc4.api.Template;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.Sync;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.GetSyncMonitor;
import com.uc4.communication.requests.SetSyncMonitor;
import com.uc4.communication.requests.XMLRequest;

public class Syncs extends ObjectTemplate {


	public Syncs(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public Sync getSyncFromObject(UC4Object object){return (Sync) object;}
	
	public boolean createSync(String SyncName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.createObject(SyncName, Template.SYNC, FolderLocation);
	}
	
	public GetSyncMonitor getSyncMonitor(String SyncName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4ObjectName objname = broker.common.getUC4ObjectNameFromString(SyncName);
		GetSyncMonitor req = new GetSyncMonitor(objname);
		return (GetSyncMonitor) broker.common.sendGenericXMLRequestAndWait(req, true);
	}
	
	public SetSyncMonitor setSyncMonitor(String ObjName, String SyncState, int SyncValue) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4ObjectName objname = broker.common.getUC4ObjectNameFromString(ObjName);
		SetSyncMonitor req = new SetSyncMonitor(objname, SyncState,SyncValue);
		return (SetSyncMonitor) broker.common.sendGenericXMLRequestAndWait(req, true);
	}
	
}
