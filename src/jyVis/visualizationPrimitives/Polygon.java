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
package jyVis.visualizationPrimitives;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import transformation.Window2D;

/**
 * A polygon visualization primitive
 * 
 * @author Curran Kelleher
 * 
 */
public class Polygon extends VisualizationPrimitive {
	/**
	 * The x coordinates of the polygon
	 */
	protected double[] xCoordinates;

	/**
	 * The y coordinates of the polygon
	 */
	protected double[] yCoordinates;

	/**
	 * create the list of cached points for indexing in the quad tree
	 */
	protected List<Point> selectablePoints;

	/**
	 * The polygon (in pixel space) to draw
	 */
	protected java.awt.Polygon p;

	/**
	 * Construct an empty polygon
	 * 
	 */
	public Polygon() {
	}

	/**
	 * Construct a polygon with the given coordinates
	 * 
	 * @param xCoordinates
	 *            the x coordinates of the polygon
	 * @param yCoordinates
	 *            the y coordinates of the polygon (must be the same length as
	 *            xCoordinates, otherwise an IllegalArgumentException is thrown)
	 */
	public Polygon(double[] xCoordinates, double[] yCoordinates) {
		setPolygonPoints(xCoordinates, yCoordinates);
	}

	public void setPolygonPoints(float[] xCoordinates, float[] yCoordinates) {
		int n = xCoordinates.length;
		double[] xCoords = new double[n];
		double[] yCoords = new double[n];
		for (int i = 0; i < n; i++) {
			xCoords[i] = (double) xCoordinates[i];
			yCoords[i] = (double) yCoordinates[i];

		}
		setPolygonPoints(xCoords, yCoords);
	}

	public void setPolygonPoints(double[] xCoordinates, double[] yCoordinates) {
		this.xCoordinates = xCoordinates;
		this.yCoordinates = yCoordinates;
		if (xCoordinates.length != yCoordinates.length)
			throw new IllegalArgumentException(
					"the x and y coordinate arrays must be the same size!"
							+ " xCoordinates is length " + xCoordinates.length
							+ ", and yCoordinates is length "
							+ yCoordinates.length);
		p = new java.awt.Polygon(new int[xCoordinates.length],
				new int[xCoordinates.length], xCoordinates.length);

		// create the list of cached points for indexing in the quad tree
		selectablePoints = new LinkedList<Point>();
		for (int i = 0; i < xCoordinates.length; i++)
			selectablePoints.add(new Point());
	}

	/**
	 * Draws this polygon, using the transformation information calculated in
	 * computeWindowTransformation().
	 * 
	 * @param g
	 *            the Graphics object to draw on.
	 */
	public void paint(Graphics g, Window2D w) {

		for (int i = 0; i < xCoordinates.length; i++) {
			p.xpoints[i] = (int) w.getXPixel(xCoordinates[i]);
			p.ypoints[i] = (int) w.getYPixel(yCoordinates[i]);
			// System.out.println(p.xpoints[i]+" "+p.ypoints[i]);
		}
		p.invalidate();

		if (fill) {
			g.setColor(color);
			g.fillPolygon(p);
		}
		if (drawOutline) {
			g.setColor(Color.black);
			g.drawPolygon(p);
		}
	}

	/**
	 * Determines whether or not the specified point is inside this circle.
	 * 
	 * @param point
	 *            the point where the mouse was clicked
	 * @return true if the specified point is inside this circle
	 */
	public boolean contains(Point point) {
		return p.contains(point);
	}

	/**
	 * Gets the points which make this pbject selected if they are inside the
	 * selection polygon.
	 * 
	 */
	public List<Point> getSelectablePoints() {
		for (int i = 0; i < xCoordinates.length; i++) {
			selectablePoints.get(i).x = p.xpoints[i];
			selectablePoints.get(i).y = p.ypoints[i];
		}
		return selectablePoints;
	}

	public Rectangle getBoundingBox() {
		return p.getBounds();
	}
}
/*
 * CVS Log
 * 
 * $Log: Polygon.java,v $
 * Revision 1.1  2007/08/15 17:59:16  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/30 23:43:04 ckellehe Cleaned up
 * Javadoc
 * 
 * Revision 1.1 2007/07/26 00:30:59 ckellehe Initial Creation Revision 1.5
 * 2007/07/11 19:25:45 rbeaven merger of window computation and paint Revision
 * 1.4 2007/06/20 21:26:59 ckellehe Partially implemented a Java scripting
 * engine, so that Java code can now specify callback functions for widgets, so
 * can now use the wiget framework directly. Used the Java engine to implement a
 * text field + combo box duo, which was previously implemented in Python. Now
 * this text field + combo box interface is available as a standard widget, and
 * can be used in all supported languages Created an example Groovy script that
 * uses the text field + combo box interface.
 * 
 * Revision 1.3 2007/06/08 16:40:22 ckellehe Added a "fill" flag to JVObject,
 * which determines whether or not the shape is filled in, and implemented it in
 * the appropriate subclasses
 * 
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
