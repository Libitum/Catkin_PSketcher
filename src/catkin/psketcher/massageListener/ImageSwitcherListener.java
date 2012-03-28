package catkin.psketcher.massageListener;

import catkin.frame.FrameWork;
import catkin.frame.EnumClass.Device;
import catkin.psketcher.PSketcherActivity;
import catkin.psketcher.R;
import catkin.psketcher.image.Sketcher;
import catkin.psketcher.util.BytesBitmap;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageSwitcher;


public class ImageSwitcherListener implements OnClickListener
{
	
	private int sign=1;
	private FrameWork Fw;
	private Handler  handler;
	private Dialog mDialog;
	private Context mContext;
	private ImageSwitcher mSwitcher;
	private PSketcherActivity mActivity;
	
	public ImageSwitcherListener(PSketcherActivity mainActivity)
	{
	  mActivity=mainActivity;
	  mContext=mActivity.mContext;
	  Fw=FrameWork.getInstance(mContext,this.toString());
	  mSwitcher=mActivity.mSwitcher;
	}

	@Override
	public void onClick(View arg0) 
	{
		// TODO Auto-generated method stub
		if(sign==0)
		{
			return;
		}
		sign=0;
		/*
		 * 显示等待图片
		 */
		showWaitingPicture(mContext, R.layout.loading_process_dialog_color);
		
		/*
		 * 创建一个线程用来处理图片
		 */
		Thread sketcherThread = new Thread()
			{
			
				public void run()
				{
				    Log.d("liuna", "Enter Thread");
					if(Fw.judge(Device.CLOUD))
					{	
					 Log.d("liuna","positon:"+mActivity.position);
				      Bitmap img=dealPicture(mActivity.position);
				      byte[]BitmapBytes=BytesBitmap.getBytes(img);
				      Fw.putValue("BitmapBytes"+mActivity.position,BitmapBytes);
					}
				   Message msg = new Message();
                   msg.obj = Fw.getValue("BitmapBytes"+mActivity.position);
                   msg.what = 0;
                   handler.sendMessage(msg);
				}
			};
		sketcherThread.start();
		
		/*
		 * 处理线程发过来的消息	，显示处理后的图片
		 */
		handler = new Handler() 
		{
		            @Override
		            public void handleMessage(Message msg) 
		            {
		            	Log.d("liuna","App Enter Message Handler");
		                switch (msg.what) 
		                {
		                 case 0:
		                	try
		                	{	
		                	
		                	mDialog.dismiss();
		                	Log.d("liuna","path"); 	  
		                	BitmapDrawable drawable=new BitmapDrawable(BytesBitmap.getBitmap((byte[])(msg.obj))); 
		                	mSwitcher.setImageDrawable(drawable);
		                	
		                	}
		                	catch(Exception e)
		                	{
		                		Log.d("liuna","Handler："+e.toString());
		                 	}
		                	
		                }
		            }

		        };
		sign=1;
		}		
	
	/*
	 * 显示等待动画
	 */
	public void showWaitingPicture(Context mContext, int layout)
	{
	    OnKeyListener keyListener = new OnKeyListener()
	    {
	        @Override
	        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
	        {
	           return false;
	        }
	    };
	    mDialog = new AlertDialog.Builder(mContext).create();
	    mDialog.setOnKeyListener(keyListener);
	    mDialog.show();
	    mDialog.setContentView(layout);
	}
	
	/*
	 * 处理图片
	 */
   private Bitmap dealPicture(int position)
	{
		 //Bitmap img = BitmapFactory.decodeFile(s);
	     Log.d("liuna","position:"+mActivity.position);
	     Bitmap img=BitmapFactory.decodeResource(mContext.getResources(),mActivity.resourceList.get(mActivity.position) );
		 return Sketcher.toSketcher(img);	 
	}
	
}
