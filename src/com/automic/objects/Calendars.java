package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.DateTime;
import com.uc4.api.UC4ObjectName;
import com.uc4.api.objects.Calendar;
import com.uc4.api.objects.StaticCalendarKeyword;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Calendars extends ObjectTemplate{

	public Calendars(Connection conn, boolean verbose) {
		super(conn, verbose);
		
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	public Calendar getCalendarFromObject(UC4Object object){return (Calendar) object;}
	public ArrayList<UC4Object> getAllCalendars() throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.CALE);
	}
	public ArrayList<UC4Object> getAllCalendarsWithFilter(String filter) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjectsWithNameFilter(ObjectTypeEnum.CALE,filter);
	}
	public void addStaticKeyword(String CalendarName, String KeywordName, int YearStart, int MonthStart, int DayStart, int YearEnd, int MonthEnd, int DayEnd) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		UC4Object obj = broker.common.openObject(CalendarName, true);
		Calendar calendar = (Calendar) obj;
		
		DateTime from = new DateTime(YearStart,MonthStart,DayStart);
		DateTime to = new DateTime(YearEnd,MonthEnd,DayEnd);
		StaticCalendarKeyword st = new StaticCalendarKeyword(new UC4ObjectName(KeywordName), from, to);
		//st.add(DateTime.nowDate());
		//st.add(DateTime.nowDate().addDays(-80));
		//st.add(DateTime.nowDate().addDays(80));
		calendar.addKeyword(st);	
		broker.common.saveAndCloseObject(calendar);
	}

}
