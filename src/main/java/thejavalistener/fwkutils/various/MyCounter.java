package thejavalistener.fwkutils.various;

import thejavalistener.fwkutils.string.MyString;

public class MyCounter
{
	private String description;
	private long cnt = 0;
	
	public MyCounter()
	{
		this("");
	}
	
	public MyCounter(String descr)
	{
		setDescription(descr);
	}
	
	public MyCounter(long initValue)
	{
		this(initValue,"");
	}
	
	public MyCounter(long initValue,String descr)
	{
		setDescription(descr);
		cnt = initValue;
	}

	
	public void reset()
	{
		cnt = 0;
	}
	
	public void incr()
	{
		cnt++;
	}
	
	public int asInteger()
	{
		return (int)cnt;
	}
	
	public long asLong()
	{
		return cnt;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description=description;
	}
	
	public String asString()
	{
		return Long.toString(cnt);
	}

	public String asString(int len,char c)
	{		
		return MyString.lpad(asString(),c,len);
	}
}
