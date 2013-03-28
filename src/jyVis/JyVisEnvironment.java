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
package jyVis;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import jyVis.data.DataTable;
import jyVis.graphics.DrawingPanel;
import jyVis.widgets.JLInternalFrame;

/**
 * The JyVis base environment, which contains a window, a desktop (space for
 * internal frames), a split pane for property panels to go in, a list of data
 * tables
 * 
 * @author Curran Kelleher
 * 
 */
public class JyVisEnvironment {
	/**
	 * The desktop for the base window.
	 */
	JDesktopPane baseDesktop;

	/**
	 * The base window
	 */
	JFrame baseWindow;

	/**
	 * The split pane which contains the baseDesktop and the property panel for
	 * the currently focused tool, and is placed in the content pane of the
	 * baseWindow
	 */
	JSplitPane splitPane;

	/**
	 * The list of data tables
	 */
	List<DataTable> dataTables = new ArrayList<DataTable>();

	/**
	 * The currently selected data table
	 */
	DataTable selectedDataTable;

	/**
	 * The panel in the selected internal frame
	 */
	Component selectedPanel;

	/**
	 * The GUI for JyVis system-wide properties
	 */
	SystemPropertyPanel systemPropertiesPanel;

	public JFrame createBaseWindow() {
		if (baseWindow == null) {
			baseWindow = new JFrame("JyVis!");
			baseWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			// center the frame on the screen with a default size

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			// int frameWidth = 800, frameHeight = 600;
			int frameWidth = screenSize.width * 3 / 4, frameHeight = screenSize.height * 3 / 4;
			// int frameWidth = 800, frameHeight = 600;
			baseWindow.setBounds(screenSize.width / 2 - frameWidth / 2,
					screenSize.height / 2 - frameHeight / 2, frameWidth,
					frameHeight);
			baseDesktop = new JDesktopPane();

			baseDesktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

			// create the splitPane, which will contain the baseDesktop and the
			// property panel for the currently focused tool window
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					baseDesktop, null);
			splitPane.setResizeWeight(1);
			splitPane.setOneTouchExpandable(true);

			baseWindow.getContentPane().add(splitPane);
		}
		return baseWindow;
	}

	public void setUpInternalFrame(final Component panel, String title) {

		JLInternalFrame internalFrame = new JLInternalFrame(title);

		internalFrame.add(panel);

		internalFrame.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameActivated(InternalFrameEvent e) {
				selectedPanel = panel;
			}
		});

		baseDesktop.add(internalFrame);

		// put the frame in the center of the screen with a default size
		Dimension screenSize = baseDesktop.getSize();
		int frameWidth = Math.min(screenSize.width, 500);
		int frameHeight = Math.min(screenSize.height, 500);

		internalFrame.setInternalState(
				(screenSize.width - frameWidth - 260) / 2, screenSize.height
						/ 2 - frameHeight / 2, frameWidth, frameHeight, false,
				false, true);

		if (panel instanceof DrawingPanel) {
			final DrawingPanel drawingPanel = (DrawingPanel) panel;

			internalFrame.addInternalFrameListener(new InternalFrameAdapter() {
				public void internalFrameActivated(InternalFrameEvent e) {
					showPropertyPanel(drawingPanel.propertyPanel);
				}

				public void internalFrameClosing(InternalFrameEvent e) {
					showPropertyPanel(null);
				}
			});
			internalFrame.addComponentListener(new ComponentAdapter() {
				public void componentHidden(ComponentEvent e) {
					// clear out the property panel of this frame's
					// drawingPanel when this frame loses focus.
					showPropertyPanel(null);
				}
			});
			showPropertyPanel(drawingPanel.propertyPanel);
		}

	}

	/**
	 * Displays the specified thing in the non-baseDesktop part of the split
	 * pane.
	 * 
	 * @param propertyPanel
	 *            the Component to put in the split pane, can be null (which
	 *            just makes that side of the split pane shrink to almost
	 *            nothing)
	 */
	void showPropertyPanel(Component propertyPanel) {
		splitPane.setRightComponent(propertyPanel);
		splitPane.setDividerSize(10);
	}
}
