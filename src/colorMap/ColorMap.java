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
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class which performs mapping from values to colors. There are two modes of
 * color mapping: continuous and discrete. In continuous mode, the color map is
 * defined by a set of color-value pairs, whose values are between 0 and 1.
 * Input values are mapped to colors by performing linear interpolation between
 * the colors of the two color-value pairs whose values are closest to the input
 * value. In discrete mode, the color map is defined by an array of colors.
 * Input values are integers which are mapped to colors by looking up the color
 * in the array at the index of the input value.
 * 
 * @author Curran Kelleher
 * 
 */
public class ColorMap {

	/**
	 * The list of color-value pairs which define this color map.
	 * 
	 */
	public List<ColorNode> colorNodes;

	/**
	 * Temporary variable used when creating colors
	 */
	private float[] rgba_current = new float[4];

	/**
	 * Temporary variable used when creating colors
	 */
	private float[] rgba_next = new float[4];

	/**
	 * Temporary variable used when creating colors
	 */
	private float[] rgba_result = new float[4];

	/**
	 * True when this color map is in discrete mode
	 */
	private boolean discrete;

	/**
	 * The cached array of colors which will be queried by the getColor()
	 * method.
	 */
	Color[] colors;

	/**
	 * Construct a discrete ColorMap with the specified Colors. When this
	 * constructor is used, getColor(int discreteValue) should be used to
	 * retrieve colors, not getColor(double value)
	 * 
	 * @param colors
	 *            the colors which will comprise this color map initially.
	 */
	public ColorMap(Color[] colors) {
		discrete = true;
		setDiscreteColors(colors);
	}

	/**
	 * Construct a continuous ColorMap with the specified ColorNodes
	 * 
	 * @param colorNodes
	 *            the color nodes which will comprise this color map initially
	 */
	public ColorMap(ColorNode[] colorNodes) {
		discrete = false;
		ArrayList<ColorNode> colorNodesList = new ArrayList<ColorNode>(
				colorNodes.length);
		for (int i = 0; i < colorNodes.length; i++)
			colorNodesList.add(colorNodes[i]);
		setContinuousColors(colorNodesList);
	}

	/**
	 * Construct a continuous ColorMap with the specified ColorNodes
	 * 
	 * @param colorNodes
	 *            the color nodes which will comprise this color map initially
	 */
	public ColorMap(List<ColorNode> colorNodes) {
		setContinuousColors(colorNodes);
	}

	/**
	 * Construct a discrete ColorMap with the specified Colors
	 * 
	 * @param colors
	 *            the new array of colors for this discrete color map
	 */
	public void setDiscreteColors(Color[] colors) {
		this.colors = colors;
		colorNodes = new ArrayList<ColorNode>(colors.length);
		for (int i = 0; i < colors.length; i++)
			colorNodes.add(new ColorNode(colors[i], 0));
		alignDiscreteNodes();
	}

	/**
	 * Resets the values of the color nodes to reflect their discrete positions
	 * 
	 */
	public void alignDiscreteNodes() {
		Collections.sort(colorNodes);
		for (int i = 0; i < colorNodes.size(); i++)
			colorNodes.get(i).value = (double) i / colors.length;
	}

	/**
	 * Sets this ColorMap to be defined by the specified ColorNodes
	 * 
	 * @param colorNodes
	 */
	private void setContinuousColors(List<ColorNode> colorNodes) {
		colors = new Color[512];
		// create a copy so the internal list doesn't reflect changes to the
		// original list that was passed in
		this.colorNodes = new ArrayList<ColorNode>(colorNodes.size());
		this.colorNodes.addAll(colorNodes);
		calculateColors();
	}

	/**
	 * Populates the color array based on the current color nodes
	 * 
	 */
	public void calculateColors() {
		Collections.sort(colorNodes);
		if (isDiscrete())
			for (int i = 0; i < colorNodes.size(); i++)
				colors[i] = colorNodes.get(i).color;
		else
			for (int i = 0; i < colors.length; i++)
				colors[i] = createColorForValue((double) i / colors.length);
	}

	/**
	 * Creates a new Color object based on the current color nodes
	 * 
	 * @param value
	 *            the value between 0 and 1 for which to generate the
	 *            corresponding Color.
	 * @return a new Color object corresponding to the specified value on this
	 *         color map.
	 */
	private Color createColorForValue(double value) {
		if (colorNodes.size() == 1)
			return colorNodes.get(0).color;

		for (int i = 0; i < colorNodes.size() - 1; i++) {
			ColorNode current = colorNodes.get(i);
			double min = current.value;
			rgba_current = current.color.getComponents(rgba_current);

			ColorNode next = colorNodes.get(i + 1);
			double max = next.value;
			rgba_next = next.color.getComponents(rgba_next);

			// if the value is in the current range between color node
			// values...
			if (value >= min && value <= max) {
				// assign the color properly
				for (int c = 0; c < rgba_current.length; c++) {
					double percentBtwMinAndMax = (value - min) / (max - min);
					rgba_result[c] = (float) ((1.0 - percentBtwMinAndMax)
							* rgba_current[c] + percentBtwMinAndMax
							* rgba_next[c]);
				}
			}// if it is lower than the lowest node value
			else if (i == 0 && value < min)
				return current.color;
			else
			// if it is greater than the greatest node
			if (i == colorNodes.size() - 2 && value > max)
				return next.color;

		}
		return new Color(rgba_result[0], rgba_result[1], rgba_result[2],
				rgba_result[3]);
	}

