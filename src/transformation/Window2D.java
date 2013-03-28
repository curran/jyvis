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
package transformation;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * A class which translates between pixel-space and coordinate-space.
 * 
 * @author Curran Kelleher
 * 
 */
public class Window2D {

	/**
	 * The transformation between x coordinate space (the domain) to x pixel
	 * space (the range)
	 */
	Transformation xTransformation = new Transformation();

	/**
	 * The transformation between y coordinate space (the domain) to y pixel
	 * space (the range)
	 */
	Transformation yTransformation = new Transformation();

	/**
	 * 
	 * @return the height of the pixel space
	 */
	public int getHeight() {
		return (int) (yTransformation.range.min - yTransformation.range.max);
	}

	/**
	 * Returns the pixel point (a new Point object) corresponding to the
	 * specified coordinate-space x and y
	 * 
	 * @param x
	 * @param y
	 */
	public Point getPoint(double x, double y) {
		return new Point((int) getXPixel(x), (int) getYPixel(y));
	}

	/**
	 * 
	 * @return the width of the pixel space
	 */
	public int getWidth() {
		return (int) (xTransformation.range.max - xTransformation.range.min);
	}

	/**
	 * Gets the maximum x of the coordinate space
	 */
	public double getXMax() {
		return xTransformation.domain.max;
	}

	/**
	 * Gets the minimum x of the coordinate space
	 */
	public double getXMin() {
		return xTransformation.domain.min;
	}

	/**
	 * Translates an x value from coordinate space into pixel space.
	 * 
	 * @param xVal
	 *            the value in coordinate space
	 * @return the corresponding value in pixel space
	 */
	public double getXPixel(double xVal) {
		return xTransformation.transform(xVal);
	}

	/**
	 * Converts the specified x magnitude from coordinate space to pixel space
	 * 
	 * @param x
	 *            the magnitude in coordinate space
	 * @return the equivalent magnitude in pixel space
	 */
	public double getXPixelMagnitude(double x) {
		return getXPixel(x) - getXPixel(0);
	}

	/**
	 * Translates an x value from pixel space into coordinate space.
	 * 
	 * @param xPixel
	 *            the value in pixel space
	 * @return the corresponding value in coordinate space
	 */
	public double getXValue(double xPixel) {
		return xTransformation.inverseTransform(xPixel);
	}

	/**
	 * Converts the specified x magnitude from pixel space to coordinate space
	 * 
	 * @param xPix
	 *            the magnitude in pixelspace
	 * @return the equivalent magnitude in coordinate space
	 */
	public double getXValueMagnitude(int xPix) {
		return getXValue(xPix) - getXValue(0);
	}

	/**
	 * Gets the maximum y of the coordinate space
	 */
	public double getYMax() {
		return yTransformation.domain.max;
	}

	/**
	 * Gets the minimum y of the coordinate space
	 */
	public double getYMin() {
		return yTransformation.domain.min;
	}

	/**
	 * Translates a y value from coordinate space into pixel space.
	 * 
	 * @param yVal
	 *            the value in coordinate space
	 * @return the corresponding value in pixel space
	 */
	public double getYPixel(double yVal) {
		return yTransformation.transform(yVal);
	}

	/**
	 * Converts the specified y magnitude from coordinate space to pixel space
	 * 
	 * @param y
	 *            the magnitude in coordinate space
	 * @return the equivalent magnitude in pixel space
	 */
	public double getYPixelMagnitude(double y) {
		return getYPixel(0) - getYPixel(y);
	}

	/**
	 * Translates a y value from pixel space into coordinate space.
	 * 
	 * @param yPixel
	 *            the value in pixel space
	 * @return the corresponding value in coordinate space
	 */
	public double getYValue(double yPixel) {
		return yTransformation.inverseTransform(yPixel);
	}

	/**
	 * Converts the specified y magnitude from pixel space to coordinate space
	 * 
	 * @param yPix
	 *            the magnitude in pixelspace
	 * @return the equivalent magnitude in coordinate space
	 */
	public double getYValueMagnitude(int yPix) {
		return getYValue(0) - getYValue(yPix);
	}

