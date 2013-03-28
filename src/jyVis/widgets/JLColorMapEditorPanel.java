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
package jyVis.widgets;

import java.awt.event.MouseListener;
import java.io.File;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import jyVis.JyVisSettings;
import scripting.Binding;
import colorMap.ColorMap;
import colorMap.ColorMapEditorPanel;
import colorMap.ColorMapModel;
import fileUtils.FileUtils;
import fileUtils.XMLFileIO;

/**
 * A panel which displays a ColorMap and provides user interaction for editing
 * it. Color nodes (the color-value pairs that define the color map) are
 * represented graphically as small upside down house shapes filled with their
 * color. The color nodes are movable. When a color node is clicked, the user is
 * prompted to select a new color for the node. Dropdown menus allow the user to
 * create new nodes, delete existing ones, or change the color of existing ones.
 * 
 * @author Curran Kelleher
 */
@SuppressWarnings("serial")
public class JLColorMapEditorPanel extends ColorMapEditorPanel implements
		JLWidget<ColorMap>, MouseListener {
	/**
	 * The binding to the internal state of the widget
	 */
	Binding binding;

	/**
	 * Constructs a color map editor panel which uses a binding created by
	 * accessing the setter and getter functions of the specified property in
	 * the specified Java bean.
	 * 
	 * @param bean
	 * @param property
	 */
	public JLColorMapEditorPanel(Object bean, String property) {
		this(Binding.createBinding(bean, property));
	}

	/**
	 * Constructs a color map editor panel which uses a binding created by
	 * accessing the setter and getter functions of the specified property in
	 * the specified Java bean.
	 * 
	 * @param bean
	 * @param property
	 * @param modeSwitchingEnabled
	 *            Whether or not the user is presented with the option of
	 *            switching between continuous and discreet modes
	 */
	public JLColorMapEditorPanel(Object bean, String property,
			boolean modeSwitchingEnabled) {
		this(bean, property);
		this.modeSwitchingEnabled = modeSwitchingEnabled;
	}

	/**
	 * Constructs a color map editor panel which uses a binding created by
	 * accessing the specified setter and getter functions
	 * 
	 * @param getterFunction
	 *            the getter function
	 * @param setterFunction
	 *            the setter function
	 */
	public JLColorMapEditorPanel(Object getterFunction, Object setterFunction) {
		this(Binding.createBinding(getterFunction, setterFunction));
	}

	/**
	 * Construct a color map editor panel which is linked to the some internal
	 * state which is accessed through the specified binding.
	 * 
	 * @param binding
	 *            the binding used to access the internal state for this widget
	 */
	public JLColorMapEditorPanel(Binding binding) {
		super(ColorMap.getDefaultContinuousColorMap());
		this.binding = binding;
		// set up this widget with the JLWidget periodic check and update
		// framework (periodically, the internal and external states are checked
		// for consistency. when they don't match, the external state (the
		// actual state of the UI) is changed to reflect the internal state).
		JLPeriodicUpdateThread.setupJLWidget(this);
		// set this flag so that the load/save preset options will appear in the
		// popup menu
		supportsPresets = true;
		setUpPresets();
	}

	/**
	 * Does nothing, because there are no threads owned by this widget.
	 */
	public void kill() {

	}

	/**
	 * This method is called whenever the color map is changed.
	 */
	protected void somethingHasChanged() {
		binding.setInternalState(getExternalState());
	}

	/**
	 * This method is called when the user clicks on the "save preset" option of
	 * the popup menu.
	 * 
	 */
	protected void savePreset() {
		String fileName = JOptionPane.showInputDialog("Enter Preset Name");

		if (fileName != null)
			XMLFileIO.writeToFile(new ColorMapModel(getAssociatedColorMap()),
					new File(colorMapPresetsDirectory + "\\" + fileName + "."
							+ colorMapFileExtension));
	}

	/**
	 * This method is called when the user clicks on the "load preset" option of
	 * the popup menu.
	 * 
	 * 
	 */
	protected void loadPresetFromFile() {
		String filePath = FileUtils.promptUserToOpenFile(
				JyVisSettings.runtimeRoot + "colorMaps", "Color Map",
				"colormap");
		if (filePath != null) {
			Object object = XMLFileIO.readFromFile(new File(filePath));
			ColorMapModel model = object instanceof ColorMapModel ? (ColorMapModel) object
					: null;
			if (model != null)
				setColorMap(model.getAsColorMap());
		}
		somethingHasChanged();
	}

	public void setExternalState(ColorMap newValue) {
		setColorMap(newValue);
	}

	public ColorMap getExternalState() {
		return getAssociatedColorMap();
	}

	public ColorMap getInternalState() {
		return (ColorMap) binding.getInternalStateAs(ColorMap.class);
	}

	private void setUpPresets() {
		colorMapPresetsDirectory = JyVisSettings.runtimeRoot + "colorMaps";
		if (ColorMapEditorPanel.availablePresets == null)
			reloadPresets();
	}

	public void reloadPresets() {
		ColorMapEditorPanel.availablePresets = new TreeMap<String, ColorMapModel>();
		File directory = new File(colorMapPresetsDirectory);
		if (directory.exists()) {
			// get the files in the current directory
			File[] subfiles = directory.listFiles();
			// create menus or menu items for each of the files
			for (int i = 0; i < subfiles.length; i++) {
				// get the name of the file
				String name = subfiles[i].getName();
				// chop off the file extension
				int l = name.lastIndexOf('.');
				if (l != -1) {
					String ext = name.substring(l + 1).toLowerCase();
					name = name.substring(0, l);
					if (ext.equals(colorMapFileExtension)) {
						Object object = XMLFileIO.readFromFile(subfiles[i]);
						if (object instanceof ColorMapModel)
							availablePresets.put(name, (ColorMapModel) object);
					}
				}
			}
		}
		super.reloadPresets();
	}

}
/*
 * CVS Log
 * 
 * $Log: JLColorMapEditorPanel.java,v $
 * Revision 1.1  2007/08/15 17:59:14  curran
 * Initial commit to SourceForge
 * Revision 1.5 2007/08/04 17:23:48
 * ckellehe Removed color map dependency on JyVis Revision 1.4 2007/08/02
 * 15:16:33 ckellehe Added delete color map preset menu
 * 
 * Revision 1.3 2007/08/01 22:03:48 ckellehe Added color map editor GUI features
 * Revision 1.2 2007/08/01 19:36:01 ckellehe Changed the color map editor in the
 * system UI - disabled continuous toggling Revision 1.1 2007/07/26 00:31:00
 * ckellehe Initial Creation Revision 1.10 2007/06/27 20:13:23 rbeaven *** empty
 * log message ***
 * 
 * Revision 1.9 2007/06/27 19:08:04 rbeaven implemented discrete color maps
 * 
 * Revision 1.8 2007/06/27 14:49:02 ckellehe Renamed some scripting related
 * files, created general XML file IO, changed color and shape map to use it,
 * restructured sessions
 * 
 * Revision 1.7 2007/06/19 18:50:49 ckellehe Encapsulated Function objects from
 * scripts such that internal state script generation is independent of the
 * language used to create the function Revision 1.6 2007/06/15 16:54:33
 * ckellehe Added a ShapeMap binding requirement for JyVisScriptingEngine, used
 * it in JLShapeMapEditorPanel
 * 
 * Revision 1.5 2007/06/15 16:15:30 ckellehe Added a ColorMap binding
 * requirement for JyVisScriptingEngine, used it in JLColorMapEditorPanel
 * Revision 1.4 2007/06/14 15:50:56 ckellehe Renamed JythonBottleneck to
 * ScriptBottleneck
 * 
 * Revision 1.3 2007/06/11 15:00:46 rbeaven *** empty log message ***
 * 
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
