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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import jyVis.JyVisSettings;
import jyVis.data.DataRecord;
import jyVis.data.DataSelection;
import jyVis.data.DataTable;
import jyVis.visualizationPrimitives.VisualizationPrimitive;
import quadTree.QuadTree;
import transformation.Window2D;

/**
 * The panel which draws visualization primitives and handles selection
 * interaction. The default plotting range for these objects is 0,0,1,1. Once an
 * object is added via the add(object) method, it is persistant.
 * 
 * @author Curran Kelleher
 */
@SuppressWarnings("serial")
public class DrawingPanel extends Component implements Observer {
	/**
	 * The objects which will be drawn on this pane
	 */
	private List<VisualizationPrimitive> objects = new ArrayList<VisualizationPrimitive>();

	/**
	 * The window into the coordinate space in which objects in this panel are
	 * drawn.
	 */
	public Window2D window = new Window2D();

	/**
	 * the property panel associated with this drawing panel.
	 */
	public Component propertyPanel;

	/**
	 * The polygon (in pixel space) which will be drawn to represent the
	 * selection as it is being made.
	 */
	public Polygon selectionPolygon;

	/**
	 * The object used to spacially index the visualization primitives for
	 * efficient selection and mouse interactions.
	 */
	public VisualizationPrimitiveIndex index = new VisualizationPrimitiveIndex(
			objects);

	/**
	 * A flag indicating whether a selection is in the process of being made or
	 * not. This is used to signal whether or not to use anti-aliasing for
	 * drawing selected objects
	 */
	public static boolean selectionIsBeingMade = false;

	/**
	 * The data table associated with this JVDrawingPanel for the purpose of
	 * selection.
	 */
	public DataTable data;

	/**
	 * The back buffer image
	 */
	private BufferedImage bufferImage;

	/**
	 * The back buffer image which is displayed behind a selection
	 */
	private BufferedImage bufferImageBehindSelection;

	/**
	 * The back buffer Graphics objects
	 */
	private Graphics2D bufferGraphics, bufferGraphicsBehindSelection;

	/**
	 * If true, then the window will always be square (not distorted). It is
	 * false by default
	 */
	public boolean makeWindowSquare = false;

	/**
	 * Variables for keeping track of when the size of the panel changes
	 */
	int oldWidth, oldHeight;

	/**
	 * The dimension used for probing in this panel
	 */
	public int probingDimension = 0;

	/**
	 * Construct an empty drawing panel which is associated with the specified
	 * DataTable for the purpose of selection.
	 * 
	 * @param data
	 *            the data whose records will be able to be selected by this
	 *            drawing panel.
	 */
	public DrawingPanel(DataTable data) {
		this();
		this.data = data;
		// add this panel as an observer of the data, so that it will get
		// notified when the selection changes
		data.addObserver(this);
	}

	/**
	 * Construct an empty drawing panel, with no selection capabilities.
	 * 
	 */
	public DrawingPanel() {
		// set up the selection mouse listener
		DrawingPanelMouseListener l = new DrawingPanelMouseListener(this);
		addMouseListener(l);
		addMouseMotionListener(l);
		setBackground(JyVisSettings.defaultBackgroundColor);
	}

