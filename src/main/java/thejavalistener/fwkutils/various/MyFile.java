package thejavalistener.fwkutils.various;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

public class MyFile
{
	/** Verifica $filename no existe => lo crea, retornando true o false si lo creó o no */
	public static boolean createIfNotExists(String filename)
	{
		try
		{
			File f = new File(filename);
			boolean existe = f.exists();
			if( !existe )
			{
				f.createNewFile();
			}				
			
			return !existe;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
		
    public static File getMostRecentFileName(String dir, String wcard)
    {
        File d = new File(dir);
        if (!d.exists() || !d.isDirectory())
        {
            return null;
        }

        PathMatcher matcher = (wcard == null || wcard.isBlank())
            ? null
            : FileSystems.getDefault().getPathMatcher("glob:" + wcard);

        File[] files = d.listFiles(f -> {
            if (!f.isFile())
            {
                return false;
            }
            if (matcher == null)
            {
                return true;
            }
            return matcher.matches(Paths.get(f.getName()));
        });

        if (files == null || files.length == 0)
        {
            return null;
        }

        File last = null;
        long lastTime = Long.MIN_VALUE;

        for (File f : files)
        {
            long t = f.lastModified();
            if (t > lastTime)
            {
                lastTime = t;
                last = f;
            }
        }

        return last != null ? last: null;
    }

    public static void writeFile(String fullFileName, String textContent)
    {
        try
        {
            java.nio.file.Path path = java.nio.file.Paths.get(fullFileName);

            // Crear directorios si no existen
            java.nio.file.Path parent = path.getParent();
            if(parent != null)
                java.nio.file.Files.createDirectories(parent);

            // Escribir archivo (UTF-8), sobrescribe si existe
            java.nio.file.Files.writeString(
                path,
                textContent,
                java.nio.charset.StandardCharsets.UTF_8,
                java.nio.file.StandardOpenOption.CREATE,
                java.nio.file.StandardOpenOption.TRUNCATE_EXISTING
            );
        }
        catch(Exception e)
        {
            throw new RuntimeException("Error escribiendo archivo: " + fullFileName, e);
        }
    }

	public static String normalize(String path)
	{
		if (path == null) return null;
	    return Paths.get(path).normalize().toString();	}
}
