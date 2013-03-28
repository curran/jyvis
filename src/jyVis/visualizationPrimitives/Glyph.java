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
import java.util.LinkedList;
import java.util.List;

import shapeMap.Shape;
import transformation.Window2D;

/**
 * A glyph visualization primitive. This is a shape shifting shape with an x, y
 * and size.
 * 
 * @author Curran Kelleher
 * 
 */
public class Glyph extends Polygon {
	/**
	 * The size and position of this glyph
	 */
	public double size = 1, x = 0, y = 0;

	/**
	 * The center point of this circle in pixel space
	 */
	Point pixelPoint = new Point(0, 0);

	/**
	 * the points which make this object selected if they are inside the
	 * selection polygon
	 */
	private List<Point> selectablePoints;

	public Glyph() {
		this(0, 0, 0);
	}

	/**
	 * Construct a glyph with the specified position and size. The default shape
	 * is a square.
	 * 
	 * @param x
	 * @param y
	 * @param size
	 */
	public Glyph(double x, double y, double size) {
		this.size = size;
		this.x = x;
		this.y = y;

		double[] xCoords = { -1, 1, 1, -1 };
		double[] yCoords = { 1, 1, -1, -1 };
		setPolygonPoints(xCoords, yCoords);

		selectablePoints = new LinkedList<Point>();
		selectablePoints.add(pixelPoint);
	}

	public void setShape(Shape shape) {
		setPolygonPoints(shape.xPoints, shape.yPoints);
	}

	/**
	 * Gets the points which make this object selected if they are inside the
	 * selection polygon.
	 * 
	 */
	public List<Point> getSelectablePoints() {
		return selectablePoints;
	}

	/**
	 * Calculates the pixel-space points that this object will need when drawing
	 * and testing for selections.
	 * 
	 * @param w
	 *            the window to use for coordinate-space to pixel-space
	 *            transformations.
	 */
	public void paint(Graphics g, Window2D w) {
		// these calculations retain the aspect ratio of the shapes, while
		// making their size be influenced equally by the width and height of
		// the screen
		double a = size * (w.getWidth() + w.getHeight()) / 2;
		for (int i = 0; i < xCoordinates.length; i++) {
			p.xpoints[i] = (int) w.getXPixel(xCoordinates[i] * a / w.getWidth()
					+ x);
			p.ypoints[i] = (int) w.getYPixel(yCoordinates[i] * a
					/ w.getHeight() + y);

		}
		p.invalidate();

		pixelPoint.x = (int) w.getXPixel(x);
		pixelPoint.y = (int) w.getYPixel(y);

		if (fill) {
			g.setColor(color);
			g.fillPolygon(p);
		}
		if (drawOutline) {
			g.setColor(Color.black);
			g.drawPolygon(p);
		}
	}
}
/*
 * CVS Log
 * 
 * $Log: Glyph.java,v $
 * Revision 1.1  2007/08/15 17:59:16  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/30 23:43:04 ckellehe Cleaned up
 * Javadoc
 * 
 * Revision 1.1 2007/07/26 00:30:59 ckellehe Initial Creation
 * 
 * Revision 1.4 2007/07/11 19:25:45 rbeaven merger of window computation and
 * paint
 * 
 * Revision 1.3 2007/06/28 16:27:48 rbeaven *** empty log message ***
 * 
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
