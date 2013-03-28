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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import scripting.Binding;

/**
 * A check box JLWidget, inherits from JCheckBox.
 * 
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class JLCheckBox extends JCheckBox implements ItemListener,
		JLWidget<Boolean> {

	/**
	 * The binding to the internal state of the widget
	 */
	Binding binding;

	/**
	 * A flag indicating whether or not to respond to external changes. If
	 * false, UI events will be executed as scripts. If true, UI events will be
	 * ignored. This is to avoid unnecessarily executing scripts.
	 */
	private boolean ignoreExternalChanges = false;

	/**
	 * Constructs a check box which uses a binding created by accessing the
	 * setter and getter functions of the specified property in the specified
	 * Java bean.
	 * 
	 * @param string
	 *            the text to display next to the check box
	 * @param bean
	 * @param property
	 */
	public JLCheckBox(String string, Object bean, String property) {
		this(string, Binding.createBinding(bean, property));
	}

	/**
	 * Constructs a check box which uses a binding created by accessing the
	 * specified setter and getter functions
	 * 
	 * @param string
	 *            the text to display next to the check box
	 * @param getterFunction
	 *            the getter function
	 * @param setterFunction
	 *            the setter function
	 */
	public JLCheckBox(String string, Object getterFunction,
			Object setterFunction) {
		this(string, Binding.createBinding(getterFunction, setterFunction));
	}

	/**
	 * Creates a check box widget which is fully linked to the some internal
	 * state which is accessed through the specified binding.
	 * 
	 * @param binding
	 *            the binding used to access the internal state for this widget
	 * @param text
	 *            the text displayed along with the check box
	 */
	public JLCheckBox(String text, Binding binding) {
		// set the text
		super(text);
		this.binding = binding;

		// add the listener which will receive check/uncheck events from the
		// check box
		addItemListener(this);
		// set up this frame with the JLWidget periodic check and update
		// framework (periodically, the internal and external states are checked
		// for consistency. when they don't match, the external state (the
		// actual state of the UI) is changed to reflect the internal state).
		JLPeriodicUpdateThread.setupJLWidget(this);
	}

	/**
	 * Called when the check box is checked or unchecked
	 */
	public void itemStateChanged(ItemEvent e) {
		// if the "ignore UI events" flag is not set...
		if (!ignoreExternalChanges)
			binding.setInternalState(getExternalState());
	}

	/**
	 * Part of the JLWidget framework. Gets the internal value, the "model," the
	 * value currently held internally, accessed by the getter ans setter
	 * methods (indexed in GlobalObjects by the indices in getterFunctionIndex
	 * and setterFunctionIndex)
	 */
	public Boolean getInternalState() {
		return (Boolean) binding.getInternalStateAs(Boolean.class);
	}

	/**
	 * Part of the JLWidget framework. Gets the external value, the "view," the
	 * value currently being displayed by the UI.
	 */
	public Boolean getExternalState() {
		return isSelected();
	}

	/**
	 * Part of the JLWidget framework. Sets the external value, the "view," the
	 * value currently being displayed by the UI. This actually updates the UI,
	 * which may cause events to be fired, but the events in this case will be
	 * ignored because when this method is called, the IgnoreExternalChanges
	 * flag will have been set to true (via the setIgnoreExternalChanges()
	 * method)
	 */
	public void setExternalState(Boolean val) {
		ignoreExternalChanges = true;
		setSelected(val);
		ignoreExternalChanges = false;
	}

	/**
	 * This method does nothing, no processes to kill.
	 */
	public void kill() {

	}
}
/*
 * CVS Log
 * 
 * $Log: JLCheckBox.java,v $
 * Revision 1.1  2007/08/15 17:59:14  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:31:00 ckellehe Initial
 * Creation Revision 1.9 2007/06/19 18:50:49 ckellehe Encapsulated Function
 * objects from scripts such that internal state script generation is
 * independent of the language used to create the function Revision 1.8
 * 2007/06/14 19:40:29 ckellehe Added a String binding requirement for
 * JVScriptingEngine, used it in JLTextField Revision 1.7 2007/06/14 18:44:14
 * ckellehe Changed the widget scripting framework to include automatic
 * recognition of languages based on function objects
 * 
 * Revision 1.6 2007/06/14 16:25:43 ckellehe Created JVScriptingEngineManager
 * and made other changes to the scripting framework Revision 1.5 2007/06/14
 * 15:50:56 ckellehe Renamed JythonBottleneck to ScriptBottleneck
 * 
 * Revision 1.4 2007/06/14 15:49:55 ckellehe Started implementing
 * JVScriptingEngine
 * 
 * Revision 1.3 2007/06/13 20:33:36 ckellehe Created and started implementing
 * the JLWidgetBinding framework, which separates Jython-specific code from the
 * widget code. This is the first step toward having the ability to swap out
 * Jython for other scripting languages.
 * 
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
