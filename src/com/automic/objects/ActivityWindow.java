package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.uc4.api.Task;
import com.uc4.api.TaskFilter;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.ActivityList;

public class ActivityWindow extends ObjectTemplate{

	public ActivityWindow(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	@SuppressWarnings("unused")
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	// return the content of the activity window
		public List<Task> getActivityWindowContent(TaskFilter taskFilter) throws IOException {		
			ActivityList list = new ActivityList(taskFilter);
			connection.sendRequestAndWait(list);		
			List<Task> tasks = new ArrayList<Task>();
			return tasks;
		}
		
		// return the content of the activity window
		public List<Task> getActivityWindowContent() throws IOException {		
			TaskFilter taskFilter = new TaskFilter();
			return getActivityWindowContent(taskFilter);
		}
}
