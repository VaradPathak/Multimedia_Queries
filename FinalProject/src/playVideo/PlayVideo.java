package playVideo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;

import playWaveFile.PlaySound;
import playWaveFile.PlayWaveFile;

/**
 * @author Varad
 * 
 */
public class PlayVideo implements Runnable {
	int width;
	int height;
	String fileName;
	// List<Double[]> featureList;
	JLabel MatchedVideoLabel;
	JSlider framesPerSecond;
	PlayWaveFile audiofps;
	
	public volatile boolean IS_PAUSED;
	public volatile boolean IS_STOPPED;
	public volatile int i = 0;

	public PlayVideo(int w, int h, String videoFileName,
			JLabel MatchedVideoLabel, JSlider framesPerSecond,PlayWaveFile audiofps) {
		this.width = w;
		this.height = h;
		this.fileName = videoFileName;
		// this.featureList = featureList;
		this.MatchedVideoLabel = MatchedVideoLabel;
		this.IS_PAUSED = false;
		this.IS_STOPPED = false;
		this.framesPerSecond = framesPerSecond;
		this.i = 0;
		this.audiofps =audiofps;
		
	}

	public void PauseVideo() {
		IS_PAUSED = true;
	}

	public void resumeVideo() {
		IS_PAUSED = false;
		//this.run();

	}

	public void stopVideo() {
		this.i = 0;
		framesPerSecond.setValue(0);
		IS_STOPPED = true;
		
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
			//framesPerSecond = new JSlider(JSlider.HORIZONTAL,
		    //        0, (int)iteration, 0);
			framesPerSecond.setMaximum((int)iteration); 
			framesPerSecond.setMinimum(0);

		
		    while(true)
		    {
		    	while(this.audiofps.IS_STARTED !=true)
		    	{
		    		
		    	}
		    	this.audiofps.playSound.i = i;
		    	if (IS_STOPPED == true)
					break;
				while (i < iteration) {
					if (IS_STOPPED == true)
						break;
					if (IS_PAUSED == false) {
						// for (int i = 0; i < iteration && IS_PAUSED ==false; i++)
						// {
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
	
						MatchedVideoLabel.setIcon(new ImageIcon(currentFrame));
						framesPerSecond.setValue(i);
						
						TimeUnit.MILLISECONDS.sleep(35);
						i++; // for while
						this.audiofps.playSound.i = i;
					} else {
	
						Thread.sleep(30);
					}
				}
				i = 0;
				this.audiofps.playSound.i = i;
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
