package com.automic;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.channels.UnresolvedAddressException;
import java.util.ArrayList;

import com.uc4.communication.Connection;
import com.uc4.communication.requests.CreateSession;

public class ConnectionManager {

	private Connection conn = null;
	
	public ArrayList<Connection> ConnectionList = new ArrayList<Connection>();
	
	public ConnectionManager(){
		
	}
	
	public Connection connectToClient(AECredentials credentials) throws IOException{
		
		//System.out.println("Authenticating to Client "+credentials.getAEClientToConnect()+" with user "+credentials.getAEUserLogin());
		try{
			conn = Connection.open(credentials.getAEHostnameOrIp(), credentials.getAECPPort());
		}catch (UnresolvedAddressException e){
			System.out.println(" -- ERROR: Could Not Resolve Host or IP: "+credentials.getAEHostnameOrIp());
			System.exit(999);
		}catch (ConnectException c){
			System.out.println(" -- ERROR: Could Not Connect to Host: " + credentials.getAEHostnameOrIp());
			System.out.println(" --     Hint: is the host or IP reachable?");
			System.exit(998);
			
		}
		
		CreateSession sess = conn.login(credentials.getAEClientToConnect(), credentials.getAEUserLogin(), 
				credentials.getAEDepartment(), credentials.getAEUserPassword(), credentials.getAEMessageLanguage());
		
		if(sess.getMessageBox()!=null){
			System.out.println("-- Error: " + sess.getMessageBox()); 
			System.exit(990);
			return null;
		}
		// Check Server Version:
		String serverVersion = conn.getSessionInfo().getServerVersion();
		if(! SupportedAEVersions.SupportedVersions.contains(serverVersion)){
			System.err.println( " -- Error! Version of the Automation Engine does not seem supported.");
			System.err.println( " -- current version is: "+serverVersion);
			System.err.println( " -- versions supported: "+SupportedAEVersions.SupportedVersions.toString());
			System.exit(1);
		}
		
		ConnectionList.add(conn);
		return conn;
		
	}
	
	public Connection switchToClient(AECredentials credentials) throws IOException{
		conn = Connection.open(credentials.getAEHostnameOrIp(), credentials.getAECPPort());
		CreateSession sess = conn.login(credentials.getAEClientToConnect(), credentials.getAEUserLogin(), 
				credentials.getAEDepartment(), credentials.getAEUserPassword(), credentials.getAEMessageLanguage());
		ConnectionList.add(conn);
		return conn;
	}
	
	public Connection switchToExistingClient(String ClientNumber){
		for(Connection conn: ConnectionList){
			if(conn.getSessionInfo().getClient().equalsIgnoreCase(ClientNumber)){
				return conn;
			}
		}
		return null;
	}
}
