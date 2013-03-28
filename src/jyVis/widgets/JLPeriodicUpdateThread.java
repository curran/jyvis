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

import java.util.ArrayList;
import java.util.List;

/**
 * The periodic update thread for the JLWidget framework. In this framework,
 * JLWidgets have an internal state (the model of the MVC paradigm) and an
 * external state (the view and controller of the MVC paradigm). When the user
 * interacts with the JLWidgets, a script is executed via
 * JythonBottleneck.exec() which sets its internal state to match it's newly
 * changed external state. Upon constructon, a JLWidget must set itself up with
 * the JLPeriodicUpdateThread, which periodically compares the JLWidgets
 * internal and external states. If they are different, the external state is
 * set to match the internal state.
 * 
 * @author Curran Kelleher
 * 
 */
public class JLPeriodicUpdateThread implements Runnable {
	/**
	 * The time between updates.
	 */
	public static int periodTime = 1000;

	/**
	 * When true, the update thread functions. When false, the update thread
	 * does not function until it is set to true again.
	 */
	public static boolean performUpdates = true;

	/**
	 * The singleton instance of JLPeriodicUpdateThread.
	 */
	private static JLPeriodicUpdateThread INSTANCE = null;

	/**
	 * The list of all widgets which get checked and updated if necessary.
	 */
	private List<JLWidget<?>> widgets = new ArrayList<JLWidget<?>>();

	/**
	 * The constructor is private because this class is a singleton.
	 * 
	 */
	private JLPeriodicUpdateThread() {
		// start the update thread
		(new Thread(this)).start();
	}

	/**
	 * Gets the singleton instance of JLPeriodicUpdateThread
	 * 
	 */
	private static JLPeriodicUpdateThread getInstance() {
		if (INSTANCE == null)
			INSTANCE = new JLPeriodicUpdateThread();
		return INSTANCE;
	}

	/**
	 * Called to start the update thread. Updates all widgets periodically
	 * (every "periodTime" milliseconds)
	 * 
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(periodTime);
				if (performUpdates)
					_updateAllWidgets();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Iterates through all widgets, checking for differences between internal
	 * and external states, and updating the external to match the internal if
	 * they are different.
	 * 
	 */
	private void _updateAllWidgets() {
		synchronized (widgets) {
			for (JLWidget<?> widget : widgets)
				updateWidget(widget);
		}
	}

	@SuppressWarnings("unchecked")
	private void updateWidget(JLWidget widget) {
		Object internalState = widget.getInternalState();
		Object externalState = widget.getExternalState();
		// compare the internal and external values.
		// if they are out of synch...
		if (!internalState.equals(externalState))
			// set the external value to reflect the internal value.
			widget.setExternalState(internalState);
	}

	/**
	 * Sets up a JLWidget so that periodically, its internal and external states
	 * are checked for consistency, and if they are different, out of synch,
	 * then the external is set to match the internal.
	 * 
	 */
	public static void setupJLWidget(JLWidget<?> widget) {
		getInstance().updateWidget(widget);
		List<JLWidget<?>> widgetsList = getInstance().widgets;
		synchronized (widgetsList) {
			widgetsList.add(widget);
		}
	}

	/**
	 * Iterates through all widgets, checking for differences between internal
	 * and external states, and updates the external to match the internal if
	 * they are different.
	 * 
	 */
	public static void updateAllWidgets() {
		getInstance()._updateAllWidgets();
	}

	/**
	 * Calls kill() on all widgets, and clears the update thread's list of
	 * widgets to update. Probably the only time this should be called is when a
	 * session is being replayed.
	 * 
	 */
	public static void killAllWidgets() {
		List<JLWidget<?>> widgets = getInstance().widgets;
		synchronized (widgets) {
			for (JLWidget<?> widget : widgets)
				widget.kill();
			widgets.clear();
		}
	}
}
/*
 * CVS Log
 * 
 * $Log: JLPeriodicUpdateThread.java,v $
 * Revision 1.2  2008/09/29 20:59:51  curran
 * Removed unnecessary @SuppressWarnings tags
 *
 * Revision 1.1  2007/08/15 17:59:14  curran
 * Initial commit to SourceForge
 * Revision 1.2 2007/07/26 14:51:47
 * ckellehe Corrected type checking issues
 * 
 * Revision 1.1 2007/07/26 00:31:00 ckellehe Initial Creation
 * 
 * Revision 1.4 2007/06/28 16:18:21 ckellehe Made the "play all events" action
 * in the session replay UI temporarily turn off the update thread. Revision 1.3
 * 2007/06/27 20:03:08 ckellehe Fixed a bug where the internal frame created fr
 * a visualization by a script was not getting displayed properly. Revision 1.2
 * 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
