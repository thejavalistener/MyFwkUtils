package thejavalistener.fwk.image;

import java.io.File;
import java.util.function.Consumer;

public class MyImage extends MyImageBase
{
	// --- Configuración general ---

	/** Descarga una imagen desde url->targetPath sólo si no existe previamente o rewriteIfExists es true */
	public static void downloadToFile(String url, String targetPath, boolean rewriteIfExists)
	{
		File dest=new File(targetPath);
		if(!dest.exists()||rewriteIfExists)
		{
			_downloadWithRetries(url,dest);
		} 
	}

	public static void downloadToFileAsync(String url, String targetPath, boolean rewriteIfExists,Consumer<Boolean> onFinished)
	{
		new Thread(() -> {
			try
			{
				File dest=new File(targetPath);
				if(!dest.exists()||rewriteIfExists)
				{
					_downloadWithRetries(url,new File(targetPath));
					onFinished.accept(true);
				}
			}
			catch(Exception e)
			{
				onFinished.accept(false);
			}
		}).start();
	}

	// --- Escalado a disco ---
	public static void scaleToFile(String pathOriginal, String pathScaled, int w, int h, boolean rewriteIfExists)
	{
		File original=new File(pathOriginal);
		File scaled=new File(pathScaled);
		if(!scaled.exists()||rewriteIfExists)
		{
			_scaleImage(original,scaled,w,h);
		}
	}


}