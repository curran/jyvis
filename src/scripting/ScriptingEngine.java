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

import java.util.List;

/**
 * The common interface for all scripting engines used by ScriptingEngineManager
 * and ScriptBottleneck
 * 
 * @author Curran Kelleher
 * 
 */
public interface ScriptingEngine {
	/**
	 * Returns the name of the language. For example, "python"
	 * 
	 */
	String getLanguageName();

	/**
	 * Gets the file extension of script files in the language of this engine.
	 * For example, "py"
	 */
	String getFileExtension();

	/**
	 * 
	 * @return the list of the base classes for classes which are functions
	 *         passed in from the language of this engine
	 */
	List<Class<?>> getFunctionBaseClasses();

	/**
	 * Executes the script in this interpreter
	 * 
	 * @param script
	 */
	void exec(String script);

	/**
	 * Imports the specified Java class in the interpreter of the specified
	 * language if the class is not already defined.
	 * 
	 * @param fullPackageName
	 *            the full name of the package containing the class to import.
	 *            For example "javax.swing"
	 * @param className
	 *            the name of the class to import. For example, "JButton"
	 */
	void importIfNotDefined(String fullPackageName, String className);

	/**
	 * Wraps the specified callback function in to a Function object.
	 * 
	 * @param callbackFunction
	 *            the function, as it is when it is passed into Java from the
	 *            scripting language
	 * @return the wrapped function
	 */
	Function wrapFunction(Object callbackFunction);

}
