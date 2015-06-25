package com.automic.objects;

import java.io.IOException;

import com.uc4.api.Template;
import com.uc4.api.objects.DatabaseConnection;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.RAConnection;
import com.uc4.api.objects.SAPConnection;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Connections extends ObjectTemplate{
	public Connections(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	// should contain stuff for all connection types: DatabaseConnection, RAConnection, SAPConnection
		public DatabaseConnection getDBConnectionFromObject(UC4Object object){return (DatabaseConnection) object;}
		public RAConnection getRAConnectionFromObject(UC4Object object){return (RAConnection) object;}
		public SAPConnection getSAPConnectionFromObject(UC4Object object){return (SAPConnection) object;}
		
		public void createDBConnection(String DBConnectionName, IFolder FolderLocation) throws IOException{
			ObjectBroker broker = getBrokerInstance();
			broker.common.createObject(DBConnectionName, Template.CONN_DB, FolderLocation);
		}
		public void createSAPConnection(String EventName, IFolder FolderLocation) throws IOException{
			ObjectBroker broker = getBrokerInstance();
			broker.common.createObject(EventName, Template.CONN_SAP, FolderLocation);
		}
		// below method is probably wrong...
		public void createRAConnectiont(String EventName, IFolder FolderLocation) throws IOException{
			ObjectBroker broker = getBrokerInstance();
			broker.common.createObject(EventName, Template.CONN_SQL, FolderLocation);
		}
	
		
		
}
