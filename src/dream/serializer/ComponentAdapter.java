package dream.serializer;

import com.google.gson.*;
import dream.components.Component;

import java.lang.reflect.Type;

public class ComponentAdapter implements JsonDeserializer<Component>, JsonSerializer<Component>
{
    @Override
    public JsonElement serialize(Component component, Type type, JsonSerializationContext jsonSerializationContext)
    {
        JsonObject object = new JsonObject();
        object.add("type", new JsonPrimitive(component.getClass().getCanonicalName()));
        object.add("properties", jsonSerializationContext.serialize(component, component.getClass()));
        return object;
    }

    @Override
    public Component deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject object = jsonElement.getAsJsonObject();
        String objectType = object.get("type").getAsString();
        JsonElement element = object.get("properties");
        try
        {
            return jsonDeserializationContext.deserialize(element, Class.forName(objectType));
        }
        catch (ClassNotFoundException ex)
        {
            throw new JsonParseException("Unknown Type: " + objectType, ex);
        }
    }
}
