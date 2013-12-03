/**
 * 
 */
package extractInformation;

import java.awt.Color;

/**
 * @author Varad
 * 
 */
public class ExtractChroma {

	int macroBlockDimension = 32;

	/**
	 * We divide the image in 32 x 32 micro-blocks and
	 * average the Hue Value for that block to extract
	 * only the chroma component of the frame/image. 
	 * @return result Average Hue Of each micro-block
	 * @param input A frame byte array
	 */
	public Double[] extractChroma(byte[] input) {
		int height = 288;
		int width = 352;
		Double[] result = new Double[25];
		int arrayInd = 0;

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				int ind = ((((j * 56) + 11) * width) + ((i * 70) + 19));
				result[arrayInd] = 0.0;
				for (int y = 0; y < macroBlockDimension; y++) {
					for (int x = 0; x < macroBlockDimension; x++) {

						byte r = input[ind + x + macroBlockDimension * y];
						byte g = input[ind + height * width + x
								+ macroBlockDimension * y];
						byte b = input[ind + height * width * 2 + x
								+ macroBlockDimension * y];

						/*if (j == 0 && i == 1 && x == 0 && y == 0) {
							System.out
									.println((ind + x + width * y)
											+ ", "
											+ (ind + height * width + x + macroBlockDimension
													* y)
											+ ", "
											+ (ind + height * width * 2 + x + macroBlockDimension
													* y));
						}*/
						// To HSB

						float[] hsb = Color.RGBtoHSB(r, g, b, null);
						result[arrayInd] += (double) hsb[0];
					}
				}
				result[arrayInd] /= 25;
				arrayInd += 1;
			}
		}

		return result;
	}
}
