package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.automic.utils.Utils;
import com.uc4.api.FolderListItem;
import com.uc4.api.Template;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.UC4TimezoneName;
import com.uc4.api.UC4UserName;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.CreateObject;
import com.uc4.communication.requests.DeleteObject;
import com.uc4.communication.requests.FolderList;
import com.uc4.communication.requests.FolderTree;

public class Folders extends ObjectTemplate{

	public Folders(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	@SuppressWarnings("unused")
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public IFolder getFolderFromObject(UC4Object object){return (IFolder) object;}
	
	public IFolder getRootFolder() throws IOException{
		FolderTree tree = new FolderTree();
		this.connection.sendRequestAndWait(tree);
		return tree.root();		
	}
	
	public FolderTree getFolderTree() throws IOException{
		FolderTree tree = new FolderTree();
		this.connection.sendRequestAndWait(tree);
		return tree;	
	}
	
	// Returns a list of ALL Folders (including folders in folders, folders in folders in folders etc.)
	public ArrayList<IFolder> getAllFolders(boolean OnlyExtractFolderObjects) throws IOException{
		return getFoldersRecursively(getRootFolder(), OnlyExtractFolderObjects);
	}
	
	// Internal Method
	private void addFoldersToList(ArrayList<IFolder> folderList,
			IFolder myFolder, boolean onlyExtractFolderObjects) {
		if(onlyExtractFolderObjects){
			if( myFolder.getType().equals("FOLD")){folderList.add(myFolder);}
			if( myFolder.getType().equals("FOLD") && myFolder.subfolder() != null){	
				Iterator<IFolder> it0 = myFolder.subfolder();
				while (it0.hasNext()){
					addFoldersToList(folderList,it0.next(),onlyExtractFolderObjects);
				}
			}
		}else{
			folderList.add(myFolder);
			if(  myFolder.subfolder() != null){
				Iterator<IFolder> it0 = myFolder.subfolder();
				while (it0.hasNext()){
					addFoldersToList(folderList,it0.next(),onlyExtractFolderObjects);
				}
			}
		}

	}
	
	public IFolder getVersionControlFolder() throws IOException{
		 ArrayList<IFolder> allFolders = getAllFolders(false);
		 for(IFolder folder : allFolders){
			 if(folder.getType().equalsIgnoreCase("VERSCONTROL")){return folder;}
		 }
		 return null;
	}
	
	public IFolder getNoFolderFolder() throws IOException{
		return getFolderTree().getNoFolder();
	}
	
	public IFolder getFavoritesFolder() throws IOException{
		 ArrayList<IFolder> allFolders = getAllFolders(false);
		 for(IFolder folder : allFolders){
			 if(folder.getType().equalsIgnoreCase("FAV")){return folder;}
		 }
		 return null;
	}
	
	public IFolder getRecentFolder() throws IOException{
		 ArrayList<IFolder> allFolders = getAllFolders(false);
		 for(IFolder folder : allFolders){
			 if(folder.getType().equalsIgnoreCase("RCNT")){return folder;}
		 }
		 return null;
	}
	
	public IFolder getRecycleBinFolder() throws IOException{
		 ArrayList<IFolder> allFolders = getAllFolders(false);
		 for(IFolder folder : allFolders){
			 if(folder.getType().equalsIgnoreCase("TRASH")){return folder;}
		 }
		 return null;
	}
	
	public IFolder getTransportCaseFolder() throws IOException{
		 ArrayList<IFolder> allFolders = getAllFolders(false);
		 for(IFolder folder : allFolders){
			 if(folder.getType().equalsIgnoreCase("TRANSCASE")){return folder;}
		 }
		 return null;
	}
	
	// Returns a folder by name, can be passed a Full path or just a folder name
	public IFolder getFolderByName(String FolderName) throws IOException{
		
		 if(FolderName.contains("<No Folder>") || FolderName.contains("<no folder>") || FolderName.contains("<NO FOLDER>")
				 || FolderName.equalsIgnoreCase("NO FOLDER")){
			 return getNoFolderFolder();
		 }
		 
		// Bug Fix: '/' isnt enough to establish that a full path is passed..
		 if(FolderName.matches("\\d{4}") || FolderName.contains("-") || (FolderName.contains("/") && !FolderName.contains("No Folder"))){
			 return getFolderByFullPathName(FolderName);
		 }
		
		ArrayList<IFolder> foundFolders = new ArrayList<IFolder>();
		 ArrayList<IFolder> allFolders = getAllFolders(true);
		 for(IFolder folder : allFolders){
			 if(folder.getName().equalsIgnoreCase(FolderName)){
				 foundFolders.add(folder);
				 }
		 }
		 if(foundFolders.size() == 1){return foundFolders.get(0);}
		 else{
			 System.out.println(Utils.getWarningString(foundFolders.size()+" Folder(s) Found corresponding to name: " + FolderName));
			 if(foundFolders.size() == 0){ 
				 // No folder found
				 System.out.println(Utils.getWarningString("=> Are you sure the Folder specified exists in the Target Client?"));
			 }else{
				 // Too many folders found
				 for(int i=0;i<foundFolders.size();i++){
					 System.out.println("  ----> "+foundFolders.get(i).fullPath());
				 }
				 System.out.println(Utils.getWarningString("  => Please select only one folder. HINT: Try passing the full path name. Ex: \"0002/CUSTOM.DEMOS/ARCHIVE/ABC/JOBS\" or \"SWINVM1 - 0002/CUSTOM.DEMOS/ARCHIVE/ABC/JOBS\""));
			 }
		 }
		 return null;
	}
	
	// below method takes as an input either "AEV10 - 0005/UC4.APPLICATIONS/JFORUM_BREN"
	// or simply: "0005/UC4.APPLICATIONS/JFORUM_BREN"
	public IFolder getFolderByFullPathName(String FolderName) throws IOException{
		if(FolderName.contains("<No Folder>")){return getNoFolderFolder();}
		
		 ArrayList<IFolder> allFolders = getAllFolders(true);
		 for(IFolder folder : allFolders){
			 String FullPath = "";
			 // the IFolder.fullpath() method ALWAYS returns the system name before the path (ex: "AEV10 - 0005/UC4.APPLICATIONS/JFORUM_BREN")
			 // therefore it is necessary to modify it for comparison.. if the FolderName passed does not also contain the system name..
			 if(!FolderName.contains(" - ")){
				 FullPath = folder.fullPath().split(" - ")[1].trim();
			 }else{
				 FullPath = folder.fullPath().trim();
			 }
		
			 if(FullPath.equalsIgnoreCase(FolderName.trim())){
				 
				 return folder;
			}
		 }
		 
		 return null;
	}
	
	// Returns a FolderList = the content of a given folder
	public FolderList getFolderContentWithTypes(IFolder folder, List<String> ObjectTypes) throws IOException{	
		FolderList objectsInRootFolder = new FolderList(folder,ObjectTypes);
		connection.sendRequestAndWait(objectsInRootFolder);
		return objectsInRootFolder;
	}
	
	// Returns a FolderList = the content of a given folder
	public FolderList getFolderContent(IFolder folder) throws IOException{
		FolderList objectsInRootFolder = new FolderList(folder);
		connection.sendRequestAndWait(objectsInRootFolder);
		return objectsInRootFolder;
	}
	
	public FolderList showFolderContent(IFolder folder) throws IOException{
		FolderList objectsInRootFolder = new FolderList(folder);
		connection.sendRequestAndWait(objectsInRootFolder);
		if(objectsInRootFolder != null){
			Iterator<FolderListItem> it = objectsInRootFolder.iterator();
			while(it.hasNext()){
				FolderListItem item = it.next();
				String NAME = item.getName();
				if(NAME.equals("LOGIN.WIN01")){System.out.println("Content Found: " + item.getName()+":"+item.getTitle()+":"+folder.getName());}
				//System.out.println("Content Found: " + item.getName()+":"+item.getTitle());
			}
		}
		return objectsInRootFolder;
	}
	
	public void showSubFolders(IFolder folder) throws IOException{
		Iterator<IFolder> subfolderIT = folder.subfolder();
		if(subfolderIT!=null){
			while(subfolderIT.hasNext()){
				IFolder f = subfolderIT.next();
				System.out.println("Subfolder:" + f.getName()+":"+f.getType() );
			}
		}
	}
	
	// Same as above, requires only a folder name to run
	public FolderList getFolderContentByName(String FolderName) throws IOException{	
		return this.getFolderContent(this.getFolderByName(FolderName));
	}
	
	// Delete a Folder
	public boolean deleteFolder(IFolder fold) throws IOException {
		//System.out.print("Deleting folder "+ fold.getName() + " ... ");
		DeleteObject req = new DeleteObject(fold);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Folder: "+fold.fullPath()+" Successfully Deleted."));
			return true;
		}
		return false;
	}
	
