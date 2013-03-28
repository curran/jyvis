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

import java.awt.Graphics;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import jyVis.data.Normalization;
import transformation.Window2D;

/**
 * A generic axis. All fields which define this axis are exposed as public
 * fields. After they are modified, calculateAxis() should be called.
 * 
 * @author Curran Kelleher
 * 
 */
public class Axis extends Line {

	/**
	 * The tick marks
	 */
	List<Line> tickmarks = new LinkedList<Line>();

	/**
	 * The numbers to go along with the tick marks
	 */
	List<TextLabel> numbers = new LinkedList<TextLabel>();

	/**
	 * The number of major tick marks
	 */
	public int numMajorTickMarks = 9;

	/**
	 * The number of minor tick marks between major tick marks
	 */
	public int numMinorTickMarks = 0;

	/**
	 * The length of the major tick marks
	 */
	public double majorTickMarkLength = 0.03;

	/**
	 * The length of the minor tick marks
	 */
	public double minorTickMarkLength = 0.015;

	/**
	 * The offset of the tick marks (their distance away from the axis)
	 */
	public double tickMarkOffset = 0;

	/**
	 * The size of the numbers
	 */
	public double numberSize = 12;

	/**
	 * factor that determines the number spacing relative to the tick mark
	 * spacing
	 */
	public double numSpacingFactor = 1;

	/**
	 * The offset of the numbers (their distance away from the axis)
	 */
	public double numberOffset = 0.025;

	/**
	 * The string used for number formatting (see Javas DecimalFormat)
	 */
	public String numberFormatString = "###.###";

	/**
	 * The normalization used to generate the numbers to display along this axis
	 */
	public Normalization normalization;

	/**
	 * The dimension in the dataset that this axis uses
	 */
	public int dimension = 0;

	/**
	 * The title label for this axis. The x, y, and theta fields of this
	 * JVTextLabel are reset when this axis gets painted, to be in synch with
	 * titleX, titleY, and titleTheta (defined in this axis), therefore
	 * modifying them manually does nothing.
	 */
	public TextLabel label = new TextLabel();

	/**
	 * The position of the title along the X-axis of this axis. In this context,
	 * the X-axis refers to the lengthwise direction: 0 on the X-axis
	 * corresponds to the first point of this axis (x1,y1), 1 on the X-axis
	 * corresponds to the second point of this axis (x2,y2).
	 */
	public double labelX = 0.5;

	/**
	 * The position of the title along the Y-axis of this axis. In this context,
	 * the Y-axis refers to the widthwise direction: 0 on the Y-axis corresponds
	 * to a point on the line of this axis, 1 on the Y-axis corresponds to a
	 * point 1 unit (in the coordinate space) away from the line of this axis.
	 */
	public double labelY = 0.05;

	/**
	 * The rotation in degrees of the label of this axis, in the clockwise
	 * direction, about the point (titleX,titleY). This is 0 by default. This
	 * rotation is in addition to the rotation which aligns the text with the
	 * label, which is always performed.
	 */
	public double labelRotation = 0;

	/**
	 * The rotation of all numbers on this axis
	 */
	public double numbersRotation = 0;

	/**
	 * The x alignment of the numbers - either CENTER, LEFT, or RIGHT
	 */
	public String numbersXAlignment = "CENTER";

	/**
	 * The y alignment of the numbers - either CENTER, BOTTOM, or TOP
	 */
	public String numbersYAlignment = "CENTER";

	/**
	 * A flag indicating whether or not to draw the axis. If this is false, the
	 * axis will not be drawn. (true by default)
	 */
	public boolean visible = true;

	/**
	 * Construct a blank axis
	 */
	public Axis(Normalization normalization) {
		this(0, 0, 0, 0, normalization);
	}

	/**
	 * Construct an axis with the given start and end points
	 * 
	 * @param x1
	 *            the x of the start point
	 * @param y1
	 *            the y of the start point
	 * @param x2
	 *            the x of the end point
	 * @param y2
	 *            the y of the end point
	 */
	public Axis(double x1, double y1, double x2, double y2,
			Normalization normalization) {
		super(x1, y1, x2, y2);
		label.size = 14;
		this.normalization = normalization;
	}

	/**
	 * Calculates all attributes of the axis. This means the tick marks and the
	 * title text label. This method has a side effect on the normalization, it
	 * sets its output range to be (0,1) so the normalization performs correct
	 * inverse calculations.
	 * 
	 */
	public void calculateAxis() {
		double dx = x2 - x1;
		double dy = y2 - y1;
		double d = Math.sqrt(dx * dx + dy * dy);
		double xHat = dx / d;
		double yHat = dy / d;
		double halfMajor = majorTickMarkLength / 2;
		double halfMinor = minorTickMarkLength / 2;
		double percent, x, y, xExpansion, yExpansion;

		// calculate the tick marks
		tickmarks.clear();
		for (float i = 0; i < (numMajorTickMarks - 1) * numMinorTickMarks
				+ numMajorTickMarks; i++) {
			percent = i / (numMajorTickMarks - 1) / (numMinorTickMarks + 1);
			x = percent * dx + x1;
			y = percent * dy + y1;
			x -= tickMarkOffset * yHat;
			y += tickMarkOffset * xHat;
			if (i % (numMinorTickMarks + 1) == 0) {
				xExpansion = yHat * halfMajor;
				yExpansion = xHat * halfMajor;
			} else {
				xExpansion = yHat * halfMinor;
				yExpansion = xHat * halfMinor;
			}
			tickmarks.add(new Line(x - xExpansion, y + yExpansion, x
					+ xExpansion, y - yExpansion));
		}

		// calculate the numbers
		NumberFormat formatter = new DecimalFormat(numberFormatString);
		numbers.clear();

		for (float i = 0; i < numMajorTickMarks; i++) {
			percent = i / (numMajorTickMarks - 1);
			x = percent * dx + x1;
			y = percent * dy * numSpacingFactor + y1 - (numSpacingFactor - 1.0)
					/ 2.0;
			x -= numberOffset * yHat;
			y += numberOffset * xHat;
			// DataTable data;
			// double number = data.dimensions.get(dimension);
			// System.out.println("percent = "+percent);
			double number = normalization.inverseNormalize(percent, dimension);
			TextLabel textLabel = new TextLabel(formatter.format(number), x, y,
					numbersXAlignment, numbersYAlignment, numbersRotation);
			textLabel.size = numberSize;
			numbers.add(textLabel);
		}

		// calculate the position and rotation of the title text label
		label.x = x1 + dx * labelX + yHat * labelY;
		label.y = y1 + dy * labelX - xHat * labelY;
		label.text = normalization.data.dimensions.get(dimension).toString();
	}

