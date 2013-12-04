package extractInformation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Varad
 * 
 */
public class ExtractFeatures implements Runnable {
	int width;
	int height;
	String fileName;
	List<Double[]> chromaFeatureList;

	public ExtractFeatures(int w, int h, String videoFileName,
			List<Double[]> chromaFeatureList) {
		this.width = w;
		this.height = h;
		this.fileName = videoFileName;
		this.chromaFeatureList = chromaFeatureList;
	}

	@Override
	public void run() {
		InputStream is = null;
		try {
			File file = new File(fileName);
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
			saveFeatures();

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

	private void saveFeatures() throws IOException {
		String featureFileName = this.fileName.substring(0,
				this.fileName.lastIndexOf("."))
				+ ".csv";

		File featureFile = new File(featureFileName);
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

	private void extractFeatures(long size, byte[] bytes) {
		ExtractChroma extract = new ExtractChroma();
		long iteration = size / (height * width * 3);
		int frameSize = (height * width * 3);
		int skip = height * width;
		for (int i = 0; i < iteration; i++) {
			int ind = (i * frameSize);
			byte[] frame = new byte[(int) frameSize];

			System.arraycopy(bytes, ind, frame, 0, skip);
			System.arraycopy(bytes, ind + skip, frame, skip, skip);
			System.arraycopy(bytes, ind + (skip * 2), frame, (skip * 2), skip);

			this.chromaFeatureList.add(extract.extractChroma(frame));
		}
	}

}
