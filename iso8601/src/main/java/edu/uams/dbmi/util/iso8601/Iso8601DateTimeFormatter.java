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

import edu.uams.dbmi.util.iso8601.Iso8601Date.DateConfiguration;

public class Iso8601DateTimeFormatter {
	
	Iso8601DateFormatter.FormatOptions dfo;
	Iso8601TimeFormatter.FormatOptions tfo;
	
	Iso8601DateFormatter df;
	Iso8601TimeFormatter tf;
	
	public Iso8601DateTimeFormatter() {
		dfo = new Iso8601DateFormatter.FormatOptions();
		dfo.setExtended(true);
		tfo = new Iso8601TimeFormatter.FormatOptions();
		tfo.setExtended(true);
		
		dfo.setConfiguration(DateConfiguration.YEAR_MONTH_DAY);
		tfo.setPrecedeWithT(true);
		tfo.setIncludeTzMinIfZero(true);
		
		df = new Iso8601DateFormatter(dfo);
		tf = new Iso8601TimeFormatter(tfo);
	}
	
	public Iso8601DateTimeFormatter(Iso8601DateFormatter.FormatOptions dfo,
			Iso8601TimeFormatter.FormatOptions tfo) {
		if (!areConsistent(dfo, tfo)) {
			throw new IllegalArgumentException("Date and time format options" +
					" are inconsistent.");
		}
		this.dfo = (Iso8601DateFormatter.FormatOptions) dfo.clone();
		this.tfo = (Iso8601TimeFormatter.FormatOptions) tfo.clone();
		
		df = new Iso8601DateFormatter(dfo);
		tf = new Iso8601TimeFormatter(tfo);
	}
	
	protected boolean areConsistent(Iso8601DateFormatter.FormatOptions dfo,
			Iso8601TimeFormatter.FormatOptions tfo) {
		/*
		 * If date is in extended format, then time must be also, per the 
		 * 	ISO8601 standard.
		 */
		boolean ok = (dfo.isExtended() == tfo.isExtended());
		/*
		 * It is allowable to leave out the 'T' if both sender and receiver
		 * 	agree beforehand.
		 * 
		 * TODO Should then relax this constraint, but make the user
		 * 	explicitly do so, with all kinds of warnings.
		 */
		ok = ok && tfo.isPrecedeWithT();
		return ok;
	}
	
	public String format(Iso8601DateTime dt) {
		return (df.format(dt.getDate()) + 
				tf.format(dt.getTime()));
	}
}
