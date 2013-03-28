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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import scripting.Binding;

/**
 * A text field JLWidget, inherits from JTextField.
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class JLTextField extends JTextField implements JLWidget<String>,
		KeyListener {

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
	 * A flag indicating that setExternalState is being called for the first
	 * time. This means that the text should be updated no matter what, but
	 * normally the text should not be updated if the text field has focus
	 * (because that would annoy the user)
	 */
	private boolean firstTimeSettingExternalState = true;

	/**
	 * Constructs a text field which uses a binding created by accessing the
	 * setter and getter functions of the specified property in the specified
	 * Java bean.
	 * 
	 * @param bean
	 * @param property
	 */
	public JLTextField(Object bean, String property) {
		this(Binding.createBinding(bean, property));
	}

	/**
	 * Constructs a text field which uses a binding created by accessing the
	 * specified setter and getter functions
	 * 
	 * @param getterFunction
	 *            the getter function for the text of this text field
	 * @param setterFunction
	 *            the setter function for the text of this text field
	 */
	public JLTextField(Object getterFunction, Object setterFunction) {
		this(Binding.createBinding(getterFunction, setterFunction));
	}

	/**
	 * Creates a text field widget which is linked to the some internal state
	 * which is accessed through the specified binding.
	 * 
	 * @param binding
	 *            the binding used to access the internal state for this widget
	 */
	public JLTextField(Binding binding) {
		this.binding = binding;

		// set up the key listener to listen for when the user hits the enter
		// key
		addKeyListener(this);
		// set up this frame with the JLWidget periodic check and update
		// framework (periodically, the internal and external states are checked
		// for consistency. when they don't match, the external state (the
		// actual state of the UI) is changed to reflect the internal state).
		JLPeriodicUpdateThread.setupJLWidget(this);
	}

	public void keyPressed(KeyEvent e) {

		// if the "ignore UI events" flag is not set...
		if (e.getKeyChar() == '\n' && !ignoreExternalChanges
				&& !getInternalState().equals(getExternalState()))
			binding.setInternalState(getExternalState());
	}

	public void keyReleased(KeyEvent arg0) {

	}

	public void keyTyped(KeyEvent arg0) {

	}

	/**
	 * Part of the JLWidget framework. Gets the internal value, the "model," the
	 * value currently held internally, accessed by the getter ans setter
	 * methods (indexed in GlobalObjects by the indices in getterFunctionIndex
	 * and setterFunctionIndex)
	 */
	public String getInternalState() {
		return (String) binding.getInternalStateAs(String.class);
	}

	/**
	 * Part of the JLWidget framework. Gets the external value, the "view," the
	 * value currently being displayed by the UI.
	 */
	public String getExternalState() {
		return getText();
	}

	/**
	 * Part of the JLWidget framework. Sets the external value, the "view," the
	 * value currently being displayed by the UI. This actually updates the UI,
	 * which may cause events to be fired, but the events in this case will be
	 * ignored because when this method is called, the IgnoreExternalChanges
	 * flag will have been set to true (via the setIgnoreExternalChanges()
	 * method)
	 */
	public void setExternalState(String val) {
		ignoreExternalChanges = true;
		// if this is the first time (the initialization), then set the text
		// regardless
		if (firstTimeSettingExternalState) {
			firstTimeSettingExternalState = false;
			setText(val);
		}
		// don't set it if it has focus, that would annoy the user
		else if (!hasFocus())
			setText(val);
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
 * $Log: JLTextField.java,v $
 * Revision 1.1  2007/08/15 17:59:14  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/30 23:43:04 ckellehe Cleaned
 * up Javadoc Revision 1.1 2007/07/26 00:31:00 ckellehe Initial Creation
 * 
 * Revision 1.6 2007/06/19 18:50:49 ckellehe Encapsulated Function objects from
 * scripts such that internal state script generation is independent of the
 * language used to create the function Revision 1.5 2007/06/14 19:52:34
 * ckellehe Added a Integer binding requirement for JVScriptingEngine, used it
 * in JLList
 * 
 * Revision 1.4 2007/06/14 19:40:29 ckellehe Added a String binding requirement
 * for JVScriptingEngine, used it in JLTextField
 * 
 * Revision 1.3 2007/06/14 15:50:56 ckellehe Renamed JythonBottleneck to
 * ScriptBottleneck
 * 
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
