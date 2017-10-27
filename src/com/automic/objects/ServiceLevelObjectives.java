package com.automic.objects;

import java.io.IOException;

import javax.smartcardio.CardTerminals.State;

import com.automic.utils.UC4TaskState;
import com.automic.utils.Utils;
import com.uc4.api.InvalidUC4NameException;
import com.uc4.api.ServiceLevelObjectiveListItem;
import com.uc4.api.Time;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.UC4TimezoneName;
import com.uc4.api.objects.ServiceLevelObjective;
import com.uc4.api.objects.SloSelection.Beneficiary;
import com.uc4.api.objects.TaskState;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.ServiceLevelObjectiveList;

public class ServiceLevelObjectives extends ObjectTemplate{
	public ServiceLevelObjectives(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	@SuppressWarnings("unused")
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public ServiceLevelObjectiveList getSLOs() throws IOException{
		ServiceLevelObjectiveList req = new ServiceLevelObjectiveList();
		//ServiceLevelObjectiveListItem
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			return req;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return req;
	}
	
	public ServiceLevelObjective getSLO(ServiceLevelObjectiveListItem item, boolean ReadOnly){
		return getSLO(item.getName(),ReadOnly);
	}
	
	public ServiceLevelObjective getSLO(String SLOName, boolean ReadOnly){
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = null;
		try {
			obj = broker.common.openObject(SLOName, ReadOnly);
		} catch (IOException e) {
			// error
		}
		if(obj == null){return null;}
		
		return (ServiceLevelObjective) obj;
	}
	
	
	public void enable(ServiceLevelObjective obj){
		changeSLOState(obj,true);
	}
	
	public void disable(ServiceLevelObjective obj){
		changeSLOState(obj,false);
	}
	
	public boolean ActivateLatestStartTime(ServiceLevelObjective obj,String HHMM){
		obj.fulfillment().setConsiderStartTime(true);
		if(!checkTime(HHMM)){return false;}
		obj.fulfillment().setLatestStartTime(new Time(Short.parseShort(HHMM.substring(0, 2)),Short.parseShort(HHMM.substring(2, 4)),Short.parseShort("00")));
		obj.fulfillment().setWeekdays(true, true, true, true, true, true, true);	
		return true;
	}
	
	public boolean ActivateLatestEndTime(ServiceLevelObjective obj,String HHMM){
		obj.fulfillment().setConsiderStartTime(true);
		if(!checkTime(HHMM)){return false;}
		obj.fulfillment().setLatestEndTime(new Time(Short.parseShort(HHMM.substring(0, 2)),Short.parseShort(HHMM.substring(2, 4)),Short.parseShort("00")));
		obj.fulfillment().setWeekdays(true, true, true, true, true, true, true);
		return true;
	}
	
	public boolean setWeekDays(ServiceLevelObjective obj,boolean Monday, boolean Tuesday, boolean Wednesday, boolean Thursday, boolean Friday, boolean Saturday, boolean Sunday){
		obj.fulfillment().setWeekdays(Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday);	
		return true;
	}
	
	// following method not yet available in UI
	@Deprecated
	public void setEmailOnFulfill(ServiceLevelObjective obj, String EmailAdr){
		obj.fulfillment().setEmailOnFulfillment(true);
		obj.fulfillment().setEmailRecipientFulfillment(EmailAdr);
	}
	
	public void setExampleQuery(ServiceLevelObjective obj, String XMLQuery){	
		obj.selection().setQuery(XMLQuery);
	}
	
	public void setExampleQuery(ServiceLevelObjective obj){	
		String XMLQUERY = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "<queryTreeNode groupOperator=\"ANY\">"
				+ "<node groupOperator=\"ALL\">"
				+ "<node><criterion>"
				+ "<attributeValue>OBJECT_NAME</attributeValue>"
				+ "<operator>CONTAINS</operator>"
				+ "<value>PSP</value></criterion></node>"
				+ "<node><criterion>"
                +"<attributeValue>OBJECT_TYPE</attributeValue>"
                + "<operator>EQUALS</operator>"
                + "<value>JOBS</value>"
                + "</criterion></node>"
                + "</node><node groupOperator=\"ALL\">"
                +"<node><criterion>"
                + "<attributeValue>OBJECT_NAME</attributeValue>"
                + "<operator>CONTAINS</operator>"
                + "<value>TEST</value></criterion></node><node>"
                +"<criterion>"
                + "<attributeValue>OBJECT_TYPE</attributeValue>"
                + "<operator>EQUALS</operator>"
                + "<value>JOBS</value></criterion></node></node></queryTreeNode>";
		
		obj.selection().setQuery(XMLQUERY);
	}
	
	// Name: Key from custom attr variable defined in UC_CUSTOM_ATTRIBUTES (ex: &BU# or &TEAM#)
	// Value: Key from the VARA Object referenced in UC_CUSTOM_ATTRIBUTES from Name
	public void addBeneficary(ServiceLevelObjective obj, String Name, String Value){
		Beneficiary b = new Beneficiary(Name, Value);
		obj.selection().addBeneficiary(b);
	}

	// following method not yet available in UI
	@Deprecated
	public void setEmailOnViolation(ServiceLevelObjective obj, String EmailAdr){
		obj.fulfillment().setEmailOnViolation(true);
		obj.fulfillment().setEmailRecipientViolation(EmailAdr);
	}
	
	public void setExecuteOnViolation(ServiceLevelObjective obj, String ObjectName){
		obj.fulfillment().setExecuteOnViolation(true);
		obj.fulfillment().setOnViolationObject(new UC4ObjectName(ObjectName));
	}
	
	public void setExecuteOnFulfill(ServiceLevelObjective obj, String ObjectName){
		obj.fulfillment().setExecuteOnFulfillment(true);
		obj.fulfillment().setOnFulfillmentObject(new UC4ObjectName(ObjectName));
	}
	
	public void setConsiderStatus(ServiceLevelObjective obj, boolean considerStatus){
		obj.fulfillment().setConsiderStatus(considerStatus);
	}
	
	private void changeSLOState(ServiceLevelObjective obj, boolean MakeActive){
		obj.attributes().setEnableSLOMonitoring(MakeActive);
	}
	
	public void ActivatePermanentMonitor(ServiceLevelObjective obj){
		obj.attributes().setMonitorPermanently();
	}
	
	public void setRuntimeOptions(ServiceLevelObjective obj, boolean considerMRT, boolean considerSRT){
		obj.fulfillment().setConsiderMRT(considerMRT);
		obj.fulfillment().setConsiderSRT(considerSRT);
	}
	
	public boolean setExpectedStatus(ServiceLevelObjective obj, String ExpectedStatus){
		obj.fulfillment().setConsiderStatus(true);
		TaskState state = UC4TaskState.getTaskStateFromValue(ExpectedStatus);
		if (state == null){return false;}
		obj.fulfillment().setConsiderStatus(true);
		obj.fulfillment().setExpectedStatus(state);
		return true;
	}
	
	public boolean ActivateLimitedMonitor(ServiceLevelObjective obj, String HHMMFrom, String HHMMTo){
		obj.attributes().setMonitorRestricted();
		if(!checkTime(HHMMFrom)){return false;}
		if(!checkTime(HHMMTo)){return false;}
		obj.attributes().setOperationWindowFrom(new Time(Short.parseShort(HHMMFrom.substring(0, 2)),Short.parseShort(HHMMFrom.substring(2, 4)),Short.parseShort("00")));
		obj.attributes().setOperationWindowTo(new Time(Short.parseShort(HHMMTo.substring(0, 2)),Short.parseShort(HHMMTo.substring(2, 4)),Short.parseShort("00")));
		return true;
	}
	
	public boolean setTimeZone(ServiceLevelObjective obj, String TZ){
		UC4TimezoneName tzname = null;
		try{
			tzname = new UC4TimezoneName(TZ);
		}catch(InvalidUC4NameException e){
			return false;
		}
		obj.attributes().setTimezone(tzname);
		return true;
	}
	
	private boolean checkTime(String HHMM){
		if(HHMM.length() != 4){return false;}
		String HH = HHMM.substring(0, 2);
		String MM = HHMM.substring(2, 4);
		
		if(!Utils.isInteger(HH)){return false;}
		if(!Utils.isInteger(MM)){return false;}
		
		int iHH = Integer.parseInt(HH);
		int iMM = Integer.parseInt(MM);
		
		if(iHH < 0 || iHH > 23){return false;}
		if(iMM < 0 || iMM > 59){return false;}
		return true;
	}
}
