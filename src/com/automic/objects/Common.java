package com.automic.objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXException;

import com.uc4.api.DateTime;
import com.uc4.api.FolderListItem;
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
import com.uc4.communication.requests.CancelTask;
import com.uc4.communication.requests.CloseObject;
import com.uc4.communication.requests.CreateObject;
import com.uc4.communication.requests.DeepRename;
import com.uc4.communication.requests.DeleteObject;
import com.uc4.communication.requests.DuplicateObject;
import com.uc4.communication.requests.ExecuteObject;
import com.uc4.communication.requests.ExportObject;
import com.uc4.communication.requests.FolderList;
import com.uc4.communication.requests.GetReplaceList;
import com.uc4.communication.requests.ImportObject;
import com.uc4.communication.requests.MoveObject;
import com.uc4.communication.requests.OpenObject;
import com.uc4.communication.requests.RenameObject;
import com.uc4.communication.requests.ReplaceObject;
import com.uc4.communication.requests.RestoreObjectVersion;
import com.uc4.communication.requests.SaveObject;
import com.uc4.communication.requests.SearchObject;
import com.uc4.communication.requests.TemplateList;
import com.uc4.communication.requests.VersionControlList;



public class Common extends ObjectTemplate{
	
	public Common(Connection conn, boolean verbose) {
		super(conn, verbose);
	}

	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
/**
	
	*
	** The Methods below are generic.
 * @return 
 * @throws TimeoutException 
 * @throws SAXException 
 * @throws IOException 
	** 
	*
	
	**/

	public TemplateList getTemplateList() throws TimeoutException, IOException{
		TemplateList req = new TemplateList();
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
		return req;
	}
	
