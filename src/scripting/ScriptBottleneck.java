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
package scripting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import session.SessionEntry;
import fileUtils.FileUtils;

public class ScriptBottleneck {

	public static List<SessionEntry> currentSession = new ArrayList<SessionEntry>();

	public static int selectionEventCompressionKey = -1;

	/**
	 * Executes the specified script in the interpreter of the specified
	 * language. Use this version of exec() to store script events which are
	 * compressible by some unique key (typically an index of something in
	 * GlobalObjects can be used). This means that when the state of the system
	 * is stored, only the last scripts executed with each key are stored. This
	 * should only be used when executing the script overwrites the effect of
	 * the previous script executed with the same key.
	 * 
	 * @param language
	 *            the language specifying the interpreter to use
	 * @param script
	 *            the script to execute
	 * @param eventCompressionKey
	 */
	public static void exec(String language, String script,
			int eventCompressionKey) {
		// TODO implement this
		exec(language, script);
	}

	/**
	 * Executes the specified script in the interpreter of the specified
	 * language
	 * 
	 * @param language
	 *            the language specifying the interpreter to use
	 * @param script
	 *            the script to execute
	 */
	public static void exec(String language, String script) {
		ScriptingEngine engine = ScriptingEngineManager.getEngine(language);
		if (engine != null) {
			try {
				engine.exec(script);
				currentSession.add(new SessionEntry(language, script));
			} catch (Exception e) {
				System.err.println("Error executing " + language + " script");
				e.printStackTrace();
			}
		} else
			throw new IllegalArgumentException("No such language: " + language);
	}

	/**
	 * Executes the file at the specified file path. The interpreter used to
	 * interpret the file depends on the file extension
	 * 
	 * @param path
	 */
	public static void execfile(String path) {
		File file = new File(path);
		String language = ScriptingEngineManager
				.getLanguageForFileExtension(FileUtils.getFileExtension(file));
		String script = FileUtils.readTextFile(file);
		exec(language, script);
	}

	/**
	 * Imports the specified Java class in the interpreter of the specified
	 * language if the class is not already defined.
	 * 
	 * @param language
	 *            the language of the interpreter to import the class in
	 * @param classToImport
	 *            the class to import.
	 */
	public static void importIfNotDefined(String language,
			Class<?> classToImport) {
		ScriptingEngine engine = ScriptingEngineManager.getEngine(language);

		if (engine != null)
			engine.importIfNotDefined(classToImport.getPackage().getName(),
					classToImport.getSimpleName());
		else
			throw new IllegalArgumentException("No such language: " + language);
	}
}
