package edu.uams.dbmi.util.iso8601;

import org.junit.Test;

import junit.framework.TestCase;

public class Iso8601TimeZoneFormatterTest extends TestCase {
	
	@Test
	public static void testFormatNegativeHourZeroMinute() {
		int hourOffset = -5;
		int minuteOffset = 0;
		
		String formatted1 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset);
		assertEquals(formatted1, "-05:00");
		Iso8601TimeZoneFormatter.FormatOptions fo = new Iso8601TimeZoneFormatter.FormatOptions(true, true);
		String formatted2 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo);
		assertEquals(formatted2, "-05:00");
		Iso8601TimeZoneFormatter.FormatOptions fo2 = new Iso8601TimeZoneFormatter.FormatOptions(true, false);
		String formatted3 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo2);
		assertEquals(formatted3, "-05");
		Iso8601TimeZoneFormatter.FormatOptions fo3 = new Iso8601TimeZoneFormatter.FormatOptions(false, false);
		String formatted4 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo3);
		assertEquals(formatted4, "-05");
		Iso8601TimeZoneFormatter.FormatOptions fo4 = new Iso8601TimeZoneFormatter.FormatOptions(false, true);
		String formatted5 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo4);
		assertEquals(formatted5, "-0500");
		
		hourOffset = -17;
		String formatted6 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset);
		assertEquals(formatted6, "-17:00");
		Iso8601TimeZoneFormatter.FormatOptions fo5 = new Iso8601TimeZoneFormatter.FormatOptions(true, true);
		String formatted7 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo5);
		assertEquals(formatted7, "-17:00");
		Iso8601TimeZoneFormatter.FormatOptions fo6 = new Iso8601TimeZoneFormatter.FormatOptions(true, false);
		String formatted8 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo6);
		assertEquals(formatted8, "-17");
		Iso8601TimeZoneFormatter.FormatOptions fo7 = new Iso8601TimeZoneFormatter.FormatOptions(false, false);
		String formatted9 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo7);
		assertEquals(formatted9, "-17");
		Iso8601TimeZoneFormatter.FormatOptions fo8 = new Iso8601TimeZoneFormatter.FormatOptions(false, true);
		String formatted10 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo8);
		assertEquals(formatted10, "-1700");
		
	}
	
	@Test
	public static void testFormatPositiveHourZeroMinute() {
		int hourOffset = 5;
		int minuteOffset = 0;
		
		String formatted1 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset);
		assertEquals(formatted1, "+05:00");
		Iso8601TimeZoneFormatter.FormatOptions fo = new Iso8601TimeZoneFormatter.FormatOptions(true, true);
		String formatted2 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo);
		assertEquals(formatted2, "+05:00");
		Iso8601TimeZoneFormatter.FormatOptions fo2 = new Iso8601TimeZoneFormatter.FormatOptions(true, false);
		String formatted3 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo2);
		assertEquals(formatted3, "+05");
		Iso8601TimeZoneFormatter.FormatOptions fo3 = new Iso8601TimeZoneFormatter.FormatOptions(false, false);
		String formatted4 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo3);
		assertEquals(formatted4, "+05");
		Iso8601TimeZoneFormatter.FormatOptions fo4 = new Iso8601TimeZoneFormatter.FormatOptions(false, true);
		String formatted5 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo4);
		assertEquals(formatted5, "+0500");
		
		hourOffset = 17;
		String formatted6 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset);
		assertEquals(formatted6, "+17:00");
		Iso8601TimeZoneFormatter.FormatOptions fo5 = new Iso8601TimeZoneFormatter.FormatOptions(true, true);
		String formatted7 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo5);
		assertEquals(formatted7, "+17:00");
		Iso8601TimeZoneFormatter.FormatOptions fo6 = new Iso8601TimeZoneFormatter.FormatOptions(true, false);
		String formatted8 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo6);
		assertEquals(formatted8, "+17");
		Iso8601TimeZoneFormatter.FormatOptions fo7 = new Iso8601TimeZoneFormatter.FormatOptions(false, false);
		String formatted9 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo7);
		assertEquals(formatted9, "+17");
		Iso8601TimeZoneFormatter.FormatOptions fo8 = new Iso8601TimeZoneFormatter.FormatOptions(false, true);
		String formatted10 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo8);
		assertEquals(formatted10, "+1700");
	}
	
	@Test
	public static void testFormatNegativeHour30Minute() {
		int hourOffset = -5;
		int minuteOffset = 30;
		
		String formatted1 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset);
		assertEquals(formatted1, "-05:30");
		Iso8601TimeZoneFormatter.FormatOptions fo = new Iso8601TimeZoneFormatter.FormatOptions(true, true);
		String formatted2 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo);
		assertEquals(formatted2, "-05:30");
		Iso8601TimeZoneFormatter.FormatOptions fo2 = new Iso8601TimeZoneFormatter.FormatOptions(true, false);
		String formatted3 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo2);
		assertEquals(formatted3, "-05:30");
		Iso8601TimeZoneFormatter.FormatOptions fo3 = new Iso8601TimeZoneFormatter.FormatOptions(false, false);
		String formatted4 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo3);
		assertEquals(formatted4, "-0530");
		Iso8601TimeZoneFormatter.FormatOptions fo4 = new Iso8601TimeZoneFormatter.FormatOptions(false, true);
		String formatted5 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo4);
		assertEquals(formatted5, "-0530");
		
		hourOffset = -17;
		String formatted6 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset);
		assertEquals(formatted6, "-17:30");
		Iso8601TimeZoneFormatter.FormatOptions fo5 = new Iso8601TimeZoneFormatter.FormatOptions(true, true);
		String formatted7 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo5);
		assertEquals(formatted7, "-17:30");
		Iso8601TimeZoneFormatter.FormatOptions fo6 = new Iso8601TimeZoneFormatter.FormatOptions(true, false);
		String formatted8 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo6);
		assertEquals(formatted8, "-17:30");
		Iso8601TimeZoneFormatter.FormatOptions fo7 = new Iso8601TimeZoneFormatter.FormatOptions(false, false);
		String formatted9 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo7);
		assertEquals(formatted9, "-1730");
		Iso8601TimeZoneFormatter.FormatOptions fo8 = new Iso8601TimeZoneFormatter.FormatOptions(false, true);
		String formatted10 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo8);
		assertEquals(formatted10, "-1730");		
	}
	
	@Test
	public static void testFormatPositiveHour30Minute() {
		int hourOffset = 5;
		int minuteOffset = 30;
		
		String formatted1 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset);
		assertEquals(formatted1, "+05:30");
		Iso8601TimeZoneFormatter.FormatOptions fo = new Iso8601TimeZoneFormatter.FormatOptions(true, true);
		String formatted2 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo);
		assertEquals(formatted2, "+05:30");
		Iso8601TimeZoneFormatter.FormatOptions fo2 = new Iso8601TimeZoneFormatter.FormatOptions(true, false);
		String formatted3 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo2);
		assertEquals(formatted3, "+05:30");
		Iso8601TimeZoneFormatter.FormatOptions fo3 = new Iso8601TimeZoneFormatter.FormatOptions(false, false);
		String formatted4 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo3);
		assertEquals(formatted4, "+0530");
		Iso8601TimeZoneFormatter.FormatOptions fo4 = new Iso8601TimeZoneFormatter.FormatOptions(false, true);
		String formatted5 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo4);
		assertEquals(formatted5, "+0530");
		
		hourOffset = 17;
		String formatted6 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset);
		assertEquals(formatted6, "+17:30");
		Iso8601TimeZoneFormatter.FormatOptions fo5 = new Iso8601TimeZoneFormatter.FormatOptions(true, true);
		String formatted7 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo5);
		assertEquals(formatted7, "+17:30");
		Iso8601TimeZoneFormatter.FormatOptions fo6 = new Iso8601TimeZoneFormatter.FormatOptions(true, false);
		String formatted8 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo6);
		assertEquals(formatted8, "+17:30");
		Iso8601TimeZoneFormatter.FormatOptions fo7 = new Iso8601TimeZoneFormatter.FormatOptions(false, false);
		String formatted9 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo7);
		assertEquals(formatted9, "+1730");
		Iso8601TimeZoneFormatter.FormatOptions fo8 = new Iso8601TimeZoneFormatter.FormatOptions(false, true);
		String formatted10 = Iso8601TimeZoneFormatter.formatTimeZone(hourOffset, minuteOffset, fo8);
		assertEquals(formatted10, "+1730");		
	}
	
	
}
