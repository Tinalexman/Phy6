package dream.light;

import dream.components.graphics.Color;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class DirectionalLight
{
    private Color color;
    private float intensity;
    private Vector3f direction;

    public DirectionalLight(Vector4f color, Vector3f direction, float intensity)
    {
        this.color = new Color(color);
        this.direction = direction;
        this.intensity = intensity;
    }

    public Vector3f getDirection()
    {
        return this.direction;
    }

    public void setDirection(Vector3f direction)
    {
        this.direction = direction;
    }

    public Color getColor()
    {
        return this.color;
    }

    public float getIntensity()
    {
        return this.intensity;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public void setIntensity(float intensity)
    {
        this.intensity = intensity;
    }
}
