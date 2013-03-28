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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The top-level mouse listener added to every JVDrawingPanel. This class
 * contains three mouse listeners, one for selection, one for probing, and one
 * for delegating events to visualization primitives with mouse listeners
 * attached to them, and handles the logic of delegating events to each one.
 * 
 * @author Curran Kelleher
 * 
 */
public class DrawingPanelMouseListener extends MouseAdapter {
	/**
	 * The parent drawing panel which this listener has been added to
	 */
	DrawingPanel parentPanel;

	/**
	 * The mouse listener for selections.
	 */
	SelectionMouseListener selectionListener;

	/**
	 * The mouse listener for probing.
	 */
	ProbingMouseListener probingListener;

	/**
	 * The object which manages mouse event delegation to JVObjects which have
	 * JVMouseListeners attached to them. Mouse events are delegated to this
	 * object when appropriate.
	 */
	JVMouseListenerDelegator delegator;

	public DrawingPanelMouseListener(DrawingPanel parent) {
		this.parentPanel = parent;
		selectionListener = new SelectionMouseListener(parent);
		delegator = new JVMouseListenerDelegator(parent);
		probingListener = new ProbingMouseListener(parent);
	}

	public void mousePressed(MouseEvent e) {
		delegator.assignObjectWithMouseFocus(e);
		if (!delegator.mousePressed(e))
			selectionListener.mousePressed(e);
	}

	public void mouseDragged(MouseEvent e) {
		if (!delegator.mouseDragged(e))
			selectionListener.mouseDragged(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (!delegator.mouseReleased(e))
			selectionListener.mouseReleased(e);
	}

	public void mouseMoved(MouseEvent e) {
		probingListener.mouseMoved(e);
	}

	public void mouseExited(MouseEvent e) {
		probingListener.mouseExited(e);
	}

}
/*
 * CVS Log
 * 
 * $Log: DrawingPanelMouseListener.java,v $
 * Revision 1.1  2007/08/15 17:59:20  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/31 18:30:18
 * ckellehe Fixed probing bug where it doesn't go away, added click-sort feature
 * and active dimension editing in HeatMap
 * 
 * Revision 1.1 2007/07/26 00:30:59 ckellehe Initial Creation Revision 1.1
 * 2007/06/20 19:25:35 ckellehe Added a probing framework Revision 1.1
 * 2007/06/20 16:26:39 ckellehe Refactored mouse listener structure of
 * JVDrawingPanel
 * 
 */
