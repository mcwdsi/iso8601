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

import java.util.HashMap;

/*
 * Year	Jun 30	Dec 31
1972	+1	+1
1973	0	+1
1974	0	+1
1975	0	+1
1976	0	+1
1977	0	+1
1978	0	+1
1979	0	+1
1980	0	0
1981	+1	0
1982	+1	0
1983	+1	0
1984	0	0
1985	+1	0
1986	0	0
1987	0	+1
1988	0	0
1989	0	+1
1990	0	+1
1991	0	0
1992	+1	0
1993	+1	0
1994	+1	0
1995	0	+1
1996	0	0
1997	+1	0
1998	0	+1
1999	0	0
2000	0	0
2001	0	0
2002	0	0
2003	0	0
2004	0	0
2005	0	+1
2006	0	0
2007	0	0
2008	0	+1
2009	0	0
2010	0	0
2011	0	0
2012	+1	0
2013	0	0
2014	0	0
2015	+1	0
2016	0	+1

 */
public class LeapSecondTable {
	//static int[][] LEAP_SECOND_INDEX = {
	
	public static HashMap<Integer, Integer> LEAP_SECOND_INDEX = new HashMap<Integer, Integer>();
	
	/*
	 * lowest order bit: 1 = leap second on 12/31: 23:59:60
	 * next lowest bit:  1 = leap second on 6/30: 23:59:60
	 */
	static {
		LEAP_SECOND_INDEX.put(1972,	3);
		LEAP_SECOND_INDEX.put(1973,	1);
		LEAP_SECOND_INDEX.put(1974,	1);
		LEAP_SECOND_INDEX.put(1975,	1);
		LEAP_SECOND_INDEX.put(1976,	1);
		LEAP_SECOND_INDEX.put(1977,	1); 
		LEAP_SECOND_INDEX.put(1978,	1); 
		LEAP_SECOND_INDEX.put(1979,	1); 
		LEAP_SECOND_INDEX.put(1981,	2);
		LEAP_SECOND_INDEX.put(1982,	2);
		LEAP_SECOND_INDEX.put(1983,	2);
		LEAP_SECOND_INDEX.put(1985,	2);
		LEAP_SECOND_INDEX.put(1987,	1);
		LEAP_SECOND_INDEX.put(1989,	1);
		LEAP_SECOND_INDEX.put(1990,	1);
		LEAP_SECOND_INDEX.put(1992,	2);
		LEAP_SECOND_INDEX.put(1993,	2);
		LEAP_SECOND_INDEX.put(1994,	2);
		LEAP_SECOND_INDEX.put(1995,	1);
		LEAP_SECOND_INDEX.put(1997,	2);
		LEAP_SECOND_INDEX.put(1998,	1);
		LEAP_SECOND_INDEX.put(2005,	1);
		LEAP_SECOND_INDEX.put(2008,	1);
		LEAP_SECOND_INDEX.put(2012, 2);
		LEAP_SECOND_INDEX.put(2015, 2);
		LEAP_SECOND_INDEX.PUT(2016, 1);
	}
	
	public static boolean hasLeapSecond(Iso8601Date d) {
		boolean ok = false;
		if (d.isDay()) {
			int mo = d.getMonth();
			int da = d.getDayOfMonth();
			if ((da == 30 && mo == 6) || (da == 31 && mo == 12)) {
				int yr = d.getYear();
				Integer bitIndex = LEAP_SECOND_INDEX.get(yr);
				if (bitIndex != null) {
					ok = (mo == 12 && (bitIndex & 1) == 1) ||
							(mo == 6 && (bitIndex  & 2) == 2);
				}
			}
		}
		return ok;
	}
}
