package rScripts;

import jyVis.data.DataEntry;
import jyVis.data.DataRecord;
import jyVis.data.DataTable;

import org.rosuda.JRclient.RSrvException;
import org.rosuda.JRclient.Rconnection;

/**
 * A class providing an access point to R.
 * 
 * @author curran
 * 
 */
public class RInterface {

	public static double[] runRScript(String script, DataTable data) {
		System.out.println(generateDataInputCommand(data) + "\n" + script);
		return runRScript(generateDataInputCommand(data) + "\n" + script);
	}

	public static double[] runRScript(String script) {
		Rconnection c = getLocalRconnection();
		if (c != null) {
			try {
				return c.eval(script).asDoubleArray();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static Rconnection getLocalRconnection() {
		try {
			return new Rconnection();
		} catch (RSrvException e) {
			// If we get this kind of error, it may be because Rserve is not
			// running
			if (e.getMessage().equals("Cannot connect: Connection refused")) {
				try {
					String rserveStartCommand = "R CMD Rserve --vanilla";
					// do a blocking call to the shell command for starting
					// Rserve
					int exitValue = Runtime.getRuntime().exec(
							rserveStartCommand).waitFor();
					// if it returned success, try connecting again
					if (exitValue == 0)
						return new Rconnection();
					// otherwise, Rserve is probably not installed
					else
						System.err
								.println("Could not start Rserve - is it installed properly? Shell command \""
										+ rserveStartCommand
										+ "\" exited with exit value "
										+ exitValue + ".");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else
				e.printStackTrace();
		}
		return null;
	}

	/**
	 * Generates an R expression which assigns all data in the specified data
	 * table to a matrix in R called "data"
	 * 
	 * @param data
	 *            the data set to express in R
	 * @return the R expression assigning the data to a matrix called "data"
	 */
	public static String generateDataInputCommand(DataTable data) {
		if (data != null && data.dimensions.size() > 0
				&& data.records.size() > 1) {
			StringBuffer b = new StringBuffer();
			b.append("data <- matrix(c(");
			for (DataRecord r : data.records)
				for (DataEntry e : r)
					b.append(e.value + ",");
			b.deleteCharAt(b.length() - 1);
			b.append(")," + data.dimensions.size() + ")");
			return b.toString();
		} else
			return null;
	}

}
