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


import java.math.BigDecimal;
import org.junit.Test;

import edu.uams.dbmi.util.iso8601.Iso8601TimeFormatter.FormatOptions;

import junit.framework.TestCase;

public class Iso8601TimeFormatterTest extends TestCase {
	@Test
	public void testFormatUnitTime() {
		testSet("15", "33", "22", ".123456789", "+05", "00");
		testSet("15", "33", "22", ".00010000", "-04", "30");
	}

	private void testSet(String hrTxt, String miTxt, String seTxt, 
			String frTxt, String tzHrTxt, String tzMinTxt) {
		
		String basicHr = hrTxt;
		String basicHrMin = hrTxt + miTxt;
		String basicHrMinSec = hrTxt + miTxt + seTxt;
		String basicHrMinSecSs = basicHrMinSec + frTxt;
		
		String extendedHr = hrTxt;
		String extendedHrMin = hrTxt + ":" + miTxt;
		String extendedHrMinSec = extendedHrMin + ":" + seTxt;
		String extendedHrMinSecSs = extendedHrMinSec + frTxt;
		
		IsoUnitTimeBuilder tb = new IsoUnitTimeBuilder(
				Integer.parseInt(hrTxt));
		testConfigurations(tb, miTxt, seTxt, frTxt, basicHr, extendedHr,
				basicHrMin, extendedHrMin, basicHrMinSec, 
				extendedHrMinSec, basicHrMinSecSs, 
				extendedHrMinSecSs);
		
		tb = new IsoUnitTimeBuilder(Integer.parseInt(hrTxt));
		tb.setIsUtc(true);
		testConfigurations(tb, miTxt, seTxt, frTxt, basicHr + "Z", extendedHr + "Z",
				basicHrMin + "Z", extendedHrMin + "Z", basicHrMinSec + "Z", 
				extendedHrMinSec + "Z", basicHrMinSecSs + "Z", 
				extendedHrMinSecSs + "Z");
		
		tb = new IsoUnitTimeBuilder(Integer.parseInt(hrTxt));
		tb.setTimeZoneOffsetHour(Integer.parseInt(
				tzHrTxt.replace('+', ' ').trim()));
		basicHr += tzHrTxt;
		extendedHr += tzHrTxt;
		basicHrMin += tzHrTxt;
		extendedHrMin += tzHrTxt;
		basicHrMinSec += tzHrTxt;
		extendedHrMinSec += tzHrTxt;
		basicHrMinSecSs += tzHrTxt;
		extendedHrMinSecSs += tzHrTxt;
		/*
		 * testConfigurations(tb, miTxt, seTxt, frTxt, basicHr, extendedHr,
				basicHrMin, extendedHrMin, basicHrMinSec, 
				extendedHrMinSec, basicHrMinSecSs, 
				extendedHrMinSecSs);
				*/
		testConfigurations(tb, miTxt, seTxt, frTxt, basicHr + "00",
				extendedHr + ":00", basicHrMin + "00", 
				extendedHrMin + ":00", basicHrMinSec + "00",
				extendedHrMinSec + ":00", basicHrMinSecSs + "00",
				extendedHrMinSecSs + ":00");
				//*/
		tb = new IsoUnitTimeBuilder(Integer.parseInt(hrTxt));
		tb.setTimeZoneOffsetHour(Integer.parseInt(
				tzHrTxt.replace('+', ' ').trim()));
		tb.setTimeZoneOffsetMinute(Integer.parseInt(tzMinTxt));
		basicHr += tzMinTxt;
		extendedHr += ":"+ tzMinTxt;
		basicHrMin += tzMinTxt;
		extendedHrMin += ":"+ tzMinTxt;
		basicHrMinSec += tzMinTxt;
		extendedHrMinSec += ":" + tzMinTxt;
		basicHrMinSecSs += tzMinTxt;
		extendedHrMinSecSs +=":" + tzMinTxt;
		testConfigurations(tb, miTxt, seTxt, frTxt, basicHr, extendedHr,
				basicHrMin, extendedHrMin, basicHrMinSec, 
				extendedHrMinSec, basicHrMinSecSs, 
				extendedHrMinSecSs);
	}
	
