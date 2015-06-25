package com.automic.objects;

import com.uc4.communication.Connection;

public class SAPQueueManager extends ObjectTemplate{
	public SAPQueueManager(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	@SuppressWarnings("unused")
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
}
