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

import java.util.ArrayList;
import java.util.List;

/**
 * The class which encapsulates the state of a ListEditor. This state is
 * comprised of a list of indices of included elements, and a list of indices of
 * excluded elements, the indices referring to the original Object array which
 * the ListEditor was constructed with.
 * 
 * @author Curran Kelleher
 * 
 */
public class ListState {
	public final List<Integer> includedIndices;
	public final List<Integer> excludedIndices;

	/**
	 * Construct a list state with the specified lists of included and excluded
	 * indices.
	 * 
	 * @param includedIndices
	 * @param excludedIndices
	 */
	public ListState(List<Integer> includedIndices,
			List<Integer> excludedIndices) {
		this.includedIndices = includedIndices;
		this.excludedIndices = excludedIndices;
	}

	/**
	 * Construct a new list state where the indices of all the elements in the
	 * specified list are included, and none are included.
	 * 
	 * @param objects
	 */
	public ListState(Object[] objects) {
		this.includedIndices = new ArrayList<Integer>();
		for (int i = 0; i < objects.length; i++)
			includedIndices.add(i);
		this.excludedIndices = new ArrayList<Integer>();
	}

	/**
	 * Construct a new list state where the indices of all the elements in the
	 * specified list are included, and none are included.
	 * 
	 * @param objects
	 */
	public ListState(List<?> objects) {
		this(objects.toArray());
	}

	public String toString() {
		return "ListState(" + includedIndices + "," + excludedIndices + ")";
	}

}
