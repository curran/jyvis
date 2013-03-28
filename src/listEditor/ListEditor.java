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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A composite widget for editing lists - moving elements up or down and
 * excluding/including them. This widget is composed of two JLists and 4
 * buttons. On the left is the list containing excluded elements, on the right
 * is the list containing included elements, and in the center there are two
 * buttons for transferring selected elements between the included/excluded
 * lists. At the bottom of the list of included elements are two buttons for
 * moving selected elements up or down.
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class ListEditor extends JPanel {
	Object[] list;

	List<Integer> includedIndices = new ArrayList<Integer>();

	List<Integer> excludedIndices = new ArrayList<Integer>();

	ListEditorModel inListModel = new ListEditorModel(includedIndices);

	JList inList = new JList(inListModel);

	ListEditorModel notInListModel = new ListEditorModel(excludedIndices);

	JList notInList = new JList(notInListModel);

	List<ListEditorListener> listeners = new ArrayList<ListEditorListener>();

	/**
	 * Construct a list editor for the specified list. Initially all elements
	 * are included.
	 * 
	 * @param list
	 */
	public ListEditor(List<?> list) {
		this(list.toArray());
	}

	/**
	 * Construct a list editor for the specified list. Initially all elements
	 * are included.
	 * 
	 * @param list
	 */
	public ListEditor(Object[] list) {
		this.list = list;
		for (int i = 0; i < list.length; i++)
			includedIndices.add(i);

		JButton upButton = new JButton("Up");
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveSelection(-1);
			}
		});
		JButton downButton = new JButton("Dn");
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveSelection(1);
			}
		});
		JButton leftButton = new JButton("<");
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectedIndices = inList.getSelectedIndices();
				if (selectedIndices.length != 0) {
					selectedIndices = notInListModel.add(inListModel
							.remove(selectedIndices));
					notInList.setSelectedIndices(selectedIndices);
					fireListChanged();
				}
			}
		});
		JButton rightButton = new JButton(">");
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectedIndices = notInList.getSelectedIndices();
				if (selectedIndices.length != 0) {
					selectedIndices = inListModel.add(notInListModel
							.remove(selectedIndices));
					inList.setSelectedIndices(selectedIndices);
					fireListChanged();
				}
			}
		});

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JScrollPane scrollPane = new JScrollPane(notInList);
		scrollPane.setPreferredSize(new Dimension(90, 200));
		add(scrollPane);
		add(createCenterPanel(leftButton, rightButton));
		add(createRightPanel(upButton, downButton));
	}

	private Component createCenterPanel(JButton leftButton, JButton rightButton) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(leftButton);
		p.add(rightButton);
		p.setMaximumSize(new Dimension(50, 50));

		return p;
	}

	private Component createRightPanel(JButton upButton, JButton downButton) {

		JPanel upDownPanel = new JPanel(new GridLayout(1, 2));
		upDownPanel.add(upButton);
		upDownPanel.add(downButton);

		JPanel p = new JPanel((new BorderLayout()));
		p.add(upDownPanel, BorderLayout.SOUTH);
		p.add(new JScrollPane(inList), BorderLayout.CENTER);
		p.setPreferredSize(new Dimension(90, 200));

		return p;
	}

	private void moveSelection(int offset) {
		int[] s = inList.getSelectedIndices();
		if (s.length != 0)
			if (s[0] + offset >= 0
					&& s[s.length - 1] + offset < includedIndices.size()) {
				inListModel.move(s, offset);
				for (int i = 0; i < s.length; i++)
					s[i] += offset;
				inList.setSelectedIndices(s);
				fireListChanged();
			}
	}

	protected void fireListChanged() {
		ListState listState = getListState();
		for (ListEditorListener listener : listeners)
			listener.ListChanged(listState);
	}

	/**
	 * Gets the current state of this list - the included and excluded indices
	 * in the order they appear on screen
	 * 
	 */
	public ListState getListState() {
		return new ListState(includedIndices, excludedIndices);
	}

	/**
	 * Sets the current state of this list - the included and excluded indices
	 * in the order they appear on screen. This causes an event to be fired to
	 * all listeners.
	 * 
	 */
	public void setListState(ListState state) {
		inListModel.set(state.includedIndices);
		notInListModel.set(state.excludedIndices);
		fireListChanged();
	}

	/**
	 * Adds a listener which will receive events when this list is edited.
	 * 
	 * @param listener
	 */
	public void addListEditorListener(ListEditorListener listener) {
		listeners.add(listener);
	}

	private class ListEditorModel extends AbstractListModel {
		List<Integer> indices;

		public ListEditorModel(List<Integer> indices) {
			this.indices = indices;
		}

		public Object getElementAt(int i) {
			return list[indices.get(i)];
		}

		public int[] add(List<Integer> indicesToAdd) {
			return add(0, indicesToAdd);
		}

		public void set(List<Integer> newIndices) {
			int oldSize = indices.size();
			int newSize = newIndices.size();
			int sizeDifference = newSize - oldSize;
			indices.clear();
			indices.addAll(newIndices);

			if (sizeDifference > 0)
				fireIntervalAdded(this, oldSize, newSize - 1);
			else if (sizeDifference < 0)
				fireIntervalRemoved(this, newSize, oldSize - 1);

			fireContentsChanged(this, 0, newIndices.size() - 1);
		}

		private int[] add(int index, List<Integer> indicesToAdd) {
			if (indicesToAdd.size() != 0) {
				int min = indices.size();
				int max = min + indicesToAdd.size() - 1;
				indices.addAll(index, indicesToAdd);
				fireIntervalAdded(this, min, max);
			}
			int[] newIndices = new int[indicesToAdd.size()];
			for (int i = 0; i < indicesToAdd.size(); i++)
				newIndices[i] = index + i;
			return newIndices;
		}

		public List<Integer> remove(int[] indicesToRemove) {
			List<Integer> result = new LinkedList<Integer>();
			int n = indicesToRemove.length;
			if (n != 0) {
				for (int i = 0; i < n; i++)
					result.add(indices.remove(indicesToRemove[i] - i));
				fireIntervalRemoved(this, indicesToRemove[0],
						indicesToRemove[n - 1]);
			}
			return result;
		}

		public void move(int[] indicesToMove, int offset) {
			int n = indicesToMove.length;
			for (int i = 0; i < n; i++) {
				int indexOfObject = offset == -1 ? indicesToMove[i]
						: indicesToMove[n - i - 1];
				swap(indexOfObject, indexOfObject + offset);
			}
			fireIntervalRemoved(this, indicesToMove[0] + offset,
					indicesToMove[n - 1] + offset);
		}

		private void swap(int i, int j) {
			Integer temp = indices.get(i);
			indices.set(i, indices.get(j));
			indices.set(j, temp);
		}

		public int getSize() {
			return indices.size();
		}
	}
}
