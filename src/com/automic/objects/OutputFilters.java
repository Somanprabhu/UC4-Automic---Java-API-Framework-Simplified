package com.automic.objects;

import java.io.IOException;

import com.uc4.api.Template;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.OutputFilter;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class OutputFilters extends ObjectTemplate {


	public OutputFilters(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public OutputFilter getOutputFilterFromObject(UC4Object object){return (OutputFilter) object;}
	
	public void createOutputFilter(String OFilternName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(OFilternName, Template.FILTER_OUTPUT, FolderLocation);
	}

}
