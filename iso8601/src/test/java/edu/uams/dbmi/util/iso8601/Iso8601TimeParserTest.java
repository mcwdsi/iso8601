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

import junit.framework.TestCase;

public class Iso8601TimeParserTest extends TestCase {

	@Test
	public void testParser() {
		Iso8601TimeParser p = new Iso8601TimeParser();
		testSet(p, "20", "22", "33", ".1", "-05", "30");
		testSet(p, "20", "22", "33", ".123", "+01", "00");
		testSet(p, "00", "00", "00", ".000", "-01", "00");
		testSet(p, "24", "00", "00", ".000", "+12", "30");
		testSet(p, "04", "05", "09", ".999", "-07", "00");
		testSet(p, "19", "40", "17", ".999999999", "-06", "00");
	}
	
	public void testSet(Iso8601TimeParser p, String hrTxt, String miTxt,
			String seTxt, String frTxt, String tzHrTxt, String tzMinTxt) {
		int hr = Integer.parseInt(hrTxt);
		int mi = Integer.parseInt(miTxt);  
		int se = Integer.parseInt(seTxt);
		double fr = Double.parseDouble(frTxt);
		int tzHr = Integer.parseInt(tzHrTxt.replace('+', ' ').trim());
		int tzMi = Integer.parseInt(tzMinTxt);
		
		testWithoutFraction(p, hrTxt, miTxt, seTxt, tzHrTxt, tzMinTxt, hr, mi,
				se, tzHr, tzMi);
		testWithFraction(p, hrTxt, miTxt, seTxt, frTxt, tzHrTxt, tzMinTxt, hr, 
				mi, se, fr, tzHr, tzMi);
	}

