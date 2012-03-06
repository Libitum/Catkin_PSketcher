package catkin.psketcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import catkin.psketcher.IO.IO;
import catkin.psketcher.image.Sketcher;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery.LayoutParams;

public class PSketcherActivity extends Activity implements
AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {
    /** Called when the activity is first created. */
	/**
	 * 手机设备中图像列表
	 */
	private List<String> ImageList;
	private ImageSwitcher mSwitcher;
	String photoURL;            //保存图片文件
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /*
         * 获取mSwitcher变量以及它的设置参数
         */
        mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		mSwitcher.setFactory(this); 
	    mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right));
		
		/*
		 * 从sd卡指定路径获取图片
		 */
		
		ImageList = IO.getImagesFromSD("/sdcard/");
		
		/*
		 * 获取gallery并设置适配器
		 */
		Gallery g = (Gallery) findViewById(R.id.gallery);
		if(ImageList == null || ImageList.size() == 0)
			return;
		g.setAdapter(new ImageAdapter(this, ImageList));
		g.setOnItemSelectedListener(this);
		
		/*
		 * 响应单击控件事件    测试接口
		 */
	
		mSwitcher.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//arg0.setDrawingCacheEnabled(true);
				//Bitmap img=arg0.getDrawingCache(false);//如果不设置为true 则图片和屏幕的显示大小一样
			    Bitmap img = BitmapFactory.decodeFile(photoURL);
				Bitmap img2=Sketcher.toSketcher(img);
				
				try {
					IO.saveBitmap(img2,photoURL+"Sketcher.png");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("liuna",e.toString());
				}
				mSwitcher.setImageURI(Uri.parse(photoURL+"Sketcher.png"));
			}
			
		});
		
		
		
    }
    
    
	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		ImageView i = new ImageView(this);

		i.setBackgroundColor(0xFF000000);

		i.setScaleType(ImageView.ScaleType.FIT_XY);

		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		/**
		 * 获取当前要显示的Image的路径
		 */
		photoURL = ImageList.get(position);
		Log.i("A", String.valueOf(position));
		/**
		 * 设置当前要显示的Image的路径	 
		 */
		mSwitcher.setImageURI(Uri.parse(photoURL));
			
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}