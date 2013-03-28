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

import java.awt.Color;

import colorMap.ColorMap;

/**
 * A class which manages a color map, normalization, and dimension for assigning
 * colors based on the value at a specific record and dimension in a data table.
 * 
 * @author Curran Kelleher
 * 
 */
public class DataColorMap {
	public final DataTable data;
	public ColorMap colorMap = ColorMap.getDefaultContinuousColorMap();
	public Normalization normalization;
	public int dimension = 0;

	/**
	 * Construct a data color map associated with the specified data, with a
	 * default initial color map
	 * 
	 */
	public DataColorMap(DataTable data) {
		this.data = data;
		normalization = new Normalization(data);
	}

	/**
	 * Gets the color corresponding to the value in the associated data table at
	 * the specified record index and the dimension index stored in the
	 * <code>dimension</code> field of this object.
	 * 
	 * @param record
	 *            the index of the record to generate a color for
	 * @return the color corresponding to the value in the associated data table
	 *         at the specified record index
	 */
	public Color getColor(int record) {
		return colorMap.getColor(normalization.normalize(record, dimension));
	}

	/**
	 * Gets the index of the dimension used for generating colors.
	 * 
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * Sets the index of the dimension used for generating colors.
	 * 
	 */
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public ColorMap getColorMap() {
		return colorMap;
	}

	public void setColorMap(ColorMap colorMap) {
		this.colorMap = colorMap;
	}
}
