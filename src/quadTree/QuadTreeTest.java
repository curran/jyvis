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
package quadTree;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * An example of using a QuadTree
 * 
 * @author Curran Kelleher
 * 
 */
public class QuadTreeTest {
	public static void main(String[] args) {

		DrawingPanel p = new DrawingPanel();
		int width = 500;
		for (int i = 0; i < 10000; i++)
			p.addCircle(new Circle((int) (Math.random() * width), (int) (Math
					.random() * width), 2));

		// test the case of many overlapping points
		p.addCircle(new Circle(5, 5, 2));
		p.addCircle(new Circle(5, 5, 2));
		p.addCircle(new Circle(5, 5, 2));
		p.addCircle(new Circle(5, 5, 2));
		p.addCircle(new Circle(5, 5, 2));
		p.addCircle(new Circle(5, 5, 2));
		p.addCircle(new Circle(5, 5, 2));

		// test the case of a point outside the quadtree
		p.addCircle(new Circle(width + 5, width + 5, 2));

		JFrame f = new JFrame();
		f.setBounds(200, 200, width + 30, width + 40);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.getContentPane().add(p);
		f.setVisible(true);
	}
}

class Circle {
	int r;

	Point center;

	public Circle(int x, int y, int r) {
		center = new Point(x, y);
		this.r = r;
	}

	public void paint(Graphics g) {
		g.fillOval(center.x - r, center.y - r, 2 * r, 2 * r);
	}
}

@SuppressWarnings("serial")
class DrawingPanel extends JPanel {
	List<Circle> circles = new ArrayList<Circle>();

	List<Point> selectionPoints = new ArrayList<Point>();

	List<Circle> selectedCircles = new ArrayList<Circle>();;

	private boolean selection;

	QuadTree<Circle> q;

	Polygon polygon = new Polygon();

	public DrawingPanel() {
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				selectionPoints.add(e.getPoint());
				repaint();
			}
		});
		addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				selection = true;
				selectionPoints.clear();
				selectionPoints.add(e.getPoint());
			}

			public void mouseReleased(MouseEvent arg0) {
				selection = false;
				repaint();
			}
		});

		q = new QuadTree<Circle>(0, 0, 500, 500);

	}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.white);
		for (Circle c : circles)
			c.paint(g);
		if (selection) {
			int n = selectionPoints.size();
			int[] xs = new int[n];
			int[] ys = new int[n];
			for (int i = 0; i < n; i++) {
				xs[i] = selectionPoints.get(i).x;
				ys[i] = selectionPoints.get(i).y;
			}
			polygon = new Polygon(xs, ys, n);
			g.setColor(Color.green);
			q.resetColors();
			q.performSpatialQuery(polygon, selectedCircles);
			for (Circle c : selectedCircles)
				c.paint(g);

			// paint the quadtree
			q.paint(g);

			g.setColor(Color.white);
			g.drawPolygon(polygon);
		}

	}

	public void addCircle(final Circle circle) {
		circles.add(circle);
		q.put(new PointValuePair<Circle>(circle.center, circle));
	}

}
/*
 * CVS Log
 * 
 * $Log: QuadTreeTest.java,v $
 * Revision 1.1  2007/08/15 17:59:16  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:30:56 ckellehe Initial
 * Creation Revision 1.2 2007/06/08 15:22:08 ckellehe Initial Creation
 * 
 * 
 */
