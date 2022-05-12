package dream.scene;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dream.components.Component;
import dream.nodes.Node;
import dream.serializer.ComponentAdapter;
import dream.serializer.NodeAdapter;
import dream.serializer.SceneAdapter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SceneManager
{
    private static Gson gson;

    public static void initialize()
    {
        SceneManager.gson = new GsonBuilder()
                            .setPrettyPrinting()
                            .registerTypeAdapter(Node.class, new NodeAdapter())
                            .registerTypeAdapter(EditorScene.class, new SceneAdapter())
                            .registerTypeAdapter(Component.class, new ComponentAdapter())
                            .create();
    }

    public static void saveScene(EditorScene scene, String sceneFilePath)
    {
        try
        {
            FileWriter writer = new FileWriter(sceneFilePath + EditorScene.sceneExtension);
            writer.write(gson.toJson(scene));
            writer.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public static EditorScene loadScene(String sceneFilePath)
    {
        EditorScene scene = null;
        try
        {
            String sceneData = new String(Files.readAllBytes(Paths.get(sceneFilePath + EditorScene.sceneExtension)));
            if(!sceneData.equals(""))
            {
                scene = gson.fromJson(sceneData, EditorScene.class);
                scene.loaded();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return scene;
    }
}
