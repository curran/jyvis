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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.TreeMap;

/**
 * A generic table of data.
 * 
 * @author Curran Kelleher
 * @see DataRecord
 * @see DataEntry
 */
public class DataTable extends Observable {

	/**
	 * The list of records which make up this data table.
	 */
	public List<DataRecord> records;

	/**
	 * The current list of selections, that is, the list of lists of the
	 * currently selected records, or null if there is currently no selection.
	 */
	private List<DataSelection> selections;

	/**
	 * The metadata associated with each of the dimensions of this DataTable. If
	 * the data table is found to be non-rectangular, then this will be null
	 * (because it would be meaningless)
	 */
	public List<DataDimensionMetadata> dimensions;

	/**
	 * The name of this dataset, derived from the file name
	 */
	private String name;

	/**
	 * Constructs a data table from the given table of Strings. If a string
	 * represents a valid number, it becomes a data entry with a double value
	 * and no string value.If a string does not represent a valid number, it
	 * becomes a data entry with the original string value, and a double value
	 * based on that string's ID within that dimension.
	 * 
	 * @param stringsTable
	 *            the table of Strings build the data table from
	 */
	public DataTable(List<List<String>> stringsTable) {
		this(stringsTable, null);
	}

	/**
	 * Constructs a data table from the given table of Strings and name. If a
	 * string represents a valid number, it becomes a data entry with a double
	 * value and no string value.If a string does not represent a valid number,
	 * it becomes a data entry with the original string value, and a double
	 * value based on that string's ID within that dimension.
	 * 
	 * @param stringsTable
	 *            the table of Strings build the data table from
	 * @param name
	 *            the name of this dataset, used for displaying
	 */
	public DataTable(List<List<String>> stringsTable, String name) {

		this.name = name;
		records = new ArrayList<DataRecord>(stringsTable.size());

		int numberOfDimensions = stringsTable.get(0).size();

		// the map of strings to their IDs
		List<TreeMap<String, Integer>> stringIDMaps = new ArrayList<TreeMap<String, Integer>>(
				numberOfDimensions);

		// the counts of the number of Strings with unique IDs for each lookup
		// table
		int[] stringIDCounts = new int[numberOfDimensions];

		// populate the list of maps and
		DataRecord titleRecord = new DataRecord(numberOfDimensions, 0);
		for (int dimensionIndex = 0; dimensionIndex < numberOfDimensions; dimensionIndex++) {
			titleRecord.add(new DataEntry(stringsTable.get(0).get(
					dimensionIndex), 0.0));
			stringIDMaps.add(new TreeMap<String, Integer>());
			stringIDCounts[dimensionIndex] = 0;
		}
		records.add(titleRecord);

		// find the string which need to be assigned IDs
		for (int recordIndex = 1; recordIndex < stringsTable.size(); recordIndex++) {
			List<String> stringRecord = stringsTable.get(recordIndex);
			for (int dimensionIndex = 0; dimensionIndex < stringRecord.size(); dimensionIndex++) {
				String stringEntry = stringRecord.get(dimensionIndex);
				try {
					Double.parseDouble(stringEntry);
				} catch (Exception e) {
					stringIDMaps.get(dimensionIndex).put(stringEntry, 0);
				}
			}
		}

		// calculate the lexicographical ordering of string IDs
		for (int dimensionIndex = 0; dimensionIndex < numberOfDimensions; dimensionIndex++) {
			TreeMap<String, Integer> map = stringIDMaps.get(dimensionIndex);
			int i = 0;
			for (String s : map.keySet()) {
				map.put(s, new Integer(i));
				i++;
			}
		}

		// make the data entry objects and add them
		for (int recordIndex = 1; recordIndex < stringsTable.size(); recordIndex++) {
			List<String> stringRecord = stringsTable.get(recordIndex);

			DataRecord record = new DataRecord(stringRecord.size(),
					recordIndex - 1);
			for (int dimensionIndex = 0; dimensionIndex < stringRecord.size(); dimensionIndex++) {
				String stringEntry = stringRecord.get(dimensionIndex);
				try {
					record.add(new DataEntry(Double.parseDouble(stringEntry)));
				} catch (Exception e) {
					TreeMap<String, Integer> treeMap = stringIDMaps
							.get(dimensionIndex);
					Integer integer = treeMap.get(stringEntry);

					record.add(new DataEntry(stringEntry, integer));
				}
			}
			records.add(record);
		}
		// compute the metadata
		computeTableAttributes();
	}

