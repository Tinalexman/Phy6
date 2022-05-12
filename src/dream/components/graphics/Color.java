package dream.components.graphics;


import org.joml.Vector4f;

public class Color
{
    private final Vector4f rgba;

    public Color()
    {
        this.rgba = new Vector4f(1.0f);
    }

    public Color(float red, float green, float blue, float alpha)
    {
        this.rgba = new Vector4f(red, green, blue, alpha);
    }

    public Color(Vector4f color)
    {
        this.rgba = new Vector4f(color);
    }

    public Color(Color color)
    {
        this.rgba = new Vector4f(color.rgba);
    }

    public Vector4f getRGBA()
    {
        return this.rgba;
    }

    public float getRed()
    {
        return this.rgba.x;
    }

    public float getGreen()
    {
        return this.rgba.y;
    }

    public float getBlue()
    {
        return this.rgba.z;
    }

    public float getAlpha()
    {
        return this.rgba.w;
    }

    public void setRed(float red)
    {
        if(this.rgba.x != red)
            this.rgba.x = red;
    }

    public void setGreen(float green)
    {
        if(this.rgba.y != green)
            this.rgba.y = green;
    }

    public void setBlue(float blue)
    {
        if(this.rgba.z != blue)
            this.rgba.z = blue;
    }

    public void setAlpha(float alpha)
    {
        if(this.rgba.w != alpha)
            this.rgba.w = alpha;
    }
}
