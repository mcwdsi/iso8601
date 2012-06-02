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

public class Iso8601DateTimeParser {
	
	Iso8601DateParser dp;
	Iso8601TimeParser tp;
	
	/*
	 * Notes: 
	 * 
	 * 1.  If date is in extended form, then time must be as well
	 * 2.  We eventually need to allow user to remove "T" as the standard
	 * 		allows leaving it out if sender/receiver mutually agree 
	 * 		beforehand.
	 *  
	 */
	public Iso8601DateTimeParser() {
		dp = new Iso8601DateParser();
		tp = new Iso8601TimeParser();
	}
	
	public Iso8601DateTime parse(String s) throws Iso8601DateParseException,
			Iso8601TimeParseException {
		Iso8601DateTime dt = null;
		String[] parts = s.split("T");
		if (parts.length != 2) {
			throw new Iso8601DateParseException("Date and time must both be " +
					"present, and the only things present.");
		}
		Iso8601Date d = dp.parse(parts[0]);
		Iso8601Time t = tp.parse(parts[1]);
		boolean consistent = 
			(parts[0].contains("-") && tp.isExtendedConsistent()) ||
			(!parts[0].contains("-") && !tp.isExtended());
		if (!consistent) {
			throw new Iso8601DateParseException("If date is in extended " +
					"format, then time must be also (" + s + ")");
		}
		dt = new Iso8601DateTime(d, t);
		return dt;
	}
}
