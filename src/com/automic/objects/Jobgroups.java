package com.automic.objects;

import java.io.IOException;

import com.uc4.api.Template;
import com.uc4.api.objects.Group;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Jobgroups extends ObjectTemplate{

	public Jobgroups(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public Group getJobGroupFromObject(UC4Object object){return (Group) object;}
	
	public void createJobgroup(String JGroupName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(JGroupName, Template.JOBG, FolderLocation);
	}
}
