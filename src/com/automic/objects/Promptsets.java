package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.objects.PromptSet;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Promptsets extends ObjectTemplate{

	public Promptsets(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public PromptSet getPromptSetFromObject(UC4Object object){return (PromptSet) object;}
	public ArrayList<UC4Object> getAllPromptsets() throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.PRPT);
	}
	public ArrayList<UC4Object> getAllPromptsetsWithFilter(String filter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.PRPT,filter);
	}
}
