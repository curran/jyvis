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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import transformation.Window2D;

public class Image extends VisualizationPrimitive {
	BufferedImage image;

	public Image(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, width, height);
	}

	public void paint(Graphics g, Window2D w) {
		g.drawImage(image, 0, 0, null);
	}

	public void fillPixel(Point p, Color color) {
		fillPixel(p.x, p.y, color);
	}

	public void fillPixel(int x, int y, Color color) {
		if (x < image.getWidth() && x > 0 && y < image.getHeight() && y > 0)
			image.setRGB(x, y, color.getRGB());
	}

	public Rectangle getBoundingBox() {
		return new Rectangle(0, 0, image.getWidth(), image.getHeight());
	}
}
