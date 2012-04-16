package catkin.psketcher.eventsHandler;

import java.util.List;

import catkin.psketcher.PSketcherActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageSwitcher;

public class AdapterViewListener implements AdapterView.OnItemClickListener 
{
	
	private List<Integer> ImageList;
	private ImageSwitcher mSwitcher;
	private PSketcherActivity mActivity;
	
	public AdapterViewListener(PSketcherActivity activity)
	{
		mActivity=activity;
		ImageList=mActivity.resourceList;
		mSwitcher=mActivity.mSwitcher;
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
	{
		// TODO Auto-generated method stub
		/**
		 * 获取当前要显示的Image的路径
		 */
		mActivity.position = position;
		/**
		 * 设置当前要显示的Image的路径	 
		 */
		//mSwitcher.setImageURI(Uri.parse(mActivity.photoURL));
		 mSwitcher.setImageResource(ImageList.get(position));
		
	}

}
