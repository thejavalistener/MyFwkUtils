package thejava.listener.fwkutils.log2;
//package thejava.listener.fwkutils.log;
//
//import java.io.*;
//
//public class MyLogOutputStream extends OutputStream
//{
//	private OutputStream consoleOutputStream;
//	private OutputStream fileOutputStream;
//	
//	public MyLogOutputStream()
//	{
//		this(null);
//	}
//	
//	public MyLogOutputStream(String logFileName,boolean append)
//	{
//		try
//		{
//			fileOutputStream = new FileOutputStream(logFileName,append);
//			this.consoleOutputStream = System.out;			
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
//	
//	public MyLogOutputStream(OutputStream f)
//	{
//		this.consoleOutputStream = System.out;
//		this.fileOutputStream = f;
//	}
//
//	@Override
//	public void write(int i) throws IOException
//	{
//		consoleOutputStream.write(i);
//		if( fileOutputStream!=null ) fileOutputStream.write(i);
//	}
//
//	@Override
//	public void write(byte[] buf, int off, int len) throws IOException
//	{
//		consoleOutputStream.write(buf,off,len);
//		if( fileOutputStream!=null ) fileOutputStream.write(buf,off,len);
//	}
//
//	@Override
//	public void flush() throws IOException
//	{
//		consoleOutputStream.flush();
//		if( fileOutputStream!=null ) fileOutputStream.flush();
//	}
//
//	@Override
//	public void close() throws IOException
//	{
//		consoleOutputStream.close();
//		if( fileOutputStream!=null ) fileOutputStream.close();
//	}
//}
