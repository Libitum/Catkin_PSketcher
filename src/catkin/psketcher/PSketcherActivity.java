package catkin.psketcher;

import java.util.List;
import catkin.psketcher.massageListener.AdapterViewListener;
import catkin.psketcher.massageListener.ImageSwitcherListener;
import catkin.psketcher.util.IO;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

public class PSketcherActivity extends Activity implements ViewFactory 
{
    /** Called when the activity is first created. */
	/**
	 * 手机设备中图像列表
	 */
	public List<String> pathList;
	public List<Integer>resourceList;
	public ImageSwitcher mSwitcher;
	public Integer position;            //保存当前图片文件路径
	public Context mContext;
		 	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext=this;
        /*
         * 获取mSwitcher变量以及它的设置参数
         */      
        mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		mSwitcher.setFactory(this); 
	    mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right));
		
		resourceList=IO.getImagesFromResource();//从资源中获得图片	
		Log.d("liuna","从资源中获得图片完成");
		/*
		 * 获取gallery并设置适配器
		 */
		Gallery g = (Gallery) findViewById(R.id.gallery);
		if(resourceList== null || resourceList.size() == 0)
			return;
		g.setAdapter(new ImageAdapter(this, resourceList));
		
		/*
		 * 监听adaperView接收到的消息
		 */
		g.setOnItemClickListener(new AdapterViewListener(this));
		
		/*
		 * 监听imageSwitcher的单击事件
		 */
		mSwitcher.setImageResource(resourceList.get(0));
		mSwitcher.setOnClickListener(new ImageSwitcherListener(this));
    }


    @Override
	public View makeView() 
    {
		// TODO Auto-generated method stub
		ImageView i = new ImageView(mContext);

		i.setBackgroundColor(0xFF000000);
		
		i.setScaleType(ImageView.ScaleType.FIT_XY);

		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
	}
       
}