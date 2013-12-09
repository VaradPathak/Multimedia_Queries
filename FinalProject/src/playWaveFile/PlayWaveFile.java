package playWaveFile;

/**
 * plays a wave file using PlaySound class
 * 
 * @author Giulio
 */
public class PlayWaveFile implements Runnable {
	String filename;
	public PlaySound playSound;
	int numberOfFrames;

	public volatile boolean IS_PAUSED;
	public volatile boolean IS_STOPPED;
	public volatile boolean IS_STARTED;
	public volatile int i = 0;

	public PlayWaveFile(String filename) {
		this.filename = filename;
		this.IS_STARTED =false;
	}

	@Override
	public void run() {

		// initializes the playSound Object
		this.playSound = new PlaySound(this.filename);
		
		// plays the sound
		try {
			while (true) {
				if (this.IS_STOPPED == true)
					break;
				if (this.IS_PAUSED == false) {
					this.IS_STARTED =true;
					this.playSound.play();
					
					this.playSound.i = 0;
				}
			}
		} catch (PlayWaveException | InterruptedException e) {
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
