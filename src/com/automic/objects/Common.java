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
import com.uc4.communication.requests.QuickSearch;
import com.uc4.communication.requests.RenameObject;
import com.uc4.communication.requests.ReplaceObject;
import com.uc4.communication.requests.ResetOpenFlag;
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
 * @return 
 * @throws TimeoutException 
 * @throws SAXException 
 * @throws IOException 
	** 
	*
	
	**/

	public GetChangeLog getChanges() throws TimeoutException, IOException{
		GetChangeLog req = new GetChangeLog();
		req.selectAllChangeTypes();
		req.selectAllObjects();
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		
		return req;
	}
	
	public TemplateList getTemplateList() throws TimeoutException, IOException{
		TemplateList req = new TemplateList();
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		return req;
	}
	
	public void restoreObjectVersions(VersionControlListItem VersionControlObject) throws IOException{
		RestoreObjectVersion req = new RestoreObjectVersion(VersionControlObject);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" \t %% "+req.getMessageBox().getText().toString().replace("\n", "").replace("restored.","restored in version "+VersionControlObject.getVersionNumber()));
		}else{
			Say(" \t ++ Object: "+VersionControlObject.getSavedName()+" Successfully restored in version: "+VersionControlObject.getVersionNumber());
		}
	}
	
	public UC4Object openVersionControlObject(VersionControlListItem VersionControlObject) throws IOException{
		UC4Object obj = openObject(VersionControlObject.getSavedName().toString(), true);
		return obj;
	}
	
	public boolean restoreSpecificVersion(String ObjectName, int VersionNumber) throws IOException{
		VersionControlList vcl = getObjectVersions(ObjectName);
		if(vcl.size() != 0){
			Iterator<VersionControlListItem> lastVersions = vcl.iterator();
			boolean Restored = false;
			while(lastVersions.hasNext()){
				VersionControlListItem item = lastVersions.next();
				if(item.getVersionNumber() == VersionNumber){
					restoreObjectVersions(item);
					Restored = true;
					return true;
				}
			}
			return Restored;
		}else{
			System.out.println("\t -- Error: No Version Found. Is Versioning Activated?");
			return false;
		}
		
	}
	
	public void restorePreviousVersion(String ObjectName) throws IOException{
		VersionControlList vcl = getObjectVersions(ObjectName);
		if(vcl.size() != 0){
			VersionControlListItem lastVersion = vcl.iterator().next();
			restoreObjectVersions(lastVersion);
		}else{
			System.out.println(" \t -- Error: No Previous Version Found. Is Versioning Activated?");
		}
		
	}
	public int getLastVersionNumber(String ObjectName) throws IOException{
		VersionControlList vcl = getObjectVersions(ObjectName);
		if(vcl.size() == 0){return 0;}
		VersionControlListItem lastVersion = vcl.iterator().next();
		return lastVersion.getVersionNumber();
	}
	
	public VersionControlList getObjectVersions(String ObjectName) throws IOException{
		UC4ObjectName objName = new UC4ObjectName(ObjectName);
		VersionControlList req = new VersionControlList(objName);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
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
					System.out.println(" -- "+mov.getMessageBox().getText().toString().replace("\n", ""));
				}else{
					Say(" \t ++ Object: "+item.getName()+" From: "+FolderSource.fullPath()+" Successfully moved to Folder: "+FolderTarget.fullPath());
				}
			}
		}
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
	public ArrayList<QuickSearchItem> quickSearch(String ObjectName) throws IOException{

		ArrayList<QuickSearchItem> items = new ArrayList<QuickSearchItem>();
		
		QuickSearch exp = new QuickSearch(ObjectName);
		connection.sendRequestAndWait(exp);
		if (exp.getMessageBox() != null) {
			System.out.println(" -- "+exp.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Iterator<QuickSearchItem> iter = exp.iterator();
			while(iter.hasNext()){
				items.add(iter.next());
			}
			return items;
		}
		return null;
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
	public List<SearchResultItem> searchUsersAndGroups(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		// BUG: All filters unselected?!
		ser.setTypeUSER(true);
		ser.setTypeUSRG(true);
		//ser.unselectAllObjectTypes();
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
	public List<SearchResultItem> searchStorages(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		// BUG: All filters unselected?!
		ser.setTypeSTORE(true);
		//ser.unselectAllObjectTypes();
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
	
	public List<SearchResultItem> searchJobflows(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setTypeJOBP(true);
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
	public List<SearchResultItem> searchConnections(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setTypeCONN(true);
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
	public List<SearchResultItem> searchLogins(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setTypeLOGIN(true);
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
	public List<SearchResultItem> searchVaras(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		ser.setTypeVARA(true);
		connection.sendRequestAndWait(ser);
		Iterator<SearchResultItem> it =  ser.resultIterator();
		List<SearchResultItem> results = new ArrayList<SearchResultItem>();
		while(it.hasNext()){
			SearchResultItem item = it.next();
			results.add(item);
		}
	return results;
	}
	public List<SearchResultItem> searchJobs(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
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
	public List<SearchResultItem> searchSched(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		ser.setTypeJSCH(true);
		connection.sendRequestAndWait(ser);
		Iterator<SearchResultItem> it =  ser.resultIterator();
		List<SearchResultItem> results = new ArrayList<SearchResultItem>();
		while(it.hasNext()){
			SearchResultItem item = it.next();
			results.add(item);
		}
	return results;
	}
	public List<SearchResultItem> searchExecutableObjects(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setTypeExecuteable();
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
	public List<SearchResultItem> searchJOBF(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setTypeJOBF(true);
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
	public List<SearchResultItem> searchObjectWithFilter(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		// BUG: All filters unselected?!
		ser.selectAllObjectTypes();
		//ser.unselectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
	/*	
		ser.setTypeCALE(true);
		ser.setTypeCALL(true);
		ser.setTypeCITC(true);
		ser.setTypeCLNT(true);
		ser.setTypeCODE(true);
		ser.setTypeCONN(true);
		ser.setTypeCPIT(true);
		ser.setTypeDASH(true);
		ser.setTypeDOCU(true);
		ser.setTypeEVNT(true);
		ser.setTypeExecuteable();
		ser.setTypeFILTER(true);
		ser.setTypeFOLD(true);
		ser.setTypeHOST(true);
		ser.setTypeHOSTG(true);
		ser.setTypeHSTA(true);
		ser.setTypeJOBF(true);
		ser.setTypeJOBF(true);
		ser.setTypeJOBG(true);
		ser.setTypeJOBP(true);
		ser.setTypeJOBQ(true);
		ser.setTypeJOBS(true);
		ser.setTypeJSCH(true);
		ser.setTypeLOGIN(true);
		ser.setTypePRPT(true);
		ser.setTypeQUEUE(true);
		ser.setTypeSCRI(true);
		ser.setTypeSERV(true);
		ser.setTypeSTORE(true);
		ser.setTypeSYNC(true);
		ser.setTypeTZ(true);
		ser.setTypeUSER(true);
		ser.setTypeUSRG(true);
		ser.setTypeVARA(true);
		ser.setTypeXSL(true);
		*/
		connection.sendRequestAndWait(ser);
		Iterator<SearchResultItem> it =  ser.resultIterator();
		List<SearchResultItem> results = new ArrayList<SearchResultItem>();
		while(it.hasNext()){
			SearchResultItem item = it.next();
			results.add(item);
		}
	return results;
	}
	public List<SearchResultItem> searchJOBSOrJOBPWithFilter(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		
		if(ObjectName.equals("*")){
			//ser.setName("");
		}else{
			ser.setName(ObjectName);
		}
		
		ser.setTypeJOBS(true);
		ser.setTypeJSCH(true);
		ser.setTypeJOBP(true);		
		connection.sendRequestAndWait(ser);
		Iterator<SearchResultItem> it =  ser.resultIterator();
		List<SearchResultItem> results = new ArrayList<SearchResultItem>();
		while(it.hasNext()){
			SearchResultItem item = it.next();
			results.add(item);
		}
	return results;
	}
	
	public List<SearchResultItem> searchCalendars(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.unselectAllObjectTypes();
		ser.setTypeCALE(true);
		ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		
		if(ObjectName.equals("*")){
			//ser.setName("");
		}else{
			ser.setName(ObjectName);
		}
		
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
	
	public int searchObjectForUsageCount(String ObjectName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		SearchObject ser = new SearchObject();
		ser.selectAllObjectTypes();
		ser.setSearchUseOfObjects(true);
		//ser.setSearchLocation(broker.folders.getRootFolder().fullPath(), true);
		ser.setName(ObjectName);
		ser.setIncludeUseInScripts(false);
		connection.sendRequestAndWait(ser);
		Iterator<SearchResultItem> it =  ser.resultIterator();
		int count = 0;
		while(it.hasNext()){
			count++;
			it.next();
		}
		return count;
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
	
	public void replaceObject(String SourceObjectName, String TargetObjectName) throws IOException{
		
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
			System.out.println(" -- "+rep.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Object "+ SourceObjectName +" Successfully Replaced by: "+TargetObjectName);
		}

	}
	public void renameObject(String SourceObjectName, String TargetObjectName, IFolder folder) throws IOException{
		UC4ObjectName sourceName = new UC4ObjectName(SourceObjectName);
		UC4ObjectName targetName = new UC4ObjectName(TargetObjectName);
		String SourceObjectTitle = "";
		List<SearchResultItem> items = searchObject(SourceObjectName);
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

		// last boolean returns an OpenObject
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

	public void reclaimObject(String ObjectName) throws TimeoutException, IOException{
		if(ObjectName.contains("/")){
			UC4UserName objName = new UC4UserName(ObjectName);
			ResetOpenFlag req = new ResetOpenFlag(objName);
			connection.sendRequestAndWait(req);	
			if (req.getMessageBox() != null) {
				System.err.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
			}else{
				Say(" \t ++ Object: "+objName+" Successfully reclaimed");
			}
		}else{
			UC4ObjectName objName = new UC4ObjectName(ObjectName);
			ResetOpenFlag req = new ResetOpenFlag(objName);
			connection.sendRequestAndWait(req);	
			if (req.getMessageBox() != null) {
				System.err.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
			}else{
				Say(" \t ++ Object: "+objName+" Successfully reclaimed");
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
			Say(" \t ++ Object: "+obj.getName()+" Successfully closed");
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
	
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public int executeObjectNow(String name) throws IOException {

		Say(" \t ++ Executing object Now: "+name);

		ExecuteObject execute = new ExecuteObject(new UC4ObjectName(name));
		connection.sendRequestAndWait(execute);

		if (execute.getMessageBox() != null || execute.getRunID() == 0) {
			if (execute.getMessageBox() != null) System.err.println(" -- "+execute.getMessageBox().getText().toString().replace("\n", ""));
			//System.out.println("-- Failed to execute object:"+name + ":" +execute.getMessageBox().getText());
		}else{	
			Say(" \t ++ Object: "+name+"++ Successfully executed with Run ID: "+execute.getRunID());
		}
		return execute.getRunID();
	}
	
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public int executeObjectOnce(String name, String timezone, DateTime startDate, DateTime logicalDate) throws IOException {

		Say(" \t ++ Executing object Once: "+name);

		ExecuteObject execute = new ExecuteObject(new UC4ObjectName(name));
		//DateTime startDate = DateTime.now().addDays(1);
		//DateTime logicalDate = DateTime.now().addDays(2);
		execute.executeOnce(startDate, logicalDate, new UC4TimezoneName(timezone), false, null);
		//execute.executeOnce(startDate, logicalDate, new UC4TimezoneName("TZ.ANG"), false, null);
		connection.sendRequestAndWait(execute);

		if (execute.getMessageBox() != null || execute.getRunID() == 0) {
			if (execute.getMessageBox() != null) System.err.println(" -- "+execute.getMessageBox().getText().toString().replace("\n", ""));
			//System.out.println("-- Failed to execute object:"+name + ":" +execute.getMessageBox().getText());
		}else{		
			Say(" \t ++ Object: "+name+" Successfully executed with Run ID: "+execute.getRunID());
		}
		return execute.getRunID();
	}
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public int executeObjectRecurring(String name, ExecuteRecurring recurringPattern) throws IOException {

		Say(" \t ++ Executing object Recurring: "+name);

		ExecuteObject execute = new ExecuteObject(new UC4ObjectName(name));
		//ExecuteRecurring rec = new ExecuteRecurring();
		//rec.setExecutionInterval(1);
		
		execute.executeRecurring(recurringPattern);
		
		connection.sendRequestAndWait(execute);

		if (execute.getMessageBox() != null || execute.getRunID() == 0) {
			if (execute.getMessageBox() != null) System.err.println(" -- "+execute.getMessageBox().getText().toString().replace("\n", ""));
			//System.out.println("-- Failed to execute object:"+name + ":" +execute.getMessageBox().getText());
		}else{		
			Say(" \t ++ Object: "+name+" Successfully executed with Run ID: "+execute.getRunID());
		}
		return execute.getRunID();
	}
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
		Say(" \t ++ Creating object: "+name+" of Type: "+template.getType());

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
		//Say(" \t ++ Deleting object: "+name);

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
	
	// Cancel an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public void cancelObject(int rundId) throws IOException {
		System.out.print("Cancel RundId("+ rundId +") ... ");
		CancelTask cancel = new CancelTask(rundId, false);
		connection.sendRequestAndWait(cancel);
		if (cancel.getMessageBox() != null) {
			System.out.println(cancel.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" \t ++ Object with RUNID: "+rundId+" Successfully Cancelled.");
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
	public int getSearchCount(String namePattern) throws IOException {
		SearchObject s = new SearchObject();		
		s.selectAllObjectTypes();		
		s.setTypeFOLD(true);		
		s.setName(namePattern);
		connection.sendRequestAndWait(s);
		return s.size();
	}
}
