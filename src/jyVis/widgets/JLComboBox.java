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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;

import scripting.Binding;

/**
 * A combo box selector JLWidget, inherits from JComboBox.
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class JLComboBox extends JComboBox implements JLWidget<Integer>,
		ActionListener {
	/**
	 * The binding to the internal state of the widget
	 */
	Binding binding;

	/**
	 * A flag indicating whether or not to respond to external changes. If
	 * false, UI events will be executed as scripts. If true, UI events will be
	 * ignored. This is to avoid unnecessarily executing scripts.
	 */
	private boolean ignoreExternalChanges;

	/**
	 * Creates a combo box widget which is fully linked to the some internal
	 * state which is accessed through the specified binding.
	 * 
	 * @param binding
	 *            the binding used to access the internal state for this widget
	 */
	public JLComboBox(List<?> list, Binding binding) {
		super(new Vector<Object>(list));

		this.binding = binding;
		// add the selection listener
		addActionListener(this);
		// set up this frame with the JLWidget periodic check and update
		// framework (periodically, the internal and external states are checked
		// for consistency. when they don't match, the external state (the
		// actual state of the UI) is changed to reflect the internal state).
		JLPeriodicUpdateThread.setupJLWidget(this);
	}

	/**
	 * Constructs a combo box which uses a binding created by accessing the
	 * setter and getter functions of the specified property in the specified
	 * Java bean.
	 * 
	 * @param list
	 *            the list of things to put in the combo box
	 * @param bean
	 * @param property
	 */
	public JLComboBox(List<?> list, Object bean, String property) {
		this(list, Binding.createBinding(bean, property));
	}

	/**
	 * Constructs a combo box which uses a binding created by accessing the
	 * specified setter and getter functions
	 * 
	 * @param list
	 *            the list of things to put in the combo box
	 * @param getterFunction
	 *            the getter function
	 * @param setterFunction
	 *            the setter function
	 */
	public JLComboBox(List<?> list, Object getterFunction, Object setterFunction) {
		this(list, Binding.createBinding(getterFunction, setterFunction));
	}

	/**
	 * Called in response to a selection being made in the combo box
	 */
	public void actionPerformed(ActionEvent e) {
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
	public Integer getInternalState() {
		return (Integer) binding.getInternalStateAs(Integer.class);
	}

	/**
	 * Part of the JLWidget framework. Gets the external value, the "view," the
	 * value currently being displayed by the UI.
	 */
	public Integer getExternalState() {
		return getSelectedIndex();
	}

	/**
	 * Part of the JLWidget framework. Sets the external value, the "view," the
	 * value currently being displayed by the UI. This actually updates the UI,
	 * which may cause events to be fired, but the events in this case will be
	 * ignored because when this method is called, the IgnoreExternalChanges
	 * flag will have been set to true (via the setIgnoreExternalChanges()
	 * method)
	 */
	public void setExternalState(Integer o) {
		ignoreExternalChanges = true;
		setSelectedIndex(o.intValue());
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
 * $Log: JLComboBox.java,v $
 * Revision 1.1  2007/08/15 17:59:14  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/30 23:43:04 ckellehe Cleaned
 * up Javadoc
 * 
 * Revision 1.1 2007/07/26 00:31:00 ckellehe Initial Creation Revision 1.6
 * 2007/06/20 21:26:59 ckellehe Partially implemented a Java scripting engine,
 * so that Java code can now specify callback functions for widgets, so can now
 * use the wiget framework directly. Used the Java engine to implement a text
 * field + combo box duo, which was previously implemented in Python. Now this
 * text field + combo box interface is available as a standard widget, and can
 * be used in all supported languages Created an example Groovy script that uses
 * the text field + combo box interface.
 * 
 * Revision 1.5 2007/06/19 18:50:49 ckellehe Encapsulated Function objects from
 * scripts such that internal state script generation is independent of the
 * language used to create the function Revision 1.4 2007/06/15 15:01:26
 * ckellehe implemted the JLWidgetBinding framework in JLComboBox Revision 1.3
 * 2007/06/14 15:50:56 ckellehe Renamed JythonBottleneck to ScriptBottleneck
 * 
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
