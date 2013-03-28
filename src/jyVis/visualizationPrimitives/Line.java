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

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import transformation.Window2D;

/**
 * A line visualization primitive
 * 
 * @author Curran Kelleher
 * 
 */
public class Line extends VisualizationPrimitive {
	/**
	 * the x of the start point
	 */
	public double x1;

	/**
	 * the y of the start point
	 */
	public double y1;

	/**
	 * the x of the end point
	 */
	public double x2;

	/**
	 * the y of the end point
	 */
	public double y2;

	/**
	 * The start point in pixel space
	 */
	Point a = new Point(0, 0);

	/**
	 * The end point in pixel space
	 */
	Point b = new Point(0, 0);

	/**
	 * The bounding box of tis line
	 */
	Rectangle boundingBox;

	/**
	 * the points which make this pbject selected if they are inside the
	 * selection polygon
	 */
	private List<Point> selectablePoints;

	/**
	 * Construct a line with the start and end points at (0,0)
	 */
	public Line() {
		this(0, 0, 0, 0);
	}

	/**
	 * Construct a line with the given start and end points
	 * 
	 * @param x1
	 *            the x of the start point
	 * @param y1
	 *            the y of the start point
	 * @param x2
	 *            the x of the end point
	 * @param y2
	 *            the y of the end point
	 */
	public Line(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;

		selectablePoints = new LinkedList<Point>();
		selectablePoints.add(a);
		selectablePoints.add(b);
	}

	/**
	 * Calculates the pixel-space points that this object will need when drawing
	 * and testing for selections.
	 * 
	 * 
	 */
	public void computeWindowTransformation(Window2D w) {

	}

	/**
	 * Draws this line, using the transformation information calculated in
	 * computeWindowTransformation(). The line is drawn if either fill or
	 * drawOutline are set to true
	 * 
	 * @param g
	 *            the Graphics object to draw on.
	 * @param w
	 *            the window to use for coordinate-space to pixel-space
	 *            transformations.
	 */
	public void paint(Graphics g, Window2D w) {
		a.x = (int) w.getXPixel(x1);
		b.x = (int) w.getXPixel(x2);
		a.y = (int) w.getYPixel(y1);
		b.y = (int) w.getYPixel(y2);

		if (fill || drawOutline) {
			g.setColor(color);
			g.drawLine(a.x, a.y, b.x, b.y);
		}
	}

	/**
	 * This method always returns false for JVLine.
	 */
	public boolean contains(Point point) {
		return false;
	}

	/**
	 * Gets the points which make this pbject selected if they are inside the
	 * selection polygon. The return type is point-value pair because these are
	 * used as entries in the selection quadtree.
	 * 
	 */
	public List<Point> getSelectablePoints() {
		return selectablePoints;
	}

	/**
	 * returns null
	 */
	public Rectangle getBoundingBox() {
		if (boundingBox == null)
			boundingBox = new Rectangle();
		boundingBox.x = a.x;
		boundingBox.y = a.y;
		boundingBox.width = boundingBox.height = 0;
		boundingBox.add(b);

		return boundingBox;
	}
}
/*
 * CVS Log
 * 
 * $Log: Line.java,v $
 * Revision 1.1  2007/08/15 17:59:16  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/30 23:43:03 ckellehe Cleaned up
 * Javadoc
 * 
 * Revision 1.1 2007/07/26 00:30:59 ckellehe Initial Creation Revision 1.5
 * 2007/07/11 19:25:45 rbeaven merger of window computation and paint
 * 
 * Revision 1.4 2007/07/03 18:36:38 ckellehe Created JLIntegerSpinner Revision
 * 1.3 2007/06/08 16:40:22 ckellehe Added a "fill" flag to JVObject, which
 * determines whether or not the shape is filled in, and implemented it in the
 * appropriate subclasses
 * 
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
