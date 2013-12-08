package extractInformation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import playWaveFile.PlayWaveException;

/**
 * @author Varad
 * 
 */
public class ExtractFeatures implements Runnable {
	int width;
	int height;
	String videoFileName;
	String audioFileName;
	List<Double[]> chromaFeatureList;
	List<Double[]> audioFeatureList;
	int flag;

	public ExtractFeatures(int w, int h, String videoFileName,
			List<Double[]> chromaFeatures, String audiofilename,
			List<Double[]> audioFeatures, int flag) {
		this.width = w;
		this.height = h;
		this.videoFileName = videoFileName;
		this.audioFileName = audiofilename;
		if (flag == 0) {
			this.chromaFeatureList = new ArrayList<>();
			this.audioFeatureList = new ArrayList<>();
		} else {
			this.chromaFeatureList = chromaFeatures;
			this.audioFeatureList = audioFeatures;
		}
		this.flag = flag;
	}

	@Override
	public void run() {
		InputStream is = null;
		try {
			File file = new File(videoFileName);
			long size = file.length();
			is = new FileInputStream(file);
			byte[] bytes = new byte[(int) size];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			extractFeatures(size, bytes);
			// if extracting features for query , don't save on disk
			if (flag != 1) {
				saveVideoFeatures();
				saveAudioFeatures();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * Save extracted audio features to the corresponding csv file
	 * 
	 * @throws IOException
	 */
	private void saveAudioFeatures() throws IOException {
		String save_path = "/home/hrushikesh/eclipse/projects/final/audio_csv_results/";
		String audioFeatureFileName = this.videoFileName.substring(0,
				this.videoFileName.lastIndexOf(".")) + ".csv";

		audioFeatureFileName = audioFeatureFileName.substring(
				audioFeatureFileName.lastIndexOf("/"),
				audioFeatureFileName.length());
		audioFeatureFileName = save_path + "/" + audioFeatureFileName;
		File featureFile = new File(audioFeatureFileName);
		if (!featureFile.exists()) {
			featureFile.createNewFile();
		}

		FileWriter fw = new FileWriter(featureFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for (Double[] sampleAudioFeature : this.audioFeatureList) {
			for (int i = 0; i < sampleAudioFeature.length; i++) {
				if (i < sampleAudioFeature.length - 1) {
					bw.write(sampleAudioFeature[i] + ",");
				} else {
					bw.write(sampleAudioFeature[i] + "\n");
				}
			}
		}

		bw.close();
	}

	/**
	 * Save extracted video features to the corresponding csv file
	 * 
	 * @throws IOException
	 */
	private void saveVideoFeatures() throws IOException {
		String save_path = "/home/hrushikesh/eclipse/projects/final/csv_results/";
		String videoFeatureFileName = this.videoFileName.substring(0,
				this.videoFileName.lastIndexOf(".")) + ".csv";

		videoFeatureFileName = videoFeatureFileName.substring(
				videoFeatureFileName.lastIndexOf("/"),
				videoFeatureFileName.length());
		videoFeatureFileName = save_path + "/" + videoFeatureFileName;
		File featureFile = new File(videoFeatureFileName);
		if (!featureFile.exists()) {
			featureFile.createNewFile();
		}

		FileWriter fw = new FileWriter(featureFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for (Double[] frameChromaFeature : this.chromaFeatureList) {
			for (int i = 0; i < frameChromaFeature.length; i++) {
				if (i < frameChromaFeature.length - 1) {
					bw.write(frameChromaFeature[i] + ",");
				} else {
					bw.write(frameChromaFeature[i] + "\n");
				}
			}
		}

		bw.close();
	}

	/**
	 * Extract audio and video features from the corresponding files
	 * 
	 * @param size
	 *            : total size of the video file
	 * @param bytes
	 *            : Byte array containing RGB values of the video, frame by
	 *            frame
	 */
	private void extractFeatures(long size, byte[] bytes) {
		ExtractChroma extractChroma = new ExtractChroma();
		long iteration = size / (height * width * 3);
		int frameSize = (height * width * 3);
		int skip = height * width;
		for (int i = 0; i < iteration; i++) {
			int ind = (i * frameSize);
			byte[] frame = new byte[(int) frameSize];

			System.arraycopy(bytes, ind, frame, 0, skip);
			System.arraycopy(bytes, ind + skip, frame, skip, skip);
			System.arraycopy(bytes, ind + (skip * 2), frame, (skip * 2), skip);

			this.chromaFeatureList.add(extractChroma.extractChroma(frame));
		}
		ExtractAudio extractAudio = new ExtractAudio(this.audioFileName);
		try {
			extractAudio.extractAudio(this.audioFeatureList);
		} catch (PlayWaveException e) {
			e.printStackTrace();
		}
	}

}
