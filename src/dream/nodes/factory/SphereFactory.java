package dream.nodes.factory;

import dream.nodes.Node;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class SphereFactory extends Factory
{

    @Override
    public void initialize()
    {
        // TODO: This is wrong
        // 6 floats per vertex. 8 vertices per sphere
        this.nodeVertexArray = new float[maximumNodes[0] * 6 * 8];

        this.nodeVAO = glGenVertexArrays();
        glBindVertexArray(nodeVAO);

        this.vertexVBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.vertexVBO);
        glBufferData(GL_ARRAY_BUFFER, (long) this.nodeVertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        this.indexVBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.indexVBO);
        glBufferData(GL_ARRAY_BUFFER, (long) this.nodeIndexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);
    }


    @Override
    public void prepareNodes(int size)
    {

    }
}
