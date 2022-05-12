package dream.assets;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Texture
{
    private final TextureData textureData;
    private transient int textureID;

    public Texture(String filePath)
    {
        this.textureData = new TextureData();
        this.textureData.filePath = filePath;
    }

    public void onDestroyRequested()
    {
        glDeleteTextures(this.textureID);
    }

    public String getFilePath()
    {
        return this.textureData.filePath;
    }

    public int getWidth()
    {
        return this.textureData.width;
    }

    public int getHeight()
    {
        return this.textureData.height;
    }

    public int getTextureID()
    {
        return this.textureID;
    }

    public Texture(int textureWidth, int textureHeight)
    {
        this.textureID = glGenTextures();
        this.textureData = new TextureData();
        this.textureData.filePath = "Generated";
        glBindTexture(GL_TEXTURE_2D, this.textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, textureWidth, textureHeight, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
    }

    private void loadTextureData()
    {
        if(this.textureData.filePath.equals(""))
            return;

        try(MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true);
            this.textureData.buffer = STBImage.stbi_load(this.textureData.filePath, w, h, channels, 4);
            if(this.textureData.buffer == null)
                throw new Exception("Cannot load texture file: " + this.textureData.filePath
                        + " due to " + STBImage.stbi_failure_reason());

            this.textureData.width = w.get();
            this.textureData.height = h.get();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void loadTexture()
    {
        loadTextureData();
        if(this.textureData.buffer != null)
            return;

        this.textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.textureID);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.textureData.width, this.textureData.height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, this.textureData.buffer);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glGenerateMipmap(GL_TEXTURE_2D);

        if(this.textureData.buffer != null)
            STBImage.stbi_image_free(this.textureData.buffer);
    }

    static class TextureData
    {
        public int width = 0;
        public int height = 0;
        public ByteBuffer buffer = null;
        public String filePath = "";
    }

}
