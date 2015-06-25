package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.UC4UserName;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.Job;
import com.uc4.api.objects.UC4Object;
import com.uc4.api.objects.User;
import com.uc4.communication.Connection;
import com.uc4.communication.requests.MoveUserToClient;
import com.uc4.communication.requests.UserList;

public class Users extends ObjectTemplate{

	public Users(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public User getUserFromObject(UC4Object object){return (User) object;}
	public UserList getUserList() throws IOException{
		UserList req = new UserList();
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
		return req;
	}
	public User getUserFromName(String UserName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(UserName, false);
		User user = (User) obj;
		return user;
	}
	
	
	public void moveUserToClient(String UserName, String FolderName, int client) throws IOException{
		int currentClient = Integer.parseInt(this.connection.getSessionInfo().getClient());
		if(currentClient != 0){
			System.out.println(" -- Error: You can only move a client when connected to Client 0. Current Client is: "+currentClient);
			System.exit(1);
		}
		UC4UserName user = new UC4UserName(UserName);
		ObjectBroker broker = getBrokerInstance();
		IFolder folder = broker.folders.getFolderByName(FolderName);
		MoveUserToClient req = new MoveUserToClient(user,folder,client);
		connection.sendRequestAndWait(req);
		if (req.getMessageBox() != null) {
			System.out.println(" -- "+req.getMessageBox().getText());
		}
	}

}
