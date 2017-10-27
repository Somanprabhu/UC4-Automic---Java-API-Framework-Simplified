package com.automic.objects;

import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

import com.automic.utils.Utils;
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
	
	public boolean importObjects(String FilePathForImport, IFolder folder, boolean overwriteObject, boolean overwriteFolderLinks) throws SAXException, IOException{
		File file = new File(FilePathForImport);
		ImportObject req = new ImportObject(file, folder, overwriteObject, overwriteFolderLinks);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object(s) Successfully Imported into Folder ["+folder.fullPath()+"] (Object Overwrite: "+overwriteObject+", Link Overwrite: "+overwriteFolderLinks+")."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
}

