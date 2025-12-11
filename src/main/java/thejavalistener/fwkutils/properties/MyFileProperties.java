package thejavalistener.fwkutils.properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.gson.JsonObject;

import thejavalistener.fwkutils.various.MyFile;
import thejavalistener.fwkutils.various.MyJson;

public class MyFileProperties extends MyFilePropertiesBase
{
	private Properties properties;

	// Constructor automático (usa DEFAULT_FILE_PATH)
	public MyFileProperties()
	{
		super();
		_load(fullFilename);
	}

	// Constructor explícito
	public MyFileProperties(String filename)
	{
		super(filename);
		_load(fullFilename);
	}

	private void _load(Path fullFilename)
	{
		MyFile.createIfNotExists(fullFilename.toString());
		properties=new Properties();

		try (FileInputStream fis=new FileInputStream(fullFilename.toString()))
		{
			properties.load(fis);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException("Error cargando propiedades de "+fullFilename,ex);
		}
	}

	public String getPropertiesFilename()
	{
		return fullFilename.toString();
	}

	// -----------------------------------------------------------
	// Escritura genérica
	// -----------------------------------------------------------
	public void putObject(String key, Object value)
	{
		if(value==null) throw new IllegalArgumentException("No se puede guardar un valor nulo para la clave: "+key);

		String json;

		if(value instanceof Class<?> clazz)
		{
			JsonObject jsonObj=new JsonObject();
			jsonObj.addProperty("type","java.lang.Class");
			jsonObj.addProperty("data",clazz.getName());
			json=jsonObj.toString();
		}
		else
		{
			json=MyJson.toJson(value);
		}

		putString(key,json);
	}

	// -----------------------------------------------------------
	// Lectura genérica
	// -----------------------------------------------------------
	@SuppressWarnings("unchecked")
	public <T> T getObject(String key)
	{
		String value=getString(key);
		if(value==null) return null;

		Object obj=MyJson.fromJson(value);
		if(obj==null) return null;

		if(obj instanceof List<?> list) return (T)new ArrayList<>(list);
		if(obj instanceof Map<?,?> map) return (T)new LinkedHashMap<>(map);

		return (T)obj;
	}

	// -----------------------------------------------------------
	// Auxiliares
	// -----------------------------------------------------------
	@SuppressWarnings("unchecked")
	public <T> T putObjectIfAbsent(String key, Object value)
	{
		T existing=(T)getObject(key);
		if(existing==null)
		{
			putObject(key,value);
			return (T)value;
		}
		return existing;
	}

	public boolean contains(String key)
	{
		return properties.containsKey(key);
	}

	public String getString(String key)
	{
		return properties.getProperty(key);
	}

	public void putString(String key, String value)
	{
		properties.put(key,value);

		try (FileOutputStream fos=new FileOutputStream(fullFilename.toString()))
		{
			properties.store(fos,"");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException("Error guardando propiedades en "+fullFilename,ex);
		}
	}

	public String putStringIfAbsent(String key, String value)
	{
		String existing=getString(key);
		if(existing==null)
		{
			putString(key,value);
			existing=value;
		}
		return existing;
	}

	// -----------------------------------------------------------
	// Obtención de keys y valores
	// -----------------------------------------------------------
	public List<String> getAllKeys()
	{
		return new ArrayList<>(properties.stringPropertyNames());
	}

	public List<String> getAllKeys(String domain)
	{
		List<String> result=new ArrayList<>();
		if(properties==null||properties.isEmpty()) return result;

		String prefix=(domain==null||domain.isEmpty())?"":domain;
		if(!prefix.isEmpty()&&!prefix.endsWith(".")) prefix+=".";

		for(String key:properties.stringPropertyNames())
		{
			if(key.startsWith(prefix)) result.add(key);
		}

		return result;
	}

	private List<Object> _getValues(List<String> keys)
	{
		List<Object> ret=new ArrayList<>();
		for(String k:keys)
			ret.add(getObject(k));
		return ret;
	}

	public List<Object> getAllValues()
	{
		return _getValues(getAllKeys());
	}

	public List<Object> getAllValues(String domain)
	{
		return _getValues(getAllKeys(domain));
	}

	public List<Pair> getAll()
	{
		return _getAll(getAllKeys());
	}

	public List<Pair> getAll(String domain)
	{
		return _getAll(getAllKeys(domain));
	}

	private List<Pair> _getAll(List<String> keys)
	{
		List<Pair> ret=new ArrayList<>();
		for(String k:keys)
			ret.add(new Pair(k,getObject(k)));
		return ret;
	}

	public static class Pair
	{
		public final String key;
		public final Object value;

		public Pair(String key,Object value)
		{
			this.key=key;
			this.value=value;
		}
	}
}
