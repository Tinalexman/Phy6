package dream.nodes.factory;

import dream.components.graphics.MeshFactory;
import dream.nodes.NodeType;

import java.util.Arrays;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class CubeFactory extends Factory
{
    public static final float[] defaultCubeSize = {0.01f};

    // 3 floats per vertex. 8 vertices per cube
    private static final int totalFloatsPerVertex = 3;
    private static final int totalVertexPerCube = 8;

    // 6 ints per face drawn, 6 faces per cube
    private static final int totalIntsPerFace = 6;
    private static final int totalFacePerCube = 6;

    public CubeFactory()
    {
        super();
    }

    @Override
    public void initialize()
    {
        this.nodeVertexArray = new float[maximumNodes[0] * CubeFactory.totalFloatsPerVertex * CubeFactory.totalVertexPerCube];
        this.nodeIndexArray = new int[maximumNodes[0] * CubeFactory.totalFacePerCube * CubeFactory.totalIntsPerFace];

        this.nodeVAO = glGenVertexArrays();
        glBindVertexArray(this.nodeVAO);

        this.vertexVBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.vertexVBO);
        glBufferData(GL_ARRAY_BUFFER, (long) this.nodeVertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        this.indexVBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indexVBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, (long) this.nodeIndexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);
    }

    @Override
    public void prepareNodes(int size)
    {
        if(size <= 0 || size >= this.maximumNodes[0])
            return;

        int vertexPointer = 0, indexPointer = 0;
        for(int counter = 0; counter < size; ++counter)
        {
            float[] vertices = MeshFactory.getMeshVertices(NodeType.Cube, CubeFactory.defaultCubeSize[0]);
            int[] indices = MeshFactory.getMeshIndices(NodeType.Cube);

            System.arraycopy(vertices, 0, this.nodeVertexArray, vertexPointer, vertices.length);
            System.arraycopy(indices, 0, this.nodeIndexArray, indexPointer, indices.length);

            vertexPointer += CubeFactory.totalVertexPerCube;
            indexPointer += CubeFactory.totalFacePerCube * CubeFactory.totalIntsPerFace;
        }

        enable();

        glBindBuffer(GL_ARRAY_BUFFER, this.vertexVBO);
        glBufferSubData(GL_ARRAY_BUFFER,  0, Arrays.copyOfRange(this.nodeVertexArray, 0,
                size * CubeFactory.totalFloatsPerVertex * CubeFactory.totalVertexPerCube));

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indexVBO);
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER,  0, Arrays.copyOfRange(this.nodeVertexArray, 0,
                size * CubeFactory.totalFacePerCube * CubeFactory.totalIntsPerFace));

        disable();
    }



}
