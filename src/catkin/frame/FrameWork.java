package catkin.frame;

import catkin.frame.EnumClass.Code;
import catkin.frame.EnumClass.Device;
import catkin.frame.EnumClass.Method;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class FrameWork { 

	private Context mContext;
	private String deviceId;
	private Device appDevice;
	private String appId="5";
	private SocketHandler proPort;
	
	/*
	 * 单例机制,构造函数的参数是调用框架的应用的上下文环境和类字符串
	 */
	private FrameWork(Context c,String appString)
	{ 
		
		
		mContext=c;
		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE); 
		deviceId=tm.getDeviceId();
		Log.d("liuna","deviceId:"+deviceId);
		if(deviceId==null)
		{
			appDevice=Device.CLOUD;
		}
		else
			appDevice=Device.MOBILE;
		
		//appId=String.valueOf(appString.hashCode());
		proPort=new SocketHandler(appDevice);
		
	}	
	
	private static FrameWork fw=null;
	
    public static FrameWork getInstance(Context c,String s)
	{
    	
		if(fw==null)
		{
			fw=new FrameWork(c,s);
		}
		return fw;	
		
	}
	
	/*
	 * 判断函数是否应该在调用该函数的设备上运行,返回true表示在发起的这个应用中执行
	 */
	public  boolean judge(Device PreferToRun )
	{
		
		if(PreferToRun==appDevice||!isContextFit(appDevice))
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
		try{
		
			DataStream value=new DataStream(Method.PUT,appId);
			value.addValue(varName, varValue);
			DataStream result=proPort.processPort(value);
			String code= result.getCode();
			if(code.equals(Code.ACCEPTED.toString()))
			{
				Log.d("liuna","putValue success");
				return true;
			}
			else
			{
			Log.d("liuna","putValue Failed ,putValue的返回值是："+code);
			return false;
			}
			
		}catch(Exception e)
		{
			Log.d("liuna",e.toString());
			return false;
		}
	}

	/*
	 * 从总线获取数据，参数是要获取数据的信息 
	 */
	public  Object getValue(String varName) 
	{
		Log.d("liuna","Enter getValue");
		try
		{
		DataStream value=new DataStream(Method.GET,appId);
		value.addKey(varName);
		
		int ConnectTimes=10;
		long interval=3000;//3毫秒
		DataStream  result=proPort.processPort(value);
		String code= result.getCode();
		while((!code.equals(Code.OK.toString()))&&ConnectTimes-->0)
		{
			Log.d("liuna","ConnectTimes==:"+ConnectTimes);
			try{
		    Thread.sleep(interval);
			}catch(Exception e)
			{
				Log.d("liuna",e.toString());
			}
			result=proPort.processPort(value);
			code= result.getCode();
		}
		
		if(code.equals(Code.OK.toString()))
		{
			Log.d("liuna","getValue success");
			return result.getObject();
		}
		else
		{
		 Log.d("liuna","getValue Failed\n"+code);
		 return null;
		}
		}catch(Exception e)
		{
			Log.d("liuna","read value Failed");
			return null;
		}
		
	}
	
	public boolean putValue(String []varName,Object[]varValue)
	{
		DataStream value=new DataStream(Method.PUT,appId);
		value.addValue(varName, varValue);
		DataStream result=proPort.processPort(value);
		
		String code= result.getCode();
		if(code.equals(Code.OK.toString()))
		{
			return true;
		}
		else
		{
		 Log.d("liuna",code);
		 return false;
		}
	}
	
	public Object [] getValue(String []varName)
	{
		DataStream value=new DataStream(Method.GET,appId);
		value.addKey(varName);
		DataStream result=proPort.processPort(value);
		String code= result.getCode();
		if(code.equals(Code.OK.toString()))
		{
			return result.getObjects(varName.length);
		}
		else
		return null;
	}

	public boolean putFile(String pathName,String pathUrl)
	{
		DataStream value=new DataStream(Method.PUT,appId);
		if(appDevice==Device.CLOUD)
		  value.addFileInfo(Device.CLOUD, Device.MOBILE,pathUrl);
		else
		  value.addFileInfo( Device.MOBILE,Device.CLOUD,pathUrl);
		value.addValue(pathName, (Object)pathUrl);
		DataStream result=proPort.processPort(value);
		String code= result.getCode();
		if(code.equals(Code.OK.toString()))
		{
			return true;
		}
		else
		return false;
		
	}
	
	public Object getFile(String pathName)
	{
		
		DataStream value=new DataStream(Method.GET,appId);
		value.addKey(pathName);
		DataStream result=proPort.processPort(value);
		String code= result.getCode();
		if(code.equals(Code.OK.toString()))
		{
			return result.getObject();
		}
		else
		return null;

	}
	
    public boolean putFile(String []pathName,String[]pathUrl)
 	{
    	
		DataStream value=new DataStream(Method.PUT,appId);
		if(appDevice==Device.CLOUD)
		  value.addFileInfo(Device.CLOUD, Device.MOBILE,pathUrl);
		else
		  value.addFileInfo( Device.MOBILE,Device.CLOUD,pathUrl);
		
		value.addValue(pathName, (Object[])pathUrl);
		DataStream result=proPort.processPort(value);
		String code= result.getCode();
		if(code.equals(Code.OK.toString()))
		{
			return true;
		}
		else
		return false;
		
 	}
   
	public Object[] getFile(String []pathName)
    {
		
    	DataStream value=new DataStream(Method.GET,appId);
		value.addKey(pathName);
		DataStream result=proPort.processPort(value);
		String code= result.getCode();
		if(code.equals(Code.OK.toString()))
		{
			return result.getObjects(pathName.length);
		}
		else
		return null;
		
    }
	
	 private boolean isContextFit(Device appDevice)
	   {
		   return true;
		   
	   }
}
