package dream.components.general;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform
{

    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;
    public int zIndex;
    //public Quaternionf rotation;

    private final Matrix4f transformationMatrix;

    public Transform()
    {
        this.position = new Vector3f(0.0f);
        this.rotation = new Vector3f(0.0f);
        this.scale = new Vector3f(1.0f);
        this.zIndex = 0;
        this.transformationMatrix = new Matrix4f().identity();
    }

    public Transform(Transform transform)
    {
        this.position = new Vector3f(transform.position);
        this.rotation = new Vector3f(transform.rotation);
        this.scale = new Vector3f(transform.scale);
        this.zIndex = transform.zIndex;
        this.transformationMatrix = new Matrix4f(transform.transformationMatrix);
    }

    public Vector3f getPosition()
    {
        return this.position;
    }

    public void incrementPosition(float xPosition, float yPosition, float zPosition)
    {
        this.position.x += xPosition;
        this.position.y += yPosition;
        this.position.z += zPosition;
        recalculateMatrix();
    }

    public void setPosition(Vector3f position)
    {
        if(!this.position.equals(position))
        {
            this.position = position;
            recalculateMatrix();
        }
    }

    public int getZIndex()
    {
        return this.zIndex;
    }

    public void setZIndex(int zIndex)
    {
        if(this.zIndex != zIndex)
        {
            this.zIndex = zIndex;
            recalculateMatrix();
        }
    }

    public Vector3f getRotation()
    {
        return this.rotation;
    }

    public void incrementRotation(float xRotation, float yRotation, float zRotation)
    {
        this.rotation.x += xRotation;
        this.rotation.y += yRotation;
        this.rotation.z += zRotation;
        recalculateMatrix();
    }

    public void setRotation(Vector3f rotation)
    {
        if(!this.rotation.equals(rotation))
        {
            this.rotation = rotation;
            recalculateMatrix();
        }
    }

    public Vector3f getScale()
    {
        return this.scale;
    }

    public void incrementScale(float xScale, float yScale, float zScale)
    {
        this.scale.x *= xScale;
        this.scale.y *= yScale;
        this.scale.z *= zScale;
        recalculateMatrix();
    }

    public void setScale(Vector3f scale)
    {
        if(!this.scale.equals(scale))
        {
            this.scale = scale;
            recalculateMatrix();
        }
    }

    public void recalculateMatrix()
    {
        this.transformationMatrix.identity()
                .scale(this.scale)
                .rotate((float) Math.toRadians(this.rotation.x), new Vector3f(1.0f, 0.0f, 0.0f))
                .rotate((float) Math.toRadians(this.rotation.y), new Vector3f(0.0f, 1.0f, 0.0f))
                .rotate((float) Math.toRadians(this.rotation.z), new Vector3f(0.0f, 0.0f, 1.0f))
                .translate(this.position);
    }

    public Matrix4f getTransformationMatrix()
    {
        return this.transformationMatrix;
    }

    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof Transform transform))
            return false;
        return this.position.equals(transform.position) &&
                this.rotation.equals(transform.rotation) &&
                this.scale.equals(transform.scale) &&
                this.zIndex == transform.zIndex;
    }

    public Transform copy()
    {
        return new Transform(this);
    }

    public void apply(Transform transform)
    {
        this.scale.add(transform.scale);
        this.rotation.add(transform.rotation);
        this.position.add(transform.position);
        recalculateMatrix();
    }

}
