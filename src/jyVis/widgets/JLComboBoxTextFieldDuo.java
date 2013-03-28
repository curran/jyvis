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

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;

import jyVis.JyVis;
import scripting.Binding;
import scripting.Coercer;
import scripting.Function;

/**
 * This is a combination of two existing widgets: JLComboBox and JLTextField.
 * The selected index in the combo box determines the content and function of
 * the text field.
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class JLComboBoxTextFieldDuo extends JPanel {
	/**
	 * The combo box part of this box + text field dynamic duo
	 */
	public JLComboBox comboBox;

	/**
	 * The text field part of this box + text field dynamic duo
	 */
	public JLTextField textField;

	Function updateFunction;

	private Integer selectedIndexInComboBox = 0;

	/**
	 * Construct a combo box + text field dynamic duo which calls back to the
	 * specified internal state getter and setter functions.
	 * 
	 * @param entries
	 *            the list of entries in the combo box. Each entry has a title
	 *            (the string displayed in the combo box), and a binding (which
	 *            is used by the text field when that entry is selected)
	 */
	public JLComboBoxTextFieldDuo(final List<Entry> entries) {
		super(new GridLayout(2, 1));
		comboBox = new JLComboBox(entries, new Function() {
			public Object call(Object arg) {
				return selectedIndexInComboBox;
			}
		}, new Function() {
			public Object call(Object arg) {
				selectedIndexInComboBox = (Integer) Coercer.getObjectAsType(
						arg, Integer.class);
				JyVis.updateAllWidgets();
				return null;
			}
		});

		textField = new JLTextField(new Function() {
			public Object call(Object arg) {
				return entries.get(selectedIndexInComboBox).binding
						.getInternalState();
			}
		}, new Function() {
			public Object call(Object arg) {
				entries.get(selectedIndexInComboBox).binding
						.setInternalState(Coercer.getObjectAsType(arg,
								String.class));
				if (updateFunction != null)
					updateFunction.call(null);
				return null;
			}
		});

		add(comboBox);
		add(textField);
	}

	public JLComboBoxTextFieldDuo(List<Entry> entries, Function updateFunction) {
		this(entries);
		this.updateFunction = updateFunction;
	}

	public static class Entry {
		String title;

		Binding binding;

		public Entry(String title, Binding binding) {
			this.title = title;
			this.binding = binding;
		}

		public Entry(String title, Object object, String property) {
			this(title, Binding.createBinding(object, property));
		}

		public String toString() {
			return title;
		}
	}
}
