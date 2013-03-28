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
import java.util.List;
import java.util.Map;

import jyVis.graphics.DrawingPanel;
import jyVis.visualizationPrimitives.VisualizationPrimitive;

/**
 * A data record, or row of entries, in a DataTable.
 * 
 * @author Curran Kelleher
 * @see DataTable
 * @see DataEntry
 */
@SuppressWarnings("serial")
public class DataRecord extends ArrayList<DataEntry> {
	/**
	 * The JVObjects associated with this record, mapped by the JVDrawingPanels
	 * in which they occur. This exists for efficient linking of selections.
	 * When records are selected, the selected records (from
	 * DataTable.selectedRecords) are iterated through to be drawn. The
	 * JVDrawingPanel which is doing the drawing queries this map to see what it
	 * should draw to represent this record. Initially, this map is null.
	 */
	public Map<DrawingPanel, List<VisualizationPrimitive>> objects;

	/**
	 * The index of this record in it's parent data table
	 */
	public int index;

	/**
	 * Constructs a DataRecord with the specified initial capacity. This
	 * pre-allocates the ArrayList so it will never have to take CPU time to
	 * reallocate a larger array when elements are being added to it.
	 * 
	 * @param initialCapacity
	 */
	public DataRecord(int initialCapacity, int index) {
		super(initialCapacity);
		this.index = index;
	}
}
/*
 * CVS Log
 * 
 * $Log: DataRecord.java,v $
 * Revision 1.1  2007/08/15 17:59:10  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:30:58 ckellehe Initial
 * Creation
 * 
 * Revision 1.2 2007/06/08 15:01:45 ckellehe Initial Creation
 * 
 */
