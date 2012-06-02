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

import org.junit.Test;

import edu.uams.dbmi.util.iso8601.Iso8601Date;
import edu.uams.dbmi.util.iso8601.Iso8601Date.DateConfiguration;

import junit.framework.TestCase;

public class Iso8601DateTest extends TestCase {
	@Test 
	public void testIso8601Date() {
		Iso8601Date d = new Iso8601Date();
		assertNotNull(d);
	}

	@Test
	public void testIso8601Date_Century() {
		Iso8601Date d1 = new Iso8601Date(Iso8601Date.DateConfiguration.CENTURY, 14);
		assertEquals(d1.getCentury(), 14);
		assertTrue(d1.isCentury());
		assertFalse(d1.isYear());
		assertFalse(d1.isMonth());
		assertFalse(d1.isWeek());
		assertFalse(d1.isDay());
		assertNotNull(d1.getCalendarForFirstDayInInterval());
	}
	
	@Test
	public void testLeapYearFunction() {
		assertTrue(Iso8601Date.isLeapYear(2000));
		assertFalse(Iso8601Date.isLeapYear(2001));
		assertTrue(Iso8601Date.isLeapYear(1980));
		assertTrue(Iso8601Date.isLeapYear(2004));
		assertFalse(Iso8601Date.isLeapYear(1900));
		assertTrue(Iso8601Date.isLeapYear(0));
	}
	
	@Test
	public void testIso8601Date_Year() {
		Iso8601Date d1 = new Iso8601Date(Iso8601Date.DateConfiguration.YEAR, 1833);
		assertEquals(d1.getYear(), 1833);
		assertEquals(d1.getCentury(), 18);
		assertFalse(d1.isCentury());
		assertTrue(d1.isYear());
		assertFalse(d1.isMonth());
		assertFalse(d1.isWeek());
		assertFalse(d1.isDay());
		assertNotNull(d1.getCalendarForFirstDayInInterval());
	}
	
	@Test
	public void testIso8601Date_YearMonth() {
		Iso8601Date d1 = new Iso8601Date(Iso8601Date.DateConfiguration.YEAR_MONTH, 1912, 1);
		assertEquals(d1.getCentury(), 19);
		assertEquals(d1.getYear(), 1912);
		assertEquals(d1.getMonth(), 1);
		assertFalse(d1.isCentury());
		assertFalse(d1.isYear());
		assertTrue(d1.isMonth());
		assertFalse(d1.isWeek());
		assertFalse(d1.isDay());
		assertNotNull(d1.getCalendarForFirstDayInInterval());
	}	
	
	@Test
	public void testIso8601Date_YearMonthDay() {
		Iso8601Date d1 = new Iso8601Date(
				Iso8601Date.DateConfiguration.YEAR_MONTH_DAY, 1688, 12, 31);
		assertEquals(d1.getCentury(), 16);
		assertEquals(d1.getYear(), 1688);
		assertEquals(d1.getMonth(), 12);
		assertEquals(d1.getDayOfMonth(), 31);
		assertFalse(d1.isCentury());
		assertFalse(d1.isYear());
		assertFalse(d1.isMonth());
		assertFalse(d1.isWeek());
		assertTrue(d1.isDay());
		assertNotNull(d1.getCalendarForDay());
	}
	
	@Test
	public void testIso8601Date_YearWeek() {
		Iso8601Date d1 = new Iso8601Date(
				Iso8601Date.DateConfiguration.YEAR_WEEK, 1755, 33);
		assertEquals(d1.getCentury(), 17);
		assertEquals(d1.getYear(), 1755);
		assertEquals(d1.getWeekOfYear(), 33);
		assertFalse(d1.isCentury());
		assertFalse(d1.isYear());
		assertFalse(d1.isMonth());
		assertTrue(d1.isWeek());
		assertFalse(d1.isDay());
		assertNotNull(d1.getCalendarForFirstDayInInterval());
	}
	
