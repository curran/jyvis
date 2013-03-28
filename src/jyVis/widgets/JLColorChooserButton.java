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
package jyVis.widgets;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import scripting.Binding;

/**
 * A colored button which prompts the user to select a color when clicked.
 * 
 * @author Curran Kelleher
 * 
 */
@SuppressWarnings("serial")
public class JLColorChooserButton extends JPanel implements JLWidget<Color>,
		MouseListener {
	/**
	 * The binding to the internal state of the widget
	 */
	Binding binding;

	/**
	 * Constructs a color box which uses a binding created by accessing the
	 * setter and getter functions of the specified property in the specified
	 * Java bean.
	 * 
	 * @param bean
	 * @param property
	 */
	public JLColorChooserButton(Object bean, String property) {
		this(Binding.createBinding(bean, property));
	}

	/**
	 * Constructs a color box which uses a binding created by accessing the
	 * specified setter and getter functions
	 * 
	 * @param getterFunction
	 *            the getter function
	 * @param setterFunction
	 *            the setter function
	 */
	public JLColorChooserButton(Object getterFunction, Object setterFunction) {
		this(Binding.createBinding(getterFunction, setterFunction));
	}

	/**
	 * Construct a color chooser button which is linked to the some internal
	 * state which is accessed through the specified binding.
	 * 
	 * @param binding
	 *            the binding used to access the internal state for this widget
	 */
	public JLColorChooserButton(Binding binding) {
		this.binding = binding;
		addMouseListener(this);
		// set this border so that the button doesn't change size when the
		// border is changed later
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
				getBackground(), getBackground()));
		// set up this widget with the JLWidget periodic check and update
		// framework (periodically, the internal and external states are checked
		// for consistency. when they don't match, the external state (the
		// actual state of the UI) is changed to reflect the internal state).
		JLPeriodicUpdateThread.setupJLWidget(this);
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}

	public void mouseReleased(MouseEvent e) {
		if (contains(e.getPoint())) {
			Color color = JColorChooser.showDialog(this, "Choose Color",
					getBackground());
			setBackground(color);
			setBorder(createBlankBorder());
			if (color != null)
				binding.setInternalState(color);
		}

	}

	private Border createBlankBorder() {
		Color color = getBackground();
		return BorderFactory.createBevelBorder(BevelBorder.RAISED, color,
				color, color, color);
	}

	public void mouseEntered(MouseEvent e) {
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	}

	public void mouseExited(MouseEvent e) {
		setBorder(createBlankBorder());
	}

	public Color getExternalState() {
		return getBackground();
	}

	public Color getInternalState() {
		return (Color) binding.getInternalStateAs(Color.class);
	}

	public void kill() {

	}

	public void setExternalState(Color newValue) {
		setBackground(newValue);
	}
}
/*
 * CVS Log
 * 
 * $Log: JLColorChooserButton.java,v $
 * Revision 1.1  2007/08/15 17:59:14  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/07/26 00:31:00 ckellehe
 * Initial Creation
 * 
 * Revision 1.7 2007/06/19 18:50:49 ckellehe Encapsulated Function objects from
 * scripts such that internal state script generation is independent of the
 * language used to create the function Revision 1.6 2007/06/15 16:27:10
 * ckellehe Fixed sizing bug in color chooser button
 * 
 * Revision 1.5 2007/06/15 15:11:12 ckellehe Added a Color binding requirement
 * for JVScriptingEngine, used it in JLColorChooserButton Revision 1.4
 * 2007/06/14 15:50:56 ckellehe Renamed JythonBottleneck to ScriptBottleneck
 * 
 * Revision 1.3 2007/06/12 18:06:49 ckellehe Fixed bug where the size of the
 * color chooser button shanged when the borders changed. Now it is always the
 * same size
 * 
 * Revision 1.2 2007/06/08 15:01:44 ckellehe Initial Creation
 * 
 */
