package com.automic.objects;

import java.io.IOException;

import com.uc4.api.objects.Login;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.GenericStatistics;
import com.uc4.communication.requests.TemplateList;

public class Statistics  extends ObjectTemplate{

	public Statistics(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public GenericStatistics getGenericStatistics(int Client, String Agentname) throws TimeoutException, IOException{
		GenericStatistics req = new GenericStatistics();
		req.selectAllPlatforms();
		req.selectAllTypes();
		req.setClient(Client);
		req.setSourceHost(Agentname);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
		return req;
		
	}
	
}
