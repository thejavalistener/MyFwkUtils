package thejavalistener.fwkutils.reflect.gpt;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MyClassGPT
{
	// -------------------------------------------------------------
	// Instanciación dinámica
	// -------------------------------------------------------------

	public static Class<?> forName(String className)
	{
		try
		{
			return Class.forName(className);
		}
		catch(ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static <T> T newInstance(Class<T> clazz)
	{
		try
		{
			Constructor<T> ctor=clazz.getDeclaredConstructor();
			ctor.setAccessible(true);
			return ctor.newInstance();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	// -------------------------------------------------------------
	// Tipos finales (no analizables)
	// -------------------------------------------------------------

	public static boolean isFinalType(Class<?> clazz)
	{
		if(clazz==null) return false;

		return clazz.isPrimitive()||clazz.equals(String.class)||Number.class.isAssignableFrom(clazz)||clazz.equals(Boolean.class)||clazz.equals(Character.class)
				||java.util.Date.class.isAssignableFrom(clazz)||clazz.isEnum();
	}

	// -------------------------------------------------------------
	// Campos declarados
	// -------------------------------------------------------------

	/**
	 * Devuelve el Field con el nombre dado, buscando también en superclases.
	 */
	public static Field getDeclaredField(Class<?> clazz, String name)
	{
		if(clazz==null) return null;

		try
		{
			return clazz.getDeclaredField(name);
		}
		catch(NoSuchFieldException e)
		{
			return getDeclaredField(clazz.getSuperclass(),name);
		}
	}

	/**
	 * Devuelve todos los campos de una clase, incluyendo los heredados si se
	 * indica.
	 */
	public static List<Field> getDeclaredFields(Class<?> clazz, boolean includeSuper)
	{
		List<Field> result=new ArrayList<>();
		if(clazz==null) return result;

		for(Field f:clazz.getDeclaredFields())
			result.add(f);

		if(includeSuper) result.addAll(getDeclaredFields(clazz.getSuperclass(),true));

		return result;
	}

	/**
	 * Devuelve todos los campos que cumplan la condición indicada.
	 */
	public static List<Field> getDeclaredFields(Class<?> clazz, Function<Field,Boolean> filter)
	{
		List<Field> fields=getDeclaredFields(clazz,true);
		if(filter==null) return fields;

		List<Field> result=new ArrayList<>();
		for(Field f:fields)
		{
			try
			{
				if(Boolean.TRUE.equals(filter.apply(f))) result.add(f);
			}
			catch(Exception ignored)
			{
			}
		}
		return result;
	}

	/**
	 * Devuelve todos los campos anotados con una anotación dada.
	 */
	public static List<Field> getDeclaredFields(Class<?> clazz, Class<? extends Annotation> annClass)
	{
		if(clazz==null||annClass==null) return List.of();

		return getDeclaredFields(clazz,f -> f.isAnnotationPresent(annClass));
	}

	// -------------------------------------------------------------
	// Métodos generales sobre anotaciones
	// -------------------------------------------------------------

	/**
	 * Indica si la clase del objeto o la clase dada tiene la anotación
	 * indicada.
	 */
	public static boolean isAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annClass)
	{
		if(annClass==null) return false;
		return clazz.isAnnotationPresent(annClass);
	}

	// -------------------------------------------------------------
	// Utilidades varias
	// -------------------------------------------------------------

	/**
	 * Devuelve el nombre completo (package + simpleName) de la clase dada.
	 */
	public static String getFullName(Class<?> clazz)
	{
		if(clazz==null) return null;
		Package p=clazz.getPackage();
		return (p!=null?p.getName()+".":"")+clazz.getSimpleName();
	}

	/**
	 * Devuelve el nombre simple de la clase sin package.
	 */
	public static String getSimpleName(Object o)
	{
		if(o==null) return null;
		return (o instanceof Class<?> c)?c.getSimpleName():o.getClass().getSimpleName();
	}
}