	/**
	 * Changes the pixel space of this window and it's (x,y) offsets such that
	 * the aspect ratios of the coordinate space and the pixel space are the
	 * same, eliminating distortion.
	 * 
	 */
	public void makeWindowSquare() {
		int height = getHeight(), width = getWidth();
		int xOffset = 0, yOffset = 0;

		if (height > width) {
			yOffset = (height - width) / 2;
			setHeight(width);
		} else {
			xOffset = (width - height) / 2;
			setWidth(height);
		}
		setLocation(xOffset, yOffset);
	}

	/**
	 * Sets the coordinate space
	 * 
	 * @param xMin
	 *            The minimum x value in the coordinate space.
	 * @param xMax
	 *            The maximum x value in the coordinate space.
	 * @param yMin
	 *            The minimum y value in the coordinate space.
	 * @param yMax
	 *            The maximum y value in the coordinate space.
	 */
	public void set(double xMin, double xMax, double yMin, double yMax) {
		xTransformation.domain.min = xMin;
		yTransformation.domain.min = yMin;
		xTransformation.domain.max = xMax;
		yTransformation.domain.max = yMax;
	}

	/**
	 * Sets the pixel space size
	 * 
	 * @param width
	 *            the width, in pixels, of the pixel space
	 * @param height
	 *            the height, in pixels, of the pixel space
	 */
	public void setBounds(int x, int y, int width, int height) {
		xTransformation.range.min = x;
		yTransformation.range.min = y + height;
		xTransformation.range.max = x + width;
		yTransformation.range.max = y;
	}

	/**
	 * Sets the height of the pixel space
	 */
	public void setHeight(int newHeight) {
		setSize(getWidth(), newHeight);
	}

	/**
	 * Sets the (x,y) offset of the pixel space of this window
	 * 
	 * @param x
	 * @param y
	 */
	private void setLocation(int x, int y) {
		setBounds(x, y, getWidth(), getHeight());
	}

	/**
	 * Sets the pixel space size
	 * 
	 * @param size
	 *            the width and height of the pixel space
	 */
	public void setSize(Dimension size) {
		setSize(size.width, size.height);
	}

	/**
	 * Sets the pixel space size
	 * 
	 * @param width
	 *            the width, in pixels, of the pixel space
	 * @param height
	 *            the height, in pixels, of the pixel space
	 */
	public void setSize(int width, int height) {
		xTransformation.range.min = 0;
		yTransformation.range.min = height;
		xTransformation.range.max = width;
		yTransformation.range.max = 0;
	}

	/**
	 * Sets the width of the pixel space
	 */
	public void setWidth(int newWidth) {
		setSize(newWidth, getHeight());
	}

	/**
	 * Sets the maximum x of the coordinate space
	 */
	public void setXMax(double max) {
		xTransformation.domain.max = max;
	}

	/**
	 * Sets the minimum x of the coordinate space
	 */
	public void setXMin(double min) {
		xTransformation.domain.min = min;
	}

	/**
	 * Sets the maximum y of the coordinate space
	 */
	public void setYMax(double max) {
		yTransformation.domain.max = max;
	}

	/**
	 * Sets the minimum y of the coordinate space
	 */
	public void setYMin(double min) {
		yTransformation.domain.min = min;
	}

	/**
	 * Changes the coordinate space of this window to reflect the specified
	 * rectangle as it is transformed to the original coordinate space. This
	 * will cause distortion if the rectangle is not a square, so if you don't
	 * want any distortion, call makeWindowSquare() after calling this method.
	 * 
	 * @param rectangle
	 *            the rectangle to zoom to
	 */
	public void zoomToRectangle(Rectangle rectangle) {
		set(getXValue(rectangle.x), getXValue(rectangle.x + rectangle.width),
				getYValue(rectangle.y + rectangle.height),
				getYValue(rectangle.y));
	}
}
/*
 * CVS Log
 * 
 * $Log: Window2D.java,v $
 * Revision 1.1  2007/08/15 17:59:13  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/30 23:43:04 ckellehe Cleaned up
 * Javadoc
 * 
 * Revision 1.1 2007/07/26 00:30:59 ckellehe Initial Creation
 * 
 */
