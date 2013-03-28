/******************************************************************************
 * Copyright (C) 2007  Institute for Visualization and Perception Research,
 *                     University of Massachusetts Lowell
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package jyVis.data;

/**
 * A value, or entry, in a data table.
 * 
 * @author Curran Kelleher
 * @see DataTable
 * @see DataRecord
 * 
 */
public class DataEntry {
	/**
	 * The double value of this data dimension.
	 */
	public final double value;

	/**
	 * The string associated with this data dimension. This field is null if the
	 * data dimension is numerical (which is most of the time).
	 */
	public final String stringValue;

	/**
	 * Construct a DataDimension with the specified double value. The
	 * stringValue field will be set to null.
	 * 
	 * @param value
	 *            the double value of this DataDimension
	 */
	public DataEntry(double value) {
		this.value = value;
		stringValue = null;
	}

	/**
	 * Construct a DataDimension with the specified string and double values.
	 * Usually when this constructor is used, the double value is an ID which
	 * corresponds to the string (So if two DataDimensions in the same table
	 * have the same string value, they should also have the same double value).
	 * 
	 * @param stringValue
	 *            the string associated with this data dimension
	 * @param value
	 *            the double value of this DataDimension
	 */
	public DataEntry(String stringValue, double value) {
		this.stringValue = stringValue;
		this.value = value;
	}

	/**
	 * If the stringValue of this dimension is not null, then it is returned
	 * from this method. Otherwise, a string representation of this dimension's
	 * double value is returned.
	 */
	public String toString() {
		return stringValue != null ? stringValue : "" + value;
	}
}
/*
 * CVS Log
 * 
 * $Log: DataEntry.java,v $
 * Revision 1.1  2007/08/15 17:59:10  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:30:58 ckellehe Initial
 * Creation
 * 
 * Revision 1.2 2007/06/08 15:01:45 ckellehe Initial Creation
 * 
 */
