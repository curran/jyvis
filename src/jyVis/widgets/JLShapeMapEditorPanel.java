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

import jyVis.JyVisSettings;
import scripting.Binding;
import shapeMap.Shape;
import shapeMap.ShapeMap;
import shapeMap.ShapeMapEditorPanel;
import shapeMap.ShapeMapModel;
import fileUtils.FileUtils;
import fileUtils.XMLFileIO;

/**
 * A panel which displays a ShapeMap and provides user interaction for editing
 * it. Shapes in the ShapeMap are drawn on this panel, and are movable.
 * 
 * @author Curran Kelleher
 */
@SuppressWarnings("serial")
public class JLShapeMapEditorPanel extends ShapeMapEditorPanel implements
		JLWidget<ShapeMap>, MouseListener {
	/**
	 * The binding to the internal state of the widget
	 */
	Binding binding;

	/**
	 * Constructs a shape map editor panel which uses a binding created by
	 * accessing the setter and getter functions of the specified property in
	 * the specified Java bean.
	 * 
	 * @param bean
	 * @param property
	 */
	public JLShapeMapEditorPanel(Object bean, String property) {
		this(Binding.createBinding(bean, property));
	}

	/**
	 * Constructs a shape map editor panel which uses a binding created by
	 * accessing the specified setter and getter functions
	 * 
	 * @param getterFunction
	 *            the getter function
	 * @param setterFunction
	 *            the setter function
	 */
	public JLShapeMapEditorPanel(Object getterFunction, Object setterFunction) {
		this(Binding.createBinding(getterFunction, setterFunction));
	}

	/**
	 * Construct a shape map editor panel which is linked to the some internal
	 * state which is accessed through the specified binding.
	 * 
	 * @param binding
	 *            the binding used to access the internal state for this widget
	 */
	public JLShapeMapEditorPanel(Binding binding) {
		super(ShapeMap.getDefaultShapeMap());
		this.binding = binding;
		// set up this widget with the JLWidget periodic check and update
		// framework (periodically, the internal and external states are checked
		// for consistency. when they don't match, the external state (the
		// actual state of the UI) is changed to reflect the internal state).
		JLPeriodicUpdateThread.setupJLWidget(this);

		// set this flag so that the load/save preset options will appear in the
		// popup menu
		supportsPresets = true;
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
	 * 
	 */
	protected void savePreset() {
		String filePath = FileUtils.promptUserToSaveFile(
				JyVisSettings.runtimeRoot + "shapeMaps", "Shape Map",
				"shapemap");
		if (filePath != null)
			XMLFileIO.writeToFile(new ShapeMapModel(getAssociatedShapeMap()),
					new File(filePath));
	}

	/**
	 * This method is called when the user clicks on the "load preset" option of
	 * the popup menu.
	 * 
	 * 
	 */
	protected void loadPreset() {
		String filePath = FileUtils.promptUserToOpenFile(
				JyVisSettings.runtimeRoot + "shapeMaps", "Shape Map",
				"shapemap");
		if (filePath != null) {
			Object object = XMLFileIO.readFromFile(new File(filePath));
			ShapeMapModel model = object instanceof ShapeMapModel ? (ShapeMapModel) object
					: null;
			if (model != null)
				setShapeMap(model.getAsShapeMap());
		}
		somethingHasChanged();
	}

	/**
	 * This method is called when the user clicks on the "Create Shape File"
	 * option of the popup menu.
	 * 
	 */
	protected void saveShape(Shape shape) {
		String filePath = FileUtils.promptUserToSaveFile(
				JyVisSettings.runtimeRoot + "shapeMaps/shapes", "Shape",
				"shape");
		if (filePath != null)
			XMLFileIO.writeToFile(shape, (new File(filePath)));
	}

	/**
	 * This method is called to get the shape to add when the user clicks on the
	 * "Create Shape" option of the popup menu.
	 * 
	 * @return the Shape object to add to the shape map, or null if there is no
	 *         shape to add
	 */
	protected Shape loadShape() {
		String filePath = FileUtils.promptUserToOpenFile(
				JyVisSettings.runtimeRoot + "shapeMaps/shapes", "Shape",
				"shape");
		if (filePath != null) {
			Object object = XMLFileIO.readFromFile(new File(filePath));
			return object instanceof Shape ? (Shape) object : null;
		} else
			return null;

	}

	public void setExternalState(ShapeMap newValue) {
		setShapeMap(newValue);
	}

	public ShapeMap getExternalState() {
		return getAssociatedShapeMap();
	}

	public ShapeMap getInternalState() {
		return (ShapeMap) binding.getInternalStateAs(ShapeMap.class);
	}
}
/*
 * CVS Log
 * 
 * $Log: JLShapeMapEditorPanel.java,v $
 * Revision 1.1  2007/08/15 17:59:13  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:31:00
 * ckellehe Initial Creation Revision 1.7 2007/06/27 14:49:02 ckellehe Renamed
 * some scripting related files, created general XML file IO, changed color and
 * shape map to use it, restructured sessions Revision 1.6 2007/06/19 18:50:49
 * ckellehe Encapsulated Function objects from scripts such that internal state
 * script generation is independent of the language used to create the function
 * Revision 1.5 2007/06/15 16:54:33 ckellehe Added a ShapeMap binding
 * requirement for JyVisScriptingEngine, used it in JLShapeMapEditorPanel
 * Revision 1.4 2007/06/14 15:50:56 ckellehe Renamed JythonBottleneck to
 * ScriptBottleneck
 * 
 * Revision 1.3 2007/06/11 15:00:46 rbeaven *** empty log message ***
 * 
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