	/**
	 * This must be called after building a DataTable. This method computes and
	 * populates the table metadata, which is absolutely necessary for the
	 * normalizations which visualizations use.
	 * 
	 */
	private void computeTableAttributes() {
		// only operate if the data is there
		if (records.size() > 0) {
			// assume the data is rectangular (to be confirmed later),
			// therefore all widths are the same, so use the width of the first
			// record as the width of every record
			int width = records.get(0).size();
			dimensions = new ArrayList<DataDimensionMetadata>(width);

			// create the dimension metadata
			for (int i = 0; i < width; i++)
				dimensions.add(new DataDimensionMetadata(i));

			// compute the maximum and minimum values, exclude the first row
			// because that row is assumed to contain the names of the
			// dimensions.
			boolean firstRow = true;
			for (DataRecord r : records) {
				if (firstRow)// skip the names row
				{
					firstRow = false;
					continue;
				}
				if (r.size() == width)
					for (int i = 0; i < width; i++) {
						double val = r.get(i).value;
						DataDimensionMetadata d = dimensions.get(i);
						if (val > d.maxValue)
							d.maxValue = val;
						if (val < d.minValue)
							d.minValue = val;
					}
				else {
					// if we are here, then the data table is not
					// rectangular,meaning that the contents of
					// dimensionMetadata is worthless
					dimensions = null;
					// so we'll just forget about it and return
					return;
				}
			}

			// if the data is tabular...
			if (dimensions != null && records.size() > 0) {
				// remove the row of names from the data
				DataRecord names = records.remove(0);
				// compute the names for the dimensions
				for (int i = 0; i < width; i++)
					dimensions.get(i).name = names.get(i).toString().trim();
			}
		}
	}

	/**
	 * Gets the specified value in the table.
	 * 
	 * @param record
	 *            the index of the record which the value is in
	 * @param dimension
	 *            the index of the dimension which contains the value
	 * @return The value in the table
	 */
	public double get(int record, int dimension) {
		return records.get(record).get(dimension).value;
	}

	/**
	 * Gets the current list of selections.
	 * 
	 * @return The current list of selections, or null if there is currently no
	 *         selection.
	 */
	public List<DataSelection> getSelections() {
		return selections;
	}

	/**
	 * Sets the current list of selections.
	 * 
	 * @param selections
	 *            The list of selections to set as the current one, or null if
	 *            there is currently no selection.
	 */
	public void setSelections(List<DataSelection> selections) {
		this.selections = selections;
	}

	/**
	 * Sets the current list of selections to be a new list with only the
	 * specified selection in it.
	 * 
	 * @param selection
	 *            The selection to set as the current one
	 */
	public void setSelection(DataSelection selection) {
		LinkedList<DataSelection> list = new LinkedList<DataSelection>();
		list.add(selection);
		setSelections(list);
	}

	/**
	 * Adds a selection to the list of selections.
	 * 
	 * @param selectedRecords
	 *            the new selection which will be added to the list of
	 *            selections.
	 */
	public void addSelection(DataSelection selectedRecords) {
		if (selectedRecords != null) {
			if (selections == null)
				selections = new ArrayList<DataSelection>();
			selections.add(selectedRecords);
		}
	}

	/**
	 * Sends a repaint notification to all drawing panels which are displaying
	 * this data.
	 * 
	 */
	public void updateDrawingPanels() {
		setChanged();
		notifyObservers();
	}

	/**
	 * @return the name of this dataset, which is derived from the file name, or
	 *         the empty string if this dataset has no name.
	 */
	public String getName() {
		return name != null ? name : "";
	}

	/**
	 * 
	 * @return the current list of selections represented as bit sets encoded
	 *         into Strings using base-64 encoding, or null if there are no
	 *         selections.
	 */
	public List<String> getSelectionsAsBits() {
		if (selections == null)
			return null;
		List<String> bits = new ArrayList<String>();
		for (DataSelection s : selections)
			bits.add(Base64.encode(s.getAsBitSet()));
		return bits;
	}

	/**
	 * Sets the current list of selections to be a new list popupated with
	 * selections derived by decoding the specified base-64 encoded String into
	 * a bit set (in which the indices of the bit set's set bits (the '1' bits)
	 * correspond to the indices of the records to put in the selection). Then
	 * the drawing panels are updated.
	 * 
	 * @param selectionsAsBits
	 *            the list of Strings (base-64 encoded bit sets) to derive the
	 *            new list of selections from.
	 */
	public void setSelectionsAsBits(List<String> selectionsAsBits) {
		if (selectionsAsBits == null)
			selections = null;
		else {
			if (selections == null)
				selections = new ArrayList<DataSelection>();
			else
				selections.clear();
			for (String bits : selectionsAsBits) {
				DataSelection dataSelection = new DataSelection();
				dataSelection.setAsBitSet(Base64.decode(bits), this);
				selections.add(dataSelection);
			}
		}
		updateDrawingPanels();
	}

}
/*
 * CVS Log
 * 
 * $Log: DataTable.java,v $
 * Revision 1.2  2008/09/29 20:59:52  curran
 * Removed unnecessary @SuppressWarnings tags
 *
 * Revision 1.1  2007/08/15 17:59:11  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/26 19:35:49 ckellehe Got
 * Parallel Coordinates working
 * 
 * Revision 1.1 2007/07/26 00:30:58 ckellehe Initial Creation
 * 
 * 
 */
