package catkin.psketcher.IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Environment;



public class IO {
	
	public IO() {
		// TODO Auto-generated constructor stub
	}

	private static boolean isImageFile(String fName) {
		boolean re;
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/**
		 * 依据文件扩展名判断是否为图像文件
		 */
		if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			re = true;
		} else {
			re = false;
		}
		return re;
	}
	
	public static  List<String> getImagesFromSD(String path) {
		List<String> imageList = new ArrayList<String>();

		File f = new File(path);
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			f = new File(Environment.getExternalStorageDirectory().toString());
		}
		else
		{
			return imageList;
		}

		File[] files = f.listFiles();

		if(files == null || files.length == 0)
			return imageList;
		/**
		 * 将所有图像文件的路径存入ArrayList列表
		 */
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (isImageFile(file.getPath()))
				imageList.add(file.getPath());
		}
		return imageList;
	}

	 public static void saveBitmap(Bitmap bitmap,String path) throws IOException
	    {
	    	 File f = new File(path);
	         f.createNewFile();
	         FileOutputStream fOut = null;
	         try {
	                 fOut = new FileOutputStream(f);
	         } catch (FileNotFoundException e) {
	                 e.printStackTrace();
	         }
	         bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
	         try {
	                 fOut.flush();
	         } catch (IOException e) {
	                 e.printStackTrace();
	         }
	         try {
	                 fOut.close();
	         } catch (IOException e) {
	                 e.printStackTrace();
	         }

	    }


}
