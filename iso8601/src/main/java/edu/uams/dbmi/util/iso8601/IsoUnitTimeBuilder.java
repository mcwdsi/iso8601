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

public class IsoUnitTimeBuilder extends IsoTimeBuilder {

	Integer se;
	Unit<Duration> u;
	int subSecond;
	
	public IsoUnitTimeBuilder(int hr) {
		super(hr);
	}
	
	public void setSecond(int sec) {
		se = sec;
	}
	
	public Integer getSecond() {
		return se;
	}

	public void setUnit(Unit<Duration> u) {
		this.u = u;
	}
	
	public Unit<Duration> getUnit() {
		return u;
	}
	
	public void setSubsecond(int subsecond) {
		subSecond = subsecond;
	}
	
	public int getSubsecond() {
		return subSecond;
	}

}
