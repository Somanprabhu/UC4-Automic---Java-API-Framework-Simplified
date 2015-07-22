package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.QueueStatus;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.Queue;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.ModifyQueueStatus;

public class Queues extends ObjectTemplate{

	public Queues(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public Queue getQueueFromObject(UC4Object object){return (Queue) object;}
	public ArrayList<UC4Object> getAllQueues() throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.QUEUE);
	}
	public ArrayList<UC4Object> getAllQueuesWithFilter(String filter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.QUEUE,filter);
	}
	public void stopQueue(String QueueName) throws IOException{
		UC4ObjectName name = new UC4ObjectName(QueueName);
		ModifyQueueStatus req = new ModifyQueueStatus(name, QueueStatus.RED);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" ++ Queue: "+QueueName+" Successfully Stopped.");
		}
	}
	public void startQueue(String QueueName) throws IOException{
		UC4ObjectName name = new UC4ObjectName(QueueName);
		ModifyQueueStatus req = new ModifyQueueStatus(name, QueueStatus.GREEN);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
		}else{
			Say(" ++ Queue: "+QueueName+" Successfully Started.");
		}
	}
}
