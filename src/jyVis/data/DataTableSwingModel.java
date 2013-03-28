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
package jyVis.data;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * An implementation of Java's TableModel which uses a DataTable as it's model.
 * 
 * @author Curran Kelleher
 * 
 */
public class DataTableSwingModel implements TableModel {
	private DataTable data;

	/**
	 * Construct a swing TableModel which uses the specified DataTable as it's
	 * data model
	 * 
	 * @param data
	 */
	public DataTableSwingModel(DataTable data) {
		this.data = data;
	}

	/**
	 * Does nothing
	 */
	public void addTableModelListener(TableModelListener arg0) {

	}

	public Class<?> getColumnClass(int arg0) {
		return DataEntry.class;
	}

	public int getColumnCount() {
		return data.dimensions.size();
	}

	public String getColumnName(int index) {
		return data.dimensions.get(index).name;

	}

	public int getRowCount() {
		return data.records.size();
	}

	public Object getValueAt(int record, int dimension) {
		return data.records.get(record).get(dimension);
	}

	/**
	 * Always returns false
	 */
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	/**
	 * Does nothing
	 */
	public void removeTableModelListener(TableModelListener arg0) {

	}

	/**
	 * Does nothing, changing a data table is not supported
	 */
	public void setValueAt(Object arg0, int arg1, int arg2) {

	}
}
