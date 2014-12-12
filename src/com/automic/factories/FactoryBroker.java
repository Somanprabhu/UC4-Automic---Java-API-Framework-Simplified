package com.automic.factories;

import com.uc4.communication.Connection;

public class FactoryBroker {
	private Connection[] connections;
	
	public ExportImportFactory exportImportFactory;

	public FactoryBroker(Connection[] connections, boolean verbose){
		this.connections = connections;
		
		exportImportFactory = new ExportImportFactory(this.connections,verbose);
	}
	
}
