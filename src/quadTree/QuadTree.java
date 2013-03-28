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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * A general quad tree, a 2D index of point-value pairs which can be queried
 * with a polygon, returning results in O(log(n)) time.
 * 
 * @author Curran Kelleher
 * 
 * @param <T>
 *            the type of the values that which will be indexed by points.
 */
@SuppressWarnings("serial")
public class QuadTree<T> extends Rectangle {
	/**
	 * The maximum capacity for quadtree nodes, or "buckets". When the number of
	 * entries inside a node exceeds this number, then that node splits into
	 * four subnodes.
	 */
	private int threshold;

	/**
	 * True if the quad tree will be drawn
	 */
	public static boolean paintQuadTree;

	/**
	 * The color that the unused parts of the quadtree will be drawn
	 */
	private static final Color nodeColor_untouched = Color.DARK_GRAY;

	/**
	 * The color which the parts of the tree which have been used in performing
	 * a spacial query will be painted.
	 */
	private static final Color nodeColor_traversed = Color.red;

	/**
	 * The current color of this node.
	 */
	private Color nodeColor = nodeColor_untouched;

	/**
	 * The entries (point-value pairs) inside this node
	 */
	private List<PointValuePair<T>> entries = new ArrayList<PointValuePair<T>>();

	/**
	 * A flag indicating whether this node is a leaf (true) or has subnodes
	 * (false). If true, then "entries" exists and "subtrees" does not exist. If
	 * false, then "entries" does not exist and "subtrees" exists
	 */
	private boolean leaf = true;

	/**
	 * The four subnodes of this node. This is populated when number of entries
	 * exceeds the capacity of this node. (see splitThisNode()) A List is used
	 * as opposed to an array because arrays cannot be created using generics.
	 */
	private List<QuadTree<T>> subtrees;

	/**
	 * If there are any points outside the top level of the quadtree, yet still
	 * need to be indexed, they will go in this node.
	 */
	private QuadTree<T> nodeToContainPointsOutsideTheTree;

	/**
	 * Construct an emtpy quadtree node of size (0,0)
	 */
	public QuadTree() {
		this(0, 0, 0, 0);
	}

	/**
	 * Construct a quadtree node covering the rectangular region specified by
	 * the two points: (x1,y1) - the upper left corner, and (x2,y2) - the lower
	 * right corner. A default of 5 is used as the threshold for node splitting.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public QuadTree(int x1, int y1, int x2, int y2) {
		this(x1, y1, x2, y2, 5);
	}

	/**
	 * Construct a quadtree node covering the rectangular region specified by
	 * the two points: (x1,y1) - the upper left corner, and (x2,y2) - the lower
	 * right corner.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param threshold -
	 *            The maximum capacity for quadtree nodes (or "buckets"). When
	 *            the number of entries inside a node exceeds this number, then
	 *            that node splits into four subnodes.
	 */
	private QuadTree(int x1, int y1, int x2, int y2, int threshold) {
		super(x1, y1, x2 - x1, y2 - y1);
		this.threshold = threshold;
	}

	/**
	 * Indexes the specified entry into this quadtree.
	 * 
	 * @param e
	 */
	public void put(PointValuePair<T> e) {
		if (getWidth() == 0 && getHeight() == 0)
			throw new RuntimeException(
					"Don't index into a quadtree of 0 size, it's silly");
		if (leaf) {
			entries.add(e);
			// handle the case where there are overlapping points,
			// this test is to avoid infinite recursion when the number of
			// overlapping points is greater than the threshold
			if (!allEntriesAreEqual())
				splitThisNode();
		} else
			localNodeThatContainsPoint(e.point).put(e);
	}

	/**
	 * 
	 * @return true if all entries are equal
	 */
	private boolean allEntriesAreEqual() {
		Point firstPoint = entries.get(0).point;
		for (PointValuePair<T> e : entries)
			if (!e.point.equals(firstPoint))
				return false;
		return true;
	}

	/**
	 * Returns the one subnode which contains the specified point, of the four
	 * subnodes of this node
	 * 
	 * @param p
	 *            the point to test
	 * @return the subnode of this node which contains that point
	 */
	private QuadTree<T> localNodeThatContainsPoint(Point p) {
		for (QuadTree<T> n : subtrees)
			if (n.contains(p))
				return n;
		// if we are here, then the point is outside the tree
		if (nodeToContainPointsOutsideTheTree == null)
			nodeToContainPointsOutsideTheTree = new QuadTree<T>(
					Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE,
					Integer.MAX_VALUE, Integer.MAX_VALUE);
		return nodeToContainPointsOutsideTheTree;
	}

