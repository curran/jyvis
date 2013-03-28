package tools.experimental.radvizWithAutolayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jyVis.data.DataRecord;

import org.apache.commons.math.stat.inference.TTestImpl;

/**
 * The class responsible for automatic layout of dimensional anchors in RadViz
 * 
 * @author curran
 * 
 */
public class RadVizAnchorLayoutEngine {
	static TTestImpl t = new TTestImpl();

	public static void layoutAnchors(RadViz radViz, int classifierDimensionIndex) {
		// create the list of relevant metadata needed for the algorithm: each
		// dimension has an associated "best class" and t-stat.
		List<RadVizDimensionMetadata> dimensionsMetadata = new ArrayList<RadVizDimensionMetadata>();

		// sort the data into bins - create the list of record classes
		Collection<RecordClass> recordClasses = computeRecordClasses(radViz,
				classifierDimensionIndex);

		// fill the list of relevant metadata
		for (Integer i : radViz.dimensionsListState.includedIndices) {
			String bestClassName = null;
			double bestTStat = Double.MIN_VALUE;
			for (RecordClass r : recordClasses) {
				// compute T-Stat centered on the current record class
				// meaning t = t(records in this class, all other records)
				double[] valuesFromThisClass = getValuesFromClass(radViz, i, r);
				double[] valuesFromAllOtherClasses = getValuesFromAllOtherClasses(
						radViz, i, r, recordClasses);
				double currentTStat = t.t(valuesFromThisClass,
						valuesFromAllOtherClasses);
				if (currentTStat > bestTStat) {
					bestTStat = currentTStat;
					bestClassName = r.className;
				}
			}
			dimensionsMetadata.add(new RadVizDimensionMetadata(i,
					bestClassName, bestTStat));
		}

		// sort the metadata by t-stats
		Collections.sort(dimensionsMetadata,
				new Comparator<RadVizDimensionMetadata>() {
					public int compare(RadVizDimensionMetadata a,
							RadVizDimensionMetadata b) {
						return Double.compare(a.tValue, b.tValue);
					}
				});

		// sort the metadata by best record class
		Collections.sort(dimensionsMetadata,
				new Comparator<RadVizDimensionMetadata>() {
					public int compare(RadVizDimensionMetadata a,
							RadVizDimensionMetadata b) {
						return a.bestClassName
								.compareToIgnoreCase(b.bestClassName);
					}
				});

		// use the sorted dimensions to inform the anchor angles: all anchors
		// spread evenly
		// int n = dimensionsMetadata.size();
		// for (int i = 0; i < n; i++)
		// radViz.anchors[dimensionsMetadata.get(i).dimensionIndex].setAngle(2
		// * (double) i / n * Math.PI);

		// bin out the lists of dimensions for each class, to use for
		// calculating the angles of the anchors
		List<List<RadVizDimensionMetadata>> dimensionClasses = new LinkedList<List<RadVizDimensionMetadata>>();
		String currentClassName = null;
		List<RadVizDimensionMetadata> currentList = null;
		for (RadVizDimensionMetadata d : dimensionsMetadata) {
			System.out.println("d.bestClassName = "+d.bestClassName);
			if (d.bestClassName.equals(currentClassName))
				currentList.add(d);
			else {
				currentList = new LinkedList<RadVizDimensionMetadata>();
				currentClassName = d.bestClassName;
				currentList.add(d);
				dimensionClasses.add(currentList);
			}
		}

		// use the sorted dimensions to inform the anchor angles: all anchors
		// spread evenly within their class, and all classes spread evenly
		// n = number of classes
		// m = number of dimensions in the current class
		int n = dimensionClasses.size();
		System.out.println("number of classes = "+n);
		for (int i = 0; i < n; i++) {
			List<RadVizDimensionMetadata> currentDimensionClass = dimensionClasses
					.get(i);
			int m = currentDimensionClass.size();
			for (int j = 0; j < m; j++) {
				RadVizDimensionMetadata currentDimension = currentDimensionClass
						.get(j);
				radViz.anchors[currentDimension.dimensionIndex].setAngle(2
						* Math.PI * (((double) i / n)+((double)j/m*1.0/n)));
			}
		}
	}
	

	private static double[] getValuesFromAllOtherClasses(RadViz radViz, int d,
			RecordClass recordClassToExclude,
			Collection<RecordClass> allRecordClasses) {
		List<double[]> valuesList = new LinkedList<double[]>();
		int totalSize = 0;
		for (RecordClass r : allRecordClasses)
			if (!r.equals(recordClassToExclude)) {
				double[] valuesFromClass = getValuesFromClass(radViz, d, r);
				valuesList.add(valuesFromClass);
				totalSize += valuesFromClass.length;
			}
		int i = 0;
		double[] valuesFromAllOtherClasses = new double[totalSize];
		for (double[] values : valuesList)
			for (int j = 0; j < values.length; j++)
				valuesFromAllOtherClasses[i++] = values[j];

		return valuesFromAllOtherClasses;
	}

	private static double[] getValuesFromClass(RadViz radViz, int d,
			RecordClass r) {
		double[] valuesFromThisClass = new double[r.recordIndices.size()];
		for (int i = 0; i < r.recordIndices.size(); i++)
			valuesFromThisClass[i] = radViz.data.get(r.recordIndices.get(i), d);
		return valuesFromThisClass;
	}

	private static Collection<RecordClass> computeRecordClasses(RadViz radViz,
			int classifierDimensionIndex) {
		// create the map of record class names to "record classes" - one record
		// class is the group of records in one class determined by the
		// classifier dimension
		Map<String, RecordClass> recordClassesMap = new HashMap<String, RecordClass>();
		for (DataRecord r : radViz.data.records) {
			String className = r.get(classifierDimensionIndex).stringValue;
			RecordClass recordClass = recordClassesMap.get(className);
			if (recordClass == null)
				recordClassesMap.put(className, recordClass = new RecordClass(
						className));
			recordClass.recordIndices.add(r.index);
		}

		Collection<RecordClass> recordClasses = recordClassesMap.values();
		return recordClasses;
	}

}

class RadVizDimensionMetadata {
	int dimensionIndex;
	String bestClassName;
	double tValue;

	public RadVizDimensionMetadata(int dimensionIndex, String bestClassName,
			double tStat) {
		this.dimensionIndex = dimensionIndex;
		this.bestClassName = bestClassName;
		tValue = tStat;
	}
}

class RecordClass {
	String className;
	List<Integer> recordIndices = new LinkedList<Integer>();

	public RecordClass(String className) {
		this.className = className;
	}
}