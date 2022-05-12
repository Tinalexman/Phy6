package dream.camera;

import dream.camera.Camera;
import dream.graphics.Graphics;
import dream.io.input.Input;
import dream.io.output.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class CameraRay
{
    private final Vector3f currentRay;
    private final Camera camera;

    public CameraRay(Camera camera)
    {
        this.camera = camera;
        this.currentRay = new Vector3f();
    }

    public Vector3f getCurrentRay()
    {
        return this.currentRay;
    }

    private Vector3f calculateMouseRay()
    {
        float[] coordinates = Input.getScreenCoordinates();
        Vector2f normalized = getNormalizedCoordinates(coordinates[0], coordinates[1]);
        Vector4f clippedCoordinates = new Vector4f(normalized.x, normalized.y, -1f, 1f);
        Vector4f eyeCoordinates = toEyeSpace(clippedCoordinates);
        return toWorldSpace(eyeCoordinates);
    }

    private Vector3f toWorldSpace(Vector4f eyeCoordinates)
    {
       Matrix4f invertedViewMatrix = camera.getInverseViewMatrix();
       Vector4f worldRay = new Vector4f();
       invertedViewMatrix.transform(eyeCoordinates, worldRay);
       return new Vector3f(worldRay.x, worldRay.y, worldRay.z).normalize();
    }

    private Vector4f toEyeSpace(Vector4f clippedCoordinates)
    {
        Matrix4f invertedProjectionMatrix = camera.getInverseProjectionMatrix();
        Vector4f eyeCoordinates = new Vector4f();
        invertedProjectionMatrix.transform(clippedCoordinates, eyeCoordinates);
        return new Vector4f(eyeCoordinates.x, eyeCoordinates.y, -1f, 0f);
    }

    private Vector2f getNormalizedCoordinates(float mouseX, float mouseY)
    {
        float x = (2f * mouseX) / Graphics.getWindowWidth() - 1;
        float y = (2f * mouseY) / Graphics.getWindowHeight() - 1;
        return new Vector2f(x, y);
    }

    public void update()
    {
        this.currentRay.set(calculateMouseRay());
    }
}
