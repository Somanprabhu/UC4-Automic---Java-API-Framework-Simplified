package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		
		public Client getCurrentClient() throws IOException{
			ObjectBroker broker = getBrokerInstance();
			String name = connection.getSessionInfo().getClient();
			Client client = (Client) broker.common.openObject(name,true);
			return client;
		}
		
		public void setClientSetting(String SettingName, String SettingValue) throws IOException{
			ObjectBroker broker = getBrokerInstance();
			String name = connection.getSessionInfo().getClient();
			Client client = (Client) broker.common.openObject(name,true);
			//client.setSetting("AUTO_FORECAST_DAYS", "10");
			client.setSetting(SettingName, SettingValue);
			broker.common.saveObject(client);
			broker.common.closeObject(client);
		}
		
		public String getClientSetting(String SettingName) throws IOException{
			ObjectBroker broker = getBrokerInstance();
			String name = connection.getSessionInfo().getClient();
			Client client = (Client) broker.common.openObject(name,true);
			String SettingValue = client.getSetting(SettingName);
			broker.common.saveObject(client);
			broker.common.closeObject(client);
			return SettingValue;
		}
		
		public void suspendCurrentClient() throws TimeoutException, IOException{
			SuspendClient req = new SuspendClient();
			connection.sendRequestAndWait(req);
			if (req.getMessageBox() != null) {
				System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
			}else{
				Say(" ++ Client: "+connection.getSessionInfo().getClient()+" Successfully Stopped.");
			}
		}
		
		public void resumeCurrentClient() throws TimeoutException, IOException{
			ResumeClient req = new ResumeClient();
			connection.sendRequestAndWait(req);
			if (req.getMessageBox() != null) {
				System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
			}else{
				Say(" ++ Client: "+connection.getSessionInfo().getClient()+" Successfully Started.");
			}
		}
		
		public void suspendAClient(ClientListItem Client) throws TimeoutException, IOException{
			SuspendClient req = new SuspendClient(Client);
			connection.sendRequestAndWait(req);
			if (req.getMessageBox() != null) {
				System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
			}else{
				Say(" ++ Client: "+Client.getClient()+" Successfully Stopped.");
			}
		}
		
		public void resumeAClient(ClientListItem Client) throws TimeoutException, IOException{
			ResumeClient req = new ResumeClient(Client);
			connection.sendRequestAndWait(req);
			if (req.getMessageBox() != null) {
				System.out.println(" -- "+req.getMessageBox().getText().toString().replace("\n", ""));
			}else{
				Say(" ++ Client: "+Client.getClient()+" Successfully Started.");
			}
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
