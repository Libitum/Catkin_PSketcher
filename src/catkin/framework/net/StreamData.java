package catkin.framework.net;

import java.util.ArrayList;
import java.util.List;

import catkin.framework.util.EnumClass.Device;
import catkin.framework.util.EnumClass.Method;



import android.util.Log;

public class StreamData {

	private ArrayList<String> HEAD=null;
	private ArrayList<Object> BODY=null;
	
	
	public StreamData(Method M, String id)
	{
		Log.d("liuna","construct DataStream");
		HEAD=new ArrayList<String>();
		BODY=new ArrayList<Object>();
		HEAD.add("METHOD:"+M.toString());
		HEAD.add("APPID:"+id);
	}
	
	public StreamData()
	{
		HEAD=new ArrayList<String>();
		BODY=new ArrayList<Object>();
	}
	
    public boolean addValue(String varName,Object varValue)
	{
		
		if(varName!=null&&varValue!=null)
		{
		HEAD.add("KEY:"+varName);
		BODY.add(varValue);
		return true;
		}
		else
		return false;
	}
	
	public boolean addValue(String []varName,Object[]varValue)
	{
		if(varName.length!=varValue.length)
		{
			Log.d("liuna","变量名和变量值个数不一致");
			return false;
		}
		else
		{
			for(int i=0;i<varName.length;i++)
			{
				addValue(varName[i],varValue[i]);
			}
		}
		return true;
	}

	public boolean addFileInfo(Device from ,Device to, String url)
	{
		if(url!=null)
		{	
		HEAD.add("FROM:"+from.toString());
		HEAD.add("TO:"+to.toString());
		HEAD.add("FILE:"+url);
		return true;
		}
		else
		return false;
	}
	
    public boolean addFileInfo(Device from ,Device to, String[]url)
	{
		HEAD.add("FROM:"+from.toString());
		HEAD.add("TO:"+to.toString());	
		if(url!=null)
		{
			for(int i=0;i<url.length;i++)
		    HEAD.add(url[i]);
		    return true;
		}
		else
		    return false;
	}
	
	public boolean addKey(String varName)
	{
		if(varName!=null)
		{
			HEAD.add("KEY:"+varName);
			return true;
		}
		else
		{
			Log.d("liuna","key值为空");;
			return false;
		}
		
	}
	
	public boolean addKey(String []varName)
	{
		if(varName==null)
			return false;
		else
		{
			for(int i=0;i<varName.length;i++)
				addKey(varName[i]);
			return true;
		}
		
	}
	
	public boolean addHead(String s)
	{
		HEAD.add(s);
		return true;
	}
	
    public boolean addBody(Object o)
	{
		BODY.add(o);
		return true;
	}
	
	public List<String> getHead()
	{
		return HEAD;
	}
	
    public List<Object> getBody()
	{
		return BODY;
	}
    
    public String getCode()
    {
    	if(!HEAD.isEmpty())
    	{   
    		String result=null;
    		for(int i=0;i<HEAD.size();i++)
    		{
    			String s=HEAD.get(i);
    			
    			result=find("CODE",s); 
    			if(result!=null)
    				break; 
    		}
    		return result;
    	}
    	Log.d("liuna","get Code Failed");
    	return null;
    }
	
    public Object getObject()
    {
    	Log.d("liuna","Enter getBody");
    	if(BODY!=null&&BODY.size()==1)
    	return BODY.get(0);
    	else
    	{
    	Log.d("liuna","getObject return null");
    	return null;
    	}
    }
    
    public Object[] getObjects(int num)
    {
    	if(BODY==null||BODY.size()!=num)
    		return null;
    	else
    	{
    	  Object []O=new Object[num];
    	  for(int i=0;i<BODY.size();i++)
    		  O[i]=BODY.get(i);
    	  return O;
    	  
    	}
    	
    	 
    }
/*
 * 
 */
    private String find(String name, String headLine)
    {
    	String s=headLine.trim();
    	if(!s.startsWith(name))
    	{
    		return null;
    	}
    	else
    	{
    		s=s.substring(name.length());
    		s=s.trim();
    		String temp=":";
    		if(s.startsWith(temp))
    		{
    			s=s.substring(temp.length());
    			return s.trim();
    		}
    		else
    		{
    			Log.d("liuna","head格式错误");
    			return null;
    		}
    	}
    	
    }
       
 }
    
    
    
    
    
