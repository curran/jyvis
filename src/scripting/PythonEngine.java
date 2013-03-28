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

import java.util.LinkedList;
import java.util.List;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class PythonEngine implements ScriptingEngine {

	PythonInterpreter interpreter = new PythonInterpreter();

	public void exec(String script) {
		interpreter.exec(script);
	}

	public String getFileExtension() {
		return "py";
	}

	public List<Class<?>> getFunctionBaseClasses() {
		List<Class<?>> list = new LinkedList<Class<?>>();
		list.add(PyObject.class);
		return list;
	}

	public String getLanguageName() {
		return "python";
	}

	public void importIfNotDefined(String fullPackageName, String className) {
		if (interpreter.get(className) == null)
			ScriptBottleneck.exec(getLanguageName(), "from " + fullPackageName
					+ " import " + className);
	}

	public Function wrapFunction(Object function) {
		final PyObject funktastic = (PyObject) function;
		return new Function() {
			public Object call(Object arg) {
				if (arg == null)
					return funktastic.__call__();
				else
					return funktastic.__call__(Py.java2py(arg));
			}
		};
	}

}
