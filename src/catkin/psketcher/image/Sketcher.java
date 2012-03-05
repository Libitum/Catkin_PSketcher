package catkin.psketcher.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

/**
 * @author Libitum(libitum@about.com)
 *
 */
public class Sketcher {
	public static Bitmap toSketcher(final Bitmap img){
		int w = img.getWidth();
		int h = img.getHeight();
		Bitmap bgImg = Sketcher.origin2Gray(img);
		Bitmap fgImg = Sketcher.gray2Gauss(bgImg);
		for(int i=0; i<w; i++){
			for(int j=0; j<h; j++){
				int color;
				if(fgImg.getPixel(i, j) > 255){
					color = 255;
				}
				else{
					color = Math.min(bgImg.getPixel(i, j) * 255 
							/ (255 - fgImg.getPixel(i, j)),255);
				}
				img.setPixel(i, j, color);
			}
		}
		
		return img;
	}
	/**
	 * @param img
	 * @return
	 */
	private static Bitmap origin2Gray(final Bitmap img){
		int w = img.getWidth();
		int h = img.getHeight();
		Bitmap grayImg = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		for(int i=0; i<w; i++){
			for(int j=0; j<h; j++){
				int color = img.getPixel(i, j);
				float grayColor = Color.blue(color) * 0.114f + Color.green(color) * 0.587f
						+ Color.red(color) * 0.299f;
				grayImg.setPixel(i, j, (int)grayColor);
			}
		}
		return grayImg;
	}
	

	/**
	 * @param img
	 * @param k
	 * @return
	 */
	private static Bitmap gray2Gauss(final Bitmap img, final int k){
		int w = img.getWidth();
		int h = img.getHeight();
		Bitmap gaussImg = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		//计算高斯核
		float sigma = (k/2.0f-1f)*0.3f + 0.8f;
		float[][] gaussKernal = new float[2*k+1][2*k+1];
		for(int i=0; i<=2*k+1; i++){
			for(int j=0; j<2*k+1; j++){
				float xDistance = (i-k) * (i-k);
				float yDistance = (j-k) * (j-k);
				gaussKernal[i][j] = (float)Math.exp(-(xDistance + yDistance)
						/ (2f*sigma*sigma)) / (2f*sigma*sigma*(float)Math.PI);
			}
		}
		//进行高斯滤波
		for(int i=0; i<w; i++){
			for(int j=0; j<h; j++){
				float color = 0f;
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
						color += (255f - img.getPixel(i-k+ii, j-k+jj)) * gaussKernal[ii][jj];
					}
				}
				gaussImg.setPixel(i, j, (int)color);
			}
		}
		
		return gaussImg;
	}
	
	private static Bitmap gray2Gauss(final Bitmap img){
		return Sketcher.gray2Gauss(img, 5);
	}
	
	
}
