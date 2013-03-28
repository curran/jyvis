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
package tools.parallelCoordinates;

import java.awt.Color;
import java.util.ArrayList;

import transformation.Transformation2D;

import jyVis.data.DataColorMap;
import jyVis.data.DataRecord;
import jyVis.data.DataTable;
import jyVis.data.Normalization;
import jyVis.graphics.DrawingPanel;
import jyVis.visualizationPrimitives.Axis;
import jyVis.visualizationPrimitives.Line;
import jyVis.visualizationPrimitives.TextLabel;
import listEditor.ListState;

/**
 * A parallel coordinates visualization tool
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class ParallelCoordinates extends DrawingPanel {
	/**
	 * title text displayed in the plot
	 */
	TextLabel title = new TextLabel(data.getName(), 0.5, 0.96);

	/**
	 * The list of the axes, which contains an Axis for each dimension
	 */
	ParallelCoordinatesAxis[] axes = new ParallelCoordinatesAxis[data.dimensions
			.size()];

	/**
	 * The list of records. Each record is a list of lines
	 */
	ListOfLines[] records = new ListOfLines[data.records.size()];

	/**
	 * The object which manages a color map, normalization, and persistent
	 * dimension index for assigning colors to records
	 */
	DataColorMap colorMap = new DataColorMap(data);

	/**
	 * The transformation which maps the visualization from the unit square to a
	 * smaller rectangle on the screen where it is drawn
	 */
	Transformation2D plotSpace = new Transformation2D(0.1, 0.9, 0.1, 0.85);

	/**
	 * The state encapsulating the active and inactive dimensions and their
	 * ordering
	 */
	ListState dimensionsListState = new ListState(data.dimensions);

	/**
	 * Construct a plot which will visualize the specified data
	 */
	public ParallelCoordinates(DataTable data) {
		super(data);
		add(title);

		// initialize the axes
		for (int i = 0; i < data.dimensions.size(); i++) {
			ParallelCoordinatesAxis axis = new ParallelCoordinatesAxis(i);
			axes[i] = axis;
			this.add(axis);
		}

		// initialize the records (each record is a list of lines)
		for (int i = 0; i < data.records.size(); i++) {
			ListOfLines listOfLines = new ListOfLines();
			records[i] = listOfLines;
			DataRecord record = data.records.get(i);
			for (Line line : listOfLines) {
				add(line);
				line.setUpForSelection(record, this);
			}
		}

		this.propertyPanel = new ParallelCoordinatesGUI(this);
		updateColors();
		updateLayout();
	}

	public void updateColors() {
		for (int i = 0; i < records.length; i++) {
			Color color = colorMap.getColor(i);
			for (Line line : records[i])
				line.setColor(color);
		}
	}

	public void updateLayout() {

		int numberOfActiveDimensions = dimensionsListState.includedIndices
				.size();
		int lineIndex = 0;

		// hide everything
		for (ParallelCoordinatesAxis axis : axes)
			axis.visible = false;
		for (ListOfLines record : records)
			for (Line line : record)
				line.fill = false;

		// show and layout what should be shown
		for (Integer activeDimensionIndex : dimensionsListState.includedIndices) {
			ParallelCoordinatesAxis axis = axes[activeDimensionIndex];
			axis.visible = true;

			// lay out the current axis
			double x = plotSpace.transformX((double) lineIndex
					/ (numberOfActiveDimensions - 1));
			axis.x1 = axis.x2 = x;
			axis.y1 = plotSpace.getRangeYMin();
			axis.y2 = plotSpace.getRangeYMax();

			// lay out the lines
			for (int i = 0; i < records.length; i++) {
				ListOfLines record = records[i];
				double y = plotSpace.transformY(axis.normalizedValues[i]);
				// lay out the line to the left of the current axis
				if (lineIndex > 0) {
					Line previousLine = record.get(lineIndex - 1);
					previousLine.fill = true;
					previousLine.x2 = x;
					previousLine.y2 = y;
				}
				// lay out the line to the right of the current axis
				if (lineIndex < numberOfActiveDimensions - 1) {
					Line nextLine = record.get(lineIndex);
					nextLine.fill = true;
					nextLine.x1 = x;
					nextLine.y1 = y;
				}
			}
			lineIndex++;
		}

		updateDisplay();
	}

	class ParallelCoordinatesAxis extends Axis {

		public double[] normalizedValues = new double[data.records.size()];

		public ParallelCoordinatesAxis(int dimension) {
			super(new Normalization(data));
			this.dimension = dimension;

			// set up the axis properties
			numMajorTickMarks = 2;
			labelRotation = 90;
			labelX = -.1;
			labelY = 0;
			numSpacingFactor = 1.1;
			numberOffset = 0;
			numbersYAlignment = "BOTTOM";

			// calculate the normalized values
			for (int record = 0; record < data.records.size(); record++)
				normalizedValues[record] = normalization.normalize(record,
						dimension);
		}
	}

	class ListOfLines extends ArrayList<Line> {
		public ListOfLines() {
			for (int i = 0; i < data.dimensions.size() - 1; i++)
				add(new Line());
		}
	}
}
