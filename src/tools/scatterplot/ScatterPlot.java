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
package tools.scatterplot;

import jyVis.data.DataDimensionMetadata;
import jyVis.data.DataTable;
import jyVis.data.Normalization;
import jyVis.graphics.DrawingPanel;
import jyVis.visualizationPrimitives.Axis;
import jyVis.visualizationPrimitives.Glyph;
import jyVis.visualizationPrimitives.GlyphCollection;
import jyVis.visualizationPrimitives.TextLabel;
import transformation.Interval;
import transformation.Transformation;

/**
 * A scatterplot visualization tool
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class ScatterPlot extends DrawingPanel {

	/**
	 * Collection of the gylphs and their properties
	 */
	GlyphCollection glyphs = new GlyphCollection(this);

	/**
	 * The properties of the X axis
	 */
	ScatterplotAxis x = new ScatterplotAxis();

	/**
	 * The properties of the Y axis
	 */
	ScatterplotAxis y = new ScatterplotAxis();

	/**
	 * title text displayed in the plot
	 */
	TextLabel title = new TextLabel(data.getName(), 0.5, 0.96);
	
	public ScatterPlotBean bean = new ScatterPlotBean(this);

	/**
	 * persistent ranges for each dimension
	 */
	public Interval[] dimensionIntervals = new Interval[data.dimensions.size()];

	/**
	 * public constructor
	 * 
	 * @param data
	 *            reference to the associated data table
	 */
	public ScatterPlot(DataTable data) {
		super(data);
		initializeAxes();
		
		propertyPanel = new ScatterPlotGUI(this,bean);

		add(title);
		add(x.axis);
		add(y.axis);

		for (int i = 0; i < dimensionIntervals.length; i++) {
			DataDimensionMetadata dim = data.dimensions.get(i);
			dimensionIntervals[i] = new Interval(dim.minValue, dim.maxValue);
		}

		updateXY();
	}

	private void initializeAxes() {
		setXRangeOnScreen(0.14, 0.93);
		x.axis.labelY = 0.07;
		x.axis.numberOffset = -0.025;

		setYRangeOnScreen(0.11, 0.9);
		y.axis.labelY = -0.11;
		y.axis.numbersXAlignment = "RIGHT";
	}

	public void setXRangeOnScreen(double min, double max) {
		x.axis.x1 = min;
		x.axis.x2 = max;
		x.setOutputInterval(min, max);
		y.axis.x1 = y.axis.x2 = x.axis.x1;
	}

	public void setYRangeOnScreen(double min, double max) {
		y.axis.y1 = min;
		y.axis.y2 = max;
		y.setOutputInterval(min, max);
		x.axis.y1 = x.axis.y2 = y.axis.y1;
	}

	/**
	 * updates the x and y locations of the glyphs, then updates the display
	 */
	public void updateXY() {
		// find the normalized position of the glyphs
		for (Glyph glyph : glyphs) {
			int record = glyph.record();
			glyph.x = x.getOutputValue(record);
			glyph.y = y.getOutputValue(record);
		}

		// update the drawing panel
		updateDisplay();
	}

	/**
	 * A class which encapsulates the properties of a single scatterplot axis
	 */
	public class ScatterplotAxis {
		Axis axis;

		Normalization normalization;

		/**
		 * The transformation which stores the output interval for this axis
		 */
		private Transformation outputTransformation = new Transformation();

		public ScatterplotAxis() {
			normalization = new Normalization(data,
					new Normalization.DimensionIntervalMapping() {
						public void setDomainFromDimension(int dimension,
								Interval domain) {
							domain.set(dimensionIntervals[dimension]);
						}
					});
			axis = new Axis(normalization);
		}

		/**
		 * Sets the minimum value on screen
		 * 
		 * @param min
		 */
		public void setOutputInterval(double min, double max) {
			outputTransformation.range.min = min;
			outputTransformation.range.max = max;
		}

		/**
		 * Gets the output value on screen in this axis for the specified record
		 * 
		 */
		public double getOutputValue(int record) {
			return outputTransformation.transform(normalization.normalize(
					record, axis.dimension));
		}
	}
}
