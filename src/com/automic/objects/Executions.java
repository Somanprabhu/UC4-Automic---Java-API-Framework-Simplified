package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.automic.utils.ReportTypeEnum;
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
		public void cancelObject(int rundId) throws IOException {
			System.out.print("Cancel RundId("+ rundId +") ... ");
			CancelTask cancel = new CancelTask(rundId, false);
			connection.sendRequestAndWait(cancel);
			if (cancel.getMessageBox() != null) {
				System.out.println(cancel.getMessageBox().getText().toString().replace("\n", ""));
			}else{
				Say(" \t ++ Object with RUNID: "+rundId+" Successfully Cancelled.");
			}
		}
		
	//public void cancelObject(int ObjectRunId) throws IOException{getBrokerInstance().common.cancelObject(ObjectRunId);}
	
	
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public int executeObjectNow(String name) throws IOException {

		Say(" \t ++ Executing object Now: "+name);

		ExecuteObject execute = new ExecuteObject(new UC4ObjectName(name));
		connection.sendRequestAndWait(execute);

		if (execute.getMessageBox() != null || execute.getRunID() == 0) {
			if (execute.getMessageBox() != null) System.err.println(" -- "+execute.getMessageBox().getText().toString().replace("\n", ""));
			//System.out.println("-- Failed to execute object:"+name + ":" +execute.getMessageBox().getText());
		}else{	
			Say(" \t ++ Object: "+name+"++ Successfully executed with Run ID: "+execute.getRunID());
		}
		return execute.getRunID();
	}
	
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public int executeObjectOnce(String name, String timezone, DateTime startDate, DateTime logicalDate) throws IOException {

		Say(" \t ++ Executing object Once: "+name);

		ExecuteObject execute = new ExecuteObject(new UC4ObjectName(name));
		//DateTime startDate = DateTime.now().addDays(1);
		//DateTime logicalDate = DateTime.now().addDays(2);
		execute.executeOnce(startDate, logicalDate, new UC4TimezoneName(timezone), false, null);
		//execute.executeOnce(startDate, logicalDate, new UC4TimezoneName("TZ.ANG"), false, null);
		connection.sendRequestAndWait(execute);

		if (execute.getMessageBox() != null || execute.getRunID() == 0) {
			if (execute.getMessageBox() != null) System.err.println(" -- "+execute.getMessageBox().getText().toString().replace("\n", ""));
			//System.out.println("-- Failed to execute object:"+name + ":" +execute.getMessageBox().getText());
		}else{		
			Say(" \t ++ Object: "+name+" Successfully executed with Run ID: "+execute.getRunID());
		}
		return execute.getRunID();
	}
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public int executeObjectRecurring(String name, ExecuteRecurring recurringPattern) throws IOException {

		Say(" \t ++ Executing object Recurring: "+name);

		ExecuteObject execute = new ExecuteObject(new UC4ObjectName(name));
		//ExecuteRecurring rec = new ExecuteRecurring();
		//rec.setExecutionInterval(1);
		
		execute.executeRecurring(recurringPattern);
		
		connection.sendRequestAndWait(execute);

		if (execute.getMessageBox() != null || execute.getRunID() == 0) {
			if (execute.getMessageBox() != null) System.err.println(" -- "+execute.getMessageBox().getText().toString().replace("\n", ""));
			//System.out.println("-- Failed to execute object:"+name + ":" +execute.getMessageBox().getText());
		}else{		
			Say(" \t ++ Object: "+name+" Successfully executed with Run ID: "+execute.getRunID());
		}
		return execute.getRunID();
	}
	
	public int getLatestRunId(String ObjectName) throws IOException{
		UC4ObjectName objName = new UC4ObjectName(ObjectName);
		LatestReport req = new LatestReport(objName);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		return req.latestRunID();
	}
	
	public ReportTypeList getListOfReportTypesAvailableForRun(int RunID) throws IOException{
		ReportTypeList req = new ReportTypeList(RunID);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		return req;
	}
	
	public Report getReport(int RunID, String ReportType) throws IOException{
		Report req = new Report(RunID, ReportType);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		return req;
	}
	public Report getActivationReport(int RunID) throws IOException{
		Report req = new Report(RunID, ReportTypeEnum.ACTIVATION);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		return req;
	}
	public Report getRuntimeReport(int RunID) throws IOException{
		Report req = new Report(RunID, ReportTypeEnum.RUNTIME_REPORT);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		return req;
	}
	public Report getJobReportReport(int RunID) throws IOException{
		Report req = new Report(RunID, ReportTypeEnum.JOB_REPORT);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		return req;
	}
	public Report getPostProcessReportReport(int RunID) throws IOException{
		Report req = new Report(RunID, ReportTypeEnum.POST_REPORT);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		return req;
	}
	public void addCommentToRun(int RunID, String comment) throws IOException{
		AddComment req = new AddComment(RunID, comment);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
	}
	public ArrayList<Comment> getCommentsFromRun(int RunID) throws IOException{
		GetComments req = new GetComments(RunID);
		ArrayList<Comment> allComments = new ArrayList<Comment>();
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}
		Iterator <Comment> it = req.iterator();
		while(it.hasNext()){
			allComments.add(it.next());
		}
		return allComments;
	}
}
