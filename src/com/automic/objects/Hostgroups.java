package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Hostgroups extends ObjectTemplate{

	public Hostgroups(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public ArrayList<UC4Object> getAllHostgroups() throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.HOSTG);
	}
	public ArrayList<UC4Object> getAllHostgroupsWithFilter(String filter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.HOSTG,filter);
	}
}
