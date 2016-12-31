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
import java.util.TimeZone;

import javax.measure.converter.UnitConverter;

import edu.uams.dbmi.util.iso8601.Iso8601Date.DateConfiguration;

public class Iso8601DateTime {
	Iso8601Date date;
	Iso8601Time time;
	GregorianCalendar c;
	
	/**
	 * Convenience constructor that builds an ISO8601 date/time object
	 * 	from current system date/time (by calling GregorianCalendar.
	 * 	getInstance()). So the Iso8601Time
	 * 	member variable will be an instance of Iso8601UnitTime where
	 * 	getUnit() returns TimeUnit.MILLISECOND.
	 */
	public Iso8601DateTime() {
		c = (GregorianCalendar)GregorianCalendar.getInstance();
		date = new Iso8601Date(DateConfiguration.YEAR_MONTH_DAY, 
				c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, 
				c.get(Calendar.DAY_OF_MONTH));
		IsoUnitTimeBuilder tb = new IsoUnitTimeBuilder(c.get(Calendar.HOUR_OF_DAY));
		tb.setMinute(c.get(Calendar.MINUTE));
		tb.setSecond(c.get(Calendar.SECOND));
		tb.setUnit(TimeUnit.MILLISECOND);
		tb.setSubsecond(c.get(Calendar.MILLISECOND));
		int[] tzOff = getTzHourMinOffsetForCalendar(c);
		tb.setTimeZoneOffsetHour(tzOff[0]);
		tb.setTimeZoneOffsetMinute(tzOff[1]);
		time = new Iso8601UnitTime(tb);
	}
	
	public Iso8601DateTime(Iso8601Date date, Iso8601Time time) {
		if (date == null || time == null) {
			throw new NullPointerException("Iso8601DateTime(): neither " +
					"date nor time may be null.");
		}
		
		if (!date.isDay()) {
			throw new IllegalArgumentException("The Iso8601Date passed to " +
					"this constructor does not represent a day, and thus " +
					"time of day is meaningless.");
		}
		
		if (areDateAndTimeCompatibile(date, time)) {
			this. date = date;
			this. time = time;
			buildCalendar();
		} else {
			throw new IllegalArgumentException("Date and time are not compatible.");
		}
	}
	
	private void buildCalendar() {
		int offMillis = time.getTimeZoneHourOffset() * 3600000;
		offMillis += time.getTimeZoneMinuteOffset() * 60000;
		String[] tzIds = TimeZone.getAvailableIDs(offMillis);
		if (tzIds != null && tzIds.length > 0) {
			TimeZone tz = TimeZone.getTimeZone(tzIds[0]);
			//c.setTimeZone(tz);
			c = (GregorianCalendar)Calendar.getInstance(tz);
			c.set(date.getYear(), date.getMonth()-1, date.getDayOfMonth());
		}
		if (time.getHour() == 24) {
			c.add(Calendar.DAY_OF_MONTH, 1); 
			c.set(Calendar.HOUR, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			time.hr = 0;
			date = new Iso8601Date(DateConfiguration.YEAR_MONTH_DAY, 
					c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1,
					c.get(Calendar.DAY_OF_MONTH));
		} else {
			c.set(Calendar.HOUR, time.getHour());
			if (time instanceof Iso8601UnitTime) {
				Iso8601UnitTime u = (Iso8601UnitTime)time;
				/*
				 * If time unit is not an hour, then it is less than an hour, 
				 *  so at the largest, the unit is minute, so minute must be
				 *  present.
				 */
				if (!u.getUnit().equals(TimeUnit.HOUR)) {
					c.set(Calendar.MINUTE, u.getMinute());
					/*
					 * If the unit is not an hour or a minute, then the 
					 *  largest it can be is second
					 */
					if (!u.getUnit().equals(TimeUnit.MINUTE)) {
						//TODO One question is what happens with leap seconds?
						//  I'm assuming java.util.Gregorian calendar handles 
						//	them with ease, but who knows?
						c.set(Calendar.SECOND, u.getSecond());
						/*
						 * If it's not hour, minute, or second, then it's
						 * 	smaller than second. Convert to nearest 
						 *  millisecond.
						 */
						if (!u.getUnit().equals(TimeUnit.SECOND)) {
							UnitConverter tc = u.getUnit().getConverterTo(
									TimeUnit.MILLISECOND);
							double d = tc.convert(u.getSubsecond());
							int milli = (int)Math.floor(d);
							c.set(Calendar.MILLISECOND, milli);
						}
					}
				}
			}
			
		}
	}

	/*
	 * Main goal of this function is to ensure that if the time has
	 * 	second == 60, then the date is one on which there has been a 
	 * 	leap second, in reality.
	 * 
	 * The Iso8601UnitTime class will make sure that for second==60,
	 * 	the hour == 23 and minute == 59.  So no need to check that here.
	 */
	private boolean areDateAndTimeCompatibile(Iso8601Date date,
			Iso8601Time time) {
		/*
		 * We default to true, and only switch to false if the second is 60
		 * 	and there was no leap second on the date.
		 */
		boolean ok = true;
		/*
		 * Only Iso8601UnitTime objects can have seconds, and therefore
		 * 	leap seconds.
		 */
		if (time instanceof Iso8601UnitTime) {
			Iso8601UnitTime tu = (Iso8601UnitTime)time;
			/*
			 * It's only a leap second if second == 60
			 */
			if (tu.getSecond() != null && tu.getSecond() == 60) {
				/*
				 * If date had a leap second, then we'll stay at true, 
				 * 	otherwise if date had no leap second, we have an 
				 *  illegitimate date/time combination, so switch to 
				 *  false, in which cases caller of method should fail.
				 */
				ok = LeapSecondTable.hasLeapSecond(date);
			}
		}
		return ok;
	}

	public Iso8601Date getDate() {
		return date;
	}
	
	public Iso8601Time getTime() {
		return time;
	}
	
	/**
	 * Get the calendar object that corresponds to this ISO 8601 date/time 
	 * 	object.  Note that calling getDate().getCalendar() will return the
	 * 	same instance.
	 * 
	 * Note that the time zone may not be a perfect match.  The method that
	 * 	creates the calendar searches the java.util.TimeZone class for a
	 * 	list of time zone "ids" based on the combined hour/minute offset.
	 * 	It then sets the time zone to be the one based on the first id
	 * 	returned in the list.
	 * 
	 * If no time zone id is returned corresponding to the offset, then 
	 * 	the default time zone of the JVM that created the object is 
	 * 	assumed.
	 * 
	 * @return The Gregorian calendar object that corresponds to this 
	 * 				ISO8601 date/time
	 */
	public Calendar getCalendar() {
		return c;
	}
	
	protected int[] getTzHourMinOffsetForCalendar(GregorianCalendar c) {
		int tzOffsetMillis = c.get(Calendar.ZONE_OFFSET);
		int dstOffsetMillis = c.get(Calendar.DST_OFFSET);
		
		int offsetMillis = tzOffsetMillis + dstOffsetMillis;
		
		int[] result = new int[2];
		result[0] = offsetMillis / 3600000;
		
		int remainder = offsetMillis % 3600000;
		if (remainder != 0) {
			result[1] = remainder / 60000;
		}
		
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o instanceof Iso8601DateTime) {
			Iso8601DateTime dt = (Iso8601DateTime)o;
			eq = (time.equals(dt.time) && date.equals(dt.date));
		}
		return eq;
	}
}
