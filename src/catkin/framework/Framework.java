package catkin.framework;

import catkin.framework.buffer.BufferData;
import catkin.framework.net.SocketHandler;
import catkin.framework.net.StreamData;
import catkin.framework.util.EnumClass.Code;
import catkin.framework.util.EnumClass.Device;
import catkin.framework.util.EnumClass.Method;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Framework extends BaseFramework{ 

	private Context mContext;
	private Device appDevice;
	private String appId=null;
	private SocketHandler  socketHandler;
	boolean IsBusExist=false;//标志这个应用是否要迁移执行
	
	/*
	 * 单例机制,构造函数的参数是调用框架的应用的上下文环境和类字符串
	 */
	private Framework(Context c,Object app)
	{ 
		super();	
		mContext=c;
		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE); 
		String deviceId=tm.getDeviceId();
		Log.d("liuna","deviceId:"+deviceId);
		if(deviceId==null)
		{
			appDevice=Device.CLOUD;
		}
		else
			appDevice=Device.MOBILE;
		socketHandler=new SocketHandler(appDevice);
		appId=Integer.valueOf(app.toString().hashCode()).toString();
		Log.d("liuna","app:"+app);
		Log.d("liuna","appId:"+appId);	
			
		super.setSocketHandler(socketHandler);	
		IsBusExist=isBusExist();
	}	
	
	private static Framework fw=null;
	
    public synchronized static Framework getInstance(Context c,Object s)
	{
    	
		if(fw==null)
		{
			fw=new Framework(c,s);
		}
		return fw;		
	}
	
	/*
	 * 判断函数是否应该在调用该函数的设备上运行,返回true表示在发起的这个应用中执行
	 */
	public  boolean judge(Device PreferToRun )
	{
		boolean IsRunMigrate=isContextFit(appDevice); //在每段代码要迁移前，都判断一下环境是否合适，最麻烦的就是出现有时合适有时不合适的情况
		Log.d("liuna","IsBusExist="+IsBusExist);
		Log.d("liuna","IsRunMigrate="+IsRunMigrate);
		
		if(PreferToRun==appDevice||!IsBusExist||!IsRunMigrate)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/*
	 * 将数据放回总线，参数是要放回总线的数据，以及数据的描述信息
	 */
	
	public  boolean putValue(String varName,Object varValue) 
	{   
		Log.d("liuna","Enter putValue");
		BufferData bufferData=new BufferData(varName,varValue);
		StreamData streamData=null;
		if(IsBusExist)
		{
		    streamData=new StreamData(Method.PUT,appId);
			streamData.addValue(varName, varValue);
		}
		return super.put(bufferData,streamData);
		
	}

	/*
	 * 从总线获取数据，参数是要获取数据的信息 
	 */
	public  Object getValue(String varName) 
	{
		Log.d("liuna","Enter getValue");
		BufferData bufferData=new BufferData(varName);
		StreamData streamData=null;
		if(IsBusExist)
		{
			streamData=new StreamData(Method.GET,appId);
			streamData.addKey(varName);
		}
		Object[]O= super.get(bufferData, streamData, 1);
		return O[0];
		
	}
	
	public  boolean putValue(String[] varName,Object[] varValue) 
	{   
		Log.d("liuna","Enter putValue");
		BufferData bufferData=new BufferData(varName,varValue);
		StreamData streamData=null;
		if(IsBusExist)
		{
		    streamData=new StreamData(Method.PUT,appId);
			streamData.addValue(varName, varValue);
		}
		return super.put(bufferData,streamData);
	}

	/*
	 * 从总线获取数据，参数是要获取数据的信息 
	 */
	public  Object[] getValue(String []varName) 
	{
		Log.d("liuna","Enter getValue");
		BufferData bufferData=new BufferData(varName);
		StreamData streamData=null;
		if(IsBusExist)
		{
			streamData=new StreamData(Method.GET,appId);
			streamData.addKey(varName);
		}
		Object[]O= super.get(bufferData, streamData, varName.length);
		return O;	
	}
	
	public boolean putFile(String pathName,String pathUrl)
	{
		Log.d("liuna","Enter putFile");
		BufferData bufferData=new BufferData(pathName,pathUrl);
		StreamData streamData=null;
		if(IsBusExist)
		{
			streamData=new StreamData(Method.PUT,appId);
			if(appDevice==Device.CLOUD)
				streamData.addFileInfo(Device.CLOUD, Device.MOBILE,pathUrl);
			else
				streamData.addFileInfo( Device.MOBILE,Device.CLOUD,pathUrl);
			streamData.addValue(pathName, (Object)pathUrl);
		}
		return super.put(bufferData,streamData);
		
	}
	
	public Object getFile(String pathName)
	{
		
		Log.d("liuna","Enter getValue");
		BufferData bufferData=new BufferData(pathName);
		StreamData streamData=null;
		if(IsBusExist)
		{
			streamData=new StreamData(Method.GET,appId);
			streamData.addKey(pathName);
		}
		Object[]O= super.get(bufferData, streamData, 1);
		return O[0];
		

	}
	
    public boolean putFile(String []pathName,String[]pathUrl)
 	{
    	Log.d("liuna","Enter putFile");
		BufferData bufferData=new BufferData(pathName,pathUrl);
		StreamData streamData=null;
		if(IsBusExist)
		{
			streamData=new StreamData(Method.PUT,appId);
			if(appDevice==Device.CLOUD)
				streamData.addFileInfo(Device.CLOUD, Device.MOBILE,pathUrl);
			else
				streamData.addFileInfo( Device.MOBILE,Device.CLOUD,pathUrl);
			streamData.addValue(pathName, (Object[])pathUrl);
		}
		return super.put(bufferData,streamData);
  	
 	}
   
	public Object[] getFile(String []pathName)
    {
		Log.d("liuna","Enter getValue");
		BufferData bufferData=new BufferData(pathName);
		StreamData streamData=null;
		if(IsBusExist)
		{
			streamData=new StreamData(Method.GET,appId);
			streamData.addKey(pathName);
		}
		Object[]O= super.get(bufferData, streamData, 1);
		return O;
		
		
    }
	
	boolean isBusExist()
	{
		return socketHandler.testConnect();
	}
	 private boolean isContextFit(Device appDevice)
	 {
		 return true;
		   
	 }
}


