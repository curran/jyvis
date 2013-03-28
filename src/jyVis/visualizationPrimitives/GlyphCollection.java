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
package jyVis.visualizationPrimitives;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jyVis.data.DataColorMap;
import jyVis.data.DataRecord;
import jyVis.data.Normalization;
import jyVis.graphics.DrawingPanel;
import shapeMap.ShapeMap;
import transformation.Interval;
import transformation.Transformation;
import colorMap.ColorMap;

/**
 * A class which manages the color, shape, and size aspects of a collection of
 * glyphs which correspond to records in a DataTable.
 * 
 * @author Curran Kelleher
 * 
 */
public class GlyphCollection implements Iterable<Glyph> {

	/**
	 * The drawing panel that the glyphs are drawn on
	 */
	public DrawingPanel panel;

	/**
	 * The list of glyphs that this class is managing.
	 */
	List<Glyph> glyphs;

	/**
	 * The object which manages color mapping
	 */
	DataColorMap dataColorMap;

	/**
	 * The shape map used to assign colors to the glyphs in this glyphCollection
	 */
	ShapeMap shapeMap = ShapeMap.getDefaultShapeMap();

	/**
	 * The normalization used for calculating the input values to the shape map
	 */
	Normalization shapeNormalization;

	/**
	 * The index of the dimension which will determine the shape. If this is
	 * assigned directly, updateGlyphs() must be called to see the updates on
	 * screen
	 */
	public int shapeDimension = 0;

	/**
	 * The transformation which stores the size interval for the glyphs
	 */
	public Transformation sizeTransformation = new Transformation(new Interval(
			0.003, 0.02));

	/**
	 * The normalization used for calculating sizes
	 */
	Normalization sizeNormalization;

	/**
	 * The index of the dimension which will determine the size. If this is
	 * assigned directly, updateGlyphs() must be called to see the updates on
	 * screen
	 */
	public int sizeDimension = 0;

	/**
	 * The flag determining whether or not the glyphs will be outlined
	 */
	public boolean drawOutlines = false;

	/**
	 * Constructs a collection of glyphs. When this constructor is invoked, new
	 * JVGlyph objects are created for every record in the data table inside of
	 * the panel, set up for selection, and added to the panel.
	 * 
	 */
	public GlyphCollection(DrawingPanel panel) {
		// store the reference to the panel
		this.panel = panel;

		dataColorMap = new DataColorMap(panel.data);

		// create the default normalization for the shape map
		shapeNormalization = new Normalization(panel.data);

		// create the default normalization for the shape map
		sizeNormalization = new Normalization(panel.data);

		// create a glyph for every data record and set them up for selection.
		glyphs = new ArrayList<Glyph>(panel.data.records.size());
		for (DataRecord record : panel.data.records) {
			Glyph glyph = new Glyph();
			glyph.setUpForSelection(record, panel);
			glyphs.add(glyph);
			panel.add(glyph);
		}

		// initialize the glyph properties
		updateGlyphs(true, true, true, true, false);

	}

	/**
	 * 
	 * @return the list of glyphs that this class is managing.
	 */
	public List<Glyph> getGlyphs() {
		return glyphs;
	}

	/**
	 * Updates all properties of the glyphs.
	 * 
	 */
	public void updateGlyphs() {
		updateGlyphs(true, true, true, true);
	}

	/**
	 * Sets the index of the dimension which will determine the colors of the
	 * glyphs, calculates the new colors, and updates the display.
	 * 
	 * @param dimension
	 */
	public void setColorDimension(int dimension) {
		if (dataColorMap.dimension != dimension) {
			dataColorMap.setDimension(dimension);
			updateGlyphs(true, false, false, false);
		}
	}

	/**
	 * Gets the index of the dimension which will determine the colors of the
	 * glyphs
	 * 
	 */
	public int getColorDimension() {
		return dataColorMap.getDimension();
	}

	/**
	 * Gets the index of the dimension which will determine the shapes of the
	 * glyphs
	 */
	public int getShapeDimension() {
		return shapeDimension;
	}

	/**
	 * Gets the index of the dimension which will determine the sizes of the
	 * glyphs
	 */
	public int getSizeDimension() {
		return sizeDimension;
	}

	/**
	 * Sets the index of the dimension which will determine the shapes of the
	 * glyphs, calculates the new shapes, and updates the display.
	 * 
	 * @param shapeDimension
	 */
	public void setShapeDimension(int shapeDimension) {
		if (this.shapeDimension != shapeDimension) {
			this.shapeDimension = shapeDimension;
			updateGlyphs(false, true, false, false);
		}
	}

