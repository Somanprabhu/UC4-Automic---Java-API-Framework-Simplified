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
import com.uc4.communication.requests.ExecuteObject;
import com.uc4.communication.requests.GetComments;
import com.uc4.communication.requests.GetComments.Comment;
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
	
	public void executeObjectNow(String ObjectName) throws IOException{
		
		getBrokerInstance().common.executeObject(ObjectName);
		
	}
	public void executeObjectOnce(String ObjectName) throws IOException{
		
		ExecuteObject obj = new ExecuteObject(new UC4ObjectName(ObjectName));
		
		DateTime startDate = DateTime.now().addDays(1);
		DateTime logicalDate = DateTime.now().addDays(2);
		
		obj.executeOnce(startDate, logicalDate, new UC4TimezoneName("TZ.ANG"), false, null);

		this.connection.sendRequestAndWait(obj);
		int runID = obj.getRunID();
	}
	public void executeObjectRecurring(String ObjectName) throws IOException{
		
		ExecuteObject obj = new ExecuteObject(new UC4ObjectName(ObjectName));
		
		ExecuteRecurring rec = new ExecuteRecurring();
		rec.setExecutionInterval(1);
		obj.executeRecurring(rec);
		//getBrokerInstance().common.saveAndCloseObject(obj);
		this.connection.sendRequestAndWait(obj);
		int runID = obj.getRunID();
	}
	
	public void cancelObject(int ObjectRunId) throws IOException{getBrokerInstance().common.cancelObject(ObjectRunId);}
	
	public int getLatestRunId(String ObjectName) throws IOException{
		UC4ObjectName objName = new UC4ObjectName(ObjectName);
		LatestReport req = new LatestReport(objName);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
		return req.latestRunID();
	}
	
	public ReportTypeList getListOfReportTypesAvailableForRun(int RunID) throws IOException{
		ReportTypeList req = new ReportTypeList(RunID);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
		return req;
	}
	
	public Report getReport(int RunID, String ReportType) throws IOException{
		Report req = new Report(RunID, ReportType);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
		return req;
	}
	public Report getActivationReport(int RunID) throws IOException{
		Report req = new Report(RunID, ReportTypeEnum.ACTIVATION);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
		return req;
	}
	public Report getRuntimeReport(int RunID) throws IOException{
		Report req = new Report(RunID, ReportTypeEnum.RUNTIME_REPORT);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
		return req;
	}
	public Report getJobReportReport(int RunID) throws IOException{
		Report req = new Report(RunID, ReportTypeEnum.JOB_REPORT);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
		return req;
	}
	public Report getPostProcessReportReport(int RunID) throws IOException{
		Report req = new Report(RunID, ReportTypeEnum.POST_REPORT);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
		return req;
	}
	public void addCommentToRun(int RunID, String comment) throws IOException{
		AddComment req = new AddComment(RunID, comment);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
	}
	public ArrayList<Comment> getCommentsFromRun(int RunID) throws IOException{
		GetComments req = new GetComments(RunID);
		ArrayList<Comment> allComments = new ArrayList();
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
		Iterator <Comment> it = req.iterator();
		while(it.hasNext()){
			allComments.add(it.next());
		}
		return allComments;
	}
}
