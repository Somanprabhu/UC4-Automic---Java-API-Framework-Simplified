package com.automic.objects;

import java.io.IOException;

import com.automic.utils.Utils;
import com.uc4.api.objects.ServiceLevelObjective;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.ServiceLevelObjectiveList;

public class ServiceLevelObjectives extends ObjectTemplate{
	public ServiceLevelObjectives(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	@SuppressWarnings("unused")
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public ServiceLevelObjectiveList getSLOs() throws IOException{
		ServiceLevelObjectiveList req = new ServiceLevelObjectiveList();
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			return req;
		}
		return req;
	}
}
