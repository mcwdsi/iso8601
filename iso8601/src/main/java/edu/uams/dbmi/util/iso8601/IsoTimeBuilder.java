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

public abstract class IsoTimeBuilder {
	Integer hr;
	Integer min;
	
	int tzHr;
	int tzMin;
	
	boolean isUtc;
	
	public IsoTimeBuilder(int hr) {
		this .hr = hr;
		min = null;
	}
	
	public void setMinute(int min) {
		this .min = min;
	}

	public void setTimeZoneOffsetHour(int hr) {
		this. tzHr = hr;
	}
	
	public void setTimeZoneOffsetMinute(int min) {
		this. tzMin = min;
	}
	
	public void setIsUtc(boolean isUtc) {
		this. isUtc = isUtc;
	}
	
	public Integer getHour() {
		return hr;
	}
	
	public Integer getMinute() {
		return min;
	}
	
	public boolean isUtc() {
		return isUtc;
	}
	
	public int getTimeZoneOffsetHour() {
		return tzHr;
	}
	
	public int getTimeZoneOffsetMinute() {
		return tzMin;
	}
}
