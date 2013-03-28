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
package jyVis.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ProgressMonitor;
import javax.swing.UIManager;

/**
 * A utility class for reading and writing CVS (Comma Separated Value) data
 * table files.
 * 
 * @author Curran Kelleher
 * 
 */
public class CSVFileUtils {

	/**
	 * Reads the specified CSV file into a DataTable object.
	 * 
	 * @param inputFile
	 *            the CSV file to read
	 * @return a DataTable object which has the contents of the file in it, or
	 *         null if there was an error.
	 */
	public static DataTable readFile(File inputFile) {

		List<List<String>> stringsTable = new ArrayList<List<String>>();

		// Set the title of the dialog if desired
		String title = "Loading File...";
		UIManager.put("ProgressMonitor.progressText", title);
		int min = 0;
		int max = 100;
		String name = inputFile.getName();
		ProgressMonitor pm = new ProgressMonitor(null, "Loading " + name, "",
				min, max);

		pm.setMillisToDecideToPopup(0);
		pm.setMillisToPopup(0);
		pm.setProgress(50);

		try {
			BufferedReader in = new BufferedReader(new FileReader(inputFile));
			String str;
			
			int line = 0;
			while ((str = in.readLine()) != null)
				if (!str.equals("")) {
					// pm.setProgress(Math.min(line/200, max-1));
					pm.setNote("reading line " + line++);

					if (pm.isCanceled())
						return null;
					String[] dimensions = str.split(",");
					List<String> record = new ArrayList<String>();
//					for (int i = 0; i < dimensions.length; i++)
//						record.add(dimensions[i]);
//					
					for(String entry:dimensions){
						entry = entry.trim();
						if(entry.startsWith("\"") && entry.endsWith("\""))
							entry = entry.substring(1, entry.length()-1);
			
						record.add(entry);
					}
					
					
					
					stringsTable.add(record);
				}
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
			pm.setProgress(max);
			return null;
		}
		pm.setProgress(max);
		// this constructor fully creates the dataTable from the table of
		// strings
		if (name.contains("."))
			name = name.substring(0, name.lastIndexOf('.'));
		return new DataTable(stringsTable, name);
	}

	/**
	 * 
	 * Reads the specified CSV file into a DataTable object.
	 * 
	 * @param filePath
	 *            the path of the CSV file to read
	 * @return a DataTable object which has the contents of the file in it, or
	 *         null if there was an error.
	 */
	public static DataTable readFile(String filePath) {
		return readFile(new File(filePath));
	}

	/**
	 * Writes a csv file containing the specified records.
	 * 
	 * @param outputFile
	 *            the file to write to
	 * @param records
	 *            the records to include in the file
	 * @param dimensionNames
	 *            the list from which dimension names are extracted
	 */
	public static void writeFile(File outputFile, List<DataRecord> records,
			List<DataDimensionMetadata> dimensionNames) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));

			int numDims = dimensionNames.size();
			for (int i = 0; i < numDims; i++)
				out.write(dimensionNames.get(i).name
						+ (i < (numDims - 1) ? "," : ""));
			out.write('\n');
			for (DataRecord record : records)
				for (int i = 0; i < numDims; i++)
					out.write(record.get(i).toString()
							+ (i < (numDims - 1) ? "," : "\n"));

			out.close();
		} catch (IOException e) {
		}
	}
}
/*
 * CVS Log
 * 
 * $Log: CSVFileUtils.java,v $
 * Revision 1.3  2008/12/18 23:59:32  curran
 * Added a correlation matrix tool, and completed R integration.
 *
 * Revision 1.2  2008/12/11 21:29:40  curran
 * Started adding support for R script integration through RServe
 *
 * Revision 1.1  2007/08/15 17:59:11  curran
 * Initial commit to SourceForge
 * Revision 1.1 2007/08/02 14:44:20 ckellehe Added
 * selection export Revision 1.1 2007/07/26 00:30:58 ckellehe Initial Creation
 * 
 * 
 */
