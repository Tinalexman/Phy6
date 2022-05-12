package dream.debug;

import org.joml.Vector3f;

public class Line
{
    protected static final int defaultLineLifetime = 1;
    protected static final int defaultCircleSegments = 8;
    protected static final int indefiniteLifetime = -10;
    protected static final Vector3f defaultColor = new Vector3f(1.0f, 0.0f, 0.0f);

    protected Vector3f start;
    protected Vector3f color;
    protected Vector3f end;
    protected int lifetime;

    public Line(Vector3f start, Vector3f end, Vector3f color, int lifetime)
    {
        this.start = start;
        this.end = end;
        this.color = color;
        this.lifetime = lifetime;
    }

    public Vector3f getColor()
    {
        return this.color;
    }

    public void setColor(Vector3f color)
    {
        this.color = color;
    }

    public Vector3f getStart()
    {
        return this.start;
    }

    public void setStart(Vector3f start)
    {
        this.start = start;
    }

    public Vector3f getEnd()
    {
        return this.end;
    }

    public void setEnd(Vector3f end)
    {
        this.end = end;
    }

    public int beginFrame()
    {
        this.lifetime--;
        return this.lifetime;
    }
}
