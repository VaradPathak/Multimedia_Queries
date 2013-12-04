package playVideo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;

import userInterface.Frontend;
import extractInformation.ExtractChroma;

/**
 * @author Varad
 * 
 */
public class PlayVideo implements Runnable {
	int width;
	int height;
	String fileName;
	List<Double[]> featureList;

	public PlayVideo(int w, int h, String videoFileName,
			List<Double[]> featureList) {
		this.width = w;
		this.height = h;
		this.fileName = videoFileName;
		this.featureList = featureList;
	}

	@Override
	public void run() {
		InputStream is = null;
		try {
			File file = new File(fileName);
			long size = file.length();
			BufferedImage currentFrame = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			long frameSize = (height * width * 3);
			is = new FileInputStream(file);
			long iteration = size / (height * width * 3);
			byte[] bytes = new byte[(int) size];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			Frontend frontend = new Frontend();
			frontend.createUi();

			for (int i = 0; i < iteration - 2; i++) {
				int ind = (int) (i * frameSize);
				
				for (int y = 0; y < height; y++) {

					for (int x = 0; x < width; x++) {
						byte r = bytes[ind];
						byte g = bytes[ind + height * width];
						byte b = bytes[ind + height * width * 2];

						int pix = 0xff000000 | ((r & 0xff) << 16)
								| ((g & 0xff) << 8) | (b & 0xff);
						currentFrame.setRGB(x, y, pix);
						ind++;
					}
				}

				frontend.OriginalVideoLabel
						.setIcon(new ImageIcon(currentFrame));
				TimeUnit.MILLISECONDS.sleep(15);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
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

}
