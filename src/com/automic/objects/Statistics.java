package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;

import com.uc4.api.DateTime;
import com.uc4.api.StatisticSearchItem;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.Login;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.GenericStatistics;
import com.uc4.communication.requests.GetLastRuntimes;
import com.uc4.communication.requests.ObjectStatistics;
import com.uc4.communication.requests.TemplateList;

public class Statistics  extends ObjectTemplate{

	public Statistics(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public GetLastRuntimes getLastRuntimes(String ObjName) throws TimeoutException, IOException{
		GetLastRuntimes req = new GetLastRuntimes(new UC4ObjectName(ObjName));
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		return req;
	}
	
	public ObjectStatistics getObjectStatistics(String ObjName) throws TimeoutException, IOException{
		ObjectStatistics req = new ObjectStatistics(new UC4ObjectName(ObjName));
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		return req;
	}
	public GenericStatistics getGenericStatistics(int Client, String Agentname) throws TimeoutException, IOException{

		GenericStatistics req = new GenericStatistics();
		req.selectAllPlatforms();
		req.selectAllTypes();
		req.setClient(Client);
		req.setSourceHost(Agentname);
		
		connection.sendRequestAndWait(req);
		
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		return req;
		
	}
		public int getGenericStatisticsCount(int Client, String Agentname) throws TimeoutException, IOException{

			GenericStatistics req = new GenericStatistics();
			req.selectAllPlatforms();
			req.selectAllTypes();
			req.setClient(Client);
			req.setSourceHost(Agentname);

			connection.sendRequestAndWait(req);
			
			if (req.getMessageBox() != null && req.getMessageBox().getText().toString().contains("too many statistics")) {
				//System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
				// -- Your selection results in too many statistics (count '44347').
				 String toProc = req.getMessageBox().getText().toString();
				 // Extracting the count returned
				 String processed = toProc.replace("5000", "").replaceAll("[^0-9]","");
				
				return Integer.parseInt(processed);
				
			}
			
			if (req.getMessageBox() != null) {
				System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
			}
			return req.size();
		}
	
}
