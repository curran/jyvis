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
package transformation;

/**
 * A class representing an interval (or a range) of real numbers, above some
 * lower bound and below some upper bound.
 * 
 * @author Curran Kelleher
 * 
 */
public class Interval {
	/**
	 * The lower bound of this interval
	 */
	public double min = 0;

	/**
	 * The upper bound of this interval
	 */
	public double max = 1;

	/**
	 * Construct a default interval with a lower bound of 0 and an upper bound
	 * of 1
	 */
	public Interval() {
		this(0, 1);
	}

	/**
	 * Construct an interval with the specified lower and upper bounds
	 * 
	 * @param min
	 * @param max
	 */
	public Interval(double min, double max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Sets the min and max values of this interval to be the same as those of
	 * the specified interval
	 * 
	 * @param interval
	 *            the interval to copy
	 */
	public void set(Interval interval) {
		this.min = interval.min;
		this.max = interval.max;
	}
}
/*
 * CVS Log
 * 
 * $Log: Interval.java,v $
 * Revision 1.1  2007/08/15 17:59:13  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:30:59 ckellehe Initial
 * Creation
 * 
 */