	@Test
	public void testIso8601Date_YearWeekDay() {
		Iso8601Date d1 = new Iso8601Date(
				Iso8601Date.DateConfiguration.YEAR_WEEK_DAY, 1755, 33, 5);
		assertEquals(d1.getCentury(), 17);
		assertEquals(d1.getYear(), 1755);
		assertEquals(d1.getWeekOfYear(), 33);
		assertEquals(d1.getDayOfWeek(), 5);
		assertFalse(d1.isCentury());
		assertFalse(d1.isYear());
		assertFalse(d1.isMonth());
		assertFalse(d1.isWeek());
		assertTrue(d1.isDay());
		assertNotNull(d1.getCalendarForDay());
	}
	
	@Test
	public void testIso8601Date_YearDayOfYear() {
		Iso8601Date d1 = new Iso8601Date(
				Iso8601Date.DateConfiguration.YEAR_DAY_OF_YEAR, 1972, 221);
		assertEquals(d1.getCentury(), 19);
		assertEquals(d1.getYear(), 1972);
		assertEquals(d1.getDayOfYear(), 221);
		assertFalse(d1.isCentury());
		assertFalse(d1.isYear());
		assertFalse(d1.isMonth());
		assertFalse(d1.isWeek());
		assertTrue(d1.isDay());
		assertNotNull(d1.getCalendarForDay());
	}
	
	@Test
	public void testIso8601Date_Check2011() {
		int yr = 2011;
		int dayOfYear = 1;
		for (int mo=1; mo<13; mo++) {
			int cDay;
			if (Iso8601Date.is31DayMonth(mo)) {
				cDay = 31;
			} else if (mo==2) {
				cDay = 28;
			} else {
				cDay = 30;
			}
			
			for (int dy = 1; dy<=cDay; dy++) {
				Iso8601Date d = new Iso8601Date(
						Iso8601Date.DateConfiguration.YEAR_MONTH_DAY, 
						yr, mo, dy);
				Iso8601Date d2 = new Iso8601Date(
						Iso8601Date.DateConfiguration.YEAR_DAY_OF_YEAR,
						yr, dayOfYear);
				assertEquals(d, d2);
				assertEquals(d.getCentury(), 20);
				assertEquals(d.getYear(), yr);
				assertEquals(d.getMonth(), mo);
				assertEquals(d.getDayOfMonth(), dy);
				
				GregorianCalendar c = d.getCalendarForDay();
				assertEquals(c.get(Calendar.YEAR), 2011);
				assertEquals(c.get(Calendar.MONTH), mo-1);
				assertEquals(c.get(Calendar.DAY_OF_MONTH), dy);
				assertEquals(d.getDayOfYear(), dayOfYear);
				assertEquals(d.getDayOfYear(), c.get(Calendar.DAY_OF_YEAR));
				dayOfYear++;
			}
		}
	}
	
