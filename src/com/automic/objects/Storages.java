package com.automic.objects;

import java.io.IOException;
import java.util.Iterator;

import com.uc4.api.objects.Login;
import com.uc4.api.objects.LoginDefinition;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Storages extends ObjectTemplate{
	public Storages(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public void getStorageContent(String StorageName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		System.out.println("!! No Implementation for Storage Object for now..");
	}
}
