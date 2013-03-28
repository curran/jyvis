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

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jyVis.widgets.JLCheckBox;
import jyVis.widgets.JLColorChooserButton;
import jyVis.widgets.JLColorMapEditorPanel;
import jyVis.widgets.UIUtils;

/**
 * The GUI for JyVis system-wide properties
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class SystemPropertyPanel extends JPanel {
	JyVisSettings settings = new JyVisSettings();

	public SystemPropertyPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		p.add(createSelectionsGUI());

		this.add(p);
	}

	JPanel createSelectionsGUI() {
		JPanel p = UIUtils.createTitledPanel("JyVis Properties");

		p.setPreferredSize(new Dimension(250, 250));

		p.add(new JLCheckBox("Realtime Selection", settings,
				"UpdateSelectionWhileDragging"));

		p.add(new JLabel("Non-Selected Color"));
		p.add(new JLColorChooserButton(settings,
				"ColorOfObjectsBehindSelections"));

		p.add(new JLabel("Default Background Color"));
		p.add(new JLColorChooserButton(settings, "defaultBackgroundColor"));

		p.add(new JLabel("Multiple Selection Color Map"));
		p.add(new JLColorMapEditorPanel(settings, "selectionsColorMap", false));

		for (Component c : p.getComponents())
			((JComponent) c).setAlignmentX(0.05f);
		p.setAlignmentX(0.5f);
		return p;
	}
}
