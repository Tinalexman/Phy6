package dream.debug;

import dream.camera.Camera;
import dream.shader.Shader;
import dream.shader.ShaderConstants;
import dream.tools.Maths;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class Debug
{
    protected static final int[] maximumLines = {500};
    public static List<Line> lines = new ArrayList<>();
    protected static float lineWidth = 1.5f;
    private static final int floatsPerVertex = 6;
    private static final int vertexPerLine = 2;
    private static final int vertexInfo = floatsPerVertex * vertexPerLine;

    protected static float[] vertexArray;

    protected static Shader debugShader;
    protected static Camera camera;

    protected static int vaoID, vboID;

    public static void initialize(Camera camera)
    {
        // 6 floats per vertex (position and color). 2 vertices per line
        Debug.vertexArray = new float[maximumLines[0] * Debug.vertexInfo];

        try
        {
            Debug.debugShader = new Shader("debugShader.glsl");
            Debug.debugShader.createShaderObject();
            Debug.debugShader.storeUniforms(ShaderConstants.viewMatrix, ShaderConstants.projectionMatrix);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        Debug.vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        Debug.vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) Debug.vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(Debug.lineWidth);

        Debug.camera = camera;
    }

    public static void beginFrame()
    {
        for(int pos = 0; pos < Debug.lines.size(); pos++)
        {
            if(Debug.lines.get(pos).beginFrame() < 0)
            {
                Debug.lines.remove(pos);
                pos--;
            }
        }
    }

    public static int[] getMaximumLines()
    {
        return Debug.maximumLines;
    }

    public static void setMaximumLines(int numOfLines)
    {
        Debug.maximumLines[0] = numOfLines;
    }

    public static boolean hasLines()
    {
        return Debug.lines.size() > 0;
    }

    public static void drawLines()
    {
        if(Debug.lines.size() == 0)
            return;

        int index = 0;
        for(Line line : Debug.lines)
        {
            for(int i = 0; i < 2; i++)
            {
                Vector3f position = (i == 0) ? line.start : line.end;
                Vector3f color = line.color;

                Debug.vertexArray[index] = position.x;
                Debug.vertexArray[index + 1] = position.y;
                Debug.vertexArray[index + 2] = position.z;

                Debug.vertexArray[index + 3] = color.x;
                Debug.vertexArray[index + 4] = color.y;
                Debug.vertexArray[index + 5] = color.z;
                index += Debug.floatsPerVertex;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, Debug.vboID);
        glBufferSubData(GL_ARRAY_BUFFER,  0, Arrays.copyOfRange(Debug.vertexArray,
                0, Debug.lines.size() * Debug.vertexInfo));

        Debug.debugShader.startProgram();
        Debug.debugShader.updateVPMatrices(camera.getViewMatrix(), camera.getProjectionMatrix());

        glBindVertexArray(Debug.vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawArrays(GL_LINES, 0, lines.size());

        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);

        Debug.debugShader.stopProgram();
    }

    public static void addLine(Vector3f start, Vector3f end, Vector3f color, int lifetime)
    {
        if(Debug.lines.size() >= maximumLines[0])
            return;
        Debug.lines.add(new Line(start, end, color, lifetime));
    }

    public static void addLine(Vector3f start, Vector3f end, Vector3f color)
    {
        addLine(start, end, color, Line.defaultLineLifetime);
    }

    public static void addLine(Vector3f start, Vector3f end)
    {
        addLine(start, end, Line.defaultColor, Line.defaultLineLifetime);
    }

    public static void addLine(Vector2f start, Vector2f end, Vector3f color, int zIndex, int lifetime)
    {
        if(Debug.lines.size() >= maximumLines[0])
            return;
        Debug.lines.add(new Line(new Vector3f(start.x, start.y, zIndex), new Vector3f(end.x, end.y, zIndex), color, lifetime));
    }

    public static void addBox(Vector2f center, Vector2f dimensions, Vector3f color, int lifetime, float rotation, int zIndex)
    {
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

        Vector2f[] vertices =
        {
                new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
                new Vector2f(max.x, min.y), new Vector2f(max.x, min.y)
        };

        if(rotation != 0.0f)
        {
            for(Vector2f vertex : vertices)
                Maths.rotateVertexPoint(vertex, center, rotation);
        }

        addLine(vertices[0], vertices[1], color, zIndex, lifetime);
        addLine(vertices[0], vertices[3], color, zIndex, lifetime);
        addLine(vertices[1], vertices[2], color, zIndex, lifetime);
        addLine(vertices[2], vertices[3], color, zIndex, lifetime);
    }

    public static void addBox(Vector2f center, Vector2f dimensions, Vector3f color, int lifetime, int zIndex)
    {
        addBox(center, dimensions, color, lifetime, 0.0f, zIndex);
    }

    public static void addBox(Vector2f center, Vector2f dimensions, Vector3f color, int zIndex)
    {
        addBox(center, dimensions, color, Line.defaultLineLifetime, 0.0f, zIndex);
    }

    public static void addBox(Vector2f center, Vector2f dimensions, int zIndex)
    {
        addBox(center, dimensions, Line.defaultColor, Line.defaultLineLifetime, 0.0f, zIndex);
    }


    public static void addCircle(Vector2f center, Vector3f color, float radius, int subDivision, int zIndex, int lifetime)
    {
        if(subDivision < Line.defaultCircleSegments)
            return;

        Vector2f[] points = new Vector2f[subDivision];
        int increment = 360 / subDivision;
        int currentAngle = 0;

        for(int i = 0; i < subDivision; i++)
        {
            Vector2f temp = new Vector2f(radius, 0);
            Maths.rotateVertexPoint(temp, new Vector2f(), currentAngle);
            points[i] = new Vector2f(temp).add(center);

            if(i > 0)
                addLine(points[i - 1], points[i], color, zIndex, lifetime);

            currentAngle += increment;
        }

        addLine(points[subDivision - 1], points[0], color, zIndex, lifetime);
    }

    public static void addCircle(Vector2f center, Vector3f color, float radius, int lifetime, int zIndex)
    {
        addCircle(center, color, radius, Line.defaultCircleSegments, zIndex, lifetime);
    }

    public static void addCircle(Vector2f center, float radius, int zIndex)
    {
        addCircle(center, Line.defaultColor, radius, Line.defaultCircleSegments, zIndex, Line.defaultLineLifetime);
    }


    //TODO: Write add cube method too
    public static void addCube(Vector3f origin, Vector3f dimension, int lifetime)
    {

    }




}
