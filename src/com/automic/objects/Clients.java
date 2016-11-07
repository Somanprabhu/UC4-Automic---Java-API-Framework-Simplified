package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.automic.AECredentials;
import com.automic.ConnectionManager;
import com.automic.utils.Utils;
import com.uc4.api.SearchResultItem;
import com.uc4.api.objects.Client;
import com.uc4.api.systemoverview.ClientListItem;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.ClientList;
import com.uc4.communication.requests.ResumeClient;
import com.uc4.communication.requests.SearchObject;
import com.uc4.communication.requests.SuspendClient;

public class Clients extends ObjectTemplate{

		public Clients(Connection conn, boolean verbose) {
			super(conn, verbose);
		}
		
		private ObjectBroker getBrokerInstance(){
			return new ObjectBroker(this.connection,true);
		}
		
//		public boolean createClient(int ClientNumber){
//			ObjectBroker broker = getBrokerInstance();
//			broker.common.createObject(name, Template., fold)
//		}
		
		public Client getCurrentClient() throws IOException{
			ObjectBroker broker = getBrokerInstance();
			String name = connection.getSessionInfo().getClient();
			Client client = (Client) broker.common.openObject(name,true);
			return client;
		}
		
		public boolean setCurrentClientSetting(String SettingName, String SettingValue) throws IOException{
			ObjectBroker broker = getBrokerInstance();
			String name = connection.getSessionInfo().getClient();
			Client client = (Client) broker.common.openObject(name,true);
			//client.setSetting("AUTO_FORECAST_DAYS", "10");
			client.setSetting(SettingName, SettingValue);
			return broker.common.saveAndCloseObject(client);
		}
		
		public boolean setClientSetting(AECredentials creds, String SettingName, String SettingValue) throws IOException{
			
			// 0 - initiate a new connection & broker to a different client
			Connection ConnToClient = ConnectionManager.connectToClient(creds);
			ObjectBroker BrokerToClient = new ObjectBroker(ConnToClient,true);
			
			// 1 - get the client name from new connection
			String name = ConnToClient.getSessionInfo().getClient();
			
			// 2 - open the Client via the new broker
			Client client = (Client) BrokerToClient.common.openObject(name,false);

			// 3 - set the Client Setting and save
			client.setSetting(SettingName, SettingValue);	
			return BrokerToClient.common.saveAndCloseObject(client);
		}
		
		public String getCurrentClientSetting(String SettingName) throws IOException{
			ObjectBroker broker = getBrokerInstance();
			String name = connection.getSessionInfo().getClient();
			Client client = (Client) broker.common.openObject(name,true);
			String SettingValue = client.getSetting(SettingName);
			broker.common.saveObject(client);
			broker.common.closeObject(client);
			return SettingValue;
		}
		
		public HashMap<String, String> getCurrentClientSettings() throws IOException{
			
			HashMap<String, String> SettingsMap = new HashMap<String, String>();
			
			ObjectBroker broker = getBrokerInstance();
			String name = connection.getSessionInfo().getClient();
			Client client = (Client) broker.common.openObject(name,true);
			
			Iterator<String> settingIterator = client.settingNames();
			while(settingIterator.hasNext()){
				String SettingName = settingIterator.next();
				String SettingValue = client.getSetting(SettingName);
				SettingsMap.put(SettingName, SettingValue);
			}
			
			return SettingsMap;
		}
		
		public HashMap<String, String> getClientSettings(AECredentials creds) throws IOException{
			
			HashMap<String, String> SettingsMap = new HashMap<String, String>();
			
			// 0 - initiate a new connection & broker to a different client
			Connection ConnToClient = ConnectionManager.connectToClient(creds);
			ObjectBroker BrokerToClient = new ObjectBroker(ConnToClient,true);
			String name = ConnToClient.getSessionInfo().getClient();
			
			Client client = (Client) BrokerToClient.common.openObject(name,true);
			
			Iterator<String> settingIterator = client.settingNames();
			while(settingIterator.hasNext()){
				String SettingName = settingIterator.next();
				String SettingValue = client.getSetting(SettingName);
				SettingsMap.put(SettingName, SettingValue);
			}
			
			return SettingsMap;
		}
		
		public boolean clearCurrentClientSettings() throws IOException{
			
			ObjectBroker broker = getBrokerInstance();
			String name = connection.getSessionInfo().getClient();
			Client client = (Client) broker.common.openObject(name,false);
			client.clearSettings();
			broker.common.saveObject(client);
			return broker.common.saveAndCloseObject(client);
		}
		
		public boolean clearClientSettings(AECredentials creds) throws IOException{
			
			// 0 - initiate a new connection & broker to a different client
			Connection ConnToClient = ConnectionManager.connectToClient(creds);
			ObjectBroker BrokerToClient = new ObjectBroker(ConnToClient,true);
			String name = ConnToClient.getSessionInfo().getClient();
			
			Client client = (Client) BrokerToClient.common.openObject(name,false);
			client.clearSettings();

			return BrokerToClient.common.saveAndCloseObject(client);
		}
		
		public String getClientSetting(AECredentials creds, String SettingName) throws IOException{
			// 0 - initiate a new connection & broker to a different client
			Connection ConnToClient = ConnectionManager.connectToClient(creds);
			ObjectBroker BrokerToClient = new ObjectBroker(ConnToClient,true);
			
			// 1 - get the client name from new connection
			String name = ConnToClient.getSessionInfo().getClient();
			
			// 2 - open the Client via the new broker
			Client client = (Client) BrokerToClient.common.openObject(name,true);

			String SettingValue = client.getSetting(SettingName);

			return SettingValue;
		}
		
