package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.automic.AECredentials;
import com.automic.ConnectionManager;
import com.automic.utils.Utils;
import com.uc4.api.InvalidUC4NameException;
import com.uc4.api.SearchResultItem;
import com.uc4.api.Template;
import com.uc4.api.UC4UserName;
import com.uc4.api.objects.Client;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.UC4Object;
import com.uc4.api.objects.User;
import com.uc4.api.objects.UserPrivileges.Privilege;
import com.uc4.api.objects.UserRight;
import com.uc4.api.objects.UserRight.Type;
import com.uc4.api.systemoverview.UserListItem;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.ActivateLDAPConnection;
import com.uc4.communication.requests.DeactivateLDAPConnection;
import com.uc4.communication.requests.DisconnectUser;
import com.uc4.communication.requests.MoveUserToClient;
import com.uc4.communication.requests.SynchronizeLDAP;
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
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say("");
			return req;
		}
		return req;
	}
	
	public SynchronizeLDAP synchronizeLDAPUser(String UserName) throws TimeoutException, IOException{
		UC4UserName uname = null;
		try{
			uname = new UC4UserName(UserName);
		}catch(InvalidUC4NameException e){
			Say(Utils.getErrorString("Username not a valid Automic name: userName."));
			return null;
		}
		SynchronizeLDAP req = new SynchronizeLDAP(uname);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			return req;
		}
		return req;
	}
	
	public DeactivateLDAPConnection DeactivateLDAPConnection(UserListItem item) throws TimeoutException, IOException{
		DeactivateLDAPConnection req = new DeactivateLDAPConnection(item);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() != null) {
			Say(Utils.getErrorString(req.getMessageBox()));
		}
		return req;
	}
	
	public DeactivateLDAPConnection DeactivateLDAPConnection(String username) throws IOException{
		boolean UserFound = false;
		UserList list = this.getBrokerInstance().users.getUserList();
		Iterator<UserListItem> it = list.iterator();
		while(it.hasNext()){
			UserListItem item = it.next();
			if(item.getName().toString().equalsIgnoreCase(username)){
				UserFound=true;
				return DeactivateLDAPConnection(item);
			}
		}
		if(!UserFound){
			Say(Utils.getErrorString("User Not Found: " + username));
			return null;
		}
		return null;
	}
	
	public ActivateLDAPConnection activateLDAPConnection(UserListItem item) throws TimeoutException, IOException{
		ActivateLDAPConnection req = new ActivateLDAPConnection(item);
		sendGenericXMLRequestAndWait(req);
		return req;
	}
	
	public ActivateLDAPConnection activateLDAPConnection(String username) throws IOException{
		boolean UserFound = false;
		UserList list = this.getBrokerInstance().users.getUserList();
		Iterator<UserListItem> it = list.iterator();
		while(it.hasNext()){
			UserListItem item = it.next();
			if(item.getName().toString().equalsIgnoreCase(username)){
				UserFound=true;
				return activateLDAPConnection(item);
			}
		}
		if(!UserFound){
			Say(Utils.getErrorString("User Not Found: " + username));
			return null;
		}
		return null;
	}
	
	
	public String disconnectActiveUser(String UserName) throws IOException{
		UserList list = getUserList();
		Iterator<UserListItem> it = list.iterator();
		boolean UserFound = false;
		while(it.hasNext()){
			UserListItem uli = it.next();
			if(uli.getName().toString().equalsIgnoreCase(UserName)){
				UserFound = true;
				DisconnectUser req = new DisconnectUser(uli);
				sendGenericXMLRequestAndWait(req);
			}
		}
		return "User not found.";
	}
	
	public User getUserFromName(String UserName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(UserName, false);
		User user = (User) obj;
		return user;
	}
	
	public User createUserInCurrentClient(String Username, IFolder folder) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(Username, Template.USER, folder);
		User user = getUserFromObject(broker.common.openObject(Username, false));
		return user;
	}
	
	public User createAdminUserInClient(AECredentials creds, String Username, String Password, IFolder folder) throws IOException{
		
		// 0 - initiate a new connection & broker to a different client
		Connection ConnToClient = ConnectionManager.connectToClient(creds);
		ObjectBroker BrokerToClient = new ObjectBroker(ConnToClient,true);
		
		return createUserInClient(BrokerToClient, Username, Password, folder);
	}
	
	public User createAdminUserInCurrentClient(String Username, String Password, IFolder folder) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return createUserInClient(broker, Username, Password, folder);
	}
	
	private User createUserInClient(ObjectBroker broker, String Username, String Password, IFolder folder) throws IOException{
        
		IFolder destFolder = null;
		if(folder == null){destFolder = broker.folders.getRootFolder();}
		else{destFolder = folder;}
		broker.common.createObject(Username, Template.USER,destFolder);
		User user = getUserFromObject(broker.common.openObject(Username, false));
		user.attributes().setPassword(Password);
		user.attributes().setActive(true);
		user.attributes().setLocked(false);
		UserRight all = new UserRight();
		all.setCancel(true);
		all.setDefineSLA(true);
		all.setDelete(true);
		all.setExecute(true);
		all.setModifyAtRuntime(true);
		all.setRead(true);
		all.setReport(true);
		all.setStatistics(true);
		all.setWrite(true);
		all.setFileNameDestination("*");
		all.setFileNameSource("*");
		all.setGrp(1);
		all.setHost("*");
		all.setHostDestination("*");
		all.setLogin("*");
		all.setLoginDestination("*");
		all.setName("*");
		all.setType(Type.ALL);
		user.authorizations().addRight(all);
		
		user.privileges().setPrivilege(Privilege.ACCESS_NOFOLDER, true);
		user.privileges().setPrivilege(Privilege.ACCESS_RECYCLE_BIN, true);
		user.privileges().setPrivilege(Privilege.ACCESS_SYSTEM_OVERVIEW, true);
		user.privileges().setPrivilege(Privilege.ACCESS_TRANSPORT_CASE, true);
		user.privileges().setPrivilege(Privilege.AUTHORIZATIONS_OBJECT_LEVEL, true);
		user.privileges().setPrivilege(Privilege.AUTO_FORECAST, true);
		user.privileges().setPrivilege(Privilege.BACKEND_VARIABLE, true);
		user.privileges().setPrivilege(Privilege.CHANGE_SYSTEM_STATUS, true);
		user.privileges().setPrivilege(Privilege.CREATE_DIAGNOSTIC_INFO, true);
		user.privileges().setPrivilege(Privilege.ECC_ACCESS_ANALYTICS, true);
		user.privileges().setPrivilege(Privilege.ECC_ADMINISTRATION, true);
		user.privileges().setPrivilege(Privilege.ECC_DASHBOARDS, true);
		user.privileges().setPrivilege(Privilege.ECC_DECISION_AUTOMATION, true);
		user.privileges().setPrivilege(Privilege.ECC_MANAGE_SLA_AND_BU, true);
		user.privileges().setPrivilege(Privilege.ECC_MESSAGES, true);
		user.privileges().setPrivilege(Privilege.ECC_PREDICTIVE_ANALYSIS, true);
		user.privileges().setPrivilege(Privilege.ECC_PROCESS_AUTOMATION, true);
		user.privileges().setPrivilege(Privilege.ECC_PROCESS_DEVELOPMENT, true);
		user.privileges().setPrivilege(Privilege.ECC_PROCESS_MONITORING, true);
		user.privileges().setPrivilege(Privilege.ECC_SERVICE_LEVEL_GOVENOR, true);
		user.privileges().setPrivilege(Privilege.FAVORITES_USER_GROUP, true);
		user.privileges().setPrivilege(Privilege.FILE_TRANSFER_WITHOUT_USERID, true);
		user.privileges().setPrivilege(Privilege.FILEEVENTS_WITHOUT_LOGIN, true);
		user.privileges().setPrivilege(Privilege.ILM_ACTIONS, true);
		user.privileges().setPrivilege(Privilege.LOGON_CALL_API, true);
		user.privileges().setPrivilege(Privilege.MESSAGES_ADMINISTRATORS, true);
		user.privileges().setPrivilege(Privilege.MESSAGES_OWN_CLIENT, true);
		user.privileges().setPrivilege(Privilege.MESSAGES_OWN_GROUP, true);
		user.privileges().setPrivilege(Privilege.MESSAGES_SECURTY, true);
		user.privileges().setPrivilege(Privilege.MODIFY_STATUS_MANUALLY, true);
		user.privileges().setPrivilege(Privilege.RESET_OPEN_FLAG, true);
		user.privileges().setPrivilege(Privilege.SAP_CRITERIA_MANAGER, true);
		user.privileges().setPrivilege(Privilege.SELECTIVE_STATISTICS, true);
		user.privileges().setPrivilege(Privilege.SERVER_USAGE_ALL_CLIENTS, true);
		user.privileges().setPrivilege(Privilege.SQL_VARIABLE, true);
		user.privileges().setPrivilege(Privilege.TAKE_OVER_TASK, true);
		user.privileges().setPrivilege(Privilege.VERSION_MANAGEMENT, true);
		user.privileges().setPrivilege(Privilege.WORK_IN_RUNBOOK_MODE, false);

		broker.common.saveObject(user);
		return user;
	}
	
	public boolean moveUserToClient(String UserName, String FolderName, int client) throws IOException{
		int currentClient = Integer.parseInt(this.connection.getSessionInfo().getClient());
		if(currentClient != 0){
			Utils.getErrorString("You can only move a client when connected to Client 0. Current Client is: "+currentClient);
			return false;
		}
		UC4UserName user = new UC4UserName(UserName);
		ObjectBroker broker = getBrokerInstance();
		IFolder folder = broker.folders.getFolderByName(FolderName);
		MoveUserToClient req = new MoveUserToClient(user,folder,client);
		sendGenericXMLRequestAndWait(req);
		
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("User: "+UserName+" successfully moved to Client: "+client));
			return true;
		}
		return false;
	}
	
	public boolean moveUserToClient(String UserName, int client) throws IOException{
		int currentClient = Integer.parseInt(this.connection.getSessionInfo().getClient());
		if(currentClient != 0){
			Say(Utils.getErrorString("You can only move a client when connected to Client 0. Current Client is: "+currentClient));
			return false;
		}
		
		UC4UserName user = null;
		if(UserName.contains(" ")){
			user = new UC4UserName(UserName.split(" ")[2]);
		}
		else{
			user = new UC4UserName(UserName);
		}
				
		ObjectBroker broker = getBrokerInstance();
		List<SearchResultItem> foundUsers = broker.searches.searchUsersAndGroups(user.getName());
		if(foundUsers.isEmpty()){
			Say(Utils.getErrorString("Could Not Find User: " + user.getName()));
			return false;
		}
		if(foundUsers.size()>1){
			Say(Utils.getErrorString("Found Multiple Users Matching: " + user.getName()));
			return false;
		}
		SearchResultItem item = foundUsers.get(0);
		
		IFolder UserFolder = null; 
		if(!item.getFolder().contains(" ")){
			UserFolder = broker.folders.getRootFolder();
		}else{
			UserFolder = broker.folders.getFolderByFullPathName(item.getFolder());
		}

		MoveUserToClient req = new MoveUserToClient(user,UserFolder,client);
		sendGenericXMLRequestAndWait(req);
		if (req.getMessageBox() == null) {
			Say(Utils.getSuccessString("User: "+UserName+" successfully moved to Client: "+client));
			return true;
		}
		return false;
	}
}
