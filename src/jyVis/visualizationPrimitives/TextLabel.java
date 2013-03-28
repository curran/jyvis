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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import transformation.Window2D;

/**
 * A text label visualization primitive. Mouse interaction only works correctly
 * for non-rotated text.
 * 
 * @author Curran Kelleher
 * 
 */
public class TextLabel extends Rectangle {
	/**
	 * A constant which can be assigned to xAlignment or yAlignment which
	 * specifies that the value of the corresponding coordinate (either x or y)
	 * defines the center point of this text label.
	 */
	public static final int CENTER = 0;

	/**
	 * A constant which can be assigned to xAlignment which specifies that the
	 * value of x defines the leftmost point of this text label.
	 */
	public static final int LEFT = 1;

	/**
	 * A constant which can be assigned to xAlignment which specifies that the
	 * value of x defines the rightmost point of this text label.
	 */
	public static final int RIGHT = 2;

	/**
	 * A constant which can be assigned to yAlignment which specifies that the
	 * value of y defines the bottom point of this text label.
	 */
	public static final int BOTTOM = 3;

	/**
	 * A constant which can be assigned to yAlignment which specifies that the
	 * value of y defines the top point of this text label.
	 */
	public static final int TOP = 4;

	/**
	 * text of the label
	 */
	public String text;

	/**
	 * width of the text and label
	 */
	int textWidth;

	/**
	 * height of the text and label
	 */
	int textHeight;

	/**
	 * The size of the text. If the width of the drawing panel is 500 pixels,
	 * then this field is in the unit of points. Otherwise, the size of the text
	 * is scaled proportional to the width of the drawing panel.
	 */
	public double size = 30;

	/**
	 * The font of the text of this text label. The size of this is changed
	 * automatically as a function of the "size" field and the width of the
	 * drawing panel, so changes to the size of this font object will be
	 * overridden. To change the size, change the "size" field.
	 */
	Font font = new Font("Serif", Font.PLAIN, 30);

	/**
	 * the way this label will be aligned with respect to it's x coordinate.
	 * Either JVTextLabel.CENTER, JVTextLabel.LEFT, or JVTextLabel.RIGHT.
	 */
	public int xAlignment;

	/**
	 * the way this label will be aligned with respect to it's y coordinate.
	 * Either JVTextLabel.CENTER, JVTextLabel.BOTTOM, or JVTextLabel.TOP.
	 */
	public int yAlignment;

	/**
	 * rotation of the text
	 */
	public double rotation = 0;

	/**
	 * location of the label
	 */
	public double x, y;

	/**
	 * The affine transform used by all text labels for performing rotations.
	 */
	static final AffineTransform transform = new AffineTransform();

	static final AffineTransform tempTransform = new AffineTransform();

	/**
	 * Construct a text label with no text and coordinates (0,0).
	 * 
	 */
	public TextLabel() {
		this("");
	}

	/**
	 * Construct a text label with the specified text, at location (0,0).
	 * 
	 * @param text
	 *            the text of this text label
	 */
	public TextLabel(String text) {
		this(text, 0, 0);
	}

	/**
	 * Construct a text label with the specified text and location (the
	 * coordinates specify the center by default. This can be changed by setting
	 * the xAlignment and yAlignment fields).
	 * 
	 * @param text
	 *            the text of this text label
	 * @param x1
	 *            the x1 coordinate
	 * @param y1
	 *            the y1 coordinate
	 * 
	 */
	public TextLabel(String text, double x1, double y1) {
		this(text, x1, y1, CENTER, CENTER);
	}

	/**
	 * 
	 * Construct a text label with the specified text, location, x alignment,
	 * and x alignment.
	 * 
	 * @param text
	 *            the text of this text label
	 * @param x1
	 *            the x1 coordinate
	 * @param y1
	 *            the y1 coordinate
	 * @param xAlignment
	 *            the way this label will be aligned with respect to it's x
	 *            coordinate. Either JVTextLabel.CENTER, JVTextLabel.LEFT, or
	 *            JVTextLabel.RIGHT.
	 * @param yAlignment
	 *            the way this label will be aligned with respect to it's y
	 *            coordinate. Either JVTextLabel.CENTER, JVTextLabel.BOTTOM, or
	 *            JVTextLabel.TOP.
	 */
	public TextLabel(String text, double x1, double y1, int xAlignment,
			int yAlignment) {
		this(text, x1, y1, xAlignment, yAlignment, 0);
	}

