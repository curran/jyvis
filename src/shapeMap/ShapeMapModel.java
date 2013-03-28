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

import java.util.List;

/**
 * This class stores the data necessary to reconstruct a shape map - the model
 * of the shape map. This includes only a list of shapes. This class is a Java
 * Bean, so can be written to an XML file using Java's XMLEncoder
 * 
 * @author Curran Kelleher
 * 
 */
public class ShapeMapModel {
	/**
	 * The list of shapes which defines this shape map model
	 */
	List<Shape> shapes;

	/**
	 * construct an empty shape map model
	 * 
	 */
	public ShapeMapModel() {
	}

	/**
	 * Construct a ShapeMapModel from the given shape map
	 * 
	 * @param shapeMap
	 */
	public ShapeMapModel(ShapeMap shapeMap) {
		shapes = shapeMap.getShapes();
	}

	/**
	 * Generate a new shape map based on this ShapeMapModel
	 * 
	 */
	public ShapeMap getAsShapeMap() {
		return new ShapeMap(shapes);
	}

	/**
	 * gets the list of shapes which defines this shape map model
	 * 
	 */
	public List<Shape> getShapes() {
		return shapes;
	}

	/**
	 * sets the list of shapes which defines this shape map model
	 * 
	 * @param shapes
	 */
	public void setShapes(List<Shape> shapes) {
		this.shapes = shapes;
	}

}
/*
 * CVS Log
 * 
 * $Log: ShapeMapModel.java,v $
 * Revision 1.1  2007/08/15 17:59:21  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/30 23:43:03 ckellehe
 * Cleaned up Javadoc
 * 
 * Revision 1.1 2007/07/26 00:31:01 ckellehe Initial Creation
 * 
 * Revision 1.3 2007/06/11 15:00:46 rbeaven *** empty log message ***
 * 
 * Revision 1.2 2007/06/08 15:22:08 ckellehe Initial Creation
 * 
 * 
 */
