package thejavalistener.fwkutils.various;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MyJson
{
	public static String toJson(Object value)
	{
		Gson gson = new GsonBuilder().create();
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type",value.getClass().getName());
		jsonObject.add("data",gson.toJsonTree(value));
		
		return gson.toJson(jsonObject);
	}
	
	public static <T> T fromJson(String jsonString)
	{
        Gson gson = new GsonBuilder().create();
		
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        String className = jsonObject.get("type").getAsString();
        JsonElement data = jsonObject.get("data");

        
        
        // Deserializa el objeto usando la información del tipo
        Class<?> clazz = MyReflection.clasz.forName(className);
        T deserializedObject = (T)gson.fromJson(data, clazz);
		
		return deserializedObject;
	}	
}
