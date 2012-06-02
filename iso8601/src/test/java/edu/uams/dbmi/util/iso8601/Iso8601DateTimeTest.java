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

import javax.measure.quantity.Duration;
import javax.measure.unit.Unit;

import org.junit.Test;

import edu.uams.dbmi.util.iso8601.Iso8601Date.DateConfiguration;

import junit.framework.TestCase;

public class Iso8601DateTimeTest extends TestCase {
	
	@Test
	public void testDateTimes() {
		String ans1 =  "2011-11-11T11:11:11.111-11:00";
		int yr1 = 2011;
		int mo1 = 11;
		int da1 = 11;
		int hr1 = 11;
		int mi1 = 11;
		int se1 = 11;
		int subSec1 = 111;
		Unit<Duration> u1 = TimeUnit.MILLISECOND;
		int tzHr1 = -11;
		int tzMi1 = 0;
		
		dateTimeTest(ans1, yr1, mo1, da1, hr1, mi1, se1, subSec1, u1, tzHr1,
				tzMi1);
		
		String ans2 = "2001-01-02T00:00:00+04:00";
		int yr2 = 2001;
		int mo2 = 1;
		int da2 = 1;
		int hr2 = 24;
		int mi2 = 0;
		int se2 = 0;
		int subSec2 = 0;
		Unit<Duration> u2 = TimeUnit.SECOND;
		int tzHr2 = 4;
		int tzMi2 = 0;
		
		dateTimeTest(ans2, yr2, mo2, da2, hr2, mi2, se2, subSec2, u2, tzHr2,
				tzMi2);
	}
	
	@Test
	public void dateTimeTest(String ans, int yr, int mo, int da, int hr,
			int mi, int se, int subSec, Unit<Duration> u, int tzHr, 
			int tzMin) {
		Iso8601Date d1 = new Iso8601Date(
				DateConfiguration.YEAR_MONTH_DAY, yr, mo, da);
		assertNotNull(d1);
		
		IsoUnitTimeBuilder tb = new IsoUnitTimeBuilder(hr);
		tb.setMinute(mi);
		tb.setSecond(se);
		tb.setUnit(u);
		tb.setSubsecond(subSec);
		tb.setTimeZoneOffsetHour(tzHr);
		tb.setTimeZoneOffsetMinute(tzMin);
		Iso8601UnitTime t1 = new Iso8601UnitTime(tb);
		assertNotNull(t1);
		
		Iso8601DateTime dt1Construct = new Iso8601DateTime(d1,t1);
		assertNotNull(dt1Construct);
		
		Iso8601DateTimeParser p1 = new Iso8601DateTimeParser();
		Iso8601DateTimeFormatter f1 = new Iso8601DateTimeFormatter();
		
		Iso8601DateTime dt1Parsed = null;
		String dt1TxtFormatted = null;
		
		Iso8601DateFormatter df = new Iso8601DateFormatter();
		System.out.println(df.format(dt1Construct.getDate()));
		try {
			dt1Parsed = p1.parse(ans);
			//Iso8601UnitTime t2 = (Iso8601UnitTime)dt1Parsed.getTime();
			System.out.println(df.format(dt1Parsed.getDate()));
			assertNotNull(dt1Parsed);
			dt1TxtFormatted = f1.format(dt1Construct);
			assertNotNull(dt1TxtFormatted);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			assertTrue(dt1Parsed != null);
		} catch (Iso8601TimeParseException e) {
			e.printStackTrace();
			assertTrue(dt1Parsed != null);
		}
		
		assertTrue(dt1Construct.equals(dt1Parsed));
		assertEquals(ans, dt1TxtFormatted);
	}
	
	@Test
	public void testCurrentDateTimeRoundTrip() {
		Iso8601DateTime dt = new Iso8601DateTime();
		Iso8601DateTimeFormatter f = new Iso8601DateTimeFormatter();
		String dtTxt = f.format(dt);
		Iso8601DateTimeParser p = new Iso8601DateTimeParser();
		Iso8601DateTime dtp = null;
		String dtTxt2 = null;
		try {
			dtp = p.parse(dtTxt);
			dtTxt2 = f.format(dtp);
		} catch (Iso8601DateParseException e) {
			e.printStackTrace();
			fail();
		} catch (Iso8601TimeParseException e) {
			e.printStackTrace();
			fail();
		}
		
		assertTrue(dt.equals(dtp));
		assertEquals(dtTxt, dtTxt2);
		System.out.println(dtTxt);
	}
}
