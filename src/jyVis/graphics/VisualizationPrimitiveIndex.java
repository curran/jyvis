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
package jyVis.graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;

import jyVis.data.DataRecord;
import jyVis.data.DataSelection;
import jyVis.visualizationPrimitives.VisualizationPrimitive;
import quadTree.PointValuePair;
import quadTree.QuadTree;

/**
 * Indexes visualization primitives for efficient selection and mouse
 * interaction.
 * 
 * @author Curran Kelleher
 * 
 */
public class VisualizationPrimitiveIndex {
	QuadTree<DataRecord> quadtree = new QuadTree<DataRecord>();

	/**
	 * The list populated when queries are performed. This is done in order to
	 * avoid making new list objects every time the tree is queried.
	 */
	List<VisualizationPrimitive> objects;

	boolean indexingRequired = false;

	/**
	 * Construct an index which will index the objects in the specified list
	 * 
	 * @param objects
	 */
	public VisualizationPrimitiveIndex(List<VisualizationPrimitive> objects) {
		this.objects = objects;
	}

	/**
	 * Gets the visualization primitive below the specified point on the drawing
	 * panel
	 * 
	 * @param point
	 *            the pixel point on the drawing panel to query with
	 */
	public VisualizationPrimitive performPointQueryForListeningObject(
			Point point) {
		for (VisualizationPrimitive object : objects)
			if (object.getBoundingBox().contains(point))
				if (object.hasMouseListeners())
					if (object.contains(point))
						return object;

		return null;
	}

	/**
	 * Gets the record associated with the visualization primitive below the
	 * specified point on the drawing panel
	 * 
	 * @param point
	 *            the pixel point on the drawing panel to query with
	 */
	public DataRecord performPointQueryForRecord(Point point) {
		for (VisualizationPrimitive object : objects)
			if (object.getBoundingBox().contains(point))
				if (object.contains(point))
					if (object.getAssociatedRecord() != null)
						return object.getAssociatedRecord();

		return null;
	}

	/**
	 * Clears all entries in this index, and sets it up for indexing to a pixel
	 * space of the specified size when queried
	 * 
	 * @param width
	 * @param height
	 */
	public void reset(int width, int height) {
		quadtree.reset(width, height);
		indexingRequired = true;
	}

	public void paintQuadTree(Graphics g) {
		quadtree.paint(g);
	}

	public void performSpatialQuery(Polygon polygon,
			DataSelection selectedRecords) {
		ensureIndex();
		if (QuadTree.paintQuadTree)
			quadtree.resetColors();
		quadtree.performSpatialQuery(polygon, selectedRecords);
	}

	/**
	 * Ensures that the objects have been indexed
	 * 
	 */
	private void ensureIndex() {
		if (indexingRequired) {
			synchronized (this) {
				for (VisualizationPrimitive o : objects) {
					List<Point> selectablePoints = o.getSelectablePoints();
					DataRecord associatedRecord = o.getAssociatedRecord();
					if (selectablePoints != null && associatedRecord != null)
						for (Point p : selectablePoints)
							quadtree.put(new PointValuePair<DataRecord>(p,
									associatedRecord));
				}
				indexingRequired = false;
			}
		}
	}
}
/*
 * CVS Log
 * 
 * $Log: VisualizationPrimitiveIndex.java,v $
 * Revision 1.2  2008/09/29 20:59:52  curran
 * Removed unnecessary @SuppressWarnings tags
 *
 * Revision 1.1  2007/08/15 17:59:21  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:30:59
 * ckellehe Initial Creation Revision 1.6 2007/07/06 19:02:49 cmathai Made
 * progress on heatmap
 * 
 * Revision 1.5 2007/06/20 21:26:59 ckellehe Partially implemented a Java
 * scripting engine, so that Java code can now specify callback functions for
 * widgets, so can now use the wiget framework directly. Used the Java engine to
 * implement a text field + combo box duo, which was previously implemented in
 * Python. Now this text field + combo box interface is available as a standard
 * widget, and can be used in all supported languages Created an example Groovy
 * script that uses the text field + combo box interface.
 * 
 * Revision 1.4 2007/06/19 21:53:55 ckellehe Fixed some bugs with the
 * JVMouselistener framework Revision 1.3 2007/06/19 17:00:13 rbeaven
 * Implemented the JVMouseListener framework, added a test Groovy script for it
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
