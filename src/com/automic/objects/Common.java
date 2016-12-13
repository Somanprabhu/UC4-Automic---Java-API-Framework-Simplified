package com.automic.objects;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXException;

import com.automic.utils.Utils;
import com.uc4.api.DateTime;
import com.uc4.api.FolderListItem;
import com.uc4.api.QuickSearchItem;
import com.uc4.api.SearchResultItem;
import com.uc4.api.TaskFilter;
import com.uc4.api.Template;
import com.uc4.api.UC4HostName;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.UC4TimezoneName;
import com.uc4.api.UC4UserName;
import com.uc4.api.VersionControlListItem;
import com.uc4.api.objects.ExecuteRecurring;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.ActivityList;
import com.uc4.communication.requests.CacheList;
import com.uc4.communication.requests.CancelTask;
import com.uc4.communication.requests.CloseObject;
import com.uc4.communication.requests.CreateObject;
import com.uc4.communication.requests.DeepRename;
import com.uc4.communication.requests.DeleteObject;
import com.uc4.communication.requests.DuplicateObject;
import com.uc4.communication.requests.ExecuteObject;
import com.uc4.communication.requests.ExportObject;
import com.uc4.communication.requests.ExportWithReferences;
import com.uc4.communication.requests.FindReferencedObjects;
import com.uc4.communication.requests.FolderList;
import com.uc4.communication.requests.GetChangeLog;
import com.uc4.communication.requests.GetReplaceList;
import com.uc4.communication.requests.ImportObject;
import com.uc4.communication.requests.MoveObject;
import com.uc4.communication.requests.OpenObject;
import com.uc4.communication.requests.QuarantineList;
import com.uc4.communication.requests.QueueList;
import com.uc4.communication.requests.QuickSearch;
import com.uc4.communication.requests.RenameObject;
import com.uc4.communication.requests.ReplaceObject;
import com.uc4.communication.requests.ResetOpenFlag;
import com.uc4.communication.requests.RestoreObjectVersion;
import com.uc4.communication.requests.SaveObject;
import com.uc4.communication.requests.SearchObject;
import com.uc4.communication.requests.TemplateList;
import com.uc4.communication.requests.VersionControlList;
import com.uc4.communication.requests.XMLRequest;

public class Common extends ObjectTemplate{
	
