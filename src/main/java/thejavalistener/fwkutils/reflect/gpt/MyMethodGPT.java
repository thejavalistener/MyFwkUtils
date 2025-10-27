
package thejavalistener.fwkutils.reflect.gpt;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidades para trabajar con métodos mediante reflexión:
 * detección de getters/setters, invocación dinámica, nombres estándar, etc.
 */
public class MyMethodGPT
{
	// -------------------------------------------------------------
	//  Nombres de getters y setters
	// -------------------------------------------------------------

	public static String getGetterName(String attribute)
	{
		if (attribute == null || attribute.isEmpty())
			return null;
		return "get" + Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
	}

	public static String getSetterName(String attribute)
	{
		if (attribute == null || attribute.isEmpty())
			return null;
		return "set" + Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
	}

	public static String attributeFromGetter(String getterName)
	{
		if (getterName == null)
			return null;
		if (getterName.startsWith("get") && getterName.length() > 3)
			return Character.toLowerCase(getterName.charAt(3)) + getterName.substring(4);
		if (getterName.startsWith("is") && getterName.length() > 2)
			return Character.toLowerCase(getterName.charAt(2)) + getterName.substring(3);
		return null;
	}

	// -------------------------------------------------------------
	//  Detección de tipo de método
	// -------------------------------------------------------------

	public static boolean isGetter(Method m)
	{
		if (m == null)
			return false;
		if (m.getParameterCount() != 0)
			return false;

		String name = m.getName();
		Class<?> ret = m.getReturnType();

		if (name.startsWith("get") && !ret.equals(void.class))
			return true;

		if (name.startsWith("is") && (ret.equals(boolean.class) || ret.equals(Boolean.class)))
			return true;

		return false;
	}

	public static boolean isSetter(Method m)
	{
		if (m == null)
			return false;
		if (m.getParameterCount() != 1)
			return false;

		String name = m.getName();
		return name.startsWith("set") && m.getReturnType().equals(void.class);
	}

	// -------------------------------------------------------------
	//  Búsqueda e invocación dinámica
	// -------------------------------------------------------------

	/**
	 * Busca un método por nombre y cantidad de parámetros (en la jerarquía).
	 */
	public static Method getMethod(Class<?> clazz, String name, int paramCount)
	{
		if (clazz == null || name == null)
			return null;

		Class<?> aux = clazz;
		while (aux != null && !aux.equals(Object.class))
		{
			for (Method m : aux.getDeclaredMethods())
			{
				if (m.getName().equals(name) && m.getParameterCount() == paramCount)
					return m;
			}
			aux = aux.getSuperclass();
		}
		return null;
	}

	/**
	 * Ejecuta un método de nombre dado sobre un objeto con parámetros opcionales.
	 */
	public static Object invoke(Object target, String methodName, Object... args)
	{
		if (target == null || methodName == null)
			return null;

		try
		{
			Class<?>[] types = new Class<?>[args != null ? args.length : 0];
			for (int i = 0; i < types.length; i++)
				types[i] = (args[i] != null) ? args[i].getClass() : Object.class;

			Method m = getCompatibleMethod(target.getClass(), methodName, types);
			if (m == null)
				return null;

			m.setAccessible(true);
			return m.invoke(target, args);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	// -------------------------------------------------------------
	//  Auxiliares privados
	// -------------------------------------------------------------

	private static Method getCompatibleMethod(Class<?> clazz, String name, Class<?>[] paramTypes)
	{
		List<Method> candidates = new ArrayList<>();
		Class<?> aux = clazz;

		while (aux != null && !aux.equals(Object.class))
		{
			for (Method m : aux.getDeclaredMethods())
				if (m.getName().equals(name))
					candidates.add(m);
			aux = aux.getSuperclass();
		}

		for (Method m : candidates)
		{
			if (paramTypes.length == m.getParameterCount())
				return m;
		}
		return null;
	}
}
