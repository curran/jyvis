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
package examples;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import jyVis.widgets.JLListEditor;
import listEditor.ListState;

public class JLListEditorTest {
	Object[] objects = new Object[] { "A", "B", "C", "D", "A", "B", "C", "D",
			"A", "B", "C", "D", "A", "B", "C", "D" };
	ListState listState = new ListState(objects);

	public JLListEditorTest() {
		showEditor(0);
		showEditor(300);
	}

	private void showEditor(int x) {
		JLListEditor listEditor = new JLListEditor(objects, this, "ListState");
		JFrame f = new JFrame();

		f.setBounds(x, x, 300, 300);
		f.add(listEditor);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new JLListEditorTest();
	}

	public ListState getListState() {
		return listState;
	}

	public void setListState(ListState listState) {
		this.listState = listState;
	}

}
