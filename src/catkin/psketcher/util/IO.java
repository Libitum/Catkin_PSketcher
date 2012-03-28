package catkin.psketcher.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;



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

		File f = null;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			f = new File(Environment.getExternalStorageDirectory().toString()+path);//获取文件路径
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
		Sort sort=new Sort();
		Collections.sort(imageList, sort); 
		return imageList;
	}

	 public static void saveBitmap(Bitmap bitmap,String path,String name) throws IOException
	    {
		     Log.d("liuna","enter saveBitmap");
		     File fileDir=new File(path);
		     if(!fileDir.exists())
		     {
		    	 fileDir.mkdir();
		     }
		     Log.d("liuna","创建目录成功");
	    	 File f = new File(path+name);
	         f.createNewFile();
	         FileOutputStream fOut = null;
	         try {
	                 fOut = new FileOutputStream(f);
	         } catch (FileNotFoundException e) {
	                Log.d("liuna",e.toString());
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

class Sort implements Comparator<String>
{
	  public int compare(String o1, String o2) 
	  {
	    return -(o1.compareTo(o2));//按照长度比较
	  }
}
