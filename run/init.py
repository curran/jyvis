from jyVis import JyVis
from jyVis.widgets import JLMenuItem
from javax.swing import JMenuBar, JMenu

baseWindow = JyVis.getBaseWindow()

menuBar = JMenuBar()

fileMenu = JMenu("File")

fileMenu.add(JLMenuItem("Open Dataset",JyVis.promptUserToOpenDataTable))

fileMenu.add(JLMenuItem("Save Session",JyVis.promptUserToSaveSession))

fileMenu.add(JLMenuItem("Load Session",JyVis.promptUserToReplaySession))

fileMenu.add(JLMenuItem("Export Selection as CSV",JyVis.promptUserToExportSelection))

fileMenu.add(JLMenuItem("Export Image as PNG",JyVis.promptUserToExportImageAsPNG))

fileMenu.addSeparator() 
fileMenu.add(JLMenuItem("Properties","JyVis.showSystemPropertyPanel()"))

#TODO remove this
fileMenu.add(JLMenuItem("RTest","JyVis.doRTest()"))

menuBar.add(fileMenu)

# build the Visualizations menu from the contents of the plugin directory
visMenu = JyVis.createMenuForScriptDirectory("run//Visualizations")
if(visMenu != None): menuBar.add(visMenu)

baseWindow.setJMenuBar(menuBar)

baseWindow.setVisible(1)
