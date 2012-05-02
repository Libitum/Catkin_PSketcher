package catkin.framework.buffer;

import java.util.ArrayList;
import java.util.HashMap;


public class Buffer {
	private HashMap<String,Object> buffer=null;
	/*
	 * 构造函数 单例模式
	 */
	private Buffer()
	{
    	buffer=new  HashMap<String,Object>();
	}
	private static Buffer bf=new Buffer();
	public synchronized static Buffer getInstance()
	{
	   return bf;
	}
	
   /*
    * get data from buffer
    */
	public BufferData get(BufferData data)
	{
		ArrayList<String> key=data.getKey();
		
		int i=0;
		for(i=0;i<key.size();i++)
		{
			if(buffer.containsKey(key.get(i)))
			{
				data.addValue(buffer.get(key.get(i)));
				buffer.remove(key.get(i));
			}
		}
		return data;
	}
	/*
	 * put data into buffer
	 */
	public BufferData put(BufferData data)
	{
		ArrayList<String> key=data.getKey();
		ArrayList<Object> object=data.getObject();
		if(key.size()!=object.size())
			return null;
		else
		{
			int i=0;
			for(i=0;i<key.size();i++)
				buffer.put(key.get(i),object.get(i));
			return data;
		}
	}
	
}
