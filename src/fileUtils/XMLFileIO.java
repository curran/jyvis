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

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;

/**
 * A utility class for reading and writing objects to and from XML files.
 * 
 * @author Curran Kelleher
 * 
 */
public class XMLFileIO {

	/**
	 * Writes an object to a file. Prompts the user to overwrite if the file
	 * exists already
	 * 
	 * @param object
	 *            the object to encode
	 * @param destination
	 *            the destination file
	 */
	public static boolean writeToFile(Object object, File destination) {
		try {
			XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(destination)));
			e.writeObject(object);
			e.close();
			return true;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "The file " + destination
					+ " does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Reads an object from a file.
	 * 
	 * @param source
	 *            the file to read
	 * @return the object which was encoded into the source file
	 */
	public static Object readFromFile(File source) {
		try {
			XMLDecoder d = new XMLDecoder(new BufferedInputStream(
					new FileInputStream(source)));
			Object result = d.readObject();
			d.close();
			return result;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "The file " + source
					+ " does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}
	}
}
