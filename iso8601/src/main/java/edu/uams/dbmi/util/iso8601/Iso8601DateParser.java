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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uams.dbmi.util.iso8601.Iso8601Date.DateConfiguration;



public class Iso8601DateParser {
	
	int cLen=2;			//length of string for just CC
	int yLen=4;			//length of string for just CCDY
	
	int ymLen=6;		//length of string for CCDYMM
	int ymhLen=7;		//length of string for CCDY-MM
	int ymdLen=8;		//length of string for CCDYMMDD
	int ymdhLen=10;		//length of string for CCDY-MM-DD
	
	int yWwLen=7;		//length of string for CCDYWww
	int yWwhLen=8; 		//length of string for CCDY-Www
	int yWwdLen=8; 		//length of string for CCDYWwwd
	int yWwdhLen=10;	//length of string for CCDY-Www-d
	
	int ydLen=7; 		//length of string for CCDYddd
	int ydhLen=8; 		//length of string for CCDY-ddd
	
	static final String NUM_PATTERN_TXT = "\\d+";
	static Pattern NUM_PATTERN = Pattern.compile(NUM_PATTERN_TXT);
		
	/*
	 *  Format for dates may be:
	 *	CC			e.g., 19 for beginning of 1900 to end of 1999
	 *	CCDY		e.g,  2011 for current year
	 *	CCDYMM		e.g., 201109 for current month of current year (dash may separate Y and MM)
	 *	CCDYMMDD	e.g., 20110924 for today (dash may separate MM and DD)
	 *				  this format represents a day, and thus allows time of day to follow in DateTime
	 *	CCDY'W'ww	e.g., 2011W38 for current week of year (dash may separate Y and 'W')
	 *	CCDY'W'wwd	e.g., 2011W386 for today (dash may separate ww and d)
	 *				  this format represents a day, and thus allows time of day to follow in DateTime
	 *	CCDYddd		e.g., 2011267 for today (dash may separate Y and ddd)
	 *				  this format represents a day, and thus allows time of day to follow in DateTime
	 *
	 *	Also, for any of these formats, the CC may be preceded by a '+' or '-'.
	 *	Also, whenever a dash is used in one position where it's permitted, it MUST be used in all positions
	 *		where it's permitted.
	 *
	 *	? any others?
	 *
	 *	Note: the standard allows additional digits for years by mutual agreement of sender and receiver
	 *		of data.  We flex the class by having an additional parameter that is the number of 
	 *		extra year digits permitted by the parser, and adjust the parser behavior accordingly.
	 *		
	 *		For example, in the case where sender/receiver decide to include 2 extra digits, the sequence
	 *		201109 would be interpreted as the year 201,109 instead of year 2011 and month 09.
	 */
	int century;
	int year;
	int month;
	int dayOfMonth;
	int weekOfYear;
	int dayOfWeek;
	int dayOfYear;
	boolean isDay;
	boolean isInit;
	boolean isYearNegative;
	boolean isNumeric;
	
	int extraYearDigits;

	public Iso8601DateParser() {
		initialize();
	}

	protected void initialize() {
		isDay = false;
		isInit = false;
		isYearNegative = false;
		isNumeric = false;
	}

	public Iso8601DateParser(int extraYearDigits) {
		this();
		setExtraYearDigits(extraYearDigits);
	}

	public void setExtraYearDigits(int extraYearDigits) {
		if (extraYearDigits < 1) {
			throw new IllegalArgumentException("Must specify at least 1 extra digit for year (" +
				extraYearDigits + ")");
		}
		this. extraYearDigits = extraYearDigits;
		
		cLen += this .extraYearDigits;
		yLen += this .extraYearDigits;
		
		ymLen += this .extraYearDigits;
		ymhLen += this .extraYearDigits;
		ymdLen += this .extraYearDigits;
		ymdhLen += this .extraYearDigits;
		
		yWwLen += this .extraYearDigits;
		yWwhLen += this .extraYearDigits;
		yWwdLen += this .extraYearDigits;
		yWwdhLen += this .extraYearDigits;
		
		ydLen += this .extraYearDigits;
		ydhLen += this .extraYearDigits;
	}

	public Iso8601Date parse(String s) throws Iso8601DateParseException {
		initialize();
		Iso8601Date d = null;
		int len = s.length();
		if (s.startsWith("+")) {
			s = s.substring(1, len);
			len = s.length();
		} else if (s.startsWith("-")) {
			s = s.substring(1, len);
			len = s.length();
			isYearNegative = true;
		}
		setIsNumeric(s);
		
		if (len == cLen && isNumeric) {
			setCentury(Integer.parseInt(s));
			d = new Iso8601Date(DateConfiguration.CENTURY, century);
		} else if (len == yLen && isNumeric) {
			setYear(Integer.parseInt(s));
			d = new Iso8601Date(DateConfiguration.YEAR, year);
		} else if (len == ymLen && isNumeric) {
			setYear(Integer.parseInt(s.substring(0, ymLen-2)));
			int month = Integer.parseInt(s.substring(ymLen-2, len));
			d = new Iso8601Date(DateConfiguration.YEAR_MONTH, year, month);
		} else if (len > ymLen) {
			if (s.contains("W")) {
				//System.out.println("Parsing week format " + s);
				d = parseWeekFormat(s);
			} else if (s.contains("-")) {
				d = parseHyphenatedFormat(s);
			} else if (len == ydLen && isNumeric) {
				setYear(Integer.parseInt(s.substring(0, ydLen-3)));
				int dayOfYear = Integer.parseInt(s.substring(ydLen-3, len));
				d = new Iso8601Date(
						DateConfiguration.YEAR_DAY_OF_YEAR, year, dayOfYear);
			} else if (len == ymdLen && isNumeric) {
				setYear(Integer.parseInt(s.substring(0, ymdLen-4)));
				int month = Integer.parseInt(s.substring(ymdLen-4, ymdLen-2));
				int day = Integer.parseInt(s.substring(ymdLen-2, len));
				d = new Iso8601Date(
						DateConfiguration.YEAR_MONTH_DAY, year, month, day);					
			}
		} else {
			throw new Iso8601DateParseException("Illegal format (" + s + ")");
		}
		
		return d;
	}

