package com.automic.objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import com.automic.utils.Utils;
import com.automic.utils.XMLUtils;
import com.uc4.api.FolderListItem;
import com.uc4.api.SearchResultItem;
import com.uc4.api.UC4HostName;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.UC4UserName;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.UC4Object;
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
	public boolean  exportFolder(IFolder folder, String FilePathForExport) throws IOException{
		File file = new File(FilePathForExport);
		ExportObject req = new ExportObject(folder,file,true);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Folder Successfully Exported."));
			return true;
		}
		return false;
	}
	
	// only works in v11+
	public boolean  exportFolders(IFolder[] folder, String FilePathForExport) throws IOException{
		File file = new File(FilePathForExport);
		ExportObject req = new ExportObject(folder,file,true);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Folders Successfully Exported."));
			return true;
		}
		return false;
	}
	
	public boolean  exportObject(String ObjectName, String FilePathForExport) throws IOException{
		//UC4ObjectName objName = new UC4ObjectName(ObjectName);
		UC4ObjectName objName = null;
		if (ObjectName.indexOf('/') != -1) objName = new UC4UserName(ObjectName);
		else if (ObjectName.indexOf('-')  != -1) objName = new UC4HostName(ObjectName);
		else objName = new UC4ObjectName(ObjectName);		
		
		File file = new File(FilePathForExport);
		ExportObject req = new ExportObject(objName,file);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object(s) Successfully Exported."));
			return true;
		}
		return false;
	}
	
	public boolean  exportObject(SearchResultItem item, String FilePathForExport) throws IOException{
		String ObjectName = item.getName();
		UC4ObjectName objName = null;
		if (ObjectName.indexOf('/') != -1) objName = new UC4UserName(ObjectName);
		else if (ObjectName.indexOf('-')  != -1) objName = new UC4HostName(ObjectName);
		else objName = new UC4ObjectName(ObjectName);		
		
		File file = new File(FilePathForExport);
		ExportObject req = new ExportObject(objName,file);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			System.out.println(item.getFolder());
			Say(Utils.getSuccessString("Object(s) Successfully Exported."));
			return true;
		}
		return false;
	}
	
	public boolean  exportObjectAndAddFolder(SearchResultItem item, String FilePathForExport) throws IOException, ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException{
		//UC4ObjectName objName = new UC4ObjectName(ObjectName);
		UC4ObjectName objName = null;
		if (item.getName().indexOf('/') != -1) objName = new UC4UserName(item.getName());
		else if (item.getName().indexOf('-')  != -1) objName = new UC4HostName(item.getName());
		else objName = new UC4ObjectName(item.getName());		
		List<SearchResultItem> items = getBrokerInstance().searches.searchObjectExcludeFolders(item.getName());
		String FolderName = items.get(0).getFolder();
		File file = new File(FilePathForExport);
		ExportObject req = new ExportObject(objName,file);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			XMLUtils.addFolderToXMLFile(file,FolderName);
			System.out.println("Test:" + FolderName);
			Say(Utils.getSuccessString("Object(s) Successfully Exported."));
			return true;
		}
		return false;
	}
	
	public boolean  exportObjectSilent(String ObjectName, String FilePathForExport) throws IOException{
		//UC4ObjectName objName = new UC4ObjectName(ObjectName);
		UC4ObjectName objName = null;
		if (ObjectName.indexOf('/') != -1) objName = new UC4UserName(ObjectName);
		else if (ObjectName.indexOf('-')  != -1) objName = new UC4HostName(ObjectName);
		else objName = new UC4ObjectName(ObjectName);		
		
		File file = new File(FilePathForExport);
		ExportObject req = new ExportObject(objName,file);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			//Say(Utils.getSuccessString("Object(s) Successfully Exported."));
			return true;
		}
		return false;
	}
	
	//v11+ only
	public ArrayList<UC4ObjectName> getReferencedObjects(String ObjectName) throws TimeoutException, IOException{
		UC4ObjectName objName = new UC4ObjectName(ObjectName);
		ArrayList<UC4ObjectName> ObjArray = new ArrayList<UC4ObjectName>();
	
		FindReferencedObjects req = new FindReferencedObjects(objName);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			//Say(Utils.getSuccessString("Object(s) Successfully Exported."));
			Iterator<UC4ObjectName> it = req.iterator();
			while(it.hasNext()){
				ObjArray.add(it.next());
			}
			return ObjArray;
		}
		return ObjArray;
	}
	
	public boolean exportObjectWithReferences(String ObjectName, String FilePathForExport) throws IOException{
		UC4ObjectName objName = null;
		if (ObjectName.indexOf('/') != -1) objName = new UC4UserName(ObjectName);
		else if (ObjectName.indexOf('-')  != -1) objName = new UC4HostName(ObjectName);
		else objName = new UC4ObjectName(ObjectName);		
		
		File file = new File(FilePathForExport);
		ExportWithReferences req = new ExportWithReferences(objName,file);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object(s) Successfully Exported."));
			return true;
		}
		return false;
	}
	
	public void  exportObject(FolderListItem item, String FilePathForExport) throws IOException{
		exportObject(item.getName(), FilePathForExport);
	}

	public boolean exportObjects(UC4ObjectName[] objectNames, String FilePathForExport) throws IOException{
		File file = new File(FilePathForExport);
		ExportObject req = new ExportObject(objectNames,file);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Objects "+" Successfully Exported to File: "+FilePathForExport.toString()));
			return true;
		}
		return false;
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
