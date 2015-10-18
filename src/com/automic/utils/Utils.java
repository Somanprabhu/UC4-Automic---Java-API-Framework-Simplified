package com.automic.utils;

import com.uc4.api.Template;

public class Utils {

	// List is incomplete.. add as necessary!!
	public static Template convertStringToTemplate(String s){
		
		  if(s.equals("CALE")){return Template.CALE;}
		  if(s.equals("CALL")){return Template.CALL;}
		  if(s.equals("CALL_STANDARD")){return Template.CALL_STANDARD;}
		  if(s.equals("CALL_MAIL")){return Template.CALL_MAIL;}
		  if(s.equals("CONN_SAP")){return Template.CONN_SAP;}
		  if(s.equals("CONN_SQL")){return Template.CONN_SQL;}
		  if(s.equals("CALL_ALARM")){return Template.CALL_ALARM;}
		  if(s.equals("CODE")){return Template.CODE;}
		  if(s.equals("CPIT")){return Template.CPIT;}
		  if(s.equals("DOCU")){return Template.DOCU;}
		  if(s.equals("EVNT_CONS")){return Template.EVNT_CONS;}
		  if(s.equals("EVNT_FILE")){return Template.EVNT_FILE;}
		  if(s.equals("EVNT_TIME")){return Template.EVNT_TIME;}
		  if(s.equals("EVNT_DB")){return Template.EVNT_DB;}
		  if(s.equals("FILTER_OUTPUT")){return Template.FILTER_OUTPUT;}
		  if(s.equals("FOLD")){return Template.FOLD;}
		  if(s.equals("HSTA")){return Template.HSTA;}
		  if(s.equals("JOBF")){return Template.JOBF;}
		  if(s.equals("JOBG")){return Template.JOBG;}
		  if(s.equals("JOBI")){return Template.JOBI;}
		  if(s.equals("JOBP")){return Template.JOBP;}
		  if(s.equals("JOBQ_PS_PROCESSREQUEST")){return Template.JOBQ_PS_PROCESSREQUEST;}
		  if(s.equals("JOBQ_R3_ALL_JOBS")){return Template.JOBQ_R3_ALL_JOBS;}
		  if(s.equals("JOBQ_R3_INTERCEPTED_JOBS")){return Template.JOBQ_R3_INTERCEPTED_JOBS;}
		  if(s.equals("JOBQ_R3_JAVA_JOBS")){return Template.JOBQ_R3_JAVA_JOBS;}
		  if(s.equals("JOBS_BS2000")){return Template.JOBS_BS2000;}
		  if(s.equals("JOBS_GCOS8")){return Template.JOBS_GCOS8;}
		  if(s.equals("JOBS_JMX")){return Template.JOBS_JMX;}
		  if(s.equals("JOBS_MPE")){return Template.JOBS_MPE;}
		  if(s.equals("JOBS_NSK")){return Template.JOBS_NSK;}
		  if(s.equals("JOBS_OA")){return Template.JOBS_OA;}
		  if(s.equals("JOBS_OS390")){return Template.JOBS_OS390;}
		  if(s.equals("JOBS_OS400")){return Template.JOBS_OS400;}
		  if(s.equals("JOBS_PS")){return Template.JOBS_PS;}
		  if(s.equals("JOBS_SAP_ABAP")){return Template.JOBS_SAP_ABAP;}
		  if(s.equals("JOBS_SAP_JAVA")){return Template.JOBS_SAP_JAVA;}
		  if(s.equals("JOBS_SAP_PI")){return Template.JOBS_SAP_PI;}
		  if(s.equals("JOBS_SIEBEL")){return Template.JOBS_SIEBEL;}
		  if(s.equals("JOBS_SQL")){return Template.JOBS_SQL;}
		  if(s.equals("JOBS_UNIX")){return Template.JOBS_UNIX;}
		  if(s.equals("JOBS_VMS")){return Template.JOBS_VMS;}
		  if(s.equals("JOBS_WIN")){return Template.JOBS_WIN;}
		  if(s.equals("JOBS_GENERIC")){return Template.JOBS_GENERIC;}
		  if(s.equals("JSCH")){return Template.JSCH;}
		  if(s.equals("LOGIN")){return Template.LOGIN;}
		  if(s.equals("SCRI")){return Template.SCRI;}
		  if(s.equals("STORE")){return Template.STORE;}
		  if(s.equals("SYNC")){return Template.SYNC;}
		  if(s.equals("TZ")){return Template.TZ;}
		  if(s.equals("TZ_CET")){return Template.TZ_CET;}
		  if(s.equals("TZ_CST")){return Template.TZ_CST;}
		  if(s.equals("TZ_EST")){return Template.TZ_EST;}
		  if(s.equals("TZ_GMT")){return Template.TZ_GMT;}
		  if(s.equals("TZ_PST")){return Template.TZ_PST;}
		  if(s.equals("TZ_SYD")){return Template.TZ_SYD;}
		  if(s.equals("USER")){return Template.USER;}
		  if(s.equals("USRG")){return Template.USRG;}
		  if(s.equals("VARA")){return Template.VARA;}
		  if(s.equals("HOSTG")){return Template.HOSTG;}
		  if(s.equals("XSL")){return Template.XSL;}
		  if(s.equals("QUEUE")){return Template.QUEUE;}
		  if(s.equals("CONN_DB")){return Template.CONN_DB;}
		  if(s.equals("PROMPT_SET")){return Template.PROMPT_SET;}
		  if(s.equals("VARA_FILELIST")){return Template.VARA_FILELIST;}
		  if(s.equals("VARA_MULTI")){return Template.VARA_MULTI;}
		  if(s.equals("VARA_SQL")){return Template.VARA_SQL;}
		  if(s.equals("VARA_SQLI")){return Template.VARA_SQLI;}
		  if(s.equals("VARA_SEC_SQL")){return Template.VARA_SEC_SQL;}
		  if(s.equals("VARA_SEC_SQLI")){return Template.VARA_SEC_SQLI;}
		  if(s.equals("VARA_BACKEND")){return Template.VARA_BACKEND;}
		  if(s.equals("VARA_EXEC")){return Template.VARA_EXEC;}
		  if(s.equals("JOBP_IF")){return Template.JOBP_IF;}
		  if(s.equals("JOBP_FOREACH")){return Template.JOBP_FOREACH;}
		  if(s.equals("DASH")){return Template.DASH;}
		  if(s.equals("LOCA")){return Template.LOCA;}
		  if(s.equals("VARA_XML")){return Template.VARA_XML;}
		  if(s.equals("PERIOD")){return Template.PERIOD;}
		
		return null;
	}
	
}