	private void testConfigurations(IsoUnitTimeBuilder tb, String miTxt,
			String seTxt, String frTxt,	String basicHr,
			String extendedHr, String basicHrMin, String extendedHrMin,
			String basicHrMinSec, String extendedHrMinSec, 
			String basicHrMinSecSs, String extendedHrMinSecSs) {
		testConfigurations(new Iso8601UnitTime(tb), basicHr, extendedHr);

		tb.setMinute(Integer.parseInt(miTxt));
		testConfigurations(new Iso8601UnitTime(tb), basicHrMin,
				extendedHrMin);

		tb.setSecond(Integer.parseInt(seTxt));
		testConfigurations(new Iso8601UnitTime(tb), basicHrMinSec,
				extendedHrMinSec);

		BigDecimal bd = new BigDecimal(frTxt);
		tb.setSubsecond(bd.unscaledValue().intValue());
		long unitDivisor = BigDecimal.TEN.pow(bd.scale()).longValue();
		tb.setUnit(TimeUnit.SECOND.divide(unitDivisor));
		testConfigurations(new Iso8601UnitTime(tb), basicHrMinSecSs,
				extendedHrMinSecSs);
	}
	
	public void testConfigurations(Iso8601UnitTime t, String basicAns, 
			String extendedAns) {
		FormatOptions fo = new FormatOptions();
		fo.setExtended(false);
		fo.setIncludeTzMinIfZero(true);
		fo.setPrecedeWithT(false);
		
		Iso8601TimeFormatter tf = new Iso8601TimeFormatter(fo);
		assertEquals(tf.format(t), basicAns);
		
		fo.setPrecedeWithT(true);
		tf = new Iso8601TimeFormatter(fo);
		assertEquals(tf.format(t), "T" + basicAns);
		
		fo.setPrecedeWithT(false);
		fo.setExtended(true);
		tf = new Iso8601TimeFormatter(fo);
		assertEquals(tf.format(t), extendedAns);
		
		fo.setPrecedeWithT(true);
		tf = new Iso8601TimeFormatter(fo);
		assertEquals(tf.format(t), "T" + extendedAns);
	}

