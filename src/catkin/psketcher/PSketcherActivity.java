package catkin.psketcher;


import java.io.IOException;
import java.util.List;
import catkin.frame.FrameWork;
import catkin.frame.EnumClass.Device;
import catkin.psketcher.image.Sketcher;
import catkin.psketcher.serializable.SerialBitmap;
import catkin.util.BytesBitmap;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;
import android.widget.Gallery.LayoutParams;

public class PSketcherActivity extends Activity implements
AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {
    /** Called when the activity is first created. */
	/**
	 * 手机设备中图像列表
	 */
	private List<String> ImageList;
	private ImageSwitcher mSwitcher;
	private String photoURL;            //保存图片文件路径
	private Handler  handler;
	private int sign=1;  
	private Dialog mDialog;
	private Context mContext;
	private FrameWork Fw;
	
	 	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext=this;
        Fw=FrameWork.getInstance(mContext,this.toString());
        
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
					      Bitmap img=dealPicture(photoURL);
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
			handler = new Handler() {
			            @Override
			            public void handleMessage(Message msg) {
			            	Log.d("liuna","App Enter Message Handler");
			                switch (msg.what) {
			                case 0:
			                	try{
			                		
			                	mDialog.dismiss();
			                	String path=save(BytesBitmap.getBitmap((byte[])(msg.obj)), photoURL);
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
   
	/*
	 * 处理图片
	 */
  public Bitmap dealPicture(String s)
	{
		 Bitmap img = BitmapFactory.decodeFile(s);
		 return Sketcher.toSketcher(img);
	}
	
    public String save(Bitmap img,String s)
	{
		try
		{
			IO.saveBitmap(img,s+"Sketcher.png");
			return s+"Sketcher.png";
		}catch(IOException e)
		{
			Log.d("liuna",e.toString());
			return null;
		}
		
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
	 * (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		/**
		 * 获取当前要显示的Image的路径
		 */
		photoURL = ImageList.get(position-5);
		Log.i("A", String.valueOf(position));
		/**
		 * 设置当前要显示的Image的路径	 
		 */
		mSwitcher.setImageURI(Uri.parse(photoURL));
		
	}
}