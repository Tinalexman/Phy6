package dream.scene;

import dream.nodes.Node;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorScene
{
    public static String sceneExtension = ".scn";

    public Map<String, Node> sceneObjects;
    public transient boolean hasLoaded;
    public String name;
    public transient RuntimeScene runtimeScene;
    public boolean[] saved;

    public EditorScene()
    {
        this("New Scene");
    }

    public EditorScene(String sceneName)
    {
        this.sceneObjects = new HashMap<>();
        this.runtimeScene = new RuntimeScene();
        this.name = sceneName;
        this.hasLoaded = false;
        this.saved = new boolean[]{false};
    }

    public List<Node> getSceneObjects(boolean getEditorScene)
    {
        List<Node> nodes = new ArrayList<>(this.sceneObjects.values());
        return getEditorScene ? nodes : List.copyOf(nodes);
    }

    public int size()
    {
        return this.sceneObjects.size();
    }

    public boolean hasLoaded()
    {
        return this.hasLoaded;
    }

    public void loaded()
    {
        this.hasLoaded = true;
    }

    public void addSceneObject(Node node)
    {
        this.sceneObjects.put(node.name, node);
        this.saved[0] = false;
        node.start();
    }

    public void removeSceneObject(Node node)
    {
        node.removeAllComponents();
        this.sceneObjects.remove(node.name);
        this.saved[0] = false;
    }

    public <T extends Node> int getNextOrdinalFor(Class<T> componentClass)
    {
        List<T> T_nodes = new ArrayList<>();
        for(Node node : sceneObjects.values())
        {
            if(node.getClass().isAssignableFrom(componentClass))
                T_nodes.add(componentClass.cast(node));
        }

        int lastIndex = 0;

        for(T t : T_nodes)
        {
            String className = t.getClass().getSimpleName();
            String nodeName = t.name;
            int nameLength = className.length();
            if(nodeName.startsWith(className))
            {
                int index;
                String tIndex = nodeName.substring(nameLength).trim();
                try
                {
                    index = Integer.parseInt(tIndex);
                    if(index >= lastIndex)
                        lastIndex = index;
                }
                catch (NumberFormatException ignored)
                {

                }
            }
        }
        return lastIndex + 1;
    }

    public List<String> formatNodes(List<Node> nodes)
    {
        List<String> names = new ArrayList<>();
        nodes.forEach(node -> names.add(node.name));
        return names;
    }

    public Node getNode(int ID)
    {
        for(Node node : this.sceneObjects.values())
        {
            if(node.ID == ID)
                return node;
        }
        return null;
    }

    public boolean isSaved()
    {
        return this.saved[0];
    }

    public void setSaved(boolean saved)
    {
        this.saved[0] = saved;
    }

    public boolean[] getSaved()
    {
        return this.saved;
    }
}
