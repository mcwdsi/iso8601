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
import edu.uams.dbmi.util.iso8601.Iso8601DateParseException;
import edu.uams.dbmi.util.iso8601.Iso8601DateParser;

import junit.framework.TestCase;

public class Iso8601DateParserTest extends TestCase {

	@Test
	public void testWeekFormatParse() {
		Iso8601DateParser parser = new Iso8601DateParser();
		
		String s1 = "2010-W01-1";
		try {
			Iso8601Date d1 = parser.parse(s1);
			assertNotNull(d1);
			assertTrue(d1.isDay());
			assertEquals(d1.getYear(), 2010);
			assertEquals(d1.getWeekOfYear(), 1);
			assertEquals(d1.getDayOfWeek(), 1);
		} catch (Iso8601DateParseException e) {
			fail();
			e.printStackTrace();
		}
		
		String s2 = "2010-W01";
		try {
			Iso8601Date d2 = parser.parse(s2);
			assertNotNull(d2);
			assertFalse(d2.isDay());
			assertTrue(d2.isWeek());
			assertEquals(d2.getYear(), 2010);
			assertEquals(d2.getWeekOfYear(), 1);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s3 = "2010W01";
		try {
			Iso8601Date d3 = parser.parse(s3);
			assertNotNull(d3);
			assertFalse(d3.isDay());
			assertTrue(d3.isWeek());
			assertEquals(d3.getYear(), 2010);
			assertEquals(d3.getWeekOfYear(), 1);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s4 = "2010W011";
		try {
			Iso8601Date d4 = parser.parse(s4);
			assertNotNull(d4);
			assertTrue(d4.isDay());
			assertEquals(d4.getYear(), 2010);
			assertEquals(d4.getWeekOfYear(), 1);
			assertEquals(d4.getDayOfWeek(), 1);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s5 = "2010011";
		try {
			Iso8601Date d5 = parser.parse(s5);
			assertNotNull(d5);
			assertTrue(d5.isDay());
			assertEquals(d5.getYear(), 2010);
			assertEquals(d5.getDayOfYear(), 11);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s6 = "2010W1";
		boolean correctError = false;
		try {
			parser.parse(s6);
		} catch (Iso8601DateParseException e) {
			System.err.println(e.getMessage());
			correctError = true;
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			fail();
		}
		assertTrue(correctError);
		
		String s7 = "201001";
		try {
			Iso8601Date d7 = parser.parse(s7);
			assertNotNull(d7);
			assertTrue(d7.isMonth());
			assertEquals(d7.getYear(), 2010);
			assertEquals(d7.getMonth(), 1);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s8 = "2010-W011";
		boolean correctError2 = false;
		try {
			parser.parse(s8);
		} catch (Iso8601DateParseException e) {
			correctError2 = true;
			System.err.println(e.getMessage());
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		assertTrue(correctError2);
	}
	
	@Test
	public void testCenturyParse() {
		Iso8601DateParser parser = new Iso8601DateParser();
		
		String c1 = "20";
		try {
			testCenturyString(parser, c1, 20);
		} catch (Iso8601DateParseException e1) {
			e1.printStackTrace();
			fail();
		}
		
		String c2 = "+15";
		try {
			testCenturyString(parser, c2, 15);
		} catch (Iso8601DateParseException e1) {
			e1.printStackTrace();
			fail();
		}

		
		String c3 = "-05";
		try {
			testCenturyString(parser, c3, -5);
		} catch (Iso8601DateParseException e1) {
			e1.printStackTrace();
			fail();
		}
		
		String c4 = "00";
		try {
			testCenturyString(parser, c4, 0);
		} catch (Iso8601DateParseException e1) {
			e1.printStackTrace();
			fail();
		}
		
		String c5 = "5";
		boolean error1 = false;
		try {
			parser.parse(c5);
		} catch (Iso8601DateParseException e) {
			error1 = true;
			System.err.println(e.getMessage());
		}
		assertTrue(error1);
		
		String c6 = "123";
		boolean error2 = false;
		try {
			parser.parse(c6);
		} catch (Iso8601DateParseException e) {
			error2 = true;
			System.err.println(e.getMessage());
		}
		assertTrue(error2);
	}

	public void testCenturyString(Iso8601DateParser parser, String c1, int c) throws Iso8601DateParseException {
			Iso8601Date d1 = parser.parse(c1);
			assertNotNull(d1);
			assertTrue(d1.isCentury());
			assertEquals(d1.getCentury(), c);
			assertFalse(d1.isDay());
			assertFalse(d1.isWeek());
			assertFalse(d1.isMonth());
			assertFalse(d1.isYear());
			GregorianCalendar cal = d1.getCalendarForFirstDayInInterval();
			assertNotNull(cal); 
			if (c > 0) {
				assertEquals(cal.get(Calendar.YEAR), c*100);
				assertEquals(cal.get(Calendar.ERA), GregorianCalendar.AD);
			} else {
				assertEquals(cal.get(Calendar.YEAR), -c*100+1);
				assertEquals(cal.get(Calendar.ERA), GregorianCalendar.BC);
			}
	}
	
	@Test 
	public void testYearParse() {
		Iso8601DateParser parser = new Iso8601DateParser();
		
		String s1 = "2011";
		try {
			testYearString(parser, s1, 2011);
		} catch (Iso8601DateParseException e7) {
			fail();
			e7.printStackTrace();
		}
		
		String s2 = "+2011";
		try {
			testYearString(parser, s2, 2011);
		} catch (Iso8601DateParseException e6) {
			fail();
			e6.printStackTrace();
		}
		
		String s3 = "-2011";
		try {
			testYearString(parser, s3, -2011);
		} catch (Iso8601DateParseException e5) {
			fail();
			e5.printStackTrace();
		}
		
		String s4 = "0054";
		try {
			testYearString(parser, s4, 54);
		} catch (Iso8601DateParseException e3) {
			fail();
			e3.printStackTrace();
		}
		
		String s5 ="0000";
		try {
			testYearString(parser, s5, 0);
		} catch (Iso8601DateParseException e4) {
			fail();
			e4.printStackTrace();
		}
		
		String s6 = "9999";
		try {
			testYearString(parser, s6, 9999);
		} catch (Iso8601DateParseException e3) {
			fail();
			e3.printStackTrace();
		}
		
		String s7= "0007";
		try {
			testYearString(parser, s7, 7);
		} catch (Iso8601DateParseException e2) {
			fail();
			e2.printStackTrace();
		}
		
		String s8 = "-0237";
		try {
			testYearString(parser, s8, -237);
		} catch (Iso8601DateParseException e1) {
			fail();
			e1.printStackTrace();
		}
		
		String s9 = "+10000";
		boolean errExpected = false;
		try {
			testYearString(parser, s9, 2011);
		} catch (Iso8601DateParseException e) {
			errExpected = true;
		}
		assertTrue(errExpected);
		
		String s10 = "_5";
		errExpected = false;
		try {
			testYearString(parser, s10, 2011);
		} catch (Iso8601DateParseException e) {
			System.err.println(e.getMessage());
			errExpected = true;
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			fail();
		}
		assertTrue(errExpected);
	}
	
	public void testYearString(Iso8601DateParser parser, String s, int c) throws Iso8601DateParseException {
		Iso8601Date d1 = parser.parse(s);
		assertNotNull(d1);
		assertTrue(d1.isYear());
		assertEquals(d1.getYear(), c);
		assertEquals(d1.getCentury(), c/100);
		assertFalse(d1.isDay());
		assertFalse(d1.isWeek());
		assertFalse(d1.isMonth());
		assertFalse(d1.isCentury());
		GregorianCalendar cal = d1.getCalendarForFirstDayInInterval();
		assertNotNull(cal); 
		if (c > 0) {
			assertEquals(cal.get(Calendar.YEAR), c);
			assertEquals(cal.get(Calendar.ERA), GregorianCalendar.AD);
		} else {
			assertEquals(cal.get(Calendar.YEAR), -c+1);
			assertEquals(cal.get(Calendar.ERA), GregorianCalendar.BC);
		}
	}
	
	@Test
	public void testYearMonthParse() {
		Iso8601DateParser parser = new Iso8601DateParser();
		
		String s1 = "1582-08";
		try {
			testYearMonthString(parser, s1, 1582, 8);
		} catch (Iso8601DateParseException e) {
			fail();
			e.printStackTrace();
		}
		
		String s2 = "158208";
		try {
			testYearMonthString(parser, s2, 1582, 8);
		} catch (Iso8601DateParseException e) {
			fail();
			e.printStackTrace();
		}
				
		String s3 = "+167104";
		try {
			testYearMonthString(parser, s3, 1671, 4);
		} catch (Iso8601DateParseException e) {
			fail();
			e.printStackTrace();
		}
				
		String s4 = "-001412";
		try {
			testYearMonthString(parser, s4, -14, 12);
		} catch (Iso8601DateParseException e) {
			fail();
			e.printStackTrace();
		}
				
		String s5 = "-2312-09";
		try {
			testYearMonthString(parser, s5, -2312, 9);
		} catch (Iso8601DateParseException e) {
			fail();
			e.printStackTrace();
		}
		try {
			testYearMonthString(parser, s1, 1582, 8);
		} catch (Iso8601DateParseException e) {
			fail();
			e.printStackTrace();
		}
				
		String s6 = "+2312-09";
		try {
			testYearMonthString(parser, s6, 2312, 9);
		} catch (Iso8601DateParseException e) {
			fail();
			e.printStackTrace();
		}
				
		String s7 = "1582-7";
		boolean errExpected = false;
		try {
			testYearMonthString(parser, s7, 1582, 7);
		} catch (Iso8601DateParseException e) {
			errExpected = true;
			System.err.println(e.getMessage());
		}
		assertTrue(errExpected);
				
		String s8 = "-12345";
		errExpected = false;
		try {
			testYearMonthString(parser, s8, 1582, 8);
		} catch (Iso8601DateParseException e) {
			errExpected = true;
			System.err.println(e.getMessage());
		}
		assertTrue(errExpected);
		
	}

	public void testYearMonthString(Iso8601DateParser parser, String s1, int yr, int mo) throws Iso8601DateParseException {
		Iso8601Date d1 = parser.parse(s1);
		assertNotNull(d1);
		assertTrue(d1.isMonth());
		assertEquals(d1.getYear(), yr);
		assertEquals(d1.getCentury(), yr/100);
		assertEquals(d1.getMonth(), mo);
		assertFalse(d1.isDay());
		assertFalse(d1.isWeek());
		assertFalse(d1.isYear());
		assertFalse(d1.isCentury());
		GregorianCalendar cal = d1.getCalendarForFirstDayInInterval();
		assertNotNull(cal); 
		if (yr > 0) {
			assertEquals(cal.get(Calendar.YEAR), yr);
			assertEquals(cal.get(Calendar.ERA), GregorianCalendar.AD);
		} else {
			assertEquals(cal.get(Calendar.YEAR), -yr+1);
			assertEquals(cal.get(Calendar.ERA), GregorianCalendar.BC);
		}		
		assertEquals(cal.get(Calendar.MONTH), mo-1);
	}
	
	@Test
	public void testYearMonthDayParse() {
		Iso8601DateParser parser = new Iso8601DateParser();
		
		String s1 = "+0000-01-01";
		try {
			testYearMonthDayString(parser, s1, 0, 1, 1);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s2 = "-0000-12-31";
		try {
			testYearMonthDayString(parser, s2, 0, 12, 31);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s3 = "+00000101";
		try {
			testYearMonthDayString(parser, s3, 0, 1, 1);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s4 = "-00010101";
		try {
			testYearMonthDayString(parser, s4, -1, 1, 1);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s5 = "00001231";
		try {
			testYearMonthDayString(parser, s5, 0, 12, 31);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s6 = "-0005-07-22";
		try {
			testYearMonthDayString(parser, s6, -5, 7, 22);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s7 = "+2011-10-02";
		try {
			testYearMonthDayString(parser, s7, 2011, 10, 2);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s8 = "2010-5-31";
		boolean expectedError = false;
		try {
			testYearMonthDayString(parser, s8, 2010, 5, 31);
		} catch (Iso8601DateParseException e) {
			expectedError = true;
			System.err.println(e.getMessage());
		}
		assertTrue(expectedError);
		
		String s9 = "1942-05-1";
		expectedError = false;
		try {
			testYearMonthDayString(parser, s9, 2010, 5, 31);
		} catch (Iso8601DateParseException e) {
			expectedError = true;
			System.err.println(e.getMessage());
		}
		assertTrue(expectedError);
		
		
	}

	public void testYearMonthDayString(Iso8601DateParser parser, String s, int yr,
			int mo, int da) throws Iso8601DateParseException {
		Iso8601Date d1 = parser.parse(s);
		assertNotNull(d1);
		assertTrue(d1.isDay());
		assertEquals(d1.getYear(), yr);
		assertEquals(d1.getCentury(), yr/100);
		assertEquals(d1.getMonth(), mo);
		assertEquals(d1.getDayOfMonth(), da);
		assertFalse(d1.isMonth());
		assertFalse(d1.isWeek());
		assertFalse(d1.isYear());
		assertFalse(d1.isCentury());
		GregorianCalendar cal = d1.getCalendarForDay();
		assertNotNull(cal); 
		if (yr > 0) {
			assertEquals(cal.get(Calendar.YEAR), yr);
			assertEquals(cal.get(Calendar.ERA), GregorianCalendar.AD);
		} else {
			assertEquals(cal.get(Calendar.YEAR), -yr+1);
			assertEquals(cal.get(Calendar.ERA), GregorianCalendar.BC);
		}		
		assertEquals(cal.get(Calendar.MONTH), mo-1);		
		assertEquals(cal.get(Calendar.DAY_OF_MONTH), da);
	}
	
	@Test
	public void testYearDayParse() {
		Iso8601DateParser parser = new Iso8601DateParser();
		
		String s1 = "2011233";
		try {
			testYearDayString(parser, s1, 2011, 233);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s2 = "+1922-009";
		try {
			testYearDayString(parser, s2, 1922, 9);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
				
		String s3 = "-0322-322";
		try {
			testYearDayString(parser, s3, -322, 322);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
				
		String s4 = "1965-012";
		try {
			testYearDayString(parser, s4, 1965, 12);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
				
		String s5 = "0000001";
		try {
			testYearDayString(parser, s5, 0, 1);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
				
		String s6 = "9999-365";
		try {
			testYearDayString(parser, s6, 9999, 365);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
				
		String s7 = "1877-000";
		boolean expectedErr = false;
		try {
			testYearDayString(parser, s7, 2011, 233);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		} catch (IllegalArgumentException e1) {
			System.err.println(e1.getMessage());
			expectedErr = true;
		}
		assertTrue(expectedErr);
				
		String s8 = "1982-222-";
		expectedErr = false;
		try {
			testYearDayString(parser, s8, 1982, 222);
		} catch (Iso8601DateParseException e) {
			System.err.println(e.getMessage());
			expectedErr = true;
		}
		assertTrue(expectedErr);		
	}

	private void testYearDayString(Iso8601DateParser parser, String s, int yr, int da) throws Iso8601DateParseException {
		Iso8601Date d1 = parser.parse(s);
		assertNotNull(d1);
		assertTrue(d1.isDay());
		assertEquals(d1.getYear(), yr);
		assertEquals(d1.getCentury(), yr/100);
		assertEquals(d1.getDayOfYear(), da);
		assertFalse(d1.isMonth());
		assertFalse(d1.isWeek());
		assertFalse(d1.isYear());
		assertFalse(d1.isCentury());
		GregorianCalendar cal = d1.getCalendarForDay();
		assertNotNull(cal); 
		if (yr > 0) {
			assertEquals(cal.get(Calendar.YEAR), yr);
			assertEquals(cal.get(Calendar.ERA), GregorianCalendar.AD);
		} else {
			assertEquals(cal.get(Calendar.YEAR), -yr+1);
			assertEquals(cal.get(Calendar.ERA), GregorianCalendar.BC);
		}		
		assertEquals(cal.get(Calendar.DAY_OF_YEAR), da);	
	}
	
	@Test
	public void testExtendedYearParse() {
		Iso8601DateParser parser = new Iso8601DateParser();
		parser.setExtraYearDigits(2);
		
		String s1 = "+001988-03-02";
		try {
			testYearMonthDayString(parser, s1, 1988, 3, 2);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s2 = "-0000000101";
		try {
			testYearMonthDayString(parser, s2, 0, 1, 1);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
				
		String s3 = "1988";
		try {
			testCenturyString(parser, s3, 1988);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s4 = "002011";
		try {
			testYearString(parser, s4, 2011);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s5 = "+00201110";
		try {
			testYearMonthString(parser, s5, 2011, 10);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s6 = "-123456-08";
		try {
			testYearMonthString(parser, s6, -123456, 8);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s7 = "+001776-07-04";
		try {
			testYearMonthDayString(parser, s7, 1776, 7, 4);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		}
		
		String s8 = "1945-01";
		boolean errExpected = false;
		try {
			testYearMonthString(parser, s8, 1945, 1);
		} catch (Iso8601DateParseException e) {
			System.err.println(e.getMessage());
			errExpected = true;
		}
		assertTrue(errExpected);
		
		String s9 = "02000-01-01";
		errExpected = false;
		try {
			testYearMonthDayString(parser, s9, 2000, 1, 1);
		} catch (Iso8601DateParseException e) {
			System.err.println(e.getMessage());
			errExpected = true;
		}
		assertTrue(errExpected);
	}
}
