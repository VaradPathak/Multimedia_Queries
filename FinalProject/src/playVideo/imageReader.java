package playVideo;

import java.util.ArrayList;
import java.util.List;

import playWaveFile.PlayWaveFile;

public class imageReader {

	public static void main(String[] args) throws InterruptedException {

		// String fileName = args[0];
		// int width = Integer.parseInt(args[1]);
		// int height = Integer.parseInt(args[2]);
		List<Double[]> feature1 = new ArrayList<>();
		List<Double[]> queryFeature1 = new ArrayList<>();
		List<Double[]> queryFeature2 = new ArrayList<>();

		String videoFileName = "F:\\git\\Multimedia_Queries\\FinalProject\\video_data\\soccer1.rgb";
		String audioFileName = "F:\\git\\Multimedia_Queries\\FinalProject\\all_audio_files\\soccer1.wav";

		String videoquery1 = "F:\\git\\Multimedia_Queries\\FinalProject\\video_data\\soccer2.rgb";
		// String audioquery1 =
		// "F:\\git\\Multimedia_Queries\\FinalProject\\all_audio_files\\soccer2.wav";

		String videoquery2 = "F:\\git\\Multimedia_Queries\\FinalProject\\video_data\\talk1.rgb";
		// String audioquery2 =
		// "F:\\git\\Multimedia_Queries\\FinalProject\\all_audio_files\\talk1.wav";
		int width = 352;
		int height = 288;

		Runnable audioTask = new PlayWaveFile(audioFileName);
		Thread audioWorker = new Thread(audioTask);
		audioWorker.setName("audio");

		Runnable videoTask = new PlayVideo(width, height, videoFileName,
				feature1);
		Thread videoWorker = new Thread(videoTask);
		videoWorker.setName("video");

		Runnable videoTask1 = new PlayVideo(width, height, videoquery1,
				queryFeature1);
		Thread videoWorker1 = new Thread(videoTask1);
		videoWorker.setName("video1");

		Runnable videoTask2 = new PlayVideo(width, height, videoquery2,
				queryFeature2);
		Thread videoWorker2 = new Thread(videoTask2);
		videoWorker.setName("video2");

		videoWorker.start();
		videoWorker.join();

		videoWorker1.start();
		videoWorker1.join();

		videoWorker2.start();
		videoWorker2.join();

		System.out.println(feature1.size() + " " + queryFeature1.size() + " "
				+ queryFeature2.size());
		System.out.println(matchVideo(feature1, queryFeature1));
		System.out.println(matchVideo(feature1, queryFeature2));
	}

	static double matchVideo(List<Double[]> feature, List<Double[]> feature2) {
		double totalMatchRating = 0.0;
		for (Double[] fea1 : feature) {
			double min = Integer.MAX_VALUE;
			for (Double[] fea2 : feature2) {
				double mad = mad(fea1, fea2);
				min = min > mad ? mad : min;
			}
			totalMatchRating += min;
		}
		return totalMatchRating;
	}

	static double mad(Double[] arr1, Double[] arr2) {
		double result = 0.0;
		for (int i = 0; i < arr2.length; i++) {
			Double double1 = arr2[i] - arr1[i];
			double1 = double1 > 0 ? double1 : (double1 * -1);
			result += double1;
		}
		result /= 25;
		return result;
	}
}