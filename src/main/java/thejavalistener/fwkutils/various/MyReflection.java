package thejavalistener.fwkutils.various;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import thejavalistener.fwkutils.string.MyString;

public class MyReflection
{
	static class runtime
	{
		public static void unsoportedMethod()
		{
			StackTraceElement ste[]=Thread.currentThread().getStackTrace();
			int p=ste.length>2?2:0;
			throw new RuntimeException("Unsoported Method:"+ste[p].getMethodName()+" in class:"+ste[p].getClassName());
		}

		public static StackTraceElement peekStackTrace()
		{
			StackTraceElement ste[]=Thread.currentThread().getStackTrace();
			int p=ste.length>3?3:0;
			return ste[p];
		}		
	}

	public static class clasz
	{
		public static List<Class<?>> getInnerClasses(Class<?> clazz)
		{
			return getInnerClasses(clazz,c->true);		
		}

		public static List<Class<?>> getInnerClasses(Class<?> clazz,Function<Class<?>,Boolean> includeInResult)
		{
			try
			{
				List<Class<?>> ret = new ArrayList<>();
				for(Class<?> c:clazz.getDeclaredClasses())
				if( includeInResult.apply(c) )
				{
					ret.add(c);
				}
				
				return ret;				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		
		public static Method getMethod(Class<?> clazz,String mtdName,List<Class<?>> argTypes)
		{
			Class<?>[] arrArgTypes = MyCollection.toArray(argTypes,Class.class);
			return getMethod(clazz,mtdName,arrArgTypes);
		}
		
		public static Method getMethod(Class<?> clazz,String mtdName,Class<?> argTypes[])
		{
			try
			{
				Method mtd = null;
				Class<?> aux = clazz;
				while( !aux.equals(Object.class) && mtd==null )
				{
					// todos los metodos de la clase
					Method mtds[] = aux.getDeclaredMethods();

					// filtro los metodos que coinciden con el nombre mtdName
					List<Method> mtds1 = MyCollection.extract(mtds,(t)->t,(t)->t.getName().equals(mtdName));

					if( mtds1.size()>0 )
					{
						// filtro por cantidad de parametros
						List<Method> mtds2 = MyCollection.extract(mtds1,(t)->t,(t)->t.getParameterCount()==argTypes.length);
						if( mtds2.size()>0 )
						{
							// filtro por tipo de parametros
							List<Method> mtds3 = MyCollection.extract(mtds2,(t)->t,(t)->_concuerdanParametros(t.getParameterTypes(),argTypes));
							if( mtds3.size()>0 )
							{
								return mtds3.get(0);
							}
						}
					}
					
					aux = aux.getSuperclass();
				}
				
				return mtd;		
				
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				throw new RuntimeException();
			}		
		}
		
		private static boolean _concuerdanParametros(Class<?> paramTypes[],Class<?> argTypes[])
		{
			for(int i=0;i<paramTypes.length;i++)
			{
				Class<?> p = getWrapperFor(paramTypes[i]);
				Class<?> a = getWrapperFor(argTypes[i]);
				if( !p.equals(a) )
				{
					return false;
				}
			}
			
			return true;
		}

		public static Class<?> getWrapperFor(Class<?> primitiveClass)
		{
			switch( primitiveClass.getName() )
			{
				case "byte":
					return Byte.class;
				case "char":
					return Character.class;
				case "short":
					return Short.class;
				case "int":
					return Integer.class;
				case "long":
					return Long.class;
				case "float":
					return Float.class;
				case "double":
					return Double.class;
				case "boolean":
					return Boolean.class;
				default:
					return primitiveClass;
			}		
		}
		
		
		
		@SuppressWarnings({"unchecked", "rawtypes"})
		public static boolean isAnnotedWith(Class<?> c, Class annClass)
		{
			return c.getAnnotation(annClass)!=null;
		}
		
		public static List<String> getAttributes(Class<?> c)
		{
			ArrayList<String> getters=new ArrayList<>();
			ArrayList<String> setters=new ArrayList<>();

			Method mtds[]=c.getDeclaredMethods();
			for(int i=0; i<mtds.length; i++)
			{
				String mtdName=mtds[i].getName();
				if(mtdName.startsWith("get"))
				{
					getters.add(mtdName.substring(3));
				}
				else
				{
					if(mtdName.startsWith("set"))
					{
						setters.add(mtdName.substring(3));
					}
				}
			}

			ArrayList<String> ret=new ArrayList<>();
			for(int i=0; i<getters.size(); i++)
			{
				String mtdName=getters.get(i);
				if(setters.contains(mtdName))
				{
					ret.add(MyString.switchCase(mtdName,0));
				}
			}

			return ret;
		}
		
		/** Retorna los declared fields que cumplan con la condición establecida por $func */
		public static Field[] getDeclaredFields(Class<?> c, Function<Field,Boolean> func)
		{
			Field[] fields=c.getDeclaredFields();
			return Stream.of(fields).filter(f -> func.apply(f)).toArray(Field[]::new);
		}

		/** Retorna los declared fields anotados con annClass */
		@SuppressWarnings({"unchecked", "rawtypes"})
		public static Field[] getDeclaredFields(Class<?> clazz, Class annClass)
		{
			Field[] fields=clazz.getDeclaredFields();
			return Stream.of(fields).filter(f -> f.getAnnotation(annClass)!=null).toArray(Field[]::new);
		}
		
		public static boolean isFinalClass(Class<?> c)
		{
			switch(c.getCanonicalName())
			{
				case "char", "java.lang.Character":
				case "short", "java.lang.Short":
				case "int", "java.lang.Integer":
				case "long", "java.lang.Long":
				case "float", "java.lang.Float":
				case "double", "java.lang.Double":
				case "boolean", "java.lang.Boolean":
				case "java.lang.String":
				case "java.sql.Date":
				case "java.sql.Timestamp":
				case "java.sql.Time":
					return true;
				default:
					return false;
			}
		}
		
		/** Retorna el primer field anotado con $annClass o null si no existe ninguno */
		public static Field getDeclaredField(Class<?> c, Class<?> annClass)
		{
			Field f[]=clasz.getDeclaredFields(c,annClass);
			return f!=null&&f.length>0?f[0]:null;
		}

		/** Es el viejo Class.forName pero capturando la excepción */
		public static Class<?> forName(String classname)
		{
			try
			{
				return Class.forName(classname);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		/** Retorna c.getConstructor().newInstance() pero capturando la excepción */
		public static <T> T newInstance(Class<T> c)
		{
			try
			{
				return c.getConstructor().newInstance();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		public static Object innerClassNewInstance(Object outerInstance, Class<?> inner)
		{
		    try
		    {
		        var ctor = inner.getDeclaredConstructor(outerInstance.getClass());
		        ctor.setAccessible(true);
		        return ctor.newInstance(outerInstance);
		    }
		    catch (Exception e)
		    {
		        throw new RuntimeException(e);
		    }
		}
		
		public static List<Field> getFields(Class<?> clazz,Function<Field,Boolean> filter)
		{
			Field arr[] = clazz.getDeclaredFields();
			return MyCollection.extract(arr,(f)->f,filter);
		}

	}
	
	public static class object
	{
		public static String toString(Object o, Function<Field,Boolean> func)
		{
			StringBuffer sb=new StringBuffer();
			Field[] fields=o.getClass().getDeclaredFields();

			for(int i=0; i<fields.length; i++)
			{
				Field f=fields[i];

				if(func.apply(f))
				{
					sb.append(f.getName()+"="+invokeGetter(o,f.getName()));
					sb.append(i<fields.length-1?",":"");
				}
			}

			return sb.toString();
		}
		
		/** Retorna los fieldValue de los campos de $target */
		public static List<Object> getValues(Object target)
		{
			List<Object> ret=new ArrayList<>();
			for(String att:clasz.getAttributes(target.getClass()))
			{
				ret.add(invokeGetter(target,att));
			}

			return ret;
		}
		
		/** Retorna el valor de $fieldName */
		public static Object getFieldValue(Object target, String fieldName)
		{
			try
			{
				Field f=target.getClass().getDeclaredField(fieldName);
				f.setAccessible(true);
				return f.get(target);
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		
		public static void invokeSetter(Object target,String fieldName,Object value)
		{
			if( value==null ) return;
			String setterName = method.getSetterName(fieldName);
			invokeMethod(target,setterName,new Object[]{value});
		}
		
		public static Object invokeGetter(Object target,String fieldName)
		{
			String getterName = method.getGetterName(fieldName);
			return invokeMethod(target,getterName);
		}
		
		/** Ejemplo: facade.validarPerfil o obj1.obj2.ejecutarTalCosa 
		 *  Los parentesis son opcionales, pueden estar o no. */
		public static Object invokeMethodByPath(Object target,String fullPathMethod,Object ...args)
		{
			// separo el string por "."
			List<String> lst = MyString.split(fullPathMethod,".");
			
			// obtengo el nombre del metodo
			String mtd = lst.get(lst.size()-1);
			
			// recorro a traves del path
			Object aux = target;
			for(int i=0; i<lst.size()-1;i++ )
			{
				String fieldName = lst.get(i);
				aux = object.getFieldValue(aux,fieldName);
			}
			
			// invoco
			return invokeMethod(aux,mtd,args);
		}
		
		/** Simplifica el viejo $target.getClass().getMethod($mtdName,$args) */
		public static Object invokeMethod(Object target,String mtdName,Object ...args)
		{
			try
			{
				List<Class<?>> argTypes = MyCollection.extract(args,(t)->t.getClass());
				Method mtd = clasz.getMethod(target.getClass(),mtdName,argTypes);
				if( mtd!=null )
				{
					return mtd.invoke(target,args);
				}			
				else
				{
					throw new RuntimeException("El metodo: "+mtdName+", no se encuentra en: "+target.getClass().getName());
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	static class method
	{
		public static String getSetterName(String attName)
		{
			return "set"+MyString.switchCase(attName,0);
		}
		
		public static String getGetterName(String attName)
		{
			return "get"+MyString.switchCase(attName,0);
		}
			
		public static String getSetterName(Field f)
		{
			return getSetterName(f.getName());
		}
		
		public static String getGetterName(Field f)
		{
			return "get"+MyString.switchCase(f.getName(),0);
		}		
	}
	
	static class pkg
	{
		public static List<Class<?>> getClasses(String packageName,boolean recursive) 
		{
			return getClasses(packageName,recursive,(c)->true);
		}
		
		public static List<Class<?>> getClasses(String packageName,boolean recursive,Function<Class<?>,Boolean> filter) 
		{
			try
			{
				ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
				assert classLoader!=null;
				
				String path=packageName.replace('.','/');
				Enumeration<URL> resources = classLoader.getResources(path);
				List<File> dirs = new ArrayList<File>();
				while(resources.hasMoreElements())
				{
					URL resource=resources.nextElement();
					dirs.add(new File(resource.getFile()));
				}
				
				List<Class<?>> ret = new ArrayList<>();
				for(File directory:dirs)
				{
					ret.addAll(_findClasses(directory,packageName,recursive));
				}
				
				ret = MyCollection.extract(ret,(t)->t,filter);
				return ret;
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		private static List<Class<?>> _findClasses(File dir, String auxPkgName,boolean recursive) 
		{
			try
			{
				List<Class<?>> ret = new ArrayList<>();
				if(!dir.exists())
				{
					return ret;
				}
				
				File[] files=dir.listFiles();
				for(File file:files)
				{
					if(file.isDirectory() && recursive)
					{
						assert !file.getName().contains(".");
						ret.addAll(_findClasses(file,auxPkgName+"."+file.getName(),recursive));
					}
					else if(file.getName().endsWith(".class"))
					{
						Class<?> clazz = Class.forName(auxPkgName+'.'+file.getName().substring(0,file.getName().length()-6));
						ret.add(clazz);
					}
				}
				return ret;			
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
	}

}
