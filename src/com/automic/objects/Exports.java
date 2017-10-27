package com.automic.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
import com.uc4.api.PlatformSwHwType;
import com.uc4.api.SearchResultItem;
import com.uc4.api.UC4HostName;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.UC4UserName;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.ResourceItem;
import com.uc4.api.objects.Storage;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.DownloadBinary;
import com.uc4.communication.requests.ExportObject;
import com.uc4.communication.requests.ExportWithReferences;
import com.uc4.communication.requests.FindReferencedObjects;
import com.uc4.communication.requests.FolderList;
import com.uc4.communication.requests.TransportObject;

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
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
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
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean  transportObject(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(ObjectName, true);
		if(obj == null){return false;}
		TransportObject req = new TransportObject(obj);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object(s) Successfully Added to Transport Case."));
			broker.common.closeObject(obj);
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		broker.common.closeObject(obj);
		return false;
	}
	
	public boolean  exportObject(String ObjectName, String FilePathForExport) throws IOException{
		UC4ObjectName objName = getBrokerInstance().common.getUC4ObjectNameFromString(ObjectName);	
		
		File file = new File(FilePathForExport);
		ExportObject req = new ExportObject(objName,file);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object(s) Successfully Exported."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean exportObjects(UC4ObjectName[] objectNames, File FilePathForExport) throws IOException{
		
		ExportObject req = new ExportObject(objectNames,FilePathForExport);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Objects "+" Successfully Exported to File: "+FilePathForExport.toString()));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean exportObjects(UC4ObjectName[] objectNames, String FilePathForExport) throws IOException{
		File file = new File(FilePathForExport);
		ExportObject req = new ExportObject(objectNames,file);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Objects "+" Successfully Exported to File: "+FilePathForExport.toString()));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean  exportObjects(SearchResultItem item, String FilePathForExport) throws IOException{
		String ObjectName = item.getName();
		UC4ObjectName objName = getBrokerInstance().common.getUC4ObjectNameFromString(ObjectName);
		
		File file = new File(FilePathForExport);
		ExportObject req = new ExportObject(objName,file);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object(s) Successfully Exported."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean  exportObject(SearchResultItem item, String FilePathForExport) throws IOException{
		String ObjectName = item.getName();
		UC4ObjectName objName = getBrokerInstance().common.getUC4ObjectNameFromString(ObjectName);
		
		File file = new File(FilePathForExport);
		ExportObject req = new ExportObject(objName,file);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object(s) Successfully Exported."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean  exportObjectAndAddFolder(SearchResultItem item, String FilePathForExport) throws IOException, ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException{
		UC4ObjectName objName = getBrokerInstance().common.getUC4ObjectNameFromString(item.getName());
		
		List<SearchResultItem> items = getBrokerInstance().searches.searchObjectExcludeFolders(item.getName());
		String FolderName = items.get(0).getFolder();
		String ObjectType = item.getObjectType();
		File file = new File(FilePathForExport);
		
//		if(ObjectType.equals("STORE")){
//			String nativeDir = file.getAbsolutePath().replace(".xml.tmp", "");
//			getBrokerInstance().exports.extractBinariesFromStorage(item.getName(), nativeDir);
//		}
//		
		ExportObject req = new ExportObject(objName,file);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			XMLUtils.addFolderToXMLFile(file,FolderName);
			Say(Utils.getSuccessString("Object(s) Successfully Exported."));
			return true;
		}else{
			System.out.println("\t -- Error: " + req.getMessageBox().getText());
		}
		return false;
	}
	
	public String getFolderFromExportXML(String ExportFile){
		File file = new File(ExportFile);
		
		return ExportFile;
		
	}
	
	public boolean  exportObjectSilent(String ObjectName, String FilePathForExport) throws IOException{
		UC4ObjectName objName = getBrokerInstance().common.getUC4ObjectNameFromString(ObjectName);
		
		File file = new File(FilePathForExport);
		ExportObject req = new ExportObject(objName,file);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			//Say(Utils.getSuccessString("Object(s) Successfully Exported."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public File extractBinaryFromStorage(String StorageObjName, String ItemName, String TargetFileName) throws TimeoutException, IOException{
		ObjectBroker broker = getBrokerInstance();
		DownloadBinary req = new DownloadBinary(new UC4ObjectName(StorageObjName),ItemName,PlatformSwHwType.ALL);
		broker.common.sendGenericXMLRequestAndWait(req);
		ByteBuffer buffer = req.getBinaryContent();
		
		File file = new File(TargetFileName);
	    boolean append = false;
	    FileChannel wChannel = new FileOutputStream(file, append).getChannel();
	    wChannel.write(buffer);
	    wChannel.close();
	    return file;
	}
	
	// returns the list of files extracted - v11+ only
	public ArrayList<String> extractBinariesFromStorage(String StorageObjName, String PathToSaveFilesIn) throws TimeoutException, IOException{
		ObjectBroker broker = getBrokerInstance();
		ArrayList<String> ListOfFiles = new ArrayList<String>();
		Storage STORE = (Storage) broker.common.openObject(StorageObjName, true);
		String DATASEPARATOR = "____";
		String NAKEYWORD = "NA";
		String RESOURCEMARKER = "RESOURCE";
		Iterator<ResourceItem> iter = STORE.resourceItems().resourceItemsIterator();
		while(iter.hasNext()){
			ResourceItem item = iter.next();
			String ITEM = item.getName();
			String VERSION = NAKEYWORD;
			String SW = NAKEYWORD;
			String PLATFORM = NAKEYWORD;
			String HW = NAKEYWORD;
			if(!item.getVersion().equals("")){VERSION = item.getVersion();}
			if(!item.getSw().equals("*")){SW = item.getSw();}
			if(!item.getHw().equals("*")){HW = item.getHw();}
			if(!item.getPlatform().equals("*")){PLATFORM = item.getPlatform();}
			
			String FILENAME = 
					DATASEPARATOR + RESOURCEMARKER + DATASEPARATOR+
					item.getName() +DATASEPARATOR+
					item.getFilename()+DATASEPARATOR +
					VERSION+DATASEPARATOR+ 
					item.getSize()+DATASEPARATOR+
					item.getFileType()+DATASEPARATOR+
					SW+DATASEPARATOR+
					HW+DATASEPARATOR+
					PLATFORM+DATASEPARATOR+
					item.getChecksum()
					;
			
			if(PathToSaveFilesIn != null && !PathToSaveFilesIn.equals("")){
				FILENAME = PathToSaveFilesIn + FILENAME;
			}else{
				FILENAME = "./"+FILENAME;
			}
			
			File file = extractBinaryFromStorage(StorageObjName,ITEM,FILENAME);
			ListOfFiles.add(file.getAbsolutePath());
		}
		return ListOfFiles;
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
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return ObjArray;
	}
	
	public boolean exportObjectWithReferences(String ObjectName, String FilePathForExport) throws IOException{
		UC4ObjectName objName = getBrokerInstance().common.getUC4ObjectNameFromString(ObjectName);
		
		File file = new File(FilePathForExport);
		ExportWithReferences req = new ExportWithReferences(objName,file);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object(s) Successfully Exported."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public void  exportObject(FolderListItem item, String FilePathForExport) throws IOException{
		exportObject(item.getName(), FilePathForExport);
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
