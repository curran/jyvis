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
package shapeMap;

import java.util.ArrayList;
import java.util.List;

/**
 * A ShapeMap holds a list of JVShapes, and maps input values from 0 to 1 to
 * shapes in that list.
 * 
 * @author Curran Kelleher
 * 
 */
public class ShapeMap {
	/**
	 * The list of shapes which defines this shape map
	 */
	private List<Shape> shapes;

	/**
	 * Construct an empty ShapeMap
	 * 
	 */
	public ShapeMap() {

	}

	/**
	 * Construct a ShapeMap with the specified shapes.
	 * 
	 * @param shapes
	 */
	public ShapeMap(List<Shape> shapes) {
		// make a copy of the list
		this.shapes = new ArrayList<Shape>(shapes);
	}

	/**
	 * Maps the specified value to a shape from this ShapeMap's list of shapes
	 * 
	 * @param value
	 *            a value from 0 to 1
	 * @return the shape corresponding to that value in this ShapeMap
	 */
	public Shape getShape(double value) {
		return shapes.get(value != 1 ? (int) (value * shapes.size()) : shapes
				.size() - 1);
	}

	/**
	 * Returns a default shape map
	 * 
	 */
	public static ShapeMap getDefaultShapeMap() {
		List<Shape> shapes = new ArrayList<Shape>();
		// square
		float[] xCoords = { -1, 1, 1, -1 };
		float[] yCoords = { 1, 1, -1, -1 };
		shapes.add(new Shape(xCoords, yCoords));

		// jewel
		float[] xCoords1 = { -.5f, .5f, 1, 0, -1 };
		float[] yCoords1 = { 1, 1, .5f, -1, .5f };
		shapes.add(new Shape(xCoords1, yCoords1));
		// cross
		float a = 1.0f / 3;
		float[] xCoords2 = { a, a, 1, 1, a, a, -a, -a, -1, -1, -a, -a };
		float[] yCoords2 = { 1, a, a, -a, -a, -1, -1, -a, -a, a, a, 1 };
		shapes.add(new Shape(xCoords2, yCoords2));
		return new ShapeMap(shapes);
	}

	/**
	 * 
	 * @return this ShapeMap's list of shapes
	 */
	public List<Shape> getShapes() {
		return shapes;
	}
}
/*
 * CVS Log
 * 
 * $Log: ShapeMap.java,v $
 * Revision 1.1  2007/08/15 17:59:21  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/30 23:43:03 ckellehe Cleaned up
 * Javadoc
 * 
 * Revision 1.1 2007/07/26 00:31:01 ckellehe Initial Creation
 * 
 * Revision 1.4 2007/06/12 19:27:13 rbeaven *** empty log message ***
 * 
 * Revision 1.3 2007/06/12 15:43:03 rbeaven *** empty log message ***
 * 
 * Revision 1.2 2007/06/08 15:22:08 ckellehe Initial Creation
 * 
 * 
 */
