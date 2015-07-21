package com.automic.utils;

import com.uc4.api.Template;

public class Utils {

	// List is incomplete.. add as necessary!!
	public static Template convertStringToTemplate(String s){
		if(s.equals("JOBS.WIN")){return Template.JOBS_WIN;}
		if(s.equals("JOBS.UNIX")){return Template.JOBS_UNIX;}
		if(s.equals("JOBS.SQL")){return Template.JOBS_SQL;}
		if(s.equals("FOLD")){return Template.FOLD;}
		if(s.equals("USER")){return Template.USER;}

		return null;
	}
	
}
