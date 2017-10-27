package com.automic.objects;

import com.uc4.communication.Connection;

public class ObjectBroker {

	private Connection connection;
	
	public Folders folders;
	public Jobs jobs;
	public Common common;
	public AutomationEngine automationEngine;
	public ActivityWindow activityWindow;
	public Jobplans jobPlans;
	public Calendars calendars;
	public Hostgroups hostgroups;
	public Logins logins;
	public Promptsets promptsets;
	public Queues queues;
	public Scripts scripts;
	public Usergroups usergroups;
	public Variables variables;
	public Clients clients;
	public FileTransfers fileTransfers;
	public Agents agents;
	public Links links;
	public Executions executions;
	public Storages storages;
	public Users users;
	public TransportCases transports;
	public Schedules schedules;
	public Notifications notifications;
	public Events events;
	public Connections connections;
	public Timezones timezones;
	public Syncs syncs;
	public Documentations documentations;
	public OutputFilters outputFilters;
	public Jobgroups jobgroups;
	public Includes includes;
	public Dashboards dashboards;
	public SAPQueueManager sapqueuemanager;
	public Statistics statistics;
	public Searches searches;
	public Versions versions;
	public Imports imports;
	public Exports exports;
	public ServiceLevelObjectives servicelevelobjectives;
	public AgentAssignments agentassignments;
	public Templates templates;
	public Rules rules;
	
	public ObjectBroker(Connection conn, boolean verbose){
		this.connection = conn;
		
		folders = new Folders(this.connection,verbose);
		jobs = new Jobs(this.connection,verbose);
		common = new Common(this.connection,verbose);
		automationEngine = new AutomationEngine(this.connection,verbose);
		activityWindow = new ActivityWindow(this.connection,verbose);
		jobPlans = new Jobplans(this.connection,verbose);
		calendars = new Calendars(this.connection,verbose);
		hostgroups = new Hostgroups(this.connection,verbose);
		logins = new Logins(this.connection,verbose);
		promptsets = new Promptsets(this.connection,verbose);
		queues = new Queues(this.connection,verbose);
		scripts = new Scripts(this.connection,verbose);
		usergroups = new Usergroups(this.connection,verbose);
		variables = new Variables(this.connection,verbose);
		clients = new Clients(this.connection,verbose);
		fileTransfers = new FileTransfers(this.connection,verbose);
		agents = new Agents(this.connection,verbose);
		links = new Links(this.connection,verbose);
		executions = new Executions(this.connection,verbose);
		storages = new Storages(this.connection,verbose);
		users = new Users(this.connection,verbose);
		transports = new TransportCases(this.connection,verbose);
		schedules = new Schedules(this.connection,verbose);
		notifications = new Notifications(this.connection,verbose);
		events = new Events(this.connection,verbose);
		connections = new Connections(this.connection,verbose);
		timezones = new Timezones(this.connection,verbose);
		syncs = new Syncs(this.connection,verbose);
		documentations = new Documentations(this.connection,verbose);
		outputFilters = new OutputFilters(this.connection,verbose);
		jobgroups = new Jobgroups(this.connection,verbose);
		includes = new Includes(this.connection,verbose);
		dashboards = new Dashboards(this.connection,verbose);
		sapqueuemanager = new SAPQueueManager(this.connection,verbose);
		statistics = new Statistics(this.connection,verbose);
		searches = new Searches(this.connection,verbose);
		versions = new Versions(this.connection,verbose);
		imports = new Imports(this.connection,verbose);
		exports = new Exports(this.connection,verbose);
		servicelevelobjectives = new ServiceLevelObjectives(this.connection, verbose);
		agentassignments = new AgentAssignments(this.connection,verbose);
		rules = new Rules(this.connection,verbose);
		templates = new Templates(this.connection,verbose);
	}
	
	public void setConnection(Connection conn){
		this.connection = conn;
	}
}
