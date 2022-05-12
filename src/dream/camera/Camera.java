package dream.camera;

import dream.graphics.Graphics;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera
{
    public static float minimumCameraNearPlane = 0.000001f;
    public static float maximumCameraFarPlane = 1000000.0f;

    public Vector3f cameraRotation; // pitch = about x  yaw = about y roll = about z
    public Vector3f cameraPosition;

    public float[] nearPlane;
    public float[] farPlane;
    public float[] cameraZoom;
    public float[] aspectRatio;

    protected boolean[] hasChanged;
    protected boolean active;

    protected Matrix4f inverseViewMatrix;
    protected Matrix4f inverseProjectionMatrix;
    protected Matrix4f viewMatrix;
    protected Matrix4f projectionMatrix;

    protected CameraRay cameraRay;

    private static Camera currentCamera;

    public Camera()
    {
        this.cameraPosition = new Vector3f(0.0f, 0.0f, 1.0f);
        this.cameraRotation = new Vector3f(0.0f, 0.0f, 0.0f);

        this.hasChanged = new boolean[] {true};
        this.active = false;

        this.inverseViewMatrix = new Matrix4f().identity();
        this.inverseProjectionMatrix = new Matrix4f().identity();

        this.cameraRay = new CameraRay(this);
        this.cameraRay.update();
    }

    public static Camera getCurrentCamera()
    {
        return Camera.currentCamera;
    }

    public void requestActivation()
    {
        Camera.currentCamera = this;
    }

    public void setAspectRatio(float[] aspectRatio)
    {
        this.aspectRatio = aspectRatio;
        this.hasChanged[0] = true;
        this.cameraRay.update();
    }

    public Matrix4f getViewMatrix()
    {
        if(hasChanged[0])
        {
            this.viewMatrix = new Matrix4f().identity();
            this.viewMatrix
                    .rotate((float) Math.toRadians(this.cameraRotation.x), new Vector3f(1.0f, 0.0f, 0.0f))
                    .rotate((float) Math.toRadians(this.cameraRotation.y), new Vector3f(0.0f, 1.0f, 0.0f))
                    .rotate((float) Math.toRadians(this.cameraRotation.z), new Vector3f(0.0f, 0.0f, 1.0f))
                    .translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
            this.viewMatrix.invert(this.inverseViewMatrix);
            this.cameraRay.update();
        }
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix()
    {
        if(hasChanged[0])
        {
            this.projectionMatrix = new Matrix4f().identity();
            this.projectionMatrix.perspective(this.cameraZoom[0], this.aspectRatio[0],
                    this.nearPlane[0], this.farPlane[0]);
            this.projectionMatrix.invert(this.inverseProjectionMatrix);
            this.cameraRay.update();
        }
        return this.projectionMatrix;
    }

    public Matrix4f getInverseViewMatrix()
    {
        return this.inverseViewMatrix;
    }

    public Matrix4f getInverseProjectionMatrix()
    {
        return this.inverseProjectionMatrix;
    }

    public float[] getNearPlane()
    {
        return this.nearPlane;
    }

    public float[] getFarPlane()
    {
        return this.farPlane;
    }

    public void setNearPlane(float nearPlane)
    {
        this.nearPlane[0] = nearPlane;
        this.hasChanged[0] = true;
        this.cameraRay.update();
    }

    public void setFarPlane(float farPlane)
    {
        this.farPlane[0] = farPlane;
        this.hasChanged[0] = true;
        this.cameraRay.update();
    }

    public Vector3f getPosition()
    {
        return this.cameraPosition;
    }

    public void setPosition(Vector3f position)
    {
        if(!this.cameraPosition.equals(position))
        {
            this.cameraPosition.set(position);
            this.hasChanged[0] = true;
            this.cameraRay.update();
        }
    }

    public void moveCamera(Vector3f position)
    {
        if(this.cameraPosition.equals(position))
            return;

        if(position.z != 0)
        {
            this.cameraPosition.x += (float) Math.sin(Math.toRadians(this.cameraRotation.y)) * -1.0f * position.z;
            this.cameraPosition.z += (float) Math.cos(Math.toRadians(this.cameraRotation.y)) * position.z;
        }

        if(position.x != 0)
        {
            this.cameraPosition.x += (float) Math.sin(Math.toRadians(this.cameraRotation.y - 90.0f)) * -1.0f * position.x;
            this.cameraPosition.z += (float) Math.cos(Math.toRadians(this.cameraRotation.y - 90.0f)) * position.x;
        }

        this.cameraPosition.y += position.y;
        this.hasChanged[0] = true;
        this.cameraRay.update();
    }

    public void setRotation(Vector3f rotation)
    {
        if(!this.cameraRotation.equals(rotation))
        {
            this.cameraRotation.set(rotation);
            this.hasChanged[0] = true;
            this.cameraRay.update();
        }
    }

    public void moveRotation(Vector3f rotation)
    {
        if(!this.cameraRotation.equals(rotation))
        {
            this.cameraPosition.x += rotation.x;
            this.cameraPosition.y += rotation.y;
            this.cameraPosition.z += rotation.z;

            this.hasChanged[0] = true;
            this.cameraRay.update();
        }
    }

    public void moveRotation(float x, float y, float z)
    {
        moveRotation(new Vector3f(x, y, z));
    }

    public Vector3f getCameraRotation()
    {
        return this.cameraRotation;
    }

    public void incrementCameraZoom(float zoom)
    {
        this.cameraZoom[0] += zoom;
        this.hasChanged[0] = true;
        this.cameraRay.update();
    }

    public float[] getCameraZoom()
    {
        return this.cameraZoom;
    }

    public void setCameraZoom(float zoom)
    {
        this.cameraZoom[0] = zoom;
        this.hasChanged[0] = true;
        this.cameraRay.update();
    }

    public boolean[] hasChanged()
    {
        return hasChanged;
    }

    public void hasChanged(boolean change)
    {
        this.hasChanged[0] = change;
    }

    public void reset()
    {
        this.hasChanged[0] = false;
    }

    public Vector3f getCameraRay()
    {
        return this.cameraRay.getCurrentRay();
    }

}
