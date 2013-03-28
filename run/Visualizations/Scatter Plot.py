from jyVis import JyVis
from tools.scatterplot import ScatterPlot
data = JyVis.getSelectedData()
if(data != None):
	JyVis.createWindow(ScatterPlot(data))