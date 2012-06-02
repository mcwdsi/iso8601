 /* Copyright 2011 University of Arkansas for Medical Sciences
  *
  *   Licensed under the Apache License, Version 2.0 (the "License");
  *   you may not use this file except in compliance with the License.
  *   You may obtain a copy of the License at
  *
  *       http://www.apache.org/licenses/LICENSE-2.0
  *
  *   Unless required by applicable law or agreed to in writing, software
  *   distributed under the License is distributed on an "AS IS" BASIS,
  *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  *   See the License for the specific language governing permissions and
  *   limitations under the License.
  */
package edu.uams.dbmi.util.iso8601;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.uams.dbmi.util.iso8601.InconsistentIso8601DateException;

/**
 * Class for representing dates according to ISO8601.
 * 
 * It can represent a century, year, month, week, or day, according
 * 	to the Gregorian calendar.  Examples include 19 (for century beginning
 * 	with 1900 and ending with 1999), 1985, 1985-04, 1985-W15, and 1985-04-12,
 * 	respectively.
 * 
 * You can determine the duration of the interval by calling the isCentury(),
 * 	isYear(), isMonth(), isWeek(), isDay() methods.
 *  
 * You can configure days in one of three ways: year/month/day, 
 * 	year/day of year, or year/week of year/day of week. The object will
 * 	compute the other configurations for you (so if you configure using
 * 	year/month/day, you can call getDayOfYear() which will be computed
 * 	for you and returned.  Similarly for getWeekOfYear() and getDayOfWeek()).
 * 
 * The object is backed by an instance of java.util.GregorianCalendar which
 * 	you can obtain by calling getCalendarForDay() (for days) or 
 * 	getCalendarForFirstDayInInterval() (for weeks, months, years, and
 * 	centuries, which are longer than days, so we arbitrarily return a 
 * 	GregorianCalendar set to the first day in the interval).  The time and 
 * 	time zone of the GregorianCalendar instance are meaningless.
 *  
 * @author williamhogan
 *
 */
public class Iso8601Date {
	int century;
	int year;
	int yearInt;
	int month;
	int dayOfMonth;
	int weekOfYear;
	int dayOfWeek;
	int dayOfYear;
	
	int weekCalendarYear; //for cases where weeks 52 and 53 span New Year's Day
	
	boolean isDay = false;
	boolean isWeek = false;
	boolean isMonth = false;
	boolean isYear = false;
	boolean isCentury = false;
	
	DateConfiguration configuration;
	GregorianCalendar calendar;
	
	public enum DateConfiguration {
		CENTURY,
		YEAR,
		YEAR_MONTH,
		YEAR_MONTH_DAY,
		YEAR_WEEK,
		YEAR_WEEK_DAY,
		YEAR_DAY_OF_YEAR;
	}

	/**
	 *  Convenience constructor for getting the current date, per default
	 *  	locale.
	 *  
	 *  It will be a day-long interval (isDay() == true).
	 *
	 */
	public Iso8601Date() {
		isDay = true;
		calendar = (GregorianCalendar)GregorianCalendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek(4);
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		configuration = DateConfiguration.YEAR_MONTH_DAY;
		setWeekCalendarYear();
	}

	/**
	 * Create an ISO8601 date object of the given configuration that takes
	 * 	one parameters (in particular, CENTURY and YEAR).
	 * 
	 * @param config The date configuration that says what the parameter
	 * 				represents.
	 * @param param The parameter
	 */
	public Iso8601Date(DateConfiguration config, int param) {
		if (config.equals(DateConfiguration.YEAR)) {
			setYear(param);
			isYear = true;
		} else if (config.equals(DateConfiguration.CENTURY)) {
			setCentury(param);
			isCentury = true;
		} else {
			throw new IllegalArgumentException("The legal, single-parameter " +
					"configurations are YEAR and CENTURY. (config=" + 
					config + ")");
		}
		initialize(config);
	}
	
	private void initialize(DateConfiguration config) {
		configuration = config;
		checkConsistency();
		setupCalendar();
	}

