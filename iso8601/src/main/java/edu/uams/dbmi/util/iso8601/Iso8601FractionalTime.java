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

public class Iso8601FractionalTime extends Iso8601Time {
	
	Double fr;
	Double frHr;
	Double frMi;
	
	public Iso8601FractionalTime(int hr, double fr) {
		super(hr);
		this.fr = fr;
		this.frHr = fr;
	}
	
	public Iso8601FractionalTime(int hr, int min, double fr) {
		super(hr, min);
		this.fr = fr;
		this.frMi = fr;
	}
	
	public Iso8601FractionalTime(IsoFractionalTimeBuilder tb) {
		super(tb);
		this.fr = tb.fr;
		if (this.mi != null) {
			this.frMi = fr;
		} else {
			this.frHr = fr;
		}
	}
	
	public Double getFractionalHour() {
		return frHr;
	}
	
	public Double getFractionalMinute() {
		return frMi;
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o instanceof Iso8601FractionalTime) {
			Iso8601FractionalTime t = (Iso8601FractionalTime)o;
			eq = (hr == t.hr && mi == t.mi && fr == t.fr &&
					frHr == t.frHr && frMi == t.frMi);
		}
		return eq;
	}
}