	public void testHour(String hrTxt, FormatOptions fo, String basicHr,
			String extendedHr, String tzHrTxt, String tzMinTxt) {
		fo.setExtended(false);
		fo.setIncludeTzMinIfZero(false);
		fo.setPrecedeWithT(false);
		IsoUnitTimeBuilder tb = new IsoUnitTimeBuilder(
				Integer.parseInt(hrTxt));
		Iso8601UnitTime t1 = new Iso8601UnitTime(tb);
		Iso8601TimeFormatter tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), basicHr);
		fo.setPrecedeWithT(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), "T" + basicHr);
		
		fo.setPrecedeWithT(false);
		fo.setExtended(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), extendedHr);
		fo.setPrecedeWithT(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), "T" + extendedHr);

		fo.setExtended(false);
		fo.setIncludeTzMinIfZero(false);
		fo.setPrecedeWithT(false);
		tb.setIsUtc(true);
		t1 = new Iso8601UnitTime(tb);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), basicHr + "Z");
		
		fo.setExtended(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), extendedHr + "Z");
		
		tb.setIsUtc(false);
		tb.setTimeZoneOffsetHour(Integer.parseInt(
				tzHrTxt.replace('+', ' ').trim()));
		t1 = new Iso8601UnitTime(tb);
		assertEquals(tf1.format(t1), extendedHr + tzHrTxt);
		
		tb.setTimeZoneOffsetMinute(Integer.parseInt(tzMinTxt));
		t1 = new Iso8601UnitTime(tb);
		if (tzMinTxt.equals("00")) {
			assertEquals(tf1.format(t1), extendedHr + tzHrTxt);
			fo.setIncludeTzMinIfZero(true);
			tf1 = new Iso8601TimeFormatter(fo);
			assertEquals(tf1.format(t1), extendedHr + tzHrTxt + ":" 
					+ tzMinTxt);
		} else {
			assertEquals(tf1.format(t1), extendedHr + tzHrTxt + 
					":" + tzMinTxt);
		}
		
		
	}
	
	public void testMinute(String hrTxt, String miTxt, FormatOptions fo, 
			String basicHrMin, String extendedHrMin) {
		fo.setExtended(false);
		fo.setIncludeTzMinIfZero(false);
		fo.setPrecedeWithT(false);
		IsoUnitTimeBuilder tb = new IsoUnitTimeBuilder(
				Integer.parseInt(hrTxt));
		tb.setMinute(Integer.parseInt(miTxt));
		Iso8601UnitTime t1 = new Iso8601UnitTime(tb);
		Iso8601TimeFormatter tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), basicHrMin);
		fo.setPrecedeWithT(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), "T" + basicHrMin);
		
		fo.setPrecedeWithT(false);
		fo.setExtended(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), extendedHrMin);
		fo.setPrecedeWithT(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), "T" + extendedHrMin);
		
		fo.setExtended(false);
		fo.setIncludeTzMinIfZero(false);
		fo.setPrecedeWithT(false);
		tb.setIsUtc(true);
		t1 = new Iso8601UnitTime(tb);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), basicHrMin + "Z");
		
		fo.setExtended(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), extendedHrMin + "Z");
	}

	public void testSecond(String hrTxt, String miTxt, String seTxt, 
			FormatOptions fo, String basicHrMinSec, String extendedHrMinSec) {
		fo.setExtended(false);
		fo.setIncludeTzMinIfZero(false);
		fo.setPrecedeWithT(false);
		IsoUnitTimeBuilder tb = new IsoUnitTimeBuilder(
				Integer.parseInt(hrTxt));
		tb.setMinute(Integer.parseInt(miTxt));
		tb.setSecond(Integer.parseInt(seTxt));
		Iso8601UnitTime t1 = new Iso8601UnitTime(tb);
		Iso8601TimeFormatter tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), basicHrMinSec);
		fo.setPrecedeWithT(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), "T" + basicHrMinSec);
		
		fo.setPrecedeWithT(false);
		fo.setExtended(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), extendedHrMinSec);
		fo.setPrecedeWithT(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), "T" + extendedHrMinSec);
		
		fo.setExtended(false);
		fo.setIncludeTzMinIfZero(false);
		fo.setPrecedeWithT(false);
		tb.setIsUtc(true);
		t1 = new Iso8601UnitTime(tb);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), basicHrMinSec + "Z");
		
		fo.setExtended(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), extendedHrMinSec + "Z");
	}
	
	public void testSubsecond(String hrTxt, String miTxt, String seTxt, 
			String frTxt, FormatOptions fo, String basicHrMinSecSs, 
			String extendedHrMinSecSs) {
		fo.setExtended(false);
		fo.setIncludeTzMinIfZero(false);
		fo.setPrecedeWithT(false);
		IsoUnitTimeBuilder tb = new IsoUnitTimeBuilder(
				Integer.parseInt(hrTxt));
		tb.setMinute(Integer.parseInt(miTxt));
		tb.setSecond(Integer.parseInt(seTxt));
		BigDecimal bd = new BigDecimal(frTxt);
		tb.setSubsecond(bd.unscaledValue().intValue());
		long unitDivisor = BigDecimal.TEN.pow(bd.scale()).longValue();
		tb.setUnit(TimeUnit.SECOND.divide(unitDivisor));
		
		Iso8601UnitTime t1 = new Iso8601UnitTime(tb);
		Iso8601TimeFormatter tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), basicHrMinSecSs);
		fo.setPrecedeWithT(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), "T" + basicHrMinSecSs);
		
		fo.setPrecedeWithT(false);
		fo.setExtended(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), extendedHrMinSecSs);
		fo.setPrecedeWithT(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), "T" + extendedHrMinSecSs);
		
		fo.setExtended(false);
		fo.setIncludeTzMinIfZero(false);
		fo.setPrecedeWithT(false);
		tb.setIsUtc(true);
		t1 = new Iso8601UnitTime(tb);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), basicHrMinSecSs + "Z");
		
		fo.setExtended(true);
		tf1 = new Iso8601TimeFormatter(fo);
		assertEquals(tf1.format(t1), extendedHrMinSecSs + "Z");
	}
}
