package com.automic.objects;

import java.io.IOException;

import com.uc4.api.objects.Client;
import com.uc4.communication.Connection;

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
		
}
