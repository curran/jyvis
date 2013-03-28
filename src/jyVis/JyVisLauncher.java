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
package jyVis;

import javax.swing.UIManager;

import scripting.ScriptBottleneck;

/**
 * The entry point of JyVis. It sets the native look and feel, then executes the
 * file "init.py".
 * 
 * @author Curran Kelleher
 * 
 */
public class JyVisLauncher {
	/**
	 * The entry point of JyVis, executes the file "init.py".
	 * 
	 * @param args
	 *            command line arguments not used
	 */
	public static void main(String[] args) {
		launchJyVis();
	}

	/**
	 * Sets the native look and feel, then executes the file "init.py".
	 * 
	 */
	public static void launchJyVis() {
		// set the native look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// execute the initialization script
		ScriptBottleneck.execfile(JyVisSettings.runtimeRoot + "init.py");
	}
}
/*
 * CVS Log
 * 
 * $Log: JyVisLauncher.java,v $
 * Revision 1.1  2007/08/15 17:59:16  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:31:01 ckellehe
 * Initial Creation
 * 
 * 
 */