	/*
	 * In this method, we create a GregorianCalendar instance that corresponds
	 * 	to either (1) the day represented (if it's specific to the day) or 
	 *  (2) the first day of the interval represented (if it's longer than 
	 *  one day, such as week, month, year, century).
	 */
	private void setupCalendar() {
		calendar = (GregorianCalendar)GregorianCalendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek(4);
		switch (configuration) {
			/*
		 	 * If we have century or year, set calendar to Jan 1 of the year 
		 	 *  (or the first year in the century).
		 	 */
			case CENTURY:
			case YEAR:
				calendar.set(year, 0, 1);
				break;
			/*
			 * If we have a month, set calendar to first day of the month.
			 */
			case YEAR_MONTH:
				/* 
				 * It is (month-1) because the Calendar class and its
				 *  derivatives use Jan==0, Feb==1, ... Dec==11. 
				 */
				calendar.set(year, month-1, 1);
				break;
			case YEAR_MONTH_DAY:
				/* 
				 * It is (month-1) because the Calendar class and its
				 *  derivatives use Jan==0, Feb==1, ... Dec==11. 
				 */
				calendar.set(year, month-1, dayOfMonth);
				setWeekCalendarYear();
				break;
			/*
			 * If we have a day of the year, then it's easy: just set 
			 * 	Calendar as you would normally otherwise.
			 */
			case YEAR_DAY_OF_YEAR:
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
				setWeekCalendarYear();
				break;
			/*
			 * If we have a week, set calendar to the first day of that week.
			 */
			case YEAR_WEEK:
				initializeCalendar(year, weekOfYear, 1);
				break;
			/*
			 * Again, this case is easy: set calendar to specified week and 
			 *   day, noting that for ISO 8601, first day of week is Monday
			 *   and there are at least 4 days from the first week of the 
			 *   year within the current year.
			 */
			case YEAR_WEEK_DAY:
				initializeCalendar(year, weekOfYear, dayOfWeek);
				break;
			default:
				System.err.println("Unrecognized DateConfiguration: " + 
						configuration);
		}
	}

	private void setWeekCalendarYear() {
		int mo = calendar.get(Calendar.MONTH) + 1;
		int we = calendar.get(Calendar.WEEK_OF_YEAR);
		/*
		 * If we're in December, but we're being told it's week 1 of the year,
		 * 	then the calendar year is one less than the week calendar year.
		 * 
		 * On the other hand, if we're in January, but we're being told that it
		 * 	is week 52 or 53 of the year, then the calendar year is one greater
		 * 	than the week calendar year.
		 * 
		 * Otherwise, the two are the same.
		 */
		if (mo == 12 && we == 1) {
			weekCalendarYear = year + 1;
		} else if (month == 1 && we > 51) {
			weekCalendarYear = year - 1;
		} else {
			weekCalendarYear = year;
		}
		
	}

	/*
	 * Method that initializes a java.util.GregorianCalendar object that
	 * 	coincides with this ISO8601 date object.  We also use it to help
	 * 	interconvert among various equivalent representations of 
	 * 	particular days.
	 */
	private void initializeCalendar(int yr, int wOfY, int dOfW) {
		calendar.set(Calendar.YEAR, yr);
		calendar.set(Calendar.WEEK_OF_YEAR, wOfY);
		int cDOfW = -1;
		cDOfW = convertIsoDayOfWeekToCalendar(dOfW);
		calendar.set(Calendar.DAY_OF_WEEK, cDOfW);	
		
		weekCalendarYear = yr;
		if (yr != calendar.get(Calendar.YEAR)) {
			//System.out.println(yr + "\t" + calendar.get(Calendar.YEAR));
			year = calendar.get(Calendar.YEAR);
		} else {
			year = yr;
		}
	}

	private void setCentury(int param) {
		this. century = param;
		setYear(this .century * 100);
	}

	private void setYear(int param) {
		this .year = param;
	}
	
	/**
	 * Create an ISO8601 date object of the given configuration that takes
	 * 	two parameters (in particular, YEAR_WEEK, YEAR_DAY_OF_YEAR, and 
	 * 	YEAR_MONTH.
	 * 
	 * @param config  The DateConfiguration that indicates what the two 
	 * 	paramters represent.
	 * @param param1  The first parameter per the DateConfiguration
	 * @param param2  The second parameter per the DateConfiguration
	 */
	public Iso8601Date(DateConfiguration config, int param1, int param2) {
		if (config.equals(DateConfiguration.YEAR_MONTH)) {
			setYear(param1);
			setMonth(param2);
			isMonth = true;
		} else if (config.equals(DateConfiguration.YEAR_WEEK)) {
			setYear(param1);
			setWeek(param2);
			isWeek = true;
		} else if (config.equals(DateConfiguration.YEAR_DAY_OF_YEAR)) {
			setYear(param1);
			setDayOfYear(param2);
			isDay = true;
		} else {
			throw new IllegalArgumentException("The legal, two-parameter " +
					"configurations are YEAR_MONTH, YEAR_WEEK, and " +
					"YEAR_DAY_OF_YEAR. (config=" + config + ")");
		}
		initialize(config);
	}

