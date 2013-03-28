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
from jyVis import JyVis
from jyVis.graphics import DrawingPanel
from jyVis.widgets import JLCheckBox, JLTextField
from jyVis.visualizationPrimitives import Line, TextLabel
from javax.swing import JPanel, JLabel

class Test(DrawingPanel):
	def __init__(self):
		self.line = Line(0,0,1,1)
		self.add(self.line)
		
		self.textLabel = TextLabel("Hello",.5,.8)
		self.add(self.textLabel);
		
		self.propertyPanel = self.createPropertyPanel()
		
	def createPropertyPanel(self):
		panel = JPanel()
		panel.add(JLabel("Yo"))
		panel.add(JLCheckBox("Show Line",self.getShowLine,self.setShowLine));
		panel.add(JLTextField(self.getLabelText,self.setLabelText));
		
		
		return panel
		
	def getShowLine(self): return self.line.fill
	def setShowLine(self, showLine):
		self.line.fill = showLine
		self.updateDisplay()
		
	def getLabelText(self): return self.textLabel.text
	def setLabelText(self, text):
		self.textLabel.text = text
		self.updateDisplay()
	
		
JyVis.createWindow(Test())
