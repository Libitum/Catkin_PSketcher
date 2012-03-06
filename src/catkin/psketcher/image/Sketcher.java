package catkin.psketcher.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

/**
 * This is a static class to convert the origin photo to sketcher.
 * @author Libitum(libitum@about.me)
 */
public class Sketcher {
	/**
	 * Convert the image to sketcher
	 * @param img Bitmap of origin image
	 * @return Bitmap of sketcher
	 */
	public static Bitmap toSketcher(final Bitmap img){
		int w = img.getWidth();
		int h = img.getHeight();
		Bitmap bgImg = Sketcher.origin2Gray(img);
		Bitmap fgImg = Sketcher.gray2Gauss(bgImg);
		for(int i=0; i<w; i++){
			for(int j=0; j<h; j++){
				int gray;
				gray = Math.min(Color.red(bgImg.getPixel(i, j)) * 255 
						/ (255 - Color.red(fgImg.getPixel(i, j))),255);
				img.setPixel(i, j, Color.rgb(gray, gray, gray));
			}
		}
		
		return img;
	}
	/**
	 * Convert the origin photo to gray image.
	 * @param img Bitmap of origin photo
	 * @return bitmap of gray image
	 */
	private static Bitmap origin2Gray(final Bitmap img){
		int w = img.getWidth();
		int h = img.getHeight();
		Bitmap grayImg = Bitmap.createBitmap(w, h, Config.RGB_565);
		for(int i=0; i<w; i++){
			for(int j=0; j<h; j++){
				int color = img.getPixel(i, j);
				int gray = (int)(Color.red(color) * 0.299f + Color.green(color) 
						* 0.587f + Color.blue(color) * 0.114f);
				int grayColor = Color.rgb(gray, gray, gray);
				grayImg.setPixel(i, j, grayColor);
			}
		}
		return grayImg;
	}
	

	/**
	 * Convert the gray image to Gauss image
	 * @param img Bitmap of gray image
	 * @param k Gauss radius.
	 * @return Bitmap of Gauss image
	 */
	private static Bitmap gray2Gauss(final Bitmap img, final int k){
		int w = img.getWidth();
		int h = img.getHeight();
		Bitmap gaussImg = Bitmap.createBitmap(w, h, Config.RGB_565);
		//calculate gauss kernel
		float sigma = (k/2.0f-1f)*0.3f + 0.8f;
		float[][] gaussKernel = new float[2*k+1][2*k+1];
		for(int i=0; i<=2*k+1; i++){
			for(int j=0; j<2*k+1; j++){
				float xDistance = (i-k) * (i-k);
				float yDistance = (j-k) * (j-k);
				gaussKernel[i][j] = (float)Math.exp(-(xDistance + yDistance)
						/ (2f*sigma*sigma)) / (2f*sigma*sigma*(float)Math.PI);
			}
		}
		//do gauss filter
		for(int i=0; i<w; i++){
			for(int j=0; j<h; j++){
				float gray = 0f;
				for(int ii=0; ii<2*k+1; ii++){
					if(i-k+ii<0){
						ii = k - i;
					}
					else if(i-k+ii>=w){
						break;
					}
					for(int jj=0; jj<2*k+1; jj++){
						if(j-k+jj<0){
							jj = k - j;
						}
						else if(j-k+jj>=h){
							break;
						}
						int c = img.getPixel(i-k+ii, j-k+jj);
						gray += (255f - Color.red(c)) * gaussKernel[ii][jj];
					}
				}
				gaussImg.setPixel(i, j, Color.rgb((int)gray, (int)gray, (int)gray));
			}
		}
		
		return gaussImg;
	}
	
	/**
	 * @param img Bitmap of gray image
	 * @return Bitmap of gauss image with gauss radius 5
	 */
	private static Bitmap gray2Gauss(final Bitmap img){
		return Sketcher.gray2Gauss(img, 5);
	}
	
	
}
