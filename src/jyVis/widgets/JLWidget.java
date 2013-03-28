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

/**
 * The common interface for all UI widgets which work with the JLWidget ("Jython
 * Linked Widget") framework. In this framework, JLWidgets have an internal
 * state (the model of the MVC paradigm) and an external state (the view and
 * controller of the MVC paradigm). When the user interacts with the JLWidgets,
 * a script is executed via JythonBottleneck.exec() which sets its internal
 * state to match it's newly changed external state. Upon constructon, a
 * JLWidget must set itself up with the JLPeriodicUpdateThread, which
 * periodically compares the JLWidgets internal and external states. If they are
 * different, the external state is set to match the internal state.
 * 
 * @author Curran Kelleher
 * 
 */
public interface JLWidget<T> {

	/**
	 * Part of the JLWidget framework. Gets the internal value, the "model," the
	 * value currently held internally.
	 */
	T getInternalState();

	/**
	 * Part of the JLWidget framework. Gets the external value, the "view," the
	 * value currently being displayed by the UI.
	 */
	T getExternalState();

	/**
	 * Part of the JLWidget framework. Sets the external value, the "view," the
	 * value currently being displayed by the UI. This actually updates the UI,
	 * which may cause events to be fired, but the events in this case should be
	 * ignored (no script should be generated). The implementation of this
	 * method needs to take care of that, typically by setting a flag to ignore
	 * UI events, changing the UI, then turning that flag off. If this is not
	 * done, there are redundant double executions of scripts.
	 * 
	 * @param newValue
	 *            the new value which the UI should have
	 */
	void setExternalState(T newValue);

	/**
	 * Kills the widget. Most importantly, if the widget has any dangling
	 * threads attached to it, calling this method should kill them, so they do
	 * not interfere with session replay (for example, when a timer goes off to
	 * update the internal state after GlobalObjects has been cleared, an
	 * exception is thrown because the object referenced by the script (sent by
	 * the timer terminating) is not there anymore)
	 * 
	 */
	void kill();

}
/*
 * CVS Log
 * 
 * $Log: JLWidget.java,v $
 * Revision 1.1  2007/08/15 17:59:14  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:31:00 ckellehe Initial
 * Creation Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
