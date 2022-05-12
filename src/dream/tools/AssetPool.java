package dream.tools;

import dream.DreamEngine;
import dream.assets.Texture;
import dream.shader.Shader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class AssetPool
{
    private static final Texture[] systemIcons = new Texture[SystemIcon.totalIcons * 2];

    private static final Map<String, Shader> SHADERS = new HashMap<>();
    private static final Map<String, Texture> TEXTURES = new HashMap<>();

    public static Shader getShader(String resourceName)
    {
        String path = DreamEngine.resourcePath + "\\shaders\\" + resourceName;
        return AssetPool.SHADERS.getOrDefault(path, null);
    }

    public static void addShader(String resourceName)
    {
        String path = DreamEngine.resourcePath + "\\shaders\\" + resourceName;
        if(AssetPool.SHADERS.containsKey(path))
            return;

        Shader shader = new Shader(path);
        shader.createShaderObject();
        AssetPool.SHADERS.put(path, shader);
    }

    public static Texture getTexture(String resourceName)
    {
        String path = DreamEngine.resourcePath + "\\textures\\" + resourceName;
        return AssetPool.TEXTURES.getOrDefault(path, null);
    }

    public static Texture getTexture(int systemIcon, boolean dark)
    {
        if(systemIcon == 0)
            return null;
        return systemIcons[systemIcon + (dark ? SystemIcon.totalIcons : 0)];
    }

    public static void addTexture(String resourceName)
    {
        String path = DreamEngine.resourcePath + "\\textures" + resourceName;
        if(AssetPool.TEXTURES.containsKey(path))
            return;

        Texture texture = new Texture(path);
        texture.loadTexture();
        AssetPool.TEXTURES.put(path, texture);

        int systemIconID = getSystemIcon(resourceName);
        if(systemIconID != 0)
            systemIcons[systemIconID] = texture;
    }

    public static Shader addAndGetShader(String resourceName)
    {
        Shader shader = getShader(resourceName);
        if(shader == null)
            addShader(resourceName);
        return shader;
    }

    public static Texture addAndGetTexture(String resourceName)
    {
        Texture texture = getTexture(resourceName);
        if(texture == null)
            addTexture(resourceName);
        return texture;
    }

    public static ArrayList<Texture> getAllTextures()
    {
        return new ArrayList<>(AssetPool.TEXTURES.values());
    }

    public static ArrayList<Shader> getAllShaders()
    {
        return new ArrayList<>(AssetPool.SHADERS.values());
    }

    private static int getSystemIcon(String textureName)
    {
        // Note: If you want to add a new system icon, add it here, make sure it is named appropriately
        // E.G (SystemLightBlahBlahBlah.whatever)
        // And also change the total variable in the SystemIcon class.
        int ID = 0;
        textureName = textureName.substring(8);

        if(textureName.startsWith("System"))
        {
            if(textureName.contains("Document"))
                ID = SystemIcon.document;
            else if(textureName.contains("Done"))
                ID = SystemIcon.done;
            else if(textureName.contains("File"))
                ID = SystemIcon.newFile;
            else if(textureName.contains("Folder"))
                ID = SystemIcon.folder;
            else if(textureName.contains("Menu"))
                ID = SystemIcon.menu;
            else if(textureName.contains("Play"))
                ID = SystemIcon.play;
            else if(textureName.contains("Save"))
                ID = SystemIcon.save;
            else if(textureName.contains("Stop"))
                ID = SystemIcon.stop;
            else if(textureName.contains("Trash"))
                ID = SystemIcon.trash;
            else if(textureName.contains("Wait"))
                ID = SystemIcon.wait;

            if(ID != 0 && textureName.contains("Dark"))
                ID += SystemIcon.totalIcons;
        }
        return ID;
    }

    public static Texture[] getSystemIcons()
    {
        return AssetPool.systemIcons;
    }
}