	/**
	 * Draws the objects in this panel
	 */
	public void paint(Graphics g) {
		// update/create the back buffer if necessary
		if (bufferImage == null || oldWidth != getWidth()
				|| oldHeight != getHeight())
			updateBufferGraphics();

		List<DataSelection> selections = data == null ? null : data
				.getSelections();
		synchronized (this) {
			// if there is no selection...
			if (selections == null)
				// draw the buffered image
				g.drawImage(bufferImage, 0, 0, this);
			else {
				if (bufferImageBehindSelection == null)
					updateBufferBehindSelection();

				// draw the grayscale buffered image
				g.drawImage(bufferImageBehindSelection, 0, 0, this);

				// set anti-aliasing
				if (!selectionIsBeingMade)
					((Graphics2D) g).setRenderingHint(
							RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);

				// draw the selected objects
				Color temp;
				int numSelections = selections.size();
				for (int i = 0; i < numSelections; i++) {
					List<DataRecord> selectedRecords = selections.get(i);
					Color color = JyVisSettings.selectionsColorMap.getColor(i
							% JyVisSettings.selectionsColorMap.colorNodes
									.size());
					for (DataRecord r : selectedRecords) {
						Map<DrawingPanel, List<VisualizationPrimitive>> objects = r.objects;
						List<VisualizationPrimitive> list = objects.get(this);
						for (VisualizationPrimitive o : list) {
							temp = o.color;
							o.color = color;
							o.paint(g, window);
							o.color = temp;
						}
					}
				}
			}

			// draw the selection polygon if a selection is being made
			if (selectionPolygon != null) {
				// set the color to be the inverse of the background
				Color backgroundColor = getBackground();
				g.setColor(new Color(255 - backgroundColor.getRed(),
						255 - backgroundColor.getGreen(), 255 - backgroundColor
								.getBlue()));
				// draw the selection polygon
				g.drawPolygon(selectionPolygon);
			}
			if (QuadTree.paintQuadTree)
				index.paintQuadTree(g);
		}
	}

	public void setBackground(Color color) {
		super.setBackground(color);
		updateDisplay();
	}

	/**
	 * Redraws the objects without re-indexing them for selection and mouse
	 * interactions. Use this method instead of updateDisplay() when the
	 * position of things is not changing for faster performance
	 */
	public void updateDisplay() {
		updateBufferGraphics();
		repaint();
	}

	private void updateBufferGraphics() {
		int w = getWidth();
		int h = getHeight();
		if (w > 0 && h > 0) {
			synchronized (this) {
				if (bufferImage == null || oldWidth != w || oldHeight != h) {
					oldWidth = w;
					oldHeight = h;
					bufferImage = new BufferedImage(w, h,
							BufferedImage.TYPE_INT_RGB);
					bufferGraphics = bufferImage.createGraphics();
					bufferGraphics.setRenderingHint(
							RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
				}
				// set the current pixel space
				// update the window to reflect the current panel size
				window.setSize(getSize());

				if (makeWindowSquare)
					window.makeWindowSquare();

				// draw the background
				bufferGraphics.setColor(getBackground());
				bufferGraphics.fillRect(0, 0, w, h);

				// draw all of the objects
				synchronized (objects) {
					index.reset(window.getWidth(), window.getHeight());
					bufferImageBehindSelection = null;// a flag to recreate it
					for (VisualizationPrimitive o : objects)
						o.paint(bufferGraphics, window);
				}
			}
		}
	}

	private void updateBufferBehindSelection() {

		// if there is data
		if (data != null) {
			int w = getWidth();
			int h = getHeight();
			if (bufferImageBehindSelection == null) {
				// create the buffer image to paint behind selections
				bufferImageBehindSelection = new BufferedImage(w, h,
						BufferedImage.TYPE_4BYTE_ABGR);
				bufferGraphicsBehindSelection = bufferImageBehindSelection
						.createGraphics();
				bufferGraphicsBehindSelection.setRenderingHint(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
			}

			bufferGraphicsBehindSelection.setColor(getBackground());
			bufferGraphicsBehindSelection.fillRect(0, 0, w, h);

			Color temp;
			for (VisualizationPrimitive o : objects) {
				// paint for behind selections
				temp = o.color;
				o.color = JyVisSettings.colorOfObjectsBehindSelections;
				o.paint(bufferGraphicsBehindSelection, window);
				o.color = temp;
			}
		}
	}

	/**
	 * Adds a JVObject to this panel's list of objects to be drawn.
	 * 
	 * @param object
	 */
	public void add(VisualizationPrimitive object) {
		synchronized (objects) {
			objects.add(object);
		}
	}

	/**
	 * Adds the specified list of JVObjects to this panel's list of objects to
	 * be drawn.
	 * 
	 * @param objects
	 */
	public void add(List<VisualizationPrimitive> objects) {
		synchronized (objects) {
			this.objects.addAll(objects);
		}
	}

	/**
	 * Clears this panel's list of objects to be drawn.
	 * 
	 */
	public void clearObjects() {
		synchronized (objects) {
			objects.clear();
		}
	}

	/**
	 * This method is called when the selection in the data table changes.
	 */
	public void update(Observable arg0, Object arg1) {
		repaint();
	}

	/**
	 * Puts this drawing panel in a frame and shows it
	 * 
	 */
	public void showInFrame() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int frameWidth = screenSize.width, frameHeight = screenSize.height;
		showInFrame(frameWidth, frameHeight);
	}

	/**
	 * Puts this drawing panel in a frame with the specified size, centered on
	 * the screen, and shows it
	 * 
	 */
	public void showInFrame(int frameWidth, int frameHeight) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame f = new JFrame();
		f.setBounds(screenSize.width / 2 - frameWidth / 2, screenSize.height
				/ 2 - frameHeight / 2, frameWidth, frameHeight);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.add(this);
		f.setVisible(true);

		window.setSize(getSize());
	}

