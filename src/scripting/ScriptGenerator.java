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

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import listEditor.ListState;

import shapeMap.Shape;
import shapeMap.ShapeMap;

import colorMap.ColorMap;
import colorMap.ColorNode;

/**
 * Generates Python code which recreates a copy of a given object (if that
 * object's type is supported) when executed.
 * 
 * @author Curran Kelleher
 * 
 */
public class ScriptGenerator {
	private static ScriptGenerator INSTANCE;

	Map<Class<?>, ScriptGeneratorImplementation> generators;

	public ScriptGenerator() {
		generators = new HashMap<Class<?>, ScriptGeneratorImplementation>();
		generators.put(Integer.class, new ScriptGeneratorImplementation() {
			public String generateCode(Object o) {
				return ((Integer) o).toString();
			}
		});

		generators.put(Boolean.class, new ScriptGeneratorImplementation() {
			public String generateCode(Object o) {
				return "" + (((Boolean) o) ? 1 : 0);
			}
		});

		generators.put(String.class, new ScriptGeneratorImplementation() {
			public String generateCode(Object o) {
				return "\"" + o.toString() + "\"";
			}
		});

		generators.put(Color.class, new ScriptGeneratorImplementation() {
			public void importStuffIfNeeded() {
				ScriptBottleneck.importIfNotDefined("python", Color.class);
			}

			public String generateCode(Object o) {
				Color c = (Color) o;
				return "Color(" + c.getRed() + "," + c.getGreen() + ","
						+ c.getBlue() + ")";
			}
		});

		generators.put(ColorMap.class, new ScriptGeneratorImplementation() {
			public void importStuffIfNeeded() {
				ScriptBottleneck.importIfNotDefined("python", ColorMap.class);
				ScriptBottleneck.importIfNotDefined("python", ColorNode.class);
				ScriptBottleneck.importIfNotDefined("python", Color.class);
			}

			public String generateCode(Object o) {
				StringBuffer b = new StringBuffer();
				b.append("ColorMap([");
				ColorMap colorMap = (ColorMap) o;
				if (!colorMap.isDiscrete()) {
					List<ColorNode> colorNodes = colorMap.getColorNodes();
					for (int i = 0; i < colorNodes.size(); i++) {
						ColorNode colorNode = colorNodes.get(i);
						Color c = colorNode.color;
						b.append("ColorNode(Color(" + c.getRed() + ","
								+ c.getGreen() + "," + c.getBlue() + "),"
								+ colorNode.value + ")");
						if (i != colorNodes.size() - 1)
							b.append(",");
					}
				} else {
					List<ColorNode> colorNodes = colorMap.getColorNodes();
					for (int i = 0; i < colorNodes.size(); i++) {
						Color c = colorNodes.get(i).color;
						b.append("Color(" + c.getRed() + "," + c.getGreen()
								+ "," + c.getBlue() + ")");
						if (i != colorNodes.size() - 1)
							b.append(",");
					}

				}
				b.append("])");
				return b.toString();
			}
		});

		generators.put(ShapeMap.class, new ScriptGeneratorImplementation() {
			public void importStuffIfNeeded() {
				ScriptBottleneck.importIfNotDefined("python", ShapeMap.class);
				ScriptBottleneck.importIfNotDefined("python", Shape.class);
			}

			public String generateCode(Object o) {
				ShapeMap shapeMap = (ShapeMap) o;
				StringBuffer b = new StringBuffer();
				b.append("ShapeMap([");
				List<Shape> shapes = shapeMap.getShapes();
				for (int i = 0; i < shapes.size(); i++) {
					Shape shape = shapes.get(i);
					b.append("Shape([");

					int n = shape.xPoints.length;
					for (int j = 0; j < n; j++) {
						b.append(shape.xPoints[j]);
						if (j != n - 1)
							b.append(",");
					}
					b.append("],[");
					for (int j = 0; j < n; j++) {
						b.append(shape.yPoints[j]);
						if (j != shape.yPoints.length - 1)
							b.append(",");
					}
					b.append("])");

					if (i != shapes.size() - 1)
						b.append(",");
				}
				b.append("])");
				return b.toString();
			}
		});

		generators.put(ListState.class, new ScriptGeneratorImplementation() {
			public void importStuffIfNeeded() {
				ScriptBottleneck.importIfNotDefined("python", ListState.class);
			}

			public String generateCode(Object o) {
				return o.toString();
			}
		});
	}

	public static ScriptGenerator getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ScriptGenerator();
		return INSTANCE;
	}

	/**
	 * Generates Python (Jython) code which reconstructs the specified object
	 * when executed. If the generated code depends on classes which have not
	 * been imported, then the required classes are imported (by executing
	 * scripts through the script bottleneck) at the time this method is called.
	 * 
	 * @param o
	 *            the object for which re-construction code is generated
	 */
	public static String generateCode(Object o) {
		ScriptGeneratorImplementation generator = getInstance().generators
				.get(o.getClass());
		if (generator == null)
			throw new IllegalArgumentException("The class "
					+ o.getClass().getName()
					+ " is not supported by the script generator.");
		generator.importStuffIfNeeded();
		return generator.generateCode(o);
	}

	abstract class ScriptGeneratorImplementation {
		abstract String generateCode(Object o);

		void importStuffIfNeeded() {
		}
	}
}
