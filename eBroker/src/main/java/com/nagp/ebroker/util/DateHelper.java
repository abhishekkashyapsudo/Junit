package com.nagp.ebroker.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

public class DateHelper {

	/** 
	 * Constructs a new MyUtilities.
	 * @throws InstantiationException
	 */
	public DateHelper() throws InstantiationException
	{
	    throw new InstantiationException("Instances of this type are forbidden.");
	}
	
	public static boolean isValidDate() {
		return isWeekDay() && isValidTime();
	}

	private static boolean isValidTime() {
		LocalTime start =  LocalTime.parse( "09:00:00" ) ;
		LocalTime stop = LocalTime.parse("15:00:00");
		LocalTime target = LocalTime.now();

		return  target.isAfter( start ) && target.isBefore( stop )  ;
	}

	private static boolean isWeekDay() {
		LocalDate today = LocalDate.now();
		DayOfWeek day = DayOfWeek.of(today.get(ChronoField.DAY_OF_WEEK));
		return day != DayOfWeek.SUNDAY && day != DayOfWeek.SATURDAY;

	}

}
