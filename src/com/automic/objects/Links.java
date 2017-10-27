package com.automic.objects;

import java.io.IOException;
import java.util.List;

import com.automic.utils.Utils;
import com.uc4.api.FolderListItem;
import com.uc4.api.SearchResultItem;
import com.uc4.api.objects.IFolder;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.DeleteLink;
import com.uc4.communication.requests.FolderList;
import com.uc4.communication.requests.LinkTo;

public class Links extends ObjectTemplate{

	public Links(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public boolean createObjectLink(String SourceObjectName, IFolder TargetFolder) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		// 1- searching for the object first
		List<SearchResultItem> results = broker.searches.searchObject(SourceObjectName);
		if(results.size() != 1){
			System.out.println(Utils.getErrorString("Object Name: "+ SourceObjectName +" mapped to "+results.size() + " objects: Cannot locate!"));
			return false;
		}
		String OriginFolderName = results.get(0).getFolder();
		// 2- once found, the object needs to be located in a folder
		IFolder OriginFolder = broker.folders.getFolderByFullPathName(OriginFolderName);
		FolderList originFolderList = broker.folders.getFolderContent(OriginFolder);
		// 3- once located, the object needs to be gotten as a FolderListItem to comply with the method provided by the API..
		FolderListItem LocatedItem = null;
		for(FolderListItem item : originFolderList){
			if(item.getName().equals(SourceObjectName)){
				LocatedItem = item;
				break;
			}
		}
		return createObjectLink(LocatedItem, TargetFolder);
	}
	
	public boolean createObjectLink(FolderListItem item, IFolder TargetFolder) throws IOException{
		LinkTo req = new LinkTo(item,TargetFolder);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Link to Object: "+item.getName()+" Successfully Created in Folder: "+TargetFolder.fullPath()));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean deleteObjectLink(FolderListItem item) throws IOException{
		DeleteLink req = new DeleteLink(item);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Link to: "+item.getName()+" Successfully Deleted"));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
}
