package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.Job;
import com.uc4.api.objects.JobPlan;
import com.uc4.api.objects.PromptElement;
import com.uc4.api.objects.PromptSet;
import com.uc4.api.objects.PromptSetDefinition;
import com.uc4.api.objects.UC4Object;
import com.uc4.api.prompt.CheckGroupElement;
import com.uc4.api.prompt.ComboElement;
import com.uc4.api.prompt.DateElement;
import com.uc4.api.prompt.LabelElement;
import com.uc4.api.prompt.NumberElement;
import com.uc4.api.prompt.OnChangeResetElement;
import com.uc4.api.prompt.RadioGroupElement;
import com.uc4.api.prompt.TextElement;
import com.uc4.communication.Connection;
import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.PreviewVariable;
import com.uc4.communication.requests.PromptInputHelp;

public class Promptsets extends ObjectTemplate{

	public Promptsets(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	/**
	 * 
	 * @deprecated
	 * Not sure how this is supposed to work? cant get it to work..
	 */
//	public void getPromptSetHelp(String VariableName, String PromptSetName, UC4Object object) throws TimeoutException, IOException{
//		ObjectBroker broker = getBrokerInstance();
//		UC4ObjectName PrptName = new UC4ObjectName("PRPT.TEST.1");
//		PromptInputHelp prpthelp = new PromptInputHelp("PCK.AUTOMIC_AMAZON.PRV.VARA.INSTANCE_TYPE", PrptName, "*", 0,object);
//		
//		broker.common.sendGenericXMLRequestAndWait(prpthelp);
//		//System.out.println("Debug:"+prpthelp.size());
//		
//	}
	
	public PromptSet getPromptSetFromObject(UC4Object object){return (PromptSet) object;}
	
	public ArrayList<UC4Object> getAllPromptsets() throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.PRPT);
	}
	
	public ArrayList<UC4Object> getAllPromptsetsWithFilter(String filter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.PRPT,filter);
	}
	
	/**
	 * 
	 * @deprecated
	 * Below Method is provided as an example of showing / manipulating the content of promptsets.
	 * It isnt meant to be used as-is.
	 */
	public void showPromptSetContent(String ObjectName) throws IOException{
		UC4Object obj = getBrokerInstance().common.openObject(ObjectName, false);
		if(obj.getType().contains("JOBP")){
			JobPlan iobj = (JobPlan) obj;
			Iterator<PromptSetDefinition> it = iobj.values().promptSetIterator();
			showPromptSetContent(it);
		}
		if(obj.getType().contains("JOBS")){
			Job iobj = (Job) obj;
			Iterator<PromptSetDefinition> it = iobj.values().promptSetIterator();
			showPromptSetContent(it);
		}else{
			// there is other objects that can have promptsets attached to them.. JSCH, etc.
		}
	}

	public void showLabelElementContent(LabelElement el){
		System.out.println("Caption:  " + el.getCaption());
		System.out.println("Custom Field:  " + el.getCustomField());
		System.out.println("Tooltip:  " + el.getTooltip());
		System.out.println("Variable Name:  " + el.getVariable());
	}
	
	public void showComboElementContent(ComboElement el){
		System.out.println("Caption:  " + el.getCaption());
		System.out.println("Custom Field:  " + el.getCustomField());
		System.out.println("Tooltip:  " + el.getTooltip());
		System.out.println("Variable Name:  " + el.getVariable());
		Iterator<OnChangeResetElement> it = el.getAllOnChangeResetElementIterator();
		while(it.hasNext()){
			OnChangeResetElement ocel = it.next();
			System.out.println("\t OnChangeReset: " + ocel.getVariable());
		}
		System.out.println("Data Reference:  " + el.getDataReference());
		System.out.println("Quote Character:  " + el.getQuoteCharacter());
		System.out.println("XML Key:  " + el.getXmlKey());
		System.out.println("XML Path:  " + el.getXPath());
		System.out.println("Item List Size:  " + el.getItemList().size());
		for(String item : el.getItemList()){System.out.println("Item: " + item);}
		System.out.println("Is Dynamic:  " + el.isDynamic());
		System.out.println("Is Locked:  " + el.isLocked());
		System.out.println("Has List:  " + el.hasList());
		
		showVaraValues(el.getDataReference().getName());
	}
	
	public void showTextElementContent(TextElement el){
		System.out.println("Caption:  " + el.getCaption());
		System.out.println("Custom Field:  " + el.getCustomField());
		System.out.println("Tooltip:  " + el.getTooltip());
		System.out.println("Variable Name:  " + el.getVariable());
		Iterator<OnChangeResetElement> it = el.getAllOnChangeResetElementIterator();
		while(it.hasNext()){
			OnChangeResetElement ocel = it.next();
			System.out.println("\t OnChangeReset: " + ocel.getVariable());
		}
		System.out.println("Data Reference:  " + el.getDataReference());
		System.out.println("Quote Character:  " + el.getQuoteCharacter());
		System.out.println("XML Key:  " + el.getXmlKey());
		System.out.println("XML Path:  " + el.getXPath());
		System.out.println("Is Locked:  " + el.isLocked());
		System.out.println("Has List:  " + el.hasList());
		System.out.println("Max Length:  " + el.getMaxLength());
		System.out.println("Multi Select Separator:  " + el.getMultiSelectSeparator());
		System.out.println("Regular Expression:  " + el.getRegExp());
		showVaraValues(el.getDataReference().getName());
	}
	
	public void showNumberElementContent(NumberElement el){
		System.out.println("Caption:  " + el.getCaption());
		System.out.println("Custom Field:  " + el.getCustomField());
		System.out.println("Tooltip:  " + el.getTooltip());
		System.out.println("Variable Name:  " + el.getVariable());
		System.out.println("Data Reference:  " + el.getDataReference());
		System.out.println("Quote Character:  " + el.getQuoteCharacter());
		System.out.println("Is Locked:  " + el.isLocked());
		System.out.println("Has List:  " + el.hasList());
		System.out.println("Minimum Value:  " + el.getMinValue());
		System.out.println("Maximum Value:  " + el.getMaxValue());
		showVaraValues(el.getDataReference().getName());
	}
	
	public void showCheckGroupElementContent(CheckGroupElement el){
		System.out.println("Caption:  " + el.getCaption());
		System.out.println("Custom Field:  " + el.getCustomField());
		System.out.println("Tooltip:  " + el.getTooltip());
		System.out.println("Variable Name:  " + el.getVariable());
		Iterator<OnChangeResetElement> it = el.getAllOnChangeResetElementIterator();
		while(it.hasNext()){
			OnChangeResetElement ocel = it.next();
			System.out.println("\t OnChangeReset: " + ocel.getVariable());
		}
		System.out.println("Data Reference:  " + el.getDataReference());
		System.out.println("Quote Character:  " + el.getQuoteCharacter());
		System.out.println("XML Key:  " + el.getXmlKey());
		System.out.println("XML Path:  " + el.getXPath());
		System.out.println("Item List Size:  " + el.getItemList().size());
		for(String item : el.getItemList()){System.out.println("Item: " + item);}
		System.out.println("Is Locked:  " + el.isLocked());
		System.out.println("Has List:  " + el.hasList());
		System.out.println("Multi Select Separator:  " + el.getMultiSelectSeparator());
		showVaraValues(el.getDataReference().getName());
	}
	
	public void showRadioGroupElementContent(RadioGroupElement el){
		System.out.println("Caption:  " + el.getCaption());
		System.out.println("Custom Field:  " + el.getCustomField());
		System.out.println("Tooltip:  " + el.getTooltip());
		System.out.println("Variable Name:  " + el.getVariable());
		System.out.println("Data Reference:  " + el.getDataReference());
		System.out.println("Quote Character:  " + el.getQuoteCharacter());
		System.out.println("XML Key:  " + el.getXmlKey());
		System.out.println("XML Path:  " + el.getXPath());
		System.out.println("Item List Size:  " + el.getItemList().size());
		for(String item : el.getItemList()){System.out.println("Item: " + item);}
		System.out.println("Is Locked:  " + el.isLocked());
		System.out.println("Has List:  " + el.hasList());
		showVaraValues(el.getDataReference().getName());
	}
	
	
	public void showDateElementContent(DateElement el){
		System.out.println("Caption:  " + el.getCaption());
		System.out.println("Custom Field:  " + el.getCustomField());
		System.out.println("Tooltip:  " + el.getTooltip());
		System.out.println("Variable Name:  " + el.getVariable());
		System.out.println("Data Reference:  " + el.getDataReference());
		System.out.println("Is Locked:  " + el.isLocked());
		System.out.println("Has List:  " + el.hasList());
		System.out.println("Minimum Value:  " + el.getMinValue());
		System.out.println("Maximum Value:  " + el.getMaxValue());
		System.out.println("Calendar Condition:  [" + el.getCalendarCondition().getName().getName()+"|" + el.getCalendarCondition().getKeywordAsString()+"]");
		System.out.println("Output Format:  " + el.getOutputFormat());
	}
	
	private void showPromptSetContent(Iterator<PromptSetDefinition> it) throws IOException{
		while(it.hasNext()){
			PromptSetDefinition def = it.next();
			// Current Values / keys
			String[] ElIDs = def.getElementIDs();
			for(String s : ElIDs){
				System.out.println("[Variable Name | Variable Value]: ["+s +"|"+def.getValue(s)+"]");
			}
			
			// Prompt Definition
			PromptSet prpt = (PromptSet) getBrokerInstance().common.openObject(def.getName().getName(), true);
			Iterator<PromptElement> itEl = prpt.designer().iterator();
			while(itEl.hasNext()){
				PromptElement PrptEl = itEl.next();
				String ClassType = PrptEl.getClass().getName().replace("com.uc4.api.prompt.", "");
				System.out.println("\nElement Type: " + ClassType);
				
				if(ClassType.contains("LabelElement")){showLabelElementContent((LabelElement) PrptEl);}
				
				if(ClassType.contains("ComboElement")){showComboElementContent((ComboElement) PrptEl);}
				
				if(ClassType.contains("TextElement")){showTextElementContent((TextElement) PrptEl);}
				
				if(ClassType.contains("NumberElement")){showNumberElementContent((NumberElement) PrptEl);}
				
				if(ClassType.contains("RadioGroupElement")){showRadioGroupElementContent((RadioGroupElement) PrptEl);}
				
				if(ClassType.contains("CheckGroupElement")){showCheckGroupElementContent((CheckGroupElement) PrptEl);}
				
				if(ClassType.contains("DateElement")){showDateElementContent((DateElement) PrptEl);}
			}

		}
	}
	
	private void showVaraValues(String VaraName){
		ObjectBroker broker = getBrokerInstance();
		
		try{
			ArrayList<String> Values = broker.variables.getPossibleVaraValues(VaraName, 1);
			System.out.println("Possible Values: "+Arrays.toString(Values.toArray()));
		}catch (TimeoutException t){}
		catch(IOException i){}	
	}
}
