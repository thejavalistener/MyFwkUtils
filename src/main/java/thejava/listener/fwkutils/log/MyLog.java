package thejava.listener.fwkutils.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Function;

import thejavalistener.fwkutils.various.MyFile;

public class MyLog
{
	private final Object fileLock = new Object();
	public MyLogConfig config=null;
	private FileOutputStream fos=null;
	// private StringBuffer stringBuffer = null;

	private ThreadLocal<StringBuilder> buffer=ThreadLocal.withInitial(StringBuilder::new);

	public MyLog append(Object o)
	{
		buffer.get().append(o);
		return this;
	}

	// public void info()
	// {
	// info(stringBuffer);
	// stringBuffer.delete(0,stringBuffer.length());
	// }

	public void info()
	{
		flushWithTag(config.infoTag);
	}

	public void warn()
	{
		flushWithTag(config.warningTag);
	}

	public void error()
	{
		flushWithTag(config.errorTag);
	}

	private void flushWithTag(String tag)
	{
		StringBuilder sb=buffer.get();
		if(sb.length()==0) return;

		String msg="["+tag+"] "+sb.toString();
		println(msg);

		buffer.remove(); 
	}

	public MyLog()
	{
		this(null,false);
	};

	public MyLog(String logFilename,boolean append)
	{
		try
		{
			if(logFilename!=null) fos=new FileOutputStream(logFilename,append);
		}
		catch(IOException e)
		{
			throw new RuntimeException("Imposible abrir: "+logFilename,e);
		}

		config=new MyLogConfig();
	}

	public void close()
	{
		try
		{
			if(fos!=null) fos.close();
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	// private static final Function<StackTraceElement,Boolean> disposable =
	// ste -> ste.getClassName().startsWith("java.lang")
	// || ste.getClassName().equals(MyLog.class.getName());

	private String sout()
	{
		boolean showClassName=config.showClassName;
		boolean showMethodName=config.showMethodName;
		boolean showLineNumber=config.showLineNumber;
		boolean showTimestamp=config.showTimestamp;
		return sout(showClassName,showMethodName,showLineNumber,showTimestamp);
	}

	private String sout(boolean showClassName, boolean showMethodName, boolean showLineNumber, boolean showTimestamp)
	{
		StackTraceElement ste=currStackTrace();

		if(ste==null) return "";

		String className=ste.getClassName();
		String methodName=ste.getMethodName();
		int lineNumber=ste.getLineNumber();

		String ret="";
		if(showClassName) ret+="["+className+"]";
		if(showMethodName) ret+="["+methodName+"]";
		if(showLineNumber) ret+="["+lineNumber+"]";
		if(showTimestamp) ret+="["+Long.toString(System.currentTimeMillis())+"]";

		return ret;
	}

	public void println()
	{
		println("");
	}

	public void println(Object o)
	{
		try
		{
			String mssg=(o!=null?o.toString():"null (param is null)");
			String toLog=mssg.isEmpty()?sout():mssg+" "+sout();

			if (fos != null)
			{
			    synchronized (fileLock)
			    {
			        fos.write((toLog + System.lineSeparator()).getBytes());
			        fos.flush();
			    }
			}

			if(fos==null||config.enableConsoleEcho)
			{
				System.out.println(toLog);
			}
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void _tagged(String tag, Object o)
	{
		String msg="["+tag+"] "+o;
		println(msg);
	}

	public void info(Object o)
	{
		_tagged(config.infoTag,o.toString());
	}

	public void error(Object o)
	{
		_tagged(config.errorTag,o.toString());
	}

	public void warn(Object o)
	{
		_tagged(config.warningTag,o.toString());
	}

	public StackTraceElement currStackTrace()
	{
		return currStackTrace(f -> false);
	}

	public StackTraceElement currStackTrace(Function<StackTraceElement,Boolean> discart)
	{
		StackTraceElement[] ste=Thread.currentThread().getStackTrace();

		for(StackTraceElement e:ste)
		{
			String cls=e.getClassName();

			// 1) descartar únicamente lo que NO NOS SIRVE
			if(cls.startsWith("java.lang")) continue; // Thread, etc.
			if(cls.equals(MyLog.class.getName())) continue; // métodos de MyLog

			// 2) descartar extra si el caller quiere
			if(discart.apply(e)) continue;

			// 3) este es el frame correcto → f() o main()
			return e;
		}

		// fallback
		return ste.length>0?ste[ste.length-1]:null;
	}

	public void qdbg()
	{
		println(sout(true,true,true,true));
	}

	public static File getMostRecentLogFileName(String logDir, String wildCardFileName)
	{
		return MyFile.getMostRecentFileName(logDir,wildCardFileName);
	}
}