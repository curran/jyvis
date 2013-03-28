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
package quadTree;

import java.awt.Point;

/**
 * The generic interface for quad tree entries, which are point-value pairs.
 * 
 * @author Curran Kelleher
 * 
 * @param <T>
 *            the type of the value
 */
public class PointValuePair<T> {
	/**
	 * The point part of this point-value pair
	 */
	public Point point;

	/**
	 * The value part of this point-value pair
	 */
	public T value;

	/**
	 * Constructs a PointValuePair with the specified point and value
	 * 
	 * @param point
	 * @param value
	 * 
	 */
	public PointValuePair(Point point, T value) {
		this.value = value;
		this.point = point;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
/*
 * CVS Log
 * 
 * $Log: PointValuePair.java,v $
 * Revision 1.1  2007/08/15 17:59:16  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:30:56 ckellehe
 * Initial Creation
 * 
 * Revision 1.2 2007/06/08 15:22:08 ckellehe Initial Creation
 * 
 * 
 */
