package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.automic.utils.Utils;
import com.uc4.api.Task;
import com.uc4.api.TaskFilter;
import com.uc4.api.TaskPromptSetName;
import com.uc4.api.objects.OCVPanel.CITValue;
import com.uc4.api.objects.PromptElement;
import com.uc4.api.prompt.LabelElement;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.ActivityList;
import com.uc4.communication.requests.AddComment;
import com.uc4.communication.requests.AdoptTask;
import com.uc4.communication.requests.CancelTask;
import com.uc4.communication.requests.DeactivateTask;
import com.uc4.communication.requests.ModifyTaskState;
import com.uc4.communication.requests.QuitTask;
import com.uc4.communication.requests.RestartTask;
import com.uc4.communication.requests.SubmitPrompt;
import com.uc4.communication.requests.SuspendTask;
import com.uc4.communication.requests.TaskPromptSetContent;
import com.uc4.communication.requests.TaskPromptSetNames;
import com.uc4.communication.requests.XMLRequest;

public class ActivityWindow extends ObjectTemplate{

	public ActivityWindow(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	@SuppressWarnings("unused")
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	// return the content of the activity window
	public List<Task> getActivityWindowContent(TaskFilter taskFilter) throws IOException {		
		if(taskFilter !=null){
			ActivityList list = new ActivityList(taskFilter);
			connection.sendRequestAndWait(list);		
			List<Task> tasks = new ArrayList<Task>();
			for (Task t : list) {
				tasks.add(t);
				
			}
			return tasks;
		}else{
			return getActivityWindowContent();
		}

	}
	
	// return the content of the activity window
	public List<Task> getActivityWindowContent() throws IOException {		
		TaskFilter taskFilter = new TaskFilter();
		taskFilter.selectAllObjects();
		taskFilter.selectAllPlatforms();
		taskFilter.setTypeJSCH(true);
		taskFilter.setTypeAPI(true);
		return getActivityWindowContent(taskFilter);
	}
		
	public boolean deactivateTask(int RunID, boolean force) throws IOException {		
		DeactivateTask req = new DeactivateTask(RunID, force); // force
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Task "+ RunID +" Deactivated."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean adoptTask(int RunID) throws IOException {		
		AdoptTask req = new AdoptTask(RunID); // force
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Task "+ RunID +" Adopted."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public TaskPromptSetNames getTaskPromptsetNames(int RunID) throws IOException{
		//adoptTask(RunID);
		ArrayList<TaskPromptSetName> array = new ArrayList<TaskPromptSetName>();
		TaskPromptSetNames req = new TaskPromptSetNames(RunID);
		sendGenericXMLRequestAndWait(req);

		return req;
	}
	
	public ArrayList<TaskPromptSetName> getTaskPromptsetNamesAsArray(int RunID) throws IOException{
		//adoptTask(RunID);
		ArrayList<TaskPromptSetName> array = new ArrayList<TaskPromptSetName>();
		TaskPromptSetNames req = new TaskPromptSetNames(RunID);
		sendGenericXMLRequestAndWait(req);

		Iterator<TaskPromptSetName> it = req.iterator();
		while(it.hasNext()){
			array.add(it.next());
		}
		return array;
	}
	
	public void test(TaskPromptSetNames prptNames, int RunID) throws TimeoutException, IOException{
		// getting all prompts
		System.out.println("prpt names size:" + prptNames.size());
		Iterator<TaskPromptSetName> it0 = prptNames.iterator();
		while(it0.hasNext()){
			TaskPromptSetName tName = it0.next();
			TaskPromptSetContent req = new TaskPromptSetContent(tName, RunID);
			sendGenericXMLRequestAndWait(req);
			System.out.println("Type of prptset:" + req.getType());
			Iterator<PromptElement> it1 = req.iterator();
			
			while(it1.hasNext()){
				PromptElement elmt = it1.next();
				
				//DateElement, LabelElement, NumberElement, OnChangeResetElement, RadioGroupElement, TimeElement
				//LabelElement Lelmt = (LabelElement) elmt;
				System.out.println("Values: "+elmt.getMessageInsert()+" | "+elmt.getVariable()+" | "+elmt.getValue());
				elmt.setValue("GAGA");
			}
			SubmitPrompt sumbit = new SubmitPrompt(prptNames, req);
			sendGenericXMLRequestAndWait(sumbit);
			if(sumbit.getMessageBox() != null){
				System.out.println(sumbit.getMessageBox().getText());
			}else{
				System.out.println("Done?");
			}
		}	
		
	}
	
	public void showPromptSetContent(TaskPromptSetNames prptNames, int RunID) throws TimeoutException, IOException{
		// getting all prompts
		Iterator<TaskPromptSetName> it0 = prptNames.iterator();
		while(it0.hasNext()){
			TaskPromptSetName tName = it0.next();
			System.out.println("\t %% Promptset Found:" + tName);
			
			TaskPromptSetContent req = new TaskPromptSetContent(tName, RunID);
			sendGenericXMLRequestAndWait(req);
			Iterator<PromptElement> it1 = req.iterator();
			while(it1.hasNext()){
				PromptElement elmt = it1.next();
				System.out.println("\t %% [Variable Name | Variable Value]: "+" [ "+elmt.getVariable()+" | "+elmt.getValue()+" ]");
			}
		}
		
	}
	
	//"PromptName1['&VAR1#':'Value 1','&VAR2#':'dsfsdf']|PromptName2['&VAR1#':'Value 1','&VAR2#':'dsfsdf']"
	public void submitPromptSetContent(TaskPromptSetNames prptNames, HashMap<String, HashMap<String, String>> map, int RunID) throws TimeoutException, IOException{
		
		ArrayList<TaskPromptSetContent> AlLContents = new ArrayList<TaskPromptSetContent>();
		
		// FOR EACH PROMPT
		Iterator<TaskPromptSetName> it0 = prptNames.iterator();
		while(it0.hasNext()){
			TaskPromptSetName tName = it0.next();

			// getting all Vars for the given promptset
			HashMap<String, String> AllVarsFromInput = map.get(tName.getName().getName());
			// IF there is no entry for the prompt then skip.
			if(AllVarsFromInput != null){
				System.out.println("\t %% Promptset Found:" + tName);
				// Get the prompt's content
				TaskPromptSetContent req = new TaskPromptSetContent(tName, RunID);
				sendGenericXMLRequestAndWait(req);
				Iterator<PromptElement> it1 = req.iterator();
				// for each element of the individual prompt..
				while(it1.hasNext()){
					// Get the values to update and update them.
					PromptElement elmt = it1.next();
					String NewValue = AllVarsFromInput.get(elmt.getVariable());
					elmt.setValue(NewValue);
					System.out.println("\t %% Update of PromptSet entry "+tName+": [Variable Name | Variable Value]: "+" [ "+elmt.getVariable()+" | "+elmt.getValue()+" ]");
					//System.out.println("New Value Found!" + NewValue);	
				}	
				AlLContents.add(req);
			}
		}
		
		TaskPromptSetContent[] ContentstockArr = new TaskPromptSetContent[AlLContents.size()];
		ContentstockArr = AlLContents.toArray(ContentstockArr);
		if(ContentstockArr.length>0){
			SubmitPrompt sumbit = new SubmitPrompt(prptNames, ContentstockArr);
			sendGenericXMLRequestAndWait(sumbit);
			if(sumbit.getMessageBox() != null){
				System.out.println(sumbit.getMessageBox().getText());
			}else{
				System.out.println("\t %% Done!");
			}
		}
	}

	
	public boolean quitTask(int RunID) throws IOException {		
		QuitTask req = new QuitTask(RunID);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Task "+ RunID +" Quitted."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	
	public boolean restartTask(int RunID, boolean wait) throws IOException {		
		RestartTask req = new RestartTask(RunID); // force
		if(wait){
			sendGenericXMLRequestAndWait(req);
		}else{
			sendGenericXMLRequest(req);
		}
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Task "+ RunID +" Restarted."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean restartTask(int RunID) throws IOException {		
		RestartTask req = new RestartTask(RunID); // force
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Task "+ RunID +" Restarted."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean cancelTask(int RunID,boolean Recursive) throws IOException {		
		CancelTask req = new CancelTask(RunID, Recursive);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Task "+ RunID +" Cancelled."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	public boolean addComment(int RunID,String comment) throws IOException {		
		AddComment req = new AddComment(RunID, comment);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Task Comment Added on Runid: "+ RunID));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean stopTask(int RunID,boolean force) throws IOException {		
		SuspendTask req = new SuspendTask(RunID, force);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Task "+ RunID +" Stopped."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean modifyStatus(int RunID, int oldStatus, int newStatus) throws IOException {		
		ModifyTaskState req = new ModifyTaskState(RunID, oldStatus, newStatus); // force
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("Task "+ RunID +" Status Modified to: " + newStatus));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
		/* 
		Status	Description
		1300	Preparing
		1510	Transferred
		1520	Ready for transfer (before Connect)
		1521	Ready to be transferred
		1529	Ready for generation
		1530	Ready for start
		1531	To be called
		1540	Start initiated
		1541	Checking
		1542	Calling
		1543	Unknown
		1544	Inconsistent
		1545	Started
		1546	Connecting
		1550	Active
		1551	Transferring
		1552	Called
		1553	Accepted
		1554	Sampling files
		1556	Escalated
		1560	JobPlan blocked
		1561	Stopped - system-wide stop of automatic processing
		1562	HELD - manual stop was set
		1563	Stopped - automatic processing was stopped.
		1565	Finished sending task
		1569	Skipping Sync
		1570	Skipping
		1571	Canceling
		1572	Generating
		1573	Generated
		1574	Post processing
		1575	Ending
		1599	Alive user view
		1600	Internal checking
		1686	Waiting for start of host-group container.
		1687	Waiting for end of parallel host-group tasks
		1688	Waiting for the host of a host group
		1689	Waiting for resource (max. filetransfers exceeded
		1690	Waiting for external dependency
		1691	Waiting for remote system
		1692	Waiting for remote SYNC
		1693	Waiting for manual release
		1694	Waiting for resource (max. jobs exceeded)
		1695	Waiting for restart time
		1696	Waiting for host
		1697	Waiting for SYNC
		1698	Waiting for start time
		1700	Waiting for precondition
		1701	Sleeping
		1702	Not yet called
		1709	Waiting for end of parallel task(s)
		1710	Registered
		1800	ENDED_NOT_OK - aborted
		1801	ENDED_NOT_OK - aborted because of SYNC condition
		1802	ENDED_JP_ABEND - not executed due to abnormal JobPlan end
		1810	ENDED_VANISHED - disappeared
		1815	ENDED_LOST - ended undefined (host terminated prematurely)
		1820	FAULT_OTHER - Start impossible. Other error
		1821	FAULT_NO_HOST - Start impossible. Cannot reach host.
		1822	FAULT_ALREADY_RUNNING - task is already running
		1823	FAULT_POST_PROCESSING - error in post processing.
		1850	ENDED_CANCEL - manually canceled
		1851	ENDED_JP_CANCEL - JobPlan canceled manually
		1853	ENDED_QUEUE_CANCEL
		1854	ENDED_CONTAINER_CANCEL
		1856	ENDED_ESCALATED - aborted due to escalation
		1900	ENDED_OK - ended normally
		1902	ENDED_QUEUE_END
		1903	ENDED_CONTAINER_END
		1910	ENDED_EMPTY - task is empty (STOP NOMSG).
		1911	ENDED_TRUNCATE - Transfer incomplete due to line limit.
		1912	ENDED_EMPTY - nothing found.
		1919	ENDED_INACTIVE - inactive because logical date condition of external dependency.
		1920	ENDED_INACTIVE - inactive today because of Calendar.
		1921	ENDED_INACTIVE - task not active due to definition
		1922	ENDED_INACTIVE - Task was manually set inactive.
		1930	ENDED_SKIPPED - Skipped because of WHEN clause
		1931	ENDED_SKIPPED - Skipped because of SYNC condition.
		1940	ENDED_TIMEOUT - Not executed because of timeout (WHEN clause).
		1941	ENDED_TIMEOUT - Start time exceeded.
		1942	ENDED_TIMEOUT - ended untimely.
		1943	ENDING */
	}
	public List<Integer> getValidStatusCodes(){
		List<Integer> StatusCodes = new ArrayList<Integer>();
		
		StatusCodes.add(1300);//	Preparing
		StatusCodes.add(1510);//	Transferred
		StatusCodes.add(1520);//	Ready for transfer (before Connect)
		StatusCodes.add(1521);//	Ready to be transferred
		StatusCodes.add(1529);//	Ready for generation
		StatusCodes.add(1530);//	Ready for start
		StatusCodes.add(1531);//	To be called
		StatusCodes.add(1540);//	Start initiated
		StatusCodes.add(1541);//	Checking
		StatusCodes.add(1542);//	Calling
		StatusCodes.add(1543);//	Unknown
		StatusCodes.add(1544);//	Inconsistent
		StatusCodes.add(1545);//	Started
		StatusCodes.add(1546);//	Connecting
		StatusCodes.add(1550);//	Active
		StatusCodes.add(1551);//	Transferring
		StatusCodes.add(1552);//	Called
		StatusCodes.add(1553);//	Accepted
		StatusCodes.add(1554);//	Sampling files
		StatusCodes.add(1556);//	Escalated
		StatusCodes.add(1560);//	JobPlan blocked
		StatusCodes.add(1561);//	Stopped - system-wide stop of automatic processing
		StatusCodes.add(1562);//	HELD - manual stop was set
		StatusCodes.add(1563);//	Stopped - automatic processing was stopped.
		StatusCodes.add(1565);//	Finished sending task
		StatusCodes.add(1569);//	Skipping Sync
		StatusCodes.add(1570);//	Skipping
		StatusCodes.add(1571);//	Canceling
		StatusCodes.add(1572);//	Generating
		StatusCodes.add(1546);//	Generated
		StatusCodes.add(1574);//	Post processing
		StatusCodes.add(1575);//	Ending
		StatusCodes.add(1599);//	Alive user view
		StatusCodes.add(1600);//	Internal checking
		StatusCodes.add(1686);//	Waiting for start of host-group container.
		StatusCodes.add(1687);//	Waiting for end of parallel host-group tasks
		StatusCodes.add(1688);//	Waiting for the host of a host group
		StatusCodes.add(1689);//	Waiting for resource (max. filetransfers exceeded
		StatusCodes.add(1690);//	Waiting for external dependency
		StatusCodes.add(1691);//	Waiting for remote system
		StatusCodes.add(1692);//	Waiting for remote SYNC
		StatusCodes.add(1693);//	Waiting for manual release
		StatusCodes.add(1694);//	Waiting for resource (max. jobs exceeded)
		StatusCodes.add(1695);//	Waiting for restart time
		StatusCodes.add(1696);//	Waiting for host
		StatusCodes.add(1697);//	Waiting for SYNC
		StatusCodes.add(1698);//	Waiting for start time
		StatusCodes.add(1700);//	Waiting for precondition
		StatusCodes.add(1701);//	Sleeping
		StatusCodes.add(1702);//	Not yet called
		StatusCodes.add(1709);//	Waiting for end of parallel task(s)
		StatusCodes.add(1710);//	FAULT_ALREADY_RUNNING - task is already running
		StatusCodes.add(1800);//	ENDED_NOT_OK - aborted
		StatusCodes.add(1801);//	ENDED_NOT_OK - aborted because of SYNC condition
		StatusCodes.add(1802);//	ENDED_JP_ABEND - not executed due to abnormal JobPlan end
		StatusCodes.add(1810);//	ENDED_VANISHED - disappeared
		StatusCodes.add(1815);//	ENDED_LOST - ended undefined (host terminated prematurely)
		StatusCodes.add(1820);//	FAULT_OTHER - Start impossible. Other error
		StatusCodes.add(1821);//	FAULT_NO_HOST - Start impossible. Cannot reach host.
		StatusCodes.add(1822);//	Sleeping
		StatusCodes.add(1823);//	FAULT_POST_PROCESSING - error in post processing.
		StatusCodes.add(1850);//	ENDED_CANCEL - manually canceled
		StatusCodes.add(1851);//	ENDED_JP_CANCEL - JobPlan canceled manually
		StatusCodes.add(1853);//	ENDED_QUEUE_CANCEL
		StatusCodes.add(1854);//	ENDED_CONTAINER_CANCEL
		StatusCodes.add(1856);//	ENDED_ESCALATED - aborted due to escalation
		StatusCodes.add(1900);//	ENDED_OK - ended normally
		StatusCodes.add(1902);//	ENDED_QUEUE_END
		StatusCodes.add(1903);//	ENDED_CONTAINER_END
		StatusCodes.add(1910);//	ENDED_EMPTY - task is empty (STOP NOMSG).
		StatusCodes.add(1911);//	ENDED_TRUNCATE - Transfer incomplete due to line limit.
		StatusCodes.add(1912);//	ENDED_EMPTY - nothing found.
		StatusCodes.add(1919);//	ENDED_INACTIVE - inactive because logical date condition of external dependency.
		StatusCodes.add(1920);//	ENDED_INACTIVE - inactive today because of Calendar.
		StatusCodes.add(1921);//	ENDED_INACTIVE - task not active due to definition	
		StatusCodes.add(1922);//	ENDED_INACTIVE - Task was manually set inactive.
		StatusCodes.add(1930);//	ENDED_SKIPPED - Skipped because of WHEN clause
		StatusCodes.add(1931);//	ENDED_SKIPPED - Skipped because of SYNC condition.
		StatusCodes.add(1940);//	ENDED_TIMEOUT - Not executed because of timeout (WHEN clause).
		StatusCodes.add(1941);//	ENDED_TIMEOUT - Start time exceeded.
		StatusCodes.add(1942);//	ENDED_TIMEOUT - ended untimely.
		StatusCodes.add(1943);//	ENDING 
		
		return StatusCodes;
	}
}
