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
 * A class which keeps track of metadata for a particular dimension in a
 * DataTable. This includes the minimum, maximum, and name.
 * 
 * @author Curran Kelleher
 * 
 */
public class DataDimensionMetadata {
	/**
	 * The maximum value that exists in this dimension.
	 */
	public double maxValue = Double.MIN_VALUE;

	/**
	 * The minimum value that exists in this dimension.
	 */
	public double minValue = Double.MAX_VALUE;

	/**
	 * The index of this dimension in it's parent data table's lise of
	 * dimensions
	 */
	public final int index;

	/**
	 * The name of this dimension.
	 */
	public String name;

	/**
	 * Returns the name of this dimension.
	 */
	public String toString() {
		return name;
	}

	/**
	 * Construct a new dimension metadata with the specified index in it's
	 * parent data table
	 */
	public DataDimensionMetadata(int index) {
		this.index = index;
	}
}
/*
 * CVS Log
 * 
 * $Log: DataDimensionMetadata.java,v $
 * Revision 1.1  2007/08/15 17:59:10  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/26 19:35:49
 * ckellehe Got Parallel Coordinates working Revision 1.1 2007/07/26 00:30:58
 * ckellehe Initial Creation
 * 
 * Revision 1.2 2007/06/08 15:01:45 ckellehe Initial Creation
 * 
 */
