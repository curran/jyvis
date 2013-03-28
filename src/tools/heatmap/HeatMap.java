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
package tools.heatmap;

import java.util.Arrays;
import java.util.Comparator;

import jyVis.data.DataColorMap;
import jyVis.data.DataRecord;
import jyVis.data.DataTable;
import jyVis.graphics.DrawingPanel;
import jyVis.visualizationPrimitives.JVMouseAdapter;
import jyVis.visualizationPrimitives.Rectangle;
import jyVis.visualizationPrimitives.TextLabel;
import listEditor.ListState;
import transformation.Transformation2D;

/**
 * A heat map visualization tool
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class HeatMap extends DrawingPanel implements
		Comparator<HeatMap.HeatMapRecord> {
	TextLabel title = new TextLabel(data.getName(), 0.5, 0.96);

	HeatMapRecord[] records = new HeatMapRecord[data.records.size()];

	TextLabel[] textLabels = new TextLabel[data.dimensions.size()];

	double textGapSize = 0.01;

	double labelsSize = 15;

	DataColorMap colorMap = new DataColorMap(data);

	Transformation2D plotSpace = new Transformation2D(0.2, 1, 0, 0.9);

	ListState dimensionsListState = new ListState(data.dimensions.toArray());

	int dimensionToSortBy;

	public HeatMap(DataTable data) {
		super(data);
		add(title);
		this.propertyPanel = new HeatMapGUI(this);
		for (int i = 0; i < data.records.size(); i++)
			records[i] = new HeatMapRecord(data.records.get(i), this);
		for (int i = 0; i < data.dimensions.size(); i++) {
			TextLabel textLabel = new TextLabel(data.dimensions.get(i).name);
			textLabel.xAlignment = TextLabel.RIGHT;
			textLabel.size = labelsSize;
			final int labelDimension = i;
			textLabel.addJVMouseListener(new JVMouseAdapter() {
				public void mousePressed(double x, double y) {
					dimensionToSortBy = labelDimension;
					calculateRectangles();
				}
			});
			add(textLabels[i] = textLabel);
		}
		calculateColors();
		calculateRectangles();
	}

	public void calculateColors() {
		int numRecords = data.records.size();
		int numDimensions = data.dimensions.size();
		for (int i = 0; i < numRecords; i++) {
			HeatMapRecord heatMapRecord = records[i];
			for (colorMap.dimension = 0; colorMap.dimension < numDimensions; colorMap.dimension++)
				heatMapRecord.rectangles[colorMap.dimension].color = colorMap
						.getColor(heatMapRecord.record.index);
		}
	}

	public void calculateRectangles() {
		Arrays.sort(records, this);
		int numRecords = data.records.size();
		int numActiveDimensions = dimensionsListState.includedIndices.size();

		// hide the excluded dimensions
		for (Integer excludedDimensionIndex : dimensionsListState.excludedIndices) {
			for (int i = 0; i < numRecords; i++) {
				Rectangle rectangle = records[i].rectangles[excludedDimensionIndex];
				rectangle.fill = false;
				rectangle.x1 = rectangle.x2 = rectangle.y1 = rectangle.y2 = -1;
			}
			textLabels[excludedDimensionIndex].fill = false;
		}

		// show and lay out the included dimensions
		for (int i = 0; i < numActiveDimensions; i++) {
			int dimensionIndex = dimensionsListState.includedIndices.get(i);
			for (int record = 0; record < numRecords; record++) {
				Rectangle rectangle = records[record].rectangles[dimensionIndex];
				rectangle.x1 = plotSpace.transformX((double) record
						/ numRecords);
				rectangle.y1 = plotSpace
						.transformY(1 - ((double) (i + 1) / numActiveDimensions));
				rectangle.x2 = plotSpace.transformX((double) (record + 1)
						/ numRecords);
				rectangle.y2 = plotSpace
						.transformY(1 - ((double) i / numActiveDimensions));
				rectangle.fill = true;
			}
			TextLabel textLabel = textLabels[dimensionIndex];
			textLabel.fill = true;
			textLabel.x = plotSpace.getRangeXMin() - textGapSize;
			textLabel.y = plotSpace
					.transformY(1 - ((double) (i + .5) / numActiveDimensions));
			textLabel.size = labelsSize;
		}
		updateDisplay();
	}

	class HeatMapRecord {
		Rectangle[] rectangles = new Rectangle[data.dimensions.size()];

		DataRecord record;

		public HeatMapRecord(DataRecord record, DrawingPanel panel) {
			this.record = record;
			for (int i = 0; i < data.dimensions.size(); i++) {
				Rectangle rectangle = new Rectangle();
				rectangle.setUpForSelection(record, panel);
				panel.add(rectangles[i] = rectangle);
			}
		}
	}

	public int compare(HeatMapRecord a, HeatMapRecord b) {
		return Double.compare(a.record.get(dimensionToSortBy).value, b.record
				.get(dimensionToSortBy).value);
	}
}
