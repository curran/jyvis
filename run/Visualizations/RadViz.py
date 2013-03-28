from jyVis import JyVis
from tools.radviz import RadViz
data = JyVis.getSelectedData()
if(data != None):
	JyVis.createWindow(RadViz(data))