package com.oil.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormaterUtil {
	private static SimpleDateFormat sf = null;
	
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static String formatDateToStr(Date date) {
		return formatDateToStr(date, DATE_FORMAT);
	}
	
	public static String formatDateToStr(Date date, String format) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(format);       
		String datestr = sDateFormat.format(date); 
		return datestr;
	}
	
	public static String getCurrentDate(String format) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(format);       
		String datestr = sDateFormat.format(new Date()); 
		return datestr;
	}
	
	/*时间戳转换成字符窜*/
    public static String getDateToString(long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }
    
    
    public static String getDateToString(long time, String format) {
        Date d = new Date(time);
        sf = new SimpleDateFormat(format);
        return sf.format(d);
    }

    
	
}
