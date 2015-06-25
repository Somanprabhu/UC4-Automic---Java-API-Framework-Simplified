package com.automic.objects;

import java.io.IOException;
import com.uc4.api.Template;
import com.uc4.api.objects.ConsoleEvent;
import com.uc4.api.objects.DatabaseEvent;
import com.uc4.api.objects.FileEvent;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.TimeEvent;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Events extends ObjectTemplate{

	public Events(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	// should contain stuff for all event types: ConsoleEvent, DatabaseEvent, FileEvent and TimeEvent
	public TimeEvent getTimeEventFromObject(UC4Object object){return (TimeEvent) object;}
	public DatabaseEvent getDBEventFromObject(UC4Object object){return (DatabaseEvent) object;}
	public ConsoleEvent getConsoleEventFromObject(UC4Object object){return (ConsoleEvent) object;}
	public FileEvent getFileEventFromObject(UC4Object object){return (FileEvent) object;}
	
	public void createTimeEvent(String EventName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(EventName, Template.EVNT_TIME, FolderLocation);
	}
	public void createConsoleEvent(String EventName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(EventName, Template.EVNT_CONS, FolderLocation);
	}
	public void createDBEvent(String EventName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(EventName, Template.EVNT_DB, FolderLocation);
	}
	public void createFileEvent(String EventName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(EventName, Template.EVNT_FILE, FolderLocation);
	}
}
