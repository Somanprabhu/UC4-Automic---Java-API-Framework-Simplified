package com.automic.objects;

import java.io.IOException;

import com.uc4.api.Template;
import com.uc4.api.objects.Dashboard;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Dashboards extends ObjectTemplate{
	public Dashboards(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public Dashboard getJobGroupFromObject(UC4Object object){return (Dashboard) object;}
	
	public void createDashboard(String JGroupName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(JGroupName, Template.DASH, FolderLocation);
	}
}
