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
import java.util.Map;

import org.python.core.PyObject;

/**
 * A class which facilitates getting objects into the type you want them in.
 * 
 * @author Curran Kelleher
 * 
 */
public class Coercer {
	/**
	 * The singleton instance of Coercer
	 */
	private static Coercer INSTANCE;

	/**
	 * The map of types (as strings, the string you get when
	 * *.getClass().toString() is called) to coercer implementations which
	 * coerce objects to that type
	 */
	private Map<String, CoercerToType> coercers = new HashMap<String, CoercerToType>();

	private Coercer() {
		CoercerToType booleanCoercer = new CoercerToType() {
			public Object getObjectAsType(Object object) {
				if (object instanceof Boolean)
					return object;
				if (object instanceof Integer)
					return ((Integer) object) != 0;
				return null;
			}
		};
		coercers.put(Boolean.class.toString(), booleanCoercer);
		coercers.put("boolean", booleanCoercer);

		CoercerToType integerCoercer = new CoercerToType() {
			public Object getObjectAsType(Object object) {
				if (object instanceof Integer)
					return object;
				if (object instanceof String)
					return Integer.parseInt((String) object);
				return null;
			}
		};
		coercers.put(Integer.class.toString(), integerCoercer);
		coercers.put("int", integerCoercer);

		CoercerToType doubleCoercer = new CoercerToType() {
			public Object getObjectAsType(Object object) {
				if (object instanceof Double)
					return object;
				if (object instanceof String)
					return Double.parseDouble((String) object);
				return null;
			}
		};
		coercers.put(Double.class.toString(), doubleCoercer);
		coercers.put("double", doubleCoercer);

		coercers.put(String.class.toString(), new CoercerToType() {
			public Object getObjectAsType(Object object) {
				return object.toString();
			}
		});

	}

	private static Coercer getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Coercer();
		return INSTANCE;
	}

	/**
	 * Coerces the specified object into the specified type
	 * 
	 * @param object
	 *            the object to coerce
	 * @param type
	 *            the type to coerce it into
	 */
	@SuppressWarnings("unchecked")
	public static Object getObjectAsType(Object object, Class type) {
		if (object.getClass() == type)
			return object;
		if (object == null)
			return null;
		if (object instanceof PyObject) {
			Object javaObject = ((PyObject) object).__tojava__(type);
			if (javaObject.getClass() == type)
				return javaObject;
		}
		CoercerToType coercerToType = getInstance().coercers.get(type
				.toString());
		if (coercerToType == null)
			throw new IllegalArgumentException("Coercing to the type " + type
					+ " is not supported");
		Object objectAsType = coercerToType.getObjectAsType(object);
		if (objectAsType == null)
			throw new IllegalArgumentException("Coercing to the type " + type
					+ " from the type " + object.getClass()
					+ " is not supported");
		return objectAsType;
	}

	interface CoercerToType {
		Object getObjectAsType(Object object);
	}

}
