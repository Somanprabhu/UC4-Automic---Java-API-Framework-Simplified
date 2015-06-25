package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.Time;
import com.uc4.api.objects.Job;
import com.uc4.api.objects.Schedule;
import com.uc4.api.objects.ScheduleTask;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Schedules extends ObjectTemplate{

	public Schedules(Connection conn, boolean verbose) {
		super(conn, verbose);

	}
	private ObjectBroker getBrokerInstance() {
		return new ObjectBroker(this.connection, true);
	}
	public Schedule getScheduleFromObject(UC4Object object){return (Schedule) object;}
	public void setTaskTime(ScheduleTask task, int Hours, int Minutes, int Seconds){
		short hours = (short)Hours;
		short minutes = (short)Minutes;
		short seconds = (short)Seconds;
		Time newstime = new Time(hours,minutes,seconds);
		task.setStartTime(newstime);
	}
	
	// Modify a property of a Schedule object
	public void setSchedulePriority(UC4Object object, int priority)
			throws IOException {
		ObjectBroker broker = getBrokerInstance();
		Schedule schedule = (Schedule) object;
		schedule.attributes().setPriority(priority);
		broker.common.saveObject(schedule);
	}
	
	// Modify a property of a Schedule object
	public ArrayList<ScheduleTask> getListOfTasks(UC4Object object, int priority) throws IOException {
		ArrayList<ScheduleTask> ObjList = new ArrayList<ScheduleTask>();
		Schedule schedule = (Schedule) object;
		Iterator<ScheduleTask> iter = schedule.taskIterator();
		while(iter.hasNext()){
			ScheduleTask task = iter.next();
			ObjList.add(task);
		}
		return ObjList;
	}
	

	
	public ArrayList<UC4Object> getAllSchedules() throws IOException {
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.JSCH);
	}

	public ArrayList<UC4Object> getAllSchedulesWithFilter(String filter)
			throws IOException {
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.JSCH,
				filter);
	}
	
}
