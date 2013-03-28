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
 * A shape for use with a ShapeMap, consisting of a set of X and Y points whose
 * values should be between -1 and 1.
 * 
 */
public class Shape {
	public float[] xPoints, yPoints;

	/**
	 * Create an empty shape
	 * 
	 */
	public Shape() {
	}

	/**
	 * Create a shape with the specified points. The values of both the x and y
	 * points should be between -1 and 1, this is what is expected by the shape
	 * map and glyhps.
	 * 
	 * @param xPoints
	 *            the x coordinates of the shape, between -1 and 1
	 * @param yPoints
	 *            the y coordinates of the shape, between -1 and 1
	 */
	public Shape(float[] xPoints, float[] yPoints) {
		this.xPoints = xPoints;
		this.yPoints = yPoints;
	}

	/**
	 * Create a shape with the specified points. The values of both the x and y
	 * points should be between -1 and 1, this is what is expected by the shape
	 * map and glyhps.
	 * 
	 * @param xPoints
	 *            the x coordinates of the shape, between -1 and 1
	 * @param yPoints
	 *            the y coordinates of the shape, between -1 and 1
	 */
	public Shape(List<Number> xPoints, List<Number> yPoints) {
		int n = xPoints.size();
		this.xPoints = new float[n];
		this.yPoints = new float[n];
		for (int i = 0; i < n; i++) {
			this.xPoints[i] = xPoints.get(i).floatValue();
			this.yPoints[i] = yPoints.get(i).floatValue();
		}
	}

	/**
	 * 
	 * @return the x coordinates of the shape
	 */
	public float[] getXPoints() {
		return xPoints;
	}

	/**
	 * Sets the x coordinates of the shape
	 * 
	 * @param points
	 *            the x coordinates of the shape
	 */
	public void setXPoints(float[] points) {
		xPoints = points;
	}

	/**
	 * 
	 * @return the y coordinates of the shape
	 */
	public float[] getYPoints() {
		return yPoints;
	}

	/**
	 * Sets the y coordinates of the shape
	 * 
	 * @param points
	 *            the y coordinates of the shape
	 */
	public void setYPoints(float[] points) {
		yPoints = points;
	}
}
/*
 * CVS Log
 * 
 * $Log: Shape.java,v $
 * Revision 1.1  2007/08/15 17:59:21  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:31:01 ckellehe Initial
 * Creation
 * 
 * Revision 1.3 2007/06/18 18:21:06 ckellehe Added more widgets to the Groovy
 * scatterplot
 * 
 * Revision 1.2 2007/06/08 15:22:08 ckellehe Initial Creation
 * 
 * 
 */
