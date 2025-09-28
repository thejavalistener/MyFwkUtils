package thejavalistener.fwk.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Supplier;

public class MyMap<K,V> 
{
	private LinkedHashMap<K,V> map;
	
	public MyMap()
	{
		map = new LinkedHashMap<>();
	}
	
	public void put(K k,V v)
	{
		map.put(k,v);
	}
	
	public V get(K k)
	{
		return map.get(k);
	}
	
	public V discover(K k,Supplier<V> ifAddIsNeeded)
	{
		V v = map.get(k);
		if( v==null )
		{
			v = ifAddIsNeeded.get();
			map.put(k,v);
		}
		
		return v;
	}
	
	public List<K> keys()
	{
		return new ArrayList<>(map.keySet());
	}
	
	public V getByIndex(int idx)
	{
		return map.get(keys().get(idx));
	}
}
