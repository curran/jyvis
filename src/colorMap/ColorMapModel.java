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
package colorMap;

import java.awt.Color;
import java.util.List;

/**
 * A model for a color map. This class is a Java Bean, so can be encoded using
 * Java's XMLEncoder.
 * 
 * @author Curran Kelleher
 * 
 */

public class ColorMapModel {
	/**
	 * The list of color-value pairs which define this color map.
	 */
	private List<ColorNode> colorNodes;

	/**
	 * True when this color map is discreet, that is, when the constructor
	 * ColorMap(Color[] colors) is used.
	 */
	private boolean discrete;

	/**
	 * The array of colors used by a discrete color map
	 */
	Color[] colors;

	/**
	 * Construct an empty ColorMapFileModel
	 * 
	 */
	public ColorMapModel() {
	}

	/**
	 * Constructs a file model from the given color map
	 * 
	 * @param colorMap
	 */
	public ColorMapModel(ColorMap colorMap) {
		setDiscrete(colorMap.isDiscrete());
		if (discrete)
			setColors(colorMap.getColorArray());
		else
			setColorNodes(colorMap.getColorNodes());
	}

	/**
	 * Creates a color map based on this ColorMapFileModel and returns it
	 */
	public ColorMap getAsColorMap() {
		if (discrete)
			return new ColorMap(colors);
		else
			return new ColorMap(colorNodes);
	}

	public List<ColorNode> getColorNodes() {
		return colorNodes;
	}

	public void setColorNodes(List<ColorNode> colorNodes) {
		this.colorNodes = colorNodes;
	}

	public boolean isDiscrete() {
		return discrete;
	}

	public void setDiscrete(boolean discrete) {
		this.discrete = discrete;
	}

	public Color[] getColors() {
		return colors;
	}

	public void setColors(Color[] colors) {
		this.colors = colors;
	}

}
/*
 * CVS Log
 * 
 * $Log: ColorMapModel.java,v $
 * Revision 1.1  2007/08/15 17:59:12  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/26 14:49:29 ckellehe
 * Cleaned up documentation
 * 
 * Revision 1.1 2007/07/26 00:30:58 ckellehe Initial Creation
 * 
 * 
 */
