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

import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Duration;
import javax.measure.unit.Unit;

public class Iso8601UnitTime extends Iso8601Time {

	Integer se;
	
	Unit<Duration> u;
	int subSecond;
	
	public Iso8601UnitTime(long tmillis) {
		super(tmillis);
		setSecond((int)((tmillis % 60000L)/1000L));
		setUnitAndSubsecond(TimeUnit.MILLISECOND, (int)(tmillis%1000L));
		u=TimeUnit.MILLISECOND;
	}
	
	public Iso8601UnitTime(int hr) {
		super(hr);
		u = TimeUnit.HOUR;
	}
	
	public Iso8601UnitTime(int hr, int min) {
		super(hr, min);
		u = TimeUnit.MINUTE;
	}
	
	public Iso8601UnitTime(int hr, int min, int sec) {
		super(hr, min);
		setSecond(sec);
		u = TimeUnit.SECOND;
	}
	
	public Iso8601UnitTime(int hr, int min, int sec, Unit<Duration> u, int subsec) {
		super(hr, min);
		setSecond(sec);
		setUnitAndSubsecond(u, subsec);
	}
	
	public Iso8601UnitTime(IsoUnitTimeBuilder b) {
		super(b);
		if (b.getUnit() != null) {
			setUnitAndSubsecond(b.getUnit(), b.getSubsecond());
			setSecond(b.se);
		} else {
			if (b.se != null) {
				//System.out.println("Setting unit to SECOND.");
				this.u = TimeUnit.SECOND;
				setSecond(b.se);
			} else if (b.min != null) {
				//System.out.println("Setting unit to MINUTE.");
				this.u = TimeUnit.MINUTE;
			} else {
				//System.out.println("Setting unit to HOUR.");
				u = TimeUnit.HOUR;
			}
		}
	}
	
	protected void setSecond(Integer se) {
		if (se != null) {
			if (checkSecond(se)) {
				this.se = se;
			}
		}
	}
	
	protected boolean checkSecond(int sec) {
		/* 
		 * this.mi shouldn't be null since we can't set the second before 
		 * 	the minute, as we call superclass constructor first in all
		 * 	cases.
		 */
		return ((sec > -1 && sec < 60) || 
			(sec > -1 && sec == 60 && this.hr == 23 && this.mi == 59));
	}
	
	protected void setUnitAndSubsecond(Unit<Duration> u, int subSec) {
		this.u = u;
		if (!u.equals(TimeUnit.SECOND)) {
			UnitConverter uc = u.getConverterTo(TimeUnit.SECOND);
			double convert = uc.convert(1D);
			double check = subSec * convert;
			if (check < 1) { 
				this.subSecond = subSec;
			} else {
				throw new IllegalArgumentException("Units and value must " +
						"combine for < 1 second.");
			}
		}
	}
	
	public Integer getSecond() {
		return se;
	}
	
	public Integer getSubsecond() {
		return subSecond;
	}
	
	public Unit<Duration> getUnit() {
		return u;
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o instanceof Iso8601UnitTime) {
			Iso8601UnitTime t = (Iso8601UnitTime)o;
			eq = (hr == t.hr && mi == t.mi && tzHr == t.tzHr
					&& tzMin == t.tzMin && se == t.se &&
					u.equals(t.u) && subSecond == t.subSecond);
		}
		return eq;
	}
}
