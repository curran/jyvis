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

public class JavaFunction implements Function {
	Object object;

	String methodName;

	Class<?> parameterType;

	public JavaFunction(Object object, String methodName) {
		this.object = object;
		this.methodName = methodName;
		parameterType = getParameterType(object, methodName);
	}

	public Object call(Object arg) {

		Object[] args = arg == null ? new Object[0] : new Object[] { Coercer
				.getObjectAsType(arg, parameterType) };
		Expression expression = new Expression(object, methodName, args);

		try {
			expression.execute();
			return expression.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private Class<?> getParameterType(final Object bean,
			final String propertyName) {
		Method[] methods = bean.getClass().getMethods();
		for (int i = 0; i < methods.length; i++)
			if (methods[i].getName().equals("set" + propertyName))
				return methods[i].getParameterTypes()[0];
		return null;
	}

}
