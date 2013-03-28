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
package examples;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.python.util.PythonInterpreter;

/**
 * A simple benchmark timing interpretation and evaluation of a function call
 * 
 * JavaScript: 0.876 Jython: 2.0 Groovy: 21.844
 * 
 * 
 * @author Curran Kelleher
 * 
 */
public class Benchmarks {
	static int n = 500;

	public static void main(String[] args) {
		benchmarkJavaScript();
		benchmarkJython();
		// benchmarkGroovy();
	}

	public static void benchmarkJavaScript() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		try {
			long start = System.currentTimeMillis();
			for (int i = 0; i < n; i++)
				engine.eval("Packages.benchmarks.Benchmarks.foo(5)");
			long end = System.currentTimeMillis();
			System.out.println("JavaScript: " + (double) (end - start) / n);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	private static void benchmarkJython() {
		PythonInterpreter engine = new PythonInterpreter();
		engine.exec("from benchmarks import Benchmarks");
		long start = System.currentTimeMillis();
		for (int i = 0; i < n; i++)
			engine.exec("Benchmarks.foo(5)");
		long end = System.currentTimeMillis();
		System.out.println("Jython: " + (double) (end - start) / n);
	}

	//
	// private static void benchmarkGroovy() {
	// GroovyShell engine = new GroovyShell();
	// long start = System.currentTimeMillis();
	// for (int i = 0; i < n; i++)
	// engine.evaluate("benchmarks.Benchmarks.foo(5)");
	// long end = System.currentTimeMillis();
	// System.out.println("Groovy: " + (double) (end - start) / n);
	// }

	public static int foo(int n) {
		return n + 1;
		// System.out.println("foo");
	}
}
