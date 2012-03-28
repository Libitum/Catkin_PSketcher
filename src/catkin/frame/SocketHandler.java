package catkin.frame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import catkin.frame.EnumClass.Device;

import android.util.Log;

public class SocketHandler {
	private  int port=9527;
	private  String ip=null;
	
	public SocketHandler(Device device)
	{
		if(device==Device.MOBILE)
		this.ip="192.168.1.132";
		else 
		this.ip="127.0.0.1";
	}
	/*
	 * 端口操作
	 */
	public DataStream processPort(DataStream data)
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
	     DataStream in=readStream(inPut);
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
	 * 写流
	 */
	private void writeStream(ObjectOutputStream out, DataStream data )throws Exception
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
    private DataStream readStream(ObjectInputStream in) throws Exception 
	{
    	
    	Log.d("liuna","Enter readStream");
    	try{
		    	DataStream input=new DataStream();
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
    		   
}
