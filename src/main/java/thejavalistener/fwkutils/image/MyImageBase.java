package thejavalistener.fwkutils.image;

import static thejavalistener.fwkutils.image.MyImageBase._ensureParent;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import thejavalistener.fwkutils.various.MyThread;

public class MyImageBase
{
	private static final int MAX_RETRIES=5;

	protected static void _downloadWithRetries(String url, File dest)
	{
		IOException last=null;
		_ensureParent(dest);
		for(int i=1; i<=MAX_RETRIES; i++)
		{
			try
			{
				File tmp=new File(dest.getParentFile(),dest.getName()+".downloading");
				_downloadToTemp(url,tmp);
				if(!tmp.renameTo(dest))
				{
					_copyFile(tmp,dest);
					tmp.delete();
				}
				return;
			}
			catch(IOException e)
			{
				last=e;
				MyThread.sleep(350);
			}
		}
		throw new RuntimeException("Error descargando desde URL: "+url,last);
	}

	protected static void _ensureParent(File file)
	{
		File parent=file.getParentFile();
		if(parent!=null&&!parent.exists())
		{
			parent.mkdirs();
		}
	}

	private static void _downloadToTemp(String urlStr, File tmp) throws IOException
	{
		URL url=new URL(urlStr);
		URLConnection conn=url.openConnection();
		conn.setConnectTimeout(6000);
		conn.setReadTimeout(10000);
		conn.setRequestProperty("User-Agent","Mozilla/5.0");
		try (InputStream in=new BufferedInputStream(conn.getInputStream()); OutputStream out=new BufferedOutputStream(new FileOutputStream(tmp)))
		{
			byte[] buf=new byte[8192];
			int r;
			while((r=in.read(buf))!=-1)
			{
				out.write(buf,0,r);
			}
			out.flush();
		}
	}

	protected static void _scaleImage(File src, File dest, int w, int h)
	{
		try
		{
			BufferedImage original=ImageIO.read(src);
			BufferedImage scaled=scaleInMemory(original,w,h);
			_ensureParent(dest);
			ImageIO.write(scaled,_chooseFormatByExt(dest.getName()),dest);
		}
		catch(IOException e)
		{
			throw new RuntimeException("Error al escalar imagen: "+dest,e);
		}
	}
	
	private static String _chooseFormatByExt(String name)
	{
		int dot=name.lastIndexOf('.');
		return (dot>0&&dot<name.length()-1)?name.substring(dot+1).toLowerCase():"png";
	}


	private static void _copyFile(File src, File dest) throws IOException
	{
		try (InputStream in=new FileInputStream(src); OutputStream out=new FileOutputStream(dest))
		{
			byte[] buf=new byte[8192];
			int r;
			while((r=in.read(buf))!=-1)
				out.write(buf,0,r);
		}
	}


	private static BufferedImage scaleInMemory(BufferedImage original, int w, int h)
	{
		int originalW=original.getWidth();
		int originalH=original.getHeight();

		if(w<=0&&h<=0) throw new IllegalArgumentException("Width and height cannot both be <= 0");

		if(w<=0) w=(int)((double)originalW*h/originalH);
		else if(h<=0) h=(int)((double)originalH*w/originalW);

		Image scaled=original.getScaledInstance(w,h,Image.SCALE_SMOOTH);
		BufferedImage buffered=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d=buffered.createGraphics();
		g2d.drawImage(scaled,0,0,null);
		g2d.dispose();
		return buffered;
	}


	// --- Descarga en memoria ---
	protected static void downloadToMemory(String url, Consumer<BufferedImage> onSuccess, Runnable onFailure)
	{
		try
		{
			BufferedImage img=ImageIO.read(new URL(url));
			if(img!=null)
			{
				onSuccess.accept(img);
			}
			else
			{
				onFailure.run();
			}
		}
		catch(IOException e)
		{
			onFailure.run();
		}
	}

}
