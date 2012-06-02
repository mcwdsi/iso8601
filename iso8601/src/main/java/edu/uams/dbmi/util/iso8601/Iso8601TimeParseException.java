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

public class Iso8601TimeParseException extends Exception {

	private static final long serialVersionUID = 1043624733460892646L;
	
	public Iso8601TimeParseException() {
	}

	public Iso8601TimeParseException(String arg0) {
		super(arg0);
	}

	public Iso8601TimeParseException(Throwable arg0) {
		super(arg0);
	}

	public Iso8601TimeParseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
