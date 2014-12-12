package com.automic.factories;

import com.uc4.communication.Connection;

public class FactoryTemplate {

	protected Connection[] connections;
	protected boolean  verbose = false;
	//ObjectBroker broker;
	
	public FactoryTemplate(Connection[] connections, boolean verbose){
		this.verbose = verbose;
		this.connections = connections;
		//broker = new ObjectBroker(this.connection,true);
	}
	
	public void Say(String s){
		if(this.verbose){
			System.out.println(s);
		}
	}
}
