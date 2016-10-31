package com.automic.objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.uc4.api.FolderListItem;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.IFolder;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.ExportObject;
import com.uc4.communication.requests.ExportWithReferences;
import com.uc4.communication.requests.FindReferencedObjects;
import com.uc4.communication.requests.FolderList;

public class Exports extends ObjectTemplate{

private ObjectBroker broker;
	
	public Exports(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	
	// only works in v11+
	public void  exportFolder(IFolder folder, String FilePathForExport) throws IOException{
		File file = new File(FilePathForExport);
		ExportObject exp = new ExportObject(folder,file,true);
		connection.sendRequestAndWait(exp);
		if (exp.getMessageBox() != null) {
			System.out.println(" -- "+exp.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Folder Successfully Exported.");
		}
	}
	// only works in v11+
	public void  exportFolders(IFolder[] folder, String FilePathForExport) throws IOException{
		File file = new File(FilePathForExport);
		ExportObject exp = new ExportObject(folder,file,true);
		connection.sendRequestAndWait(exp);
		if (exp.getMessageBox() != null) {
			System.out.println(" -- "+exp.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Folders Successfully Exported.");
		}
	}
	
	public void  exportObject(String ObjectName, String FilePathForExport) throws IOException{
		UC4ObjectName objName = new UC4ObjectName(ObjectName);
		File file = new File(FilePathForExport);
		ExportObject exp = new ExportObject(objName,file);
		connection.sendRequestAndWait(exp);
		if (exp.getMessageBox() != null) {
			System.out.println(" -- "+exp.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Object(s) Successfully Exported.");
		}
	}
	
	//v11+ only
	public ArrayList<UC4ObjectName> getReferencedObjects(String ObjectName) throws TimeoutException, IOException{
		UC4ObjectName objName = new UC4ObjectName(ObjectName);
	ArrayList<UC4ObjectName> ObjArray = new ArrayList<UC4ObjectName>();
	
		FindReferencedObjects exp = new FindReferencedObjects(objName);
		connection.sendRequestAndWait(exp);
		if (exp.getMessageBox() != null) {
			System.out.println(" -- "+exp.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Iterator<UC4ObjectName> it = exp.iterator();
			while(it.hasNext()){
				ObjArray.add(it.next());
			}
			return ObjArray;
		}
		return null;
	}
	public void  exportObjectWithReferences(String ObjectName, String FilePathForExport) throws IOException{
		UC4ObjectName objName = new UC4ObjectName(ObjectName);
		File file = new File(FilePathForExport);
		ExportWithReferences exp = new ExportWithReferences(objName,file);
		connection.sendRequestAndWait(exp);
		if (exp.getMessageBox() != null) {
			System.out.println(" -- "+exp.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Object(s) Successfully Exported.");
		}
	}
	
	public void  exportObject(FolderListItem item, String FilePathForExport) throws IOException{
		exportObject(item.getName(), FilePathForExport);
	}

	public void  exportObjects(UC4ObjectName[] objectNames, String FilePathForExport) throws IOException{
		File file = new File(FilePathForExport);
		ExportObject exp = new ExportObject(objectNames,file);
		connection.sendRequestAndWait(exp);
		if (exp.getMessageBox() != null) {
			System.out.println(" -- "+exp.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Objects "+" Successfully Exported to File: "+FilePathForExport.toString());
		}
	}
	public void exportFolderContent(FolderList ItemList, String FilePathForExport)throws IOException{
		ArrayList<UC4ObjectName> ObjList = new ArrayList<UC4ObjectName>();
		Iterator<FolderListItem> it = ItemList.iterator();
		while(it.hasNext()){
			FolderListItem item = it.next();
			ObjList.add(new UC4ObjectName(item.getName()));
		}
		UC4ObjectName[] ObjectNameList = new UC4ObjectName[ObjList.size()];
		for(int i=0;i<ObjList.size();i++){
			ObjectNameList[i] = ObjList.get(i);
		}
		exportObjects(ObjectNameList,FilePathForExport);	
	}

}
