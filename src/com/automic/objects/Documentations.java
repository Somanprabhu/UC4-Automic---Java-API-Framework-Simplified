package com.automic.objects;

import java.io.IOException;

import com.uc4.api.Template;
import com.uc4.api.objects.Documentation;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Documentations extends ObjectTemplate {


	public Documentations(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public Documentation getDocumentationFromObject(UC4Object object){return (Documentation) object;}
	
	public void createDocumentation(String DocumentationName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(DocumentationName, Template.DOCU, FolderLocation);
	}
}
