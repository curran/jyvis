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

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jyVis.widgets.JLColorChooserButton;
import jyVis.widgets.JLColorMapEditorPanel;
import jyVis.widgets.JLComboBox;
import jyVis.widgets.JLListEditor;
import jyVis.widgets.UIUtils;

@SuppressWarnings("serial")
public class ParallelCoordinatesGUI extends JPanel {
	ParallelCoordinatesBean plotBean;

	ParallelCoordinates plot;

	public ParallelCoordinatesGUI(ParallelCoordinates plot) {
		this.plot = plot;

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		plotBean = new ParallelCoordinatesBean(plot);

		p.add(createDimensionsGUI());
		p.add(createAppearancesGUI());

		this.add(p);
	}

	JPanel createDimensionsGUI() {
		JPanel p = UIUtils.createTitledPanel("Dimensions");

		p.add(new JLListEditor(plot.data.dimensions.toArray(), plotBean,
				"DimensionsListState"));
		p.add(new JLabel(" "));
		p.add(new JLabel("Color Dimension"));
		p.add(new JLComboBox(plot.data.dimensions, plotBean, "ColorDimension"));

		for (Component c : p.getComponents())
			((JComponent) c).setAlignmentX(0.05f);
		p.setAlignmentX(0.5f);

		return p;
	}

	JPanel createAppearancesGUI() {
		JPanel p = UIUtils.createTitledPanel("Appearance");

		p.add(new JLabel("Color Map"));
		p.add(new JLColorMapEditorPanel(plotBean, "ColorMap"));
		p.add(new JLabel("Background Color"));
		p.add(new JLColorChooserButton(plot, "Background"));

		p.add(new JLabel("Title Properties"));
		p.add(UIUtils.createTitlePropertiesGUI(plot.title, plot));

		for (Component c : p.getComponents())
			((JComponent) c).setAlignmentX(0.05f);
		p.setAlignmentX(0.5f);

		return p;
	}
}
