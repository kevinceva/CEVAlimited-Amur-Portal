package com.ceva.base.reports;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Test {

	private static String getLastDayofMonth(int month, int year)
    {
         Calendar dateCal = Calendar.getInstance();
        dateCal.set(year, month, 2);
        String pattern = "MMMM";
        SimpleDateFormat obDateFormat = new SimpleDateFormat(pattern);
        String monthName = obDateFormat.format(dateCal.getTime());
        int maxDay = dateCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return "Last date of " + monthName + " :" + maxDay+"-"+month;
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
			System.out.println(getLastDayofMonth(0,2014));
	}

}
