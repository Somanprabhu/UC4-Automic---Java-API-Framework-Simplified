package com.automic.objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.uc4.api.PlatformSwHwType;
import com.uc4.api.Template;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.ResourceItem;
import com.uc4.api.objects.ResourceItems;
import com.uc4.api.objects.Storage;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.DeleteStorageEntry;
import com.uc4.communication.requests.GetChangeLog;
import com.uc4.communication.requests.UploadBinary;
import com.uc4.communication.requests.UploadBinary.ContentType;

// Following only works in AE v11 and up... Storage object created in v11.
public class Storages extends ObjectTemplate{
	public Storages(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}

	public Storage getStorageFromObject(UC4Object object){return (Storage) object;}
	public void viewStorageContent(String StorageName) throws IOException{
		UC4Object obj = getBrokerInstance().common.openObject(StorageName, true);
		Storage store = getStorageFromObject(obj);
		Iterator<ResourceItem> it = store.resourceItems().resourceItemsIterator();
		System.out.println(" ++ Storage Content for object: "+StorageName);
		while(it.hasNext()){
			ResourceItem item = it.next();
			System.out.println("  => "+item.toString());
		}
	}
	
	public ArrayList<ResourceItem> getStorageContent(String StorageName) throws IOException{
		UC4Object obj = getBrokerInstance().common.openObject(StorageName, true);
		Storage store = getStorageFromObject(obj);
		Iterator<ResourceItem> it = store.resourceItems().resourceItemsIterator();
		ArrayList<ResourceItem> resCollection = new ArrayList<ResourceItem>();
		while(it.hasNext()){
			ResourceItem item = it.next();
			resCollection.add(item);
		}
		return resCollection;
	}
	
	public void createStorage(String ObjectName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(ObjectName, Template.STORE, FolderLocation);
	}
	
	public ArrayList<ResourceItem>  getResourcesAsArray(Storage store){
		ArrayList<ResourceItem> ResItemArray = new ArrayList<ResourceItem>();
		Iterator<ResourceItem> resIt = store.resourceItems().resourceItemsIterator();
		while(resIt.hasNext()){
			ResourceItem item = resIt.next();
			ResItemArray.add(item);
		}
		return ResItemArray;
	}
	
	public ResourceItems getResources(Storage store){
		return store.resourceItems();

	}
	
	public DeleteStorageEntry deleteStorageEntry(Storage store, ResourceItem item) throws TimeoutException, IOException{
		DeleteStorageEntry req = new DeleteStorageEntry(item);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println("\t -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		return req;
	}
	
	private UploadBinary uploadStorageEntry(String storageName, String entryName, ContentType contentType,boolean isUpdate, String version, PlatformSwHwType platform, String pathToBinaryFile ) throws TimeoutException, IOException{
		// (UC4ObjectName storeObject, java.lang.String entryName, PlatformSwHwType osPlatformHw, boolean isUpdate, java.io.File inputFile
				File inputFile = new File(pathToBinaryFile);
				UC4ObjectName StorageObjName = new UC4ObjectName(storageName);
				UploadBinary req = new UploadBinary(StorageObjName, entryName,platform,isUpdate,inputFile);
				req.setVersion(version);
				req.setContentType(contentType);
				// the File Name in the Entry can technically be different from the actual filename.. but why bother?
				req.setFileName(inputFile.getName());
				connection.sendRequestAndWait(req);
				if (req.getMessageBox() != null) {
					System.out.println("\t -- "+req.getMessageBox().getText().toString().replace("\n", ""));
				}
				
				return req;
	}
		
	public UploadBinary updateStorageEntryBinary(String storageName, String entryName, String pathToBinaryFile, String version ) throws TimeoutException, IOException{
		// (UC4ObjectName storeObject, java.lang.String entryName, PlatformSwHwType osPlatformHw, boolean isUpdate, java.io.File inputFile
		UploadBinary req = uploadStorageEntry(storageName, entryName, ContentType.BINARY, true, version, PlatformSwHwType.ALL, pathToBinaryFile);
		return req; 
	}
	public UploadBinary addStorageEntryBinary(String storageName, String entryName, String pathToBinaryFile, String version ) throws TimeoutException, IOException{
		// (UC4ObjectName storeObject, java.lang.String entryName, PlatformSwHwType osPlatformHw, boolean isUpdate, java.io.File inputFile
		UploadBinary req = uploadStorageEntry(storageName, entryName, ContentType.BINARY, false, version, PlatformSwHwType.ALL, pathToBinaryFile);
		return req; 
	}
	public UploadBinary updateStorageEntryText(String storageName, String entryName, String pathToTextFile, String version ) throws TimeoutException, IOException{
		// (UC4ObjectName storeObject, java.lang.String entryName, PlatformSwHwType osPlatformHw, boolean isUpdate, java.io.File inputFile
		UploadBinary req = uploadStorageEntry(storageName, entryName, ContentType.TEXT, true, version, PlatformSwHwType.ALL, pathToTextFile);
		return req; 
	}
	public UploadBinary addStorageEntryText(String storageName, String entryName, String pathToTextFile, String version ) throws TimeoutException, IOException{
		// (UC4ObjectName storeObject, java.lang.String entryName, PlatformSwHwType osPlatformHw, boolean isUpdate, java.io.File inputFile
		UploadBinary req = uploadStorageEntry(storageName, entryName, ContentType.TEXT, false, version, PlatformSwHwType.ALL, pathToTextFile);
		return req; 
	}
}
