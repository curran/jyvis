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
import java.util.BitSet;

/**
 * The class which stores a selection of DataRecords. This class is a list of
 * DataRecord objects, and also provides methods supporting encoding and
 * decoding of selections as BitSet objects.
 * 
 * @author Curran Kelleher
 * @see BitSet
 */
@SuppressWarnings("serial")
public class DataSelection extends ArrayList<DataRecord> {
	/**
	 * Construct an empty selection.
	 * 
	 */
	public DataSelection() {
	}

	/**
	 * Sets this list of DataRecords such that it includes the DataRecords in
	 * the specified data whose indices correspond to the set bits (1 bits) of
	 * the specified BitSet.
	 * 
	 * @param bitSet
	 * @param data
	 */
	public void setAsBitSet(BitSet bitSet, DataTable data) {
		clear();
		for (int i = 0; i < bitSet.length(); i++)
			if (bitSet.get(i))
				this.add(data.records.get(i));
	}

	/**
	 * Returns this selection as a bit set, where the indices of the set bits
	 * correspond to the indices of the selected records.
	 */
	public BitSet getAsBitSet() {
		BitSet bitSet = new BitSet();
		for (DataRecord r : this)
			bitSet.set(r.index);
		return bitSet;
	}
}
/*
 * CVS Log
 * 
 * $Log: DataSelection.java,v $
 * Revision 1.1  2007/08/15 17:59:11  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:30:58 ckellehe
 * Initial Creation
 * 
 * 
 */