	// Create a Folder
	public boolean createFolder(String FolderName, IFolder fold) throws IOException {
		return getBrokerInstance().common.createObject(FolderName, "FOLD", fold);
	}
	
	@Deprecated
	public boolean createFolderSilently(String name, IFolder fold) throws IOException {
		UC4ObjectName objName = null;
		if (name.indexOf('/') != -1) objName = new UC4UserName(name);
		else objName = new UC4ObjectName(name);		

		CreateObject create = new CreateObject(objName,Template.FOLD,fold);
		connection.sendRequestAndWait(create);
		if (create.getMessageBox() != null) {
			if(!create.getMessageBox().getText().contains("already")){
				System.out.println(create.getMessageBox().getText().toString().replace("\n", ""));
				return false;
			}
		}else{
			//Say("\t ++ Folder: "+name+" Successfully created.");
			return true;
		}
		return false;
	}
	
	// Returns a list of ALL Folders (including folders in folders, folders in folders in folders etc.)
	public ArrayList<IFolder> getFoldersRecursively(IFolder rootFolder, boolean OnlyExtractFolderObjects ) throws IOException{
		
		ArrayList<IFolder> FolderList = new ArrayList<IFolder>();
		if(!OnlyExtractFolderObjects){FolderList.add(getRootFolder());}
		FolderList.add(rootFolder);
		Iterator<IFolder> it = rootFolder.subfolder();
		
		if(it != null){
			while (it.hasNext()){
				IFolder myFolder = it.next();
				if(! myFolder.getName().equals("<No Folder>")){
					addFoldersToList(FolderList,myFolder,OnlyExtractFolderObjects);
				}
			}
		}
	
		return FolderList; 
	}
	
}
