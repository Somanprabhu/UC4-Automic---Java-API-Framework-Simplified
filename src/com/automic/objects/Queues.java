package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;

import com.automic.utils.ObjectTypeEnum;
import com.automic.utils.Utils;
import com.uc4.api.QueueStatus;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.Queue;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.ModifyQueueStatus;
import com.uc4.communication.requests.QueueList;

public class Queues extends ObjectTemplate{

	public Queues(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public QueueList getQueueList() throws TimeoutException, IOException{
		QueueList req = new QueueList();
		return (QueueList) getBrokerInstance().common.sendGenericXMLRequestAndWait(req);
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
	
	public boolean stopQueue(String QueueName) throws IOException{
		UC4ObjectName name = new UC4ObjectName(QueueName);
		ModifyQueueStatus req = new ModifyQueueStatus(name, QueueStatus.RED);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString(" ++ Queue: "+QueueName+" Successfully Stopped."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
	
	public boolean startQueue(String QueueName) throws IOException{
		UC4ObjectName name = new UC4ObjectName(QueueName);
		ModifyQueueStatus req = new ModifyQueueStatus(name, QueueStatus.GREEN);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString(" ++ Queue: "+QueueName+" Successfully Started."));
			return true;
		}else{
			Say(Utils.getErrorString("Error:"  + req.getMessageBox().getText()));
		}
		return false;
	}
}
