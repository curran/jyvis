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
package shapeMap;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * A panel which displays a ShapeMap and provides user interaction for editing
 * it. Color nodes (the color-value pairs that define the color map) are
 * represented graphically as small upside down house shapes filled with their
 * color. The color nodes are movable. When a color node is clicked, the user is
 * prompted to select a new color for the node. Dropdown menus allow the user to
 * create new nodes, delete existing ones, or change the color of existing ones.
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class ShapeMapEditorPanel extends JPanel implements MouseListener,
		MouseMotionListener {

	/**
	 * The ShapeMap which this panel displays.
	 */
	private ShapeMap associatedShapeMap;

	/**
	 * The editors for the individual color nodes.
	 */
	List<ShapeNodeEditor> nodeEditors = new ArrayList<ShapeNodeEditor>();

	/**
	 * The editor which is currently "grabbed" by the user.
	 */
	ShapeNodeEditor grabbedNodeEditor = null;

	/**
	 * The popup menu which appears when right clicked
	 */
	final ShapeNodeEditorPopupMenu popupMenu = new ShapeNodeEditorPopupMenu();

	/**
	 * A flag which is true when and only when the mouse has been dragging. If
	 * it is true when the mouse is released, then it is set to false and
	 * somethingHasChanged() is called
	 */
	boolean mouseHasBeenDragging = false;

	/**
	 * When this flag is true, the two options "save preset" and "load preset"
	 * are made available in the popup menu. It is false by default. Subclasses
	 * which implement the methods savePreset(), loadPreset(), loadShape(), and
	 * saveShape() should set this flag to be true.
	 */
	public boolean supportsPresets = false;

	/**
	 * Construct a ShapeMapEditorPanel which will display and edit the specified
	 * ShapeMap
	 * 
	 * @param associatedShapeMap
	 */
	public ShapeMapEditorPanel(ShapeMap associatedShapeMap) {
		setShapeMap(associatedShapeMap);
		addMouseListener(this);
		addMouseMotionListener(this);
		// setMinimumSize(new Dimension(300, 300));
		setPreferredSize(new Dimension(70, 20));

	}

	/**
	 * Sets the color map associated with this color map editor
	 * 
	 * @param associatedShapeMap
	 */
	public void setShapeMap(ShapeMap associatedShapeMap) {
		this.associatedShapeMap = associatedShapeMap;
		rebuildEditorPanels();
	}

	/**
	 * Rebuilds the shape node editors (on the screen) based on the nodes of the
	 * color map.
	 * 
	 */
	private void rebuildEditorPanels() {
		synchronized (nodeEditors) {
			nodeEditors.clear();
			List<Shape> shapes = associatedShapeMap.getShapes();
			int n = shapes.size();
			for (int i = 0; i < n; i++)
				nodeEditors.add(new ShapeNodeEditor(shapes.get(i),
						(double) (i + 1) / (n + 1)));
		}
		repaint();
	}

	/**
	 * Rebuilds the nodes of the color map based on the shape node editors (on
	 * the screen).
	 */
	private void rebuildShapes() {
		Collections.sort(nodeEditors);
		List<Shape> shapes = associatedShapeMap.getShapes();
		shapes.clear();
		for (ShapeNodeEditor e : nodeEditors)
			shapes.add(e.shape);
	}

	/**
	 * Paints the color map
	 */
	public void paint(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.clearRect(0, 0, getWidth(), getHeight());

		synchronized (nodeEditors) {
			// paint the editors
			for (ShapeNodeEditor e : nodeEditors)
				e.paintOnThis(g, getWidth(), getHeight());
		}
	}

	/**
	 * The class which defines the graphical nub stub used for editing a node
	 */
	private class ShapeNodeEditor extends Polygon implements
			Comparable<ShapeNodeEditor> {
		Shape shape;

		int xLowerBound = 0, xUpperBound = 0;

		int halfEditorWidth = 4;

		int editorHeight = 3;

		int pointHeight = 4;

		int offsetFromTop = 0;

		public double position;

		public ShapeNodeEditor(Shape shape, double position) {
			super(new int[shape.xPoints.length], new int[shape.yPoints.length],
					shape.yPoints.length);
			this.shape = shape;
			this.position = position;
			setPreferredSize(new Dimension(70, 20));
		}

		/**
		 * Paints this color map node editor
		 * 
		 * @param g
		 *            the graphics to paint on
		 * @param width
		 *            the width to paint the color map
		 * @param height
		 *            the width to paint the color map
		 */
		public void paintOnThis(Graphics g, int width, int height) {

			int xCenter = (int) (position * width);

			// g.drawLine(xCenter, 0, xCenter, height);

			for (int i = 0; i < shape.xPoints.length; i++) {
				this.ypoints[i] = (int) -((shape.yPoints[i] - 1) / 2 * (height - 1));
				this.xpoints[i] = (int) (shape.xPoints[i] * height / 2)
						+ xCenter;
			}
			this.invalidate();
			g.setColor(Color.black);
			// g.fillPolygon(this.xpoints, this.ypoints, this.ypoints.length);
			g.fillPolygon(this);
		}

		public int compareTo(ShapeNodeEditor e) {
			return (int) Math.signum(position - e.position);
		}
	}

	/**
	 * This method is called whenever something about the color map is changed.
	 * 
	 * This method does nothing, but is intended for subclasses to use.
	 * 
	 */
	protected void somethingHasChanged() {

	}

	/**
	 * This method is called when the user clicks on the "save preset" option of
	 * the popup menu.
	 * 
	 * This method does nothing, but is intended for subclasses to implement.
	 * 
	 */
	protected void savePreset() {

	}

	/**
	 * This method is called when the user clicks on the "load preset" option of
	 * the popup menu.
	 * 
	 * This method does nothing, but is intended for subclasses to implement.
	 * 
	 */
	protected void loadPreset() {

	}

	/**
	 * This method is called when the user clicks on the "Create Shape File"
	 * option of the popup menu.
	 * 
	 * This method does nothing, but is intended for subclasses to implement.
	 * 
	 */
	protected void saveShape(Shape shape) {

	}

	/**
	 * This method is called to get the shape to add when the user clicks on the
	 * "Create Shape" option of the popup menu.
	 * 
	 * This method returns null if not overridden. It is intended for subclasses
	 * to implement.
	 * 
	 * @return the Shape object to add to the shape map, or null if there is no
	 *         shape to add
	 */
	protected Shape loadShape() {
		return null;
	}

	/**
	 * Figures out if the specified point is over a color node editor.
	 * 
	 * @param p
	 *            the point to check
	 * @return the ShapeNodeEditor that the point p is inside, or null if p is
	 *         not inside a ShapeNodeEditor
	 */
	private ShapeNodeEditor getEditorUnderPoint(Point p) {
		for (ShapeNodeEditor e : nodeEditors) {
			if (e.contains(p))
				return e;
		}
		return null;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		// I believe on macs, the popup trigger is on mouse down. What is why
		// this is here
		if (e.isPopupTrigger())
			popupMenu.showForNodeEditor(getEditorUnderPoint(e.getPoint()),
					getWidth(), e.getComponent(), e.getX(), e.getY());
		else
			grabbedNodeEditor = getEditorUnderPoint(e.getPoint());
	}

	public void mouseReleased(MouseEvent e) {
		grabbedNodeEditor = null;
		if (e.isPopupTrigger())
			popupMenu.showForNodeEditor(getEditorUnderPoint(e.getPoint()),
					getWidth(), e.getComponent(), e.getX(), e.getY());
		else if (mouseHasBeenDragging) {
			rebuildShapes();
			rebuildEditorPanels();
			somethingHasChanged();
			mouseHasBeenDragging = false;
		}
	}

	public void mouseDragged(MouseEvent e) {
		// if the user is dragging a node editor
		if (grabbedNodeEditor != null) {
			double v = (double) e.getX() / getWidth();
			// move the node editor
			grabbedNodeEditor.position = v < 0 ? 0 : v > 1 ? 1 : v;

			// repaint
			repaint();
			mouseHasBeenDragging = true;
			// notify possible subclasses that something has changed
			// somethingHasChanged();
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	/**
	 * The menu which pops up when the user right-clicks on the color map editor
	 * 
	 */
	private class ShapeNodeEditorPopupMenu extends JPopupMenu {
		ShapeNodeEditor associatedEditor = null;

		JMenuItem loadShapeFileMenuItem, createShapeFileMenuItem,
				deleteNodeMenuItem, savePresetMenuItem, loadPresetMenuItem;

		int currentX = 0, currentWidth = 1;

		public ShapeNodeEditorPopupMenu() {

			deleteNodeMenuItem = new JMenuItem("Delete Node");
			deleteNodeMenuItem.addActionListener(new ActionListener() {
				/**
				 * This code is called when a shape node is deleted
				 */
				public void actionPerformed(ActionEvent e) {
					if (associatedEditor != null) {
						// allow deletion only if there is more than one shape
						if (associatedShapeMap.getShapes().size() > 1) {
							associatedShapeMap.getShapes().remove(
									associatedEditor.shape);

							rebuildEditorPanels();
							// notify possible subclasses that something has
							// changed
							somethingHasChanged();
						} else {
							JOptionPane.showMessageDialog(null,
									"Cannot delete last shape.", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});

			loadShapeFileMenuItem = new JMenuItem("Load Shape File");
			loadShapeFileMenuItem.addActionListener(new ActionListener() {
				/**
				 * This is called when the user clicks on the popup menu item
				 * that says "Create Shape"
				 */
				public void actionPerformed(ActionEvent e) {
					double position = (double) currentX / currentWidth;

					Shape newShape = loadShape();
					if (newShape != null) {
						// make a new ShapeNodeEditor
						nodeEditors
								.add(new ShapeNodeEditor(newShape, position));
						// rebuild the list of shapes in the shape map to mirror
						// the
						// list of ShapeNodeEditors
						rebuildShapes();
						// make the ShapeNodeEditors evenly spaced
						rebuildEditorPanels();
						// notify possible subclasses that something has changed
						somethingHasChanged();
					}
				}
			});

			createShapeFileMenuItem = new JMenuItem("Create Shape File");
			createShapeFileMenuItem.addActionListener(new ActionListener() {
				/**
				 * This is called when the user clicks on the popup menu item
				 * that says "Create Shape File"
				 */
				public void actionPerformed(ActionEvent e) {
					String[] xPointsStrings = JOptionPane.showInputDialog(null,
							"enter comma separated x points between -1 and 1")
							.split(",");
					String[] yPointsStrings = JOptionPane.showInputDialog(null,
							"enter comma separated y points between -1 and 1")
							.split(",");

					int n = xPointsStrings.length;
					if (n != yPointsStrings.length || n < 3)
						JOptionPane
								.showMessageDialog(
										null,
										"The number of x and y points must be equal and greater than 2!",
										"Invalid input",
										JOptionPane.ERROR_MESSAGE);
					else {
						// get the numbers from the strings
						float[] xPoints = new float[n];
						float[] yPoints = new float[n];

						try {

							for (int i = 0; i < n; i++) {
								xPoints[i] = Float
										.parseFloat(xPointsStrings[i]);
								yPoints[i] = Float
										.parseFloat(yPointsStrings[i]);
							}
							// removes duplicate points
							for (int i = 0; i < n; i++) {
								for (int j = i + 1; j < n; j++) {
									if (xPoints[i] == xPoints[j]
											&& yPoints[i] == yPoints[j]) {
										n--;
										for (int k = j; k < n; k++) {
											xPoints[k] = xPoints[k + 1];
											yPoints[k] = yPoints[k + 1];
										}
									}
								}
							}
							// tests to see if there are at least three
							// noncollinear points
							boolean validShape = false;
							if (n > 2) {

								float m0;
								float x0 = xPoints[0];
								float y0 = yPoints[0];

								if (xPoints[1] != x0) {
									m0 = (yPoints[1] - y0) / (xPoints[1] - x0);

									for (int i = 2; i < n; i++) {
										float m = (yPoints[i] - y0)
												/ (xPoints[i] - x0);

										if (m != m0) {
											validShape = true;
											break;
										}
									}
								} else {
									for (int i = 2; i < n; i++) {
										if (xPoints[i] != x0) {
											validShape = true;
											break;
										}
									}
								}
							}
							if (validShape) {
								// make a shape from the numbers
								Shape shape = new Shape(xPoints, yPoints);
								// write the shape file
								saveShape(shape);
							} else {
								JOptionPane
										.showMessageDialog(
												null,
												"A shape cannot be created from these points.",
												"Invalid Input",
												JOptionPane.ERROR_MESSAGE);
							}
						} catch (Exception ex) {
							JOptionPane
									.showMessageDialog(
											null,
											"Invalid points. The X and Y points must be comma separated lists of numbers.",
											"Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});

			savePresetMenuItem = new JMenuItem("Save Preset");
			savePresetMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					savePreset();
				}
			});
			loadPresetMenuItem = new JMenuItem("Load Preset");
			loadPresetMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					loadPreset();
				}
			});

		}

		/**
		 * Shows the popup menu
		 * 
		 * @param editorUnderPoint
		 *            the ShapeNodeEditor that the menu should interact with, or
		 *            null if the menu should not interact with any node editor
		 */
		public void showForNodeEditor(ShapeNodeEditor editorUnderPoint,
				int width, Component invoker, int x, int y) {
			currentX = x;
			currentWidth = width;

			associatedEditor = editorUnderPoint;
			removeAll();
			if (associatedEditor != null) {
				add(deleteNodeMenuItem);
			} else {
				if (supportsPresets) {
					add(savePresetMenuItem);
					add(loadPresetMenuItem);
					add(createShapeFileMenuItem);
					add(loadShapeFileMenuItem);

				}
			}

			super.show(invoker, x, y);
		}
	}

	/**
	 * 
	 * @return The ShapeMap which this panel displays
	 */
	public ShapeMap getAssociatedShapeMap() {
		return associatedShapeMap;
	}
}
/*
 * CVS Log
 * 
 * $Log: ShapeMapEditorPanel.java,v $
 * Revision 1.2  2008/09/29 20:59:51  curran
 * Removed unnecessary @SuppressWarnings tags
 *
 * Revision 1.1  2007/08/15 17:59:21  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/08/02 15:33:53 ckellehe
 * Added the HSV color map
 * 
 * Revision 1.1 2007/07/26 00:31:02 ckellehe Initial Creation
 * 
 * Revision 1.4 2007/06/12 19:27:13 rbeaven *** empty log message *** Revision
 * 1.3 2007/06/12 17:42:21 ckellehe Fixed a bug where the ShapeMapEditorPanel
 * would crash if the user didn't enter any points when creating a shape. Merged
 * JV.promptUserToOpenFile() and JV.promptUserToSaveFile() into one method -
 * JV.promptUserToChooseFile() because they are so similar and had lots of
 * identical code. Revision 1.2 2007/06/08 15:22:08 ckellehe Initial Creation
 * 
 * 
 */
