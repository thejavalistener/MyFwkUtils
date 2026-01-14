package thejava.listener.fwkutils.log;

public final class MyLogs
{
	private static volatile MyLog INSTANCE;

	public static MyLog get()
	{
		if(INSTANCE==null)
		{
			synchronized(MyLogs.class)
			{
				if(INSTANCE==null)
				{
					INSTANCE=new MyLog("app.log",true);
				}
			}
		}
		return INSTANCE;
	}
}
