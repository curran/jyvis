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
 * The interface for a transformation mapping - a function which maps a value
 * from the domain interval to the range interval, and it's inverse function.
 * 
 * @author Curran Kelleher
 * 
 */
public interface TransformationMapping {
	/**
	 * Maps the specified value from the domain to the range
	 * 
	 * @param domainValue
	 *            a value from the domain interval
	 * @param domainMin
	 *            the minimum value of the domain interval
	 * @param domainMax
	 *            the maximum value of the domain interval
	 * @param rangeMin
	 *            the minimum value of the range interval
	 * @param rangeMax
	 *            the maximum value of the range interval
	 * @return the value corresponding to domainValue in the range
	 */
	abstract double transform(double domainValue, double domainMin,
			double domainMax, double rangeMin, double rangeMax);

	/**
	 * Maps the specified value from the range to the domain; performs the
	 * inverse function of transform()
	 * 
	 * @param rangeValue
	 *            a value from the range interval
	 * @param domainMin
	 *            the minimum value of the domain interval
	 * @param domainMax
	 *            the maximum value of the domain interval
	 * @param rangeMin
	 *            the minimum value of the range interval
	 * @param rangeMax
	 *            the maximum value of the range interval
	 * @return the value corresponding to rangeValue in the domain
	 */
	abstract double inverseTransform(double rangeValue, double domainMin,
			double domainMax, double rangeMin, double rangeMax);
}
/*
 * CVS Log
 * 
 * $Log: TransformationMapping.java,v $
 * Revision 1.1  2007/08/15 17:59:13  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:30:58
 * ckellehe Initial Creation
 * 
 */
