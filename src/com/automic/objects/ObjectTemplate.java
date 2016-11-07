package com.automic.objects;

import java.io.IOException;

import com.automic.utils.Utils;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.XMLRequest;

public class ObjectTemplate {

	protected Connection connection;
	protected boolean  verbose = false;
	//ObjectBroker broker;
	
	public ObjectTemplate(Connection conn, boolean verbose){
		this.verbose = verbose;
		this.connection = conn;
		//broker = new ObjectBroker(this.connection,true);
	}
	
	// Sends a generic XMLRequest to the engine
		public XMLRequest sendGenericXMLRequestAndWait(XMLRequest req) throws TimeoutException, IOException{
			if(!verbose){return sendGenericXMLRequestAndWait(req,false);}
			else{return sendGenericXMLRequestAndWait(req,true);}
			
		}
		
		// Sends a generic XMLRequest to the engine with or without showing the response
		public XMLRequest sendGenericXMLRequestAndWait(XMLRequest req,boolean showResponse) throws TimeoutException, IOException{
			connection.sendRequestAndWait(req);
			if (req.getMessageBox() != null) {
				if(showResponse){System.out.println(Utils.getErrorString(req.getMessageBox()));}
				return null;
			}
			return req;
		}
		
	public void setVerbose(boolean v){
		this.verbose = v;
	}
	
	public void hideMessages(){
		this.verbose = false;
	}
	
	public void showMessages(){
		this.verbose = true;
	}
	
	public void Say(String s){
		if(this.verbose){
			System.out.println(s);
		}
	}
	
	
}
