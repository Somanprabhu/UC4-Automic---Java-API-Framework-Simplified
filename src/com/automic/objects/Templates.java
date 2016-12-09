package com.automic.objects;

import java.io.IOException;

import com.uc4.api.Template;
import com.uc4.api.objects.IFolder;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.TemplateList;

public class Templates  extends ObjectTemplate{

	public Templates(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public Template getTemplate(String TemplateName){
		//Get folder tree
		ObjectBroker broker = getBrokerInstance();
		IFolder RootFolder =null;
		try {
			RootFolder = broker.folders.getRootFolder();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		//Get client template
		TemplateList req = new TemplateList(RootFolder);
		try {
			sendGenericXMLRequestAndWait(req);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Template objTemplate = req.getTemplate(TemplateName);
		return objTemplate;
	}
	
	// JDE RA Objects
	public Template getTemplateJOB_JDE(){return getTemplate("JOBS.JDEDWARDSAGENT.JDEJOB");}
	public Template getTemplateJOB_JDEUBE(){return getTemplate("JOBS.JDEDWARDSAGENT.JDEUBEJOB");}
	public Template getTemplateCONN_JDE_JDBC(){return getTemplate("CONN.JDEDWARDSAGENT.JDBCCONNECTION");}
	public Template getTemplateCONN_JDE(){return getTemplate("CONN.JDEDWARDSAGENT.JDEDWARDSCONNECTION");}
	
	// FTP RA Objects
	public Template getTemplateCONN_FTP(){return getTemplate("CONN.FTPAGENT.FTPCONNECTION");}
	public Template getTemplateJOB_FTP(){return getTemplate("JOBS.FTPAGENT.FTPJOB");}
	
	// WS RA Objects for v3 agents
	public Template getTemplateCONN_REST_V3(){return getTemplate("CONN.WEBSERVICE.RESTCONNECTION");}
	public Template getTemplateCONN_SOAP_V3(){return getTemplate("CONN.WEBSERVICE.SOAPCONNECTION");}
	public Template getTemplateJOB_REST_V3(){return getTemplate("JOBS.WEBSERVICE.REST");}
	public Template getTemplateJOB_SOAP_V3(){return getTemplate("JOBS.WEBSERVICE.SOAP");}
	
	// WS RA Objects for v4 REST & SOAP Agents
	public Template getTemplateCONN_REST_V4(){return getTemplate("CONN.WEBSERVICEREST.RESTCONNECTION");}
	public Template getTemplateCONN_SOAP_V4(){return getTemplate("CONN.WEBSERVICESOAP.SOAPCONNECTION");}
	public Template getTemplateJOB_REST_V4(){return getTemplate("JOBS.WEBSERVICEREST.REST");}
	public Template getTemplateJOB_SOAP_V4(){return getTemplate("JOBS.WEBSERVICESOAP.SOAP");}
	
}
