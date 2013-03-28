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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import jyVis.data.CSVFileUtils;
import jyVis.data.DataSelection;
import jyVis.data.DataTable;
import jyVis.graphics.DrawingPanel;
import jyVis.widgets.JLPeriodicUpdateThread;
import jyVis.widgets.UIUtils;
import rScripts.RInterface;
import scripting.ScriptBottleneck;
import scripting.ScriptingEngineManager;
import session.ReplayUI;
import session.SessionEntry;
import fileUtils.FileUtils;
import fileUtils.XMLFileIO;

/**
 * The global access point into JyVis, and a bunch of convenience methods.
 * 
 * @author Curran Kelleher
 * 
 */
public class JyVis {
	/**
	 * The single JyVis base environment
	 */
	private static JyVisEnvironment baseEnvironment = new JyVisEnvironment();

	/**
	 * Prompts the user to open a CSV data table. When a CSV file is selected
	 * and read, it is added to the list of data tables, and set as the
	 * currently selected one.
	 * 
	 */
	public static void promptUserToOpenDataTable() {
		final String path = FileUtils.promptUserToOpenFile(
				JyVisSettings.defaultDataDirectory, "Comma Separated Value",
				"csv");
		if (path != null)// if the user DID select a file
		{
			ScriptBottleneck.importIfNotDefined("python", JyVis.class);
			ScriptBottleneck.exec("python", "JyVis.openDataTable(\"" + path
					+ "\")");
		}
	}

	/**
	 * Prompts the user to save a session file of the current session.
	 * 
	 */
	public static void promptUserToSaveSession() {
		// ensure the existence of the session log directory
		new File(JyVisSettings.defaultSessionDirectory).mkdir();

		String path = FileUtils.promptUserToSaveFile(
				JyVisSettings.defaultSessionDirectory, "Session",
				JyVisSettings.sessionExtension);

		if (path != null)
			XMLFileIO.writeToFile(ScriptBottleneck.currentSession, new File(
					path));
	}

	/**
	 * Prompts the user to open and replay a session file.
	 * 
	 */
	public static void promptUserToReplaySession() {
		replaySession(FileUtils.promptUserToOpenFile(
				JyVisSettings.defaultSessionDirectory, "Session",
				JyVisSettings.sessionExtension));
	}

	/**
	 * Prompts the user to export the current selection as a csv file
	 */
	public static void promptUserToExportSelection() {
		DataTable selectedDataTable = baseEnvironment.selectedDataTable;
		if (selectedDataTable == null)
			UIUtils
					.showErrorDialog("There is no data open, thus no selection to save");
		else {
			List<DataSelection> selections = selectedDataTable.getSelections();
			if (selections == null)
				UIUtils.showErrorDialog("There is no selection to save");
			else {
				if (selections.size() == 0)
					UIUtils.showErrorDialog("There is no selection to save");
				else {
					if (selections.size() > 1)
						UIUtils
								.showErrorDialog("Saving multiple selections is not supported. "
										+ "Only the first of your "
										+ selections.size()
										+ " selections will be saved.");

					DataSelection selection = selections.get(0);

					String path = FileUtils.promptUserToSaveFile(
							JyVisSettings.defaultDataDirectory,
							"Comma Separated Value data", "csv");

					if (path != null)
						CSVFileUtils.writeFile(new File(path), selection,
								selectedDataTable.dimensions);
				}
			}
		}
	}