		public boolean suspendCurrentClient() throws TimeoutException, IOException{
			SuspendClient req = new SuspendClient();
			sendGenericXMLRequestAndWait(req);
			
			if (req.getMessageBox() == null) {
				Say(Utils.getSuccessString("Client: "+connection.getSessionInfo().getClient()+" Successfully Stopped."));
				return true;
			}
			return false;			
		}
		
		public boolean resumeCurrentClient() throws TimeoutException, IOException{
			ResumeClient req = new ResumeClient();
			sendGenericXMLRequestAndWait(req);
			
			if (req.getMessageBox() == null) {
				Say(Utils.getSuccessString("Client: "+connection.getSessionInfo().getClient()+" Successfully Started."));
				return true;
			}
			return false;		
		}
		
		public boolean suspendAClient(ClientListItem Client) throws TimeoutException, IOException{
			SuspendClient req = new SuspendClient(Client);
			sendGenericXMLRequestAndWait(req);
			if (req.getMessageBox() == null) {
				Say(Utils.getSuccessString("Client: "+Client.getClient()+" Successfully Stopped."));
				return true;
			}
			return false;	
		}
		
		public boolean resumeAClient(ClientListItem Client) throws TimeoutException, IOException{
			ResumeClient req = new ResumeClient(Client);
			sendGenericXMLRequestAndWait(req);
			
			if (req.getMessageBox() == null) {
				Say(Utils.getSuccessString("Client: "+Client.getClient()+" Successfully Started."));
				return true;
			}
			return false;	
		}
	
		public ClientList getSimpleClientList() throws TimeoutException, IOException{
			ClientList req = new ClientList();
			sendGenericXMLRequestAndWait(req);
			
			if (req.getMessageBox() == null) {
				return req;
			}
			return req;	
		}
		
		public ArrayList<ClientListItem> getClientList() throws TimeoutException, IOException{
			ClientList clList = new ClientList();
			connection.sendRequestAndWait(clList);
			Iterator<ClientListItem> it =  clList.iterator();
			ArrayList<ClientListItem> clients = new ArrayList<ClientListItem>();
			while(it.hasNext()){
				ClientListItem item = it.next();
				clients.add(item);
			}
			return clients;
		}
		
		public void displayClientList(ArrayList<ClientListItem> clients) throws TimeoutException, IOException{
			System.out.println("\nNumber Of Clients Defined: "+clients.size());
			//System.out.println("Client:[Client Name]:[Client Title]:[Client Active]:[# Of Objects In Client]:[# of Users In Client]");
			System.out.format("\n%-8s, %-30s, %-9s, %-18s, %-16s, %-14s, %-10s\n","[Client]","[Client Title]","[Active]","[Objects Defined]","[Users Defined]","[Activities]","[Timezone]");
			for(int i =0;i< clients.size();i++){
				ClientListItem client = clients.get(i);
				String ClientName = String.valueOf(client.getClient());
				String isClientActive = String.valueOf(client.isClientActive());
				String NumberOfObjects = String.valueOf(client.getNumberOfObjects());
				String NumberOfUsers = String.valueOf(client.getNumberOfUsers());
				String ClientTitle = client.getTitle();
				String ClientTZ = client.getTimezone().getName();
				String NumberOfActivities = String.valueOf(client.getNumberOfActivities());
				
				//System.out.println("Client:"+ClientName+":"+ClientTitle+":"+isClientActive+":"+NumberOfObjects+":"+NumberOfUsers);
				System.out.format("%-8s, %-30s, %-9s, %-18s, %-16s, %-14s, %-10s\n",ClientName,ClientTitle,isClientActive,NumberOfObjects,NumberOfUsers,NumberOfActivities,ClientTZ);
			}
		}
		
		public void displayClientList() throws TimeoutException, IOException{
			ArrayList<ClientListItem> clients = getClientList();
			System.out.println("\nNumber Of Clients Defined: "+clients.size());
			//System.out.println("Client:[Client Name]:[Client Title]:[Client Active]:[# Of Objects In Client]:[# of Users In Client]");
			System.out.format("\n%-8s, %-30s, %-9s, %-18s, %-16s, %-14s, %-10s\n","[Client]","[Client Title]","[Active]","[Objects Defined]","[Users Defined]","[Activities]","[Timezone]");
			for(int i =0;i< clients.size();i++){
				ClientListItem client = clients.get(i);
				String ClientName = String.valueOf(client.getClient());
				String isClientActive = String.valueOf(client.isClientActive());
				String NumberOfObjects = String.valueOf(client.getNumberOfObjects());
				String NumberOfUsers = String.valueOf(client.getNumberOfUsers());
				String ClientTitle = client.getTitle();
				String ClientTZ = client.getTimezone().getName();
				String NumberOfActivities = String.valueOf(client.getNumberOfActivities());
				
				//System.out.println("Client:"+ClientName+":"+ClientTitle+":"+isClientActive+":"+NumberOfObjects+":"+NumberOfUsers);
				System.out.format("%-8s, %-30s, %-9s, %-18s, %-16s, %-14s, %-10s\n",ClientName,ClientTitle,isClientActive,NumberOfObjects,NumberOfUsers,NumberOfActivities,ClientTZ);
			}
		}
		
}
