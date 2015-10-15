package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
	
	public IFolder getVersionNoFolderFolder() throws IOException{
		 ArrayList<IFolder> allFolders = getAllFolders(false);
		 for(IFolder folder : allFolders){
			 if(folder.getType().equalsIgnoreCase("NFOLD")){return folder;}
		 }
		 return null;
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
		 if(FolderName.contains("-") || FolderName.contains("/")){
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
			 System.out.println(" -- Error: "+foundFolders.size()+" Found corresponding to name:" + FolderName);
			 for(int i=0;i<foundFolders.size();i++){
				 System.out.println("  ----> "+foundFolders.get(i).fullPath());
			 }
			 System.out.println("  => Please select only one folder. HINT: Try passing the full path name. Ex: \"0002/CUSTOM.DEMOS/ARCHIVE/ABC/JOBS\" or \"SWINVM1 - 0002/CUSTOM.DEMOS/ARCHIVE/ABC/JOBS\"");
			 System.exit(1);
		 }
		 return null;
	}
	
	// below method takes as an input either "AEV10 - 0005/UC4.APPLICATIONS/JFORUM_BREN"
	// or simply: "0005/UC4.APPLICATIONS/JFORUM_BREN"
	public IFolder getFolderByFullPathName(String FolderName) throws IOException{
		 ArrayList<IFolder> allFolders = getAllFolders(true);
		 for(IFolder folder : allFolders){
			 String FullPath = "";
			 // the IFolder.fullpath() method ALWAYS returns the system name before the path (ex: "AEV10 - 0005/UC4.APPLICATIONS/JFORUM_BREN")
			 // therefore it is necessary to modify it for comparison.. if the FolderName passed does not also contain the system name..
			 if(!FolderName.contains(" - ")){
				 FullPath = folder.fullPath().split(" - ")[1];
			 }else{
				 FullPath = folder.fullPath();
			 }
			 if(FullPath.equalsIgnoreCase(FolderName)){return folder;}
		 }
		 return null;
	}
	
	// Returns a FolderList = the content of a given folder
	public FolderList getFolderContent(IFolder folder) throws IOException{
		FolderList objectsInRootFolder = new FolderList(folder);
		connection.sendRequestAndWait(objectsInRootFolder);
		return objectsInRootFolder;
	}
	
	// Same as above, requires only a folder name to run
	public FolderList getFolderContentByName(String FolderName) throws IOException{	
		return this.getFolderContent(this.getFolderByName(FolderName));
	}
	
	// Delete a Folder
	public void deleteFolder(IFolder fold) throws IOException {
		//System.out.print("Deleting folder "+ fold.getName() + " ... ");
		DeleteObject delete = new DeleteObject(fold);
		connection.sendRequestAndWait(delete);	
		if (delete.getMessageBox() != null) {
			System.out.println(" -- "+delete.getMessageBox().getText().toString().replace("\n", ""));
			//System.out.println("Failed to delete object:"+fold.fullPath());
		}else{
			Say(" ++ Folder: "+fold.fullPath()+" Successfully Deleted.");
		}
	}
	// Create a Folder
	public void createFolder(String FolderName, IFolder fold) throws IOException {
		
		getBrokerInstance().common.createObject(FolderName, "FOLD", fold);

	}
	
	// Returns a list of ALL Folders (including folders in folders, folders in folders in folders etc.)
	public ArrayList<IFolder> getFoldersRecursively(IFolder rootFolder, boolean OnlyExtractFolderObjects ) throws IOException{
		ArrayList<IFolder> FolderList = new ArrayList<IFolder>();
		if(!OnlyExtractFolderObjects){FolderList.add(getRootFolder());}
		Iterator<IFolder> it = rootFolder.subfolder();
		while (it.hasNext()){
			IFolder myFolder = it.next();
			if(! myFolder.getName().equals("<No Folder>")){
				addFoldersToList(FolderList,myFolder,OnlyExtractFolderObjects);
			}
		}
		return FolderList; 
	}
}
