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

public class LinearTransformationMapping implements TransformationMapping {

	/**
	 * Maps the specified value from the domain to the range
	 * 
	 * @param domainValue
	 *            a value from the domain
	 * @return the corresponding value in the range
	 */
	public double transform(double domainValue, double domainMin,
			double domainMax, double rangeMin, double rangeMax) {
		return (domainValue - domainMin) / (domainMax - domainMin)
				* (rangeMax - rangeMin) + rangeMin;
	}

	/**
	 * Maps the specified value from the range to the domain; performs the
	 * inverse function of transform()
	 * 
	 * @param rangeValue
	 *            a value from the range
	 * @return the corresponding value in the domain
	 */
	public double inverseTransform(double rangeValue, double domainMin,
			double domainMax, double rangeMin, double rangeMax) {
		return (domainMax - domainMin) * (rangeValue - rangeMin)
				/ (rangeMax - rangeMin) + domainMin;
	}

}
/*
 * CVS Log
 * 
 * $Log: LinearTransformationMapping.java,v $
 * Revision 1.1  2007/08/15 17:59:13  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:30:58
 * ckellehe Initial Creation
 * 
 */
