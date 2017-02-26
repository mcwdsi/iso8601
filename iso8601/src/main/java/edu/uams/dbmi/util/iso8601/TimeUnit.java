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
import javax.measure.unit.BaseUnit;
import javax.measure.unit.Unit;

public class TimeUnit {
	public static final BaseUnit<Duration> SECOND = new BaseUnit<Duration>("s");
	public static final Unit<Duration> DECISECOND = SECOND.divide(10);
	public static final Unit<Duration> CENTISECOND = SECOND.divide(100);
	public static final Unit<Duration> MILLISECOND = SECOND.divide(1000);
	public static final Unit<Duration> SECOND_MIN_4 = SECOND.divide(10000);
	public static final Unit<Duration> MICROSECOND = MILLISECOND.divide(1000);
	public static final Unit<Duration> NANOSECOND = MICROSECOND.divide(1000);
	
	public static final Unit<Duration> MINUTE = SECOND.times(60);
	public static final Unit<Duration> HOUR = SECOND.times(3600);
}
