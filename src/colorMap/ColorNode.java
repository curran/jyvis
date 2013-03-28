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
package colorMap;

import java.awt.Color;

/**
 * The class which represents a color-value pair used to define color maps.
 * 
 * @author Curran Kelleher
 * @see ColorMap
 * 
 */

public class ColorNode implements Comparable<ColorNode> {
	/**
	 * The color of this node.
	 */
	public Color color;

	/**
	 * The numerical value which this node corresponds to.
	 */
	public double value;

	/**
	 * Construct an empty color node
	 */
	public ColorNode() {
	}

	/**
	 * Construct a color node with the specified color and value
	 * 
	 * @param color
	 *            The color of this node
	 * @param value
	 *            The numerical value between 0 and 1 which this node
	 *            corresponds to
	 */
	public ColorNode(Color color, double value) {
		if (value < 0 || value > 1)
			(new Exception("value must be between 0 and 1!, it is " + value))
					.printStackTrace();
		this.color = color;
		this.value = value;
	}

	/**
	 * Compares ColorNodes by their value field.
	 */
	public int compareTo(ColorNode o) {
		return value < o.value ? -1 : value > o.value ? 1 : 0;
	}

	/**
	 * 
	 * @return the color of this color-value pair
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color of this color-value pair
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * 
	 * @return the value of this color-value pair
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Sets the value of this color-value pair
	 * 
	 * @param value
	 */
	public void setValue(double value) {
		this.value = value;
	}
}
/*
 * CVS Log
 * 
 * $Log: ColorNode.java,v $
 * Revision 1.1  2007/08/15 17:59:11  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:30:58 ckellehe Initial
 * Creation Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