	private void testWithFraction(Iso8601TimeParser p, String hrTxt,
			String miTxt, String seTxt, String frTxt, String tzHrTxt,
			String tzMinTxt, int hr, int mi, int se, double fr, int tzHr,
			int tzMi) {
		try {
			
			Iso8601Time t;
			
			/*
			 * All variations of hour
			 */
			String s1 = hrTxt + frTxt;
			t = p.parse(s1);
			testHourFraction(t, hr, fr, false, null, null);
			t = p.parse(s1.replace('.', ','));
			testHourFraction(t, hr, fr, false, null, null);
			
			String s2 = "T" + hrTxt + frTxt;
			t = p.parse(s2);
			testHourFraction(t, hr, fr, false, null, null);
			t = p.parse(s2.replace('.', ','));
			testHourFraction(t, hr, fr, false, null, null);
			
			String s3 = s1 + "Z";
			t = p.parse(s3);
			testHourFraction(t, hr, fr, true, null, null);
			t = p.parse(s3.replace('.', ','));
			testHourFraction(t, hr, fr, true, null, null);
			
			String s4 = s2 + "Z";
			t = p.parse(s4);
			testHourFraction(t, hr, fr, true, null, null);
			t = p.parse(s4.replace('.', ','));
			testHourFraction(t, hr, fr, true, null, null);
			
			String s5 = s1 + tzHrTxt;
			t = p.parse(s5);
			testHourFraction(t, hr, fr, false, tzHr, null);
			t = p.parse(s5.replace('.', ','));
			testHourFraction(t, hr, fr, false, tzHr, null);
			
			String s6 = s2 + tzHrTxt;
			t = p.parse(s6);
			testHourFraction(t, hr, fr, false, tzHr, null);
			t = p.parse(s6.replace('.', ','));
			testHourFraction(t, hr, fr, false, tzHr, null);
			
			String s7 = s5 + tzMinTxt;
			t = p.parse(s7);
			testHourFraction(t, hr, fr, false, tzHr, tzMi);
			t = p.parse(s7.replace('.', ','));
			testHourFraction(t, hr, fr, false, tzHr, tzMi);
			
			String s8 = s6 + tzMinTxt;
			t = p.parse(s8);
			testHourFraction(t, hr, fr, false, tzHr, tzMi);
			t = p.parse(s8.replace('.', ','));
			testHourFraction(t, hr, fr, false, tzHr, tzMi);
			
			String s9 = s5 + ":" + tzMinTxt;
			t = p.parse(s9);
			testHourFraction(t, hr, fr, false, tzHr, tzMi);
			t = p.parse(s9.replace('.', ','));
			testHourFraction(t, hr, fr, false, tzHr, tzMi);
			
			String s10 = s6 + ":" + tzMinTxt;
			t = p.parse(s10);
			testHourFraction(t, hr, fr, false, tzHr, tzMi);
			t = p.parse(s10.replace('.', ','));
			testHourFraction(t, hr, fr, false, tzHr, tzMi);
			
			/*
			 * All variations of minute
			 */
			String s11 = hrTxt + miTxt + frTxt;
			t = p.parse(s11);
			testMinuteFraction(t, hr, mi, fr, false, null, null);
			t = p.parse(s11.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, false, null, null);
			
			String s12 = "T" + s11;
			t = p.parse(s12);
			testMinuteFraction(t, hr, mi, fr, false, null, null);
			t = p.parse(s12.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, false, null, null);
			
			String s13 = hrTxt + ":" + miTxt + frTxt;
			t = p.parse(s13);
			testMinuteFraction(t, hr, mi, fr, false, null, null);
			t = p.parse(s13.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, false, null, null);
			
			String s14 = "T" + hrTxt + ":" + miTxt + frTxt;
			t = p.parse(s14);
			testMinuteFraction(t, hr, mi, fr, false, null, null);
			t = p.parse(s14.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, false, null, null);
			
			String s15 = s11 + "Z";
			t = p.parse(s15);
			testMinuteFraction(t, hr, mi, fr, true, null, null);
			t = p.parse(s15.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, true, null, null);
			
			String s16 = s12 + "Z";
			t = p.parse(s16); 
			testMinuteFraction(t, hr, mi, fr, true, null, null);
			t = p.parse(s16.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, true, null, null);
			
			String s17 = s13 + "Z";
			t = p.parse(s17);
			testMinuteFraction(t, hr, mi, fr, true, null, null);
			t = p.parse(s17.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, true, null, null);
			
			String s18 = s14 + "Z";
			t  = p.parse(s18);
			testMinuteFraction(t, hr, mi, fr, true, null, null);
			t = p.parse(s18.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, true, null, null);
			
			String s19 = s11 + tzHrTxt;
			t = p.parse(s19);
			testMinuteFraction(t, hr, mi, fr, false, tzHr, null);
			t = p.parse(s19.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, false, tzHr, null);
			
			String s20 = s12 + tzHrTxt;
			t = p.parse(s20);
			testMinuteFraction(t, hr, mi, fr, false, tzHr, null);
			t = p.parse(s20.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, false, tzHr, null);

			String s21 = s13 + tzHrTxt;
			t = p.parse(s21);
			testMinuteFraction(t, hr, mi, fr, false, tzHr, null);
			t = p.parse(s21.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, false, tzHr, null);
			
			String s22 = s14 + tzHrTxt;
			t = p.parse(s22);
			testMinuteFraction(t, hr, mi, fr, false, tzHr, null);
			t = p.parse(s22.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, false, tzHr, null);

			String s23 = s19 + tzMinTxt;
			t = p.parse(s23);
			testMinuteFraction(t, hr, mi, fr, false, tzHr, tzMi);
			t = p.parse(s23.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, false, tzHr, tzMi);
			
			String s24 = s20 + tzMinTxt;
			t = p.parse(s24);
			testMinuteFraction(t, hr, mi, fr, false, tzHr, tzMi);
			t = p.parse(s24.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, false, tzHr, tzMi);
			
			String s25 = s21 + ":" + tzMinTxt;
			t = p.parse(s25);
			testMinuteFraction(t, hr, mi, fr, false, tzHr, tzMi);
			t = p.parse(s25.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, false, tzHr, tzMi);
			
			String s26 = s22 + ":" + tzMinTxt;
			t = p.parse(s26);
			testMinuteFraction(t, hr, mi, fr, false, tzHr, tzMi);
			t = p.parse(s26.replace('.', ','));
			testMinuteFraction(t, hr, mi, fr, false, tzHr, tzMi);

			
			/*
			 * All variations of second
			 */
			String s27 = hrTxt + miTxt + seTxt + frTxt; 
			t = p.parse(s27);
			testSecondFraction(t, hr, mi, se, fr, false, null, null);
			t = p.parse(s27.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, false, null, null);
			
			String s28 = "T" + s27;
			t = p.parse(s28);
			testSecondFraction(t, hr, mi, se, fr, false, null, null);
			t = p.parse(s28.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, false, null, null);
			
			String s29 = hrTxt + ":" + miTxt + ":" + seTxt + frTxt;
			t = p.parse(s29);
			testSecondFraction(t, hr, mi, se, fr, false, null, null);
			t = p.parse(s29.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, false, null, null);
			
			String s30 = "T" + s29;
			t = p.parse(s30);
			testSecondFraction(t, hr, mi, se, fr, false, null, null);
			t = p.parse(s30.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, false, null, null);
			
			String s31 = s27 + "Z";
			t = p.parse(s31);
			testSecondFraction(t, hr, mi, se, fr, true, null, null);
			t = p.parse(s31.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, true, null, null);
			
			String s32 = s28 + "Z";
			t = p.parse(s32);
			testSecondFraction(t, hr, mi, se, fr, true, null, null);
			t = p.parse(s32.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, true, null, null);
			
			String s33 = s29 + "Z";
			t = p.parse(s33);
			testSecondFraction(t, hr, mi, se, fr, true, null, null);
			t = p.parse(s33.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, true, null, null);
			
			String s34 = s30 + "Z";
			t = p.parse(s34);
			testSecondFraction(t, hr, mi, se, fr, true, null, null);
			t = p.parse(s34.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, true, null, null);
			
			String s35 = s27 + tzHrTxt;
			t = p.parse(s35);
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, null);
			t = p.parse(s35.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, null);
			
			String s36 = s28 + tzHrTxt;
			t = p.parse(s36);
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, null);
			t = p.parse(s36.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, null);
			
