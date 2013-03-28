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
package jyVis.graphics;

import java.awt.event.MouseEvent;

import jyVis.visualizationPrimitives.JVMouseListener;
import jyVis.visualizationPrimitives.VisualizationPrimitive;

/**
 * The class which manages mouse event delegations to visualization primitives
 * which have mouse listeners attached to them. This class functions inside a
 * JVDrawingPanelMouseListener.
 * 
 * @author Curran Kelleher
 * @see VisualizationPrimitive
 * @see JVMouseListener
 * @see DrawingPanelMouseListener
 * 
 */
public class JVMouseListenerDelegator {
	DrawingPanel parentPanel;

	/**
	 * This is set to be the JVObject which mouse events will be delegated to.
	 * This is set to null on mouseReleased.
	 */
	VisualizationPrimitive objectWithMouseFocus = null;

	public JVMouseListenerDelegator(DrawingPanel parent) {
		this.parentPanel = parent;
	}

	/**
	 * Assigns objectWithMouseFocus to be the object under the mouse point, or
	 * null if none there
	 * 
	 * @param e
	 */
	public void assignObjectWithMouseFocus(MouseEvent e) {
		objectWithMouseFocus = parentPanel.index
				.performPointQueryForListeningObject(e.getPoint());
	}

	/**
	 * Delegates a mouse pressed event to the JVObject with mouse focus if it
	 * exists.
	 * 
	 * @param e
	 *            the mouse event
	 * @return true if successful (if the JVObject with mouse focus is not null,
	 *         and the event has been delegated to it), false if the delegation
	 *         is not appropriate (if the JVObject with mouse focus is null)
	 */
	public boolean mousePressed(MouseEvent e) {
		boolean objectExists = objectWithMouseFocus != null;
		if (objectExists)
			objectWithMouseFocus.fireMousePressedScript(parentPanel.window
					.getXValue(e.getX()), parentPanel.window
					.getYValue(e.getY()));
		return objectExists;
	}

	/**
	 * Delegates a mouse released event to the JVObject with mouse focus if it
	 * exists.
	 * 
	 * @param e
	 *            the mouse event
	 * @return true if successful (if the JVObject with mouse focus is not null,
	 *         and the event has been delegated to it), false if the delegation
	 *         is not appropriate (if the JVObject with mouse focus is null)
	 */
	public boolean mouseReleased(MouseEvent e) {
		boolean objectExists = objectWithMouseFocus != null;
		if (objectExists) {
			objectWithMouseFocus.fireMouseReleasedScript(parentPanel.window
					.getXValue(e.getX()), parentPanel.window
					.getYValue(e.getY()));
			objectWithMouseFocus = null;
		}
		return objectExists;
	}

	/**
	 * Delegates a mouse dragged event to the JVObject with mouse focus if it
	 * exists.
	 * 
	 * @param e
	 *            the mouse event
	 * @return true if successful (if the JVObject with mouse focus is not null,
	 *         and the event has been delegated to it), false if the delegation
	 *         is not appropriate (if the JVObject with mouse focus is null)
	 */
	public boolean mouseDragged(MouseEvent e) {
		boolean objectExists = objectWithMouseFocus != null;
		if (objectExists)
			objectWithMouseFocus.fireMouseDraggedScript(parentPanel.window
					.getXValue(e.getX()), parentPanel.window
					.getYValue(e.getY()));
		return objectExists;
	}
}
/*
 * CVS Log
 * 
 * $Log: JVMouseListenerDelegator.java,v $
 * Revision 1.1  2007/08/15 17:59:20  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:30:59
 * ckellehe Initial Creation Revision 1.2 2007/06/27 20:56:54 ckellehe Got
 * session replay working for radViz mouse events
 * 
 * Revision 1.1 2007/06/20 19:25:35 ckellehe Added a probing framework
 * 
 * Revision 1.1 2007/06/20 16:26:39 ckellehe Refactored mouse listener structure
 * of JVDrawingPanel
 * 
 */
