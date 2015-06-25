package com.automic.factories;

import java.io.IOException;
import java.util.Iterator;

import org.xml.sax.SAXException;

import com.automic.objects.ObjectBroker;
import com.uc4.api.FolderListItem;
import com.uc4.api.objects.IFolder;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.FolderList;

/**
 * 
 * @author bsp
 * this class implements copy of objects operations between different clients
 * Ex: Copy the content of a given folder from Client 1 to Client 130
 *
 */

public class ExportImportFactory extends FactoryTemplate{
	public ExportImportFactory(Connection[] conn, boolean verbose) {
		super(conn, verbose);
	}

	private ObjectBroker getBrokerInstance(Connection conn){
		return new ObjectBroker(conn,true);
	}
	
	// The following method is very experimental and implements no control whatsoever.. use with caution.. or improve it!
	public void CopyFolderContentBetweenClients(Connection ConnectionSource, String FolderFullPathSource, Connection ConnectionTarget, String FolderFullPathTarget) throws IOException, SAXException{
		
		ObjectBroker SourceBroker = getBrokerInstance(ConnectionSource);
		ObjectBroker TargetBroker = getBrokerInstance(ConnectionTarget);
		
		String FilePath = "/tmp/export_temp.xml";
		
		IFolder folderSource = SourceBroker.folders.getFolderByFullPathName(FolderFullPathSource);
		FolderList listSource = SourceBroker.folders.getFolderContent(folderSource);
		SourceBroker.common.exportFolderContent(listSource, FilePath);
		
		IFolder folderTarget = TargetBroker.folders.getFolderByFullPathName(FolderFullPathTarget);
		TargetBroker.common.importObjects(FilePath, folderTarget, true, true);
		FolderList listTarget = TargetBroker.folders.getFolderContent(folderTarget);
		System.out.println("Target Folder is: " + folderTarget.fullPath());
		Iterator<FolderListItem> it = listTarget.iterator();
		while(it.hasNext()){
			FolderListItem item = it.next();
			System.out.println("Object: "+item.getName());
		}
		}
		
	
	
	
}
