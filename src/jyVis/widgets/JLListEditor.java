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

import listEditor.ListEditor;
import listEditor.ListEditorListener;
import listEditor.ListState;
import scripting.Binding;

@SuppressWarnings("serial")
public class JLListEditor extends ListEditor implements JLWidget<ListState>,
		ListEditorListener {
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
	 * Creates a list editor widget which is fully linked to the some internal
	 * state which is accessed through the specified binding.
	 * 
	 * @param list
	 *            the list of things to put in the list editor, all included
	 *            initially
	 * @param binding
	 *            the binding used to access the internal state for this widget
	 */
	public JLListEditor(Object[] list, Binding binding) {
		super(list);

		this.binding = binding;
		// add the listener
		this.addListEditorListener(this);
		// set up this frame with the JLWidget periodic check and update
		// framework (periodically, the internal and external states are checked
		// for consistency. when they don't match, the external state (the
		// actual state of the UI) is changed to reflect the internal state).
		JLPeriodicUpdateThread.setupJLWidget(this);
	}

	/**
	 * Constructs a list editor which uses a binding created by accessing the
	 * setter and getter functions of the specified property in the specified
	 * Java bean.
	 * 
	 * @param list
	 *            the list of things to put in the list editor, all included
	 *            initially
	 * @param bean
	 * @param property
	 */
	public JLListEditor(Object[] list, Object bean, String property) {
		this(list, Binding.createBinding(bean, property));
	}

	/**
	 * Constructs a list editor which uses a binding created by accessing the
	 * specified setter and getter functions
	 * 
	 * @param list
	 *            the list of things to put in the list editor, all included
	 *            initially
	 * @param getterFunction
	 *            the getter function
	 * @param setterFunction
	 *            the setter function
	 */
	public JLListEditor(Object[] list, Object getterFunction,
			Object setterFunction) {
		this(list, Binding.createBinding(getterFunction, setterFunction));
	}

	/**
	 * Called in response to a change in the list
	 */
	public void ListChanged(ListState state) {
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
	public ListState getInternalState() {
		return (ListState) binding.getInternalStateAs(ListState.class);
	}

	/**
	 * Part of the JLWidget framework. Gets the external value, the "view," the
	 * value currently being displayed by the UI.
	 */
	public ListState getExternalState() {
		return getListState();
	}

	/**
	 * Sets the external state
	 */
	public void setExternalState(ListState state) {
		ignoreExternalChanges = true;
		setListState(state);
		ignoreExternalChanges = false;
	}

	/**
	 * This method does nothing, no processes to kill.
	 */
	public void kill() {

	}

}
