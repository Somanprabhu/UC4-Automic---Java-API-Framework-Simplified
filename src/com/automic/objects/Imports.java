package com.automic.objects;

import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

import com.uc4.api.objects.IFolder;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.ImportObject;

public class Imports extends ObjectTemplate{

private ObjectBroker broker;
	
	public Imports(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public void importObjects(String FilePathForImport, IFolder folder, boolean overwriteObject, boolean overwriteFolderLinks) throws SAXException, IOException{
		File file = new File(FilePathForImport);
		ImportObject imp = new ImportObject(file, folder, overwriteObject, overwriteFolderLinks);
		connection.sendRequestAndWait(imp);
		if (imp.getMessageBox() != null) {
			System.out.println(" -- "+imp.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Object(s) Successfully Imported.");
		}
	}
}

