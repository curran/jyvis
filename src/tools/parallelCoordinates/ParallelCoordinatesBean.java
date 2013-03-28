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

import listEditor.ListState;
import colorMap.ColorMap;

public class ParallelCoordinatesBean {
	ParallelCoordinates plot;

	public ParallelCoordinatesBean(ParallelCoordinates plot) {
		this.plot = plot;
	}

	public ColorMap getColorMap() {
		return plot.colorMap.colorMap;
	}

	public void setColorMap(ColorMap colorMap) {
		plot.colorMap.colorMap = colorMap;
		plot.updateColors();
		plot.updateDisplay();
	}

	public int getColorDimension() {
		return plot.colorMap.dimension;
	}

	public void setColorDimension(int colorDimension) {
		plot.colorMap.dimension = colorDimension;
		plot.updateColors();
		plot.updateDisplay();
	}

	public ListState getDimensionsListState() {
		return plot.dimensionsListState;
	}

	public void setDimensionsListState(ListState dimensionsListState) {
		plot.dimensionsListState = dimensionsListState;
		plot.updateLayout();
	}
}
