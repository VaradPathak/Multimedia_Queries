package playVideo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.util.Random;

import org.jfree.chart.*;
import org.jfree.data.statistics.*;
import org.jfree.chart.plot.PlotOrientation;










import Result.Result;
import Result.*;
import resultComp.ResultComp;
import playWaveFile.PlayWaveFile;
import userInterface.Frontend;
import extractInformation.ExtractFeatures;

public class imageReader {

	public static void main(String[] args) throws InterruptedException, IOException {

		// String fileName = args[0];
		// int width = Integer.parseInt(args[1]);
		// int height = Integer.parseInt(args[2]);
		//List<Double[]> feature1 = new ArrayList<>();
		//List<Double[]> queryFeature1 = new ArrayList<>();
		//List<Double[]> queryFeature2 = new ArrayList<>();

		//String videoFileName = "/home/hrushikesh/eclipse/projects/final/query/query3.rgb";
		//String audioFileName = "/home/hrushikesh/eclipse/projects/final/all_audio_files/talk1.wav";

		//String videoquery1 = "/home/hrushikesh/eclipse/projects/final/db/talk2.rgb";
		// String audioquery1 =
		// "F:\\git\\Multimedia_Queries\\FinalProject\\all_audio_files\\soccer2.wav";

		//String videoquery2 = "/home/hrushikesh/eclipse/projects/final/db/talk1.rgb";
		// String audioquery2 =
		// "F:\\git\\Multimedia_Queries\\FinalProject\\all_audio_files\\talk1.wav";
		int width = 352;
		int height = 288;

		//Runnable audioTask = new PlayWaveFile(audioFileName);
		//Thread audioWorker = new Thread(audioTask);
		//audioWorker.setName("audio");

		/*Runnable videoTask = new ExtractFeatures(width, height, videoFileName,
				feature1);
		Thread videoWorker = new Thread(videoTask);
		videoWorker.setName("video");

		Runnable videoTask1 = new ExtractFeatures(width, height, videoquery1,
				queryFeature1);
		Thread videoWorker1 = new Thread(videoTask1);
		videoWorker1.setName("video1");

		Runnable videoTask2 = new ExtractFeatures(width, height, videoquery2,
				queryFeature2);
		Thread videoWorker2 = new Thread(videoTask2);
		videoWorker2.setName("video2");
	*/
		//audioWorker.start();
		/*videoWorker.start();
		
		
		videoWorker.join();
		
		
		videoWorker1.start();
		videoWorker1.join();
		


		videoWorker2.start();
		videoWorker2.join();
		try {
		System.out.println(feature1.size() + " " + queryFeature1.size() + " "
				+ queryFeature2.size());
		System.out.println(matchVideo(feature1, queryFeature1)
				/ queryFeature1.size());
		
			//System.out.println(matchVideo(feature1, queryFeature2)
			//		/ queryFeature2.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		*/
		
		
		/*****************************************FEATURE EXTRACTION****************************/
		//offline feature extraction
		//Extract_features(width,height);
		/*****************************************FEATURE EXTRACTION****************************/
		
		
		String queryName; //pass this to createUi and get back the queryName
		Frontend frontend = new Frontend();
		frontend.createUi();
		
		
		
		
		//Histogram test code
		/*double[] value = new double[100];
	       Random generator = new Random();
	       for (int i=1; i < 100; i++) {
	       value[i] = generator.nextDouble();
	           int number = 10;
	       HistogramDataset dataset = new HistogramDataset();
	       dataset.setType(HistogramType.RELATIVE_FREQUENCY);
	       dataset.addSeries("Histogram",value,number);
	       String plotTitle = "Histogram"; 
	       String xaxis = "number";
	       String yaxis = "value"; 
	       PlotOrientation orientation = PlotOrientation.VERTICAL; 
	       boolean show = false; 
	       boolean toolTips = false;
	       boolean urls = false; 
	       JFreeChart chart = ChartFactory.createHistogram( plotTitle, xaxis, yaxis, 
	                dataset, orientation, show, toolTips, urls);
	       int width = 500;
	       int height = 300; 
	        try {
	        ChartUtilities.saveChartAsPNG(new File("histogram.PNG"), chart, width, height);
	        } catch (IOException e) {}
	         }
	   */
		
		
		
		
		
	}

