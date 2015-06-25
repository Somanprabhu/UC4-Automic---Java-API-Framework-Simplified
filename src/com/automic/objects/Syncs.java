package com.automic.objects;

import java.io.IOException;

import com.uc4.api.Template;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.Sync;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Syncs extends ObjectTemplate {


	public Syncs(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public Sync getSyncFromObject(UC4Object object){return (Sync) object;}
	
	public void createSync(String SyncName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(SyncName, Template.SYNC, FolderLocation);
	}
}