	/**
	 * 
	 * Construct a text label with the specified text, location, x alignment,
	 * and x alignment.
	 * 
	 * @param text
	 *            the text of this text label
	 * @param x1
	 *            the x1 coordinate
	 * @param y1
	 *            the y1 coordinate
	 * @param xAlignment
	 *            the way this label will be aligned with respect to it's x
	 *            coordinate. Either JVTextLabel.CENTER, JVTextLabel.LEFT, or
	 *            JVTextLabel.RIGHT
	 * @param yAlignment
	 *            the way this label will be aligned with respect to it's y
	 *            coordinate. Either JVTextLabel.CENTER, JVTextLabel.BOTTOM, or
	 *            JVTextLabel.TOP
	 * @param rotation
	 *            the rotation in degrees of this text label in the clockwise
	 *            direction about the point (x,y)
	 */
	public TextLabel(String text, double x1, double y1, int xAlignment,
			int yAlignment, double rotation) {
		super(x1, y1, x1, y1);
		this.x = x1;
		this.y = y1;
		this.text = text;
		this.rotation = rotation;
		if (xAlignment == TextLabel.CENTER || xAlignment == TextLabel.LEFT
				|| xAlignment == TextLabel.RIGHT)
			this.xAlignment = xAlignment;
		else
			throw (new RuntimeException(
					"invalid argument for X alignment. The argument must specify either CENTER, LEFT, or RIGHT."));
		if (yAlignment == TextLabel.CENTER || yAlignment == TextLabel.BOTTOM
				|| yAlignment == TextLabel.TOP)
			this.yAlignment = yAlignment;
		else
			throw (new RuntimeException(
					"invalid argument for Y alignment. The argument must specify either CENTER, BOTTOM, or TOP."));
		calculateRectangleSize(new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_RGB).createGraphics());
	}

	/**
	 * 
	 * Construct a text label with the specified text, location, x alignment,
	 * and x alignment.
	 * 
	 * @param text
	 *            the text of this text label
	 * @param x1
	 *            the x1 coordinate
	 * @param y1
	 *            the y1 coordinate
	 * @param xAlignment
	 *            the way this label will be aligned with respect to it's x
	 *            coordinate. Either "CENTER", "LEFT", or "RIGHT"
	 * @param yAlignment
	 *            the way this label will be aligned with respect to it's y
	 *            coordinate. Either "CENTER", "BOTTOM", or "TOP"
	 * @param rotation
	 *            the rotation in degrees of this text label in the clockwise
	 *            direction about the point (x,y)
	 */
	public TextLabel(String text, double x1, double y1, String xAlignment,
			String yAlignment, double rotation) {
		this(text, x1, y1, parseAlignmentString(xAlignment),
				parseAlignmentString(yAlignment), rotation);
	}

	/**
	 * Parses a string into an alignment code
	 * 
	 * @param alignment
	 * @return
	 */
	private static int parseAlignmentString(String alignment) {
		alignment = alignment.toUpperCase();
		if (alignment.equals("CENTER"))
			return CENTER;
		else if (alignment.equals("LEFT"))
			return LEFT;
		else if (alignment.equals("RIGHT"))
			return RIGHT;
		else if (alignment.equals("BOTTOM"))
			return BOTTOM;
		else if (alignment.equals("TOP"))
			return TOP;
		else
			return -1;
	}

	/**
	 * calculate the size of rectangle based on contained text
	 * 
	 * @param g2d
	 *            the Graphics2D object
	 */
	private void calculateRectangleSize(Graphics2D g2d) {
		g2d.setFont(font);
		FontMetrics fontMetrics = g2d.getFontMetrics();
		textWidth = fontMetrics.stringWidth(text);
		textHeight = fontMetrics.getAscent();
	}

	/**
	 * Draws text rectangle
	 * 
	 * @param g
	 *            the Graphics object to draw on.
	 * @param w
	 *            the window to use for coordinate-space to pixel-space
	 *            transformations.
	 */
	public void paint(Graphics g, Window2D w) {
		if (fill) {
			Graphics2D g2d = (Graphics2D) g;
			// update font size
			font = font.deriveFont((float) size * w.getWidth() / 500);

			calculateRectangleSize(g2d);

			// translate from coordinate to pixel space
			int xpixel = (int) w.getXPixel(x);
			int ypixel = (int) w.getYPixel(y);
			int xpixel2 = (int) w.getXPixel(x2);
			int ypixel2 = (int) w.getYPixel(y2);

			Point point0 = points.get(0);
			Point point1 = points.get(1);
			Point point2 = points.get(2);
			Point point3 = points.get(3);

			// assign pixels from coordinates
			point0.x = xpixel;
			point0.y = ypixel;
			point1.x = xpixel;
			point1.y = ypixel2;
			point2.x = xpixel2;
			point2.y = ypixel2;
			point3.x = xpixel2;
			point3.y = ypixel;

			// adjust to size of the text
			point2.x = point3.x = point0.x + textWidth;
			point1.y = point2.y = point0.y - textHeight;

			int textX = 0;
			int textY = 0;
			// adjust pixels based on text alignment
			if (xAlignment == CENTER)
				textX -= textWidth / 2;
			else if (xAlignment == RIGHT)
				textX -= textWidth;
			if (yAlignment == CENTER)
				textY += textHeight / 2;
			else if (yAlignment == TOP)
				textY += textHeight;
			// update pixels
			for (Point p : points) {
				p.x += textX;
				p.y += textY;
			}

			// update coordinates from new pixel values
			x2 = w.getXValue(point2.x);
			y2 = w.getYValue(point2.y);

			g2d.setColor(color);
			// set the rotation transform
			transform.setToTranslation(w.getXPixel(x), w.getYPixel(y));
			tempTransform.setToRotation(Math.toRadians(rotation));
			transform.concatenate(tempTransform);
			g2d.setTransform(transform);
			// draw the string
			g2d.drawString(text, textX, textY);
			// set the transform back so objects drawn in the future are drawn
			// correctly
			transform.setToIdentity();
			g2d.setTransform(transform);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public int getXAlignment() {
		return xAlignment;
	}

	public void setXAlignment(int alignment) {
		xAlignment = alignment;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getYAlignment() {
		return yAlignment;
	}

	public void setYAlignment(int alignment) {
		yAlignment = alignment;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
}
/*
 * CVS Log
 * 
 * $Log: TextLabel.java,v $
 * Revision 1.1  2007/08/15 17:59:16  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/27 16:28:33 ckellehe Re-wrote
 * RadVis, included dimension list editing Revision 1.1 2007/07/26 00:30:59
 * ckellehe Initial Creation Revision 1.5 2007/07/11 19:25:45 rbeaven merger of
 * window computation and paint
 * 
 * Revision 1.4 2007/07/06 19:02:49 cmathai Made progress on heatmap
 * 
 * Revision 1.3 2007/06/21 17:01:27 ckellehe Updated the scatterplot Groovy
 * script with all the features of the Python scatterplot
 * 
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
