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
package listEditor;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ListEditor listEditor = new ListEditor(
				new Object[] { "A", "B", "C", "D", "A", "B", "C", "D", "A",
						"B", "C", "D", "A", "B", "C", "D" });

		listEditor.addListEditorListener(new ListEditorListener() {
			public void ListChanged(ListState state) {
				System.out
						.println("includedIndices = " + state.includedIndices);
				System.out
						.println("excludedIndices = " + state.excludedIndices);
			}
		});

		JFrame f = new JFrame();
		f.setBounds(0, 0, 300, 300);
		f.add(listEditor);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

}
