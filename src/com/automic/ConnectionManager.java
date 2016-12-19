package com.automic;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.channels.UnresolvedAddressException;
import java.util.ArrayList;

import com.automic.utils.Utils;
import com.uc4.communication.Connection;
import com.uc4.communication.SSOConfiguration;
import com.uc4.communication.requests.CreateSession;

public final class ConnectionManager {

	private static Connection conn = null;
	public static ArrayList<Connection> ConnectionList = new ArrayList<Connection>();
	
	// Leave constructor empty
	public ConnectionManager(){}
	
	public static CreateSession attemptLogin(AECredentials credentials) throws IOException{
		if(credentials.getSSO()){
			return attemptSSOLogin(credentials);
		}else{
			return attemptStdLogin(credentials);
		}
	}
	
	// Try an SSO login to AE
	public static CreateSession attemptSSOLogin(AECredentials credentials) throws IOException{		
		conn = getConnection(credentials.getAEHostnameOrIp(), credentials.getAECPPortList());
		return LoginToAEwithSSO(conn,credentials.getAEClientToConnect());
	}
	


	// Try a Standard login to AE
	public static CreateSession attemptStdLogin(AECredentials credentials) throws IOException{
		conn = getConnection(credentials.getAEHostnameOrIp(), credentials.getAECPPortList());
		
		String PASSWORD = credentials.getAEUserPassword();
		// #1 - Fix. if password passed with single quotes, they are interpreted as characters.. removing them if detected:
		if(PASSWORD.startsWith("'") && PASSWORD.endsWith("'")){
			PASSWORD = PASSWORD.substring(1, PASSWORD.length()-1);
		}
		String ClearPwd = PASSWORD;
		return LoginToAE(conn,credentials.getAEClientToConnect(), credentials.getAEUserLogin(), credentials.getAEDepartment(), ClearPwd, credentials.getAEMessageLanguage());
	}
	
	// Login to AE from an established Connection (the standard way)
	private static CreateSession LoginToAE(Connection MyConnection, int Client, String Login, String Dept, String Pwd, char Language) throws IOException{
		CreateSession sess = null;
		
		try{
		sess = conn.login(Client,Login,Dept,Pwd,Language);
		}catch(RuntimeException e){
			if(e.getMessage().contains("Null input buffer")){
				System.out.println("-- Error: Login Failed: It seems you are trying to login with an invalid encrypted password that cannot be decoded. Please Check.");
			}else{
				System.out.println("-- Error: Login Failed. Unknown Runtime Error. Send the following stacktrace to vendor:");
				e.printStackTrace();
			}
			
			return null;
		}
		return sess;
	}
	
	private static CreateSession LoginToAEwithSSO(Connection myConnection,int ClientNum) throws IOException{
		SSOConfiguration ssoconfig = new SSOConfiguration(ClientNum);
		CreateSession sess = null;
		try{
			sess = myConnection.login(ssoconfig);
		}catch(RuntimeException e){
			if(e.getMessage().contains("Null input buffer")){
				return null;
			}else{
				return null;
			}
		}
		if(sess.getMessageBox()!=null){return null;}
		return sess;
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
	
	private static Connection getConnection(String Hostname, ArrayList<Integer> PortList) {
		for(int i=0;i<PortList.size();i++){
			int Port = PortList.get(i);
			try{
				conn = Connection.open(Hostname, Port);
			}catch (UnresolvedAddressException e){
				System.out.println(" -- Error: Could Not Resolve Host or IP: "+Hostname);
				System.exit(999);
				
			}catch (ConnectException c){
				System.out.println(" -- Warning: Could Not Connect to [Hostname:Port]: [" + Hostname+":"+Port+"]");
				//System.out.println(" --     Hint: is the host or IP reachable?");
				continue;
				//System.exit(998);
			} catch (IOException e) {
				System.out.println(" -- Unknown Error: Could Not Connect to Host / Port: " + Hostname+":"+Port);
				continue;
			}
			System.out.println(" %% Info: Connection Successfully Established for [Hostname:Port]: [" + Hostname+":"+Port+"]\n");
			return conn;
		}
		System.out.println(" -- Error: Could not establish connection on any port!");
		System.exit(996);
		return null;
	}
	
//	private static Connection getConnection(String Hostname, int PortNum) throws IOException{
//		try{
//			conn = Connection.open(Hostname, PortNum);
//		}catch (UnresolvedAddressException e){
//			System.out.println(" -- ERROR: Could Not Resolve Host or IP: "+Hostname);
//			System.exit(999);
//			
//		}catch (ConnectException c){
//			System.out.println(" -- ERROR: Could Not Connect to Host: " + Hostname);
//			System.out.println(" --     Hint: is the host or IP reachable?");
//			System.exit(998);
//		}
//		return conn;
//	}

public static Connection connectToClientWithSSO(AECredentials credentials) throws IOException{
		
		conn = getConnection(credentials.getAEHostnameOrIp(),credentials.getAECPPortList());
	
		CreateSession sess = LoginToAEwithSSO(conn,credentials.getAEClientToConnect());
		
		if(sess == null){
			System.out.println("\t-- Error: Cound Not Connect Using SSO."); 
			System.exit(999);
		}
		//#1 Fix Done
		if(sess.getMessageBox()!=null){
			System.out.println("-- Error: " + sess.getMessageBox()); 
			System.exit(998);
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

	public static Connection connectToClient(AECredentials credentials) throws IOException{
		if(credentials.getSSO()){
			return connectToClientWithSSO(credentials);
		}else{
			return connectToClientwithSTD(credentials);
		}
	}

private static Connection connectToClientwithSTD(AECredentials credentials) throws IOException{
	conn = getConnection(credentials.getAEHostnameOrIp(),credentials.getAECPPortList());
	
	String PASSWORD = credentials.getAEUserPassword();

	// #1 - Fix. if password passed with single quotes, they are interpreted as characters.. removing them if detected:
	if(PASSWORD.startsWith("'") && PASSWORD.endsWith("'")){
		PASSWORD = PASSWORD.substring(1, PASSWORD.length()-1);
	}
	
	String ClearPwd = PASSWORD;
	CreateSession sess = LoginToAE(conn,credentials.getAEClientToConnect(),credentials.getAEUserLogin(),credentials.getAEDepartment(),
		credentials.getAEUserPassword(),credentials.getAEMessageLanguage());
	
	//#1 Fix Done
	if(sess.getMessageBox()!=null){
		System.out.println("-- Error: " + sess.getMessageBox()); 
		System.exit(999);
	}
	
	// Check Server Version:
	String serverVersion = conn.getSessionInfo().getServerVersion();
	if(! SupportedAEVersions.SupportedVersions.contains(serverVersion)){
		System.err.println( " -- Error! Version of the Automation Engine does not seem supported.");
		System.err.println( " -- current version is: "+serverVersion);
		System.err.println( " -- versions supported: "+SupportedAEVersions.SupportedVersions.toString());
		System.exit(998);
	}
	
	ConnectionList.add(conn);
	return conn;
	
}
}
