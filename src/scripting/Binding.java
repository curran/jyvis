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

import java.beans.Expression;
import java.lang.reflect.Method;

import jyVis.GlobalObjects;

/**
 * A class which is used by JLWidget implementations to communicate with their
 * internal state.
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("unchecked")
public class Binding {
	/**
	 * The index of the setter function in GlobalObjects
	 */
	final int setterFunctionIndex;

	/**
	 * A reference to the getter function. The type of this object depends on
	 * the scripting language used to create it.
	 */
	Function getterFunction;

	/**
	 * 
	 * Create a new JLWidgetBinding object for the given parameters
	 * 
	 * @param getterFunction
	 *            the getter function (for the internal state of the widget) in
	 *            the specified language
	 * @param setterFunction
	 *            the setter function (for the internal state of the widget) in
	 *            the specified language
	 */
	public Binding(Function getterFunction, Function setterFunction) {
		super();
		this.setterFunctionIndex = GlobalObjects.add(setterFunction);
		this.getterFunction = getterFunction;
	}

	/**
	 * Sets the internal state to be the same as the external state by executing
	 * a script.
	 * 
	 * @param externalState
	 *            the current external state of the widget
	 */
	public void setInternalState(Object externalState) {
		ScriptBottleneck.importIfNotDefined("python", GlobalObjects.class);
		ScriptBottleneck.exec("python", "GlobalObjects.get("
				+ setterFunctionIndex + ").call("
				+ ScriptGenerator.generateCode(externalState) + ")",
				setterFunctionIndex);
	}

	/**
	 * Gets the internal state by means of evaluating the getter function
	 * 
	 * @return the internal state
	 */
	public Object getInternalState() {
		return getterFunction.call(null);
	}

	/**
	 * Gets the internal state by means of evaluating the getter function
	 * 
	 * @param type
	 *            the type to coerce the result into
	 * @return the internal state, coerced to the specified type
	 */
	public Object getInternalStateAs(Class type) {
		return Coercer.getObjectAsType(getInternalState(), type);
	}

	/**
	 * Generates a binding which uses bean property getter and setter functions
	 * of a Java bean
	 * 
	 * @param bean
	 *            the java object with the getter and setter functions called
	 *            "get"+propertyName and "set"+propertyName.
	 * @param propertyName
	 *            the name of the bean property
	 */
	public static Binding createBinding(final Object bean, String propertyName) {
		final String properPropertyName = Character.toUpperCase(propertyName
				.charAt(0))
				+ propertyName.substring(1);
		return new Binding(new Function() {
			public Object call(Object arg) {
				Expression expr = new Expression(bean, "get"
						+ properPropertyName, new Object[0]);
				try {
					expr.execute();
					return expr.getValue();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Function() {

			public Object call(Object arg) {
				try {
					Class parameterType = getParameterType();
					Object correctlyTypedArg = Coercer.getObjectAsType(arg,
							parameterType);
					new Expression(bean, "set" + properPropertyName,
							new Object[] { correctlyTypedArg }).execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			private Class getParameterType() {
				Method[] methods = bean.getClass().getMethods();
				for (int i = 0; i < methods.length; i++)
					if (methods[i].getName().equals("set" + properPropertyName))
						return methods[i].getParameterTypes()[0];
				return null;
			}
		});
	}

	/**
	 * 
	 * @param getterFunction
	 * @param setterFunction
	 */
	public static Binding createBinding(Object getterFunction,
			Object setterFunction) {
		if (getterFunction instanceof Function
				&& setterFunction instanceof Function)
			return new Binding((Function) getterFunction,
					(Function) setterFunction);

		ScriptingEngine engine = ScriptingEngineManager
				.getEngineForFunction(getterFunction);
		return new Binding(engine.wrapFunction(getterFunction), engine
				.wrapFunction(setterFunction));
	}
}
/*
 * CVS Log
 * 
 * $Log: Binding.java,v $
 * Revision 1.1  2007/08/15 17:59:19  curran
 * Initial commit to SourceForge
 * Revision 1.4 2007/08/01 19:06:42 ckellehe Created
 * system property panel
 * 
 * Revision 1.3 2007/07/30 23:43:04 ckellehe Cleaned up Javadoc
 * 
 * Revision 1.2 2007/07/27 16:28:33 ckellehe Re-wrote RadVis, included dimension
 * list editing Revision 1.1 2007/07/26 00:30:59 ckellehe Initial Creation
 * Revision 1.2 2007/06/19 18:50:49 ckellehe Encapsulated Function objects from
 * scripts such that internal state script generation is independent of the
 * language used to create the function
 * 
 * Revision 1.1 2007/06/14 15:49:55 ckellehe Started implementing
 * JVScriptingEngine
 * 
 * Revision 1.2 2007/06/13 20:35:55 ckellehe Initial Creation
 * 
 * 
 */
