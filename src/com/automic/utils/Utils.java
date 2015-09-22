package com.automic.utils;

import com.uc4.api.Template;

public class Utils {

	// List is incomplete.. add as necessary!!
	public static Template convertStringToTemplate(String s){
		if(s.equals("JOBS.WIN")){return Template.JOBS_WIN;}
		if(s.equals("JOBS.UNIX")){return Template.JOBS_UNIX;}
		if(s.equals("JOBS.SQL")){return Template.JOBS_SQL;}
		if(s.equals("JOBS.VMS")){return Template.JOBS_VMS;}
		if(s.equals("JOBS.BS2000")){return Template.JOBS_BS2000;}
		if(s.equals("JOBS.GCO8")){return Template.JOBS_GCOS8;}
		if(s.equals("JOBS.JMX")){return Template.JOBS_JMX;}
		if(s.equals("JOBS.MPE")){return Template.JOBS_MPE;}
		if(s.equals("JOBS.NSK")){return Template.JOBS_NSK;}
		if(s.equals("JOBS.OS400")){return Template.JOBS_OS400;}
		if(s.equals("JOBS.OS390")){return Template.JOBS_OS390;}
		if(s.equals("JOBS.PS")){return Template.JOBS_PS;}
		if(s.equals("JOBS.SIEBEL")){return Template.JOBS_SIEBEL;}
		if(s.equals("JOBS.GENERIC")){return Template.JOBS_GENERIC;}
		if(s.equals("JOBS.OA")){return Template.JOBS_OA;}
		if(s.equals("JOBS.SAP.ABAP")){return Template.JOBS_SAP_ABAP;}
		if(s.equals("JOBS.SAP.JAVA")){return Template.JOBS_SAP_JAVA;}
		if(s.equals("JOBS.SAP.BI")){return Template.JOBS_SAP_PI;}
		
		if(s.equals("FOLD")){return Template.FOLD;}
		if(s.equals("USER")){return Template.USER;}

		if(s.equals("CALE")){return Template.CALE;}
		if(s.equals("CALL")){return Template.CALL;}
		if(s.equals("CALL.MAIL")){return Template.CALL_MAIL;}
		if(s.equals("CALL.STANDARD")){return Template.CALL_STANDARD;}
		if(s.equals("CONN.DB")){return Template.CONN_DB;}
		if(s.equals("CONN.SAP")){return Template.CONN_SAP;}
		if(s.equals("CONN.SQL")){return Template.CONN_SQL;}
		if(s.equals("DOCU")){return Template.DOCU;}
		if(s.equals("EVNT.CONS")){return Template.EVNT_CONS;}
		if(s.equals("EVNT.DB")){return Template.EVNT_DB;}
		if(s.equals("EVNT.FILE")){return Template.EVNT_FILE;}
		if(s.equals("EVNT.TIME")){return Template.EVNT_TIME;}
		
		if(s.equals("FILTER.OUT")){return Template.FILTER_OUTPUT;}
		if(s.equals("HOSTG")){return Template.HOSTG;}
		if(s.equals("JOBF")){return Template.JOBF;}
		if(s.equals("JOBG")){return Template.JOBG;}
		if(s.equals("JOBI")){return Template.JOBI;}
		if(s.equals("JOBP")){return Template.JOBP;}
		if(s.equals("JOBP.FOREACH")){return Template.JOBP_FOREACH;}
		if(s.equals("JOBP.IF")){return Template.JOBP_IF;}
		if(s.equals("JSCH")){return Template.JSCH;}
		if(s.equals("HOSTG")){return Template.HOSTG;}
		if(s.equals("HSTA")){return Template.HSTA;}
		if(s.equals("LOGIN")){return Template.LOGIN;}
		if(s.equals("PRMPT")){return Template.PROMPT_SET;}
		if(s.equals("QUEUE")){return Template.QUEUE;}
		if(s.equals("SCRI")){return Template.SCRI;}
		if(s.equals("STOR")){return Template.STORE;}
		if(s.equals("SYNC")){return Template.SYNC;}
		if(s.equals("TZ")){return Template.TZ;}
		if(s.equals("USER")){return Template.USER;}
		if(s.equals("USRG")){return Template.USRG;}
		if(s.equals("VARA.")){return Template.VARA;}
		
		if(s.equals("VARA.BACKEND")){return Template.VARA_BACKEND;}
		if(s.equals("VARA.EXEC")){return Template.VARA_EXEC;}
		if(s.equals("VARA.FILELIST")){return Template.VARA_FILELIST;}
		if(s.equals("VARA.MULTI")){return Template.VARA_MULTI;}
		if(s.equals("VARA.SEC.SQL")){return Template.VARA_SEC_SQL;}
		if(s.equals("VARA.SEC.SQLI")){return Template.VARA_SEC_SQLI;}
		if(s.equals("VARA.SQL")){return Template.VARA_SQL;}
		if(s.equals("VARA.SQLI")){return Template.VARA_SQLI;}
		if(s.equals("VARA.XML")){return Template.VARA_XML;}
		
		
		return null;
	}
	
}
