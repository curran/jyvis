from jyVis import JyVis
from tools.heatmap import HeatMap
data = JyVis.getSelectedData()
if(data != None):
	JyVis.createWindow(HeatMap(data))