	public Common(Connection conn, boolean verbose) {
		super(conn, verbose);
	}

	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	// ####################
	// 
	// Generic XMLRequest Methods
	//
	// ####################
	
	
	// Move a single object to a different folder
	public boolean moveObject(String ObjectName, IFolder FolderSource, IFolder FolderTarget) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		FolderList folderList = broker.folders.getFolderContent(FolderSource);
		Iterator<FolderListItem> it = folderList.iterator();
		while(it.hasNext()){
			FolderListItem item = it.next();
			if(item.getName().equalsIgnoreCase(ObjectName)){
				MoveObject mov = new MoveObject(item,FolderSource,FolderTarget);
				sendGenericXMLRequestAndWait(mov);
				if (mov.getMessageBox() == null) {
					Say(Utils.getSuccessString("Object: "+item.getName()+" From: "+FolderSource.fullPath()+" Successfully moved to Folder: "+FolderTarget.fullPath()));
					return true;
				}
			}
		}
		return false;
	}

	// Replace an object with another one
	public boolean replaceObject(String SourceObjectName, String TargetObjectName) throws IOException{
		
		UC4ObjectName sourceName = new UC4ObjectName(SourceObjectName);
		UC4ObjectName targetName = new UC4ObjectName(TargetObjectName);
		List<SearchResultItem> results = getBrokerInstance().searches.searchObject(SourceObjectName);
		SearchResultItem[] items = new SearchResultItem[results.size()];
		items = results.toArray(items);
		GetReplaceList req = new GetReplaceList(sourceName);
		
		sendGenericXMLRequestAndWait(req);

		ReplaceObject req1 = new ReplaceObject(req,targetName,1);
		sendGenericXMLRequestAndWait(req1);
		
		if (req1.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object "+ SourceObjectName +" Successfully Replaced by: "+TargetObjectName));
			return true;
		}
		return false;
	}
	
	// Rename an object
	public boolean renameObject(String SourceObjectName, String TargetObjectName, IFolder folder) throws IOException{
		UC4ObjectName sourceName = new UC4ObjectName(SourceObjectName);
		UC4ObjectName targetName = new UC4ObjectName(TargetObjectName);
		String SourceObjectTitle = "";
	//	List<SearchResultItem> items = getBrokerInstance().searches.searchObject(SourceObjectName);
		// this method should always only be used on exact objects and not on * or ? expressions.. the items List should always yield exactly one element
	//	if(items.size()==1){
	//		SourceObjectTitle = items.get(0).getTitle();
	//	}
		
		ObjectBroker broker = getBrokerInstance();
		broker.common.reclaimObject(sourceName.getName());
		RenameObject req = new RenameObject(sourceName,targetName,folder,SourceObjectTitle);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object: "+ SourceObjectName +" Successfully Renamed to: "+TargetObjectName));
			return true;
		}
		return false;

	}
	
	// Rename an object
	//RenameObject ren = new RenameObject(tmp, new UC4ObjectName(Integer.toString(client)), tree.root(), title);
	public boolean renameObject(UC4ObjectName SourceObjectName, UC4ObjectName TargetObjectName, IFolder folder, String Title) throws IOException{
		
		ObjectBroker broker = getBrokerInstance();
		broker.common.reclaimObject(SourceObjectName.getName());
		RenameObject req = new RenameObject(SourceObjectName,TargetObjectName,folder,Title);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object: "+ SourceObjectName +" Successfully Renamed to: "+TargetObjectName));
			return true;
		}
		return false;

	}
	
	// Rename a Folder
	public boolean renameFolder(String SourceFolderName, String TargetFolderName) throws IOException{
		IFolder sourceFolder = getBrokerInstance().folders.getFolderByName(SourceFolderName);
		UC4ObjectName targetName = new UC4ObjectName(TargetFolderName);
		RenameObject req = new RenameObject(sourceFolder,targetName); // empty String is the Object Title
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object: "+ sourceFolder.fullPath() +" Successfully Renamed to: "+targetName));
			return true;
		}
		return false;
	}
	
	// Rename a Folder
	public boolean renameFolder(IFolder SourceFolder, String TargetFolderName) throws IOException{
		UC4ObjectName targetName = new UC4ObjectName(TargetFolderName);
		RenameObject req = new RenameObject(SourceFolder,targetName); // empty String is the Object Title
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object: "+ SourceFolder.fullPath() +" Successfully Renamed to: "+targetName));
			return true;
		}
		return false;
	}
	
	// the method below can potentially do a lot of harm - use with caution!
	// example: 
	
	// "GEN.SCRI.TASK1.DEEP_RENAME";
	// "GEN.SCRI.TASK2.DEEP_RENAME";
	// "GEN.SCRI.TASK3.DEEP_RENAME";
	
	// with ExistingNamePattern:	"GEN.SCRI.*DEEP_RENAME"
	// and NewNamePattern: "GEN.TEST1.*RENAMED"
	
	// will rename the objects to:
	
	// "GEN.TEST1.TASK1.DEEP_RENAMED";
	// "GEN.TEST1.TASK2.DEEP_RENAMED";
	// "GEN.TEST1.TASK3.DEEP_RENAMED";

	// Deep Rename ..
	public boolean deepRenameObjects(String ExistingPatternName, String NewPatternName) throws IOException{

			DeepRename req = new DeepRename();
			req.setNamePattern(NewPatternName);
			req.setCleanPattern(ExistingPatternName);
			sendGenericXMLRequestAndWait(req);
			if (req.getMessageBox() == null) {
				Say(Utils.getSuccessString("Object(s) with Pattern: "+ExistingPatternName+" Successfully renamed to Pattern: "+NewPatternName));
				return true;
			}
			return false;
	}
	
	// Duplicate an existing object
	public boolean duplicateObject(String SourceObjectName, String TargetObjectName, IFolder folder) throws IOException{
				
		UC4Object obj = openObject(SourceObjectName, true);
		
//		UC4ObjectName objName = null;
//		if (obj.getName().indexOf('/') != -1) objName = new UC4UserName(obj.getName());
//		else if (obj.getName().indexOf('-')  != -1) objName = new UC4HostName(obj.getName());
//		else objName = new UC4ObjectName(obj.getName());		

		UC4ObjectName DupObjName = null;
		if (TargetObjectName.indexOf('/') != -1) DupObjName = new UC4UserName(TargetObjectName);
		else if (TargetObjectName.indexOf('-')  != -1) DupObjName = new UC4HostName(TargetObjectName);
		else DupObjName = new UC4ObjectName(TargetObjectName);		

		DuplicateObject req = new DuplicateObject(obj,DupObjName,folder);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString(" \t ++ Object: "+obj.getName()+" Successfully saved in folder "+folder));
			return true;
		}
		return false;
	}
	
	// Open an Automic Object (of any kind)
	public UC4Object openObject(String name, boolean readOnly) throws IOException {
		//Say(" \t ++ Opening object: "+name);
		UC4ObjectName objName = null;
		if (name.indexOf('/') != -1) objName = new UC4UserName(name);
		else if (name.indexOf('-')  != -1) objName = new UC4HostName(name);
		else objName = new UC4ObjectName(name);		

		OpenObject req = new OpenObject(objName,readOnly,true);
		
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			return req.getUC4Object();
		}
		return null;
	}
	
	// Save an Automic Object (of any kind)
	public boolean saveObject(UC4Object obj) throws IOException {
		SaveObject req = null;
		try{
			req = new SaveObject(obj);
		}catch(InvalidObjectException e){
			System.out.println("dfglkfjfg");
		}
		
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object: "+obj.getName()+" Successfully saved"));
			return true;
		}
		return false;
	}

	// Resets the "open" flag in DB, forcing an already open object to be closed and available
	public boolean reclaimObject(String ObjectName) throws TimeoutException, IOException{
		if(ObjectName.contains("/")){
			UC4UserName objName = new UC4UserName(ObjectName);
			ResetOpenFlag req = new ResetOpenFlag(objName);
			sendGenericXMLRequestAndWait(req);
			if (req.getMessageBox() == null) {
				return true;
			}
			return false;
		}else{
			UC4ObjectName objName = new UC4ObjectName(ObjectName);
			ResetOpenFlag req = new ResetOpenFlag(objName);
			sendGenericXMLRequestAndWait(req);
			if (req.getMessageBox() == null) {
				return true;
			}
		}
		return false;
	}
	
	// close an Automic Object (of any kind)
	public boolean closeObject(UC4Object obj) throws IOException {

		CloseObject req = new CloseObject(obj);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			//Say(Utils.getSuccessString("Object: "+obj.getName()+" Successfully closed"));
			return true;
		}
		return false;
	}	
	
	// close an Automic Object (of any kind)
	public boolean saveAndCloseObject(UC4Object obj) throws IOException {
			SaveObject req = new SaveObject(obj);
			this.setVerbose(true);
			sendGenericXMLRequestAndWait(req);
			if (req.getMessageBox() == null) {
				CloseObject req1 = new CloseObject(obj);
				sendGenericXMLRequestAndWait(req1);
				if(req1.getMessageBox() == null){
					Say(Utils.getSuccessString("Object: "+obj.getName()+" Successfully saved & closed"));
					return true;
				}
			}
			return false;
		}	

	// Create an empty Automic Object (of any kind)
	public boolean createObject(String name, String templateName, IFolder fold) throws IOException{
		Template template = Template.getTemplateFor(templateName.toUpperCase());
		if ( template == null){
			System.out.println(" -- Error! Template Name " + templateName +" Does Not Seem To Match Any Existing Template..");
			return false;
		}else{
		return createObject(name, template, fold);
		}
	}
	
	// Create an empty Automic Object (of any kind)
	public boolean createObject(String name, Template template, IFolder fold) throws IOException {
		//Say(" \t ++ Creating object: "+name+" of Type: "+template.getType());
		UC4ObjectName objName = null;
		if (name.indexOf('/') != -1) objName = new UC4UserName(name);
		else if (template.isTimezone()) objName = new UC4TimezoneName(name);
		else objName = new UC4ObjectName(name);		

		CreateObject req = new CreateObject(objName,template,fold);
		sendGenericXMLRequestAndWait(req);
		if(req.getMessageBox() == null){
			Say(Utils.getSuccessString("Object: "+name+" Successfully created (Type: "+template.getType()+")"));
			return true;
		}
		return false;
	}

	// Delete an Automic Object (of any kind)
	public boolean deleteObject(String name, boolean ignoreError) throws IOException {

		UC4ObjectName objName = null;
		if (name.indexOf('/') != -1) objName = new UC4UserName(name);
		else objName = new UC4ObjectName(name);

		DeleteObject req = new DeleteObject(objName);
		
		sendGenericXMLRequestAndWait(req);
		if(req.getMessageBox() == null){
			Say(Utils.getSuccessString(" \t ++ Object: "+name+" Successfully deleted. "));
			return true;
		}
		if (req.getMessageBox().getNumber() == 4006507) {  //cannot delete because task is active
			// the message from the Engine should be enough. This is left as a reference.
			return false;
		}
		return false;
	}
	
	// Delete an Automic Object (of any kind)
	public boolean deleteObject(String name, IFolder fold, boolean ignoreError) throws IOException {
		UC4ObjectName objName = null;
		if (name.indexOf('/') != -1) objName = new UC4UserName(name);
		else objName = new UC4ObjectName(name);

		DeleteObject req = new DeleteObject(objName,fold);
		
		sendGenericXMLRequestAndWait(req);
		if(req.getMessageBox() == null){
			Say(Utils.getSuccessString(" \t ++ Object: "+name+" Successfully deleted. "));
			return true;
		}
		if (req.getMessageBox().getNumber() == 4006507) {  //cannot delete because task is active
			// the message from the Engine should be enough. This is left as a reference.
			return false;
		}
		return false;
	}
	
	public ArrayList<UC4Object> getAllObjects(String objectType) throws IOException{
		return getAllObjectsWithNameFilter(objectType,".*");
	}
	
	// match everything: ".*"
	// match only jobs starting with "JOBS": "JOBS.*"
	public ArrayList<UC4Object> getAllObjectsWithNameFilter(String objectType, String filter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		ArrayList<UC4Object> ObjList = new ArrayList<UC4Object>(); // Object Container
		ArrayList<IFolder> allFolders = broker.folders.getAllFolders(false); // list of all folders
	
		for(int i=0;i<allFolders.size();i++){
			IFolder myFolder = allFolders.get(i);
			FolderList itemList = broker.folders.getFolderContent(myFolder);
			Iterator<FolderListItem> it = itemList.iterator();
			while(it.hasNext()){
				FolderListItem item = it.next(); 
				if(item.getObjectType().equalsIgnoreCase(objectType) && item.getName().matches(filter)){
					UC4Object obj = broker.common.openObject(item.getName(), true);
					ObjList.add(obj);
				}
			}
		}
		return ObjList;
	}
	
	public ArrayList<FolderListItem> listAllObjects(String objectType) throws IOException{
		return listAllObjectsWithNameFilter(objectType,".*");
	}
	
	public ArrayList<FolderListItem> listAllObjectsWithNameFilter(String objectType, String filter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		ArrayList<FolderListItem> ObjList = new ArrayList<FolderListItem>();
		ArrayList<IFolder> allFolders = broker.folders.getAllFolders(true);
		for(int i=0;i<allFolders.size();i++){
			IFolder myFolder = allFolders.get(i);
			FolderList itemList = broker.folders.getFolderContent(myFolder);
			Iterator<FolderListItem> it = itemList.iterator();
			while(it.hasNext()){
				FolderListItem item = it.next(); 
				if(item.getObjectType().equalsIgnoreCase(objectType) && item.getName().matches(filter)){
					ObjList.add(item);
				}
			}
		}
		return ObjList;
	}
	
	public QuarantineList getQuarantineList() throws TimeoutException, IOException{
		QuarantineList req = new QuarantineList();
		return (QuarantineList) sendGenericXMLRequestAndWait(req);
	}
	
	public CacheList getCacheList() throws TimeoutException, IOException{
		CacheList req = new CacheList();
		return (CacheList) sendGenericXMLRequestAndWait(req);
	}
	
	public GetChangeLog getChanges() throws TimeoutException, IOException{
		GetChangeLog req = new GetChangeLog();
		req.selectAllChangeTypes();
		req.selectAllObjects();
		return (GetChangeLog) sendGenericXMLRequestAndWait(req);
	}
	
	public TemplateList getTemplateList() throws TimeoutException, IOException{
		TemplateList req = new TemplateList();
		return (TemplateList) sendGenericXMLRequestAndWait(req);
	}

}