	/**
	 * Gets the Color corresponding to the specified discrete value in this
	 * color map. This method will only function properly when the constructor
	 * ColorMap(Color[] colors) is used.
	 * 
	 * @param discreteValue
	 *            the integer value for which this method will return the
	 *            corresponding Color. If this value is less than 0 or greater
	 *            than the number of colors in this color map (n), the color
	 *            corresponding to 0 or n (respectively) will be returned.
	 * @return the Color corresponding to the specified value. (No new Color
	 *         object is allocated, a Color object from a pre-calculated array
	 *         is returned. see getColorArray())
	 */
	public Color getColor(int discreteValue) {
		int n = colors.length - 1;
		return colors[discreteValue < 0 ? 0 : discreteValue > n ? n
				: discreteValue];
	}

	/**
	 * Gets the Color corresponding to the specified value in this color map.
	 * 
	 * @param value
	 *            the value between 0 and 1 for which this method will return
	 *            the corresponding Color. If this value is less than 0 or
	 *            greater than 1, 0 or 1 (respectively) will be used instead.
	 * @return the Color corresponding to the specified value. (No new Color
	 *         object is allocated, a Color object from a pre-calculated array
	 *         is returned. see getColorArray())
	 */
	public Color getColor(double value) {
		int i = (int) (value * colors.length);
		int n = colors.length - 1;
		return colors[i < 0 ? 0 : i > n ? n : i];
	}

	/**
	 * Returns the precomputed array of Color objects which represent this color
	 * map.
	 * 
	 */
	public Color[] getColorArray() {
		return colors;
	}

	/**
	 * Draws this color map horizontally (leftmost = 0, rightmost = 1) in a
	 * rectangle with the specified x, y, width, and height.
	 * 
	 * @param g
	 *            the Graphics on which to draw the color map rectangle
	 * @param x
	 *            the x pixel coordinate of the rectangle
	 * @param y
	 *            the y pixel coordinate of the rectangle
	 * @param width
	 *            the width of the rectangle
	 * @param height
	 *            the height of the rectangle
	 */
	public void paintOnThis(Graphics g, int x, int y, int width, int height) {
		for (int col = 0; col < width; col++) {
			g.setColor(getColor((double) col / (width - 1)));
			g.drawLine(col, y, col, y + height);
		}
	}

	/**
	 * Gets the color nodes that comprise this color map.
	 * 
	 */
	public List<ColorNode> getColorNodes() {
		return colorNodes;
	}

	/**
	 * Generates the default continuous color map.
	 * 
	 * @return a newly allocated ColorMap object with default settings
	 */
	public static ColorMap getDefaultContinuousColorMap() {
		ColorNode[] colorNodes = { new ColorNode(Color.blue, 0),
				new ColorNode(Color.green, 0.5), new ColorNode(Color.red, 1) };
		return new ColorMap(colorNodes);

	}

	/**
	 * Generates the default discrete color map.
	 * 
	 * @return a newly allocated discrete ColorMap object with default settings
	 */
	public static ColorMap getDefaultDiscreteColorMap() {
		Color[] colors = { Color.blue, Color.green, Color.red, Color.yellow,
				Color.cyan, Color.orange, Color.pink, Color.magenta };
		return new ColorMap(colors);
	}

	/**
	 * 
	 * @return True when this color map is discrete, that is, when the
	 *         constructor ColorMap(Color[] colors) is used. Returns false if
	 *         this color map is continuous.
	 */
	public boolean isDiscrete() {
		return discrete;
	}

}
/*
 * CVS Log
 * 
 * $Log: ColorMap.java,v $
 * Revision 1.1  2007/08/15 17:59:11  curran
 * Initial commit to SourceForge
 * Revision 1.6 2007/08/03 21:46:20 ckellehe Added PNG
 * image export
 * 
 * Revision 1.5 2007/08/01 22:03:48 ckellehe Added color map editor GUI features
 * Revision 1.4 2007/07/30 23:43:04 ckellehe Cleaned up Javadoc
 * 
 * Revision 1.3 2007/07/26 19:35:49 ckellehe Got Parallel Coordinates working
 * Revision 1.2 2007/07/26 14:49:29 ckellehe Cleaned up documentation
 * 
 * Revision 1.1 2007/07/26 00:30:58 ckellehe Initial Creation
 * 
 * 
 */
