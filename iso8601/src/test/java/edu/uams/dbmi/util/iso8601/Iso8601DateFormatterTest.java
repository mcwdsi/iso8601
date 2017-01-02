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
import edu.uams.dbmi.util.iso8601.Iso8601DateFormatter.FormatOptions;

import junit.framework.TestCase;

public class Iso8601DateFormatterTest extends TestCase {
	
	@Test
	public void testYearMonthDayFormats() {
		Iso8601Date d = new Iso8601Date(DateConfiguration.YEAR_MONTH_DAY,
				2011, 10, 25);
		testYearMonthDaySet(d, "2011", "10", "25");
		
		d = new Iso8601Date(DateConfiguration.YEAR_MONTH_DAY,
				-445, 10, 25);
		testYearMonthDaySet(d, "-0445", "10", "25");

	}
	
	public void testYearMonthDaySet(Iso8601Date d, String yrTxt, String moTxt,
			String daTxt) {
		FormatOptions fo = new FormatOptions();
		testYearMonthDay(d, DateConfiguration.YEAR_MONTH_DAY, fo, yrTxt,
				moTxt, daTxt);
		testYearMonthDay(d, DateConfiguration.YEAR_MONTH, fo, yrTxt,
				moTxt, daTxt);
		testYearMonthDay(d, DateConfiguration.YEAR, fo, yrTxt,
				moTxt, daTxt);
		testYearMonthDay(d, DateConfiguration.CENTURY, fo, yrTxt,
				moTxt, daTxt);	
	}
	
	public void testYearMonthDay(Iso8601Date d, DateConfiguration dc, 
			FormatOptions fo, String yrTxt, String moTxt, String daTxt) {
		
		String basicTxt = "", extendedTxt = "";
		switch (dc) {
			case CENTURY:
				if (d.getYear() > -1)
					basicTxt = extendedTxt = yrTxt.substring(0, 2);
				else 
					basicTxt = extendedTxt = yrTxt.substring(0, 3);
				break;
			case YEAR:
				basicTxt = extendedTxt = yrTxt;
				break;
			case YEAR_MONTH:
				basicTxt = yrTxt + moTxt;
				extendedTxt = yrTxt + "-" + moTxt;
				break;
			case YEAR_MONTH_DAY:
				basicTxt = yrTxt + moTxt + daTxt;
				extendedTxt = yrTxt + "-" + moTxt + "-" + daTxt;
				break;
			default:
				throw new IllegalArgumentException("This method doesn't handle option:  " + dc.toString());
		}
		
		fo.setConfiguration(dc);
		fo.setExtended(false);
		fo.setForceSignedYear(false);
		fo.setYearDigits(4);
		Iso8601DateFormatter df1 = new Iso8601DateFormatter(fo);
		String dTxt = df1.format(d);
		assertEquals(dTxt, basicTxt);
		
		fo.setExtended(true);
		Iso8601DateFormatter df2 = new Iso8601DateFormatter(fo);
		dTxt = df2.format(d);
		assertEquals(dTxt, extendedTxt);
		
		fo.setExtended(false);
		fo.setForceSignedYear(true);
		Iso8601DateFormatter df3 = new Iso8601DateFormatter(fo);
		dTxt = df3.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "+" + basicTxt);
		else
			assertEquals(dTxt, basicTxt);
		
