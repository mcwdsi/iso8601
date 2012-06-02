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

import org.junit.Test;

import edu.uams.dbmi.util.iso8601.Iso8601Date.DateConfiguration;

import junit.framework.TestCase;


public class LeapSecondTableTest extends TestCase {
	@Test
	public void testLeapSeconds() {
		testForLeapSecond(1989, 6, 30, false);
		testForLeapSecond(1989, 12, 31, true);
		testForLeapSecond(1990, 6, 30, false);
		testForLeapSecond(1990, 12, 31, true);
		testForLeapSecond(1972, 12, 31, true);
		testForLeapSecond(1972, 6, 30, true);
		testForLeapSecond(1944, 6, 30, false);
		testForLeapSecond(1992, 6, 30, true);
		testForLeapSecond(1992, 12, 31, false);
		testForLeapSecond(1993, 6, 30, true);
		testForLeapSecond(1993, 12, 31, false);
		testForLeapSecond(1994, 6, 30, true);
		testForLeapSecond(1994, 12, 31, false);
		testForLeapSecond(1995, 6, 30, false);
		testForLeapSecond(1995, 12, 31, true);
		testForLeapSecond(1997, 6, 30, true);
		testForLeapSecond(1997, 12, 31, false);
		testForLeapSecond(1998, 6, 30, false);
		testForLeapSecond(1998, 12, 31, true);
		testForLeapSecond(2008, 6, 30, false);
		testForLeapSecond(2008, 12, 31, true);
		testForLeapSecond(2005, 6, 30, false);
		testForLeapSecond(2005, 12, 31, true);
	}
	
	public void testForLeapSecond(int yr, int mo, int da, boolean hasLeapSecond) {
		Iso8601Date d = new Iso8601Date(DateConfiguration.YEAR_MONTH_DAY,
				yr, mo, da);
		assertEquals(LeapSecondTable.hasLeapSecond(d), hasLeapSecond);
		
	}
}
