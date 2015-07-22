package com.automic.objects;

import java.io.IOException;

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
	
	public void clearTransportCase() throws IOException{
		ClearTransportCase req = new ClearTransportCase();
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say("++ Transport Case Cleared Successfully.");
		}
		
	}
	public void addObjectToTransportCase(UC4Object obj) throws IOException{
		TransportObject req = new TransportObject(obj);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say("++ Object: "+obj.getName()+" was successfully added to Transport Case.");
		}
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
