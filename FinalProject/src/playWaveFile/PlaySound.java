package playWaveFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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

	public String fileName;

	private final int EXTERNAL_BUFFER_SIZE = 3550; // 3Kb

	public volatile boolean IS_PAUSED;
	public volatile boolean IS_STOPPED;
	public volatile long i = -1;

	public SourceDataLine dataLine = null;
	public PlaySound(String filename) {
		this.fileName = filename;
		this.IS_PAUSED = false;
		this.IS_STOPPED = false;
		this.i = -1;
	}

	public void play() throws PlayWaveException, InterruptedException {
		// opens the inputStream
		AudioInputStream audioInputStream = null;
		// opens the audio channel
		

		try {
			File audioFile = new File(this.fileName);
			int fileSize = (int) audioFile.length();
			FileInputStream inputStream = new FileInputStream(this.fileName);
			InputStream waveStream = new BufferedInputStream(inputStream);
			audioInputStream = AudioSystem.getAudioInputStream(waveStream);

			// Obtain the information about the AudioInputStream
			AudioFormat audioFormat = audioInputStream.getFormat();
			Info info = new Info(SourceDataLine.class, audioFormat);
			byte[] tempBuffer = new byte[fileSize];
			audioInputStream.read(tempBuffer, 0, tempBuffer.length);

			dataLine = (SourceDataLine) AudioSystem.getLine(info);
			dataLine.open(audioFormat, EXTERNAL_BUFFER_SIZE);

			// Starts the music :P
			dataLine.start();

			// int readBytes = 0;
			// byte[] audioBuffer = new byte[EXTERNAL_BUFFER_SIZE];
			while(i<0)
			{
				//System.out.println("waiting.......");
			}
			while ((tempBuffer.length - (this.i * EXTERNAL_BUFFER_SIZE)) >= EXTERNAL_BUFFER_SIZE) {
				if (IS_STOPPED) {
					this.i = 0;
					break;
				}
				if (IS_PAUSED == false) {
					int ind = (int) (this.i * EXTERNAL_BUFFER_SIZE);
					dataLine.write(tempBuffer, ind, EXTERNAL_BUFFER_SIZE);
					//dataLine.drain();
					//TimeUnit.MILLISECONDS.sleep(24);
					//System.out.println(i);
					//this.i++;
					
				}
				
			}
		} catch (IOException e1) {
			throw new PlayWaveException(e1);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} finally {
			// plays what's left and and closes the audioChannel
			dataLine.drain();
			dataLine.close();
			try {
				audioInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
