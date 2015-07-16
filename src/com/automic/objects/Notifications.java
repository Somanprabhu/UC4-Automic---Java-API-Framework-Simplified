package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.objects.Notification;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.ActiveNotifications;
import com.uc4.communication.requests.ActiveNotifications.Entry;

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
	
	public ArrayList<Entry> getActiveNotifications() throws TimeoutException, IOException{
		ActiveNotifications active = new ActiveNotifications();
		connection.sendRequestAndWait(active);
		ArrayList<ActiveNotifications.Entry> AllActiveNotifications = new ArrayList<ActiveNotifications.Entry>();
		Iterator<ActiveNotifications.Entry> it = active.iterator();
		while(it.hasNext()){
			AllActiveNotifications.add(it.next());
		}
		return AllActiveNotifications;
	}
}
