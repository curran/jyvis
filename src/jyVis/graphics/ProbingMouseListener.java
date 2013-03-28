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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.Timer;

import jyVis.data.DataRecord;
import jyVis.visualizationPrimitives.JVMouseListener;
import jyVis.visualizationPrimitives.VisualizationPrimitive;

/**
 * The class which handles mouse events which trigger probing. This class
 * functions inside a JVDrawingPanelMouseListener.
 * 
 * @author Curran Kelleher
 * @see VisualizationPrimitive
 * @see JVMouseListener
 * @see DrawingPanelMouseListener
 * 
 */
public class ProbingMouseListener {
	/**
	 * This is the delay in milliseconds that determines how long after the
	 * user's mouse stopped moving (when it is over a record object) the probing
	 * popup is displayed.
	 */
	public static int probingDelay = 300;

	Point mousePositionOnScreen = new Point();

	Point mousePositionOnPanel = new Point();

	Timer mouseHoverTimer;

	DrawingPanel parentPanel;

	DataRecord recordUnderMouse;

	JPanel popupPanel = new JPanel();

	PopupFactory factory = PopupFactory.getSharedInstance();

	Popup popup;

	/**
	 * Construct a probing mouse listener which will interact with the specified
	 * drawing panel
	 * 
	 * @param parent
	 */
	public ProbingMouseListener(DrawingPanel parent) {
		parentPanel = parent;
		mouseHoverTimer = new Timer(probingDelay, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				recordUnderMouse = parentPanel.index
						.performPointQueryForRecord(mousePositionOnPanel);
				if (recordUnderMouse != null)
					showPopupPanel(recordUnderMouse);
			}
		});
		mouseHoverTimer.setRepeats(false);
		popupPanel.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public void mouseMoved(MouseEvent e) {
		if (parentPanel.data != null) {
			mousePositionOnScreen.x = e.getXOnScreen();
			mousePositionOnScreen.y = e.getYOnScreen();
			mousePositionOnPanel.x = e.getX();
			mousePositionOnPanel.y = e.getY();

			mouseHoverTimer.restart();
			if (popup != null)
				popup.hide();
		}
	}

	private void showPopupPanel(DataRecord record) {
		popupPanel.removeAll();
		popupPanel.add(new JLabel(parentPanel.data.dimensions
				.get(parentPanel.probingDimension).name
				+ " : " + record.get(parentPanel.probingDimension).toString()));

		if (popup == null)
			initializePopupPanelSize();

		popup = factory.getPopup(parentPanel, popupPanel,
				mousePositionOnScreen.x - popupPanel.getWidth(),
				mousePositionOnScreen.y - popupPanel.getHeight());
		popup.show();

	}

	private void initializePopupPanelSize() {
		// the first time the popup gets displayed, it's size is 0,0, so it
		// ended up being drawn under the mouse, but only the first time. There
		// seems to be no way of updating the bounds of the popupPanel without
		// executing the following code.
		popup = factory.getPopup(parentPanel, popupPanel, 0, 0);
		popup.show();
		popup.hide();
	}

	public void mouseExited(MouseEvent e) {
		mouseHoverTimer.stop();
	}
}
