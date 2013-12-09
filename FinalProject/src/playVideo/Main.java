package playVideo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import resultComp.ResultComp;
import userInterface.Frontend;
import Result.Result;
import extractInformation.ExtractFeatures;

public class Main {

	public static int percent_to_match = 25;
	public static int jumpSize = 0;

	public static void main(String[] args) throws InterruptedException {

		int width = 352;
		int height = 288;

		/***************************************** FEATURE EXTRACTION ****************************/
		// offline feature extraction
		// Main.extractFeatures(height, width);
		/***************************************** FEATURE EXTRACTION ****************************/

		// String queryName; // pass this to createUi and get back the queryName
		Frontend frontend = new Frontend();
		try {
			frontend.createUi();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Histogram test code
		/*
		 * double[] value = new double[100]; Random generator = new Random();
		 * for (int i=1; i < 100; i++) { value[i] = generator.nextDouble(); int
		 * number = 10; HistogramDataset dataset = new HistogramDataset();
		 * dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		 * dataset.addSeries("Histogram",value,number); String plotTitle =
		 * "Histogram"; String xaxis = "number"; String yaxis = "value";
		 * PlotOrientation orientation = PlotOrientation.VERTICAL; boolean show
		 * = false; boolean toolTips = false; boolean urls = false; JFreeChart
		 * chart = ChartFactory.createHistogram( plotTitle, xaxis, yaxis,
		 * dataset, orientation, show, toolTips, urls); int width = 500; int
		 * height = 300; try { ChartUtilities.saveChartAsPNG(new
		 * File("histogram.PNG"), chart, width, height); } catch (IOException e)
		 * {} }
		 */

	}

	/**
	 * Extract audeo and video features of all the videos in the db and store
	 * them in corresponding csv files. This part of offline feature extraction.
	 * 
	 * @param width
	 *            : Width of the frame
	 * @param height
	 *            : Height of the frame
	 * 
	 * @throws InterruptedException
	 */
	static void extractFeatures(int height, int width)
			throws InterruptedException {
		String videoDirectoryPath = "/home/hrushikesh/eclipse/projects/final/db/";
		String audioDirectoryPath = "/home/hrushikesh/eclipse/projects/final/all_audio_files/";

		List<Double[]> videoFeatures = new ArrayList<>();
		List<Double[]> audioFeatures = new ArrayList<>();

		File dir = new File(videoDirectoryPath);
		for (File child : dir.listFiles()) {

			String videofilename = child.getName();
			String audiofilename = videofilename.substring(0,
					videofilename.length() - 4)
					+ ".wav";
			videofilename = videoDirectoryPath + videofilename;
			audiofilename = audioDirectoryPath + audiofilename;

			// videoFeatures, audioFeatures = dummy parameter since features for
			// database are stored on disk
			Runnable extractTask = new ExtractFeatures(width, height,
					videofilename, videoFeatures, audiofilename, audioFeatures,
					0);

			Thread extractWorker = new Thread(extractTask);
			extractWorker.setName("extractFeatures");
			extractWorker.start();
			extractWorker.join();
		}

	}

	/**
	 * Create histogram of % match with the query video
	 * 
	 * @param value_map
	 *            : List of percentage match values for the video
	 */
	static void createHistoGram(Map<Integer, ArrayList<Double>> value_map) {

		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.SCALE_AREA_TO_1);

		double[] totals = new double[value_map.size()];
		for (int g = 0; g < value_map.size(); g++) {
			// for (ArrayList<Double> frameChromaFeature : value_map.values()) {
			ArrayList<Double> frameChromaFeature = value_map.get(g);
			double total = 0;
			for (int h = 0; h < frameChromaFeature.size(); h++) {

				total += frameChromaFeature.get(h);

			}
			// total = total / 1000000000;
			total = Math.abs(375000 - total); // scaling from 1 to 100;
			totals[g] = total;

		}

		dataset.addSeries("Histogram", totals, value_map.size());
		String plotTitle = "Similarity graph";
		String xaxis = "frames";
		String yaxis = "similarity";
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis,
				yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;
		try {
			ChartUtilities.saveChartAsPNG(new File("histogram.PNG"), chart,
					width, height);
		} catch (IOException e) {
		}

	}

