from jyVis import JyVis
from tools.parallelCoordinates import ParallelCoordinates
data = JyVis.getSelectedData()
if(data != None):
	JyVis.createWindow(ParallelCoordinates(data))