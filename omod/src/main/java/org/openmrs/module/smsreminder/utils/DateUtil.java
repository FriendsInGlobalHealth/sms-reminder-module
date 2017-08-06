package org.openmrs.module.smsreminder.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String formatDate (Date date) {
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = sm.format(date.getTime()); 
		return strDate;  
	}
}
