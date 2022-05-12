package dream.components.graphics;

import dream.nodes.NodeType;

public class MeshFactory
{

    public static float[] getMeshVertices(NodeType nodeType, float size)
    {
        if(nodeType == NodeType.Plane)
            return getPlaneMeshVertices(size);
        else if(nodeType == NodeType.Cube)
            return getCubeMeshVertices(size);
        else if(nodeType == NodeType.Sphere)
            return getSphereMeshVertices(size);
        return new float[] {0.0f};
    }

    public static int[] getMeshIndices(NodeType nodeType)
    {
        if(nodeType == NodeType.Plane)
            return getPlaneMeshIndices();
        else if(nodeType == NodeType.Cube)
            return getCubeMeshIndices();
        else if(nodeType == NodeType.Sphere)
            return getSphereMeshIndices();
        return new int[] {0};
    }

    private static float[] getPlaneMeshVertices(float size)
    {
//        return new float[]
//        {
//           size, 0.0f, size,
//           -size, 0.0f,  size,
//           size, 0.0f, -size,
//
//           -size, 0.0f, size,
//           size, 0.0f, -size,
//           -size, 0.0f, -size
//        };
        return new float[]
        {
           size, 0.0f, size,
           size, 0.0f, -size,
           -size, 0.0f, -size,
           -size, 0.0f, size
        };
    }

    private static int[] getPlaneMeshIndices()
    {
        return new int[]
        {
          0, 3, 1,
          3, 1, 2
        };
    }

    private static float[] getCubeMeshVertices(float size)
    {
        return new float[]
        {
            -size, size, size,
            -size, -size, size,
            size, -size, size,
            size, size, size,

            -size, size, -size,
            size, size, -size,
            -size, -size, -size,
            size, -size, -size
        };
    }

    private static int[] getCubeMeshIndices()
    {
        return new int[]
        {
            0, 1, 3, 3, 1, 2,
            4, 0, 3, 5, 4, 3,
            3, 2, 7, 5, 3, 7,
            6, 1, 0, 6, 0, 4,
            2, 1, 6, 2, 6, 7,
            7, 6, 4, 7, 4, 5
        };
    }


     private static float[] getSphereMeshVertices(float size)
     {
         return new float[]
         {
             -size, size, size,
             -size, -size, size,
             size, -size, size,
             size, size, size,

             -size, size, -size,
             size, size, -size,
             -size, -size, -size,
             size, -size, -size
         };
     }

    private static int[] getSphereMeshIndices()
    {
        return new int[]
        {
            0, 1, 3, 3, 1, 2,
            4, 0, 3, 5, 4, 3,
            3, 2, 7, 5, 3, 7,
            6, 1, 0, 6, 0, 4,
            2, 1, 6, 2, 6, 7,
            7, 6, 4, 7, 4, 5
        };
    }

    private void setIcoSphereMesh(float size)
    {
        final float horizontalAngle = (float) (Math.PI * 0.4f); // 72 degrees
        final float verticalAngle = (float) Math.atan(0.5); // elevation = 26.565 degrees

        float[] vertices = new float[36];  // array of 12 vertices
        int i1, i2;
        float z, xy;
        float hAngle1 = (float) ((-Math.PI * 0.5) - (horizontalAngle * 0.5));
        float hAngle2 = (float) (-Math.PI * 0.5);

        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = size;

        for(int i = 1; i <= 5; ++i)
        {
            i1 = i * 3;
            i2 = (i + 5) * 3;

            z = (float) (size * Math.sin(verticalAngle));
            xy = (float) (size * Math.cos(verticalAngle));

            vertices[i1] = (float) (xy * Math.cos(hAngle1));
            vertices[i2] = (float) (xy * Math.cos(hAngle2));
            vertices[i1 + 1] = (float) (xy * Math.sin(hAngle1));
            vertices[i2 + 1] = (float) (xy * Math.sin(hAngle2));
            vertices[i1 + 2] = z;
            vertices[i2 + 2] = -z;

            hAngle1 += horizontalAngle;
            hAngle2 += horizontalAngle;
        }

        i1 = 11 * 3;
        vertices[i1] = 0;
        vertices[i1 + 1] = 0;
        vertices[i1 + 2] = -size;
    }

}