	/**
	 * Prompts the user to export the current selection as a csv file
	 */
	public static void promptUserToExportImageAsPNG() {

		Component selectedPanel = baseEnvironment.selectedPanel;
		if (selectedPanel == null)
			UIUtils.showErrorDialog("There is no window selected. "
					+ "Select the window containing the image, then export.");
		else {
			String path = FileUtils.promptUserToSaveFile(
					JyVisSettings.defaultDataDirectory, "PNG Image", "png");

			if (path != null) {
				Dimension oldSize = selectedPanel.getSize();
				int width = oldSize.width;
				int height = oldSize.height;

				if (selectedPanel instanceof DrawingPanel) {
					width = Integer.parseInt(JOptionPane.showInputDialog(
							baseEnvironment.baseWindow,
							"Enter the width of the output image in pixels.",
							"" + width));
					height = (int) (width * (double) oldSize.height / oldSize.width);
				}

				BufferedImage bufferedImage = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);

				Graphics2D g = bufferedImage.createGraphics();

				selectedPanel.setSize(width, height);
				selectedPanel.paint(g);
				selectedPanel.setSize(oldSize);
				try {
					ImageIO.write(bufferedImage, "png", new File(path));
					JOptionPane.showMessageDialog(baseEnvironment.baseWindow,
							"Successfully wrote the file " + path);
				} catch (IOException e) {
					UIUtils
							.showErrorDialog("An Error occurred while writing the PNG file");
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Replay the specified session file.
	 * 
	 * @param sessionFilePath
	 *            the path of the session file to replay.
	 */
	@SuppressWarnings("unchecked")
	public static void replaySession(String sessionFilePath) {
		if (sessionFilePath != null) {
			ReplayUI replayUI = new ReplayUI((List<SessionEntry>) XMLFileIO
					.readFromFile(new File(sessionFilePath)));
			replayUI.setAlwaysOnTop(true);
		}
	}

	/**
	 * Gets the currently selected data table. If there is no data table
	 * selected, the user is prompted to open one. If the user cancels this
	 * operation, then this method returns null.
	 * 
	 * @return the currently selected data table, or null if there is none.
	 */
	public static DataTable getSelectedData() {
		if (baseEnvironment.selectedDataTable == null)
			promptUserToOpenDataTable();
		return baseEnvironment.selectedDataTable;
	}

	/**
	 * Opens the specified CSV file as a data table, and sets it as the
	 * currently selected one (the one which will be returned from the
	 * getSelectedData() method).
	 * 
	 * @param path
	 *            the path of the CSV file to open
	 */
	public static void openDataTable(String path) {
		baseEnvironment.dataTables
				.add(baseEnvironment.selectedDataTable = CSVFileUtils
						.readFile(new File(path)));
	}

	/**
	 * Gets the global base window for JyVis, or creates it if it doesn't yet
	 * exist. The bounds are set automagically, but setInternalVisible() must be
	 * called to show it
	 * 
	 * @return the base window
	 */
	public static JFrame getBaseWindow() {
		if (baseEnvironment.baseWindow == null)
			baseEnvironment.createBaseWindow();
		return baseEnvironment.baseWindow;
	}

	/**
	 * If the base desktop does not exist (if getBaseWindow() has never been
	 * called), this method returns the size of the actual screen. If the base
	 * desktop does exist (if getBaseWindow() has been called), this method
	 * returns the size of the base desktop, which contains the visualization
	 * windows.
	 * 
	 * @return the size of the screen, or the size of the baseDesktop if it
	 *         exists.
	 */
	public static Dimension getScreenSize() {
		return baseEnvironment.baseWindow == null ? Toolkit.getDefaultToolkit()
				.getScreenSize() : baseEnvironment.baseWindow.getSize();
		// TODO baseWindow.getInternalState().getSize();
	}

	/**
	 * Gets the list of open data tables
	 * 
	 */
	public static List<DataTable> getDataTables() {
		return baseEnvironment.dataTables;
	}

	/**
	 * Creates a menu based on the structure of the specified directory. The
	 * leaf menu items will execute their corresponding Jython script when
	 * clicked.
	 * 
	 * @param directory
	 *            the path of the directory to build the menu from, which should
	 *            only contain directories and Jython scripts.
	 * @return the menu generated from the specified directory. The directory
	 *         tree structure is mirrored by the menu tree structure, and leaf
	 *         menu items execute their corresponding scripts when clicked.
	 */
	public static JMenu createMenuForScriptDirectory(String directory) {
		return createMenuForScriptDirectory(new File(directory));
	}

	/**
	 * Creates a menu based on the structure of the specified directory. The
	 * leaf menu items will execute their corresponding Jython script when
	 * clicked. Menus are only created for .py files, so directories with no .py
	 * files inside them are not made into menus.
	 * 
	 * @param directory
	 *            the directory to build the menu from, which should only
	 *            contain directories and Jython scripts.
	 * @return the menu generated from the specified directory. The directory
	 *         tree structure is mirrored by the menu tree structure, and leaf
	 *         menu items execute their corresponding scripts when clicked. Null
	 *         is retured if the directory contains no .py files
	 */
	public static JMenu createMenuForScriptDirectory(File directory) {
		// get the name of the current directory
		String directoryName = directory.getName();
		// make a menu with the directory name
		JMenu menu = new JMenu(directoryName);
		// set the mnemonic to be the first letter in the name, if possible
		if (directoryName.length() > 0)
			menu.setMnemonic(directoryName.charAt(0));

		boolean directoryContainsScripts = false;

		if (directory.exists()) {
			// get the files in the current directory
			File[] subfiles = directory.listFiles();
			Arrays.sort(subfiles);
			// create menus or menu items for each of the files
			for (int i = 0; i < subfiles.length; i++)
				// if the current file is a directory...
				if (subfiles[i].isDirectory()) {
					// recurse and make a menu for it
					JMenu createdMenu = createMenuForScriptDirectory(subfiles[i]);
					if (createdMenu != null)
						menu.add(createdMenu);
				} else {
					// get the name of the file
					String name = subfiles[i].getName();
					// chop off the file extension
					int l = name.lastIndexOf('.');
					if (l != -1) {
						String ext = name.substring(l + 1).toLowerCase();
						name = name.substring(0, l);

						final String language = ScriptingEngineManager
								.getLanguageForFileExtension(ext);

						if (language != null) {
							directoryContainsScripts = true;
							// System.out.println("ext = '" + ext + "'");

							// create a menu with the cleaned up file name
							JMenuItem m = new JMenuItem(name);
							// set the mnemonic to be the first letter in the
							// name, if possible
							if (name.length() > 0)
								m.setMnemonic(name.charAt(0));
							// make a "final" reference to the file path to it
							// can be used by an anonymous inner action listener
							final String scriptPath = subfiles[i].getPath();
							// set up the action listener for the menu item
							m.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									ScriptBottleneck.execfile(scriptPath);
								}
							});
							// add the menu item to the menu
							menu.add(m);
						}
					}
				}
		} else
			return null;
		return directoryContainsScripts ? menu : null;
	}

