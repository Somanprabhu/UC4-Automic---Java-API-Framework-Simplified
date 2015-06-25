package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.objects.Script;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Scripts extends ObjectTemplate{

	public Scripts(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public Script getScriptFromObject(UC4Object object){return (Script) object;}
	public ArrayList<UC4Object> getAllScripts() throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.SCRI);
	}
	public ArrayList<UC4Object> getAllScriptsWithFilter(String filter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.SCRI,filter);
	}
}
