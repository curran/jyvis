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
package tools.radviz;

import listEditor.ListState;

/**
 * The bean providing access to properties of RadViz
 * 
 * @author Curran Kelleher
 * 
 */
public class RadVizBean {
	RadViz plot;

	public RadVizBean(RadViz plot) {
		this.plot = plot;
	}

	public ListState getDimensionsListState() {
		return plot.dimensionsListState;
	}

	public void setDimensionsListState(ListState dimensionsListState) {
		plot.dimensionsListState = dimensionsListState;
		plot.resetActiveDimensions();
		plot.updateLayout();
	}
}