	/**
	 * Splits this node; transforms it from a leaf into a tree.
	 * 
	 */
	private void splitThisNode() {
		// 1.) set the leaf flag to false
		// 2.) create subtrees
		// 3.) transfer entries into subtrees

		// 1.) set the leaf flag to false
		leaf = false;

		// 2.) create subtrees
		subtrees = new ArrayList<QuadTree<T>>(4);

		// (x1,y1)------------|
		// |------------------|
		// |------------------|
		// |------------------|
		// |------------------|
		// |------------------|
		// |------------------|
		// |-----------(x2,y2)|
		int x1 = x, y1 = y, x2 = x + width, y2 = y + height;
		int halfwayX = (x1 + x2) / 2;
		int halfwayY = (y1 + y2) / 2;

		// (x1,y1)-------------(halfwayX,y1)-------------(x2,y1)
		// |-------------------------|-------------------------|
		// |----------[0]------------|-----------[1]-----------|
		// |-------------------------|-------------------------|
		// (x1,halfwayY)----(halfwayX,halfwayY)----(x2,halfwayY)
		// |-------------------------|-------------------------|
		// |----------[2]------------|-----------[3]-----------|
		// |-------------------------|-------------------------|
		// (x1,y2)-------------(halfwayX,y2)-------------(x2,y2)
		subtrees.add(new QuadTree<T>(x1, y1, halfwayX, halfwayY, threshold));
		subtrees.add(new QuadTree<T>(halfwayX, y1, x2, halfwayY, threshold));
		subtrees.add(new QuadTree<T>(x1, halfwayY, halfwayX, y2, threshold));
		subtrees.add(new QuadTree<T>(halfwayX, halfwayY, x2, y2, threshold));

		// 3.) transfer entries into subtrees
		for (PointValuePair<T> e : entries)
			localNodeThatContainsPoint(e.point).put(e);
	}

	/**
	 * paints this tree
	 * 
	 * @param g
	 *            the graphics to paint on
	 */
	public void paint(Graphics g) {
		paint(g, 0, 0, 0, 0);
	}

	/**
	 * The convoluted recursive function which paints this tree.
	 */
	public void paint(Graphics g, int x1Indent, int y1Indent, int x2Indent,
			int y2Indent) {
		if (!leaf) {
			int indent = 1;
			subtrees.get(0)
					.paint(g, x1Indent + indent, y1Indent + indent, 0, 0);
			subtrees.get(1)
					.paint(g, 0, y1Indent + indent, x2Indent + indent, 0);
			subtrees.get(2)
					.paint(g, x1Indent + indent, 0, 0, y2Indent + indent);

			subtrees.get(3)
					.paint(g, 0, 0, x2Indent + indent, y2Indent + indent);

		}
		g.setColor(nodeColor);
		g.drawRect(x + x1Indent, y + y1Indent, width - x2Indent - x1Indent - 1,
				height - y2Indent - y1Indent - 1);
	}

	/**
	 * Resets the colors of all subtrees.
	 * 
	 */
	public void resetColors() {
		nodeColor = nodeColor_untouched;
		if (!leaf)
			for (QuadTree<T> n : subtrees)
				n.resetColors();
	}

	/**
	 * A recursive function which performs the spacial query
	 * 
	 * @param polygon
	 *            the polygon to query with
	 * @param entriesInside
	 *            the cumulative list of all entries inside the boundingBox
	 */
	public void performSpatialQuery(Shape polygon, List<T> entriesInside) {

		nodeColor = nodeColor_traversed;
		if (leaf) {
			for (PointValuePair<T> e : entries)
				if (polygon.contains(e.point))
					entriesInside.add(e.value);
		} else
			for (QuadTree<T> n : subtrees) {
				if (polygon.intersects(n)) {
					// if n is entirely inside rectangle, then there is no point
					// in testing the entries in n, because they are definitely
					// inside rectangle, so add them to the list without testing
					// them.
					if (polygon.contains(n))
						n.addAllEntries(entriesInside);
					else
						n.performSpatialQuery(polygon, entriesInside);
				}
				// if there are some points outside the tree, test them
				if (nodeToContainPointsOutsideTheTree != null)
					for (PointValuePair<T> e : nodeToContainPointsOutsideTheTree.entries)
						if (polygon.contains(e.point))
							entriesInside.add(e.value);
			}

	}

	/**
	 * Adds all entries contained in this node (in all subtrees)
	 * 
	 * @param entriesInside
	 *            the list which the entries will be added to
	 */
	private void addAllEntries(List<T> entriesInside) {
		if (leaf)
			for (PointValuePair<T> e : entries)
				entriesInside.add(e.value);
		else
			for (QuadTree<T> n : subtrees)
				n.addAllEntries(entriesInside);
	}

	/**
	 * Clears all entries in this quadtree and sets the size of the root node to
	 * be the specified size
	 * 
	 * @param width
	 * @param height
	 */
	public void reset(int width, int height) {
		leaf = true;
		entries.clear();
		subtrees = null;
		this.width = width;
		this.height = height;
	}
}
/*
 * CVS Log
 * 
 * $Log: QuadTree.java,v $
 * Revision 1.1  2007/08/15 17:59:16  curran
 * Initial commit to SourceForge
 * Revision 1.3 2007/07/30 23:43:04 ckellehe Cleaned up
 * Javadoc
 * 
 * Revision 1.2 2007/07/26 14:51:48 ckellehe Corrected type checking issues
 * 
 * Revision 1.1 2007/07/26 00:30:56 ckellehe Initial Creation Revision 1.2
 * 2007/06/08 15:22:08 ckellehe Initial Creation
 * 
 * 
 */
