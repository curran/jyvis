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
package fileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * A class providing convenience methods for dealing with files.
 * 
 * @author Curran Kelleher
 * 
 */
public class FileUtils {

	/**
	 * Prompts the user to open a file.
	 * 
	 * @param initialDirectory
	 *            the path of the initial directory to initialize the file
	 *            chooser with ("data" for example)
	 * @param fileTypeDescription
	 *            the description of the file extension. For example "Comma
	 *            Separated Value", will be turned into "Comma Separated Value
	 *            File (*.csv)" and displayed in the file chooser (if "csv" was
	 *            passed as the extension argument)
	 * @param extension
	 *            the file extension ("csv" for example)
	 * @return the path of the selection file, or null if the user cancelled the
	 *         operation.
	 */
	public static String promptUserToOpenFile(String initialDirectory,
			String fileTypeDescription, String extension) {
		return promptUserToChooseFile(initialDirectory, fileTypeDescription,
				extension, false);
	}

	/**
	 * Prompts the user to open a file.
	 * 
	 * @return the path of the file that the used selected, or null if the user
	 *         canceled the operation.
	 */
	public static String promptUserToOpenFile() {
		return promptUserToChooseFile(null, null, null, false);
	}

	/**
	 * Prompts the user to save a file.
	 * 
	 * @param initialDirectory
	 *            the path of the initial directory to initialize the file
	 *            chooser with ("data" for example)
	 * @param fileTypeDescription
	 *            the description of the file extension. For example "Comma
	 *            Separated Value", will be turned into "Comma Separated Value
	 *            File (*.csv)" and displayed in the file chooser (if "csv" was
	 *            passed as the extension argument)
	 * @param extension
	 *            the file extension ("csv" for example)
	 * @return the path of the file that the used selected, or null if the user
	 *         canceled the operation.
	 */
	public static String promptUserToSaveFile(String initialDirectory,
			String fileTypeDescription, String extension) {
		return promptUserToChooseFile(initialDirectory, fileTypeDescription,
				extension, true);
	}

	/**
	 * Prompts the user to open or save a file.
	 * 
	 * @param initialDirectory
	 *            the path of the initial directory to initialize the file
	 *            chooser with ("data" for example)
	 * @param fileTypeDescription
	 *            the description of the file extension. For example "Comma
	 *            Separated Value", will be turned into "Comma Separated Value
	 *            File (*.csv)" and displayed in the file chooser (if "csv" was
	 *            passed as the extension argument)
	 * @param extension
	 *            the file extension ("csv" for example)
	 * @param saving
	 *            true to show a "save" dialog, which automatically adds the
	 *            file extension if it is not there name, false to show an
	 *            "Open" dialog
	 * @return the path of the selection file, or null if the user cancelled the
	 *         operation.
	 */
	private static String promptUserToChooseFile(String initialDirectory,
			final String fileTypeDescription, final String extension,
			boolean saving) {
		// create a JFileChooser initialized to the initial directory
		JFileChooser chooser = new JFileChooser(initialDirectory);
		// turn off the "all files" file filter
		chooser.setAcceptAllFileFilterUsed(false);
		// set the file filter
		chooser.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				String name = f.getName();
				int i = name.lastIndexOf('.');
				return i == -1 ? false : name.substring(i).toLowerCase()
						.equals("." + extension);
			}

			public String getDescription() {
				return fileTypeDescription + " File (*." + extension + ")";
			}
		});
		// prompt the user to open a file
		if ((saving ? chooser.showSaveDialog(null) : chooser
				.showOpenDialog(null)) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();

			if (saving)
				if (selectedFile.exists())
					if (JOptionPane.showConfirmDialog(null,
							"Overwrite the file " + selectedFile.toString()
									+ "?", "Overwrite?",
							JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
						return null;

			String path = selectedFile.getPath();
			// System.out.println(path);
			StringBuffer oneSlash = new StringBuffer();
			oneSlash.append('\\');
			StringBuffer twoSlash = new StringBuffer();
			twoSlash.append('\\');
			twoSlash.append('\\');
			path = path.replace(oneSlash, twoSlash);

			String name = selectedFile.getName();
			int i = name.lastIndexOf('.');
			if (i == -1 ? false : name.substring(i).toLowerCase().equals(
					"." + extension))
				return path;
			else
				return path + "." + extension;
		} else
			return null;
	}

	/**
	 * Gets the file extension of the specified file, in lower case, excluding
	 * the "."
	 * 
	 * @param file
	 *            the file to get the extension of
	 * @return the file extension (for example "jpg"), or "" if there is no file
	 *         extension
	 */
	public static String getFileExtension(File file) {
		String name = file.getName();
		int i = name.lastIndexOf(".");
		return i == -1 ? "" : name.substring(i + 1).toLowerCase();
	}

	/**
	 * Reads the specified text file
	 * 
	 * @param inputFile
	 *            the file to read
	 * @return the text contents of the file, or null if there was an error
	 */
	public static String readTextFile(File inputFile) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(inputFile));
			StringBuffer b = new StringBuffer();
			String str;
			while ((str = in.readLine()) != null)
				b.append(str + "\n");
			in.close();
			return b.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Reads the specified text file
	 * 
	 * @param inputFilePath
	 *            the path of the file to read
	 * @return the text contents of the file, or null if there was an error
	 */
	public static String readTextFile(String inputFilePath) {
		return inputFilePath != null ? readTextFile(new File(inputFilePath))
				: null;
	}
}
