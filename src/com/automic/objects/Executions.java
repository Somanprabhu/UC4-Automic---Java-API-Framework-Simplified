package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.automic.utils.ReportTypeEnum;
import com.automic.utils.Utils;
import com.uc4.api.DateTime;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.UC4TimezoneName;
import com.uc4.api.objects.ExecuteRecurring;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.AddComment;
import com.uc4.communication.requests.GetComments;
import com.uc4.communication.requests.GetComments.Comment;
import com.uc4.communication.requests.CancelTask;
import com.uc4.communication.requests.ExecuteObject;
import com.uc4.communication.requests.LatestReport;
import com.uc4.communication.requests.Report;
import com.uc4.communication.requests.ReportTypeList;

public class Executions extends ObjectTemplate{

	public Executions(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	// Cancel an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
		public boolean cancelObject(int rundId) throws IOException {
			CancelTask req = new CancelTask(rundId, false);
			sendGenericXMLRequestAndWait(req);
			
			if (req.getMessageBox() == null) {
				Say(Utils.getSuccessString("Object with RUNID: "+rundId+" Successfully Cancelled."));
				return true;
			}
			return false;
		}
	
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public int executeObjectNow(String name) throws IOException {

		ExecuteObject req = new ExecuteObject(new UC4ObjectName(name));
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object: "+name+"++ Successfully executed with Run ID: "+req.getRunID()));
			return req.getRunID();
		}
		return req.getRunID();
		// RunID is 0 if failed.
	}
	
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public int executeObjectOnce(String name, String timezone, DateTime startDate, DateTime logicalDate) throws IOException {

		ExecuteObject req = new ExecuteObject(new UC4ObjectName(name));
		req.executeOnce(startDate, logicalDate, new UC4TimezoneName(timezone), false, null);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object: "+name+" Successfully executed with Run ID: "+req.getRunID()));
			return req.getRunID();
		}
		return req.getRunID();
	}
	
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public int executeObjectRecurring(String name, ExecuteRecurring recurringPattern) throws IOException {

		ExecuteObject req = new ExecuteObject(new UC4ObjectName(name));
		req.executeRecurring(recurringPattern);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object: "+name+" Successfully executed with Run ID: "+req.getRunID()));
			return req.getRunID();
		}
		return req.getRunID();
	}
	
	public int getLatestRunId(String ObjectName) throws IOException{
		UC4ObjectName objName = new UC4ObjectName(ObjectName);
		LatestReport req = new LatestReport(objName);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			return req.latestRunID();
		}
		return req.latestRunID();
	}
	
	public ReportTypeList getListOfReportTypesAvailableForRun(int RunID) throws IOException{
		ReportTypeList req = new ReportTypeList(RunID);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			return req;
		}
		return req;
	}
	
	public Report getReport(int RunID, String ReportType) throws IOException{
		Report req = new Report(RunID, ReportType);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			return req;
		}
		return req;
	}
	
	public Report getActivationReport(int RunID) throws IOException{
		Report req = new Report(RunID, ReportTypeEnum.ACTIVATION);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			return req;
		}
		return req;
	}
	
	public Report getRuntimeReport(int RunID) throws IOException{
		Report req = new Report(RunID, ReportTypeEnum.RUNTIME_REPORT);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			return req;
		}
		return req;
	}
	
	public Report getJobReportReport(int RunID) throws IOException{
		Report req = new Report(RunID, ReportTypeEnum.JOB_REPORT);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			return req;
		}
		return req;
	}
	
	public Report getPostProcessReportReport(int RunID) throws IOException{
		Report req = new Report(RunID, ReportTypeEnum.POST_REPORT);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			return req;
		}
		return req;
	}
	
	public boolean addCommentToRun(int RunID, String comment) throws IOException{
		AddComment req = new AddComment(RunID, comment);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			return true;
		}
		return false;
	}
	
	public ArrayList<Comment> getCommentsFromRun(int RunID) throws IOException{
		GetComments req = new GetComments(RunID);
		ArrayList<Comment> allComments = new ArrayList<Comment>();
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Iterator <Comment> it = req.iterator();
			while(it.hasNext()){
				allComments.add(it.next());
			}
			return allComments;
		}
		return allComments;
	}
}