		fo.setExtended(true);
		Iso8601DateFormatter df4 = new Iso8601DateFormatter(fo);
		dTxt = df4.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "+" + extendedTxt);
		else
			assertEquals(dTxt, extendedTxt);	
		
		fo.setExtended(false);
		fo.setForceSignedYear(false);
		fo.setYearDigits(5);
		Iso8601DateFormatter df5 = new Iso8601DateFormatter(fo);
		dTxt = df5.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "0" + basicTxt);
		else 
			assertEquals(dTxt, "-0" + basicTxt.substring(1));
		
		fo.setForceSignedYear(true);
		Iso8601DateFormatter df6 = new Iso8601DateFormatter(fo);
		dTxt = df6.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "+0" + basicTxt);
		else
			assertEquals(dTxt, "-0" + basicTxt.substring(1));
		
		fo.setExtended(true);
		Iso8601DateFormatter df7 = new Iso8601DateFormatter(fo);
		dTxt = df7.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "+0" + extendedTxt);
		else 
			assertEquals(dTxt, "-0" + extendedTxt.substring(1));
	}
	
	@Test
	public void testYearWeekDayFormats() {
		Iso8601Date d1 = new Iso8601Date(DateConfiguration.YEAR_WEEK_DAY, 
				1985, 15, 5);
		testYearWeekDaySet(d1, "1985", "15", "5", "1985");
		
		/*
		 * The date represented in week format as "2009-W01-1 is Dec 29, 2008
		 */
		Iso8601Date d2 = new Iso8601Date(DateConfiguration.YEAR_WEEK_DAY, 
				2009, 1, 1);
		testYearWeekDaySet(d2, "2009", "01", "1", "2008");
		
		/*
		 * The date represented in week format as "2009-W53-7" is January 3,
		 * 	2010.
		 */
		Iso8601Date d3 = new Iso8601Date(DateConfiguration.YEAR_WEEK_DAY,
				2009, 53, 7);
		testYearWeekDaySet(d3, "2009", "53", "7", "2010");
				
	}
	
	public void testYearWeekDaySet(Iso8601Date d, String wkCalYrTxt, String weTxt,
			String daTxt, String yrTxt) {
		FormatOptions fo = new FormatOptions();
		testYearWeekDay(d, DateConfiguration.YEAR_WEEK_DAY, fo, wkCalYrTxt,
				weTxt, daTxt, yrTxt);
		testYearWeekDay(d, DateConfiguration.YEAR_WEEK, fo, wkCalYrTxt,
				weTxt, daTxt, yrTxt);
		testYearWeekDay(d, DateConfiguration.YEAR, fo, wkCalYrTxt,
				weTxt, daTxt, yrTxt);
		testYearWeekDay(d, DateConfiguration.CENTURY, fo, wkCalYrTxt,
				weTxt, daTxt, yrTxt);	
	}
	
	public void testYearWeekDay(Iso8601Date d, DateConfiguration dc, 
			FormatOptions fo, String wkCalYrTxt, String weTxt, String daTxt,
			String yrTxt) {
		String basicTxt = "", extendedTxt = "";
		switch (dc) {
			case CENTURY:
				if (d.getYear() > -1)
					basicTxt = extendedTxt = yrTxt.substring(0, 2);
				else 
					basicTxt = extendedTxt = yrTxt.substring(0, 3);
				break;
			case YEAR:
				basicTxt = extendedTxt = yrTxt;
				break;
			case YEAR_WEEK:
				basicTxt = wkCalYrTxt + "W" + weTxt;
				extendedTxt = wkCalYrTxt + "-W" + weTxt;
				break;
			case YEAR_WEEK_DAY:
				basicTxt = wkCalYrTxt + "W" + weTxt + daTxt;
				extendedTxt = wkCalYrTxt + "-W" + weTxt + "-" + daTxt;
				break;
			default:
				throw new IllegalArgumentException("This method doesn't handle option:  " + dc.toString());
		}
		
		fo.setConfiguration(dc);
		fo.setExtended(false);
		fo.setForceSignedYear(false);
		fo.setYearDigits(4);
		Iso8601DateFormatter df1 = new Iso8601DateFormatter(fo);
		String dTxt = df1.format(d);
		assertEquals(dTxt, basicTxt);
		
		fo.setExtended(true);
		Iso8601DateFormatter df2 = new Iso8601DateFormatter(fo);
		dTxt = df2.format(d);
		assertEquals(dTxt, extendedTxt);
		
		fo.setExtended(false);
		fo.setForceSignedYear(true);
		Iso8601DateFormatter df3 = new Iso8601DateFormatter(fo);
		dTxt = df3.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "+" + basicTxt);
		else
			assertEquals(dTxt, basicTxt);
		
		fo.setExtended(true);
		Iso8601DateFormatter df4 = new Iso8601DateFormatter(fo);
		dTxt = df4.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "+" + extendedTxt);
		else
			assertEquals(dTxt, extendedTxt);	
		
		fo.setExtended(false);
		fo.setForceSignedYear(false);
		fo.setYearDigits(5);
		Iso8601DateFormatter df5 = new Iso8601DateFormatter(fo);
		dTxt = df5.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "0" + basicTxt);
		else 
			assertEquals(dTxt, "-0" + basicTxt.substring(1));
		
		fo.setForceSignedYear(true);
		Iso8601DateFormatter df6 = new Iso8601DateFormatter(fo);
		dTxt = df6.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "+0" + basicTxt);
		else
			assertEquals(dTxt, "-0" + basicTxt.substring(1));
		
		fo.setExtended(true);
		Iso8601DateFormatter df7 = new Iso8601DateFormatter(fo);
		dTxt = df7.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "+0" + extendedTxt);
		else 
			assertEquals(dTxt, "-0" + extendedTxt.substring(1));		
		
	}
	
	public void testYearDayOfYearFormat() {
		Iso8601Date d1 = new Iso8601Date(DateConfiguration.YEAR_DAY_OF_YEAR,
				1985, 102);
		testYearDayOfYearSet(d1, "1985", "102");
		
	}
	
	public void testYearDayOfYearSet(Iso8601Date d, String yrTxt, 
			String dyTxt) {
		FormatOptions fo = new FormatOptions();
		testYearDayOfYear(d, DateConfiguration.YEAR_DAY_OF_YEAR, fo, yrTxt,
				dyTxt);
		testYearDayOfYear(d, DateConfiguration.YEAR, fo, yrTxt,
				dyTxt);
		testYearDayOfYear(d, DateConfiguration.CENTURY, fo, yrTxt,
				dyTxt);
	}

	private void testYearDayOfYear(Iso8601Date d, DateConfiguration dc, 
			FormatOptions fo, String yrTxt,	String dyTxt) {
		String basicTxt = "", extendedTxt = "";
		switch (dc) {
			case CENTURY:
				if (d.getYear() > -1)
					basicTxt = extendedTxt = yrTxt.substring(0, 2);
				else 
					basicTxt = extendedTxt = yrTxt.substring(0, 3);
				break;
			case YEAR:
				basicTxt = extendedTxt = yrTxt;
				break;
			case YEAR_DAY_OF_YEAR:
				basicTxt = yrTxt + dyTxt;
				extendedTxt = yrTxt + "-" + dyTxt;
				break;
			default:
				throw new IllegalArgumentException("This method doesn't handle option:  " + dc.toString());
		}
		
		fo.setConfiguration(dc);
		fo.setExtended(false);
		fo.setForceSignedYear(false);
		fo.setYearDigits(4);
		Iso8601DateFormatter df1 = new Iso8601DateFormatter(fo);
		String dTxt = df1.format(d);
		assertEquals(dTxt, basicTxt);
		
		fo.setExtended(true);
		Iso8601DateFormatter df2 = new Iso8601DateFormatter(fo);
		dTxt = df2.format(d);
		assertEquals(dTxt, extendedTxt);
		
		fo.setExtended(false);
		fo.setForceSignedYear(true);
		Iso8601DateFormatter df3 = new Iso8601DateFormatter(fo);
		dTxt = df3.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "+" + basicTxt);
		else
			assertEquals(dTxt, basicTxt);
		
		fo.setExtended(true);
		Iso8601DateFormatter df4 = new Iso8601DateFormatter(fo);
		dTxt = df4.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "+" + extendedTxt);
		else
			assertEquals(dTxt, extendedTxt);	
		
		fo.setExtended(false);
		fo.setForceSignedYear(false);
		fo.setYearDigits(5);
		Iso8601DateFormatter df5 = new Iso8601DateFormatter(fo);
		dTxt = df5.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "0" + basicTxt);
		else 
			assertEquals(dTxt, "-0" + basicTxt.substring(1));
		
		fo.setForceSignedYear(true);
		Iso8601DateFormatter df6 = new Iso8601DateFormatter(fo);
		dTxt = df6.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "+0" + basicTxt);
		else
			assertEquals(dTxt, "-0" + basicTxt.substring(1));
		
		fo.setExtended(true);
		Iso8601DateFormatter df7 = new Iso8601DateFormatter(fo);
		dTxt = df7.format(d);
		if (d.getYear() > -1)
			assertEquals(dTxt, "+0" + extendedTxt);
		else 
			assertEquals(dTxt, "-0" + extendedTxt.substring(1));		
		
	}
}
