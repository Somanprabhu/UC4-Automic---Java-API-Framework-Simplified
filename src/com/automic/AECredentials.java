package com.automic;

import java.util.ArrayList;

public class AECredentials {

	/** 
	 *    !!! Change the credentials below to your own system !!!
	 **/
	
	private String AEHostnameOrIp;  // Automation Engine IP Adr
	//private int AECPPort; 		    // "Primary" Communication Process port
	private int AEClientToConnect;  // AE Client Number (0 - 9999)
	private String AEDepartment;    // User Department
	private String AEUserLogin;  	// AE User Login
	private String AEUserPassword; 	// AE User Password
	private char AEMessageLanguage; // Language: 'E' or 'D', or 'F'
	private boolean isSSO;
	private ArrayList<Integer> FinalPortCollection = new ArrayList<Integer>();
	
	/**
	 * @deprecated
	 * pass Port as a string or one or more ports separated by commas.
	 * ex: 2217 or 2217,2218,2219
	 */
	public AECredentials(String AEHostnameOrIp,int AECPPort,int AEClientToConnect,String AEUserLogin, String AEDepartment,String AEUserPassword,char AEMessageLanguage ){
		
		this.AEHostnameOrIp = AEHostnameOrIp;
		//this.AECPPort = AECPPort;
		this.AEClientToConnect = AEClientToConnect;
		this.AEDepartment = AEDepartment;
		this.AEUserLogin = AEUserLogin;
		this.AEUserPassword = AEUserPassword;
		this.AEMessageLanguage = AEMessageLanguage;
		this.isSSO = false;
		
		FinalPortCollection.add(AECPPort);
	}
	
	// With Port Collection instead of singular port
	public AECredentials(String AEHostnameOrIp,String PortList,int AEClientToConnect,String AEUserLogin, String AEDepartment,String AEUserPassword,char AEMessageLanguage ){
		
		this.AEHostnameOrIp = AEHostnameOrIp;
		this.AEClientToConnect = AEClientToConnect;
		this.AEDepartment = AEDepartment;
		this.AEUserLogin = AEUserLogin;
		this.AEUserPassword = AEUserPassword;
		this.AEMessageLanguage = AEMessageLanguage;
		this.isSSO = false;
		
		String[] PortsArray = PortList.replace("\\[", "").replace("\\]", "").trim().split(",");
		for(int i=0;i<PortsArray.length;i++){
			Integer myPort = Integer.parseInt(PortsArray[i]);
			this.FinalPortCollection.add(myPort);
		}
	}
	
	// for SSO
	/**
	 * @deprecated
	 * pass Port as a string or one or more ports separated by commas.
	 * ex: 2217 or 2217,2218,2219
	 */
	public AECredentials(String AEHostnameOrIp,int AECPPort,int AEClientToConnect,char AEMessageLanguage ){
		
		this.AEHostnameOrIp = AEHostnameOrIp;
		//this.AECPPort = AECPPort;
		this.AEClientToConnect = AEClientToConnect;
		this.AEDepartment = "";
		this.AEUserLogin = "";
		this.AEUserPassword = "";
		this.AEMessageLanguage = AEMessageLanguage;
		this.isSSO = true;
		FinalPortCollection.add(AECPPort);
	}
	
	// for SSO & Port collection
	public AECredentials(String AEHostnameOrIp,String PortList,int AEClientToConnect,char AEMessageLanguage ){
		
		this.AEHostnameOrIp = AEHostnameOrIp;
		this.AEClientToConnect = AEClientToConnect;
		this.AEDepartment = "";
		this.AEUserLogin = "";
		this.AEUserPassword = "";
		this.AEMessageLanguage = AEMessageLanguage;
		this.isSSO = true;
		String[] PortsArray = PortList.replace("[", "").replace("]", "").split(",");
		for(int i=0;i<PortsArray.length;i++){
			this.FinalPortCollection.add(Integer.parseInt(PortsArray[i]));
		}
	}
	
	public boolean getSSO(){
		return this.isSSO;
	}
	public void setAEHostnameOrIp(String aEHostnameOrIp) {
		AEHostnameOrIp = aEHostnameOrIp;
	}
	public void setAECPPort(int aECPPort) {
		FinalPortCollection.add(aECPPort);
	}
	
	public void setAECPPortConnection(String PortList) {
		String[] PortsArray = PortList.replace("[", "").replace("]", "").split(",");
		for(int i=0;i<PortsArray.length;i++){
			this.FinalPortCollection.add(Integer.parseInt(PortsArray[i]));
		}
	}
	
	public void setAEClientToConnect(int aEClientToConnect) {
		AEClientToConnect = aEClientToConnect;
	}
	public void setAEDepartment(String aEDepartment) {
		AEDepartment = aEDepartment;
	}
	public void setAEUserLogin(String aEUserLogin) {
		AEUserLogin = aEUserLogin;
	}
	public void setAEUserPassword(String aEUserPassword) {
		AEUserPassword = aEUserPassword;
	}
	public void setAEMessageLanguage(char aEMessageLanguage) {
		AEMessageLanguage = aEMessageLanguage;
	}
	public String getAEHostnameOrIp() {
		return AEHostnameOrIp;
	}
	public int getAECPPort() {
		return FinalPortCollection.get(0);
	}
	
	public ArrayList<Integer> getAECPPortList() {
		return FinalPortCollection;
	}
	
	public int getAEClientToConnect() {
		return AEClientToConnect;
	}
	public String getAEDepartment() {
		return AEDepartment;
	}
	public String getAEUserLogin() {
		return AEUserLogin;
	}
	public String getAEUserPassword() {
		return AEUserPassword;
	}
	public char getAEMessageLanguage() {
		return AEMessageLanguage;
	}
}