	/**
	 * Gets the dimension used for probing (when the text thing pops up on the
	 * screen when you hover over a record object)
	 */
	public int getProbingDimension() {
		return probingDimension;
	}

	/**
	 * Sets the dimension used for probing (when the text thing pops up on the
	 * screen when you hover over a record object)
	 * 
	 * @param probingDimension
	 *            the new probing dimension
	 */
	public void setProbingDimension(int probingDimension) {
		this.probingDimension = probingDimension;
	}

	/**
	 * Adjusts the window so that all objects that have been added to the panel
	 * are in view, and there is no distortion, then rerdaws the display
	 * 
	 */
	public void scaleToFit() {
		// necessary for computing bounding boxes
		updateBufferGraphics();
		Rectangle overallBoundingBox = new Rectangle();
		for (VisualizationPrimitive o : objects)
			overallBoundingBox.add(o.getBoundingBox());

		window.zoomToRectangle(overallBoundingBox);
		window.makeWindowSquare();
		updateDisplay();
	}
}
/*
 * CVS Log
 * 
 * $Log: DrawingPanel.java,v $
 * Revision 1.1  2007/08/15 17:59:21  curran
 * Initial commit to SourceForge
 * Revision 1.3 2007/08/03 21:46:20 ckellehe Added
 * PNG image export Revision 1.2 2007/07/30 23:43:04 ckellehe Cleaned up Javadoc
 * 
 * Revision 1.1 2007/07/26 00:30:59 ckellehe Initial Creation Revision 1.15
 * 2007/07/11 19:25:45 rbeaven merger of window computation and paint
 * 
 * Revision 1.14 2007/07/03 17:40:29 ckellehe Created Heatmap Revision 1.13
 * 2007/06/28 18:01:12 rbeaven *** empty log message ***
 * 
 * Revision 1.12 2007/06/28 17:57:25 rbeaven *** empty log message *** Revision
 * 1.11 2007/06/27 15:46:34 ckellehe Got sessions working
 * 
 * Revision 1.10 2007/06/20 19:25:35 ckellehe Added a probing framework Revision
 * 1.9 2007/06/20 16:26:39 ckellehe Refactored mouse listener structure of
 * JVDrawingPanel
 * 
 * Revision 1.8 2007/06/19 17:00:13 rbeaven Implemented the JVMouseListener
 * framework, added a test Groovy script for it
 * 
 * Revision 1.7 2007/06/18 20:12:09 rbeaven *** empty log message *** Revision
 * 1.6 2007/06/15 18:01:27 ckellehe Added an additional String parameter to
 * ScriptBottleneck.exec, specifying what language interpreter to use for
 * executing the script
 * 
 * Revision 1.5 2007/06/14 15:50:56 ckellehe Renamed JythonBottleneck to
 * ScriptBottleneck
 * 
 * Revision 1.4 2007/06/12 14:38:19 rbeaven Made scatterplot use GlyphCollection
 * 
 * Revision 1.3 2007/06/11 21:17:25 ckellehe Created GlyphCollection, improved
 * RadViz
 * 
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
