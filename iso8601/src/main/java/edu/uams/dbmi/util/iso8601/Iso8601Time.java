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

/**
 * A key ambiguity in the standard is what a fractional hour or minute
 * 	represents.  For example, does 12.5 represent the minute 12:30, the
 *  second 12:30:00, the millisecond 12:30:00.000, or one tenth of an
 *  hour (from 12.5 to 12.599999999...), which is 6 minutes (and thus
 *  would be the interval from 12:30:00.000 to 12:35:59.999...). In the 
 *  latter case, would 12.50 represent an interval 1/100th of an hour?
 *  Which is 0.6 minutes or 36 seconds or 12:30:00.000 to 12:30:35.999...).
 *  
 *  So, fractional times are a real problem, except for fractional seconds,
 *  where we can say what is represented is a decisecond, a centisecond, or
 *  a millisecond, depending on the precision (out to 3 digits).
 *  
 *  Thus, we need to distinguish objects that represent actual intervals
 *  vs. those that are ambiguous.  Key question next is whether we use
 *  two classes to do it, or use a single flag in this class.
 *  
 *  Also, note that if no time zone is specified, then there is also much
 *  	ambiguity: the time object could represent one of ~41 times (all
 *  	the time zones across the world end up with ~41 unique offsets 
 *  	from UTC, including UTC (offset is zero) itself).
 *  
 *  THIS CLASS ASSUMES UTC WHEN NO TIME ZONE IS SPECIFIED.  Sorry folks, 
 *  	I just can't brook the ambiguity.  Don't be ambiguous, and you'll
 *  	never encounter unanticipated behavior!
 */
public abstract class Iso8601Time {
	Integer hr;
	Integer mi;

	int tzHr;
	int tzMin;
	
	boolean isUTC;
	boolean hasTz;
	
	public Iso8601Time(long timeInMillis) {
		int hr = (int) (timeInMillis / 3600000L);
		int mi = (int) ((timeInMillis % 3600000L) / 60000L);
		IsoTimeBuilder b = new IsoUnitTimeBuilder(hr);
		b.setMinute(mi);
		initialize(b);
	}
	
	public Iso8601Time(int hr) {
		IsoTimeBuilder b = new IsoUnitTimeBuilder(hr);
		initialize(b);
	}
	
	public Iso8601Time(int hr, int min) {
		IsoTimeBuilder b = new IsoUnitTimeBuilder(hr);
		b.setMinute(min);
		initialize(b);
	}
	
	public Iso8601Time(IsoTimeBuilder b) {
		initialize(b);
	}

	private void initialize(IsoTimeBuilder b) {
		setHour(b);
		if (b.min != null) {
			setMinute(b);
		}

		hasTz = (b.isUtc || b.tzHr != 0 || b.tzMin != 0);
		isUTC = b.isUtc;
		if (hasTz) {
			setTzHour(b);
			setTzMinute(b);
		}  else {
			tzHr = 0;
			tzMin = 0;
		}
	}

	private void setTzMinute(IsoTimeBuilder b) {
		if (checkTzMin(b.tzMin)) {
			this. tzMin = b.tzMin;
		} else {
			throw new IllegalArgumentException("Time zone minute offset " +
					"must be between 0 and 59, inclusive.");
		}
	}

	public boolean checkTzMin(int min) {
		return (min > - 1 && min < 60);
	}

	private void setTzHour(IsoTimeBuilder b) {
		if (checkTzHour(b.tzHr)) {
			this. tzHr = b.tzHr;
		} else {
			throw new IllegalArgumentException("Time zone hour offset " +
					"must be between -12 and +14 inclusive.");
		}
	}
	
	private boolean checkTzHour(int hr) {
		return (hr >= -12 && hr <= 14);
	}

	private void setMinute(IsoTimeBuilder b) {
		if (!checkMinute(b.min)) {
			throw new IllegalArgumentException("Minute must be between 0" +
					" and 59, inclusive");
		}
		this. mi = b.min;
	}
	
	private boolean checkMinute(int min) {
		return ((hr < 24 && min > -1 && min < 60) ||
				(hr == 24 && min == 0));
	}

	private void setHour(IsoTimeBuilder b) {
		/*
		 * ISO8601 allows hour == 24, where it represents 00:00 of the 
		 *  	following day (i.e., midnight tonight, 2011-10-28, would
		 *  	be 2011-10-28T24:00:00 a.k.a. 2011-10-29T00:00:00).
		 */
		if (b.hr < 0 || b.hr > 24) {
			throw new  IllegalArgumentException("Hour must be between 0 and " +
					"24, inclusive");
		}
		this. hr = b.hr;
	}
	
	public int getHour() {
		return hr;
	}
	
	public int getMinute() {
		/*
		 * Will throw a NullPointerException if mi is not set.
		 */
		return mi;
	}
	
	public boolean isTimeZoneSpecified() {
		return hasTz;
	}
	
	public int getTimeZoneHourOffset() {
		return tzHr;
	}
	
	public int getTimeZoneMinuteOffset() {
		return tzMin;
	}
	
	public boolean isUtcTimeZone() {
		return isUTC;
	}
	
	@Override
	public abstract boolean equals(Object o);
}
