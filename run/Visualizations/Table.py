from jyVis import JyVis
from tools import Table
data = JyVis.getSelectedData()
if(data != None):
	JyVis.createWindow(Table(data))