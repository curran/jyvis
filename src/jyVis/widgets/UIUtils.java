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
package jyVis.widgets;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jyVis.graphics.DrawingPanel;
import jyVis.visualizationPrimitives.GlyphCollection;
import jyVis.visualizationPrimitives.TextLabel;
import jyVis.widgets.JLComboBoxTextFieldDuo.Entry;
import scripting.Function;
import scripting.JavaFunction;

/**
 * A class containing some convenience methods for some UI tasks which are
 * common to many tools
 * 
 * @author Curran Kelleher
 * 
 */
public class UIUtils {
	/**
	 * Creates a JPanel with a titled border and a BoxLayout along the Y axis
	 * 
	 * @param title
	 *            the title for the border
	 */
	public static JPanel createTitledPanel(String title) {
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createTitledBorder(title));
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		return p;
	}

	/**
	 * Creates a text field+combo box duo for editing the text, size, x, y, and
	 * rotation of the given text label.
	 * 
	 * @param title
	 *            the text label
	 * @param plot
	 *            the drawing panel to call updateDisplay() in whenever a
	 *            property of the title is changed.
	 */
	public static JLComboBoxTextFieldDuo createTitlePropertiesGUI(
			TextLabel title, DrawingPanel plot) {
		List<Entry> entries = new ArrayList<Entry>();
		entries.add(new Entry("Title Text", title, "Text"));
		entries.add(new Entry("Title Size", title, "Size"));
		entries.add(new Entry("Title X", title, "X"));
		entries.add(new Entry("Title Y", title, "Y"));
		entries.add(new Entry("Title Rotation", title, "Rotation"));
		Function updateFunction = new JavaFunction(plot, "updateDisplay");
		JLComboBoxTextFieldDuo comboBoxTextFieldDuo = new JLComboBoxTextFieldDuo(
				entries, updateFunction);
		return comboBoxTextFieldDuo;
	}

	/**
	 * Creates the GUI for editing the min and max sizes for glyphs in a glyph
	 * collection
	 * 
	 * @param glyphs
	 * @param p
	 *            the panel to add the GUI elements to
	 */
	public static void createGlyphSizeUI(GlyphCollection glyphs, JPanel p) {
		p.add(new JLabel("Size Range"));
		JPanel split = new JPanel();
		split.setLayout(new BoxLayout(split, BoxLayout.X_AXIS));
		split.add(new JLTextField(glyphs, "SizeMin"));
		split.add(new JLTextField(glyphs, "SizeMax"));
		p.add(split);
	}

	/**
	 * Shows an error message dialog box
	 * 
	 * @param errorMessage
	 */
	public static void showErrorDialog(String errorMessage) {
		JOptionPane.showMessageDialog(null, errorMessage, "Error",
				JOptionPane.ERROR_MESSAGE);

	}
}
