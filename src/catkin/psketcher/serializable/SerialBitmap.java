package catkin.psketcher.serializable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

 /**
 * SerialBitmap将bitmap序列化
 * 其中包含了通过BytesBitmap类得到的Bitmap中数据的数组
 * 和一个保存位图的名字的字符串，用于标识图片
 * @author joran
 *
 */

 public class SerialBitmap implements Serializable
 {
	 private byte[] bitmapBytes = null;
	 private String name = null;
	
	 public SerialBitmap(Bitmap bitmap, String name) 
	 {
		 
	 // TODO Auto-generated constructor stub
	  ByteArrayOutputStream baops = new ByteArrayOutputStream();
	  bitmap.compress(CompressFormat.PNG, 0, baops); 
	  this.bitmapBytes = baops.toByteArray();
	  this.name = name;
	  
	 }
	
	 public Bitmap getBitmap()
	 {
		return  BitmapFactory.decodeByteArray(this.bitmapBytes, 0, this.bitmapBytes.length);
	 }
	
	 public String getName() 
	 {
	    return this.name;
	 }

	}
 
 
 
 
 




