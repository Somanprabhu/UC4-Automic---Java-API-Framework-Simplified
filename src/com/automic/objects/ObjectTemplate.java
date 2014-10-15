package com.automic.objects;

import com.uc4.communication.Connection;

public class ObjectTemplate {

	protected Connection connection;
	protected boolean  verbose = false;
	//ObjectBroker broker;
	
	public ObjectTemplate(Connection conn, boolean verbose){
		this.verbose = verbose;
		this.connection = conn;
		//broker = new ObjectBroker(this.connection,true);
	}
	
	public void Say(String s){
		if(this.verbose){
			System.out.println(s);
		}
	}
	
	
}