			String s37 = s29 + tzHrTxt;
			t = p.parse(s37);
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, null);
			t = p.parse(s37.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, null);
			
			String s38 = s30 + tzHrTxt;
			t = p.parse(s38);
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, null);
			t = p.parse(s38.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, null);
			
			String s39 = s35 + tzMinTxt;
			t = p.parse(s39);
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, tzMi);
			t = p.parse(s39.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, tzMi);
			
			String s40 = s36 + tzMinTxt;
			t = p.parse(s40);
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, tzMi);
			t = p.parse(s40.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, tzMi);
			
			String s41 = s37 + ":" + tzMinTxt;
			t = p.parse(s41);
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, tzMi);
			t = p.parse(s41.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, tzMi);
			
			String s42 = s38 + ":" + tzMinTxt;
			t = p.parse(s42);
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, tzMi);
			t = p.parse(s42.replace('.', ','));
			testSecondFraction(t, hr, mi, se, fr, false, tzHr, tzMi);

		} catch (Iso8601TimeParseException e) {
			e.printStackTrace();
			fail();
		}
		
	}

	private void testSecondFraction(Iso8601Time t, int hr, int mi, int se,
			double fr, boolean isUtc, Integer tzHour, Integer tzMin) {
		Iso8601UnitTime tu = (Iso8601UnitTime)t;
		assertFalse(tu.getUnit().equals(TimeUnit.HOUR));
		assertFalse(tu.getUnit().equals(TimeUnit.MINUTE));		
		assertFalse(tu.getUnit().equals(TimeUnit.SECOND));
		assertEquals(t.getHour(), hr);
		assertEquals(t.getMinute(), mi);
		assertEquals(tu.getSecond().intValue(), se);
		
		if (isUtc) assertTrue(t.isUtcTimeZone());
		else assertFalse(t.isUtcTimeZone());
		
		if (tzHour != null) {
			assertEquals(t.getTimeZoneHourOffset(), tzHour.intValue());
		} else  if (!isUtc) {
			assertFalse(t.isTimeZoneSpecified());
		}
		
		if (tzMin != null) {
			assertEquals(t.getTimeZoneMinuteOffset(), tzMin.intValue());
		} 
		
	}

	private void testMinuteFraction(Iso8601Time t, int hr, int mi, double fr,
			boolean isUtc, Integer tzHour, Integer tzMin) {
		Iso8601FractionalTime tf = (Iso8601FractionalTime)t;
		assertEquals(t.getHour(), hr);
		assertEquals(t.getMinute(), mi);
		
		assertEquals(tf.getFractionalMinute(), fr);
		
		if (isUtc) assertTrue(t.isUtcTimeZone());
		else assertFalse(t.isUtcTimeZone());
		
		if (tzHour != null) {
			assertEquals(t.getTimeZoneHourOffset(), tzHour.intValue());
		} else  if (!isUtc) {
			assertFalse(t.isTimeZoneSpecified());
		}
		
		if (tzMin != null) {
			assertEquals(t.getTimeZoneMinuteOffset(), tzMin.intValue());
		} 
		
	}

	private void testHourFraction(Iso8601Time t, int hr, double fr, boolean isUtc,
			Integer tzHour, Integer tzMin) {
		Iso8601FractionalTime tf = (Iso8601FractionalTime)t;
		
		assertEquals(t.getHour(), hr);
		assertEquals(tf.getFractionalHour(), fr);
		
		if (isUtc) assertTrue(t.isUtcTimeZone());
		else assertFalse(t.isUtcTimeZone());
		
		if (tzHour != null) {
			assertEquals(t.getTimeZoneHourOffset(), tzHour.intValue());
		} else  if (!isUtc) {
			assertFalse(t.isTimeZoneSpecified());
		}
		
		if (tzMin != null) {
			assertEquals(t.getTimeZoneMinuteOffset(), tzMin.intValue());
		}
	}

	private void testWithoutFraction(Iso8601TimeParser p, String hrTxt,
			String miTxt, String seTxt, String tzHrTxt, String tzMinTxt,
			int hr, int mi, int se, int tzHr, int tzMi) {
		try {
			
			Iso8601Time t;
			
			/*
			 * All variations of hour
			 */
			String s1 = hrTxt;
			t = p.parse(s1);
			testHour(t, hr, false, null, null);
			
			String s2 = "T" + hrTxt;
			t = p.parse(s1);
			testHour(t, hr, false, null, null);
			
			String s3 = s1 + "Z";
			t = p.parse(s3);
			testHour(t, hr, true, null, null);
			
			String s4 = s2 + "Z";
			t = p.parse(s4);
			testHour(t, hr, true, null, null);
			
			String s5 = s1 + tzHrTxt;
			t = p.parse(s5);
			testHour(t, hr, false, tzHr, null);
			
			String s6 = s2 + tzHrTxt;
			t = p.parse(s6);
			testHour(t, hr, false, tzHr, null);
			
			String s7 = s5 + tzMinTxt;
			t = p.parse(s7);
			testHour(t, hr, false, tzHr, tzMi);
			
			String s8 = s6 + tzMinTxt;
			t = p.parse(s8);
			testHour(t, hr, false, tzHr, tzMi);
			
			String s9 = s5 + ":" + tzMinTxt;
			t = p.parse(s9);
			testHour(t, hr, false, tzHr, tzMi);
			
			String s10 = s6 + ":" + tzMinTxt;
			t = p.parse(s10);
			testHour(t, hr, false, tzHr, tzMi);
			
			/*
			 * All variations of minute
			 */
			String s11 = s1 + miTxt;
			t = p.parse(s11);
			testMinute(t, hr, mi, false, null, null);
			
			String s12 = s2 + miTxt;
			t = p.parse(s12);
			testMinute(t, hr, mi, false, null, null);
			
			String s13 = s1 + ":" + miTxt;
			t = p.parse(s13);
			testMinute(t, hr, mi, false, null, null);
			
			String s14 = s2 + ":" + miTxt;
			t = p.parse(s14);
			testMinute(t, hr, mi, false, null, null);
			
			String s15 = s11 + "Z";
			t = p.parse(s15);
			testMinute(t, hr, mi, true, null, null);
			
			String s16 = s12 + "Z";
			t = p.parse(s16); 
			testMinute(t, hr, mi, true, null, null);
			
			String s17 = s13 + "Z";
			t = p.parse(s17);
			testMinute(t, hr, mi, true, null, null);
			
			String s18 = s14 + "Z";
			t  = p.parse(s18);
			testMinute(t, hr, mi, true, null, null);
			
			String s19 = s11 + tzHrTxt;
			t = p.parse(s19);
			testMinute(t, hr, mi, false, tzHr, null);
			
			String s20 = s12 + tzHrTxt;
			t = p.parse(s20);
			testMinute(t, hr, mi, false, tzHr, null);

			String s21 = s13 + tzHrTxt;
			t = p.parse(s21);
			testMinute(t, hr, mi, false, tzHr, null);
			
			String s22 = s14 + tzHrTxt;
			t = p.parse(s22);
			testMinute(t, hr, mi, false, tzHr, null);

			String s23 = s19 + tzMinTxt;
			t = p.parse(s23);
			testMinute(t, hr, mi, false, tzHr, tzMi);
			
			String s24 = s20 + tzMinTxt;
			t = p.parse(s24);
			testMinute(t, hr, mi, false, tzHr, tzMi);
			
			String s25 = s21 + ":" + tzMinTxt;
			t = p.parse(s25);
			testMinute(t, hr, mi, false, tzHr, tzMi);
			
			String s26 = s22 + ":" + tzMinTxt;
			t = p.parse(s26);
			testMinute(t, hr, mi, false, tzHr, tzMi);

			
			/*
			 * All variations of second
			 */
			String s27 = s11 + seTxt; 
			t = p.parse(s27);
			testSecond(t, hr, mi, se, false, null, null);
			
			String s28 = s12 + seTxt;
			t = p.parse(s28);
			testSecond(t, hr, mi, se, false, null, null);
			
			String s29 = s13 + ":" + seTxt;
			t = p.parse(s29);
			testSecond(t, hr, mi, se, false, null, null);
			
			String s30 = s14 + ":" + seTxt;
			t = p.parse(s30);
			testSecond(t, hr, mi, se, false, null, null);
			
			String s31 = s27 + "Z";
			t = p.parse(s31);
			testSecond(t, hr, mi, se, true, null, null);
			String s32 = s28 + "Z";
			t = p.parse(s32);
			testSecond(t, hr, mi, se, true, null, null);
			String s33 = s29 + "Z";
			t = p.parse(s33);
			testSecond(t, hr, mi, se, true, null, null);
			String s34 = s30 + "Z";
			t = p.parse(s34);
			testSecond(t, hr, mi, se, true, null, null);
			
			String s35 = s27 + tzHrTxt;
			t = p.parse(s35);
			testSecond(t, hr, mi, se, false, tzHr, null);
			String s36 = s28 + tzHrTxt;
			t = p.parse(s36);
			testSecond(t, hr, mi, se, false, tzHr, null);
			String s37 = s29 + tzHrTxt;
			t = p.parse(s37);
			testSecond(t, hr, mi, se, false, tzHr, null);
			String s38 = s30 + tzHrTxt;
			t = p.parse(s38);
			testSecond(t, hr, mi, se, false, tzHr, null);
			
			String s39 = s35 + tzMinTxt;
			t = p.parse(s39);
			testSecond(t, hr, mi, se, false, tzHr, tzMi);
			String s40 = s36 + tzMinTxt;
			t = p.parse(s40);
			testSecond(t, hr, mi, se, false, tzHr, tzMi);
			String s41 = s37 + ":" + tzMinTxt;
			t = p.parse(s41);
			testSecond(t, hr, mi, se, false, tzHr, tzMi);
			String s42 = s38 + ":" + tzMinTxt;
			t = p.parse(s42);
			testSecond(t, hr, mi, se, false, tzHr, tzMi);

		} catch (Iso8601TimeParseException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testHour(Iso8601Time t, int hr, boolean isUtc, Integer tzHour,
			Integer tzMin) {
		Iso8601UnitTime tu = (Iso8601UnitTime)t;
		assertTrue(tu.getUnit().equals(TimeUnit.HOUR));
		assertFalse(tu.getUnit().equals(TimeUnit.MINUTE));		
		assertFalse(tu.getUnit().equals(TimeUnit.SECOND));
		assertEquals(t.getHour(), hr);
		
		if (isUtc) assertTrue(t.isUtcTimeZone());
		else assertFalse(t.isUtcTimeZone());
		
		if (tzHour != null) {
			assertEquals(t.getTimeZoneHourOffset(), tzHour.intValue());
		} else  if (!isUtc) {
			assertFalse(t.isTimeZoneSpecified());
		}
		
		if (tzMin != null) {
			assertEquals(t.getTimeZoneMinuteOffset(), tzMin.intValue());
		}
	}
	
	public void testMinute(Iso8601Time t, int hr, int min, boolean isUtc,
			Integer tzHour, Integer tzMin) {
		Iso8601UnitTime tu = (Iso8601UnitTime)t;
		assertFalse(tu.getUnit().equals(TimeUnit.HOUR));
		assertTrue(tu.getUnit().equals(TimeUnit.MINUTE));		
		assertFalse(tu.getUnit().equals(TimeUnit.SECOND));
		assertEquals(t.getHour(), hr);
		assertEquals(t.getMinute(), min);
		
		if (isUtc) assertTrue(t.isUtcTimeZone());
		else assertFalse(t.isUtcTimeZone());
		
		if (tzHour != null) {
			assertEquals(t.getTimeZoneHourOffset(), tzHour.intValue());
		} else  if (!isUtc) {
			assertFalse(t.isTimeZoneSpecified());
		}
		
		if (tzMin != null) {
			assertEquals(t.getTimeZoneMinuteOffset(), tzMin.intValue());
		} 
	}
	
	public void testSecond(Iso8601Time t, int hr, int min, int sec, 
			boolean isUtc, Integer tzHour, Integer tzMin) {
		Iso8601UnitTime tu = (Iso8601UnitTime)t;
		assertFalse(tu.getUnit().equals(TimeUnit.HOUR));
		assertFalse(tu.getUnit().equals(TimeUnit.MINUTE));		
		assertTrue(tu.getUnit().equals(TimeUnit.SECOND));
		assertEquals(t.getHour(), hr);
		assertEquals(t.getMinute(), min);
		assertEquals(tu.getSecond().intValue(), sec);
		
		if (isUtc) assertTrue(t.isUtcTimeZone());
		else assertFalse(t.isUtcTimeZone());
		
		if (tzHour != null) {
			assertEquals(t.getTimeZoneHourOffset(), tzHour.intValue());
		} else  if (!isUtc) {
			assertFalse(t.isTimeZoneSpecified());
		}
		
		if (tzMin != null) {
			assertEquals(t.getTimeZoneMinuteOffset(), tzMin.intValue());
		} 
	}
}
