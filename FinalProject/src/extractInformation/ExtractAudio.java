package extractInformation;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import playWaveFile.PlayWaveException;

import com.musicg.wave.Wave;
import com.musicg.wave.extension.Spectrogram;

/**
 * @author Varad
 * 
 */
public class ExtractAudio {
	String filename;
	private InputStream waveStream;

	private final int EXTERNAL_BUFFER_SIZE = 512; // 128Kb

	public ExtractAudio(String audioFileName) {
		this.filename = audioFileName;
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(this.filename);
			waveStream = new BufferedInputStream(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void extractAudio_old(List<double[]> audioFeatureList) {
		// create a wave object
		Wave wave = new Wave(filename);

		// change the spectrogram representation
		int fftSampleSize = 128;
		int overlapFactor = 0;
		Spectrogram spect = new Spectrogram(wave, fftSampleSize, overlapFactor);
		for (double[] elem : spect.getNormalizedSpectrogramData()) {
			audioFeatureList.add(elem);
		}
	}

	public void extractAudio(List<Double[]> audioFeatureList)
			throws PlayWaveException {

		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(this.waveStream);
		} catch (UnsupportedAudioFileException e1) {
			throw new PlayWaveException(e1);
		} catch (IOException e1) {
			throw new PlayWaveException(e1);
		}

		int readBytes = 0;
		byte[] audioBuffer = new byte[this.EXTERNAL_BUFFER_SIZE];

		try {
			int k = 0;
			while (readBytes != -1) {
				readBytes = audioInputStream.read(audioBuffer, 0,
						audioBuffer.length);
				if (readBytes >= 0 && k % 8 == 0) {
					Complex[] complexData = new Complex[audioBuffer.length];
					for (int i = 0; i < complexData.length; i++) {
						complexData[i] = new Complex(audioBuffer[i], 0);
					}
					Complex[] fftResult = FFT.fft(complexData);
					Double result[] = new Double[fftResult.length / 16];

					for (int i = 0, j = 0; i < fftResult.length; i += 16, j++) {
						result[j] = Math.sqrt((fftResult[i].re() * fftResult[i]
								.re())
								+ (fftResult[i].im() * fftResult[i].im()));
						result[j] /= 10000;
					}
					audioFeatureList.add(result);
				}
				k++;
			}
		} catch (IOException e1) {
			throw new PlayWaveException(e1);
		}
	}

}
