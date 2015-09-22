package com.automic.objects;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.Template;
import com.uc4.api.UC4HostName;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.Job;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Jobs extends ObjectTemplate {

	public Jobs(Connection conn, boolean verbose) {
		super(conn, verbose);

	}

	private ObjectBroker getBrokerInstance() {
		return new ObjectBroker(this.connection, true);
	}

	public Job getJobFromObject(UC4Object object){return (Job) object;}
	// Modify a property of a Job object
	public void setJobPriority(UC4Object object, int priority)
			throws IOException {
		ObjectBroker broker = getBrokerInstance();
		Job job = (Job) object;
		job.attributes().setPriority(priority);
		broker.common.saveObject(job);
	}

	// Modify a property of a Job object
	public void setJobBasics(UC4Object object, UC4HostName Hostname,
			UC4ObjectName LoginName, UC4ObjectName QueueName)
			throws IOException {
		ObjectBroker broker = getBrokerInstance();
		Job job = (Job) object;
		job.attributes().setHost(Hostname);
		job.attributes().setLogin(LoginName);
		job.attributes().setQueue(QueueName);
		job.setProcess("echo something");

		broker.common.saveObject(job);
	}

	public ArrayList<UC4Object> getAllJobs() throws IOException {
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.JOBS);
	}

	public ArrayList<UC4Object> getAllJobsWithFilter(String filter)
			throws IOException {
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.JOBS,
				filter);
	}

	// Created for testing purposes only..
	public String createRandomJobInFolder(Template template, IFolder folder,
			String JobNamePrefix) throws IOException {
		ObjectBroker broker = getBrokerInstance();
		Random rand = new Random();
		String RandomObjectName = new BigInteger(25, rand).toString(32);
		String ObjectName = JobNamePrefix + RandomObjectName;
		broker.common.createObject(ObjectName, template, folder);
		return ObjectName;
	}

	// Created for testing purposes only..
	public String createRandomJobInFolder(Template template, String command,
			String host, String login, String queue, IFolder folder,
			String JobNamePrefix) throws IOException {
		ObjectBroker broker = getBrokerInstance();
		Random rand = new Random();
		String RandomObjectName = new BigInteger(25, rand).toString(32);
		String ObjectName = JobNamePrefix + RandomObjectName;
		broker.common.createObject(ObjectName, template, folder);
		Job job = (Job) broker.common.openObject(ObjectName, false);
		job.attributes().setHost(new UC4HostName(host));
		job.attributes().setQueue(new UC4ObjectName(queue));
		job.attributes().setLogin(new UC4ObjectName(login));
		job.setProcess(command);
		broker.common.saveAndCloseObject(job);
		return ObjectName;
	}

	// Created for testing purposes only..
	public String createRandomJobInRandomFolder(Template template)
			throws IOException {
		ObjectBroker broker = getBrokerInstance();
		ArrayList<IFolder> listOfFolders = broker.folders.getAllFolders(true);
		int numberOfFolders = listOfFolders.size();
		Random rand = new Random();
		int randomNum = rand.nextInt((numberOfFolders - 0));
		IFolder myFolder = listOfFolders.get(randomNum);
		return createRandomJobInFolder(template, myFolder, "TEST.JOBS.");
	}

	// Create an Object
	public void createJob(String JobName, Template template, IFolder folder)
			throws IOException {
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(JobName, template, folder);
	}
	
	public void createJob(String JobName, String TemplateName, IFolder folder)
			throws IOException {
		ObjectBroker broker = getBrokerInstance();
		Template template = com.automic.utils.Utils.convertStringToTemplate(TemplateName);
		if ( template == null){
			System.out.println(" -- Error! Template Name " + TemplateName +" Does Not Seem To Match Any Existing Template..");
		}else{
		broker.common.createObject(JobName, template, folder);
		}
	}

	// Save an Job
	public void saveJob(UC4Object Job) throws IOException {
		ObjectBroker broker = getBrokerInstance();
		broker.common.saveObject(Job);
	}

	// Delete a Job
	public void deleteJob(UC4ObjectName jobName) throws IOException {
		ObjectBroker broker = getBrokerInstance();
		broker.common.deleteObject(jobName.getName(), false);
	}
}
