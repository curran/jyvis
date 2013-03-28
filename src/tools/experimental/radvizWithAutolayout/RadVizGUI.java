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
package tools.experimental.radvizWithAutolayout;

import java.awt.Component;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jyVis.data.DataDimensionMetadata;
import jyVis.visualizationPrimitives.TextLabel;
import jyVis.widgets.JLCheckBox;
import jyVis.widgets.JLColorChooserButton;
import jyVis.widgets.JLColorMapEditorPanel;
import jyVis.widgets.JLComboBox;
import jyVis.widgets.JLComboBoxTextFieldDuo;
import jyVis.widgets.JLListEditor;
import jyVis.widgets.JLShapeMapEditorPanel;
import jyVis.widgets.UIUtils;

/**
 * The property panel GUI for RadViz
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class RadVizGUI extends JPanel {
	RadVizBean plotBean;

	RadViz plot;

	public RadVizGUI(RadViz plot) {
		this.plot = plot;
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		add(p);

		plotBean = new RadVizBean(plot);

		p.add(createDimensionsGUI());
		p.add(createAppearancesGUI());
	}

	JPanel createDimensionsGUI() {
		JPanel p = UIUtils.createTitledPanel("Dimensions");

		List<DataDimensionMetadata> dims = plot.data.dimensions;
		p
				.add(new JLListEditor(dims.toArray(), plotBean,
						"DimensionsListState"));
		p.add(new JLabel(" "));
		p.add(new JLabel("Size Dimension"));
		p.add(new JLComboBox(dims, plot.glyphs, "SizeDimension"));
		p.add(new JLabel("Color Dimension"));
		p.add(new JLComboBox(dims, plot.glyphs, "ColorDimension"));
		p.add(new JLabel("Shape Dimension"));
		p.add(new JLComboBox(dims, plot.glyphs, "ShapeDimension"));

		for (Component c : p.getComponents())
			((JComponent) c).setAlignmentX(0.05f);
		p.setAlignmentX(0.5f);

		return p;
	}

	JPanel createAppearancesGUI() {
		JPanel p = UIUtils.createTitledPanel("Appearance");

		UIUtils.createGlyphSizeUI(plot.glyphs, p);

		p.add(new JLabel("Color Map"));
		p.add(new JLColorMapEditorPanel(plot.glyphs, "ColorMap"));
		p.add(new JLabel("Shape Map"));
		p.add(new JLShapeMapEditorPanel(plot.glyphs, "ShapeMap"));
		p.add(new JLCheckBox("Draw Outlines", plot.glyphs, "DrawOutlines"));
		p.add(new JLabel("Background Color"));
		p.add(new JLColorChooserButton(plot, "Background"));
		TextLabel title = plot.title;

		p.add(new JLabel("Title Properties"));
		JLComboBoxTextFieldDuo comboBoxTextFieldDuo = UIUtils
				.createTitlePropertiesGUI(title, plot);

		p.add(comboBoxTextFieldDuo);

		for (Component c : p.getComponents())
			((JComponent) c).setAlignmentX(0.05f);
		p.setAlignmentX(0.5f);

		return p;
	}

}
