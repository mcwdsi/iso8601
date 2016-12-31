package edu.uams.dbmi.util.iso8601;


public class Iso8601TimeZoneFormatter {
	
	public static class FormatOptions implements Cloneable {
		boolean extended;
		boolean includeMinutesIfZero;

		public FormatOptions(boolean extended, boolean includeMinutesIfZero) {
			this.extended = extended;
			this.includeMinutesIfZero = includeMinutesIfZero;
		}
		
	}

		public static String formatTimeZone(int hourOffset, int minuteOffset) {
			FormatOptions fo = new FormatOptions(true, true);
			return formatTimeZone(hourOffset, minuteOffset, fo);
		}
		
		public static String formatTimeZone(int hourOffset, int minuteOffset, FormatOptions fo) {
			StringBuilder sb = new StringBuilder();
			if (hourOffset < -24 || hourOffset > 24) {
				throw new IllegalArgumentException("hour offset must be between -24 and 24 [" + hourOffset + "]");
			}
			/*
			 * really, it's only ever 0, 30, and 45.  But if we're going to enforce that somewhere, the best place
			 *  is elsewhere.
			 */
			if (minuteOffset < 0 || minuteOffset > 59) {
				throw new IllegalArgumentException("minute offset must be between 0 and 59 [" + minuteOffset + "]");
			}
			if (hourOffset<0) {
				sb.append("-");
				hourOffset = -hourOffset;
			} else { 
				sb.append("+");
			}
			
			//we already made sure it's a legitimate number and turned negative values positive
			if (hourOffset < 10) {
				sb.append("0");
			}
			sb.append(Integer.toString(hourOffset));
			
		
			if (fo.includeMinutesIfZero || minuteOffset != 0) {
				//only include colon if we're using extended format
				if (fo.extended) {
					sb.append(":");
				}
				if (minuteOffset < 10) {
					sb.append("0");
				}
				sb.append(Integer.toString(minuteOffset));
			}
			
			return sb.toString();
		}
		
		public static String formatTimeZone(int offsetInMillis) {
			long offsetHour = offsetInMillis / 3600000L;
			long offsetMinutes = (offsetInMillis % 3600000L) / 60000L;
			if (offsetMinutes < 0) offsetMinutes = -offsetMinutes;
			return formatTimeZone((int)offsetHour, (int)offsetMinutes);
		}
	
	
}
