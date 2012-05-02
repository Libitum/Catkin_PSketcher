package catkin.framework.buffer;

import java.util.ArrayList;
/*
 * 封装存储到buffer中的数据
 */

public class BufferData {
	
	    private ArrayList<String> key=null;
	    private ArrayList<Object> object=null;
	    
	    /*
	     * get key and object
	     */
	    public ArrayList<String> getKey()
	    {
	    	return key;
	    }
	    public ArrayList<Object> getObject()
	    {
	    	return object;
	    }
	    
	    /*
	     * constructor
	     */
		public BufferData(String k)
		{
			key=new ArrayList<String>();
			object=new ArrayList<Object>();
			key.add(k);
		}
		public BufferData(String[] k)
		{
			key=new ArrayList<String>();
			object=new ArrayList<Object>();
			int i=0;
			for(i=0;i<k.length;i++)
			{
				key.add(k[i]);
			}
		}
		public BufferData(String k, Object o)
		{
			key=new ArrayList<String>();
			object=new ArrayList<Object>();
			key.add(k);
			object.add(o);
		}
		public BufferData(String[] k,Object[] o)
		{
			key=new ArrayList<String>();
			object=new ArrayList<Object>();
			int i=0;
			for(i=0;i<k.length;i++)
			{
				key.add(k[i]);
			}
			for(i=0;i<o.length;i++)
			{
				object.add(o[i]);
			}
			
		}
		
		public boolean addValue(Object o)
		{
			object.add(o);
			return true;
		}
		/*
		 * getValues
		 */
		public Object[]getValues(int num)
		{
		
			if(num!= object.size())
			{
				return null;
			}
			else
			{
				Object[]result=new Object[num];
				int i=0;
				for(i=0;i<num;i++)
				  result[i]=object.get(i);
				return result;
			}
		}

}
