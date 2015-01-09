package com.automic.factories;

import com.uc4.communication.Connection;

/**
 * 
 * @author bsp
 * purpose: Factory Brokers and factories in general are implemented to handle multi/cross client operations
 * ex: Copy objects from Client A to Client B
 * 
 * Each Operation should implement it's own Factory (ex: Export / Import objects is contained in one ExportImportFactory class)
 *
 */
public class FactoryBroker {
	private Connection[] connections;
	
	public ExportImportFactory exportImportFactory;

	public FactoryBroker(Connection[] connections, boolean verbose){
		this.connections = connections;
		
		exportImportFactory = new ExportImportFactory(this.connections,verbose);
	}
	
}