	/**
	 * Sets the index of the dimension which will determine the sizes of the
	 * glyphs, calculates the new sizes, and updates the display.
	 * 
	 * @param sizeDimension
	 */
	public void setSizeDimension(int sizeDimension) {
		if (this.sizeDimension != sizeDimension) {
			this.sizeDimension = sizeDimension;
			updateGlyphs(false, false, true, false);
		}
	}

	/**
	 * Sets the color map of this glyph collection to be the specified color
	 * map, then updates the drawing panel.
	 * 
	 * @param colorMap
	 */
	public void setColorMap(ColorMap colorMap) {
		dataColorMap.colorMap = colorMap;
		updateGlyphs(true, false, false, false);
	}

	/**
	 * Gets the color map of this glyph collection.
	 */
	public ColorMap getColorMap() {
		return dataColorMap.colorMap;
	}

	/**
	 * Sets the shape map of this glyph collection to be the specified shape
	 * map, then updates the drawing panel.
	 * 
	 * @param shapeMap
	 */
	public void setShapeMap(ShapeMap shapeMap) {
		this.shapeMap = shapeMap;
		updateGlyphs(false, true, false, false);
	}

	/**
	 * Gets the shape map of this glyph collection.
	 */
	public ShapeMap getShapeMap() {
		return shapeMap;
	}

	/**
	 * Sets the flag for drawing outlines on all glyphs in this collection, then
	 * updates the display
	 * 
	 * @param drawOutlines
	 *            whether or not outlines should be drawn on the glyphs
	 */
	public void setDrawOutlines(boolean drawOutlines) {
		this.drawOutlines = drawOutlines;
		updateGlyphs(false, false, false, true);
	}

	/**
	 * Gets the flag for drawing outlines on all glyphs in this collection
	 * 
	 */
	public boolean getDrawOutlines() {
		return drawOutlines;
	}

	/**
	 * Iterates through all glyphs, updating the properties specified, then
	 * redisplays the drawing panel.
	 * 
	 * @param updateColors
	 *            if true, the colors of all shapes will be updated
	 * @param updateShapes
	 *            if true, the shapes of all shapes will be updated
	 * @param updateSize
	 *            if true, the sizes of all shapes will be updated
	 * @param updateOutlines
	 *            if true, the outlines of all shapes will be updated
	 * 
	 */
	public void updateGlyphs(boolean updateColors, boolean updateShapes,
			boolean updateSize, boolean updateOutlines) {
		updateGlyphs(updateColors, updateShapes, updateSize, updateOutlines,
				true);
	}

	/**
	 * Iterates through all glyphs, updating the properties specified, then
	 * redisplays the drawing panel.
	 * 
	 * @param updateColors
	 *            if true, the colors of all shapes will be updated
	 * @param updateShapes
	 *            if true, the shapes of all shapes will be updated
	 * @param updateSize
	 *            if true, the sizes of all shapes will be updated
	 * @param updateOutlines
	 *            if true, the outlines of all shapes will be updated
	 * 
	 */
	public void updateGlyphs(boolean updateColors, boolean updateShapes,
			boolean updateSize, boolean updateOutlines, boolean updateDisplay) {
		for (Glyph g : glyphs) {
			int record = g.getAssociatedRecord().index;
			if (updateColors)
				g.color = dataColorMap.getColor(record);
			if (updateShapes)
				g.setShape(shapeMap.getShape(shapeNormalization.normalize(
						record, shapeDimension)));
			if (updateSize)
				g.size = sizeTransformation.transform(sizeNormalization
						.normalize(record, sizeDimension));
			if (updateOutlines)
				g.drawOutline = drawOutlines;
		}

		if (updateDisplay)
			panel.updateDisplay();
	}

	/**
	 * Returns the iterator for iterating over the list of glyphs in this glyph
	 * collection
	 */
	public Iterator<Glyph> iterator() {
		return glyphs.iterator();
	}

	public void setSizeMax(double sizeMax) {
		sizeTransformation.range.max = sizeMax;
		updateGlyphs(false, false, true, false);
	}

	public void setSizeMin(double sizeMin) {
		sizeTransformation.range.min = sizeMin;
		updateGlyphs(false, false, true, false);
	}

	public double getSizeMax() {
		return sizeTransformation.range.max;
	}

	public double getSizeMin() {
		return sizeTransformation.range.min;
	}
}
