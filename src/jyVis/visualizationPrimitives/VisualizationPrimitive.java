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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import jyVis.GlobalObjects;
import jyVis.data.DataRecord;
import jyVis.graphics.DrawingPanel;
import scripting.ScriptBottleneck;
import transformation.Window2D;

/**
 * The base class of all 2D objects for use with JyVis.
 * 
 * @author Curran Kelleher
 * 
 */
public abstract class VisualizationPrimitive {
	/**
	 * The color of this object. The default is black.
	 */
	public Color color = Color.black;

	/**
	 * A flag which determines whether or not an outline will be drawn around
	 * this object. This is false by default
	 */
	public boolean drawOutline = false;

	/**
	 * A flag which determines whether or not this object will be filled in.
	 * This is true by default
	 */
	public boolean fill = true;

	/**
	 * The record associated with this object. This is populated when
	 * setUpForSelection() is called.
	 */
	private DataRecord associatedRecord;

	/**
	 * the list of mouse listeners
	 */
	private List<JVMouseListener> mouseListeners;

	/**
	 * The index of this object inGlobalObjects. This object is added to
	 * GlobalObjects so that mouse events can be logged as scripts.
	 */
	private int globalObjectsIndex;

	/**
	 * Draws this object, using the transformation information calculated in
	 * computeWindowTransformation().
	 * 
	 * @param g
	 *            the Graphics to draw this object on
	 * @param w
	 *            the window to use for coordinate-space to pixel-space
	 *            transformations.
	 */
	public abstract void paint(Graphics g, Window2D w);

	/**
	 * Sets the color of this object.
	 * 
	 * @param color
	 *            the new Color of this object
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Sets the color of this object.
	 * 
	 * @param r
	 *            red from 0 to 1
	 * @param g
	 *            green from 0 to 1
	 * @param b
	 *            blue from 0 to 1
	 * @param a
	 *            aplha from 0 to 1
	 */
	public void setColor(double r, double g, double b, double a) {
		float red = (float)(r > 1 ? 1 : r < 0 ? 0 : r);
		float green = (float)(g > 1 ? 1 : g < 0 ? 0 : g);
		float blue = (float)(b > 1 ? 1 : b < 0 ? 0 : b);
		float alpha = (float)(a > 1 ? 1 : a < 0 ? 0 : a);
		color = new Color(red, green,
				blue, alpha);
	}

	/**
	 * Sets the color of this object.
	 * 
	 * @param r
	 *            red from 0 to 1
	 * @param g
	 *            green from 0 to 1
	 * @param b
	 *            blue from 0 to 1
	 */
	public void setColor(double r, double g, double b) {
		setColor(r, g, b, 1);
	}

	/**
	 * Determines whether or not this object will be selected when the mouse is
	 * clicked at the specified point. Usually, the implementation of this
	 * returns true if point is inside the object. False is returned if this
	 * method is not implemented.
	 * 
	 * @param point
	 *            the point where the mouse was clicked
	 * @return true if this object will be selected by the user clicking at
	 *         point "point" on the drawing panel
	 */
	public boolean contains(Point point) {
		return false;
	}

	/**
	 * Sets up this object to work with selection. This means that this
	 * graphical object is associated with the specified record, and is
	 * selectable, and will be drawn as selected, in the specified
	 * JVDrawingPanel.
	 * 
	 * @param record
	 *            the data record to link this object to
	 * @param panel
	 *            the drawing panel which will draw this object when it is
	 *            selected, and will also be a means of selecting the record
	 *            associated with this object.
	 */
	public void setUpForSelection(DataRecord record, DrawingPanel panel) {
		if (record == null)
			throw new IllegalArgumentException("record is null");
		if (panel == null)
			throw new IllegalArgumentException("panel is null");
		// create the record's map of objects if it doesn't exist already
		if (record.objects == null)
			record.objects = new HashMap<DrawingPanel, List<VisualizationPrimitive>>();

		// get the record's list of objects which are mapped to
		// the panel
		List<VisualizationPrimitive> l = record.objects.get(panel);
		// set up a new list if one doesn't already exist
		if (l == null) {
			l = new LinkedList<VisualizationPrimitive>();
			record.objects.put(panel, l);
		}
		// add this object to the list
		l.add(this);

		// set this object's associated record
		associatedRecord = record;
	}

	/**
	 * Gets the points which make this object selected if they are inside the
	 * selection polygon. If null is returned, then this object will not be
	 * selectable. null is returned if this method is not implemented.
	 * 
	 */
	public List<Point> getSelectablePoints() {
		return null;
	}

	/**
	 * 
	 * @return the record associated with this object, or null if this object
	 *         has no associated record
	 */
	public DataRecord getAssociatedRecord() {
		return associatedRecord;
	}

	/**
	 * 
	 * @return the index of the record associated with this object, or 0 if this
	 *         object has no associated record
	 */
	public int record() {
		return associatedRecord != null ? associatedRecord.index : 0;
	}

	/**
	 * Returns the bounding box of this object (in pixel space)
	 * 
	 * @return the bounding box of this object
	 */
	public abstract Rectangle getBoundingBox();

