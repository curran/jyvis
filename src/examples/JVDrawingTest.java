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

import jyVis.JyVis;
import jyVis.data.DataTable;
import jyVis.data.Normalization;
import jyVis.graphics.DrawingPanel;
import jyVis.visualizationPrimitives.Circle;

/**
 * An example of how selection is enabled in JVDrawingPanel, and how
 * Normalization can be used.
 * 
 * @author Curran Kelleher
 * 
 */
public class JVDrawingTest {
	public static void main(String[] args) {

		DataTable data = JyVis.getSelectedData();

		if (data != null) {

			DrawingPanel drawingPanel = new DrawingPanel(data);

			Normalization normalization = new Normalization(data);

			int n = data.records.size();

			for (int i = 0; i < n; i++) {
				Circle circle = new Circle();
				circle.setUpForSelection(data.records.get(i), drawingPanel);
				drawingPanel.add(circle);
				circle.x = normalization.normalize(i, 0);
				circle.y = normalization.normalize(i, 2);
			}

			drawingPanel.showInFrame();
		}
	}
}
