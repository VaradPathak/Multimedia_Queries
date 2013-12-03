package playVideo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
			JFrame frame = new JFrame("Multimedia Player");
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

			ExtractChroma extract = new ExtractChroma();

			for (int i = 0; i < iteration - 2; i++) {
				int ind = (int) (i * frameSize);
				byte[] extractFrame = new byte[(int) size];

				System.arraycopy(bytes, ind, extractFrame, 0, (int) frameSize);
				System.arraycopy(bytes, ind + height * width, extractFrame, 0,
						(int) frameSize + height * width);
				System.arraycopy(bytes, ind + height * width, extractFrame, 0,
						(int) frameSize + height * width * 2);

				this.featureList.add(extract.extractChroma(extractFrame));

				for (int y = 0; y < height; y++) {

					for (int x = 0; x < width; x++) {
						byte r = bytes[ind];
						byte g = bytes[ind + height * width];
						byte b = bytes[ind + height * width * 2];

						// To HSB

						float[] hsb = Color.RGBtoHSB(r, g, b, null);

						float hue = hsb[0];

						// System.out.println("RGB [" + r + "," + g + "," + b
						// + "] converted to HSB [" + hue + ","
						// + saturation + "," + brightness + "]");

						// End To HSB

						int pix = 0xff000000 | ((r & 0xff) << 16)
								| ((g & 0xff) << 8) | (b & 0xff);
						currentFrame.setRGB(x, y, pix);
						ind++;
					}
				}

				// Use a label to display the image
				JLabel label = new JLabel(new ImageIcon(currentFrame));
				// frame.getContentPane().add(label);

				JPanel pane = new JPanel(new BorderLayout());
				// We always have two UI elements (columns) and we have three
				// rows
				int numberOfRows = 2;
				int numberOfColumns = 4;
				// pane.setLayout(new GridLayout(numberOfRows,
				// numberOfColumns));

				// create and attach buttons
				// create a label and add it to the main window
				pane.add(label, BorderLayout.NORTH);

				JLabel firstNamelabel = new JLabel(" Firstname: ");
				pane.add(firstNamelabel, BorderLayout.LINE_START);
				pane.add(new JTextField(), BorderLayout.LINE_END);

				JLabel lastNamelabel = new JLabel(" Lastname: ");

				pane.add(lastNamelabel, BorderLayout.LINE_START);
				pane.add(new JTextField(), BorderLayout.LINE_END);

				JButton sayHello = new JButton("Say something");
				pane.add(sayHello, BorderLayout.LINE_START);

				pane.add(new JCheckBox("Nice"), BorderLayout.LINE_END);

				frame.getContentPane().add(pane);
				frame.pack();
				frame.setVisible(true);
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
