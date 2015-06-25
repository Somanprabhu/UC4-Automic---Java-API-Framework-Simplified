package com.automic.objects;

import java.io.IOException;

import com.uc4.api.Template;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.Include;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Includes extends ObjectTemplate{
	public Includes(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public Include getJobGroupFromObject(UC4Object object){return (Include) object;}
	
	public void createInclude(String JGroupName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(JGroupName, Template.JOBI, FolderLocation);
	}
}
