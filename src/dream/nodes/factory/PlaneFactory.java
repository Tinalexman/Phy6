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

public class PlaneFactory extends Factory
{
    public static final float[] defaultPlaneSize = {0.5f};

    // 3 floats per vertex. 4 vertices per plane
    private static final int totalFloatsPerVertex = 3;
    private static final int totalVertexPerPlane = 4;

    // 6 ints per face drawn, 1 face per plane
    private static final int totalIntsPerFace = 6;
    private static final int totalFacePerPlane = 1;


    public PlaneFactory()
    {
        super();
    }

    @Override
    public void initialize()
    {
        this.nodeVertexArray = new float[maximumNodes[0] * PlaneFactory.totalVertexPerPlane * PlaneFactory.totalFloatsPerVertex];
        this.nodeIndexArray = new int[maximumNodes[0] * PlaneFactory.totalIntsPerFace * PlaneFactory.totalFacePerPlane];

        this.nodeVAO = glGenVertexArrays();
        glBindVertexArray(nodeVAO);

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
            float[] vertices = MeshFactory.getMeshVertices(NodeType.Plane, PlaneFactory.defaultPlaneSize[0]);
            int[] indices = MeshFactory.getMeshIndices(NodeType.Plane);

            System.arraycopy(vertices, 0, this.nodeVertexArray, vertexPointer, vertices.length);
            System.arraycopy(indices, 0, this.nodeIndexArray, indexPointer, indices.length);

            vertexPointer += PlaneFactory.totalVertexPerPlane;
            indexPointer += PlaneFactory.totalIntsPerFace * PlaneFactory.totalFacePerPlane;
        }

        enable();

        glBindBuffer(GL_ARRAY_BUFFER, this.vertexVBO);
        glBufferSubData(GL_ARRAY_BUFFER,  0, Arrays.copyOfRange(this.nodeVertexArray, 0,
                size * PlaneFactory.totalVertexPerPlane * PlaneFactory.totalFloatsPerVertex));

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indexVBO);
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER,  0, Arrays.copyOfRange(this.nodeVertexArray, 0,
                size * PlaneFactory.totalFacePerPlane * PlaneFactory.totalIntsPerFace));

        disable();
    }

}
