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

/**
 * The mouse listener interface that can be used with JVObjects inside of a
 * JVDrawingPanel
 * 
 * @author Curran Kelleher
 * 
 */
public interface JVMouseListener {
	/**
	 * Called when the mouse was pressed on the JVobject to which this listener
	 * has been added.
	 * 
	 * @param x
	 *            the x coordinate of the mouse location in coordinate space
	 *            (from 0 to 1 by default)
	 * @param y
	 *            the y coordinate of the mouse location in coordinate space
	 *            (from 0 to 1 by default)
	 * 
	 */
	public void mousePressed(double x, double y);

	/**
	 * Called when the mouse was dragged on the JVobject to which this listener
	 * has been added. This method is only called if the mouse drag was
	 * initiated on top of the object
	 * 
	 * @param x
	 *            the x coordinate of the mouse location in coordinate space
	 *            (from 0 to 1 by default)
	 * @param y
	 *            the y coordinate of the mouse location in coordinate space
	 *            (from 0 to 1 by default)
	 * 
	 */
	public void mouseDragged(double x, double y);

	/**
	 * Called when the mouse was released over the JVobject to which this
	 * listener has been added. This method is only called if the mouse press
	 * was initiated on top of the object
	 * 
	 * @param x
	 *            the x coordinate of the mouse location in coordinate space
	 *            (from 0 to 1 by default)
	 * @param y
	 *            the y coordinate of the mouse location in coordinate space
	 *            (from 0 to 1 by default)
	 * 
	 */
	public void mouseReleased(double x, double y);

}
