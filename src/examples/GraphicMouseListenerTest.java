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
package examples;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import jyVis.graphics.DrawingPanel;
import jyVis.visualizationPrimitives.Circle;
import jyVis.visualizationPrimitives.JVMouseListener;

/**
 * An example of how mouse callback functions can be defined for objects added
 * to a DrawingPanel.
 * 
 * @author Curran Kelleher
 * 
 */
public class GraphicMouseListenerTest {

	public static void main(String[] args) {
		final DrawingPanel drawingPanel = new DrawingPanel();

		final Circle circle = new Circle(0.5, 0.5, 0.2);
		circle.addJVMouseListener(new JVMouseListener() {

			public void mouseDragged(double x, double y) {
				System.out.println("mouseDragged");
				circle.x = x;
				circle.y = y;
				drawingPanel.updateDisplay();
			}

			public void mousePressed(double x, double y) {
				System.out.println("mousePressed");
			}

			public void mouseReleased(double x, double y) {
				System.out.println("mouseReleased");
			}
		});
		drawingPanel.add(circle);

		JFrame frame = new JFrame();
		frame.setBounds(0, 0, 500, 500);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(drawingPanel);
		frame.setVisible(true);
	}

}
