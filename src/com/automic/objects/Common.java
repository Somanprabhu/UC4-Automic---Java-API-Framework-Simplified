package com.automic.objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXException;

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
	
	// Sends a generic XMLRequest to the engine
	public XMLRequest sendGenericXMLRequestAndWait(XMLRequest req) throws TimeoutException, IOException{
		return sendGenericXMLRequestAndWait(req,false);
	}
	
	// Sends a generic XMLRequest to the engine with or without showing the response
	public XMLRequest sendGenericXMLRequestAndWait(XMLRequest req,boolean showResponse) throws TimeoutException, IOException{
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			if(showResponse){System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));}
			return null;
		}
		return req;
	}

	// Move a single object to a different folder
	public void moveObject(String ObjectName, IFolder FolderSource, IFolder FolderTarget) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		FolderList folderList = broker.folders.getFolderContent(FolderSource);
		Iterator<FolderListItem> it = folderList.iterator();
		boolean ObjectNotFound = true;
		while(it.hasNext() && ObjectNotFound){
			FolderListItem item = it.next();
			if(item.getName().equalsIgnoreCase(ObjectName)){
				ObjectNotFound = false;
				MoveObject mov = new MoveObject(item,FolderSource,FolderTarget);
				connection.sendRequestAndWait(mov);
				if (mov.getMessageBox() != null) {
					System.out.println(" -- "+mov.getMessageBox().getText().toString().replace("\n", ""));
				}else{
					Say(" \t ++ Object: "+item.getName()+" From: "+FolderSource.fullPath()+" Successfully moved to Folder: "+FolderTarget.fullPath());
				}
			}
		}
	}

	// Replace an object with another one
	public void replaceObject(String SourceObjectName, String TargetObjectName) throws IOException{
		
		UC4ObjectName sourceName = new UC4ObjectName(SourceObjectName);
		UC4ObjectName targetName = new UC4ObjectName(TargetObjectName);
		List<SearchResultItem> results = getBrokerInstance().searches.searchObject(SourceObjectName);
		SearchResultItem[] items = new SearchResultItem[results.size()];
		items = results.toArray(items);
		GetReplaceList repList = new GetReplaceList(sourceName);
		connection.sendRequestAndWait(repList);
		
		ReplaceObject rep = new ReplaceObject(repList,targetName,1);
		connection.sendRequestAndWait(rep);
		if (rep.getMessageBox() != null) {
			System.out.println(" -- "+rep.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Object "+ SourceObjectName +" Successfully Replaced by: "+TargetObjectName);
		}

	}
	
	// Rename an object
	public void renameObject(String SourceObjectName, String TargetObjectName, IFolder folder) throws IOException{
		UC4ObjectName sourceName = new UC4ObjectName(SourceObjectName);
		UC4ObjectName targetName = new UC4ObjectName(TargetObjectName);
		String SourceObjectTitle = "";
		List<SearchResultItem> items = getBrokerInstance().searches.searchObject(SourceObjectName);
		// this method should always only be used on exact objects and not on * or ? expressions.. the items List should always yield exactly one element
		if(items.size()==1){
			SourceObjectTitle = items.get(0).getTitle();
		}
		RenameObject ren = new RenameObject(sourceName,targetName,folder,SourceObjectTitle); // empty String is the Object Title
		
		connection.sendRequestAndWait(ren);
		if (ren.getMessageBox() != null) {
			System.out.println(" -- "+ren.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Object "+ SourceObjectName +" Successfully Renamed to: "+TargetObjectName);
		}

	}
	
	// Rename a Folder
	public void renameFolder(String SourceFolderName, String TargetFolderName) throws IOException{
		IFolder sourceFolder = getBrokerInstance().folders.getFolderByName(SourceFolderName);
		UC4ObjectName targetName = new UC4ObjectName(TargetFolderName);
		RenameObject ren = new RenameObject(sourceFolder,targetName); // empty String is the Object Title
		connection.sendRequestAndWait(ren);
		if (ren.getMessageBox() != null) {
			System.out.println(" -- "+ren.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Object "+ sourceFolder.fullPath() +" Successfully Renamed to: "+targetName);
		}

	}
	
	// Rename a Folder
	public void renameFolder(IFolder SourceFolder, String TargetFolderName) throws IOException{
		UC4ObjectName targetName = new UC4ObjectName(TargetFolderName);
		RenameObject ren = new RenameObject(SourceFolder,targetName); // empty String is the Object Title
		connection.sendRequestAndWait(ren);
		if (ren.getMessageBox() != null) {
			System.out.println(" -- "+ren.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Object "+ SourceFolder.fullPath() +" Successfully Renamed to: "+targetName);
		}

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
	public void deepRenameObjects(String ExistingPatternName, String NewPatternName) throws IOException{

			DeepRename deepRename = new DeepRename();
			deepRename.setNamePattern(NewPatternName);
			deepRename.setCleanPattern(ExistingPatternName);		
			connection.sendRequestAndWait(deepRename);
			if (deepRename.getMessageBox() != null) {
				System.out.println(" -- "+deepRename.getMessageBox().toString().replace("\n", ""));
			}else{
				Say(" \t ++ Object(s) with Pattern: "+ExistingPatternName+" Successfully renamed to Pattern: "+NewPatternName);
			}
	}
	
	// Duplicate an existing object
	public void duplicateObject(String SourceObjectName, String TargetObjectName, IFolder folder) throws IOException{
		UC4Object obj = openObject(SourceObjectName, true);
		UC4ObjectName dupName = new UC4ObjectName(TargetObjectName);
		DuplicateObject dup = new DuplicateObject(obj,dupName,folder);
		connection.sendRequestAndWait(dup);
		if (dup.getMessageBox() != null) {
			System.out.println(" -- "+dup.getMessageBox().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Object: "+obj.getName()+" Successfully saved in folder "+folder);
		}
	}
	
	// Open an Automic Object (of any kind)
	public UC4Object openObject(String name, boolean readOnly) throws IOException {
		//Say(" \t ++ Opening object: "+name);
		UC4ObjectName objName = null;
		if (name.indexOf('/') != -1) objName = new UC4UserName(name);
		else if (name.indexOf('-')  != -1) objName = new UC4HostName(name);
		else objName = new UC4ObjectName(name);		

		OpenObject open = new OpenObject(objName,readOnly,true);
		connection.sendRequestAndWait(open);

		if (open.getMessageBox() != null) {
			System.err.println(" -- "+open.getMessageBox().toString().replace("\n", ""));
			return null;
		}
		return open.getUC4Object();
	}
	
	// Save an Automic Object (of any kind)
	public void saveObject(UC4Object obj) throws IOException {
		//Say(" \t ++ Saving object: "+obj.getName()+"(Type: "+obj.getType()+")");
		SaveObject save = new SaveObject(obj);
		connection.sendRequestAndWait(save);
		if (save.getMessageBox() != null) {
			System.out.println("\t -- "+save.getMessageBox().getText().toString().replace("\n", ""));
		}
		Say(" \t ++ Object: "+obj.getName()+" Successfully saved");
	}

	// Resets the "open" flag in DB, forcing an already open object to be closed and available
	public void reclaimObject(String ObjectName) throws TimeoutException, IOException{
		if(ObjectName.contains("/")){
			UC4UserName objName = new UC4UserName(ObjectName);
			ResetOpenFlag req = new ResetOpenFlag(objName);
			connection.sendRequestAndWait(req);	
			if (req.getMessageBox() != null) {
				System.err.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
			}else{
				//Say(" \t ++ Object: "+objName+" Successfully reclaimed");
			}
		}else{
			UC4ObjectName objName = new UC4ObjectName(ObjectName);
			ResetOpenFlag req = new ResetOpenFlag(objName);
			connection.sendRequestAndWait(req);	
			if (req.getMessageBox() != null) {
				System.err.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
			}else{
				//Say(" \t ++ Object: "+objName+" Successfully reclaimed");
			}
		}
		
	}
	
	// close an Automic Object (of any kind)
	public void closeObject(UC4Object obj) throws IOException {
		//Say(" \t ++ Closing object: "+obj.getName()+"(Type: "+obj.getType()+")");
		CloseObject close = new CloseObject(obj);
		connection.sendRequestAndWait(close);	
		if (close.getMessageBox() != null) {
			System.err.println(" -- "+close.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			//Say(" \t ++ Object: "+obj.getName()+" Successfully closed");
		}
	}	
	
	// close an Automic Object (of any kind)
	public void saveAndCloseObject(UC4Object obj) throws IOException {
			SaveObject save = new SaveObject(obj);
			connection.sendRequestAndWait(save);
			if (save.getMessageBox() != null) {
				System.out.println(" -- "+save.getMessageBox().getText().toString().replace("\n", ""));
			}else{
				CloseObject close = new CloseObject(obj);
				connection.sendRequestAndWait(close);	
				if (close.getMessageBox() != null) {
					System.err.println("\t -- "+close.getMessageBox().getText().toString().replace("\n", ""));
				}else{
					Say(" \t ++ Object: "+obj.getName()+" Successfully saved & closed");
				}
			}
		}	

	// Create an empty Automic Object (of any kind)
	public void createObject(String name, String templateName, IFolder fold) throws IOException{
		Template template = com.automic.utils.Utils.convertStringToTemplate(templateName);
		if ( template == null){
			System.out.println(" -- Error! Template Name " + templateName +" Does Not Seem To Match Any Existing Template..");
		}else{
		createObject(name, template, fold);
		}
	}
	
	// Create an empty Automic Object (of any kind)
	public void createObject(String name, Template template, IFolder fold) throws IOException {
		//Say(" \t ++ Creating object: "+name+" of Type: "+template.getType());
		UC4ObjectName objName = null;
		if (name.indexOf('/') != -1) objName = new UC4UserName(name);
		else if (template.isTimezone()) objName = new UC4TimezoneName(name);
		else objName = new UC4ObjectName(name);		

		CreateObject create = new CreateObject(objName,template,fold);
		connection.sendRequestAndWait(create);
		if (create.getMessageBox() != null) {
			System.out.println(create.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Object: "+name+" Successfully created (Type: "+template.getType()+")");
		}
	}

	// Delete an Automic Object (of any kind)
	public void deleteObject(String name, boolean ignoreError) throws IOException {
		//Say(" \t ++ Deleting object: "+name);

		UC4ObjectName objName = null;
		if (name.indexOf('/') != -1) objName = new UC4UserName(name);
		else objName = new UC4ObjectName(name);

		DeleteObject delete = new DeleteObject(objName);
		connection.sendRequestAndWait(delete);	
		if (delete.getMessageBox() != null) {
			
			if (delete.getMessageBox().getNumber() == 4006507) {  //active
				TaskFilter taskFilter = new TaskFilter();
				taskFilter.setObjectName(name);
				
				ActivityList list = new ActivityList(taskFilter);
				connection.sendRequestAndWait(list);
				//for (Task t : list) removeTask(t);
				//connection.sendRequestAndWait(delete);
				if ( delete.getMessageBox() == null)
				{	Say(" \t ++ Object: "+name+" Successfully deleted. ");
					return;
				}
			}
			
			if (ignoreError) {
				System.out.println(delete.getMessageBox().getText().toString().replace("\n", ""));
				return;
			}
			System.out.println((delete.getMessageBox().getText().toString().replace("\n", "")));
		}else{		
			Say(" \t ++ Object: "+name+" Successfully deleted. ");
		}
	}
	
	// Delete an Automic Object (of any kind)
	public void deleteObject(String name, IFolder fold, boolean ignoreError) throws IOException {
		UC4ObjectName objName = null;
		if (name.indexOf('/') != -1) objName = new UC4UserName(name);
		else objName = new UC4ObjectName(name);

		DeleteObject delete = new DeleteObject(objName, fold);
		connection.sendRequestAndWait(delete);	
		if (delete.getMessageBox() != null) {
			
			if (delete.getMessageBox().getNumber() == 4006507) {  //active
				TaskFilter taskFilter = new TaskFilter();
				taskFilter.setObjectName(name);
				ActivityList list = new ActivityList(taskFilter);
				connection.sendRequestAndWait(list);
				//for (Task t : list) removeTask(t);
				//connection.sendRequestAndWait(delete);
				if ( delete.getMessageBox() == null )
				{	Say(" \t ++ Object: "+name+" Successfully deleted. ");
					return;
				}
			}
			
			if (ignoreError) {
				System.out.println(delete.getMessageBox().getText().toString().replace("\n", ""));
				return;
			}
			System.out.println((delete.getMessageBox().getText().toString().replace("\n", "")));
		}else{		
			Say(" \t ++ Object: "+name+" Successfully deleted. ");
		}
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
