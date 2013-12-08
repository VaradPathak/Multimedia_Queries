package playWaveFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * plays a wave file using PlaySound class
 * 
 * @author Giulio
 */
public class PlayWaveFile implements Runnable {
	String filename;
	PlaySound playSound;
	int numberOfFrames;

	public volatile boolean IS_PAUSED;
	public volatile boolean IS_STOPPED;
	public volatile int i = 0;

	public PlayWaveFile(String filename) {
		this.filename = filename;
	}

	@Override
	public void run() {
		// opens the inputStream
		BufferedInputStream myStream;
		File audioFile = new File(filename);

		try {
			FileInputStream inputStream = new FileInputStream(filename);
			myStream = new BufferedInputStream(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		// initializes the playSound Object
		try {
			this.playSound = new PlaySound(myStream, audioFile.length());
		} catch (PlayWaveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int numOfFrames = this.playSound.numOfFrames;

		// plays the sound
		try {
			while (true) {
				if (this.IS_STOPPED == true)
					break;
				for (int i = 0; i < numOfFrames; i++) {
					
					if (this.IS_STOPPED == true)
						break;
					if (this.IS_PAUSED == false) {
						this.playSound.play();
					}
				}
			}
		} catch (PlayWaveException e) {
			e.printStackTrace();
			return;
		}
	}

	public void resumeAudio() {
		this.IS_PAUSED = false;
		this.playSound.resumeAudio();

	}

	public void stopAudio() {
		this.i = 0;
		IS_STOPPED = true;
		this.playSound.stopAudio();

	}

	public void PauseAudio() {
		IS_PAUSED = true;
		this.playSound.PauseAudio();

	}

}
