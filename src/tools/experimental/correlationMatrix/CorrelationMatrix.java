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
package tools.experimental.correlationMatrix;

import jyVis.JyVis;
import jyVis.data.DataTable;
import jyVis.graphics.DrawingPanel;
import jyVis.visualizationPrimitives.JVMouseAdapter;
import jyVis.visualizationPrimitives.Rectangle;
import jyVis.visualizationPrimitives.TextLabel;
import rScripts.RInterface;
import tools.scatterplot.ScatterPlot;
import fileUtils.FileUtils;

/**
 * A correlation matrix visualization tool
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class CorrelationMatrix extends DrawingPanel {
	/**
	 * title text displayed in the plot
	 */
	TextLabel title = new TextLabel(data.getName(), 0.5, 0.96);

	/**
	 * public constructor
	 * 
	 * @param data
	 *            reference to the associated data table
	 */
	public CorrelationMatrix(final DataTable data) {
		super(data);

		double[][] c = computeCorrelationMatrix(data);

		int m = data.dimensions.size();
		for (int y = 0; y < m; y++) {
			for (int x = 0; x < m; x++) {
				double x1 = (double) x / m;
				double y1 = (double) y / m;
				double x2 = (double) (x + 1) / m;
				double y2 = (double) (y + 1) / m;
				Rectangle r = new Rectangle(x1, y1, x2, y2);
				final int xDim = x;
				final int yDim = y;
				
				r.addJVMouseListener(new JVMouseAdapter() {
					public void mousePressed(double x, double y) {
						ScatterPlot plot = new ScatterPlot(data);
						plot.bean.setXDimension(xDim);
						plot.bean.setYDimension(yDim);
						JyVis.createWindow(plot);
					}
				});
				double color = c[y][x] / 2 + 0.5;
				r.setColor(color, color, color);
				add(r);
				System.out.print(color + " ");
			}
			System.out.println();
		}

		add(title);
	}

	/**
	 * Computes the correlation matrix (the Pearson correlation for each unique
	 * pair of dimensions) using R
	 * 
	 * @param data
	 *            the data set to compute the correlation matrix on
	 * @return a flat representation of the m-by-m matrix of correlations (with
	 *         only the upper triangle filled)
	 */
	private double[][] computeCorrelationMatrix(DataTable data) {
		String rScript = FileUtils
				.readTextFile("run/Visualizations/Experimental/CorrelationMatrix.R");
		double[] result = RInterface.runRScript(rScript, data);
		int m = data.dimensions.size();
		if (result.length != m * m)
			throw (new RuntimeException(
					"Size of returned result from R is wrong."));
		double[][] c = new double[m][m];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < m; j++)
				c[i][j] = result[i + j * m];
		return c;
	}
}