	/**
	 * Sequentially match with all the audio files in the db and return the
	 * matching value list
	 * 
	 * @param audioFeatures
	 *            : Audio features of the query Audio file
	 * @param audioFileName
	 *            : Name of the Audio file
	 * @return : List of matching value results of all the audio files
	 * @throws IOException
	 */
	public static Map<String, Result> match_with_all_audios(
			List<Double[]> audioFeatures, String audioFileName,
			Map<String, Map<Integer, ArrayList<Double>>> audio_value_map,
			Map<String, Integer> Audio_Window_Count_Map) throws IOException {

		String myDirectoryPath = "/home/hrushikesh/eclipse/projects/final/audio_csv_results/";

		String split = ",";
		String line = "";
		BufferedReader br = null;

		Map<String, Result> results = new HashMap<>();
		File dir = new File(myDirectoryPath);
		for (File child : dir.listFiles()) {

			List<Double[]> result_feature = new ArrayList<>();
			String resultfilename = myDirectoryPath + child.getName();
			String filename = child.getName().substring(0,
					child.getName().lastIndexOf("."));
			br = new BufferedReader(new FileReader(resultfilename));
			System.out.println(resultfilename);
			int numOfLines = 0;
			try {
				while ((line = br.readLine()) != null) {
					numOfLines++;
					// use comma as separator
					String[] temp = line.split(split);
					Double[] doubles = new Double[temp.length]; // 32 = features
																// per
					// sample
					for (int i = 0; i < temp.length; i++) {
						doubles[i] = Double.parseDouble(temp[i]);
					}
					result_feature.add(doubles);

				}
				Audio_Window_Count_Map.put(filename, numOfLines);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			br.close();
			double matchvalue = matchFeatures(audioFeatures, result_feature,
					audio_value_map, filename);
			System.out.println("done" + child.getName() + "matchvalue = "
					+ matchvalue);

			String temp = child.getName().substring(0,
					child.getName().lastIndexOf("."));

			results.put(temp, new Result(matchvalue, temp));

		}
		System.out.println("RESULTS OF AUDIO SEARCH");
		for (String name : results.keySet()) {
			System.out.println(results.get(name).rank_value);
		}

		return results;
	}

	/**
	 * Sequentially match with all the videos in the db and return the matching
	 * value list
	 * 
	 * @param featureList
	 *            : List of all the extracted features of the video
	 * @param videoFileName
	 *            : Name of the query video file
	 * @return : List of matching value results of all the videos
	 * @throws IOException
	 */
	public static List<Result> match_with_all_videos(
			List<Double[]> featureList, String videoFileName,
			Map<String, Map<Integer, ArrayList<Double>>> video_value_map)
			throws IOException {

		String myDirectoryPath = "/home/hrushikesh/eclipse/projects/final/csv_results/";

		String split = ",";
		String line = "";
		BufferedReader br = null;

		List<Result> results = new ArrayList<>();
		File dir = new File(myDirectoryPath);
		for (File child : dir.listFiles()) {

			// System.out.println(child.getName());
			List<Double[]> result_feature = new ArrayList<>();
			String resultfilename = myDirectoryPath + child.getName();
			String filename = child.getName().substring(0,
					child.getName().lastIndexOf("."));
			// String resultfilename =myDirectoryPath + "talk2.csv";
			br = new BufferedReader(new FileReader(resultfilename));
			System.out.println(resultfilename);
			try {
				while ((line = br.readLine()) != null) {

					// use comma as separator
					String[] temp = line.split(split);
					Double[] doubles = new Double[25]; // 25 = features per
														// frame
					for (int i = 0; i < temp.length; i++) {
						doubles[i] = Double.parseDouble(temp[i]);
						// System.out.println(doubles[i]);

					}
					result_feature.add(doubles);

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			br.close();
			double matchvalue = matchFeatures(featureList, result_feature,
					video_value_map, filename);
			System.out.println("done" + child.getName() + "matchvalue = "
					+ matchvalue);

			String temp = child.getName().substring(0,
					child.getName().lastIndexOf("."));
			Result r = new Result(matchvalue, temp);
			results.add(r);

		}
		Collections.sort(results, new ResultComp());
		System.out.println("SORTING BY RANK");
		for (int i = 0; i < results.size(); i++) {
			System.out.println(results.get(i).rank_value);

		}

		return results;
	}

	/**
	 * Find the maximum matching position in the video based on the sum of
	 * feature differences
	 * 
	 * @param value_map
	 *            : Map of differences of corresponding feature values
	 * @return : Minimum of sum of difference values
	 */
	static double calculate_match(Map<Integer, ArrayList<Double>> value_map) {
		double minval = Integer.MAX_VALUE;
		double temp = 0;
		for (int g = 0; g < value_map.size(); g++) {
			temp = 0;
			ArrayList<Double> featureList = value_map.get(g);
			for (int h = 0; h < featureList.size(); h++) {
				temp += featureList.get(h);

			}
			if (temp < minval)
				minval = temp;
		}

		return minval;
	}

	/**
	 * Match the query audio/video features with database video
	 * 
	 * @param queryfeature
	 *            : features of query video
	 * @param dbfeatures
	 *            : features of db video.
	 * @return
	 * @throws IOException
	 */
	static double matchFeatures(List<Double[]> queryfeature,
			List<Double[]> dbfeatures,
			Map<String, Map<Integer, ArrayList<Double>>> VO_value_map,
			String filename) throws IOException {

		// int jumpSize = 0; // indicates frame/sample separation after which to
		// compare
		int no_of_frames_to_match = queryfeature.size() * percent_to_match
				/ 100;
		jumpSize = queryfeature.size() / no_of_frames_to_match;
		System.out.println("frame difference = " + jumpSize);
		System.out.println("queryfeatures size = " + queryfeature.size());
		System.out.println("dbfeature size = " + dbfeatures.size());

		Map<Integer, ArrayList<Double>> value_map = new HashMap<>();

		int list_iterator = 0;
		int i = 0;
		int counters = 0;
		int k = 0;
		int flag = 0;
		while (i < dbfeatures.size()) {

			// run the loop for all the frames from dbvideo
			// for the frames of queryvideo separated by framedifference

			int j = 0;

			int temp = i;

			ArrayList<Double> mad_values = new ArrayList<>();

			while (j < queryfeature.size()) {

				mad_values.add(list_iterator++,
						mad(queryfeature.get(j), dbfeatures.get(i)));
				i = i + jumpSize;
				if (i >= dbfeatures.size()) {
					flag = 1;
					break;
				}
				j = j + jumpSize;
			}

			value_map.put(k++, mad_values);
			if (flag == 1)
				break;
			list_iterator = 0;

			i = temp + jumpSize;
			counters++;
		}

		System.out.println("match done, total iterations over queryvideo = "
				+ counters);

		VO_value_map.put(filename, value_map);
		return calculate_match(value_map); // calculates the value used for
											// ranking the vid
		// createHistoGram(value_map);
		// return 0;

	}

	/**
	 * Calculate mean absolute distance between the feature arrays
	 * 
	 * @param arr1
	 *            : Array of features
	 * @param arr2
	 *            : Array of features
	 * @return Mean absolute distance
	 */
	static double mad(Double[] arr1, Double[] arr2) {
		double result = 0.0;
		for (int i = 0; i < arr2.length; i++) {
			Double double1 = arr2[i] - arr1[i];
			double1 = double1 > 0 ? double1 : (double1 * -1);
			result += double1;
		}
		// result /= 25;
		result *= 1000;

		return result;
	}
}