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
 * A circle visualization primitive
 * 
 * @author Curran Kelleher
 * 
 */
public class Circle extends VisualizationPrimitive {
	/**
	 * The radius and center point of this circle.
	 */
	public double radius, x, y;

	/**
	 * The pixel-space diameter of this circle
	 */
	private int diameterPixel = 0;

	/**
	 * The center point of this circle in pixel space
	 */
	Point pixelPoint = new Point(0, 0);

	/**
	 * The bounding box of this circle
	 */
	Rectangle boundingBox;

	/**
	 * the points which make this object selected if they are inside the
	 * selection polygon
	 */
	private List<Point> selectablePoints;

	/**
	 * Construct a circle with the 0,0,0.01 as default coordinates and radius
	 * 
	 */
	public Circle() {
		this(0, 0, 0.01);
	}

	/**
	 * Construct a circle with the given coordinates (the center point) and
	 * radius
	 * 
	 * @param x
	 *            the x coordinate of this circle
	 * @param y
	 *            the y coordinate of this circle
	 * @param radius
	 *            the radius of this circle
	 */
	public Circle(double x, double y, double radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		selectablePoints = new LinkedList<Point>();
		selectablePoints.add(pixelPoint);
	}

	/**
	 * Draws this circle, using the transformation information calculated in
	 * computeWindowTransformation().
	 * 
	 * @param g
	 *            the Graphics object to draw on.
	 */
	public void paint(Graphics g, Window2D w) {
		diameterPixel = (int) (w.getXPixelMagnitude(radius) + w
				.getYPixelMagnitude(radius));

		pixelPoint.x = (int) w.getXPixel(x);
		pixelPoint.y = (int) w.getYPixel(y);
		g.setColor(color);
		if (fill || drawOutline) {
			int xp = pixelPoint.x - diameterPixel / 2;
			int yp = pixelPoint.y - diameterPixel / 2;

			if (fill)
				g.fillOval(xp, yp, diameterPixel, diameterPixel);

			if (drawOutline) {
				g.setColor(Color.BLACK);
				g.drawOval(xp, yp, diameterPixel, diameterPixel);
			}
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
		int a = pixelPoint.x - point.x;
		int b = pixelPoint.y - point.y;
		return Math.sqrt(a * a + b * b) < diameterPixel / 2;
	}

	/**
	 * Gets the points which make this object selected if they are inside the
	 * selection polygon. The return type is point-value pair because these are
	 * used as entries in the selection quadtree.
	 * 
	 */
	public List<Point> getSelectablePoints() {
		return selectablePoints;
	}

	public Rectangle getBoundingBox() {
		if (boundingBox == null)
			boundingBox = new Rectangle();

		boundingBox.x = pixelPoint.x - diameterPixel / 2;
		boundingBox.y = pixelPoint.y - diameterPixel / 2;
		boundingBox.width = diameterPixel;
		boundingBox.height = diameterPixel;
		return boundingBox;
	}
}
/*
 * CVS Log
 * 
 * $Log: Circle.java,v $
 * Revision 1.1  2007/08/15 17:59:16  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/30 23:43:04 ckellehe Cleaned up
 * Javadoc
 * 
 * Revision 1.1 2007/07/26 00:30:59 ckellehe Initial Creation Revision 1.8
 * 2007/07/11 19:25:45 rbeaven merger of window computation and paint Revision
 * 1.7 2007/07/10 15:55:40 ckellehe Added Tool interface
 * 
 */
