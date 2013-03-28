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

import transformation.Interval;
import transformation.Transformation;
import transformation.TransformationMapping;

/**
 * A normalization which can be applied to data.
 * 
 * @author Curran Kelleher
 * 
 */
public class Normalization {
	private Transformation transformation = new Transformation();

	/**
	 * The data associated with this normalization
	 */
	public final DataTable data;

	private final DimensionIntervalMapping intervalMap;

	/**
	 * Construct a normalization which will use the specified data
	 * 
	 * @param data
	 */
	public Normalization(final DataTable data) {
		this(data, new DimensionIntervalMapping() {
			public void setDomainFromDimension(int dimension, Interval domain) {
				DataDimensionMetadata dimensionMetadata = data.dimensions
						.get(dimension);
				domain.min = dimensionMetadata.minValue;
				domain.max = dimensionMetadata.maxValue;
			}
		});
	}

	/**
	 * Construct a normalization which will use the specified data
	 * 
	 * @param data
	 */
	public Normalization(DataTable data, DimensionIntervalMapping intervalMap) {
		this.data = data;
		this.intervalMap = intervalMap;
	}

	/**
	 * Normalizes the data value at the specified record and dimension in the
	 * data table associated with this normalization. The output is between 0
	 * and 1.
	 * 
	 * @param record
	 * @param dimension
	 */
	public double normalize(int record, int dimension) {
		intervalMap.setDomainFromDimension(dimension, transformation.domain);
		return transformation.transform(data.get(record, dimension));
	}

	/**
	 * Performs the inverse of the normalization function.
	 * 
	 * @param value
	 *            the value between 0 and 1 to convert into the range of the
	 *            specified dimension (using the inverse normalization function)
	 * @param dimension
	 *            the dimension used to specify the output range
	 */
	public double inverseNormalize(double value, int dimension) {
		intervalMap.setDomainFromDimension(dimension, transformation.domain);
		return transformation.inverseTransform(value);
	}

	/**
	 * Sets the mapping function that this normalization uses (for example, one
	 * which implements a linear normalization or one which implements a log
	 * normalization)
	 * 
	 * @param function
	 */
	public void setMappingFunction(TransformationMapping function) {
		transformation.function = function;
	}

	public interface DimensionIntervalMapping {
		public void setDomainFromDimension(int dimension, Interval domain);
	}
}
/*
 * CVS Log
 * 
 * $Log: Normalization.java,v $
 * Revision 1.1  2007/08/15 17:59:10  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:30:58 ckellehe
 * Initial Creation
 * 
 */
