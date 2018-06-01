package com.kal.cms.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	  public static Calendar getCalendar()
	  {
	    return Calendar.getInstance();
	  }

	  public static Calendar getCalendar(String day, String format)
	  {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(getDate(day, format));
	    return cal;
	  }

	  public static Date getDate(String str, String dateFormat)
	  {
	    Date date = null;
	    try
	    {
	      if ((str == null) || (str.length() != dateFormat.length())) {
	        return date;
	      }

	      SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	      sdf.setLenient(false);
	      try
	      {
	        date = sdf.parse(str);
	      } catch (ParseException pe) {
	        date = new Date();
	      }
	    } catch (Exception e) {
	      date = new Date();
	    }

	    return date;
	  }

	  public static Date getDate()
	  {
	    return new Date();
	  }

	  public static String getDate2String(Date date, String format)
	  {
	    if (date != null) {
	      DateFormat df = new SimpleDateFormat(format, Locale.ENGLISH);
	      return df.format(date);
	    }

	    return null;
	  }
	  public static String getDate2String(Date date, String format, Locale locale) {
	    if (date != null) {
	      DateFormat df = new SimpleDateFormat(format, locale);
	      return df.format(date);
	    }

	    return null;
	  }

	  public static String getToday(String format, Locale locale)
	  {
	    return getDate2String(getDate(), format, locale);
	  }
	  public static String getToday(String format) {
	    return getDate2String(getDate(), format);
	  }
	
}
