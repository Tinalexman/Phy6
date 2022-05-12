package dream.tools;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Maths
{
    public static float clamp(float value, float min, float max)
    {
        return Math.max(Math.min(value, max), min);
    }

    /**
     * Calculates the normal of the triangle made from the 3 vertices. The vertices must be specified in counter-clockwise order.
     * @param vertex0
     * @param vertex1
     * @param vertex2
     * @return
     */
    public static Vector3f calcNormal(Vector3f vertex0, Vector3f vertex1, Vector3f vertex2)
    {
        Vector3f tangentA = new Vector3f();
        vertex1.sub(vertex0, tangentA);

        Vector3f tangentB = new Vector3f();
        vertex2.sub(vertex0, tangentB);

        Vector3f normal = new Vector3f();
        tangentA.cross(tangentB, normal);

        normal.normalize();
        return normal;
    }

    public static void rotateVertexPoint(Vector3f point, Vector3f center, float rotation)
    {
        float radiansRotation = (float) Math.toRadians(rotation);
        float distanceFromCenter = Vector3f.distance(center.x, center.y, center.z, point.x, point.y, point.z);
        point.x = (float) (distanceFromCenter * Math.cos(radiansRotation));
        point.y = (float) (distanceFromCenter * Math.sin(radiansRotation));
    }

    public static void rotateVertexPoint(Vector2f point, Vector2f center, float rotation)
    {
        float radiansRotation = (float) Math.toRadians(rotation);
        float distanceFromCenter = Vector2f.distance(center.x, center.y, point.x, point.y);
        point.x = (float) (distanceFromCenter * Math.cos(radiansRotation));
        point.y = (float) (distanceFromCenter * Math.sin(radiansRotation));
    }

}