	@Test
	/*
	 * The ISO 8601 standard document uses April 12, 1985 as an example, so
	 *  let's make sure we get it right.
	 */
	public void testApril_12_1985() {
		Iso8601Date d = new Iso8601Date(DateConfiguration.YEAR_MONTH_DAY, 1985,
				4, 12);
		assertEquals(d.getCentury(), 19);
		assertEquals(d.getYear(), 1985);
		assertEquals(d.getMonth(), 4);
		assertEquals(d.getDayOfMonth(), 12);
		assertTrue(d.isDay());
		assertFalse(d.isCentury());
		assertFalse(d.isYear());
		assertFalse(d.isMonth());
		assertFalse(d.isWeek());
		GregorianCalendar c = d.getCalendarForDay();
		assertEquals(c.get(Calendar.YEAR), 1985);
		assertEquals(c.get(Calendar.MONTH), 3);
		assertEquals(c.get(Calendar.DAY_OF_MONTH), 12);
		assertEquals(c.get(Calendar.DAY_OF_YEAR), 102);
		assertEquals(d.getDayOfYear(), 102);
		assertEquals(d.getWeekOfYear(), 15);
		assertEquals(d.getDayOfWeek(), 5);
		assertEquals(c.get(Calendar.WEEK_OF_YEAR), 15);
		
		Iso8601Date d2 = new Iso8601Date(DateConfiguration.YEAR_DAY_OF_YEAR, 
				1985, 102);
		assertEquals(d2.getCentury(), 19);
		assertEquals(d2.getYear(), 1985);
		assertEquals(d2.getMonth(), 4);
		assertEquals(d2.getDayOfMonth(), 12);
		assertEquals(d2.getDayOfYear(), 102);
		assertTrue(d2.isDay());
		assertFalse(d2.isCentury());
		assertFalse(d2.isYear());
		assertFalse(d2.isMonth());
		assertFalse(d2.isWeek());
		GregorianCalendar c2 = d2.getCalendarForDay();
		assertEquals(c2.get(Calendar.YEAR), 1985);
		assertEquals(c2.get(Calendar.MONTH), 3);
		assertEquals(c2.get(Calendar.DAY_OF_MONTH), 12);
		assertEquals(c2.get(Calendar.DAY_OF_YEAR), 102);
		assertEquals(d2.getWeekOfYear(), 15);
		assertEquals(d2.getDayOfWeek(), 5);
		assertEquals(c2.get(Calendar.WEEK_OF_YEAR), 15);
		
		Iso8601Date d3 = new Iso8601Date(DateConfiguration.YEAR_WEEK_DAY, 
				1985, 15, 5);
		assertEquals(d3.getCentury(), 19);
		assertEquals(d3.getYear(), 1985);
		assertEquals(d3.getWeekOfYear(), 15);
		assertEquals(d3.getDayOfWeek(), 5);
		assertEquals(d3.getMonth(), 4);
		assertEquals(d3.getDayOfMonth(), 12);
		assertEquals(d3.getDayOfYear(), 102);
		assertTrue(d3.isDay());
		assertFalse(d3.isCentury());
		assertFalse(d3.isYear());
		assertFalse(d3.isMonth());
		assertFalse(d3.isWeek());
		assertEquals(d3.getMonth(), 4);
		assertEquals(d3.getDayOfMonth(), 12);
		GregorianCalendar c3 = d3.getCalendarForDay();
		assertEquals(c3.get(Calendar.YEAR), 1985);
		assertEquals(c3.get(Calendar.MONTH), 3);
		assertEquals(c3.get(Calendar.DAY_OF_MONTH), 12);
		assertEquals(c3.get(Calendar.DAY_OF_YEAR), 102);
		assertEquals(c3.get(Calendar.WEEK_OF_YEAR), 15);
		assertEquals(
				Iso8601Date.convertCalendarDayOfWeekToIso(
						c3.get(Calendar.DAY_OF_WEEK)), 5);
	}
	
	@Test
	/*
	 * 2008-12-29 is written "2009-W01-1"
	 * 
	 */
	public void testDec_29_2008() {
		Iso8601Date d1 = new Iso8601Date(DateConfiguration.YEAR_WEEK_DAY, 
				2009, 1, 1);
		assertEquals(d1.getYear(), 2008);
		assertEquals(d1.getWeekCalendarYear(), 2009);
		assertEquals(d1.getWeekOfYear(), 1);
		assertEquals(d1.getDayOfWeek(), 1);
		assertEquals(d1.getMonth(), 12);
		assertEquals(d1.getDayOfMonth(), 29);
		assertEquals(d1.getDayOfYear(), 364);
		GregorianCalendar c1 = d1.getCalendarForDay();
		assertNotNull(c1);
		assertEquals(c1.get(Calendar.YEAR), 2008);
		assertEquals(c1.get(Calendar.MONTH), 11);
		assertEquals(c1.get(Calendar.DAY_OF_MONTH), 29);
		assertEquals(c1.get(Calendar.WEEK_OF_YEAR), 1);
		assertEquals(Iso8601Date.convertCalendarDayOfWeekToIso(
				c1.get(Calendar.DAY_OF_WEEK)), 1);
		assertEquals(c1.get(Calendar.DAY_OF_YEAR), 364);
		
		Iso8601Date d2 = new Iso8601Date(DateConfiguration.YEAR_MONTH_DAY, 
				2008, 12, 29);
		assertEquals(d2.getCentury(), 20);
		assertEquals(d2.getYear(), 2008);
		assertEquals(d2.getMonth(), 12);
		assertEquals(d2.getDayOfMonth(), 29);
		assertEquals(d2.getDayOfYear(), 364);
		assertEquals(d2.getWeekOfYear(), 1);
		assertEquals(d2.getDayOfWeek(), 1);
		GregorianCalendar c2 = d1.getCalendarForDay();
		assertNotNull(c2);
		assertEquals(c2.get(Calendar.YEAR), 2008);
		assertEquals(c2.get(Calendar.MONTH), 11);
		assertEquals(c2.get(Calendar.DAY_OF_MONTH), 29);
		assertEquals(c2.get(Calendar.WEEK_OF_YEAR), 1);
		assertEquals(Iso8601Date.convertCalendarDayOfWeekToIso(
				c2.get(Calendar.DAY_OF_WEEK)), 1);
		assertEquals(c2.get(Calendar.DAY_OF_YEAR), 364);		
		
	}
	
