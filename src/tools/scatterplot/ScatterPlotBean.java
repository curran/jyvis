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

import tools.scatterplot.ScatterPlot.ScatterplotAxis;

/**
 * The class which provides getter and setter functions for properties of the
 * scatterplot which update the properties and also updates the display when it
 * is appropriate.
 * 
 * @author Curran Kelleher
 * 
 */
public class ScatterPlotBean {
	ScatterPlot plot;

	public AxisBean x, y;

	public ScatterPlotBean(ScatterPlot plot) {
		this.plot = plot;
		this.x = new AxisBean(plot.x);
		this.y = new AxisBean(plot.y);
	}

	public int getXDimension() {
		return plot.x.axis.dimension;
	}

	public void setXDimension(int dimension) {
		plot.x.axis.dimension = dimension;
		plot.updateXY();
	}

	public int getYDimension() {
		return plot.y.axis.dimension;
	}

	public void setYDimension(int dimension) {
		plot.y.axis.dimension = dimension;
		plot.updateXY();
	}

	public class AxisBean {
		ScatterplotAxis axis;

		public AxisBean(ScatterplotAxis axis) {
			this.axis = axis;
		}

		public double getMinValue() {
			return plot.dimensionIntervals[axis.axis.dimension].min;
		}

		public void setMinValue(double minValue) {
			plot.dimensionIntervals[axis.axis.dimension].min = minValue;
			plot.updateXY();
		}

		public double getMaxValue() {
			return plot.dimensionIntervals[axis.axis.dimension].max;
		}

		public void setMaxValue(double maxValue) {
			plot.dimensionIntervals[axis.axis.dimension].max = maxValue;
			plot.updateXY();
		}
	}
}
