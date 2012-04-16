package catkin.psketcher;

import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
/**
 * ****************************************************************
 * 文件名称	: ImageAdapter.java
 * 创建时间	: 2012-3-5 下午05:27:23
 * 文件描述	: 自定义Gallery适配器，
 *****************************************************************
 */
public class ImageAdapter extends BaseAdapter {
	/**
	 * 图像路径列表
	 */
	private List<Integer> lis;
	private Context mContext;
	
	public ImageAdapter(Context c,  List<Integer> li) {
		mContext = c;
		lis = li;
	}
	/*
	 * 绘制缩略图
	 */
	/*public ImageView createThumbFromSdcard(int position) {
         String filePath=lis.get(position);
		 Bitmap originalImage = BitmapFactory.decodeFile(filePath);
		 ImageView imageView = new ImageView(mContext);
		 imageView.setImageBitmap(originalImage);
		 imageView.setAdjustViewBounds(true);
		 imageView.setLayoutParams(new Gallery.LayoutParams(
		     LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));	 
		return imageView;
	}*/
	
	public ImageView createThumbFromResource(int position) {

		 ImageView imageView = new ImageView(mContext);
		 imageView.setImageResource(lis.get(position));
		 imageView.setAdjustViewBounds(true);
		 imageView.setLayoutParams(new Gallery.LayoutParams(
		     LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		 
		return imageView;
	}
	
	@Override
	public int getCount() {
		return lis.size();
	}
	@Override
	public Object getItem(int position) {
		return position;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createThumbFromResource(position);
	}
}