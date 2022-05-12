package dream.serializer;

import com.google.gson.*;
import dream.components.Component;
import dream.nodes.Node;

import java.lang.reflect.Type;

public class NodeAdapter implements JsonDeserializer<Node>, JsonSerializer<Node>
{

    @Override
    public Node deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject object = jsonElement.getAsJsonObject();
        String name = object.get("name").getAsString();
        JsonArray components = object.getAsJsonArray("components");
        int id = jsonDeserializationContext.deserialize(object.get("ID"), int.class);
        Node node = new Node(name, id);
        for(JsonElement element : components)
        {
            Component component = jsonDeserializationContext.deserialize(element, Component.class);
            node.addComponent(component);
        }
        return node;
    }

    @Override
    public JsonElement serialize(Node node, Type type, JsonSerializationContext jsonSerializationContext)
    {
        JsonObject object = new JsonObject();
        object.add("name", new JsonPrimitive(node.getClass().getCanonicalName()));
        object.add("ID", new JsonPrimitive(node.ID));
        return object;
    }
}
