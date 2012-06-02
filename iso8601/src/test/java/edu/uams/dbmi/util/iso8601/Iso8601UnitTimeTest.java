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
import junit.framework.TestCase;

public class Iso8601UnitTimeTest extends TestCase {
	@Test
	public void testInitialization() {
		Iso8601UnitTime ut1 = new Iso8601UnitTime(12);
		checkHour(ut1, 12);
		
		Iso8601UnitTime ut2 = new Iso8601UnitTime(0);
		checkHour(ut2, 0);
		
		Iso8601UnitTime ut3 = new Iso8601UnitTime(24);
		checkHour(ut3, 24);
		
		int min = 20;
		Iso8601UnitTime ut4 = new Iso8601UnitTime(0, min);
		checkUnitValue(ut4, min, TimeUnit.MINUTE);
		checkMinute(ut4, 0, min);
	}
	
	private void checkHour(Iso8601UnitTime t, int hr) {
		checkUnitValue(t, hr, TimeUnit.HOUR);
		IsoUnitTimeBuilder b = new IsoUnitTimeBuilder(hr);
		t = new Iso8601UnitTime(b);
		checkUnitValue(t, hr, TimeUnit.HOUR);
	}
	
	private void checkMinute(Iso8601UnitTime t, int hr, int min) {
		assertEquals(t.getHour(), hr);
		checkUnitValue(t, min, TimeUnit.MINUTE);
		IsoUnitTimeBuilder b = new IsoUnitTimeBuilder(hr);
		b.setMinute(min);
		t = new Iso8601UnitTime(b);
		assertEquals(t.getHour(), hr);
		checkUnitValue(t, min, TimeUnit.MINUTE);
	}

	private void checkUnitValue(Iso8601UnitTime ut1, int i, Unit<Duration> tu) {
		assertEquals(ut1.getUnit(), tu);
		if (tu.equals(TimeUnit.HOUR)) {
			assertEquals(i, ut1.getHour());
		} else if (tu.equals(TimeUnit.MINUTE)) {
			assertEquals(i, ut1.getMinute());
		} else if (tu.equals(TimeUnit.SECOND)) {
			assertEquals(i, ut1.getSecond().intValue());
		} else {
			assertEquals(i, ut1.getSubsecond().intValue());
		}
	}
}
