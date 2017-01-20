package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
		public int executeObjectNow(String name,boolean Wait) throws IOException {

			ExecuteObject req = new ExecuteObject(new UC4ObjectName(name));
			
			if(Wait){
				sendGenericXMLRequestAndWait(req);
				
				if (req.getMessageBox() == null) {
					Say(Utils.getSuccessString("Object: "+name+"++ Successfully executed with Run ID: "+req.getRunID()));
					return req.getRunID();
				}
				return req.getRunID();
				// RunID is 0 if failed.
			}else{
				sendGenericXMLRequest(req);
				return -1;
			}
			

		}
		
		//public void 
		
		// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
		public int executeObjectNow(String name,int TimeOut) throws IOException {

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
	public int executeObjectNow(String name) throws IOException {

		ExecuteObject req = new ExecuteObject(new UC4ObjectName(name));
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Object: "+name+" Successfully executed with Run ID: "+req.getRunID()));
			return req.getRunID();
		}
		return req.getRunID();
		// RunID is 0 if failed.
	}
	
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public void executeObjectOnceNoWait(String name, String timezone, DateTime startDate, DateTime logicalDate) throws IOException {

		ExecuteObject req = new ExecuteObject(new UC4ObjectName(name));
		req.executeOnce(startDate, logicalDate, new UC4TimezoneName(timezone), false, null);
		sendGenericXMLRequest(req);
	}
	
	class RunNow implements Callable<String>{
		public String ObjectName;
		public RunNow(String ObjName){
			this.ObjectName = ObjName;
		}
	    @Override
	    public String call() throws Exception {
	       // Thread.sleep(4000); // Just to demo a long running task of 4 seconds.
	    	int RunID = executeObjectNow(this.ObjectName);
	    	//System.out.println("DEBUG: Runid: " + RunID);
	        return Integer.toString(RunID);
	    }
	}
	
	class RunLater implements Callable<String>{
		public String ObjectName;
		public String TimeZone;
		public DateTime StartDate;
		public DateTime LogicalDate;
		
		public RunLater(String ObjName, String timezone, DateTime startDate, DateTime logicalDate){
			this.ObjectName = ObjName;
			this.TimeZone = timezone;
			this.StartDate = startDate;
			this.LogicalDate = logicalDate;
		}
	    @Override
	    public String call() throws Exception {
	       // Thread.sleep(4000); // Just to demo a long running task of 4 seconds.
	    	int RunID = executeObjectOnce(this.ObjectName,true,this.TimeZone,this.StartDate,this.LogicalDate);
	        return Integer.toString(RunID);
	    }
	}
	
	public int executeNowWithTimeout(String ObjectName, int Timeout) throws InterruptedException, ExecutionException {
	        ExecutorService executor = Executors.newSingleThreadExecutor();
	        Future<String> future = executor.submit(new RunNow(ObjectName));
	        int RUNID=-99;
	        try {
	           // System.out.println("Started..");
	        	String RunID = future.get(Timeout, TimeUnit.SECONDS);
	        	//System.out.println("DEBUG IT IS: "+ RunID);
	            RUNID = Integer.parseInt(RunID);
	           // executor.shutdownNow();
	           // return RUNID;
	        } catch (TimeoutException e) {
	            future.cancel(true);
	            //System.out.println(" -- Error. TimeOut Occured.");
	            RUNID = -10;
	            
	        }

	        executor.shutdownNow();
	        return RUNID;
	}
	
	public int executeLaterWithTimeout(String ObjectName, String timezone, DateTime startDate, DateTime logicalDate, int Timeout) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new RunLater(ObjectName,timezone,startDate,logicalDate));
        int RUNID=-99;
        try {
           // System.out.println("Started..");
        	String RunID = future.get(Timeout, TimeUnit.SECONDS);
            RUNID = Integer.parseInt(RunID);
            return RUNID;
        } catch (TimeoutException e) {
            future.cancel(true); 
            //System.out.println(" -- Error. TimeOut Occured.");
            RUNID = -10;
        }

        executor.shutdownNow();
        return RUNID;
}
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	public int executeObjectOnce(String name, boolean Wait, String timezone, DateTime startDate, DateTime logicalDate) throws IOException {

		ExecuteObject req = new ExecuteObject(new UC4ObjectName(name));
		req.executeOnce(startDate, logicalDate, new UC4TimezoneName(timezone), false, null);
		if(Wait){
			sendGenericXMLRequestAndWait(req);
			
			if (req.getMessageBox() == null) {
				Say(Utils.getSuccessString("Object: "+name+" Successfully executed with Run ID: "+req.getRunID()));
				return req.getRunID();
			}
			return req.getRunID();
		}else{
			sendGenericXMLRequest(req);
			return -1;
		}

	}
	
	// Execute an Automic Object (any object of the executable kind, JOBS, JOBP, EVENT, etc.)
	/**
	 * 
	 * @param name
	 * @param timezone
	 * @param startDate
	 * @param logicalDate
	 * @return
	 * @throws IOException
	 * @deprecated
	 */
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
