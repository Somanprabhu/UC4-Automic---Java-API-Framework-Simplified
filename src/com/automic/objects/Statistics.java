package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.automic.utils.Utils;
import com.uc4.api.DateTime;
import com.uc4.api.SearchResultItem;
import com.uc4.api.StatisticSearchItem;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.Login;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.GenericStatistics;
import com.uc4.communication.requests.GetLastRuntimes;
import com.uc4.communication.requests.ObjectStatistics;
import com.uc4.communication.requests.SearchObject;
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
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString(""));
			return req;
		}else{
			Say(Utils.getErrorString("Error: "  + req.getMessageBox().getText()));
		}
		return req;
	}

	public StatisticSearchItem getObjectLastStatistics(String ObjName) throws TimeoutException, IOException{
		ObjectStatistics req = new ObjectStatistics(new UC4ObjectName(ObjName));
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString(""));
			Iterator<StatisticSearchItem> it = req.iterator();
			StatisticSearchItem lastItem = it.next();
			return lastItem;
		}else{
			Say(Utils.getErrorString("Error: "  + req.getMessageBox().getText()));
		}
		return null;
	}
	
	public ObjectStatistics getObjectStatistics(String ObjName) throws TimeoutException, IOException{
		ObjectStatistics req = new ObjectStatistics(new UC4ObjectName(ObjName));
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString(""));
			return req;
		}else{
			Say(Utils.getErrorString("Error: "  + req.getMessageBox().getText()));
		}
		return req;
	}
	
public void setGenericStatisticsObjectFilter(GenericStatistics req, String ObjectTypes) throws IOException{
		req.unselectAllTypes();
		if(ObjectTypes==null || ObjectTypes.equals("*")){
			req.selectAllTypes();
		}else{
			if("JOBP".matches(ObjectTypes)){req.setTypeJOBP(true);}
			if("CALL".matches(ObjectTypes)){req.setTypeCALL(true);}
			if("CLNT".matches(ObjectTypes)){req.setTypeCLNT(true);}
			if("CPIT".matches(ObjectTypes)){req.setTypeCPIT(true);}
			if("HOST".matches(ObjectTypes)){req.setTypeHOST(true);}
			if("JOBF".matches(ObjectTypes)){req.setTypeJOBF(true);}
			if("JOBG".matches(ObjectTypes)){req.setTypeJOBG(true);}
			if("JOBQ".matches(ObjectTypes)){req.setTypeJOBQ(true);}
			if("JOBS".matches(ObjectTypes)){req.setTypeJOBS(true);}
			if("JSCH".matches(ObjectTypes)){req.setTypeJSCH(true);}
			if("SCRI".matches(ObjectTypes)){req.setTypeSCRI(true);}
			if("SERV".matches(ObjectTypes)){req.setTypeSERV(true);}
			if("SYNC".matches(ObjectTypes)){req.setTypeSYNC(true);}
			if("USER".matches(ObjectTypes)){req.setTypeUSER(true);}
			if("API".matches(ObjectTypes)){req.setTypeAPI(true);}
			if("HOSTG".matches(ObjectTypes)){req.setTypeC_HOSTG(true);}
			if("PERIOD".matches(ObjectTypes)){req.setTypeC_PERIOD(true);}
			if("EVNT_CHILD".matches(ObjectTypes)){req.setTypeEVENT_CHILD(true);}
			if("EVNT".matches(ObjectTypes)){req.setTypeEVNT(true);}
			if("JOBD".matches(ObjectTypes)){req.setTypeJOBD(true);}
			if("REPORT".matches(ObjectTypes)){req.setTypeREPORT(true);}
		}
	}

public void setGenericStatisticsPlatformFilter(GenericStatistics req, String PlatformTypes) throws IOException{
	req.unselectAllPlatforms();
	if(PlatformTypes==null || PlatformTypes.equals("*")){
		req.selectAllPlatforms();
	}else{
		if("BS2000".matches(PlatformTypes)){req.setPlatformBS2000(true);}
		if("CIT".matches(PlatformTypes)){req.setPlatformCIT(true);}
		if("GSCOS8".matches(PlatformTypes)){req.setPlatformGCOS8(true);}
		if("JMX".matches(PlatformTypes)){req.setPlatformJMX(true);}
		if("MAIL".matches(PlatformTypes)){req.setPlatformMAIL(true);}
		if("MPE".matches(PlatformTypes)){req.setPlatformMPE(true);}
		if("MVS".matches(PlatformTypes)){req.setPlatformMVS(true);}
		if("NSK".matches(PlatformTypes)){req.setPlatformNSK(true);}
		if("OA".matches(PlatformTypes)){req.setPlatformOA(true);}
		if("OS400".matches(PlatformTypes)){req.setPlatformOS400(true);}
		if("PS".matches(PlatformTypes)){req.setPlatformPS(true);}
		if("R3".matches(PlatformTypes)){req.setPlatformR3(true);}
		if("SIEBEL".matches(PlatformTypes)){req.setPlatformSiebel(true);}
		if("SQL".matches(PlatformTypes)){req.setPlatformSQL(true);}
		if("UNIX".matches(PlatformTypes)){req.setPlatformUNIX(true);}
		if("VMS".matches(PlatformTypes)){req.setPlatformVMS(true);}
		if("WIN".matches(PlatformTypes)){req.setPlatformWindows(true);}
	}
}


	//"paltform1, platform 2"
	public GenericStatistics getGenericStatistics(GenericStatistics req) throws TimeoutException, IOException{
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString(""));
			return req;
		}else{
			Say(Utils.getErrorString("Error: "  + req.getMessageBox().getText()));
		}
		return req;
	}
	
	public GenericStatistics getGenericStatistics(String Agentname) throws TimeoutException, IOException{

		GenericStatistics req = new GenericStatistics();
		req.selectAllPlatforms();
		req.selectAllTypes();
		req.setSourceHost(Agentname);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString(""));
			return req;
		}else{
			Say(Utils.getErrorString("Error: "  + req.getMessageBox().getText()));
		}
		return req;
	}
	
	public GenericStatistics getGenericStatistics(int Client, String Agentname) throws TimeoutException, IOException{

		GenericStatistics req = new GenericStatistics();
		req.selectAllPlatforms();
		req.selectAllTypes();
		req.setClient(Client);
		req.setSourceHost(Agentname);
		
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString(""));
			return req;
		}else{
			Say(Utils.getErrorString("Error: "  + req.getMessageBox().getText()));
		}
		return req;
	}
		public int getGenericStatisticsCount(int Client, String Agentname) throws TimeoutException, IOException{

			GenericStatistics req = new GenericStatistics();
			req.selectAllPlatforms();
			req.selectAllTypes();
			req.setClient(Client);
			req.setSourceHost(Agentname);

			sendGenericXMLRequestAndWait(req);
			
			if (req.getMessageBox() != null && req.getMessageBox().getText().toString().contains("too many statistics")) {
				//System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
				// -- Your selection results in too many statistics (count '44347').
				 String toProc = req.getMessageBox().getText().toString();
				 // Extracting the count returned
				 String processed = toProc.replace("5000", "").replaceAll("[^0-9]","");
				
				return Integer.parseInt(processed);
			}			
			return req.size();			
		}
	
}
