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
package colorMap;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * A panel which displays a ColorMap and provides user interaction for editing
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
public class ColorMapEditorPanel extends JPanel implements MouseListener,
		MouseMotionListener {
	/**
	 * The directory for loading color map presets
	 */
	public static String colorMapPresetsDirectory;
	/**
	 * The file extension for color map files
	 */
	public static String colorMapFileExtension = "colormap";

	/**
	 * The list of available presets used by all color map editor panels
	 */
	public static Map<String, ColorMapModel> availablePresets;
	/**
	 * The ColorMap which this panel displays.
	 */
	private ColorMap associatedColorMap;

	/**
	 * The editors for the individual color nodes.
	 */
	List<ColorNodeEditor> nodeEditors = new ArrayList<ColorNodeEditor>();

	/**
	 * The editor which is currently "grabbed" by the user.
	 */
	ColorNodeEditor grabbedNodeEditor = null;

	/**
	 * The popup menu which appears when right clicked
	 */
	protected ColorNodeEditorPopupMenu popupMenu = new ColorNodeEditorPopupMenu();

	/**
	 * A flag which is true when and only when the mouse has been dragging. If
	 * it is true when the mouse is released, then it is set to false and
	 * somethingHasChanged() is called
	 */
	boolean mouseHasBeenDragging = false;

	/**
	 * When this flag is true, the two options "save preset" and "load preset"
	 * are made available in the popup menu. It is false by default. Subclasses
	 * which implement the methods savePreset() and loadPreset() should set this
	 * flag to be true.
	 */
	public boolean supportsPresets = false;

	/**
	 * Whether or not the user is presented with the option of switching between
	 * continuous and discreet modes
	 */
	public boolean modeSwitchingEnabled = true;

	/**
	 * The threshold of brightness before the outline of a color node editor
	 * becomes black
	 */
	double blackNodeThreshold = .4;

	/**
	 * Construct a ColorMapEditorPanel which will display and edit the specified
	 * ColorMap
	 * 
	 * @param associatedColorMap
	 */
	public ColorMapEditorPanel(ColorMap associatedColorMap) {
		setColorMap(associatedColorMap);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(70, 20));
	}

	/**
	 * Sets the color map associated with this color map editor
	 * 
	 * @param associatedColorMap
	 */
	public void setColorMap(ColorMap associatedColorMap) {
		this.associatedColorMap = associatedColorMap;
		rebuildEditorPanels();
	}

	/**
	 * Clears all existing editor panels, and creates new ones which mirror the
	 * nodes of the associatedColorMap.
	 * 
	 */
	private void rebuildEditorPanels() {
		synchronized (nodeEditors) {
			nodeEditors.clear();
			for (ColorNode node : associatedColorMap.colorNodes)
				nodeEditors.add(new ColorNodeEditor(node));
		}
		repaint();
	}

	/**
	 * Paints the color map
	 */
	public void paint(Graphics g) {
		associatedColorMap.paintOnThis(g, 0, 0, getWidth(), getHeight());
		synchronized (nodeEditors) {
			// paint the editors
			for (Iterator<ColorNodeEditor> it = nodeEditors.iterator(); it
					.hasNext();)
				it.next().paintOnThis(g, getWidth(), getHeight());
		}
	}

	/**
	 * The class which defines the graphical nub stub used for editing a node
	 */
	private class ColorNodeEditor extends Polygon {
		ColorNode node;

		int xLowerBound = 0, xUpperBound = 0;

		int halfEditorWidth = 4;

		int editorHeight = 3;

		int pointHeight = 4;

		int offsetFromTop = 0;

		public ColorNodeEditor(ColorNode node) {
			super(new int[5], new int[5], 5);
			this.node = node;
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
			// if reset is not called, then contains() will not work
			this.reset();

			int thisNodesXCenter = (int) (node.value * width);
			xLowerBound = thisNodesXCenter - halfEditorWidth;
			xUpperBound = thisNodesXCenter + halfEditorWidth;
			xpoints[0] = xLowerBound;
			xpoints[1] = xUpperBound;
			xpoints[2] = xUpperBound;
			xpoints[3] = thisNodesXCenter;
			xpoints[4] = xLowerBound;

			ypoints[0] = offsetFromTop;
			ypoints[1] = offsetFromTop;
			ypoints[2] = offsetFromTop + editorHeight;
			ypoints[3] = offsetFromTop + editorHeight + pointHeight;
			ypoints[4] = offsetFromTop + editorHeight;

			npoints = 5;

			g.setColor(node.color);
			g.fillPolygon(this);

			g
					.setColor((double) (node.color.getRed()
							+ node.color.getGreen() + node.color.getBlue())
							/ (255.0 * 3) > blackNodeThreshold ? Color.black
							: Color.white);
			g.drawPolygon(this);
		}
	}

	/**
	 * Displays a color chooser for a node.
	 * 
	 * @param current
	 */
	private void displayColorChooserForNode(ColorNodeEditor current) {
		if (current == null)
			return;
		Color newColor = JColorChooser.showDialog(this, "Choose Color",
				current.node.color);
		if (newColor != null) {
			current.node.color = newColor;
			associatedColorMap.calculateColors();
			repaint();
			// notify possible subclasses that something has changed
			somethingHasChanged();
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
	protected void loadPresetFromFile() {

	}

	/**
	 * Figures out if the specified point is over a color node editor.
	 * 
	 * @param p
	 *            the point to check
	 * @return the ColorNodeEditor that the point p is inside, or null if p is
	 *         not inside a ColorNodeEditor
	 */
	private ColorNodeEditor getEditorUnderPoint(Point p) {
		for (Iterator<ColorNodeEditor> it = nodeEditors.iterator(); it
				.hasNext();) {
			ColorNodeEditor current = it.next();
			if (current.contains(p))
				return current;
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
			somethingHasChanged();
			mouseHasBeenDragging = false;
		}
		if (associatedColorMap.isDiscrete()) {
			associatedColorMap.alignDiscreteNodes();
			rebuildEditorPanels();
		}
	}

	public void mouseDragged(MouseEvent e) {
		// if the user is dragging a node editor
		if (grabbedNodeEditor != null) {
			double v = (double) e.getX() / getWidth();
			// move the node editor
			grabbedNodeEditor.node.value = v < 0 ? 0 : v > 1 ? 1 : v;
			// recalculate the color map
			associatedColorMap.calculateColors();

			if (associatedColorMap.isDiscrete()) {
				double grabbedNodeValue = grabbedNodeEditor.node.value;
				associatedColorMap.alignDiscreteNodes();
				grabbedNodeEditor.node.value = grabbedNodeValue;
			}
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
	protected class ColorNodeEditorPopupMenu extends JPopupMenu {
		ColorNodeEditor associatedEditor = null;

		JMenuItem createNodeMenuItem, deleteNodeMenuItem, changeColorMenuItem,
				switchModeMenuItem;
		JMenu presetsMenu;
		int currentX = 0, currentWidth = 1;

		public ColorNodeEditorPopupMenu() {

			deleteNodeMenuItem = new JMenuItem("Delete Node");
			deleteNodeMenuItem.addActionListener(new ActionListener() {
				/**
				 * This code is called when a color node is deleted
				 */
				public void actionPerformed(ActionEvent e) {
					if (associatedEditor != null) {
						// allow deletion only if there is more than one color
						// node
						if (associatedColorMap.colorNodes.size() > 1) {
							associatedColorMap.colorNodes
									.remove(associatedEditor.node);
							if (associatedColorMap.isDiscrete()) {
								Color[] newColors = new Color[associatedColorMap.colorNodes
										.size()];
								for (int i = 0; i < newColors.length; i++)
									newColors[i] = associatedColorMap.colorNodes
											.get(i).color;
								associatedColorMap.setDiscreteColors(newColors);
							} else
								// if its continuous...
								associatedColorMap.calculateColors();
							rebuildEditorPanels();
							// notify possible subclasses that something has
							// changed
							somethingHasChanged();
						} else
							JOptionPane.showMessageDialog(null,
									"Cannot delete last color node.", "Error",
									JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			changeColorMenuItem = new JMenuItem("Change Color");
			changeColorMenuItem.addActionListener(new ActionListener() {
				/**
				 * This is called when the user clicks on the popup menu item
				 * that says "Change Color"
				 */
				public void actionPerformed(ActionEvent e) {
					if (associatedEditor != null)
						displayColorChooserForNode(associatedEditor);
				}
			});

			createNodeMenuItem = new JMenuItem("Create Node");
			createNodeMenuItem.addActionListener(new ActionListener() {
				/**
				 * This is called when the user clicks on the popup menu item
				 * that says "Create Node"
				 */
				public void actionPerformed(ActionEvent e) {
					double value = (double) currentX / currentWidth;
					Color newColor = JColorChooser.showDialog(null,
							"Choose Color", associatedColorMap.getColor(value));
					if (newColor != null) {
						associatedColorMap.colorNodes.add(new ColorNode(
								newColor, value));
						Collections.sort(associatedColorMap.colorNodes);
						if (associatedColorMap.isDiscrete()) {
							Color[] newColors = new Color[associatedColorMap.colorNodes
									.size()];
							for (int i = 0; i < newColors.length; i++)
								newColors[i] = associatedColorMap.colorNodes
										.get(i).color;
							associatedColorMap.setDiscreteColors(newColors);
						} else {
							associatedColorMap.calculateColors();
						}
						rebuildEditorPanels();
						// notify possible subclasses that something has changed
						somethingHasChanged();
					}
				}
			});

			createPresetMenu();

			switchModeMenuItem = new JMenuItem("Switch Modes");
			switchModeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (associatedColorMap.isDiscrete())
						associatedColorMap = ColorMap
								.getDefaultContinuousColorMap();
					else
						associatedColorMap = ColorMap
								.getDefaultDiscreteColorMap();
					rebuildEditorPanels();
					somethingHasChanged();
				}
			});

		}

		public void createPresetMenu() {
			presetsMenu = new JMenu("Presets");

			if (ColorMapEditorPanel.availablePresets != null) {
				for (final Map.Entry<String, ColorMapModel> e : ColorMapEditorPanel.availablePresets
						.entrySet()) {
					JMenuItem menuItem = new JMenuItem(e.getKey());
					menuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent a) {
							associatedColorMap = e.getValue().getAsColorMap();
							rebuildEditorPanels();
							somethingHasChanged();
						}
					});
					presetsMenu.add(menuItem);
				}
				presetsMenu.addSeparator();
			}
			JMenuItem savePresetMenuItem = new JMenuItem("Save Preset");
			savePresetMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					savePreset();
					reloadPresets();
				}
			});
			presetsMenu.add(savePresetMenuItem);

			if (ColorMapEditorPanel.availablePresets != null) {
				JMenuItem removePresetMenu = new JMenu("Remove Preset");
				for (final Map.Entry<String, ColorMapModel> e : ColorMapEditorPanel.availablePresets
						.entrySet()) {
					JMenuItem menuItem = new JMenuItem(e.getKey());
					menuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent a) {
							String presetName = e.getKey();
							if (JOptionPane.showConfirmDialog(null,
									"Delete the preset called " + presetName
											+ "?", "Delete?",
									JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION)
								new File(colorMapPresetsDirectory + "\\"
										+ presetName + "."
										+ colorMapFileExtension).delete();

							reloadPresets();
						}
					});
					removePresetMenu.add(menuItem);
				}
				presetsMenu.add(removePresetMenu);
			}

			JMenuItem loadPresetMenuItemFromFile = new JMenuItem(
					"Load From File");
			loadPresetMenuItemFromFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					loadPresetFromFile();
				}
			});
			presetsMenu.add(loadPresetMenuItemFromFile);
		}

		/**
		 * Shows the popup menu
		 * 
		 * @param editorUnderPoint
		 *            the ColorNodeEditor that the menu should interact with, or
		 *            null if the menu should not interact with any node editor
		 */
		public void showForNodeEditor(ColorNodeEditor editorUnderPoint,
				int width, Component invoker, int x, int y) {
			currentX = x;
			currentWidth = width;
			associatedEditor = editorUnderPoint;
			removeAll();
			if (associatedEditor != null) {
				add(changeColorMenuItem);
				add(deleteNodeMenuItem);
			} else {
				add(createNodeMenuItem);
				if (modeSwitchingEnabled) {
					switchModeMenuItem.setText("Switch to "
							+ (associatedColorMap.isDiscrete() ? "Continuous"
									: "Discrete") + " Mode");
					add(switchModeMenuItem);
				}
				if (supportsPresets)
					add(presetsMenu);
			}
			super.show(invoker, x, y);
		}
	}

	/**
	 * 
	 * @return The ColorMap which this panel displays
	 */
	public ColorMap getAssociatedColorMap() {
		return associatedColorMap;
	}

	/**
	 * Resets the presets in the popup menu
	 */
	public void reloadPresets() {
		popupMenu.createPresetMenu();
	}
}
/*
 * CVS Log
 * 
 * $Log: ColorMapEditorPanel.java,v $
 * Revision 1.2  2008/11/14 16:54:02  curran
 * Removed unnecessary @serial annotations
 *
 * Revision 1.1  2007/08/15 17:59:11  curran
 * Initial commit to SourceForge
 * Revision 1.7 2007/08/04 17:23:48 ckellehe
 * Removed color map dependency on JyVis
 * 
 * Revision 1.6 2007/08/02 15:16:33 ckellehe Added delete color map preset menu
 * Revision 1.5 2007/08/01 22:03:48 ckellehe Added color map editor GUI features
 * Revision 1.4 2007/08/01 19:36:01 ckellehe Changed the color map editor in the
 * system UI - disabled continuous toggling
 * 
 * Revision 1.3 2007/08/01 19:31:24 ckellehe Added a clearer menu to Color Map
 * editor for switching between continuous and disctere modes. Revision 1.2
 * 2007/07/26 14:49:29 ckellehe Cleaned up documentation
 * 
 * Revision 1.1 2007/07/26 00:30:58 ckellehe Initial Creation
 */
