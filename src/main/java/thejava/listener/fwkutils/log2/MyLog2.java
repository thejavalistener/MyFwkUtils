package thejava.listener.fwkutils.log2;
//package thejava.listener.fwkutils.log;
//
//import java.io.File;
//import java.util.function.Function;
//
//import thejavalistener.fwkutils.various.MyFile;
//
//public class MyLog2
//{
//	private static MyLog2 singleton = null;
//	private MyLogConfig logConfig = MyLogConfig.get();
//	private MyLogOutputStream ps = null;	
//	
//	private StringBuffer stringBuffer = null;
//	
//	private MyLog2() 
//	{
//		stringBuffer =  new StringBuffer();
//		applyConfig();
//	};
//	
//	public void applyConfig()
//	{
//		try
//		{
//			if( logConfig.getLogFileName()!=null )
//			{
//				ps = new MyLogOutputStream(logConfig.getLogFileName(),true);
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}		
//	}
//	
//	public MyLogConfig getConfig()
//	{
//		return logConfig;
//	}
//		
//	public static MyLog2 get()
//	{
//		if( singleton==null )
//		{
//			singleton = new MyLog2();
//		}
//		
//		return singleton;
//	}
//	
//	
//    private static final Function<StackTraceElement,Boolean> disposable =
//            ste -> ste.getClassName().startsWith("java.lang")
//                   || ste.getClassName().equals(MyLog2.class.getName());
//
//    
//    private String sout()
//    {
//        boolean showClassName=singleton.logConfig.isShowClassName(); 
//        boolean showMethodName=singleton.logConfig.isShowMethodName();
//        boolean showLineNumber=singleton.logConfig.isShowLineNumber();
//    	boolean showTimestamp=singleton.logConfig.isShowTimestamp(); 
//    	return sout(showClassName,showMethodName,showLineNumber,showTimestamp);
//    }
//
//    
//    private String sout(boolean showClassName,boolean showMethodName,boolean showLineNumber,boolean showTimestamp)
//    {
//        StackTraceElement ste = currStackTrace();
//
//        String className  = ste.getClassName();
//        String methodName = ste.getMethodName();
//        int lineNumber    = ste.getLineNumber();
//
//        String ret = "";
//        if(showClassName)  ret += "[" + className  + "]";
//        if(showMethodName) ret += "[" + methodName + "]";
//        if(showLineNumber) ret += "[" + lineNumber + "]";
//    	if(showTimestamp) ret += "[" + Long.toString(System.currentTimeMillis()) + "]";
//
//        return ret;
//    }
//
//    public void println()
//    {
//        System.out.println(sout());
//    }
//
//    public void println(Object o)
//    {
//    	try
//		{
//            String mssg = (o != null ? o.toString() : "null (param is null)");
//            String toLog = mssg + " " + sout();
//            
//            if( logConfig.getLogFileName()!=null )
//            {
//            	ps.write(toLog.getBytes());
//            }
//            
//            if(logConfig.getLogFileName()==null || logConfig.isEnableConsoleEcho())
//            {
//            	System.out.println(toLog);			
//			}
//    	}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//    }
//    
//    private void _tagged(String tag,Object o)
//    {
//    	StringBuffer sb = new StringBuffer();
//    	sb.append('[');
//    	sb.append(tag).append(']').append(' ').append(o.toString());
//    	println(sb.toString());    	
//    }
//    
//    public MyLog2 append(Object o)
//    {
//    	stringBuffer.append(o);
//    	return this;
//    }
//    
//    public void info()
//    {
//    	info(stringBuffer);
//    	stringBuffer.delete(0,stringBuffer.length());
//    }
//    
//    public void warn()
//    {
//    	warn(stringBuffer);
//    	stringBuffer.delete(0,stringBuffer.length());
//    }
//    
//    public void error()
//    {
//    	error(stringBuffer);
//    	stringBuffer.delete(0,stringBuffer.length());
//    }
//
//    
//    public void info(Object o)
//    {
//    	_tagged(singleton.logConfig.getInfoTag(),o.toString());
//    }
//    public void error(Object o)
//    {
//    	_tagged(singleton.logConfig.getErrorTag(),o.toString());
//    }
//    public void warn(Object o)
//    {
//    	_tagged(singleton.logConfig.getWarningTag(),o.toString());
//    }
//
//    public StackTraceElement currStackTrace()
//    {
//        return currStackTrace(f -> false);
//    }
//    
//    public StackTraceElement currStackTrace(Function<StackTraceElement,Boolean> discart)
//    {
//        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
//
//        for (StackTraceElement e : ste)
//        {
//            String cls = e.getClassName();
//
//            // 1) descartar únicamente lo que NO NOS SIRVE
//            if (cls.startsWith("java.lang")) continue;          // Thread, etc.
//            if (cls.equals(MyLog2.class.getName())) continue;    // métodos de MyLog
//
//            // 2) descartar extra si el caller quiere
//            if (discart.apply(e)) continue;
//
//            // 3) este es el frame correcto → f() o main()
//            return e;
//        }
//
//        // fallback
//        return ste.length > 0 ? ste[ste.length - 1] : null;
//    }
//
//    public void qdbg()
//    {
//    	println(sout(true,true,true,true));
//    }
//
//    public static File getMostRecentLogFileName(String logDir,String wildCardFileName)
//    {
//    	return MyFile.getMostRecentFileName(logDir,wildCardFileName);
//    }    
//    
//    
//    
//    
//}
