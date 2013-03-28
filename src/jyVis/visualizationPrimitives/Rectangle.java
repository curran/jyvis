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
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import transformation.Window2D;

/**
 * A rectangle visualization primitive
 * 
 * @author Curran Kelleher
 * 
 */
public class Rectangle extends VisualizationPrimitive {
	/**
	 * The x, y, width, height of this rectangle.
	 */
	public double x1, y1, x2, y2;

	/**
	 * The bounding box of this rectangle
	 */
	java.awt.Rectangle boundingBox;

	/**
	 * the outer pixel-space points of this rectangle.
	 */
	protected List<Point> points = new ArrayList<Point>();

	public Point centerPoint = new Point();

	/**
	 * Construct a rectangle with the 0,0,0,0 as default coordinates and size
	 * 
	 */
	public Rectangle() {
		this(0, 0, 0, 0);
	}

	/**
	 * Construct a Rectangle with the specified lower left and upper right
	 * corner points.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public Rectangle(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		for (int i = 0; i < 4; i++)
			points.add(new Point());

	}

	/**
	 * Draws this rectangle, using the transformation information calculated in
	 * computeWindowTransformation().
	 * 
	 * @param g
	 *            the Graphics object to draw on.
	 * @param w
	 *            the window to use for coordinate-space to pixel-space
	 *            transformations.
	 */
	public void paint(Graphics g, Window2D w) {

		Graphics2D g2d = (Graphics2D) g;

		int xpixel = (int) w.getXPixel(x1);
		int ypixel = (int) w.getYPixel(y1);
		int ypixel2 = (int) w.getYPixel(y2);
		int xpixel2 = (int) w.getXPixel(x2);

		Point point0 = points.get(0);
		point0.x = xpixel;
		point0.y = ypixel;

		Point point1 = points.get(1);
		point1.x = xpixel;
		point1.y = ypixel2;

		Point point2 = points.get(2);
		point2.x = xpixel2;
		point2.y = ypixel2;

		Point point3 = points.get(3);
		point3.x = xpixel2;
		point3.y = ypixel;

		g2d.setColor(color);
		if (fill || drawOutline) {

			int width = point3.x - point1.x;
			int height = point3.y - point1.y;

			if (fill)
				g2d.fillRect(point1.x, point1.y, width, height);
			if (drawOutline) {
				g2d.setColor(Color.BLACK);
				g2d.drawRect(point1.x, point1.y, width, height);
			}
		}
	}

	/**
	 * Determines whether or not the specified point is inside this rectangle.
	 * 
	 * @param p
	 *            the point where the mouse was clicked
	 * @return true if the specified point is inside this rectangle
	 */
	public boolean contains(Point p) {
		Point point0 = points.get(0);
		Point point2 = points.get(2);

		int xpixel = point0.x;
		int ypixel = point0.y;

		int xpixel2 = point2.x;
		int ypixel2 = point2.y;

		return p.x > xpixel && p.x < xpixel2 && p.y < ypixel && p.y > ypixel2;
	}

	/**
	 * Gets the points which make this object selected if they are inside the
	 * selection polygon. The return type is point-value pair because these are
	 * used as entries in the selection quadtree.
	 * 
	 */
	public List<Point> getSelectablePoints() {
		return points;
	}

	public java.awt.Rectangle getBoundingBox() {
		if (boundingBox == null)
			boundingBox = new java.awt.Rectangle();
		Point point0 = points.get(0);
		Point point2 = points.get(2);
		boundingBox.x = point0.x;
		boundingBox.y = point2.y;
		boundingBox.width = point2.x - point0.x;
		boundingBox.height = point0.y - point2.y;
		return boundingBox;
	}
}
/*
 * CVS Log
 * 
 * $Log: Rectangle.java,v $
 * Revision 1.1  2007/08/15 17:59:16  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/30 23:43:03 ckellehe Cleaned up
 * Javadoc
 * 
 * Revision 1.1 2007/07/26 00:30:59 ckellehe Initial Creation Revision 1.5
 * 2007/07/11 21:01:32 rbeaven *** empty log message ***
 * 
 * Revision 1.4 2007/07/11 19:25:45 rbeaven merger of window computation and
 * paint Revision 1.3 2007/07/11 15:59:50 rbeaven implemented label as
 * JVRectangle with text Revision 1.2 2007/07/06 19:02:49 cmathai Made progress
 * on heatmap
 * 
 * Revision 1.1 2007/06/29 20:00:04 ckellehe Created JVRectangle
 * 
 */
