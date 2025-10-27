package thejavalistener.fwkutils.reflect.gpt;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import thejavalistener.fwkutils.reflect.MyMethod;

/**
 * Utilidades para manipular instancias mediante reflexión:
 * lectura y escritura de atributos, getters/setters, clonación superficial, etc.
 */
public class MyObjectGPT
{
	// -------------------------------------------------------------
	//  Lectura de valores
	// -------------------------------------------------------------

	public static Object getFieldValue(Object target, String fieldName)
	{
		if (target == null || fieldName == null)
			return null;

		try
		{
			Field field = MyClassGPT.getDeclaredField(target.getClass(), fieldName);
			if (field == null)
				return null;

			field.setAccessible(true);
			return field.get(target);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static Object invokeGetter(Object target, String attribute)
	{
		if (target == null || attribute == null)
			return null;

		try
		{
			String getterName = MyMethod.getGetterName(attribute);
			Method m = target.getClass().getMethod(getterName);
			return m.invoke(target);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	// -------------------------------------------------------------
	//  Escritura de valores
	// -------------------------------------------------------------

	public static void setFieldValue(Object target, String fieldName, Object value)
	{
		if (target == null || fieldName == null)
			return;

		try
		{
			Field field = MyClassGPT.getDeclaredField(target.getClass(), fieldName);
			if (field == null)
				return;

			field.setAccessible(true);
			field.set(target, value);
		}
		catch (Exception ignored) { }
	}

	public static void invokeSetter(Object target, String attribute, Object value)
	{
		if (target == null || attribute == null)
			return;

		try
		{
			String setterName = MyMethod.getSetterName(attribute);
			Field field = MyClassGPT.getDeclaredField(target.getClass(), attribute);
			Class<?> type = (field != null) ? field.getType() : (value != null ? value.getClass() : Object.class);

			Method m = target.getClass().getMethod(setterName, type);
			m.invoke(target, value);
		}
		catch (Exception ignored) { }
	}

	// -------------------------------------------------------------
	//  Obtención de atributos y valores tipo bean
	// -------------------------------------------------------------

	/**
	 * Devuelve los nombres de atributos con getter y setter públicos.
	 */
	public static List<String> getAttributes(Class<?> clazz)
	{
		List<String> result = new ArrayList<>();
		if (clazz == null)
			return result;

		Method[] methods = clazz.getMethods();
		for (Method m : methods)
		{
			if (!MyMethodGPT.isGetter(m))
				continue;

			String attr = MyMethodGPT.attributeFromGetter(m.getName());
			if (hasSetter(clazz, attr))
				result.add(attr);
		}
		return result;
	}

	public static List<String> getAttributes(Object obj)
	{
		return (obj == null) ? List.of() : getAttributes(obj.getClass());
	}

	/**
	 * Devuelve los valores actuales de todos los atributos detectados como bean.
	 */
	public static List<Object> getValues(Object obj)
	{
		List<Object> values = new ArrayList<>();
		if (obj == null)
			return values;

		for (String attr : getAttributes(obj))
			values.add(invokeGetter(obj, attr));

		return values;
	}

	// -------------------------------------------------------------
	//  Clonación superficial
	// -------------------------------------------------------------

	@SuppressWarnings("unchecked")
	public static <T> T shallowClone(T src)
	{
		if (src == null)
			return null;

		try
		{
			T clone = (T) MyClassGPT.newInstance(src.getClass());
			List<Field> fields = MyClassGPT.getDeclaredFields(src.getClass(), true);

			for (Field f : fields)
			{
				f.setAccessible(true);
				Object value = f.get(src);
				f.set(clone, value);
			}
			return clone;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	// -------------------------------------------------------------
	//  Auxiliares privados
	// -------------------------------------------------------------

	private static boolean hasSetter(Class<?> clazz, String attr)
	{
		try
		{
			String setter = MyMethod.getSetterName(attr);
			for (Method m : clazz.getMethods())
				if (m.getName().equals(setter) && m.getParameterCount() == 1)
					return true;
		}
		catch (Exception ignored) { }
		return false;
	}
}
