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

import edu.uams.dbmi.util.iso8601.Iso8601Date.DateConfiguration;

public class Iso8601DateFormatter {
	
	StringBuilder formattedDate;
	FormatOptions options;
	

	public static class FormatOptions implements Cloneable {
		boolean extended;
		boolean forceSignedYear;
		int yearDigits;
		Iso8601Date.DateConfiguration config;
		
		public FormatOptions() {
		}
		
		public void setExtended(boolean extended) {
			this. extended = extended;
		}
		
		public boolean isExtended() {
			return extended;
		}
		
		public void setForceSignedYear(boolean force) {
			this. forceSignedYear = force;
		}
		
		public boolean isForceSignedYear() {
			return forceSignedYear;
		}
		
		public void setYearDigits(int yrDigits) {
			if (yrDigits < 4) {
				throw new IllegalArgumentException("Must be at least 4 digit year.");
			}
			this. yearDigits = yrDigits;
		}
		
		public int getYearDigits() {
			return yearDigits;
		}
		
		public void setConfiguration(Iso8601Date.DateConfiguration config) {
			this.config = config;
		}
		
		public Iso8601Date.DateConfiguration getConfiguration() {
			return config;
		}
		
		public Object clone() {
			try {
				return super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 * By default, creates FormatOptions object initialized as follows:
	 * 		extended = true
	 * 		force signed year = false
	 * 		# digits for year = 4
	 * 		year month day format (as opposed to Year week day, etc).
	 */
	public Iso8601DateFormatter() {
		formattedDate = new StringBuilder();
		options = new FormatOptions();
		options.setForceSignedYear(false);
		options.setExtended(true);
		options.setYearDigits(4);
		options.setConfiguration(DateConfiguration.YEAR_MONTH_DAY);
	}
	
	public Iso8601DateFormatter(FormatOptions options) {
		formattedDate = new StringBuilder();
		this. options = options;
	}
	
	public String format(Iso8601Date d) {
		formattedDate.setLength(0);
		if (!checkCompatibilityWithFormatConfiguration(d)) {
			throw new IllegalArgumentException("Granularity of format " +
					"option and of date are incompatible.");
		}
		switch (options.getConfiguration()) {
			case CENTURY:
				addYearOrCentury(d.getCentury(), options.getYearDigits()-2);
				break;
			case YEAR:
				addYearOrCentury(d.getYear(), options.getYearDigits());
				break;
			case YEAR_MONTH:
				addYearOrCentury(d.getYear(), options.getYearDigits());
				addMonth(d.getMonth());
				break;
			case YEAR_WEEK:
				addYearOrCentury(d.getWeekCalendarYear(), options.getYearDigits());
				addWeekOfYear(d.getWeekOfYear());
				break;
			case YEAR_MONTH_DAY:
				addYearOrCentury(d.getYear(), options.getYearDigits());
				addMonth(d.getMonth());
				addDayOfMonth(d.getDayOfMonth());
				break;
			case YEAR_DAY_OF_YEAR:
				addYearOrCentury(d.getYear(), options.getYearDigits());
				addDayOfYear(d.getDayOfYear());
				break;
			case YEAR_WEEK_DAY:
				addYearOrCentury(d.getWeekCalendarYear(), options.getYearDigits());
				addWeekOfYear(d.getWeekOfYear());
				addDayOfWeek(d.getDayOfWeek());
				break;
		}
	
		return formattedDate.toString();
	}

	/*
	 * If we're asked to use year/month/day format, but the Iso8601Date
	 * 	object represents an interval with duration > 1 day, then we
	 * 	cannot do it because we don't have the day of month (or a 
	 * 	particular day of month).
	 * 
	 * So, we need to make sure the format requested matches the date.
	 *
	 * We don't just want to assume the DateConfiguration in the Iso8601Date
	 * 	object itself because despite how the object was initialized, we may
	 *  want to format it a different way!  Flexibility.
	 */
	private boolean checkCompatibilityWithFormatConfiguration(Iso8601Date d) {
		boolean ok = false;

		switch (options.getConfiguration()) {
			case CENTURY:
				ok = true;
				break;
			case YEAR:
				ok = !d.isCentury();
				break;
			case YEAR_MONTH:
				ok = !(d.isCentury() || d.isYear());
				break;
			case YEAR_WEEK:
				ok = d.isWeek() || d.isDay();
				break;
			case YEAR_DAY_OF_YEAR:
			case YEAR_MONTH_DAY:
			case YEAR_WEEK_DAY:
				ok = d.isDay();
				break;
		}
		return ok;
	}

	private void addYearOrCentury(int val, int len) {
		String yr = Integer.toString(val);
		if (options.isForceSignedYear() && val > -1) {
			formattedDate.append("+");
		} else if (val < 0) {
			formattedDate.append("-");
			yr = yr.substring(1);
		}
		
		int cPadZeroes = len - yr.length();
		for (int i=0; i<cPadZeroes; i++) {
			formattedDate.append(0);
		}
		
		formattedDate.append(yr);
	}
	
	private void addMonth(int month) {
		if (options.isExtended()) {
			formattedDate.append("-");
		}
		if (month <10) {
			formattedDate.append("0");
		}
		formattedDate.append(month);
	}
	
	private void addDayOfMonth(int day) {
		if (options.isExtended()) {
			formattedDate.append("-");
		}
		if (day <10) {
			formattedDate.append("0");
		}
		formattedDate.append(day);		
	}
	
	private void addDayOfWeek(int day) {
		if (options.isExtended()) {
			formattedDate.append("-");
		}
		formattedDate.append(day);			
	}
	
	private void addWeekOfYear(int week) {
		if (options.isExtended()) {
			formattedDate.append("-");
		}
		formattedDate.append("W");
		if (week < 10) {
			formattedDate.append("0");
		}
		formattedDate.append(week);
	}
	
	private void addDayOfYear(int day) {
		if (options.isExtended()) {
			formattedDate.append("-");
		}
		if (day < 100) {
			formattedDate.append("0");
		}
		if (day < 10) {
			formattedDate.append("0");
		}
		formattedDate.append(day);
	}
}
