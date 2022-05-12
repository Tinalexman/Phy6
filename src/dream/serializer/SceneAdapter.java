package dream.serializer;

import com.google.gson.*;
import dream.scene.EditorScene;

import java.lang.reflect.Type;

public class SceneAdapter implements JsonSerializer<EditorScene>, JsonDeserializer<EditorScene>
{

    @Override
    public EditorScene deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        return null;
    }

    @Override
    public JsonElement serialize(EditorScene scene, Type type, JsonSerializationContext jsonSerializationContext)
    {
        return null;
    }
}
