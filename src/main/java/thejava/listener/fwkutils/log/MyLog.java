//package thejavalistener.fwkutils.various;
//
//import java.util.function.Function;
//
//public class MyLog
//{
//	private static final Function<StackTraceElement,Boolean> disposable=(ste) -> ste.getClassName().startsWith("java.lang")||ste.getClassName().equals(MyLog.class.getName());
//	public static boolean showClassName = true;
//	public static boolean showLineNumber = true;
//	public static boolean showMethodName = true;
//	
//	
////	private static String sout()
////	{
////		StackTraceElement ste=currStackTrace();
////
////		// Obtener el nombre de la clase y el método
////		String className=ste.getClassName();
////		String methodName=ste.getMethodName();
////		int lineNumber=ste.getLineNumber();
////
////		String ret = "";
////		if( showClassName) ret+="["+className+"]";
////		if( showMethodName) ret+="["+methodName+"]";
////		if( showLineNumber) ret+="["+lineNumber+"]";
////		
////		return ret;
////	}
//
//	private static String sout()
//	{
//	    StackTraceElement ste = currStackTrace();
//
//	    if (ste == null) {
//	        return "[unknown]";
//	    }
//
//	    String className = ste.getClassName();
//	    String methodName = ste.getMethodName();
//	    int lineNumber = ste.getLineNumber();
//
//	    String ret = "";
//	    if (showClassName) ret += "[" + className + "]";
//	    if (showMethodName) ret += "[" + methodName + "]";
//	    if (showLineNumber) ret += "[" + lineNumber + "]";
//
//	    return ret;
//	}
//	
//	
//	public static void println()
//	{
//		System.out.println(sout());
//	}
//
//	public static void println(Object o)
//	{
//		String mssg = o!=null?o.toString():"null (param is null)";
//		System.out.println(mssg+" ("+sout()+")");
//	}
//	
////	public static void println(String message)
////	{
////		System.out.println(message+" ("+sout()+")");
////	}
//
//	public static StackTraceElement currStackTrace()
//	{
//		return currStackTrace(f -> false);
//	}
//
////	public static StackTraceElement currStackTrace(Function<StackTraceElement,Boolean> discart)
////	{
////		StackTraceElement ste[]=Thread.currentThread().getStackTrace();
////		int i=0;
////		while(disposable.apply(ste[i]))
////		{
////			i++;
////		}
////
////		while(discart.apply(ste[i]))
////		{
////			i++;
////		}
////
////		return i<ste.length?ste[i]:null;
////	}
//	
//	public static StackTraceElement currStackTrace(Function<StackTraceElement, Boolean> discart) {
//	    StackTraceElement[] ste = Thread.currentThread().getStackTrace();
//	    int i = 0;
//
//	    // Evitar desbordes en el primer while
//	    while (i < ste.length && disposable.apply(ste[i])) {
//	        i++;
//	    }
//
//	    // Evitar desbordes en el segundo while
//	    while (i < ste.length && discart.apply(ste[i])) {
//	        i++;
//	    }
//
//	    return i < ste.length ? ste[i] : null;
//	}
//	
//	public static void f()
//	{
//		MyLog.println("PSopsofpso");
//	}
//
//	
//	public static void main(String[] args)
//	{
//		MyLog.println("Hola");
//		MyLog.println("Hola");
//		MyLog.println("Hola");
//		f();
//		MyLog.println("Hola");
//	}
//}


package thejava.listener.fwkutils.log;

import java.io.FileOutputStream;
import java.util.function.Function;

import thejavalistener.fwkutils.various.MyFile;

public class MyLog
{
	private static MyLog singleton = null;
	private MyLogConfig logConfig = MyLogConfig.get();
	private FileOutputStream fos = null;
	
	
	private StringBuffer stringBuffer = null;
	
	private MyLog() 
	{
		stringBuffer =  new StringBuffer();
		applyConfig();
	};
	
