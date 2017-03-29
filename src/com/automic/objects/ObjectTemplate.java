package com.automic.objects;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import com.automic.utils.Utils;
import com.uc4.communication.Connection;
import com.uc4.communication.ConnectionAttributes;
import com.uc4.communication.IResponseHandler;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.XMLRequest;
import com.uc4.util.XMLDocument;

public class ObjectTemplate implements IResponseHandler{

	protected Connection connection;
	protected boolean  verbose = false;
	private Object lock = new Object();
	private AtomicReference<XMLRequest> reqRef = new AtomicReference<XMLRequest>(null);
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
		
		// Sends a generic XMLRequest to the engine
		public XMLRequest sendGenericXMLRequest(XMLRequest req) throws TimeoutException, IOException{
			if(!verbose){return sendGenericXMLRequest(req,false);}
			else{return sendGenericXMLRequest(req,true);}
			
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
		
		// Sends a generic XMLRequest to the engine with or without showing the response
		public XMLRequest sendGenericXMLRequest(XMLRequest req,boolean showResponse) throws TimeoutException, IOException{
			
			connection.sendRequest(req,this);
			if (req.getMessageBox() != null) {
				if(showResponse){System.out.println(Utils.getErrorString(req.getMessageBox()));}
				return null;
			}else{
				
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
	
	@Override
	public void handleResponse(XMLDocument xml, ConnectionAttributes session) {
			reqRef.get().handleResponse(xml,session);	
			if (reqRef.get().getMessageBox() != null) System.out.print(reqRef.get().getMessageBox().getText());
		//	if (reqRef.get().getMessageBox() == null) System.out.print(" %% Success!");

	}

	@Override
	public void handleIOException(IOException e) {
		synchronized (lock) {
			boolean ioError = true;
			lock.notify();
		}		
	}
	
	
}