	/**
	 * Adds a mouse listener to this object
	 * 
	 * @param mouseListener
	 */
	public void addJVMouseListener(JVMouseListener mouseListener) {
		if (mouseListeners == null) {
			mouseListeners = new ArrayList<JVMouseListener>();
			globalObjectsIndex = GlobalObjects.add(this);
		}
		mouseListeners.add(mouseListener);
	}

	/**
	 * Gets whether or not this object has any mouse listeners attached to it
	 * 
	 */
	public boolean hasMouseListeners() {
		return mouseListeners != null;
	}

	/**
	 * Executes a script through the script bottleneck which calls
	 * fireMousePressed in this object
	 * 
	 * @param x
	 *            the x coordinate of the mouse location in coordinate space
	 * @param y
	 *            the y coordinate of the mouse location in coordinate space
	 * 
	 */
	public void fireMousePressedScript(double x, double y) {
		fireScript("MousePressed", x, y);
	}

	/**
	 * Executes a script through the script bottleneck which calls
	 * fireMouseDragged in this object
	 * 
	 * @param x
	 *            the x coordinate of the mouse location in coordinate space
	 * @param y
	 *            the y coordinate of the mouse location in coordinate space
	 * 
	 */
	public void fireMouseDraggedScript(double x, double y) {
		fireScript("MouseDragged", x, y);
	}

	/**
	 * Executes a script through the script bottleneck which calls
	 * fireMouseReleased in this object
	 * 
	 * @param x
	 *            the x coordinate of the mouse location in coordinate space
	 * @param y
	 *            the y coordinate of the mouse location in coordinate space
	 * 
	 */
	public void fireMouseReleasedScript(double x, double y) {
		fireScript("MouseReleased", x, y);
	}

	private void fireScript(String string, double x, double y) {
		if (mouseListeners != null) {
			ScriptBottleneck.importIfNotDefined("python", GlobalObjects.class);
			ScriptBottleneck.exec("python", "GlobalObjects.get("
					+ globalObjectsIndex + ").fire" + string + "(" + x + ","
					+ y + ")");
		}
	}

	/**
	 * Calls the mousePressed function in all the mouse listeners which have
	 * been added to this object
	 * 
	 * @param x
	 *            the x coordinate of the mouse location in coordinate space
	 * @param y
	 *            the y coordinate of the mouse location in coordinate space
	 */
	public void fireMousePressed(double x, double y) {
		if (mouseListeners != null)
			for (JVMouseListener listener : mouseListeners)
				listener.mousePressed(x, y);
	}

	/**
	 * Calls the mouseDragged function in all the mouse listeners which have
	 * been added to this object
	 * 
	 * @param x
	 *            the x coordinate of the mouse location in coordinate space
	 * @param y
	 *            the y coordinate of the mouse location in coordinate space
	 */
	public void fireMouseDragged(double x, double y) {
		if (mouseListeners != null)
			for (JVMouseListener listener : mouseListeners)
				listener.mouseDragged(x, y);
	}

	/**
	 * Calls the mouseReleased function in all the mouse listeners which have
	 * been added to this object
	 * 
	 * @param x
	 *            the x coordinate of the mouse location in coordinate space
	 * @param y
	 *            the y coordinate of the mouse location in coordinate space
	 */
	public void fireMouseReleased(double x, double y) {
		if (mouseListeners != null)
			for (JVMouseListener listener : mouseListeners)
				listener.mouseReleased(x, y);
	}

}
/*
 * CVS Log
 * 
 * $Log: VisualizationPrimitive.java,v $
 * Revision 1.2  2008/12/18 23:59:32  curran
 * Added a correlation matrix tool, and completed R integration.
 *
 * Revision 1.1  2007/08/15 17:59:16  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/30 23:43:03
 * ckellehe Cleaned up Javadoc
 * 
 * Revision 1.1 2007/07/26 00:30:59 ckellehe Initial Creation Revision 1.11
 * 2007/07/11 19:25:45 rbeaven merger of window computation and paint
 * 
 * Revision 1.10 2007/07/06 23:33:08 ckellehe Added JVImage
 * 
 * Revision 1.9 2007/07/03 17:40:29 ckellehe Created Heatmap
 * 
 * Revision 1.8 2007/06/27 20:56:54 ckellehe Got session replay working for
 * radViz mouse events Revision 1.7 2007/06/20 21:26:59 ckellehe Partially
 * implemented a Java scripting engine, so that Java code can now specify
 * callback functions for widgets, so can now use the wiget framework directly.
 * Used the Java engine to implement a text field + combo box duo, which was
 * previously implemented in Python. Now this text field + combo box interface
 * is available as a standard widget, and can be used in all supported languages
 * Created an example Groovy script that uses the text field + combo box
 * interface.
 * 
 * Revision 1.6 2007/06/19 19:15:02 ckellehe Added moving anchors to RadViz
 * 
 * Revision 1.5 2007/06/19 17:00:13 rbeaven Implemented the JVMouseListener
 * framework, added a test Groovy script for it
 * 
 * Revision 1.4 2007/06/16 03:05:00 ckellehe Changed the GlyphCollection
 * constructor to take only a panel. Added the convenience method record() to
 * JVObject. Improved the scatterplot Groovy script Revision 1.3 2007/06/08
 * 16:40:22 ckellehe Added a "fill" flag to JVObject, which determines whether
 * or not the shape is filled in, and implemented it in the appropriate
 * subclasses Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