	private void setCentury(int century) {
		this. century = (isYearNegative) ? -century : century;
	}

	private void setYear(int year) {
		this. year = (isYearNegative) ? -year : year;
	}
		
	protected Iso8601Date parseWeekFormat(String s) throws Iso8601DateParseException {
		if (s.contains("-")) {
			//System.out.println("\thyphenated.");
			return parseWeekWithHyphen(s);
		} else {
			//System.out.println("\tnot hyphenated.");
			return parseWeekWithoutHyphen(s);
		}
	}

		private Iso8601Date parseWeekWithHyphen(String s) throws Iso8601DateParseException {
		Iso8601Date d = null;
		int len = s.length();
		if (!(len == yWwdhLen || len == yWwhLen) || s.charAt(yLen) != '-') {
			throw new Iso8601DateParseException(
					"Illegal ISO 8601 week format with hyphens (" + 
						s + ")");
		}				
		setYear(Integer.parseInt(s.substring(0, yLen)));
		int week = Integer.parseInt(s.substring(yLen+2, yLen+4));
		if (len == yWwdhLen) {
			if (s.charAt(yLen+4) != '-') {
				throw new Iso8601DateParseException(
						"Illegal ISO 8601 week format with hyphens (" + 
							s + ")");					
			}
			int day = Integer.parseInt(s.substring(yLen+5,yLen+6));
		d = new Iso8601Date(DateConfiguration.YEAR_WEEK_DAY, year, 
					week, day);
		} else {
			d = new Iso8601Date(DateConfiguration.YEAR_WEEK, year, week);
		}
		return d;
	}
		
	protected Iso8601Date parseWeekWithoutHyphen(String s) throws Iso8601DateParseException {
		Iso8601Date d = null;
		setYear(Integer.parseInt(s.substring(0, yLen)));
		//System.out.println("Year = " + year);
		//System.out.println("Char at " + yLen + " = " + s.charAt(yLen));
		if (s.charAt(yLen) != 'W') {
			throw new Iso8601DateParseException("Expected 'W' at " +
				"location: " + yLen);
		}
		int week = Integer.parseInt(s.substring(yLen+1, yLen+3));
		//System.out.println("Week = " + week);
		int len = s.length();
		if (len == yWwdLen) {
			int day = Integer.parseInt(s.substring(len-1, len));
			//System.out.println("Day = " + day);
			d = new Iso8601Date(DateConfiguration.YEAR_WEEK_DAY, year, 
					week, day);
		} else {
		d = new Iso8601Date(DateConfiguration.YEAR_WEEK, year, week);
		}
		return d;
	}
	
	protected Iso8601Date parseHyphenatedFormat(String s) throws Iso8601DateParseException {
		/*
	 * If we got here, then we have one of three formats:
		 * 	YYYY-MM
		 * 	YYYY-MM-DD
		 *  YYYY-DDD
		 */
		Iso8601Date d = null;
		if (s.charAt(yLen) != '-') {
		throw new Iso8601DateParseException("Unrecognized ISO 8601 " +
					"hyphenated format (" + s + ")");				
		}
		int len = s.length();
		setYear(Integer.parseInt(s.substring(0, yLen)));
		if (len == ymhLen) {
			int month = Integer.parseInt(s.substring(yLen+1, len));
			d = new Iso8601Date(DateConfiguration.YEAR_MONTH, year, month);
		} else if (len == ymdhLen) {
			if (s.charAt(yLen+3) != '-') {
				throw new Iso8601DateParseException("Unrecognized ISO 8601 " +
						"hyphenated format (" + s + ")");
			}
			int month = Integer.parseInt(s.substring(yLen+1, yLen+3));
			int day = Integer.parseInt(s.substring(yLen+4, len));
		d = new Iso8601Date(DateConfiguration.YEAR_MONTH_DAY, year,
					month, day);
		} else if (len == ydhLen) {
			int day = Integer.parseInt(s.substring(yLen+1, len));
			d = new Iso8601Date(DateConfiguration.YEAR_DAY_OF_YEAR, 
					year, day);
		} else {
			throw new Iso8601DateParseException("Unrecognized ISO 8601 " +
					"hyphenated format (" + s + ")");
		}
		
		return d;
	}
	
	private void setIsNumeric(String s) {
		Matcher m = NUM_PATTERN.matcher(s);
		isNumeric = m.matches();
		
	}
}


