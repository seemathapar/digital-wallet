package solution;

import java.io.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import solution.AdjacencyList;

public class AntiFraud {

	static final String UNVERIFIED = "unverified";
	static final String TRUSTED = "trusted";
	static Logger logger = Logger.getLogger("solution.AntiFraud");
	private String inputFile = null;
	private String testFile = null;
	private String outputFile1 = null;
	private String outputFile2 = null;
	private String outputFile3 = null;

	private static Graph graph = new Graph();

	AntiFraud() {
	}

	public static void main(String[] args) {
		boolean append = true;
		int limit = 1000000;
		FileHandler handler = null;
		try {			
			handler = new FileHandler("output%g.log", limit, 3, append);
			SimpleFormatter formatter = new SimpleFormatter();
			handler.setFormatter(formatter);
			logger.addHandler(handler);

			AntiFraud payMo = new AntiFraud();
			if (args.length >= 5) {
				payMo.setInputFile(args[0]);
				payMo.setTestFile(args[1]);
				payMo.setOutputFile1(args[2]);
				payMo.setOutputFile2(args[3]);
				payMo.setOutputFile3(args[4]);
			} else {
				logger.info("Usage : java <options> <inputfile> <testfile> <ouputfile1> <outputfile2> <outputfile3> ");
				return;
			}
			payMo.buildPaymentHistoryGraph(payMo.getInputFile());
			if (graph.getNumberOfVertices() > 0) {
				logger.info("****** Test file name: " + payMo.getTestFile());				
				payMo.processAllFeaturesWithSameDataSet(payMo.getTestFile());
			} else {
				System.out.println(
						" Error building the payment history graph, please check the input file and then process the transaction(s)");

			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			if (handler != null)
				handler.close();

		}

	}

	private void setOutputFile3(String fileName) {
		this.outputFile3 = fileName;

	}

	private void setOutputFile2(String fileName) {
		this.outputFile2 = fileName;

	}

	private void setOutputFile1(String fileName) {
		this.outputFile1 = fileName;
	}

	public void processAllFeaturesWithSameDataSet(String testFile) {
		Scanner input = validateFile(testFile);
		BitSet outputlist1 = new BitSet();
		BitSet outputlist2 = new BitSet();
		BitSet outputlist3 = new BitSet();
		int bitIndex = 0;
		int invalidRecordCount = 0;
		boolean isSecure = false;
		if (input != null) {
			System.out.println("******* Processing test records from file: "+ testFile);
			while (input.hasNextLine()) {
				StringBuilder record = new StringBuilder(input.nextLine());
				String[] split = record.toString().split(",");

				if (isValidRecord(split)) {
					isSecure = isNLevelFriend(split[1], split[2], 1);
					outputlist1.set(bitIndex, isSecure);
					isSecure = isNLevelFriend(split[1], split[2], 2);
					outputlist2.set(bitIndex, isSecure);
					isSecure = isNLevelFriend(split[1], split[2], 4);
					outputlist3.set(bitIndex, isSecure);
					bitIndex++;
				} else {
					invalidRecordCount++;
					logger.warning("Input record invalid. " + record);
				}
				split = null;
			}
			input.close();
			System.out.println("*******  Total number of records processed : "+ bitIndex);
			System.out.println("*******  Total number of invalid records : "+ invalidRecordCount);
			processOutput(outputlist1, this.getOutputFile1());
			processOutput(outputlist2, this.getOutputFile2());
			processOutput(outputlist3, this.getOutputFile3());
			System.out.println("******* Processing Completed *******");

		} else {
			System.out.println("There are no transactions to process. Please check the file. ");
		}

	}

	
	/**
	 * 
	 * @param testFile
	 * @param outputFileName
	 * @param featureNumber
	 */
	protected void processTransactions(String testFile, String outputFileName, int featureNumber) {
		Scanner scan = validateFile(testFile);
		BitSet outputStatus = null;
		if (scan != null) {
			outputStatus = verifyTransactions(scan, featureNumber);
			scan.close();
		} else {
			System.out.println("There are no transactions to process. Please check the file "+ testFile);
		}
		processOutput(outputStatus, outputFileName);
	}

	/**
	 * 
	 * @param outputStatusList
	 * @param outputFileName
	 */
	protected void processOutput(BitSet outputStatusList, String outputFileName) {

		PrintWriter output = null;

		if (outputStatusList != null && outputStatusList.length() > 0) {
			try {
				output = new PrintWriter(outputFileName);
				logger.info("********Begin writing output to file : " + outputFileName);
				for (int i = 0; i < outputStatusList.length(); i++) {
					if (outputStatusList.get(i)) {
						output.println(TRUSTED);
					} else {
						output.println(UNVERIFIED);
					}
				}
				logger.info("******Total records written to output file : " + outputStatusList.length());

			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
				return;
			} finally {
				if (output != null)
					output.close();
			}
		}

	}

	protected  BitSet verifyTransactions(Scanner input, int level) {

		BitSet booleanStatusList = new BitSet();
		int bitIndex = 0;
		StringBuilder record = new StringBuilder();

		boolean isSecure = false;
		while (input.hasNextLine()) {
			record = new StringBuilder(input.nextLine());
			String[] split = record.toString().split(",");

			if (isValidRecord(split)) {

				isSecure = isNLevelFriend(split[1].trim(), split[2].trim(), level);
				booleanStatusList.set(bitIndex++, isSecure);
			} else {
				logger.info("Input record invalid. " + record);
			}
			split = null;
		}
		System.out.println("Total records processed from Test file : " + bitIndex);
		return booleanStatusList;
	}

	
	/**
	 * Checks if the input record has valid data
	 * @param split
	 * @return
	 */
	protected boolean isValidRecord(String[] split) {
		if (split.length > 2) {
			if (split[1] != null && split[2] != null) {
				if (split[1].trim().length() > 0 && split[2].trim().length() > 0) {
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * Build the Adjacency list of nodes (friend's ids). Each node points to a list of other nodes
	 * connected by a earlier payment transaction.
	 * @param fileName
	 */
	public void buildPaymentHistoryGraph(String fileName) {

		System.out.println("Input file name : " + fileName);

		Scanner scan = validateFile(fileName);
		if (scan != null) {
			System.out.println(" Building the Payment History Graph ...");
			while (scan.hasNextLine()) {
				String[] split = scan.nextLine().split(",");
				if (split.length > 2) {
					getGraph().addEdge(split[1].trim(), split[2].trim());
				}
			}
			scan.close();
		}
	}

	/**
	 * Runs checks on the input files
	 * @param fileName
	 * @return
	 */
	protected  Scanner validateFile(String fileName) {
		FileInputStream file;
		Scanner scan;
		try {
			file = new FileInputStream(fileName);
			scan = new Scanner(file);
			if (!scan.hasNextLine()) {
				System.out.println("File " + fileName + " is empty");
				scan.close();
				return null;
			}
			scan.nextLine();
			if (!scan.hasNextLine()) {
				System.out.println(" File " + fileName + " has no records other than the header.");
				scan.close();
				return null;
			}
		} catch (FileNotFoundException e) {
			System.out.println(" The input file " + fileName + " does not exist");
			return null;
		}

		return scan;
	}

	/**
	 * Run a Breadth First search to find the friend of friends upto the
	 * specified level
	 * 
	 * @param g
	 * @param id1
	 * @param id2
	 * @param level
	 * @return
	 */
	public boolean isNLevelFriend(String id1, String id2, int level) {
		if (level == 1) {
			return isFriend(id1, id2);
		}

		Queue<String> q = new LinkedList<String>();
		Map<String, Boolean> visited = new HashMap<String, Boolean>();
		q.add(id1);
		visited.put(id1, true);

		while (!q.isEmpty() && level > 0) {
			String V = q.remove();
			AdjacencyList<String> friends = getGraph().get(V);
			if (friends != null) {
				for (String w : friends) {
					if (visited.get(w) == null) {
						q.add(w);
						visited.put(w, true);
					}
				}
			}
			level--;
		}
		if (visited.get(id2) == null) {
			return false;
		} else if (visited.get(id2)) {
			return true;
		}
		return false;

	}

	/**
	 * Evaluate if the payer and payee are directly connected
	 * @param friend1
	 * @param friend2
	 * @return
	 */
	protected  boolean isFriend(String friend1, String friend2) {
		AdjacencyList<String> friendList = getGraph().get(friend1);

		if (friendList != null) {
			return friendList.contains(friend2);
		}
		return false;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public String getTestFile() {
		return testFile;
	}

	public void setTestFile(String testFile) {
		this.testFile = testFile;
	}
	
	public String getOutputFile1() {
		return outputFile1;
	}

	public String getOutputFile2() {
		return outputFile2;
	}

	public String getOutputFile3() {
		return outputFile3;
	}

	public  Graph getGraph() {
		return graph;
	}

	


}
