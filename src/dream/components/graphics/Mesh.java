package dream.components.graphics;

import dream.nodes.NodeType;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class Mesh
{
    public transient float[] vertices;
    public transient float[] textures;
    public transient float[] normals;
    public transient int[] indices;

    private final transient int vaoID;

    private final transient List<Integer> listOfVbos;

    public Mesh(NodeType nodeType, float size)
    {
        this.vaoID = glGenVertexArrays();
        this.listOfVbos = new ArrayList<>();
        setMesh(nodeType, size);
    }

    public Mesh()
    {
        this.vaoID = glGenVertexArrays();
        this.listOfVbos = new ArrayList<>();
    }

    public void onStart()
    {
        if(this.vertices == null && this.textures == null && this.normals == null)
            setMesh(NodeType.Plane, 0.2f);
        load();
    }

    public void onDestroyRequested()
    {
        for(int vbo : listOfVbos)
            glDeleteBuffers(vbo);
        glDeleteVertexArrays(this.vaoID);
    }

    public void setMesh(NodeType nodeType, float size)
    {
        if(nodeType == NodeType.Cube)
            setCubeMesh(size);
        else if(nodeType == NodeType.Plane)
            setPlaneMesh(size);
        else if(nodeType == NodeType.Sphere)
            setSphereMesh(size);
    }

    private void setPlaneMesh(float size)
    {
        this.vertices = MeshFactory.getMeshVertices(NodeType.Plane, size);
        this.indices = MeshFactory.getMeshIndices(NodeType.Plane);
    }

    private void setCubeMesh(float size)
    {
        this.vertices = MeshFactory.getMeshVertices(NodeType.Cube, size);
        this.indices = MeshFactory.getMeshIndices(NodeType.Cube);
    }

    private void setSphereMesh(float size)
    {
        this.vertices = MeshFactory.getMeshVertices(NodeType.Sphere, size);
        this.indices = MeshFactory.getMeshIndices(NodeType.Sphere);
    }

    public void enable()
    {
        glBindVertexArray(this.vaoID);
        if(this.vertices != null)
            glEnableVertexAttribArray(0);
        if(this.textures != null)
            glEnableVertexAttribArray(1);
        if(this.normals != null)
            glEnableVertexAttribArray(2);
    }

    public int getVertexCount()
    {
        return (this.indices == null) ? (this.vertices.length / 3) : this.indices.length;
    }

    public void disable()
    {
        if(this.vertices != null)
            glDisableVertexAttribArray(0);
        if(this.textures != null)
            glDisableVertexAttribArray(1);
        if(this.normals != null)
            glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }


    public void load()
    {
        glBindVertexArray(this.vaoID);
        if(this.vertices != null)
            createVBO(0, 3, this.vertices);
        if(this.textures  != null)
            createVBO(1, 2, this.textures);
        if(this.normals != null)
            createVBO(2, 3, this.normals);
        if(this.indices != null)
            createEBO(this.indices);
        disable();
    }

    private void createEBO(int[] data)
    {
        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        IntBuffer buffer = createIntBuffer(data);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    private void createVBO(int location, int size, float[] data)
    {
        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = createFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(location, size, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(location);
        MemoryUtil.memFree(buffer);
        this.listOfVbos.add(vboID);
    }

    private static FloatBuffer createFloatBuffer(float[] coordinates)
    {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(coordinates.length);
        buffer.put(coordinates).flip();
        return buffer;
    }

    private static IntBuffer createIntBuffer(int[] coordinates)
    {
        IntBuffer buffer = MemoryUtil.memAllocInt(coordinates.length);
        buffer.put(coordinates).flip();
        return buffer;
    }

}
