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
 * A class which facilitates transformations between two rectangles in 2D space.
 * 
 * @author Curran Kelleher
 * 
 */
public class Transformation2D {
	/**
	 * The transformation between x domain and range
	 */
	Transformation xTransformation = new Transformation();

	/**
	 * The transformation between y domain and range
	 */
	Transformation yTransformation = new Transformation();

	/**
	 * Construct a new transformation with the default domain and range of
	 * (0,1,0,1)
	 */
	public Transformation2D() {
	}

	/**
	 * Construct a new transformation with the default domain of (0,1,0,1), and
	 * the range specified.
	 * 
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 */
	public Transformation2D(double xMin, double xMax, double yMin, double yMax) {
		setRange(xMin, xMax, yMin, yMax);
	}

	/**
	 * Sets the 2D domain of this transformation
	 * 
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 */
	public void setDomain(double xMin, double xMax, double yMin, double yMax) {
		xTransformation.domain.min = xMin;
		xTransformation.domain.max = xMax;
		yTransformation.domain.min = yMin;
		yTransformation.domain.max = yMax;
	}

	/**
	 * Sets the 2D range of this transformation
	 * 
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 */
	public void setRange(double xMin, double xMax, double yMin, double yMax) {
		xTransformation.range.min = xMin;
		xTransformation.range.max = xMax;
		yTransformation.range.min = yMin;
		yTransformation.range.max = yMax;
	}

	public double getRangeXMin() {
		return xTransformation.range.min;
	}

	public void setRangeXMin(double rangeXMin) {
		xTransformation.range.min = rangeXMin;
	}

	public double getRangeXMax() {
		return xTransformation.range.max;
	}

	public void setRangeXMax(double rangeXMax) {
		xTransformation.range.max = rangeXMax;
	}

	public double getRangeYMin() {
		return yTransformation.range.min;
	}

	public void setRangeYMin(double rangeYMin) {
		yTransformation.range.min = rangeYMin;
	}

	public double getRangeYMax() {
		return yTransformation.range.max;
	}

	public void setRangeYMax(double rangeYMax) {
		yTransformation.range.max = rangeYMax;
	}

	public double getDomainXMin() {
		return xTransformation.domain.min;
	}

	public void setDomainXMin(double domainXMin) {
		xTransformation.domain.min = domainXMin;
	}

	public double getDomainXMax() {
		return xTransformation.domain.max;
	}

	public void setDomainXMax(double domainXMax) {
		xTransformation.domain.max = domainXMax;
	}

	public double getDomainYMin() {
		return yTransformation.domain.min;
	}

	public void setDomainYMin(double domainYMin) {
		yTransformation.domain.min = domainYMin;
	}

	public double getDomainYMax() {
		return yTransformation.domain.max;
	}

	public void setDomainYMax(double domainYMax) {
		yTransformation.domain.max = domainYMax;
	}

	public double transformX(double domainValue) {
		return xTransformation.transform(domainValue);
	}

	public double transformY(double domainValue) {
		return yTransformation.transform(domainValue);
	}

	public double inverseTransformX(double rangeValue) {
		return xTransformation.inverseTransform(rangeValue);
	}

	public double inverseTransformY(double rangeValue) {
		return yTransformation.inverseTransform(rangeValue);
	}
}