	public void restoreObjectVersions(VersionControlListItem VersionControlObject) throws IOException{
		RestoreObjectVersion req = new RestoreObjectVersion(VersionControlObject);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
	}	
	public UC4Object openVersionControlObject(VersionControlListItem VersionControlObject) throws IOException{
		UC4Object obj = openObject(VersionControlObject.getSavedName().toString(), true);
		return obj;
	}
	public VersionControlList getObjectVersions(String ObjectName) throws IOException{
		UC4ObjectName objName = new UC4ObjectName(ObjectName);
		VersionControlList req = new VersionControlList(objName);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
		return req;
	}
	
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
					System.out.println(" -- "+mov.getMessageBox().getText());
				}
			}
		}
	}
	public void importObjects(String FilePathForImport, IFolder folder, boolean overwriteObject, boolean overwriteFolderLinks) throws SAXException, IOException{
		File file = new File(FilePathForImport);
		ImportObject imp = new ImportObject(file, folder, overwriteObject, overwriteFolderLinks);
		connection.sendRequestAndWait(imp);
		if (imp.getMessageBox() != null) {
			System.out.println(" -- "+imp.getMessageBox().getText());
		}
	}

	public void  exportObject(String ObjectName, String FilePathForExport) throws IOException{
		UC4ObjectName objName = new UC4ObjectName(ObjectName);
		File file = new File(FilePathForExport);
		ExportObject exp = new ExportObject(objName,file);
		connection.sendRequestAndWait(exp);
		if (exp.getMessageBox() != null) {
			System.out.println(" -- "+exp.getMessageBox().getText());
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
			System.out.println(" -- "+exp.getMessageBox().getText());
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
	
	public List<SearchResultItem> searchObject(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		// BUG: All filters unselected?!
		ser.selectAllObjectTypes();
		//ser.unselectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		ser.setTypeJOBS(true);
		connection.sendRequestAndWait(ser);
		Iterator<SearchResultItem> it =  ser.resultIterator();
		List<SearchResultItem> results = new ArrayList<SearchResultItem>();
		while(it.hasNext()){
			SearchResultItem item = it.next();
			results.add(item);
		}
	return results;
	}
	
	public List<SearchResultItem> searchObjectForUsage(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.selectAllObjectTypes();
		ser.setSearchUseOfObjects(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		ser.setIncludeUseInScripts(false);
		connection.sendRequestAndWait(ser);
		Iterator<SearchResultItem> it =  ser.resultIterator();
		List<SearchResultItem> results = new ArrayList<SearchResultItem>();
		while(it.hasNext()){
			SearchResultItem item = it.next();
			results.add(item);
		}
		return results;
	}
	
	private List<SearchResultItem> searchObjectForReplace(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.selectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		connection.sendRequestAndWait(ser);
		Iterator<SearchResultItem> it =  ser.resultIterator();
		List<SearchResultItem> results = new ArrayList<SearchResultItem>();
		while(it.hasNext()){
			SearchResultItem item = it.next();
			results.add(item);
		}
		return results;
	}
	
	public void replaceObject(String SourceObjectName, String TargetObjectName, IFolder folder) throws IOException{
		
		UC4ObjectName sourceName = new UC4ObjectName(SourceObjectName);
		UC4ObjectName targetName = new UC4ObjectName(TargetObjectName);
		List<SearchResultItem> results = searchObjectForReplace(SourceObjectName);
		SearchResultItem[] items = new SearchResultItem[results.size()];
		items = results.toArray(items);
		GetReplaceList repList = new GetReplaceList(sourceName);
		connection.sendRequestAndWait(repList);
		
		ReplaceObject rep = new ReplaceObject(repList,targetName,1);
		connection.sendRequestAndWait(rep);
		if (rep.getMessageBox() != null) {
			System.out.println(" -- "+rep.getMessageBox().getText());
		}

	}
	public void renameObject(String SourceObjectName, String TargetObjectName, IFolder folder) throws IOException{
		UC4ObjectName sourceName = new UC4ObjectName(SourceObjectName);
		UC4ObjectName targetName = new UC4ObjectName(TargetObjectName);
		RenameObject ren = new RenameObject(sourceName,targetName,folder, ""); // empty String is the Object Title
		connection.sendRequestAndWait(ren);
		if (ren.getMessageBox() != null) {
			System.out.println(" -- "+ren.getMessageBox().getText());
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

	public void deepRenameObjects(String ExistingPatternName, String NewPatternName) throws IOException{

			DeepRename deepRename = new DeepRename();
			deepRename.setNamePattern(NewPatternName);
			deepRename.setCleanPattern(ExistingPatternName);		

			connection.sendRequestAndWait(deepRename);
		
	}
	public void duplicateObject(String SourceObjectName, String TargetObjectName, IFolder folder) throws IOException{
		UC4Object obj = openObject(SourceObjectName, true);
		UC4ObjectName dupName = new UC4ObjectName(TargetObjectName);
		DuplicateObject dup = new DuplicateObject(obj,dupName,folder);
		connection.sendRequestAndWait(dup);
		if (dup.getMessageBox() != null) {
			System.out.println(" -- "+dup.getMessageBox().getText());
		}
		Say(" ++ Object: "+obj.getName()+" Successfully saved");

	}
	// Open an Automic Object (of any kind)
	public UC4Object openObject(String name, boolean readOnly) throws IOException {
		//Say(" ++ Opening object: "+name);
		UC4ObjectName objName = null;
		if (name.indexOf('/') != -1) objName = new UC4UserName(name);
		else if (name.indexOf('-')  != -1) objName = new UC4HostName(name);
		else objName = new UC4ObjectName(name);		

		// last boolean returns an OpenObject
		OpenObject open = new OpenObject(objName,readOnly,true);
		connection.sendRequestAndWait(open);

		if (open.getMessageBox() != null) {
			System.err.println(" -- "+open.getMessageBox().getText());
			System.out.println(" %% Object: "+ name +" returned a message box: "+open.getMessageBox().getNumber());
		}
		//Say(" ++ Object: "+name+" Successfully opened");
		return open.getUC4Object();
	}
	
	// Save an Automic Object (of any kind)
	public void saveObject(UC4Object obj) throws IOException {
		Say(" ++ Saving object: "+obj.getName()+"(Type: "+obj.getType()+")");
		SaveObject save = new SaveObject(obj);
		connection.sendRequestAndWait(save);
		if (save.getMessageBox() != null) {
			System.out.println(" -- "+save.getMessageBox().getText());
		}
		Say(" ++ Object: "+obj.getName()+" Successfully saved");
	}

	// close an Automic Object (of any kind)
	public void closeObject(UC4Object obj) throws IOException {
		Say(" ++ Closing object: "+obj.getName()+"(Type: "+obj.getType()+")");
		CloseObject close = new CloseObject(obj);
		connection.sendRequestAndWait(close);	
		if (close.getMessageBox() != null) {
			System.err.println(" -- "+close.getMessageBox().getText());
			System.out.println(" -- Close failed");
		}
		Say(" ++ Object: "+obj.getName()+" Successfully closed");
	}	
	// close an Automic Object (of any kind)
		public void saveAndCloseObject(UC4Object obj) throws IOException {
			saveObject(obj);
			closeObject(obj);
		}	
	
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public int executeObjectNow(String name) throws IOException {

		Say(" ++ Executing object Now: "+name);

		ExecuteObject execute = new ExecuteObject(new UC4ObjectName(name));
		connection.sendRequestAndWait(execute);

		if (execute.getMessageBox() != null || execute.getRunID() == 0) {
			if (execute.getMessageBox() != null) System.err.println(" -- "+execute.getMessageBox().getText());
			System.out.println("-- Failed to execute object:"+name + ":" +execute.getMessageBox().getText());
		}		
		Say(" ++ Object: "+name+" Successfully executed with Run ID: "+execute.getRunID());

		return execute.getRunID();
	}
	
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public int executeObjectOnce(String name, String timezone, DateTime startDate, DateTime logicalDate) throws IOException {

		Say(" ++ Executing object Once: "+name);

		ExecuteObject execute = new ExecuteObject(new UC4ObjectName(name));
		//DateTime startDate = DateTime.now().addDays(1);
		//DateTime logicalDate = DateTime.now().addDays(2);
		execute.executeOnce(startDate, logicalDate, new UC4TimezoneName(timezone), false, null);
		//execute.executeOnce(startDate, logicalDate, new UC4TimezoneName("TZ.ANG"), false, null);
		connection.sendRequestAndWait(execute);

		if (execute.getMessageBox() != null || execute.getRunID() == 0) {
			if (execute.getMessageBox() != null) System.err.println(" -- "+execute.getMessageBox().getText());
			System.out.println("-- Failed to execute object:"+name + ":" +execute.getMessageBox().getText());
		}		
		Say(" ++ Object: "+name+" Successfully executed with Run ID: "+execute.getRunID());

		return execute.getRunID();
	}
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public int executeObjectRecurring(String name, ExecuteRecurring recurringPattern) throws IOException {

		Say(" ++ Executing object Recurring: "+name);

		ExecuteObject execute = new ExecuteObject(new UC4ObjectName(name));
		//ExecuteRecurring rec = new ExecuteRecurring();
		//rec.setExecutionInterval(1);
		
		execute.executeRecurring(recurringPattern);
		
		connection.sendRequestAndWait(execute);

		if (execute.getMessageBox() != null || execute.getRunID() == 0) {
			if (execute.getMessageBox() != null) System.err.println(" -- "+execute.getMessageBox().getText());
			System.out.println("-- Failed to execute object:"+name + ":" +execute.getMessageBox().getText());
		}		
		Say(" ++ Object: "+name+" Successfully executed with Run ID: "+execute.getRunID());

		return execute.getRunID();
	}
	// Create an empty Automic Object (of any kind)
	public void createObject(String name, Template template, IFolder fold) throws IOException {
		Say(" ++ Creating object: "+name+" of Type: "+template.getType());

		UC4ObjectName objName = null;
		if (name.indexOf('/') != -1) objName = new UC4UserName(name);
		else if (template.isTimezone()) objName = new UC4TimezoneName(name);
		else objName = new UC4ObjectName(name);		

		CreateObject create = new CreateObject(objName,template,fold);
		connection.sendRequestAndWait(create);
		if (create.getMessageBox() != null) {
			System.out.println(create.getMessageBox().getText());
		}
		Say(" ++ Object: "+name+" Successfully created (Type: "+template.getType()+")");
	}

	// Delete an Automic Object (of any kind)
	public void deleteObject(String name, boolean ignoreError) throws IOException {
		Say(" ++ Deleting object: "+name);

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
				if ( delete.getMessageBox() == null )
				{	Say(" ++ Object: "+name+" Successfully deleted ");
					return;
				}
			}
			
			if (ignoreError) {
				System.out.println(delete.getMessageBox().getText());
				return;
			}
			System.out.println((delete.getMessageBox().getText()));
		}		
		Say(" ++ Object: "+name+" Successfully deleted ");
	}
	// Delete an Automic Object (of any kind)
	public void deleteObject(String name, IFolder fold, boolean ignoreError) throws IOException {
		Say(" ++ Deleting object: "+name);

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
				{	Say(" ++ Object: "+name+" Successfully deleted ");
					return;
				}
			}
			
			if (ignoreError) {
				System.out.println(delete.getMessageBox().getText());
				return;
			}
			System.out.println((delete.getMessageBox().getText()));
		}		
		Say(" ++ Object: "+name+" Successfully deleted ");
	}
	
	// Cancel an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public void cancelObject(int rundId) throws IOException {
		System.out.print("Cancel RundId("+ rundId +") ... ");
		CancelTask cancel = new CancelTask(rundId, false);
		connection.sendRequestAndWait(cancel);
		Say(" ++ Object: "+rundId+" Successfully cancelled ");
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
	public int getSearchCount(String namePattern) throws IOException {
		SearchObject s = new SearchObject();		
		s.selectAllObjectTypes();		
		s.setTypeFOLD(true);		
		s.setName(namePattern);
		connection.sendRequestAndWait(s);
		return s.size();
	}
}
