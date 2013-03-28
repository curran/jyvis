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
package tools;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import jyVis.JyVis;
import jyVis.JyVisLauncher;
import jyVis.data.DataRecord;
import jyVis.data.DataSelection;
import jyVis.data.DataTable;
import jyVis.data.DataTableSwingModel;
import scripting.ScriptBottleneck;

/**
 * A table tool
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class Table extends JPanel implements Observer {
	DataTable data;

	DataTableSwingModel tableModel;

	JTable table;

	DataSelection selection = new DataSelection();

	boolean broadcastingSelection = false;

	boolean updatingTableSelection = false;

	JPopupMenu dimensionNameEditorPopup = new JPopupMenu();

	int dimensionForPopupToEdit;

	public Table(DataTable data) {

		this.data = data;
		tableModel = new DataTableSwingModel(data);
		table = new JTable(tableModel);

		setLayout(new GridLayout(1, 0));

		data.addObserver(this);

		ListSelectionListener listener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				broadcastSelection();
			}
		};
		table.getSelectionModel().addListSelectionListener(listener);

		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
		table.setRowSorter(new TableRowSorter<DataTableSwingModel>(tableModel));

		setUpPopupMenu();
	}

	private void setUpPopupMenu() {
		final JPanel parent = this;

		JMenuItem menuItem = new JMenuItem("Edit Dimension Name");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int editingDimension = dimensionForPopupToEdit;
				data.dimensions.get(editingDimension).name = JOptionPane
						.showInputDialog(parent,
								"Enter the new dimension name.",
								data.dimensions.get(editingDimension).name);
			}
		});
		dimensionNameEditorPopup.add(menuItem);

		final JTableHeader tableHeader = table.getTableHeader();
		tableHeader.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				showPopup(e);
			}

			public void mouseReleased(MouseEvent e) {
				showPopup(e);
			}

			private void showPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					dimensionForPopupToEdit = tableHeader.columnAtPoint(e
							.getPoint());
					dimensionNameEditorPopup.show(parent, e.getX(), e.getY());
				}
			}
		});
	}

	void broadcastSelection() {
		if (!updatingTableSelection) {
			selection.clear();
			for (Integer tableRowIndex : table.getSelectedRows()) {
				int recordIndex = table.convertRowIndexToModel(tableRowIndex);
				selection.add(data.records.get(recordIndex));
			}
			broadcastingSelection = true;
			data.setSelection(selection);
			data.updateDrawingPanels();
			broadcastingSelection = false;
		}
	}

	/**
	 * Received selection updates from the data table
	 */
	public void update(Observable arg0, Object arg1) {
		if (!broadcastingSelection) {
			ListSelectionModel selectionModel = table.getSelectionModel();
			updatingTableSelection = true;
			selectionModel.clearSelection();
			List<DataSelection> selections = data.getSelections();
			if (selections != null)
				for (DataSelection selection : selections)
					for (DataRecord selectedRecord : selection) {
						int selectedIndex = table
								.convertRowIndexToView(selectedRecord.index);
						selectionModel.addSelectionInterval(selectedIndex,
								selectedIndex);
					}
			updatingTableSelection = false;
		}

	}

	/**
	 * Provided for testing the table.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JyVisLauncher.launchJyVis();
		JyVis.openDataTable("run/data/iris.csv");
		ScriptBottleneck.execfile("run/Visualizations/Table.py");
	}
}
