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
package tools.experimental.radvizWithAutolayout;

import jyVis.data.DataTable;
import jyVis.data.Normalization;
import jyVis.graphics.DrawingPanel;
import jyVis.visualizationPrimitives.Circle;
import jyVis.visualizationPrimitives.Glyph;
import jyVis.visualizationPrimitives.GlyphCollection;
import jyVis.visualizationPrimitives.JVMouseAdapter;
import jyVis.visualizationPrimitives.TextLabel;
import listEditor.ListState;

/**
 * A RadViz visualization tool
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class RadViz extends DrawingPanel {
	/**
	 * title text displayed in the plot
	 */
	TextLabel title = new TextLabel(data.getName(), 0.5, 0.96);

	/**
	 * The radius of the RadViz circle
	 */
	double radius = 0.3;

	/**
	 * The RadViz circle
	 */
	Circle radVizCircle = new Circle(.5, .5, radius);

	/**
	 * The list of the dimensional anchors
	 */
	public DimensionalAnchor[] anchors = new DimensionalAnchor[data.dimensions.size()];

	/**
	 * The collection of glyphs used to represent records. The glyphs are
	 * automatically added to the panel upon construction (see GlyphCollection
	 * documentation)
	 */
	GlyphCollection glyphs = new GlyphCollection(this);

	/**
	 * The state encapsulating the active and inactive dimensions and their
	 * ordering
	 */
	ListState dimensionsListState = new ListState(data.dimensions);

	/**
	 * A linear normalization used to normalize values for each dimensional
	 * anchor
	 */
	Normalization normalization = new Normalization(data);

	/**
	 * The factor determining how far beyond the RadViz circle to place the text
	 * labels
	 */
	double labelPosition = 1.1;

	/**
	 * Construct a plot which will visualize the specified data
	 */
	public RadViz(DataTable data) {
		super(data);
		// initialize the title
		add(title);

		// initialize the circle
		radVizCircle.fill = false;
		radVizCircle.drawOutline = true;
		add(radVizCircle);

		// initialize the dimensional anchors
		for (int i = 0; i < data.dimensions.size(); i++) {
			DimensionalAnchor anchor = new DimensionalAnchor(i);
			anchors[i] = anchor;
			this.add(anchor.circle);
			this.add(anchor.label);
		}

		// initialize the GUI
		this.propertyPanel = new RadVizGUI(this);
		this.makeWindowSquare = true;
		resetActiveDimensions();
		updateLayout();
		
		//TODO remove hard-coded value
		//RadVizAnchorLayoutEngine layoutEngine = new RadVizAnchorLayoutEngine();
		RadVizAnchorLayoutEngine.layoutAnchors(this, 4);
	}

	/**
	 * Updates the positions of all glyphs based on positions of dimensional
	 * anchors, then calls updateDisplay(), which re-renders everything.
	 */
	public void updateLayout() {
		int classifier = 4;
		RadVizAnchorLayoutEngine.layoutAnchors(this, classifier);
		double[] sumOfValues = new double[data.records.size()];

		// clear the glyph positions
		for (Glyph glyph : glyphs) {
			glyph.x = 0;
			glyph.y = 0;
		}

		// show and layout what should be shown
		for (Integer activeDimensionIndex : dimensionsListState.includedIndices) {
			DimensionalAnchor anchor = anchors[activeDimensionIndex];

			double cos = Math.cos(anchor.angle);
			double sin = Math.sin(anchor.angle);
			anchor.circle.x = cos * radius + .5;
			anchor.circle.y = sin * radius + .5;

			anchor.label.x = cos * radius * labelPosition + .5;
			anchor.label.y = sin * radius * labelPosition + .5;

			// add the vector from the current anchor to the glyph locations
			for (Glyph glyph : glyphs) {
				int record = glyph.record();
				double pull = anchor.normalizedValues[record];
				sumOfValues[record] += pull;
				glyph.x += cos * pull;
				glyph.y += sin * pull;
			}

		}

		// divide the positions by the sum of values, and
		// transform the glyphs to the RadViz circle
		for (Glyph glyph : glyphs) {
			double factor = radius / sumOfValues[glyph.record()];
			glyph.x = glyph.x * factor + .5;
			glyph.y = glyph.y * factor + .5;
		}

		// update the RadViz circle
		radVizCircle.radius = radius;

		// redraw
		updateDisplay();
	}

	void resetActiveDimensions() {
		// hide everything
		for (DimensionalAnchor anchor : anchors)
			anchor.hide();

		// show what should be shown and reset the angles
		int n = dimensionsListState.includedIndices.size();
		for (int i = 0; i < n; i++) {
			int activeDimensionIndex = dimensionsListState.includedIndices
					.get(i);
			DimensionalAnchor anchor = anchors[activeDimensionIndex];
			anchor.show();

			// lay out the current anchor
			anchor.setAngle(Math.PI * 2 * i / n);
		}
	}

	class DimensionalAnchor extends JVMouseAdapter {
		double angle;
		public double[] normalizedValues = new double[data.records.size()];
		public TextLabel label = new TextLabel();
		public Circle circle = new Circle();

		public DimensionalAnchor(int dimension) {
			// initialize the label
			label.text = data.dimensions.get(dimension).name;
			label.size = 15;

			// calculate the normalized values
			for (int record = 0; record < data.records.size(); record++)
				normalizedValues[record] = normalization.normalize(record,
						dimension);

			// set up mouse listening
			circle.addJVMouseListener(this);
		}

		public void setAngle(double angle) {
			this.angle = angle;
			boolean flip = Math.abs(angle) > Math.PI / 2;
			label.rotation = -Math.toDegrees(angle) + (flip ? 180 : 0);
			label.xAlignment = flip ? TextLabel.RIGHT : TextLabel.LEFT;
		}

		public void show() {
			circle.fill = true;
			label.fill = true;
		}

		public void hide() {
			circle.fill = false;
			label.fill = false;
		}

		public void mouseDragged(double x, double y) {
			setAngle(Math.atan2(y - .5, x - .5));
			updateLayout();
		}
	}
}
