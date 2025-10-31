package thejavalistener.fwkutils.various;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.gson.JsonObject;

/**
 * Permite guardar y recuperar valores (Strings u objetos arbitrarios) en un
 * archivo .properties, usando JSON con metadata de tipo.
 */

public class MyFileProperties
{
	private String filename;
	private Properties properties;

	public MyFileProperties(String filename)
	{
		_load(filename);
	}

	private void _load(String filename)
	{
		this.filename=filename;
		MyFile.createIfNotExists(filename);
		properties=new Properties();

		try (FileInputStream fis=new FileInputStream(filename))
		{
			properties.load(fis);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException("Error cargando propiedades de "+filename,ex);
		}

	}

	// -----------------------------------------------------------
	// Escritura genérica
	// -----------------------------------------------------------
	public void putObject(String key, Object value)
	{
		if(value==null) throw new IllegalArgumentException("No se puede guardar un valor nulo para la clave: "+key);

		String json;

		// 🔸 Caso especial: Class<?>
		if (value instanceof Class<?> clazz)
		{
		    JsonObject jsonObj = new JsonObject();
		    jsonObj.addProperty("type", "java.lang.Class");
		    jsonObj.addProperty("data", clazz.getName());
		    json = jsonObj.toString();
		}
		else
		{
		    json = MyJson.toJson(value);
		}

		
		putString(key,json);
	}

	// -----------------------------------------------------------
	// Lectura genérica
	// -----------------------------------------------------------

	@SuppressWarnings("unchecked")
	public <T> T getObject(String key)
	{
	    String value = getString(key);
	    if (value == null) return null;

	    Object obj = MyJson.fromJson(value);
	    if (obj == null) return null;

	    // 🔹 Normalizar colecciones
	    if (obj instanceof List<?> list)
	        return (T) new ArrayList<>(list);

	    if (obj instanceof Map<?, ?> map)
	        return (T) new LinkedHashMap<>(map);

	    // 🔹 Caso general: devuelve tal cual
	    return (T) obj;
	}

	
	// -----------------------------------------------------------
	// Métodos auxiliares
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
		try (FileOutputStream fos=new FileOutputStream(filename))
		{
			properties.store(fos,"");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException("Error guardando propiedades en "+filename,ex);
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
	// Demo de uso
	// -----------------------------------------------------------
	public static void main(String[] args)
	{
		MyFileProperties fp=new MyFileProperties("demo.properties");

		fp.putObject("x","java.lang.String");
		String s = fp.getObject("x");
		System.out.println(s);
		
		// String
		fp.putString("s","Mi cadena");
		System.out.println(fp.getString("s"));

		// Objeto
		Persona p=new Persona();
		p.setDni(111222333);
		p.setNombre("Pablito");
		p.setDireccion("Av. San Martín 1234");
		fp.putObject("per",p);
		p = fp.getObject("per");
		System.out.println(p);

		// Integer
		fp.putObject("i",123);
		int i = fp.getObject("i");
		System.out.println(i);

		// Date
		Date d=new Date(System.currentTimeMillis());
		fp.putObject("d",d);
		d=fp.getObject("d");
		System.out.println(d);

		// Class
		fp.putObject("clazz",String.class);
		Class<?> z = fp.getObject("clazz");
		System.out.println(z);

		// List
		List<Integer> lista=List.of(10,20,30);
		fp.putObject("nums",lista);
		List<?> lst = fp.getObject("nums");
		System.out.println(lst);

		// Map
		Map<String,Object> mapa=Map.of("a",1,"b","hola");
		fp.putObject("mapa",mapa);
		Map<?,?> m = fp.getObject("mapa");
		System.out.println(m);
	}
	
	static class Persona
	{
		private String nombre;
		private String direccion;
		private int dni;
		public String getNombre()
		{
			return nombre;
		}
		public void setNombre(String nombre)
		{
			this.nombre=nombre;
		}
		public String getDireccion()
		{
			return direccion;
		}
		public void setDireccion(String direccion)
		{
			this.direccion=direccion;
		}
		public int getDni()
		{
			return dni;
		}
		public void setDni(int dni)
		{
			this.dni=dni;
		}
		@Override
		public String toString()
		{
			return "Persona [nombre="+nombre+", direccion="+direccion+", dni="+dni+"]";
		}
	}
	
	// -----------------------------------------------------------
	// Obtención de keys por dominio
	// -----------------------------------------------------------
	public List<String> getAllKeys(String domain)
	{
	    List<String> result = new ArrayList<>();
	    if (properties == null || properties.isEmpty())
	        return result;

	    // Normaliza el dominio: si no termina en ".", se lo agrega
	    String prefix = (domain == null || domain.isEmpty()) ? "" : domain;
	    if (!prefix.isEmpty() && !prefix.endsWith(".")) {
	        prefix += ".";
	    }

	    for (String key : properties.stringPropertyNames()) {
	        if (key.startsWith(prefix)) {
	            result.add(key);
	        }
	    }

	    return result;
	}
	
	public List<Object> getAllValues()
	{
		return _getValues(getAllKeys());
	}
	
	public List<Object> getAllValues(String domain)
	{
		return _getValues(getAllKeys(domain));
	}
	
	public List<Pair> getAll(String domain)
	{
		return _getAll(getAllKeys(domain));
	}
	public List<Pair> getAll()
	{
		return _getAll(getAllKeys());
	}
	
	public List<Pair> _getAll(List<String> keys)
	{
		List<Pair> ret = new ArrayList<>();
		for(String k:keys)
		{
			ret.add(new Pair(k,getObject(k)));
		}
		
		return ret;		
	}
	
	private List<Object> _getValues(List<String> keys)
	{
		List<Object> ret = new ArrayList<>();
		for(String k:keys)
		{
			ret.add(getObject(k));
		}
		
		return ret;
	}


	// -----------------------------------------------------------
	// Sobrecarga sin parámetros: devuelve todas las claves
	// -----------------------------------------------------------
	public List<String> getAllKeys()
	{
	    return new ArrayList<>(properties.stringPropertyNames());
	}
}
