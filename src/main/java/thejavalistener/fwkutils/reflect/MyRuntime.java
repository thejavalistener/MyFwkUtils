package thejavalistener.fwkutils.reflect;

public class MyRuntime
{
	public static void unsoportedMethod()
	{
		StackTraceElement ste[]=Thread.currentThread().getStackTrace();
		int p=ste.length>2?2:0;
		throw new RuntimeException("Unsoported Method:"+ste[p].getMethodName()+" in class:"+ste[p].getClassName());
	}

	public static StackTraceElement peekStackTrace()
	{
		StackTraceElement ste[]=Thread.currentThread().getStackTrace();
		int p=ste.length>3?3:0;
		return ste[p];
	}

}
