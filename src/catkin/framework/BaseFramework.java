package catkin.framework;

import android.util.Log;
import catkin.framework.buffer.Buffer;
import catkin.framework.buffer.BufferData;
import catkin.framework.net.SocketHandler;
import catkin.framework.net.StreamData;
import catkin.framework.util.EnumClass.Code;

class BaseFramework {
	Buffer buffer;
	private SocketHandler  socketHandler;
	
	public BaseFramework()
	{
		buffer=Buffer.getInstance();		
	}
	
	protected SocketHandler setSocketHandler(SocketHandler s)
	{
		return socketHandler=s;
	}
	/*@param data contains the key and value
	 * if failed,return null;
	 */
	protected boolean putToBuffer(BufferData data)
	{
		Log.d("liuna","putToBuffer");
		if( buffer.put(data)!=null)
			return true;
		else 
			return false;
	}
	
	/*@param data contains the key
	 * if failed ,return null;
	 */
	protected BufferData getFromBuffer(BufferData data)
	{
		Log.d("liuna","getFromBuffer");
		return buffer.get(data);
	}
	
	/*
	 * put to socket ,if failed return false,else return true;
	 */
	protected boolean putToSocket(StreamData data)
	{
		Log.d("liuna","putToSocket");
		try
	    {
			StreamData result=socketHandler.handleSocket(data);
			String code= result.getCode();
			if(code.equals(Code.ACCEPTED.toString()))
			{
				Log.d("liuna","putToSocket success");
				return true;
			}
			else
			{
		   	Log.d("liuna","putToSocket Failed ,putValue的返回值是："+code);
			return false;
			}
	    }catch(Exception e)
	    {
			Log.d("liuna",e.toString());
			return false;
	    }
   }
	
	/*
	 * get from  socket ,if failed return null,else return the StreamData;
	 */
   protected StreamData getFromSocket(StreamData data)
   {
	    Log.d("liuna","getFromSocket");
	    int ConnectTimes=5;
		//long interval=3000;//3毫秒
		try
		{
			StreamData  result=socketHandler.handleSocket(data);
			String code= result.getCode();
			while((!code.equals(Code.OK.toString()))&&ConnectTimes-->0)
			{
				
				/*try
				{
					Thread.sleep(interval);
				}catch(Exception e)
				{
					Log.d("liuna",e.toString());
				}*/
				result=socketHandler.handleSocket(data);
				code= result.getCode();
			}
		
			if(code.equals(Code.OK.toString()))
			{
				Log.d("liuna","get from socket success");
				return result;
			}
			else
			{
				 Log.d("liuna","get from socket Failed\n"+code);
				 return null;
			}
		
	   }catch(Exception e)
	   {
		Log.d("liuna","get from socket Failed");
		return null;
	   }
   }

   protected boolean put(BufferData bufferData,StreamData streamData ) 
   {
	  boolean result=false;
	  if(bufferData!=null)
	  {
		  result=putToBuffer(bufferData);
		  
	  }
	  
	  if(streamData!=null)
	  {
		  result=putToSocket(streamData);  
	  }
	  return result;
	  
   }
   /*
    * 返回值，可能是BufferData 也可能是StreamData
    */
   protected Object[] get(BufferData bufferData,StreamData streamData,int num)
   {
	      Object[] result=null;
		  if(bufferData!=null)
		  {
			  result=getFromBuffer(bufferData).getValues(num);  
		  }
		  if(result==null&&streamData!=null)
		  {
			  result=getFromSocket(streamData).getObjects(num);  
		  }
		  return result;
	   
   }
}