	public void applyConfig()
	{
		try
		{
			if( logConfig.getLogFileName()!=null )
			{
				fos = new FileOutputStream(logConfig.getLogFileName());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}
	
	public MyLogConfig getConfig()
	{
		return logConfig;
	}
		
	public static MyLog get()
	{
		if( singleton==null )
		{
			singleton = new MyLog();
		}
		
		return singleton;
	}
	
	
    private static final Function<StackTraceElement,Boolean> disposable =
            ste -> ste.getClassName().startsWith("java.lang")
                   || ste.getClassName().equals(MyLog.class.getName());

    
    private String sout()
    {
        boolean showClassName=singleton.logConfig.isShowClassName(); 
        boolean showMethodName=singleton.logConfig.isShowMethodName();
        boolean showLineNumber=singleton.logConfig.isShowLineNumber();
    	boolean showTimestamp=singleton.logConfig.isShowTimestamp(); 
    	return sout(showClassName,showMethodName,showLineNumber,showTimestamp);
    }

    
    private String sout(boolean showClassName,boolean showMethodName,boolean showLineNumber,boolean showTimestamp)
    {
        StackTraceElement ste = currStackTrace();

        String className  = ste.getClassName();
        String methodName = ste.getMethodName();
        int lineNumber    = ste.getLineNumber();

        String ret = "";
        if(showClassName)  ret += "[" + className  + "]";
        if(showMethodName) ret += "[" + methodName + "]";
        if(showLineNumber) ret += "[" + lineNumber + "]";
    	if(showTimestamp) ret += "[" + Long.toString(System.currentTimeMillis()) + "]";

        return ret;
    }

    public void println()
    {
        System.out.println(sout());
    }

    public void println(Object o)
    {
    	try
		{
            String mssg = (o != null ? o.toString() : "null (param is null)");
            String toLog = mssg + " " + sout();
            
            if( logConfig.getLogFileName()!=null )
            {
            	fos.write(toLog.getBytes());
            }
            
            if(logConfig.getLogFileName()==null || logConfig.isEnableConsoleEcho())
            {
            	System.out.println(toLog);			
			}
    	}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
    
    private void _tagged(String tag,Object o)
    {
    	StringBuffer sb = new StringBuffer();
    	sb.append('[');
    	sb.append(tag).append(']').append(' ').append(o.toString());
    	println(sb.toString());    	
    }
    
    public MyLog append(Object o)
    {
    	stringBuffer.append(o);
    	return this;
    }
    
    public void info()
    {
    	info(stringBuffer);
    	stringBuffer.delete(0,stringBuffer.length());
    }
    
    public void warn()
    {
    	warn(stringBuffer);
    	stringBuffer.delete(0,stringBuffer.length());
    }
    
    public void error()
    {
    	error(stringBuffer);
    	stringBuffer.delete(0,stringBuffer.length());
    }

    
    public void info(Object o)
    {
    	_tagged(singleton.logConfig.getInfoTag(),o.toString());
    }
    public void error(Object o)
    {
    	_tagged(singleton.logConfig.getErrorTag(),o.toString());
    }
    public void warn(Object o)
    {
    	_tagged(singleton.logConfig.getWarningTag(),o.toString());
    }

    public StackTraceElement currStackTrace()
    {
        return currStackTrace(f -> false);
    }
    
    public StackTraceElement currStackTrace(Function<StackTraceElement,Boolean> discart)
    {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        for (StackTraceElement e : ste)
        {
            String cls = e.getClassName();

            // 1) descartar únicamente lo que NO NOS SIRVE
            if (cls.startsWith("java.lang")) continue;          // Thread, etc.
            if (cls.equals(MyLog.class.getName())) continue;    // métodos de MyLog

            // 2) descartar extra si el caller quiere
            if (discart.apply(e)) continue;

            // 3) este es el frame correcto → f() o main()
            return e;
        }

        // fallback
        return ste.length > 0 ? ste[ste.length - 1] : null;
    }

    public void qdbg()
    {
    	println(sout(true,true,true,true));
    }

    public static String getMostRecentLogFileName(String logDir,String wildCardFileName)
    {
    	return MyFile.getMostRecentFileName(logDir,wildCardFileName);
    }    
}
