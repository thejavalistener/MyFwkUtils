package thejava.listener.fwkutils.log;

public class MyLogConfig
{
    private boolean showClassName = false;
    private boolean showLineNumber = false;
    private boolean showMethodName = false;
    private String infoTag    = "INFO";
    private String errorTag   = "ERR ";
    private String warningTag = "WARN";
    private String logFileName = null;
    private boolean enableConsoleEcho = true;
    private boolean showTimestamp = false;
    
    private static MyLogConfig singleton = null;
    
    private MyLogConfig() {}
    
    public static MyLogConfig get() 
    {
    	if( singleton==null )
    	{
    		singleton = new MyLogConfig();
    	}
    	
    	return singleton;
    }

	public boolean isShowClassName()
	{
		return showClassName;
	}

	public void setShowClassName(boolean showClassName)
	{
		this.showClassName=showClassName;
	}

	public boolean isShowLineNumber()
	{
		return showLineNumber;
	}

	public void setShowLineNumber(boolean showLineNumber)
	{
		this.showLineNumber=showLineNumber;
	}

	public boolean isShowMethodName()
	{
		return showMethodName;
	}

	public void setShowMethodName(boolean showMethodName)
	{
		this.showMethodName=showMethodName;
	}

	public String getInfoTag()
	{
		return infoTag;
	}

	public void setInfoTag(String infoTag)
	{
		this.infoTag=infoTag;
	}

	public String getErrorTag()
	{
		return errorTag;
	}

	public void setErrorTag(String errorTag)
	{
		this.errorTag=errorTag;
	}

	public String getWarningTag()
	{
		return warningTag;
	}

	public void setWarningTag(String warningTag)
	{
		this.warningTag=warningTag;
	}

	public String getLogFileName()
	{
		return logFileName;
	}

	public void setLogFileName(String logFileName)
	{
		this.logFileName=logFileName;
	}

	public boolean isEnableConsoleEcho()
	{
		return enableConsoleEcho;
	}

	public void setEnableConsoleEcho(boolean enableConsoleEcho)
	{
		this.enableConsoleEcho=enableConsoleEcho;
	}

	public boolean isShowTimestamp()
	{
		return showTimestamp;
	}

	public void setShowTimestamp(boolean showTimestamp)
	{
		this.showTimestamp=showTimestamp;
	}
    
    
}