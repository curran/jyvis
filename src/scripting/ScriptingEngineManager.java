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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ScriptingEngineManager {
	/**
	 * The map of language names to scripting engines
	 */
	private static Map<String, ScriptingEngine> engines;

	/**
	 * The map of file extensions to language names
	 */
	private static Map<String, String> fileExtensionsToLanguages;

	/**
	 * Initializes the engines and fileExtensionsToLanguages maps if necessary
	 */
	private static void ensureInitialization() {
		if (engines == null) {
			engines = new HashMap<String, ScriptingEngine>();
			fileExtensionsToLanguages = new HashMap<String, String>();

			List<ScriptingEngine> enginesList = new LinkedList<ScriptingEngine>();
			enginesList.add(new PythonEngine());
			// enginesList.add(new GroovyEngine());
			// enginesList.add(new JavaEngine());

			for (ScriptingEngine engine : enginesList) {
				String language = engine.getLanguageName();
				engines.put(language, engine);
				fileExtensionsToLanguages.put(engine.getFileExtension()
						.toLowerCase(), language);
			}
		}
	}

	/**
	 * Gets the scripting engine with the specified language name
	 * 
	 * @param language
	 */
	public static ScriptingEngine getEngine(String language) {
		ensureInitialization();
		return engines.get(language);
	}

	/**
	 * Gets the scripting engine corresponding to the type of the specified
	 * object, where the object was a function in the scripting language which
	 * was passed to a Java method.
	 * 
	 * @param functionObject
	 */
	public static ScriptingEngine getEngineForFunction(Object functionObject) {
		ensureInitialization();
		for (ScriptingEngine engine : engines.values())
			for (Class<?> functionSuperclass : engine.getFunctionBaseClasses())
				if (functionSuperclass.isAssignableFrom(functionObject
						.getClass()))
					return engine;
		return null;
	}

	/**
	 * Gets the name of the scripting language corresponding to the type of the
	 * specified object, where the object is a function in the scripting
	 * language which was passed to a Java method.
	 * 
	 * @param functionObject
	 */
	public static String getLanguageForFunction(Object functionObject) {
		ensureInitialization();
		return getEngineForFunction(functionObject).getLanguageName();
	}

	/**
	 * Returns the name of the language that corresponds to the specified file
	 * extension
	 * 
	 * @param extension
	 */
	public static String getLanguageForFileExtension(String extension) {
		ensureInitialization();
		return fileExtensionsToLanguages.get(extension);
	}
}
