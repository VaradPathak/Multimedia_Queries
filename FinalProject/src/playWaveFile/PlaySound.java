package playWaveFile;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * 
 * <Replace this with a short description of the class.>
 * 
 * @author Giulio
 */
public class PlaySound {

	private InputStream waveStream;
	private int frameSize;
	private long fileSize;

	private final int EXTERNAL_BUFFER_SIZE = 4096; // 128Kb

	public volatile boolean IS_PAUSED;
	public volatile boolean IS_STOPPED;
	public volatile long i = 0;

	public int numOfFrames;
	private AudioFormat audioFormat;
	private Info info;
	private AudioInputStream audioInputStream;

	/**
	 * CONSTRUCTOR
	 * 
	 * @param fileSize
	 * @throws PlayWaveException
	 */
	public PlaySound(InputStream waveStream, long fileSize)
			throws PlayWaveException {
		this.waveStream = waveStream;
		this.IS_PAUSED = false;
		this.IS_STOPPED = false;
		this.i = 0;
		this.fileSize = fileSize;

		try {
			this.audioInputStream = AudioSystem
					.getAudioInputStream(this.waveStream);
		} catch (UnsupportedAudioFileException e1) {
			throw new PlayWaveException(e1);
		} catch (IOException e1) {
			throw new PlayWaveException(e1);
		}

		// Obtain the information about the AudioInputStream
		this.audioFormat = audioInputStream.getFormat();
		this.frameSize = this.audioFormat.getFrameSize();
		this.numOfFrames = (int) (this.fileSize / frameSize);
		this.info = new Info(SourceDataLine.class, this.audioFormat);

	}

	public void play() throws PlayWaveException {

		// opens the audio channel
		SourceDataLine dataLine = null;
		try {
			dataLine = (SourceDataLine) AudioSystem.getLine(this.info);
			dataLine.open(this.audioFormat, EXTERNAL_BUFFER_SIZE);
		} catch (LineUnavailableException e1) {
			throw new PlayWaveException(e1);
		}

		// Starts the music :P
		dataLine.start();

		int readBytes = 0;
		byte[] audioBuffer = new byte[EXTERNAL_BUFFER_SIZE];

		try {
			while (readBytes != -1) {
				this.i = dataLine.getLongFramePosition();
				readBytes = this.audioInputStream.read(audioBuffer, 0,
						audioBuffer.length);
				if (readBytes >= 0) {
					dataLine.write(audioBuffer, 0, readBytes);
				}
			}
		} catch (IOException e1) {
			throw new PlayWaveException(e1);
		} finally {
			// plays what's left and and closes the audioChannel
			dataLine.drain();
			dataLine.close();
		}

	}

	public void resumeAudio() {
		IS_PAUSED = false;

	}

	public void stopAudio() {
		this.i = 0;
		IS_STOPPED = true;

	}

	public void PauseAudio() {
		IS_PAUSED = true;

	}
}
