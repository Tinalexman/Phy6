package dream.io.output;

import dream.assets.Texture;

import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer
{
    private final int fboID;
    private final int rboID;
    private final Texture texture;

    protected FrameBuffer(int frameWidth, int frameHeight)
    {
        this.fboID = glGenFramebuffers();
        bindFrameBuffer();

        this.texture = new Texture(frameWidth, frameHeight);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getTextureID(), 0);

        this.rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, frameWidth, frameHeight);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new RuntimeException("FrameBuffer is not complete!");

        unbindFrameBuffer();
    }

    public void bindFrameBuffer()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, this.fboID);
    }

    public void unbindFrameBuffer()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getTextureID()
    {
        return this.texture.getTextureID();
    }

    public void destroy()
    {
        glDeleteRenderbuffers(this.rboID);
        this.texture.onDestroyRequested();
        glDeleteFramebuffers(this.fboID);
    }

}
