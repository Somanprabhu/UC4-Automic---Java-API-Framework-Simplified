package com.automic.objects;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.Template;
import com.uc4.api.UC4HostName;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.BackendCommand;
import com.uc4.api.objects.BackendVariable;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.UC4Object;
import com.uc4.api.objects.Variable;
import com.uc4.communication.Connection;

public class Variables extends ObjectTemplate{

	public Variables(Connection conn, boolean verbose) {
		super(conn, verbose);	
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	public Variable getJobFromObject(UC4Object object){return (Variable) object;}
	
	public void createStaticVariable(String VariableName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(VariableName, Template.VARA, FolderLocation);
	}
	public void createBackendVariable(String VariableName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(VariableName, Template.VARA_BACKEND, FolderLocation);
	}
	public void createFilelistVariable(String VariableName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(VariableName, Template.VARA_FILELIST, FolderLocation);
	}
	public void createMultiVariable(String VariableName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(VariableName, Template.VARA_MULTI, FolderLocation);
	}
	public void createSecSQLVariable(String VariableName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(VariableName, Template.VARA_SEC_SQL, FolderLocation);
	}
	public void createSecSQLIVariable(String VariableName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(VariableName, Template.VARA_SEC_SQLI, FolderLocation);
	}
	public void createSQLVariable(String VariableName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(VariableName, Template.VARA_SQL, FolderLocation);
	}
	public void createSQLIVariable(String VariableName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(VariableName, Template.VARA_SQLI, FolderLocation);
	}

	public ArrayList<UC4Object> getAllVariables() throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.VARA);
	}
	public ArrayList<UC4Object> getAllVariablesWithFilter(String filter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.VARA,filter);
	}
	public Variable getVariableFromName(String VarName) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		Variable Var = (Variable) broker.common.openObject(VarName, false);
		return Var;
	}
	public void updateValueInVariable(Variable var, String key, String value, boolean keepABackup) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		if(keepABackup){
			String timeStamp = new SimpleDateFormat("yyyyMMdd.HHmm").format(new Date());
			var.add(timeStamp+"_"+key, var.get(key));
		}
		var.remove(key);
		var.add(key,value);
		broker.common.saveAndCloseObject(var);				
	}
	public void ShowVariableContent(Variable var){
		Iterator<String> it = var.keyIterator();
		System.out.println(" ++ Content for Variable: "+var.getName());
		while(it.hasNext()){
			String key = it.next();
			System.out.println("  => " +key+" : "+var.get(key));
		}
	}
	public void createBackendVariable(String Name, IFolder folder, String HostAgent, String Login, String Result, 
			ArrayList<BackendCommand> windowsCommands,  ArrayList<BackendCommand> unixCommands) throws IOException {
		
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(Name, Template.VARA_BACKEND, folder);
		Variable vara = (Variable) broker.common.openObject(Name,true); 
		BackendVariable backend = vara.backend();	
		
		Say(" %% Setting backend info ... ");
		
		backend.setHost(new UC4HostName(HostAgent));
		backend.setLogin(new UC4ObjectName(Login));
		backend.setResultFormat(Result); // ex: "{1}"
		
		backend.setApplyUserLogin(true);
		
		//BackendCommand winCommand1 = new BackendCommand("WinNT", "6.1", "powershell.exe dir", "res:1(20);col:2(8)");
		//BackendCommand unixCommand1 = new BackendCommand("Suse", "7.3", "ls -s", "ls:1(12)");
		
		for(BackendCommand cmd : windowsCommands){
			backend.addWindowsCommand(cmd);
		}
		for(BackendCommand cmd : unixCommands){
			backend.addUnixCommand(cmd);
		}

		System.out.println(" ++ Backend Variable Created");
		
		broker.common.saveAndCloseObject(vara);				
		
	}
	public void addWinCommandToBackendVariable(String Variablename, BackendCommand WinCmd) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		Variable vara = (Variable) broker.common.openObject(Variablename, true);
		BackendVariable backend = vara.backend();	
		backend.addWindowsCommand(WinCmd);
		broker.common.saveAndCloseObject(vara);				
	}
	public void addUnixCommandToBackendVariable(String Variablename, BackendCommand WinCmd) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		Variable vara = (Variable) broker.common.openObject(Variablename, true);
		BackendVariable backend = vara.backend();	
		backend.addUnixCommand(WinCmd);
		broker.common.saveAndCloseObject(vara);				
	}
	
	public String getVariableType(Variable obj){
		// THIS is the only way to check the type of Variable.. (v11.2 and before) 
		// it is very ugly but it works :)
		if(obj.backend()!=null){return "BACKEND";}
		if(obj.composite()!=null){return "MULTI";}
		if(obj.dynamicExecVariable()!=null){return "EXEC";}
		if(obj.fileList()!=null){return "FILELIST";}
		if(obj.sql()!=null){return "SQL";}
		if(obj.sqlIntern()!=null){return "SQLI";}
		if(obj.sqlInternSecure()!=null){return "SEC_SQLI";}
		if(obj.sqlSecure()!=null){return "SEC_SQL";}
		//if(obj.!=null){return "";}// XML?
		
		return "STATIC";
	//	boolean IsBackend = (obj.backend()!=null);
	//	boolean IsComposite = (obj.composite()!=null);
	//	boolean IsDynamicExec = (obj.dynamicExecVariable()!=null);
	//	boolean IsFList = (obj.fileList()!=null);
	//	boolean IsSQL = (obj.sql()!=null);
	//	boolean IsSQLI = (obj.sqlIntern()!=null);
	//	boolean IsSECSQLI = (obj.sqlInternSecure()!=null);
	//	boolean IsSECSQL = (obj.sqlSecure()!=null);
	//	boolean IsStatic = (IsBackend == false && IsComposite == false && IsDynamicExec == false
	//			&& IsFList == false && IsSQL == false && IsSQLI == false && IsSECSQLI == false
	//			&& IsSECSQL == false);
	}
	
}