	/*static double matchVideo(List<Double[]> feature2, List<Double[]> feature1) {
		double totalMatchRating = 0.0;
		for (Double[] fea1 : feature1) {
			double min = Integer.MAX_VALUE;
			for (Double[] fea2 : feature2) {
				double mad = mad(fea1, fea2);
				min = min > mad ? mad : min;
			}
			totalMatchRating += min;
			// System.out.println(min);
		}
		return totalMatchRating;
	}*/
	//matchvideo (inputquery_feature, database_video_feature)
	
	static void Extract_features(int width,int height) throws InterruptedException
	{
		String myDirectoryPath = "/home/hrushikesh/eclipse/projects/final/db/";
		
		List<Double[]> feature1 = new ArrayList<>();
		File dir = new File(myDirectoryPath);
		  for (File child : dir.listFiles()) {
		    
			 //System.out.println(child.getName());
			  
			  String videofilename = child.getName();
			  Runnable extractTask = new ExtractFeatures(width, height,
					  myDirectoryPath+videofilename,feature1,0);
			  //feature1 = dummy parameter since features for database are stored on disk
			  Thread extractWorker = new Thread(extractTask);
			  extractWorker.setName("video");
			  extractWorker.start();
			  extractWorker.join();
		  }
		
		//ExtractFeatures(width, height, videoFileName,
		//		feature1);
		
		
	}
	
