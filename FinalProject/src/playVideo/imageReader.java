package playVideo;

import playWaveFile.PlayWaveFile;

public class imageReader {

	public static void main(String[] args) {

		// String fileName = args[0];
		// int width = Integer.parseInt(args[1]);
		// int height = Integer.parseInt(args[2]);

		String videoFileName = "F:\\CS576\\workspace\\FinalProject\\video_data\\soccer1.rgb";
		String audioFileName = "F:\\CS576\\workspace\\FinalProject\\all_audio_files\\soccer1.wav";
		int width = 352;
		int height = 288;

		Runnable audioTask = new PlayWaveFile(audioFileName);
		Thread audioWorker = new Thread(audioTask);
		audioWorker.setName("audio");

		Runnable videoTask = new PlayVideo(width, height, videoFileName);
		Thread videoWorker = new Thread(videoTask);
		videoWorker.setName("video");

		videoWorker.start();
		audioWorker.start();
	}
}