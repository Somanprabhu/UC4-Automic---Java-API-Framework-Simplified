package com.automic.objects;

import java.io.IOException;

import com.uc4.api.FolderListItem;
import com.uc4.api.objects.IFolder;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.ForecastList;
import com.uc4.communication.requests.LinkTo;

public class Forecasts extends ObjectTemplate{
	public Forecasts(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public void getClientForecast() throws IOException{
		ForecastList req = new ForecastList();
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
	}
}
