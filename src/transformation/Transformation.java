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
 * A class which facilitates transformations between a domain interval and a
 * range interval via an implementation of a mapping.
 * 
 * @author Curran Kelleher
 * 
 */
public class Transformation {
	/**
	 * The domain, or input range of this transformation
	 */
	public Interval domain;

	/**
	 * The range, or output range of this transformation
	 */
	public Interval range;

	/**
	 * The mapping function which is used by this transformation
	 */
	public TransformationMapping function;

	/**
	 * Construct a transformation with domain and range intervals both from 0 to
	 * 1, and a linear transformation function.
	 */
	public Transformation() {
		this(new Interval(0, 1));
	}

	/**
	 * Construct a transformation which maps from the a default domain (input
	 * interval) of 0 to 1, to the specified range (output interval), using a
	 * linear transformation function.
	 * 
	 * @param range
	 */
	public Transformation(Interval range) {
		this(new Interval(0, 1), range);
	}

	/**
	 * Construct a transformation which maps from the specified domain (input
	 * interval) to the specified range (output interval), using a linear
	 * transformation function.
	 * 
	 * @param domain
	 * @param range
	 */
	public Transformation(Interval domain, Interval range) {
		this(domain, range, new LinearTransformationMapping());
	}

	/**
	 * Construct a transformation which maps from the specified domain (input
	 * interval) to the specified range (output interval), using the specified
	 * transformation function.
	 * 
	 * @param domain
	 * @param range
	 */
	public Transformation(Interval domain, Interval range,
			TransformationMapping function) {
		this.domain = domain;
		this.range = range;
		this.function = function;
	}

	/**
	 * Maps the specified value from the domain to the range
	 * 
	 * @param domainValue
	 *            a value from the domain
	 * @return the corresponding value in the range
	 */
	public double transform(double domainValue) {
		return function.transform(domainValue, domain.min, domain.max,
				range.min, range.max);
	}

	/**
	 * Maps the specified value from the range to the domain; performs the
	 * inverse function of transform()
	 * 
	 * @param rangeValue
	 *            a value from the range
	 * @return the corresponding value in the domain
	 */
	public double inverseTransform(double rangeValue) {
		return function.inverseTransform(rangeValue, domain.min, domain.max,
				range.min, range.max);
	}
}
/*
 * CVS Log
 * 
 * $Log: Transformation.java,v $
 * Revision 1.1  2007/08/15 17:59:13  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:30:58 ckellehe
 * Initial Creation
 * 
 */
