package catkin.psketcher;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
/**
 * ****************************************************************
 * 文件名称	: ImageAdapter.java
 * 创建时间	: 2012-3-5 下午05:27:23
 * 文件描述	: 自定义Gallery适配器，
 *****************************************************************
 */
public class ImageAdapter extends BaseAdapter {
	//private static final int gHeight = 0;
	/**
	 * 图像路径列表
	 */
	private List<Integer> lis;
	private Context mContext;
	private int gWidth;//屏幕的宽
	private int gHeight; //gallery的高
	
	public ImageAdapter(Context c,  List<Integer> li,int x,int y) 
	{
		mContext = c;
		lis = li;
		gWidth=x;
		gHeight=y;
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
		 
		 imageView.setScaleType(ScaleType.FIT_XY);
		 imageView.setLayoutParams(new Gallery.LayoutParams(
			     (int)(gWidth/6), LayoutParams.FILL_PARENT));//设置图片显示比例
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