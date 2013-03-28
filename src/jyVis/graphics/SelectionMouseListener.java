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

import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import jyVis.JyVis;
import jyVis.JyVisSettings;
import jyVis.data.DataRecord;
import jyVis.data.DataSelection;
import quadTree.QuadTree;
import scripting.ScriptBottleneck;

/**
 * The mouse listener which handles selection mouse interaction for drawing
 * panels.
 * 
 * @author Curran Kelleher
 * @see DrawingPanel
 * @see DrawingPanelMouseListener
 * 
 */
public class SelectionMouseListener {
	/**
	 * True when selection is being made in lasoo mode
	 */
	private boolean lasooMode = true;

	/**
	 * the drawing panel in which this listener will make selections.
	 */
	final DrawingPanel parentPanel;

	/**
	 * The points which outline the selection polygon
	 */
	List<Point> selectionPoints = new ArrayList<Point>();

	/**
	 * The re-usable list of selected data records
	 */
	DataSelection selectedRecords = new DataSelection();

	/**
	 * The re-usable selection polygon
	 */
	Polygon polygon;

	/**
	 * The current position of the mouse while it is being dragged
	 */
	Point b = new Point();

	/**
	 * Construct a selection listener which is linked with the specified
	 * JVDrawingPanel
	 * 
	 * @param parent
	 *            the drawing panel in which this listener will make selections.
	 */
	public SelectionMouseListener(DrawingPanel parent) {
		this.parentPanel = parent;
		// this is so the parent can receive key events
		parent.setFocusable(true);
		// add the key listener which will turn on drawing of the quadtree
		parent.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == JyVisSettings.drawQuadTreeModifierKey)
					QuadTree.paintQuadTree = !QuadTree.paintQuadTree;
			}
		});

	}

	/**
	 * Records the initial point. If the button is something other than the
	 * normal left button, then lasoo mode is used.
	 */
	public void mousePressed(MouseEvent e) {

		if (parentPanel.data != null) {
			// determine whether to use rectangle mode or lasoo mode
			lasooMode = e.getButton() == MouseEvent.BUTTON2
					|| e.getButton() == MouseEvent.BUTTON3;

			parentPanel.selectionPolygon = null;
			// get focus so the parent can receive key events
			parentPanel.requestFocusInWindow();
			DrawingPanel.selectionIsBeingMade = true;
			selectionPoints.clear();

			Point a = e.getPoint();
			selectionPoints.add(a);
			if (!lasooMode) {
				// if rectangle mode...
				// create the four vertices of the rectangle
				// selection, which
				// will be modified as the mouse is dragged

				selectionPoints.add(new Point(a.x, a.y));
				selectionPoints.add(new Point(a.x, a.y));
				selectionPoints.add(new Point(a.x, a.y));
			}

			if ((e.getModifiersEx() & JyVisSettings.multipleSelectionModifierKey) == JyVisSettings.multipleSelectionModifierKey)
				// add a new selection to the list of selections
				parentPanel.data
						.addSelection(selectedRecords = new DataSelection());
			else {
				// clear all existing selections, and set the
				// current selection
				List<DataSelection> selections = new ArrayList<DataSelection>();
				selections.add(selectedRecords);
				parentPanel.data.setSelections(selections);
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (parentPanel.data != null) {
			// just move the selection polygon if in brushing mode
			if ((e.getModifiersEx() & JyVisSettings.brushingSelectionModifierKey) == JyVisSettings.brushingSelectionModifierKey) {
				int diffX = b.x - e.getX();
				int diffY = b.y - e.getY();
				for (Point p : selectionPoints) {
					p.x -= diffX;
					p.y -= diffY;
				}
				b.x = e.getX();
				b.y = e.getY();
			} else {
				b = e.getPoint();
				if (lasooMode) {
					// add the current point to the list of points from which
					// the selection polygon will be made
					selectionPoints.add(e.getPoint());
				} else {
					// modify the existing points to reflect the current
					// selection rectangle
					Point a = selectionPoints.get(0);

					selectionPoints.get(1).setLocation(b.x, a.y);
					selectionPoints.get(2).setLocation(b);
					selectionPoints.get(3).setLocation(a.x, b.y);
				}
			}
			// build the polygon from the points
			int n = selectionPoints.size();
			int[] xs = new int[n];
			int[] ys = new int[n];
			for (int i = 0; i < n; i++) {
				xs[i] = selectionPoints.get(i).x;
				ys[i] = selectionPoints.get(i).y;
			}
			polygon = new Polygon(xs, ys, n);

			parentPanel.selectionPolygon = polygon;
			if (JyVisSettings.updateSelectionWhileDragging)
				determineSelectedRecords();
			else
				parentPanel.repaint();
		}
	}

	/**
	 * Clears the list "selectedRecords", determines a new selection based on
	 * the current selection polygon "polygon", adds the newly selected records
	 * to "selectedRecords"
	 * 
	 */
	private void determineSelectedRecords() {
		selectedRecords.clear();
		parentPanel.index.performSpatialQuery(polygon, selectedRecords);
		parentPanel.data.updateDrawingPanels();
	}

	private void determineSelectedRecords(Point point) {
		DataRecord record = parentPanel.index.performPointQueryForRecord(point);
		selectedRecords.clear();
		if (record != null)
			selectedRecords.add(record);
		parentPanel.data.updateDrawingPanels();
	}

	public void mouseReleased(MouseEvent e) {
		if (parentPanel.data != null) {
			DrawingPanel.selectionIsBeingMade = false;

			// if no selection polygon was made
			if (parentPanel.selectionPolygon == null) {
				determineSelectedRecords(e.getPoint());
				if (selectedRecords.size() == 0
						&& (e.getModifiersEx() & JyVisSettings.multipleSelectionModifierKey) != JyVisSettings.multipleSelectionModifierKey)
					parentPanel.data.setSelections(null);
				parentPanel.data.updateDrawingPanels();
			} else // if a selection polygon was made
			{
				// get rid of the selection polygon
				parentPanel.selectionPolygon = null;

				if (!JyVisSettings.updateSelectionWhileDragging) {
					determineSelectedRecords();
				} else {
					if (selectedRecords.size() == 0
							&& (e.getModifiersEx() & JyVisSettings.multipleSelectionModifierKey) != JyVisSettings.multipleSelectionModifierKey)
						parentPanel.data.setSelections(null);
					parentPanel.data.updateDrawingPanels();
				}
			}
			// log the change in selection
			logSelectionChange();
		}
	}

	/**
	 * Executes a script through the script bottleneck which sets the selection
	 * to be the current one. This is necessary for replay of selections. Base
	 * 64 encoding is used
	 * 
	 */
	public void logSelectionChange() {
		List<String> selections = parentPanel.data.getSelectionsAsBits();
		StringBuffer b = new StringBuffer();

		if (selections != null) {
			b.append("[");
			for (int i = 0; i < selections.size(); i++)
				b.append("\"" + selections.get(i) + "\""
						+ (i == selections.size() - 1 ? "" : ","));
			b.append("]");
		} else
			b.append("None");

		ScriptBottleneck.exec("python", "JyVis.getDataTables()["
				+ JyVis.getDataTables().indexOf(parentPanel.data)
				+ "].setSelectionsAsBits(" + b.toString() + ")",
				ScriptBottleneck.selectionEventCompressionKey);

	}
}
/*
 * CVS Log
 * 
 * $Log: SelectionMouseListener.java,v $
 * Revision 1.1  2007/08/15 17:59:21  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/30 23:43:04
 * ckellehe Cleaned up Javadoc
 * 
 * Revision 1.1 2007/07/26 00:30:59 ckellehe Initial Creation Revision 1.1
 * 2007/06/20 19:25:35 ckellehe Added a probing framework Revision 1.5
 * 2007/06/20 16:26:39 ckellehe Refactored mouse listener structure of
 * JVDrawingPanel Revision 1.4 2007/06/19 21:53:54 ckellehe Fixed some bugs with
 * the JVMouselistener framework Revision 1.3 2007/06/19 17:00:13 rbeaven
 * Implemented the JVMouseListener framework, added a test Groovy script for it
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