	/**
	 * Creates a new window with the specified panel in it. If there is no base
	 * desktop, then an external window is returned with default bounds of 500 X
	 * 500 centered on the screen. If the base desptop exists (if
	 * getBaseWindow() has been called), then an internal frame is created,
	 * added to the base desktop, and returned. If the panel is an instance of
	 * DrawingPanel, then the new window set up with the appropriate listeners
	 * for showing it's property panel in the split pane when the window has
	 * focus
	 * 
	 */
	public static void createWindow(Component panel) {

		if (baseEnvironment.baseDesktop == null)
			if (panel instanceof DrawingPanel)
				((DrawingPanel) panel).showInFrame();
			else {
				Dimension screenSize = Toolkit.getDefaultToolkit()
						.getScreenSize();
				int frameWidth = screenSize.width, frameHeight = screenSize.height;

				JFrame f = new JFrame();
				f.setBounds(screenSize.width / 2 - frameWidth / 2,
						screenSize.height / 2 - frameHeight / 2, frameWidth,
						frameHeight);
				f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				f.add(panel);

				f.setVisible(true);
			}
		else
			baseEnvironment.setUpInternalFrame(panel, null);
	}

	/**
	 * Updates all widgets (JLWidgets) to match their internal state.
	 * 
	 */
	public static void updateAllWidgets() {
		JLPeriodicUpdateThread.updateAllWidgets();
	}

	/**
	 * Effectively resets the state of the system to be the same as it was
	 * before any scripts were executed.
	 * 
	 */
	public static void resetSystemState() {
		// in preparation for executing the script, the effective state must be
		// reset to it's initial state, before anything was executed.
		// get rid of the base window
		baseEnvironment.baseWindow.dispose();
		baseEnvironment.baseWindow = null;
		// get rid of the base desktop
		baseEnvironment.baseDesktop = null;
		// clear dataTables
		baseEnvironment.dataTables.clear();
		// clear all existing widgets
		JLPeriodicUpdateThread.killAllWidgets();
		// clear the global objects
		GlobalObjects.clear();
		// clear the events logged in the current session
		ScriptBottleneck.currentSession.clear();
	}

	/**
	 * Displays the system property panel in the split pane.
	 */
	public static void showSystemPropertyPanel() {
		if (baseEnvironment.systemPropertiesPanel == null)
			baseEnvironment.systemPropertiesPanel = new SystemPropertyPanel();
		JInternalFrame selectedFrame = baseEnvironment.baseDesktop
				.getSelectedFrame();
		if (selectedFrame != null)
			try {
				selectedFrame.setSelected(false);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		baseEnvironment
				.showPropertyPanel(baseEnvironment.systemPropertiesPanel);
	}
	
	
	public static void doRTest(){
		StringBuffer b = new StringBuffer();
		b.append(RInterface.generateDataInputCommand(getSelectedData()));		
		System.out.println(b);
//		double[] result = RInterface.runRScript(b.toString());
//		for(double d:result)
//			System.out.println(d);
	}
}