	private void setMonth(int month) {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("Month must be 1 through 12, " +
					"inclusive.");
		}
		this. month = month;
	}

	private void setWeek(int week) {
		if (week < 1 || week > 53) {
			throw new IllegalArgumentException("Week must be 1 through 12, " +
			"inclusive.");			
		}
		this. weekOfYear = week;
	}

	private void setDayOfYear(int day) {
		if (day < 1 || day > 366) {
			throw new IllegalArgumentException("Day of year must be 1 " +
					"through 366, inclusive.");
		}
		this. dayOfYear = day;
	}

	/**
	 * Create an ISO8601 date object of the given configuration that takes
	 * 	three parameters (in particular, YEAR_WEEK_DAY and 
	 * 	YEAR_MONTH_DAY.
	 * 
	 * @param config  The DateConfiguration that indicates what the two 
	 * 	paramters represent.
	 * @param param1  The first parameter per the DateConfiguration
	 * @param param2  The second parameter per the DateConfiguration
	 * @param param3  The third parameter per the DateConfiguration
	 */
	public Iso8601Date(DateConfiguration config, int param1, int param2, int param3) {
		if (config.equals(DateConfiguration.YEAR_MONTH_DAY)) {
			setYear(param1);
			setMonth(param2);
			setDayOfMonth(param3);
			isDay = true;
		} else if (config.equals(DateConfiguration.YEAR_WEEK_DAY)) {
			setYear(param1);
			setWeek(param2);
			setDayOfWeek(param3);
			isDay = true;
		} else {
			throw new IllegalArgumentException("The legal, three-parameter " +
					"configurations are YEAR_MONTH_DAY and " +
					"YEAR_WEEK_DAY. (config=" + config + ")");			
		}
		initialize(config);
	}

	private void setDayOfMonth(int day) {
		if (day < 1 || day > 31) {
			throw new IllegalArgumentException("Day of month must be 1 " +
			"through 31, inclusive.");
		}
		this. dayOfMonth = day;
	}

	private void setDayOfWeek(int day) {
		if (day < 1 || day > 7) {
			throw new IllegalArgumentException("Day of month must be 1 " +
			"through 7, inclusive.");
		}
		this. dayOfWeek = day;	
	}	
	
	private void checkConsistency() {
		switch (configuration) {
			case CENTURY:
			case YEAR:
			case YEAR_MONTH:
				break;
			case YEAR_MONTH_DAY:
				checkYearMonthDayConsistency();
				break;
			case YEAR_WEEK:
			case YEAR_WEEK_DAY:
				checkYearWeekConsistency();
				break;
			case YEAR_DAY_OF_YEAR:
				checkYearDayOfYearConsistency();
				break;
			default:
				System.err.println("Unhandled enum: " + configuration);	
		}
	}

	private void checkYearMonthDayConsistency() {
		/*
		 * First, make sure day of month is consistent with month.  If month is
		 *  February and day == 29, then check to see if it's a leap year.
		 *  
		 *  If yes, consistent, if no, not consistent.
		 */
		if (dayOfMonth == 31 && !is31DayMonth(month)) {
				throw new InconsistentIso8601DateException("Month " + month + 
						" does not have 31 days.");
		}
	}

	public static boolean is31DayMonth(int month) {
		/*
		 * Instead of comparing with each value, we can flip the even 
		 *   months with 31 days (8, 10, 12) to odd days (XOR 15), then 
		 *   bitwise-AND to look for odd number.
		 *  
		 * It reduces 7 comparisons to: 2 comparisons, a bitwise-AND, 
		 *   and in the case of August, October, and December, a 
		 *   bitwise-XOR.  NICE!  
		 */
		int monthCheck = (month>7) ? (month^15) : month;
		return ((monthCheck&1) == 1);
	}

	private void checkYearWeekConsistency() {
		/*
		 * If week <= 52, OK.  If week == 53, then we need to see if it's a
		 * 	different week than week 1 of the following year.
		 */
		if (weekOfYear == 53 && !is53WeekYear(year)) {
			throw new InconsistentIso8601DateException("Year " + year + 
				" does not have 53 weeks.");
		}
	}

	private void checkYearDayOfYearConsistency() {
		/*
		 * If day <= 365, OK.  If day == 366, then we just need to see if it's
		 *   a leap year.  If yes, consistent, if no, not consistent.
		 */
		if (dayOfYear == 366 && !isLeapYear(year)) {
			throw new InconsistentIso8601DateException("Year " + year +
					" is not a leap year, but day of year = " + dayOfYear);
		}
	}
	
	/**
	 * 
	 * @param year
	 * @return true if year is a leap year, false otherwise.
	 */
	public static boolean isLeapYear(int year) {
		return ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0) 
				|| year == 0);
	}
	
	/**
	 * 
	 * @param year
	 * @return true if year has 53 weeks according to ISO 8601, false 
	 * 			otherwise.
	 */
	public static boolean is53WeekYear(int year) {
		/*
		 * With thanks to http://www.staff.science.uu.nl/~gent0113/calendar/isocalendar_text3.htm
		 *		for this algorithm
		 */
		boolean has53;

		int py = year + year/4 - year/100 + year/400;
		has53 = (py % 7 == 4);
		if (!has53) {
			year--;
			py = year + year/4 -year/100 + year/400;
			has53 = (py % 7 == 3);
		}
		
		return has53;
	}
	
	/**
	 * 
	 * @param year
	 * @return A GregorianCalendar set to week 1, day 1, as defined by ISO
	 *  		8601, of the given year.
	 */
	public static GregorianCalendar getIsoWeekOneDayOneForYear(int year) {
		/*
		 * The first ISO week of the year always includes January 4th, per the
		 *   standard itself.
		 *   
		 *   I have verified that the following method, as discussed in the 
		 *   javadoc for java.util.Calendar, is completely equivalent to the 
		 *   January 4th method.
		 */
		GregorianCalendar c = (GregorianCalendar)GregorianCalendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(4);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.WEEK_OF_YEAR, 1);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return c;
	}
	
	/**
	 * 
	 * @return A GregorianCalendar that represents the first day of the week,
	 * 			month, or year represented by this Iso8601Date object.  If
	 * 			this object represents a single day, this method throws an
	 * 			IllegalStateException, which can be avoided by calling the 
	 * 			isDay() method to check first.
	 */
	public GregorianCalendar getCalendarForFirstDayInInterval() {
		if (isDay) {
			throw new IllegalStateException("This date object does not " +
					"represent a multi-day interval, so \"first day\" has " +
					"no meaning. Use \"get day\" instead.");
		}
		return calendar;
	}
	
	/**
	 * 
	 * @return A GregorianCalendar that represents the day represented by 
	 * 			this Iso8601Date object.  If this object represents an 
	 * 			interval longer than a single day, this method throws an
	 * 			IllegalStateException, which can be avoided by calling the 
	 * 			isDay() method to check first.
	 */
	public GregorianCalendar getCalendarForDay() {
		if (!isDay) {
			throw new IllegalStateException("This date object does not " +
					"represent a single-day interval, so \"get day\" has " +
					"no meaning.  Use \"get first day\" instead.");
		}
		return calendar;		
	}
	
	/**
	 * 
	 * @return true if this object represents a day interval, false otherwise.
	 */
	public boolean isDay() {
		return isDay;
	}
	
	/**
	 * 
	 * @return true if this object represents a week interval, false 
	 * 			otherwise.
	 */
	public boolean isWeek() {
		return isWeek;
	}
	
	/**
	 * 
	 * @return true if this object represents a month interval, false 
	 * 			otherwise.
	 */
	public boolean isMonth() {
		return isMonth;
	}
	
	/**
	 * 
	 * @return true if this object represents a year interval, false 
	 * 			otherwise.
	 */
	public boolean isYear() {
		return isYear;
	}
	
	/**
	 * 
	 * @return true if this object represents a century interval, false 
	 * 			otherwise.
	 */
	public boolean isCentury() {
		return isCentury;
	}
	
	public int getCentury() {
		if (this .century == 0) {
			this .century = year/100;
		}
		return this .century;
	}
	
	/**
	 * The year involved in this interval.  If the date was specified
	 * 		as week 1, day 1 (or 2...) of a particular year, then the
	 * 		year of the calendar day might actually be the previous year.
	 * 
	 * 		For example, 2008-W01-1 is Dec 31, 2007.  This method will
	 * 		return 2008 as the year - the parameter with which this object
	 * 		was initialized.
	 * 
	 * 		If the actual calendar year of the day is required, then use the
	 * 		getCalendarForFirstDayInInterval() or getCalendarForDay() methods
	 * 		to get the actual calendar year of the interval.
	 * 
	 * @return 
	 */
	public int getYear() {
		if (configuration.equals(DateConfiguration.CENTURY)) {
			throw new IllegalStateException("This Iso8601Date object " +
					"represents an entire century and not a specific year.");
		}
		return year;
	}
	
	public int getWeekCalendarYear() {
		if (configuration.equals(DateConfiguration.CENTURY)) {
			throw new IllegalStateException("This Iso8601Date object " +
			"represents an entire century and not a specific year.");
		}
		return weekCalendarYear;
	}
	
	/**
	 * This method returns the month of year for YEAR_WEEK_DAY,
	 * 	YEAR_DAY_OF_YEAR, YEAR_MONTH, and YEAR_MONTH_DAY
	 * 	configurations, and throws an exception otherwise.
	 * 
	 * 	The YEAR_WEEK configuration represents a week that can span months
	 * 		and even years (last few days of one, first few of next), so
	 * 		asking for the month is not meaningful.
	 * 
	 * @return The month of the year, where Jan==1, Feb==2, ..., Dec==12
	 */
	public int getMonth() {
		if (configuration.equals(DateConfiguration.CENTURY) ||
				configuration.equals(DateConfiguration.YEAR)) {
			throw new IllegalStateException("This Iso8601Date object " +
					"represents an interval longer than a month.");
		} else if (configuration.equals(DateConfiguration.YEAR_WEEK)) {
			/*
			 * What if week spans end of one month and beginning of next?
			 *   We could:
			 *   	1. Return Month at beginning of week
			 *      2. Return Month at end of week
			 *      3. Throw an IllegalStateException
			 *      4. Return month that has 4+ days of week
			 *      
			 *      I'm leaning towards #3.  The representation is an 
			 *      arbitrary week that does not have to be contained
			 *      completely inside a particular month (or even year).
			 */
			throw new IllegalStateException("This Iso8601Date object " +
					"represents a type of week that can span months and " +
					"even the " +
					"change from one year to another.");
		} else if (configuration.equals(DateConfiguration.YEAR_WEEK_DAY) ||
				configuration.equals(DateConfiguration.YEAR_DAY_OF_YEAR)) {
			month = calendar.get(Calendar.MONTH) + 1;
		}
		return month;
	}
	
	/**
	 * For representations of days, get the day of the month.  If this object
	 * 	was initialized as YEAR_WEEK_DAY or YEAR_DAY_OF_YEAR, then this value
	 *  is computed using java.util.GregorianCalendar.
	 *  
	 * @return  The day of the month
	 */
	public int getDayOfMonth() {
		if (!isDay) {
			throw new IllegalStateException("This Iso8601 object does not " +
					"represent a day.");
		}
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * For representations of days, get the day of the week.  If this object
	 * 	was initialized as YEAR_MONTH_DAY or YEAR_DAY_OF_YEAR, then this value
	 *  is computed using java.util.GregorianCalendar, and converted to the 
	 *  ISO 8601 integer value for days of the week (Mon==1, ..., Sun==7).
	 *  
	 * @return The day of the week
	 */
	public int getDayOfWeek() {
		if (!isDay) {
			throw new IllegalStateException("This Iso8601 object does not " +
					"represent a day.");
		}
		return convertCalendarDayOfWeekToIso(
				calendar.get(Calendar.DAY_OF_WEEK));
	}
	
	/**
	 * 	For representations of days, get the day of the year.  If this object
	 * 	was initialized as YEAR_MONTH_DAY or YEAR_WEEK_DAY, then this value
	 *  is computed using java.util.GregorianCalendar.  
	 *  
	 *  Note that for week 1 of the year, some days actually occur in the 
	 *  previous calendar year. For example 2008-W01-1 is Dec 31st, 2007. So,
	 *  the day of year for this date is 365, not day 1. 
	 *  
	 * @return The day of the year.
	 */
	public int getDayOfYear() {
		if (!isDay) {
			throw new IllegalStateException("This Iso8601 object does not " +
					"represent a day.");
		}		
		return calendar.get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * ISO 8601 defines Mon==1, Tue==2, etc. But java.util.Calendar has Mon==2
	 * 	and could even change in the future (hypothetically).  So, we'll take
	 *  an integer from range 1-7 inclusive and convert those values to ISO day
	 *  based on whatever integer values Calendar.MONDAY, etc. have (so long as
	 *  they're 1 through 7 inclusive).
	 * 
	 * @param cDay
	 * @return The ISO 8601 integer value for the day of the week
	 */
	public static int convertCalendarDayOfWeekToIso(int cDay) {
		int isoDay;
		switch (cDay) {
			case Calendar.MONDAY:
				isoDay = 1;
				break;
			case Calendar.TUESDAY:
				isoDay = 2;
				break;
			case Calendar.WEDNESDAY:
				isoDay = 3;
				break;
			case Calendar.THURSDAY:
				isoDay = 4;
				break;
			case Calendar.FRIDAY:
				isoDay = 5;
				break;
			case Calendar.SATURDAY:
				isoDay = 6;
				break;
			case Calendar.SUNDAY:
				isoDay = 7;
				break;
			default:
				throw new IllegalArgumentException("Day of week must be " +
				"integer between 1 and 7 inclusive.");
		}	
		return isoDay;
	}
	
	/**
	 * ISO 8601 defines Mon==1, Tue==2, etc. But java.util.Calendar has Mon==2
	 * 	and could even change in the future (hypothetically).  So, we'll take
	 *  an integer from range 1-7 inclusive and convert those values to 
	 *  java.util.Calender integer values for day of week.
	 * 
	 * @param cDay
	 * @return The java.util.Calendar integer value for the day of the week
	 */
	public static int convertIsoDayOfWeekToCalendar(int isoDay) {
		int cDOfW = -1;
		/*
		 * per ISO, day of week is numbered 1 to 7 with 1==Mon, 2==Tue, etc.
		 * 
		 * But the integer values for Calendar class are different.  So we
		 *   need to translate here. (2==Mon, etc). But just to be safe, if 
		 *   for some insane reason the integer values of Calendar change,
		 *   we protect ourselves by doing switch/case instead of just
		 *   subtracting 1.
		 */
		switch (isoDay) {
			case 1: 
				cDOfW = Calendar.MONDAY;
				break;
			case 2:
				cDOfW = Calendar.TUESDAY;
				break;
			case 3:
				cDOfW = Calendar.WEDNESDAY;
				break;
			case 4: 
				cDOfW = Calendar.THURSDAY;
				break;
			case 5: 
				cDOfW = Calendar.FRIDAY;
				break;
			case 6:
				cDOfW = Calendar.SATURDAY;
				break;
			case 7:
				cDOfW = Calendar.SUNDAY;
				break;
			default:
				throw new IllegalArgumentException("Day of week must be " +
						"integer between 1 and 7 inclusive.");
		}
		return cDOfW;
	}

	public int getWeekOfYear() {
		int wOfY;
		if (isDay) {
			wOfY = calendar.get(Calendar.WEEK_OF_YEAR);
		} else if (isWeek) {
			wOfY = weekOfYear;			
		} else {
			throw new IllegalStateException("This Iso8601Date object " +
					"represents an interval longer than a week");
		}
		return wOfY;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o instanceof Iso8601Date) {
			Iso8601Date d = (Iso8601Date)o;
			eq = centuryEquals(d) || yearEquals(d) || monthEquals(d) ||
				weekEquals(d) || dayEquals(d);
		}
		return eq;
	}
	
	private boolean centuryEquals(Iso8601Date d) {
		return (isCentury() && d.isCentury() && century == d.century);
	}
	
	private boolean yearEquals(Iso8601Date d) {
		return (isYear() && d.isYear() && year == d.year);
	}
	
	private boolean monthEquals(Iso8601Date d) {
		return (getYear() == d.getYear() && isMonth() && d.isMonth() && 
				getMonth() == d.getMonth());
	}

	private boolean weekEquals(Iso8601Date d) {
		return (getYear() == d.getYear() && isWeek() && d.isWeek() && 
				getWeekOfYear() == d.getWeekOfYear());
	}

	private boolean dayEquals(Iso8601Date d) {
		return (getYear() == d.getYear() && isDay() && d.isDay() && 
				getDayOfYear() == d.getDayOfYear());
	}
}
