package catkin.psketcher.massageListener;

import java.io.IOException;

import catkin.frame.FrameWork;
import catkin.frame.EnumClass.Device;
import catkin.psketcher.PSketcherActivity;
import catkin.psketcher.R;
import catkin.psketcher.image.Sketcher;
import catkin.psketcher.util.BytesBitmap;
import catkin.psketcher.util.IO;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

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
				      Bitmap img=dealPicture(mActivity.photoURL);
				      byte[]BitmapBytes=BytesBitmap.getBytes(img);
				      Fw.putValue("BitmapBytes",BitmapBytes);
					}
				   Message msg = new Message();
                   msg.obj = Fw.getValue("BitmapBytes");//test
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
		                	String path=save(BytesBitmap.getBitmap((byte[])(msg.obj)), mActivity.photoURL, "/sdcard/pic/sketcherPic/",null);
		                	Log.d("liuna","path");
		                	mSwitcher.setImageURI(Uri.parse(path));   
		                	
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
   private Bitmap dealPicture(String s)
	{
		 Bitmap img = BitmapFactory.decodeFile(s);
		 return Sketcher.toSketcher(img);
	}
	
   private String save(Bitmap img,String src,String destDir,String name)
	{
		try
		{
			Log.d("liuna","src:"+src);
			int P1=0,P2=0,i=0;
			int state=0;
			String destPath=null;
			if(name==null)
			{
				for(i=src.length()-1;i>0;i--)
				{
					if(state==0&&src.charAt(i)=='.')
						{
						P2=i;
						state=1;
						continue;
						}
					if(state==1&&src.charAt(i)=='/')
						{
						P1=i;
						break;
						}
				 }
		         name=src.substring(P1+1,P2)+"Sketcher.png";
			}
			destPath=destDir+name;
			IO.saveBitmap(img,destDir,name);
			return destPath;
	
		}catch(IOException e)
		{
			Log.d("liuna",e.toString());
			return null;
		}
		
	}
	  

}
