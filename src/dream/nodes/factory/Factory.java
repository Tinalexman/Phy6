package dream.nodes.factory;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public abstract class Factory
{
    protected int[] maximumNodes = {500};

    protected float[] nodeVertexArray;
    protected int[] nodeIndexArray;

    protected int nodeVAO;
    protected int vertexVBO, indexVBO;

    public Factory()
    {

    }

    public abstract void initialize();
    public abstract void prepareNodes(int size);

    public void enable()
    {
        glBindVertexArray(this.nodeVAO);
        glEnableVertexAttribArray(0);
    }

    public void disable()
    {
        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
    }

}
