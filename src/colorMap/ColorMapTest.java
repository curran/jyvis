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

import javax.swing.JFrame;

/**
 * A test for ColorMap and ColorMapEditorPanel
 * 
 * @author Curran Kelleher
 * 
 */

public class ColorMapTest {

	public static void main(String[] args) {
		colorMapTest();
		discreteColorMapTest();
	}

	private static void discreteColorMapTest() {
		JFrame f = new JFrame();
		f.setBounds(200, 200, 400, 50);
		// define a list of ColorNodes
		Color[] colors = { Color.cyan, Color.yellow, Color.red };
		// create a new color map based on the list of nodes, create a new
		// editor for that color map, and add that editor to the frame
		f.add(new ColorMapEditorPanel(new ColorMap(colors)));

		// show the frame
		f.setVisible(true);
	}

	private static void colorMapTest() {
		JFrame f = new JFrame();
		f.setBounds(200, 200, 400, 50);
		// define a list of ColorNodes
		ColorNode[] colorNodes = { new ColorNode(Color.cyan, 0.2),
				new ColorNode(Color.yellow, 0.7), new ColorNode(Color.red, 1) };
		// create a new color map based on the list of nodes, create a new
		// editor for that color map, and add that editor to the frame
		f.add(new ColorMapEditorPanel(new ColorMap(colorNodes)));
		// f.add(new ColorMapEditorPanel(ColorMap.getDefaultColorMap()));
		// show the frame
		f.setVisible(true);
	}

}
/*
 * CVS Log
 * 
 * $Log: ColorMapTest.java,v $
 * Revision 1.1  2007/08/15 17:59:11  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/26 14:49:29 ckellehe Cleaned
 * up documentation
 * 
 * Revision 1.1 2007/07/26 00:30:58 ckellehe Initial Creation
 * 
 * 
 */
