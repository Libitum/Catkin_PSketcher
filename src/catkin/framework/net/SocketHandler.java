package catkin.framework.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import catkin.framework.util.EnumClass.Device;

import android.util.Log;

public class SocketHandler {
	
	private  int port=NetInfo.port;
	private  String ip=null;
	
	public SocketHandler(Device device)
	{
		if(device==Device.MOBILE)
		this.ip=NetInfo.remoteIp;
		else 
		this.ip=NetInfo.localIp;
	}
	/*
	 * 端口操作
	 */
	public StreamData handleSocket(StreamData data)
	{
		Log.d("liuna","Enter ProcessPort");
		Socket socket = null;
		try
		{
			
	     InetAddress addr = InetAddress.getByName(ip); //UnknownHostException
	     socket = new Socket(addr,port); //IOException  
	     ObjectOutputStream outPut = new ObjectOutputStream(socket.getOutputStream());  //getOutputStream()  
	     ObjectInputStream inPut = new ObjectInputStream(socket.getInputStream());  //socket.getInputStream()  
	     writeStream(outPut,data); 
	     StreamData in=readStream(inPut);
         socket.close();    //IOException	
	     return in;
    
		}
		catch (Exception e)
		{
			Log.d("liuna","Socket出现异常\n"+e.toString());
			return null;
		}
		finally
		
		{
			try
			{
				socket.close();
			}
			catch (Exception e)
			{
				Log.d("liuna","finnally中的端口关闭出现异常");
			}
		}
	}
	
	/*
	 * 写流操作
	 */
	private void writeStream(ObjectOutputStream out, StreamData data )throws Exception
    {
		
    	Log.d("liuna","Enter writeStream");
    	List<String>head=data.getHead();
    	List<Object>body=data.getBody();
    	 for(int i=0;i<head.size();i++)
        {
        	out.writeBytes(head.get(i));
        	out.writeBytes("\n");
        }
        out.writeBytes("\n");
        Log.d("liuna","Write Body");
    	for(int i=0;i<body.size();i++)
    	{
    		out.writeObject(body.get(i));
    	}
    	out.flush();	
    	
    }
	
    /*
     * 读流操作	
     */
    private StreamData readStream(ObjectInputStream in) throws Exception 
	{
    	
    	Log.d("liuna","Enter readStream");
    	try{
		    	StreamData input=new StreamData();
		    	String s=null;
		    	while((s=in.readLine()).length()>0&&s!=null)
		    	{
		    		Log.d("liuna","read:"+s);
		    		input.addHead(s);	
		    	}
		    	Log.d("liuna","readBody");
		    	Object o=null;
		    	int objNum=0;
		    	try
		    	{
		    		
			    	while(true)
			    	{
			    	o=in.readObject();
			    	objNum++;
			    	input.addBody(o);
			    	}
		    	}
		    	catch(IOException e)
			    {
		    		Log.d("liuna","readBody finished,obj NUM is: "+String.valueOf(objNum));
		    		return input;
			    }
    	}catch (Exception e)
    	{
	    		Log.d("liuna","readStream Failed!"+e.toString());
	    		return null;
    	}
    	
    }
  
/*
 * 检测中间件是否存在
 */
    @SuppressWarnings("finally")
	public boolean testConnect()
    {
    	Log.d("liuna","Enter testConnect");
		Socket socket = null;
		try
		{
			
	     InetAddress addr = InetAddress.getByName(ip); //UnknownHostException
	     socket = new Socket(addr,port); //IOException 
	     socket.close();                //只是检测对方这个端口有没有打开
	     return true;
	     
		}catch(Exception e)
		{
			try
			{
				socket.close();
				return false;
			}
			finally
			{
				return false;
			}
		    
		}
    }
}
