package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.objects.Notification;
import com.uc4.api.objects.Schedule;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Notifications  extends ObjectTemplate{

	public Notifications(Connection conn, boolean verbose) {
		super(conn, verbose);

	}
	private ObjectBroker getBrokerInstance() {
		return new ObjectBroker(this.connection, true);
	}
	
	public ArrayList<UC4Object> getAllSchedules() throws IOException {
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.CALL);
	}

	public ArrayList<UC4Object> getAllSchedulesWithFilter(String filter)
			throws IOException {
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.CALL,
				filter);
	}
	// Modify a property of a Schedule object
	public void setNotificationPriority(UC4Object object, int priority)
			throws IOException {
		ObjectBroker broker = getBrokerInstance();
		Notification notification = (Notification) object;
		notification.attributes().setPriority(priority);
		broker.common.saveObject(notification);
	}
	
}
