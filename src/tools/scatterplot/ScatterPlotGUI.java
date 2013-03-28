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

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jyVis.data.DataDimensionMetadata;
import jyVis.visualizationPrimitives.Axis;
import jyVis.widgets.JLCheckBox;
import jyVis.widgets.JLColorChooserButton;
import jyVis.widgets.JLColorMapEditorPanel;
import jyVis.widgets.JLComboBox;
import jyVis.widgets.JLComboBoxTextFieldDuo;
import jyVis.widgets.JLShapeMapEditorPanel;
import jyVis.widgets.UIUtils;
import jyVis.widgets.JLComboBoxTextFieldDuo.Entry;
import scripting.JavaFunction;
import tools.scatterplot.ScatterPlotBean.AxisBean;

/**
 * The GUI for scatterplot
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class ScatterPlotGUI extends JPanel {
	ScatterPlotBean plotBean;

	ScatterPlot plot;

	public ScatterPlotGUI(ScatterPlot plot,ScatterPlotBean bean) {
		this.plot = plot;
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		plotBean = bean;

		p.add(createDimensionsGUI());
		p.add(createAppearancesGUI());

		this.add(p);
	}

	JPanel createDimensionsGUI() {
		JPanel p = UIUtils.createTitledPanel("Dimensions");

		List<DataDimensionMetadata> dims = plot.data.dimensions;
		p.add(new JLabel("X"));
		p.add(new JLComboBox(dims, plotBean, "XDimension"));
		p.add(new JLabel("Y"));
		p.add(new JLComboBox(dims, plotBean, "YDimension"));
		p.add(new JLabel("Size"));
		p.add(new JLComboBox(dims, plot.glyphs, "SizeDimension"));
		p.add(new JLabel("Color"));
		p.add(new JLComboBox(dims, plot.glyphs, "ColorDimension"));
		p.add(new JLabel("Shape"));
		p.add(new JLComboBox(dims, plot.glyphs, "ShapeDimension"));
		p.add(new JLabel("Probing"));
		p.add(new JLComboBox(dims, plot, "ProbingDimension"));

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

		p.add(new JLabel("Title Properties"));
		p.add(UIUtils.createTitlePropertiesGUI(plot.title, plot));
		p.add(new JLabel("X Axis"));
		p.add(createAxisUI(plotBean.x));
		p.add(new JLabel("Y Axis"));
		p.add(createAxisUI(plotBean.y));

		for (Component c : p.getComponents())
			((JComponent) c).setAlignmentX(0.05f);
		p.setAlignmentX(0.5f);

		return p;
	}

	private Component createAxisUI(AxisBean axisBean) {
		List<Entry> entries;
		Axis axis = axisBean.axis.axis;
		entries = new ArrayList<Entry>();
		entries.add(new Entry("Label Size", axis.label, "Size"));
		entries.add(new Entry("Label X", axis, "LabelX"));
		entries.add(new Entry("Label Y", axis, "LabelY"));
		entries.add(new Entry("Label Rotation", axis, "LabelRotation"));
		entries.add(new Entry("Numbers Rotation", axis, "NumbersRotation"));
		entries.add(new Entry("# Major Ticks", axis, "NumMajorTickMarks"));
		entries.add(new Entry("# Minor Ticks", axis, "NumMinorTickMarks"));
		entries.add(new Entry("Minimum Value", axisBean, "MinValue"));
		entries.add(new Entry("Maximum Value", axisBean, "MaxValue"));

		return new JLComboBoxTextFieldDuo(entries, new JavaFunction(plot,
				"updateDisplay"));
	}
}
