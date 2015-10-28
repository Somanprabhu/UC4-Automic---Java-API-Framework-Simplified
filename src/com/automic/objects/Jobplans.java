package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.Template;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.Job;
import com.uc4.api.objects.JobPlan;
import com.uc4.api.objects.JobPlanTask;
import com.uc4.api.objects.TaskState;
import com.uc4.api.objects.UC4Object;
import com.uc4.api.objects.WorkflowIF;
import com.uc4.api.objects.WorkflowLoop;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.AddJobPlanTask;

public class Jobplans extends ObjectTemplate{

private ObjectBroker broker;
	
	public Jobplans(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public JobPlan getJobPlanFromObject(UC4Object object){return (JobPlan) object;}
	public void setPriority(UC4Object object, int priority) throws IOException{
		JobPlan jobp = (JobPlan) object;
		jobp.attributes().setPriority(priority);
		broker.common.saveObject(jobp);
	}
	
	public ArrayList<UC4Object> getAllJobPlans() throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.JOBP);
	}
	
	public ArrayList<UC4Object> getAllJobPlansWithFilter(String filter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.JOBP,filter);
	}
	
	public JobPlanTask getTaskFromName(String name) throws IOException {
		//System.out.print("Add JobPlan task "+name+" ... "); 
		AddJobPlanTask add = new AddJobPlanTask(new UC4ObjectName(name));
		connection.sendRequestAndWait(add);
		if (add.getMessageBox() != null) {
			System.out.println(" -- "+add.getMessageBox().getText().toString().replace("\n", ""));
		}
		return add.getJobPlanTask();
	}
	
	public JobPlanTask getTaskFromNameAndJobPlan(JobPlan jobPlan, String TaskName) throws IOException{
		Iterator<JobPlanTask> it = jobPlan.taskIterator();
		while(it.hasNext()){
			JobPlanTask jpt = it.next();
			if(jpt.getTaskName().equals(TaskName)){
				return jpt;
			}
		}
		return null;
	}
	
	public ArrayList<Job> getAllJobsFromJobPlan(JobPlan jobPlan) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		ArrayList<Job> allJobs = new ArrayList<Job>();
		Iterator<JobPlanTask> it = jobPlan.taskIterator();
		while(it.hasNext()){
			JobPlanTask jpt = it.next();
			Job job = (Job) broker.common.openObject(jpt.getTaskName(),false);
			allJobs.add(job);
		}
		return allJobs;
	}
	
	public void createEmptyWorkPlan(String WorkplanName, IFolder folder) throws IOException{
		broker.common.createObject(WorkplanName, Template.JOBP, folder);
	}
	public void createEmptyIFWorkPlan(String WorkplanName, IFolder folder) throws IOException{
		broker.common.createObject(WorkplanName, Template.JOBP_IF, folder);
	}
	public void createEmptyFOREACHWorkPlan(String WorkplanName, IFolder folder) throws IOException{
		broker.common.createObject(WorkplanName, Template.JOBP_FOREACH, folder);
	}
	public WorkflowIF getIFWorkflowFromObject(UC4Object object){return (WorkflowIF) object;}
	public WorkflowLoop getFOREACHWorkflowFromObject(UC4Object object){return (WorkflowLoop) object;}
	public JobPlan getWorkflowFromObject(UC4Object object){return (JobPlan) object;}
	
	// The following method is provided as an example
	public void createSampleWorkPlan(IFolder folder) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		
		// Creating a few jobs
		broker.jobs.createJob("TEST.SAMPLE.JOB1", Template.JOBS_WIN, folder);
		broker.jobs.createJob("TEST.SAMPLE.JOB2", Template.JOBS_WIN, folder);
		broker.jobs.createJob("TEST.SAMPLE.JOB3", Template.JOBS_WIN, folder);
		broker.jobs.createJob("TEST.SAMPLE.JOB4", Template.JOBS_WIN, folder);
		broker.jobs.createJob("TEST.SAMPLE.JOB5", Template.JOBS_WIN, folder);
		broker.jobs.createJob("TEST.SAMPLE.JOB6", Template.JOBS_WIN, folder);
		
		// Creating a jobPlan
		String JobPlanName = "TEST.SAMPLE.JOBPLAN.1";
		broker.common.createObject("TEST.SAMPLE.JOBPLAN.1", Template.JOBP, folder);
		UC4Object obj = broker.common.openObject(JobPlanName, false);
		JobPlan jobPlan = (JobPlan) obj;
		
		// Declaring all created jobs as JobPlanTasks (including START and END points)
		JobPlanTask taskStart = jobPlan.getStartTask();
		JobPlanTask taskEnd = jobPlan.getEndTask();
		JobPlanTask task1 = getTaskFromName("TEST.SAMPLE.JOB1");
		JobPlanTask task2 = getTaskFromName("TEST.SAMPLE.JOB2");
		JobPlanTask task3 = getTaskFromName("TEST.SAMPLE.JOB3");
		JobPlanTask task4 = getTaskFromName("TEST.SAMPLE.JOB4");
		JobPlanTask task5 = getTaskFromName("TEST.SAMPLE.JOB5");
		JobPlanTask task6 = getTaskFromName("TEST.SAMPLE.JOB6");
		
		// Adding all JobPlanTasks to the actual Job Plan
		jobPlan.addTask(task1);
		jobPlan.addTask(task2);
		jobPlan.addTask(task3);
		jobPlan.addTask(task4);
		jobPlan.addTask(task5);
		jobPlan.addTask(task6);
		
		// Adding the dependencies between jobs, END and START points
		taskEnd.dependencies().addDependency(task6, TaskState.ANY_ABEND);
		task1.dependencies().addDependency(taskStart, TaskState.ANY_OK);
		task6.dependencies().addDependency(task5, TaskState.ENDED_ESCALATED);
		task5.dependencies().addDependency(task4, TaskState.ANY_ABEND);
		task4.dependencies().addDependency(task3, TaskState.ANY_OK);
		task3.dependencies().addDependency(task2, TaskState.ANY_OK);
		task2.dependencies().addDependency(task1, TaskState.ANY_OK);
		
		// Formatting the layout of the Job Plan (you may use the jobPlan.format() method instead)
		jobPlan.getStartTask().setX(1);
		jobPlan.getStartTask().setY(1);
		task1.setX(2);task1.setY((1));
		task2.setX(3);task2.setY((1));
		task3.setX(4);task3.setY((1));
		task4.setX(5);task4.setY((1));
		task5.setX(6);task5.setY((1));
		task6.setX(7);task6.setY((1));
		jobPlan.getEndTask().setX(8);
		jobPlan.getEndTask().setY(1);
		
		// Saving the Job Plan
		broker.common.saveObject(jobPlan);
		broker.common.closeObject(jobPlan);
	}
	private void setXY(JobPlanTask task, int X, int Y){
		task.setX(X);
		task.setY(Y);
	}
	
	// Added the following method to quickly create a large workplan.
	public void createLargeWorkPlan(String WorkPlanName, IFolder folder, Template template, String JobNamePrefix,int NumberOfJobs, int NumberOfJobsPerLine) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		
		// Creating a few jobs
		ArrayList<String> JobNames = new ArrayList<String>();
		for(int i=1;i<=NumberOfJobs;i++){
		String name = broker.jobs.createRandomJobInFolder(template,"echo something","WIN-AGENT2","ADMIN","CLIENT_QUEUE", folder,JobNamePrefix);
		JobNames.add(name);
		}
		
		// Creating a jobPlan
		String JobPlanName = WorkPlanName;
		broker.common.createObject(WorkPlanName, Template.JOBP, folder);
		UC4Object obj = broker.common.openObject(JobPlanName, false);
		JobPlan jobPlan = (JobPlan) obj;
		
		// Declaring all created jobs as JobPlanTasks (including START and END points)
		JobPlanTask taskStart = jobPlan.getStartTask();
		JobPlanTask taskEnd = jobPlan.getEndTask();
		ArrayList<JobPlanTask> JobPlans = new ArrayList<JobPlanTask>();
		for(int i=0;i<JobNames.size();i++){
			JobPlanTask task = getTaskFromName(JobNames.get(i));
			// Adding all JobPlanTasks to the actual Job Plan
			jobPlan.addTask(task);
			JobPlans.add(task);
			}
		
		// Handling positioning, Adding the dependencies between jobs, END and START points

		int X = 2; int Y = 1;
		
		for(int i=0;i<JobPlans.size();i++){
			setXY(JobPlans.get(i),X,Y);
			if(X==2){JobPlans.get(i).dependencies().addDependency(taskStart, TaskState.ANY_OK);}
			if((X-1)==NumberOfJobsPerLine){
				taskEnd.dependencies().addDependency(JobPlans.get(i), TaskState.ANY_OK);
				X=2;
				Y=Y+1;
			}else{
				X=X+1;
				//Y=Y;
			}
		}
		
		// Formatting the layout of the Job Plan (you may use the jobPlan.format() method instead)
		int posStartAndEnd = ((JobPlans.size() / NumberOfJobsPerLine) / 2)+1;
		
		setXY(jobPlan.getStartTask(),1,posStartAndEnd);
		setXY(jobPlan.getEndTask(),NumberOfJobsPerLine+2,posStartAndEnd);
		
		// Saving the Job Plan
		broker.common.saveObject(jobPlan);
		broker.common.closeObject(jobPlan);
	}
	
	public void createSimpleJobPlan(String name, String Job1Name, String Job2name) throws IOException {
		ObjectBroker broker = getBrokerInstance();
		JobPlan jobPlan = (JobPlan) broker.common.openObject(name, false);
		
		JobPlanTask job1 = getTaskFromName(Job1Name);
		JobPlanTask job2 = getTaskFromName(Job2name);

		System.out.print("Add tasks to JobPlan ... ");
		jobPlan.addTask(job1);
		jobPlan.addTask(job2);
		
		System.out.print("Set task dependencies ... ");
		job2.dependencies().addDependency(job1, TaskState.ANY_OK);
		
		System.out.print("Connect open ends with <START> and <END> ... ");
		jobPlan.closeJobPlanTasks(TaskState.ANY_OK);
		
		System.out.print("Calculate JobPlan layout ... ");
		jobPlan.format();
		
		System.out.print("Verifying calculated lnr and positions ... ");
		
		broker.common.saveObject(jobPlan);		
		broker.common.closeObject(jobPlan);
	}	
	
}
