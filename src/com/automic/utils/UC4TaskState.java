package com.automic.utils;

import com.uc4.api.objects.TaskState;

public class UC4TaskState{

	public UC4TaskState(){}
	
	public static TaskState getTaskStateFromValue(String s){
		if(s.equalsIgnoreCase("ANY_ABEND")){return TaskState.ANY_ABEND;}
		if(s.equalsIgnoreCase("ANY_ABEND_EXCEPT_FAULT")){return TaskState.ANY_ABEND_EXCEPT_FAULT;}
		if(s.equalsIgnoreCase("ANY_ACTIVE")){return TaskState.ANY_ACTIVE;}
		if(s.equalsIgnoreCase("ANY_BLOCKED")){return TaskState.ANY_BLOCKED;}
		if(s.equalsIgnoreCase("ANY_BLOCKED_OR_STOPPED")){return TaskState.ANY_BLOCKED_OR_STOPPED;}
		if(s.equalsIgnoreCase("ANY_EXCEPT_FAULT")){return TaskState.ANY_EXCEPT_FAULT;}
		if(s.equalsIgnoreCase("ANY_OK")){return TaskState.ANY_OK;}
		if(s.equalsIgnoreCase("ANY_OK_OR_UNBLOCKED")){return TaskState.ANY_OK_OR_UNBLOCKED;}
		if(s.equalsIgnoreCase("ANY_SKIPPED")){return TaskState.ANY_SKIPPED;}
		if(s.equalsIgnoreCase("ANY_STOPPED")){return TaskState.ANY_STOPPED;}
		if(s.equalsIgnoreCase("ANY_WAITING")){return TaskState.ANY_WAITING;}
		if(s.equalsIgnoreCase("ENDED_CANCEL")){return TaskState.ENDED_CANCEL;}
		if(s.equalsIgnoreCase("ENDED_EMPTY")){return TaskState.ENDED_EMPTY;}
		if(s.equalsIgnoreCase("ENDED_ESCALATED")){return TaskState.ENDED_ESCALATED;}
		if(s.equalsIgnoreCase("ENDED_INACTIVE")){return TaskState.ENDED_INACTIVE;}
		if(s.equalsIgnoreCase("ENDED_INACTIVE_MANUAL")){return TaskState.ENDED_INACTIVE_MANUAL;}
		if(s.equalsIgnoreCase("ENDED_NOT_OK")){return TaskState.ENDED_NOT_OK;}
		if(s.equalsIgnoreCase("ENDED_NOT_OK_SYNC")){return TaskState.ENDED_NOT_OK_SYNC;}
		if(s.equalsIgnoreCase("ENDED_OK")){return TaskState.ENDED_OK;}
		if(s.equalsIgnoreCase("ENDED_OK_OR_EMPTY")){return TaskState.ENDED_OK_OR_EMPTY;}
		if(s.equalsIgnoreCase("ENDED_OK_OR_INACTIVE")){return TaskState.ENDED_OK_OR_INACTIVE;}
		if(s.equalsIgnoreCase("ENDED_OK_OR_UNBLOCKED")){return TaskState.ENDED_OK_OR_UNBLOCKED;}
		if(s.equalsIgnoreCase("ENDED_SKIPPED")){return TaskState.ENDED_SKIPPED;}
		if(s.equalsIgnoreCase("ENDED_SKIPPED_CONDITIONS")){return TaskState.ENDED_SKIPPED_CONDITIONS;}
		if(s.equalsIgnoreCase("ENDED_SKIPPED_SYNC")){return TaskState.ENDED_SKIPPED_SYNC;}
		if(s.equalsIgnoreCase("ENDED_TIMEOUT")){return TaskState.ENDED_TIMEOUT;}
		if(s.equalsIgnoreCase("ENDED_TRUNCATE")){return TaskState.ENDED_TRUNCATE;}
		if(s.equalsIgnoreCase("ENDED_UNDEFINED")){return TaskState.ENDED_UNDEFINED;}
		if(s.equalsIgnoreCase("ENDED_VANISHED")){return TaskState.ENDED_VANISHED;}
		if(s.equalsIgnoreCase("FAULT_ALREADY_RUNNING")){return TaskState.FAULT_ALREADY_RUNNING;}
		if(s.equalsIgnoreCase("FAULT_NO_HOST")){return TaskState.FAULT_NO_HOST;}
		if(s.equalsIgnoreCase("FAULT_OTHER")){return TaskState.FAULT_OTHER;}
		if(s.equalsIgnoreCase("USER_100_200")){return TaskState.USER_100_200;}
		if(s.equalsIgnoreCase("USER_147")){return TaskState.USER_147;}
		if(s.equalsIgnoreCase("USER_201_299")){return TaskState.USER_201_299;}
		if(s.equalsIgnoreCase("USER_300")){return TaskState.USER_300;}
		if(s.equalsIgnoreCase("USER_510")){return TaskState.USER_510;}
		if(s.equalsIgnoreCase("WAITING_AGENT")){return TaskState.USER_690;}
		if(s.equalsIgnoreCase("")){return TaskState.WAITING_AGENT;}
		if(s.equalsIgnoreCase("WAITING_AGENT_OR_AGENTGROUP")){return TaskState.WAITING_AGENT_OR_AGENTGROUP;}
		if(s.equalsIgnoreCase("WAITING_AGENTGROUP")){return TaskState.WAITING_AGENTGROUP;}
		if(s.equalsIgnoreCase("WAITING_EXTERNAL")){return TaskState.WAITING_EXTERNAL;}
		if(s.equalsIgnoreCase("WAITING_GROUP")){return TaskState.WAITING_GROUP;}
		if(s.equalsIgnoreCase("WAITING_QUEUE")){return TaskState.WAITING_QUEUE;}
		if(s.equalsIgnoreCase("WAITING_SYNC")){return TaskState.WAITING_SYNC;}
		return null;
	}
}
