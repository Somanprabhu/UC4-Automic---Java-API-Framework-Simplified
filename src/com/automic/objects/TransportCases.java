package com.automic.objects;

import java.io.IOException;

import com.automic.utils.Utils;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.ClearTransportCase;
import com.uc4.communication.requests.TransportObject;

public class TransportCases extends ObjectTemplate {
	public TransportCases(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public boolean clearTransportCase() throws IOException{
		ClearTransportCase req = new ClearTransportCase();
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Transport Case Cleared Successfully."));
			return true;
		}
		return false;
	}
	
	public boolean addObjectToTransportCase(UC4Object obj) throws IOException{
		TransportObject req = new TransportObject(obj);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object: "+obj.getName()+" was successfully added to Transport Case."));
			return true;
		}
		return false;
	}
	
	public void addObjectsToTransportCase(UC4Object[] objects) throws IOException{
		for(int i=0;i<objects.length;i++){
			addObjectToTransportCase(objects[i]);
		}
	}
	
	public IFolder getTransportCaseFolder() throws IOException{
		return getBrokerInstance().folders.getTransportCaseFolder();
	}
}
