package catkin.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

/**
 * 因为Bitmap没有实现序列化，所以不能直接在序列化类(MyBitmap)中使用
 * BytesBitmap用于实现Bitmap和byte[]间的相互转换
 * @author joran
 *
 */
public class BytesBitmap {
	  public static Bitmap getBitmap(byte[] data)
	  {
	  return  BitmapFactory.decodeByteArray(data, 0, data.length);
	  }
	 
	  public static byte[] getBytes(Bitmap bitmap) 
	  {
	  ByteArrayOutputStream baops = new ByteArrayOutputStream();
	  bitmap.compress(CompressFormat.PNG, 0, baops);
	  return baops.toByteArray();
	  }
 }
