package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.objects.Login;
import com.uc4.api.objects.LoginDefinition;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Logins extends ObjectTemplate{

	public Logins(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public Login getLoginFromObject(UC4Object object){return (Login) object;}
	public void getLoginContent(Login login) throws IOException{
		//System.out.println("Content of Login: "+myLogin.getName());
		Iterator<LoginDefinition> it = login.iterator();
		while(it.hasNext()){
			LoginDefinition def = it.next();
			String host = def.getHost().toString();
			if(host.equals("")){host="*";}
			System.out.println("Host: ["+host+"] Type: ["+def.getHostType()+"] Login: ["+def.getLoginInfo()+"]");
		}
	}
	
	public Login getLoginFromName(String LoginName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(LoginName, false);
		Login login = (Login) obj;
		return login;
	}
	
	public ArrayList<UC4Object> getAllLogins() throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.LOGIN);
	}
	public ArrayList<UC4Object> getAllLoginsWithFilter(String filter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.LOGIN,filter);
	}
}
