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

import javax.swing.JMenuItem;

import scripting.Function;
import scripting.ScriptBottleneck;
import scripting.ScriptingEngineManager;

/**
 * A menu item which, when clicked, executes a Jython script through the Jython
 * bottleneck.
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class JLMenuItem extends JMenuItem {
	/**
	 * Construct a menu item which, when clicked, calls the specified function.
	 * This is intended for situations where the callback function asks the user
	 * for input, then executes a script to perform the resulting action (this
	 * way, asking for the user input doesn't get replayed).
	 * 
	 * @param title
	 *            the text to display on the menu item
	 * @param callbackFunction
	 *            the function to call when this menu item is clicked
	 */
	public JLMenuItem(String title, Object callbackFunction) {
		super(title);
		final Function callback = ScriptingEngineManager.getEngineForFunction(
				callbackFunction).wrapFunction(callbackFunction);

		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				callback.call(null);
			}
		});
	}

	/**
	 * Construct a menu item which, when clicked, executes a the specified
	 * script. This is intended for actions which do not require user input and
	 * should be replayed when a session is replayed.
	 * 
	 * @param title
	 *            the text to display on the menu item
	 * @param script
	 *            the Python script to execute when this menu item is clicked
	 */
	public JLMenuItem(String title, final String script) {
		super(title);
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ScriptBottleneck.exec("python", script);
			}
		});
	}
}
/*
 * CVS Log
 * 
 * $Log: JLMenuItem.java,v $
 * Revision 1.1  2007/08/15 17:59:14  curran
 * Initial commit to SourceForge
 * Revision 1.3 2007/08/01 19:06:42 ckellehe Created
 * system property panel Revision 1.2 2007/07/30 23:43:04 ckellehe Cleaned up
 * Javadoc Revision 1.1 2007/07/26 00:31:00 ckellehe Initial Creation
 * 
 * Revision 1.5 2007/06/19 18:50:49 ckellehe Encapsulated Function objects from
 * scripts such that internal state script generation is independent of the
 * language used to create the function Revision 1.4 2007/06/15 17:15:54
 * ckellehe Added a generateCallableFunction binding requirement for
 * JVScriptingEngine, used it in JLMenuItem Revision 1.3 2007/06/12 20:16:26
 * ckellehe Changed JLMenuItem so it takes a Python callback function as opposed
 * to a Python script string to execute when it is clicked. This enables correct
 * behavior in replaying of scripts and remote synchronication.
 * 
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