	static void createHistoGram(Map<Integer,ArrayList<Double>> value_map) {
		
		 HistogramDataset dataset = new HistogramDataset();
	      dataset.setType(HistogramType.SCALE_AREA_TO_1);
	      
          double[] totals = new double[value_map.size()];	     
		for(int g = 0; g < value_map.size(); g++)
		{
			//for (ArrayList<Double> frameChromaFeature : value_map.values()) {
			ArrayList<Double> frameChromaFeature = value_map.get(g);
			double total = 0;
			for (int h = 0; h < frameChromaFeature.size(); h++) {
				
				 total += frameChromaFeature.get(h);
				
			}
			//total = total / 1000000000;
			total = Math.abs(375000 - total); //scaling from 1 to 100;
			totals[g] = total;
			
		}
		
		  
		  dataset.addSeries("Histogram",totals,value_map.size());
	       String plotTitle = "Similarity graph"; 
	       String xaxis = "frames";
	       String yaxis = "similarity"; 
	       PlotOrientation orientation = PlotOrientation.VERTICAL; 
	       boolean show = false; 
	       boolean toolTips = false;
	       boolean urls = false; 
	       JFreeChart chart = ChartFactory.createHistogram( plotTitle, xaxis, yaxis, 
	                dataset, orientation, show, toolTips, urls);
	       int width = 500;
	       int height = 300; 
	        try {
	        ChartUtilities.saveChartAsPNG(new File("histogram.PNG"), chart, width, height);
	        } catch (IOException e) {}
	         
		
	}
	
	
	public static List<Result> match_with_all_videos(List<Double[]> feature1,String videoName) throws IOException {
		
		
		String myDirectoryPath = "/home/hrushikesh/eclipse/projects/final/csv_results/";
		
		String split = ",";
		String line = "";
		BufferedReader br = null;
		
		
		List<Result> results = new ArrayList<>();
		File dir = new File(myDirectoryPath);
		  for (File child : dir.listFiles()) {
			  
			 //System.out.println(child.getName());
			  List<Double[]> result_feature = new ArrayList<>();
			  String resultfilename =myDirectoryPath + child.getName();
			  //String resultfilename =myDirectoryPath + "talk2.csv";
			  br = new BufferedReader(new FileReader(resultfilename));
			  System.out.println(resultfilename);
			  try {
				while ((line = br.readLine()) != null) {
					  
				        // use comma as separator
					String[] temp = line.split(split);
					Double[] doubles = new Double[25]; //25 = features per frame
					for(int i = 0; i<temp.length;i++ ){
						doubles[i] = Double.parseDouble(temp[i]);
						//System.out.println(doubles[i]);
						
						
					}
					result_feature.add(doubles);
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  br.close();
			 double matchvalue = matchVideo(feature1,result_feature);
			 System.out.println("done" + child.getName() + "matchvalue = " + matchvalue);
			 
			 String temp = child.getName().substring(0, child.getName().lastIndexOf(".")) + ".rgb"; 
			 Result r = new Result(matchvalue,temp);
			 results.add(r);

			 
		  }
		  Collections.sort(results, new ResultComp());
		  System.out.println("SORTING BY RANK");
		  for(int i = 0; i< results.size();i++)
		  {
			  System.out.println(results.get(i).rank_value);
			  
		  }
			  
		  return results;
	}
	
	
	
	
	
	
	static double calculate_match(Map<Integer,ArrayList<Double>> value_map)
	{
		double minval = Integer.MAX_VALUE;
		double temp = 0;
		for(int g = 0; g < value_map.size(); g++)
		{
			temp = 0;
			ArrayList<Double> frameChromaFeature = value_map.get(g);
			for (int h = 0; h < frameChromaFeature.size(); h++) {
				temp += frameChromaFeature.get(h);
				
				
			}
			if(temp < minval) 
				minval = temp;
		}
		
		
		return minval;
	}
	/**
	 * @param queryfeature : features of queryvideo
	 * @param dbfeatures : features of db video.
	 * @return 
	 * @throws IOException 
	 */
	static double matchVideo(List<Double[]> queryfeature, List<Double[]> dbfeatures) throws IOException {
		
		int frame_difference = 0; //indicates frame difference by which to compare frames
		int no_of_frames_to_match = queryfeature.size()*25/100;
		frame_difference = queryfeature.size()/no_of_frames_to_match;
		System.out.println("frame difference = " + frame_difference);
		System.out.println("queryfeatures size = " + queryfeature.size());
		System.out.println("dbfeature size = " + dbfeatures.size());
		
		Map<Integer,ArrayList<Double>> value_map = new HashMap<>();
		
		int list_iterator =0;
		double totalMatchRating = 0.0;
		int i = 0;
		int counters = 0;
		int k = 0;
		int flag = 0;
		while(i < dbfeatures.size())
		{
		
			//run the loop for all the frames from dbvideo
			//for the frames of queryvideo separated by framedifference
			
			double min = Integer.MAX_VALUE;
			int j = 0;
			
			int temp = i;
		
			ArrayList<Double> mad_values = new ArrayList<>();
			while(j < queryfeature.size())
			{
				
					mad_values.add(list_iterator++, mad(queryfeature.get(j),dbfeatures.get(i)));
					i = i + frame_difference;
					if(i >=dbfeatures.size())
					{	
						flag =1;
						break;
					}
					j = j + frame_difference;
			}
			
			value_map.put(k++, mad_values);
			if(flag == 1)
				break;
			list_iterator=0;
			
			i = temp + frame_difference;
			//System.out.println("iteration done");
			counters++;
				
			// System.out.println(min);
		}
		
		
		
		System.out.println("match done, total iterations over queryvideo = " + counters);
		/*File featureFile = new File("q2resulttalk2.csv");
		if (!featureFile.exists()) {
			featureFile.createNewFile();
		}

		
		FileWriter fw = new FileWriter(featureFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for(int g = 0; g < k; g++)
		{
			//for (ArrayList<Double> frameChromaFeature : value_map.values()) {
			ArrayList<Double> frameChromaFeature = value_map.get(g);
			for (int h = 0; h < frameChromaFeature.size(); h++) {
				if (h < frameChromaFeature.size() - 1) {
					bw.write(frameChromaFeature.get(h) + ",");
				} else {
					bw.write(frameChromaFeature.get(h) + "\n");
				}
			}
			bw.write("\n");
			//}
		}	
		bw.close();
		*/
		
		return calculate_match(value_map); //calculates the value used for ranking the vid
		//createHistoGram(value_map);
		//return 0;
		
		
		
	}

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