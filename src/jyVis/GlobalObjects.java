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
package jyVis;

import java.util.ArrayList;
import java.util.List;

/**
 * A globally accessable list of objects. Objects are never removed from this
 * list during a session, so the index of an object never changes and can be
 * relied upon for the duration of the session in which it was added.
 * 
 * @author Curran Kelleher
 * 
 */
public class GlobalObjects {
	/**
	 * The package of this class
	 */
	public static final String globalObjectsPackage = "jyVis";

	/**
	 * The list of global objects which is accessed exclusively by the methods
	 * add(Object o) and get(int i).
	 */
	static List<Object> objects = new ArrayList<Object>();

	/**
	 * Adds the specified Object to the list of global objects, and returns the
	 * index of that object in the list. For example,
	 * <code>GlobalObjects.get(GlobalObjects.add(object))</code> will return
	 * <code>object</code>.
	 * 
	 * @param o
	 *            the Object to add to the global list
	 * @return the index of the object in the global list
	 */
	public static int add(Object o) {
		objects.add(o);
		return objects.size() - 1;
	}

	/**
	 * Gets the Object at the specified index in the global list.
	 * 
	 * @param i
	 *            the index of the Object
	 * @return the Object at the specified index
	 */
	public static Object get(int i) {
		return objects.get(i);
	}

	/**
	 * Clears the list of global objects. This will break anything that is
	 * referring to any global objects, so don't call it unless you want to
	 * break the system (such as in the case of replaying a session).
	 * 
	 */
	public static void clear() {
		objects.clear();
	}

}
/*
 * CVS Log
 * 
 * $Log: GlobalObjects.java,v $
 * Revision 1.1  2007/08/15 17:59:16  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:31:01 ckellehe
 * Initial Creation
 * 
 * Revision 1.3 2007/06/27 20:56:54 ckellehe Got session replay working for
 * radViz mouse events Revision 1.2 2007/06/08 15:01:44 ckellehe Initial
 * Creation
 * 
 */
