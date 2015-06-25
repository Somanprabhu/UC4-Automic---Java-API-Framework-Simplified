package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.objects.UC4Object;
import com.uc4.api.objects.UserGroup;
import com.uc4.communication.Connection;

public class Usergroups extends ObjectTemplate{

	public Usergroups(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public UserGroup getUserGroupFromObject(UC4Object object){return (UserGroup) object;}
	public UserGroup getUsergroupFromName(String UsrgroupName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(UsrgroupName, false);
		UserGroup userg = (UserGroup) obj;
		return userg;
	}
	public ArrayList<UC4Object> getAllUsergroups() throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.USRG);
	}
	public ArrayList<UC4Object> getAllUsergroupsWithFilter(String filter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.USRG,filter);
	}
}
