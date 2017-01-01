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

import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Duration;
import javax.measure.unit.Unit;


public class Iso8601TimeFormatter {
	StringBuilder formattedTime;
	FormatOptions options;

	public static class FormatOptions implements Cloneable {
		boolean extended;
		boolean precedeWithT;
		boolean includeTzMinIfZero;
		boolean includeTz;
		boolean includeDefaultTzIfNull;
		
		public FormatOptions() {
		}

		public void setExtended(boolean extended) {
			this. extended = extended;
		}
		
		public boolean isExtended() {
			return extended;
		}
		
		public void setPrecedeWithT(boolean precedeWithT) {
			this.precedeWithT = precedeWithT;
		}
		
		public boolean isPrecedeWithT() {
			return precedeWithT;
		}
		
		public void setIncludeTzMinIfZero(boolean includeTzMinIfZero) {
			this.includeTzMinIfZero = includeTzMinIfZero;
		}
		
		public boolean includeTzMinIfZero() {
			return includeTzMinIfZero;
		}
		
		public Object clone() {
			try {
				return super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 *	Creates a formatter with the following default options:
	 *		precede with 'T': true
	 *		include time zone minutes if zero: true
	 *		extended format: true
	 */
	public Iso8601TimeFormatter() {
		formattedTime = new StringBuilder();
		options = new FormatOptions();
		options.setExtended(true);
		options.setIncludeTzMinIfZero(true);
		options.setPrecedeWithT(true);
	}
	
	public Iso8601TimeFormatter(FormatOptions options) {
		this. options = options;
		formattedTime = new StringBuilder();
	}
	
	public String format(Iso8601Time t) {
		formattedTime.setLength(0);
		if (options.isPrecedeWithT()) {
			formattedTime.append('T');
		}
		addIntegerComponent(t.getHour());
		
		if (t instanceof Iso8601UnitTime) {
			Iso8601UnitTime tu = (Iso8601UnitTime)t;
			Unit<Duration> u = tu.getUnit();
			if (u.equals(TimeUnit.HOUR)) {
				
			} else if (u.equals(TimeUnit.MINUTE)) {
				addMinute(t);
			} else if (u.equals(TimeUnit.SECOND)) {
				addMinute(t);
				addSecond(tu);
			} else {
				addMinute(t);
				addSecond(tu);
				addSubsecond(tu);
			}
		} else {
			Iso8601FractionalTime tf = (Iso8601FractionalTime)t;
			if (tf.getFractionalHour() != null) {
				formattedTime.append(tf.getFractionalHour());
			} else if (tf.getFractionalMinute() != null) {
				formattedTime.append(tf.getFractionalMinute());
			}
		}
		
		addTimeZone(t);
		
		
		return formattedTime.toString();
	}

	protected void addSecond(Iso8601UnitTime tu) {
		if (options.isExtended()) {
			formattedTime.append(":");
		}
		addIntegerComponent(tu.getSecond());
	}

	protected void addMinute(Iso8601Time t) {
		if (options.isExtended()) {
			formattedTime.append(":");
		}
		addIntegerComponent(t.getMinute());
	}
	
	protected void addSubsecond(Iso8601UnitTime tu) {
		Unit<Duration> u = tu.getUnit();
		UnitConverter uc = TimeUnit.SECOND.getConverterTo(u);
		double conv = uc.convert(1D);
		int pow10 = (int)(Math.log10(conv));
		BigDecimal bd = BigDecimal.valueOf((long)tu.getSubsecond(), pow10);
		formattedTime.append(bd.toPlainString().substring(1));
	}

	private void addIntegerComponent(int i) {
		if (i < 0) {
			formattedTime.append("-");
			i = -i;
		}
		if (i<10) {
			formattedTime.append('0');
		}
		formattedTime.append(i);
	}
	
	protected void addTimeZone(Iso8601Time t) {
		if (t.isUtcTimeZone()) {
			formattedTime.append('Z');
		} else if (t.isTimeZoneSpecified()) {
			Iso8601TimeZoneFormatter.FormatOptions fo = 
					new Iso8601TimeZoneFormatter.FormatOptions(
							options.extended, options.includeTzMinIfZero);
			formattedTime.append(
					Iso8601TimeZoneFormatter.formatTimeZone(
							t.getTimeZoneHourOffset(), t.getTimeZoneMinuteOffset(), fo));
		}
	}
}
