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
package jyVis;

import java.awt.Color;
import java.awt.event.KeyEvent;

import colorMap.ColorMap;

/**
 * The class which houses global system settings. When instantiated, this class
 * is a Bean which gets ant sets the global values.
 * 
 * @author Curran Kelleher
 * 
 */
public class JyVisSettings {

	/**
	 * The default data directory - the initial location of the file chooser
	 * when the user is prompted to open a data set
	 */
	public static String defaultDataDirectory = JyVisSettings.runtimeRoot
			+ "data";

	/**
	 * The default session directory - the initial location of the file chooser
	 * when the user is prompted to save or load a session
	 */
	public static String defaultSessionDirectory = "sessions";

	/**
	 * The default session file extension
	 */
	public static final String sessionExtension = "session";

	/**
	 * The root of the directory structure in which files accessed at runtime
	 * reside, such as scripts and color maps
	 */
	public static final String runtimeRoot = "run/";

	/**
	 * The color map used for determinine the color to draw the selections.
	 */
	public static ColorMap selectionsColorMap = ColorMap
			.getDefaultDiscreteColorMap();

	/**
	 * The color that all objects will be drawn with on the buffer image which
	 * is drawn behind selections.
	 */
	public static Color colorOfObjectsBehindSelections = Color.lightGray;

	/**
	 * The default background color
	 */
	public static Color defaultBackgroundColor = Color.white;

	/**
	 * When true, selections are updated (and all relevant panels are repainted)
	 * as the selection is being created. If false, the selection is only
	 * updated when the selection is finished.
	 */
	public static boolean updateSelectionWhileDragging = true;

	/**
	 * The key which will enable making multiple selections.
	 */
	public static final int multipleSelectionModifierKey = KeyEvent.SHIFT_DOWN_MASK;

	/**
	 * The key which will enable brushing of selections. (meaning that when you
	 * hold down this key, the mouse will move the whole selection polygon
	 * around instead of changing it's shape)
	 */
	public static final int brushingSelectionModifierKey = KeyEvent.ALT_DOWN_MASK;

	/**
	 * The key which will turn on drawing of the quad tree.
	 */
	public static final char drawQuadTreeModifierKey = 'q';

	public String getDefaultDataDirectory() {
		return defaultDataDirectory;
	}

	public void setDefaultDataDirectory(String defaultDataDirectory) {
		JyVisSettings.defaultDataDirectory = defaultDataDirectory;
	}

	public String getDefaultSessionDirectory() {
		return defaultSessionDirectory;
	}

	public void setDefaultSessionDirectory(String defaultSessionDirectory) {
		JyVisSettings.defaultSessionDirectory = defaultSessionDirectory;
	}

	public String getSessionExtension() {
		return sessionExtension;
	}

	public String getRuntimeRoot() {
		return runtimeRoot;
	}

	public ColorMap getSelectionsColorMap() {
		return selectionsColorMap;
	}

	public void setSelectionsColorMap(ColorMap selectionsColorMap) {
		JyVisSettings.selectionsColorMap = selectionsColorMap;
	}

	public Color getColorOfObjectsBehindSelections() {
		return colorOfObjectsBehindSelections;
	}

	public void setColorOfObjectsBehindSelections(
			Color colorOfObjectsBehindSelections) {
		JyVisSettings.colorOfObjectsBehindSelections = colorOfObjectsBehindSelections;
	}

	public Color getDefaultBackgroundColor() {
		return defaultBackgroundColor;
	}

	public void setDefaultBackgroundColor(Color defaultBackgroundColor) {
		JyVisSettings.defaultBackgroundColor = defaultBackgroundColor;
	}

	public boolean getUpdateSelectionWhileDragging() {
		return updateSelectionWhileDragging;
	}

	public void setUpdateSelectionWhileDragging(
			boolean updateSelectionWhileDragging) {
		JyVisSettings.updateSelectionWhileDragging = updateSelectionWhileDragging;
	}

	public int getMultipleSelectionModifierKey() {
		return multipleSelectionModifierKey;
	}

	public int getBrushingSelectionModifierKey() {
		return brushingSelectionModifierKey;
	}

	public char getDrawQuadTreeModifierKey() {
		return drawQuadTreeModifierKey;
	}

}