	/**
	 * Calculates the pixel-space points that this object will need when drawing
	 * and testing for selections.
	 * 
	 * @param w
	 *            the window to use for coordinate-space to pixel-space
	 *            transformations.
	 */
	public void computeWindowTransformation(Window2D w) {
		super.computeWindowTransformation(w);

	}

	/**
	 * Draws this object, using the transformation information calculated in
	 * computeWindowTransformation().
	 * 
	 * @param g
	 *            the Graphics to draw this object on
	 */
	public void paint(Graphics g, Window2D w) {
		if (visible) {
			// paint the axis line
			super.paint(g, w);

			calculateAxis();
			for (VisualizationPrimitive o : tickmarks)
				o.paint(g, w);
			for (VisualizationPrimitive o : numbers)
				o.paint(g, w);

			// calculate the correct rotation for the text label
			label.rotation = Math.toDegrees(Math.atan2(b.y - a.y, b.x - a.x))
					+ labelRotation;

			// paint the tickmarks
			for (Line o : tickmarks) {
				o.color = this.color;
				o.paint(g, w);
			}

			// paint the numbers
			for (TextLabel o : numbers) {
				o.color = this.color;
				o.paint(g, w);
			}

			// paint the title
			label.color = this.color;
			label.paint(g, w);
		}
	}

	public int getNumMajorTickMarks() {
		return numMajorTickMarks;
	}

	public void setNumMajorTickMarks(int numMajorTickMarks) {
		this.numMajorTickMarks = numMajorTickMarks;
	}

	public int getNumMinorTickMarks() {
		return numMinorTickMarks;
	}

	public void setNumMinorTickMarks(int numMinorTickMarks) {
		this.numMinorTickMarks = numMinorTickMarks;
	}

	public double getLabelX() {
		return labelX;
	}

	public void setLabelX(double labelX) {
		this.labelX = labelX;
	}

	public double getLabelY() {
		return labelY;
	}

	public void setLabelY(double labelY) {
		this.labelY = labelY;
	}

	public String getNumbersXAlignment() {
		return numbersXAlignment;
	}

	public void setNumbersXAlignment(String numbersXAlignment) {
		this.numbersXAlignment = numbersXAlignment;
	}

	public String getNumbersYAlignment() {
		return numbersYAlignment;
	}

	public void setNumbersYAlignment(String numbersYAlignment) {
		this.numbersYAlignment = numbersYAlignment;
	}

	public double getLabelRotation() {
		return labelRotation;
	}

	public void setLabelRotation(double labelRotation) {
		this.labelRotation = labelRotation;
	}

	public double getNumbersRotation() {
		return numbersRotation;
	}

	public void setNumbersRotation(double numbersRotation) {
		this.numbersRotation = numbersRotation;
	}

}
/*
 * CVS Log
 * 
 * $Log: Axis.java,v $
 * Revision 1.1  2007/08/15 17:59:16  curran
 * Initial commit to SourceForge
 * Revision 1.3 2007/07/26 21:20:45 ckellehe Create List
 * editor and integrated it into parallel coordinates Revision 1.2 2007/07/26
 * 19:15:39 rbeaven *** empty log message ***
 * 
 * Revision 1.1 2007/07/26 00:30:59 ckellehe Initial Creation Revision 1.9
 * 2007/07/11 19:25:45 rbeaven merger of window computation and paint
 * 
 * Revision 1.8 2007/06/26 16:24:00 ckellehe Changed the normalization API,
 * updated Groovy scatterplot to ranges are changeable.
 * 
 * Revision 1.7 2007/06/21 17:01:27 ckellehe Updated the scatterplot Groovy
 * script with all the features of the Python scatterplot
 * 
 * Revision 1.6 2007/06/21 16:21:10 ckellehe Fixed bug in scatterplot Python
 * sctipr Revision 1.5 2007/06/21 16:13:07 ckellehe Made some changes to Axis to
 * make it's use more convenient, added axes to the Groovy scatterplot.
 * 
 * Revision 1.4 2007/06/20 18:50:41 rbeaven Implemented Minor and Major Tick
 * Marks in JVAxis Updated Visuals to reflect changes Revision 1.3 2007/06/14
 * 19:58:36 rbeaven Axis for parallel coords changed Revision 1.2 2007/06/08
 * 15:01:44 ckellehe Initial Creation
 * 
 */
