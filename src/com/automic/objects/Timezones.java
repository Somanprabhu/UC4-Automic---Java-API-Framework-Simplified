package com.automic.objects;

import java.io.IOException;

import com.uc4.api.Template;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.TimeZone;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Timezones extends ObjectTemplate {


	public Timezones(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public TimeZone getTimezoneFromObject(UC4Object object){return (TimeZone) object;}
	
	public void createStdTimezoneEvent(String TZName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(TZName, Template.TZ, FolderLocation);
	}
	public void createESTTimezone(String TZName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(TZName, Template.TZ_EST, FolderLocation);
	}
	public void createCSTTimezone(String TZName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(TZName, Template.TZ_CST, FolderLocation);
	}
	public void createGMTTimezone(String TZName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(TZName, Template.TZ_GMT, FolderLocation);
	}
	public void createPSTTimezone(String TZName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(TZName, Template.TZ_PST, FolderLocation);
	}
	public void createCETTimezone(String TZName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(TZName, Template.TZ_CET, FolderLocation);
	}
}
