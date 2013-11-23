package playWaveFile;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * plays a wave file using PlaySound class
 * 
 * @author Giulio
 */
public class PlayWaveFile implements Runnable {
	String filename;

	public PlayWaveFile(String filename) {
		this.filename = filename;
	}

	@Override
	public void run() {
		// opens the inputStream
		BufferedInputStream myStream;

		try {
			FileInputStream inputStream = new FileInputStream(filename);
			myStream = new BufferedInputStream(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		// initializes the playSound Object
		PlaySound playSound = new PlaySound(myStream);

		// plays the sound
		try {
			playSound.play();
		} catch (PlayWaveException e) {
			e.printStackTrace();
			return;
		}
	}

}