	@Test
	/*
	 * 2010-01-03 is written "2009-W53-7"
	 * 
	 */
	public void testJan_03_2010() {
		Iso8601Date d1 = new Iso8601Date(DateConfiguration.YEAR_WEEK_DAY,
				2009, 53, 7);
		Iso8601Date d2 = new Iso8601Date(DateConfiguration.YEAR_MONTH_DAY,
				2010, 1, 3);
		Iso8601Date d3 = new Iso8601Date(DateConfiguration.YEAR_DAY_OF_YEAR,
				2010, 3);

		assertTrue(d1.equals(d2));
		assertTrue(d1.equals(d3));
		assertTrue(d2.equals(d3));
		assertEquals(d1.getCentury(), 20);
		assertEquals(d1.getYear(), 2010);
		assertEquals(d1.getWeekCalendarYear(), 2009);
		assertEquals(d1.getWeekOfYear(), 53);
		assertEquals(d1.getDayOfWeek(), 7);
		assertEquals(d1.getMonth(), 1);
		assertEquals(d1.getDayOfMonth(), 3);
		assertEquals(d1.getDayOfYear(), 3);
		GregorianCalendar c1 = d1.getCalendarForDay();
		assertNotNull(c1);
		assertEquals(c1.get(Calendar.MONTH), 0);
		assertEquals(c1.get(Calendar.DAY_OF_MONTH), 3);
		assertEquals(c1.get(Calendar.WEEK_OF_YEAR), 53);
		assertEquals(Iso8601Date.convertCalendarDayOfWeekToIso(
				c1.get(Calendar.DAY_OF_WEEK)), 7);
		assertEquals(c1.get(Calendar.YEAR), 2010);
		assertEquals(c1.get(Calendar.DAY_OF_YEAR), 3);
		
		assertEquals(d2.getCentury(), 20);
		assertEquals(d2.getYear(), 2010);
		assertEquals(d2.getWeekOfYear(), 53);
		assertEquals(d2.getDayOfWeek(), 7);
		assertEquals(d2.getMonth(), 1);
		assertEquals(d2.getDayOfMonth(), 3);
		assertEquals(d2.getDayOfYear(), 3);
		GregorianCalendar c2 = d2.getCalendarForDay();
		assertNotNull(c2);
		assertEquals(c2.get(Calendar.MONTH), 0);
		assertEquals(c2.get(Calendar.DAY_OF_MONTH), 3);
		assertEquals(c2.get(Calendar.WEEK_OF_YEAR), 53);
		assertEquals(Iso8601Date.convertCalendarDayOfWeekToIso(
				c2.get(Calendar.DAY_OF_WEEK)), 7);
		assertEquals(c2.get(Calendar.YEAR), 2010);
		assertEquals(c2.get(Calendar.DAY_OF_YEAR), 3);
		
	}
}
