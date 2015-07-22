package com.automic.objects;

import java.io.IOException;

import com.uc4.communication.Connection;
import com.uc4.communication.requests.ForecastList;

public class Forecasts extends ObjectTemplate{
	public Forecasts(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	@SuppressWarnings("unused")
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public ForecastList getClientForecast() throws IOException{
		ForecastList req = new ForecastList();
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		return req;
	}
}
