package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.uc4.api.Template;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.ResourceItem;
import com.uc4.api.objects.Storage;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

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
	
	public void createStorage(String JGroupName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(JGroupName, Template.STORE, FolderLocation);
	}
	
}
