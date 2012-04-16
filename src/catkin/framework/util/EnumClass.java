package catkin.framework.util;

public class EnumClass {
	public enum Method  //枚举可以直接获得对应字符串
	{
		GET,
		PUT;
	};
	
    public enum Code
    {
    	OK(200),
    	ACCEPTED(201),
    	
    	BAD_REQUEST(400),
    	NOT_FOUND(404),
    
    	SERVER_ERROR(500),
    	TIME_OUT(504);
	    
	    private final int value;
	    Code(int i)
	    {
	    	this.value=i;
	    }
	    public int value()
	    {
	    	return this.value;
	    }
	    @Override
		public String toString()
	    {
			return String.valueOf(value);
		}
    
    };
    
	public enum Device
	{
		CLOUD,
		MOBILE;
		
	};

}
