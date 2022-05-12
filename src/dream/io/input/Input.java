package dream.io.input;

import dream.events.EventManager;
import dream.events.EventType;
import dream.events.Handler;
import dream.graphics.Graphics;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Input implements Handler
{
    private static Input instance;

    private final float[] scrolls;
    private float currentXPosition, currentYPosition;
    private float previousXPosition, previousYPosition;
    private boolean dragging;

    private final Vector2f viewportPosition;
    private final Vector2f viewportSize;
    private final Vector2f displayVector;

    private final boolean[] currentKeys;
    private final boolean[] previousKeys;
    private final boolean[] mouseButtons;

    private final float[] screenCoordinates;

    static
    {
        Input.instance = new Input();
    }

    public static void initialize()
    {
        EventManager.registerHandler(EventType.EndWindowFrame, Input.instance);
    }

    private Input()
    {
        this.currentKeys = new boolean[GLFW_KEY_LAST];
        this.previousKeys = new boolean[GLFW_KEY_LAST];

        this.scrolls = new float[2];
        this.previousXPosition = -1.0f;
        this.previousYPosition = -1.0f;
        this.currentXPosition = 0.0f;
        this.currentYPosition = 0.0f;

        this.mouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];

        this.viewportSize = new Vector2f();
        this.viewportPosition = new Vector2f();
        this.displayVector = new Vector2f();

        this.screenCoordinates = new float[2];
    }

    public static void keyCallback(long windowID, int key, int scancode, int action, int mods)
    {
        if(action == GLFW_PRESS)
            Input.instance.currentKeys[key] = true;
        else if(action == GLFW_RELEASE)
            Input.instance.currentKeys[key] = false;
    }

    public static boolean isKeyPressed(int keyCode)
    {
        return Input.instance.currentKeys[keyCode];
    }

    public static boolean isKeyTyped(int keyCode)
    {
        return Input.instance.currentKeys[keyCode] && !Input.instance.previousKeys[keyCode];
    }

    @Override
    public void respond(EventType eventType)
    {
        if(eventType == EventType.EndWindowFrame)
        {
            Input.instance.scrolls[0] = 0.0f;
            Input.instance.scrolls[1] = 0.0f;
            System.arraycopy(Input.instance.currentKeys, 0, Input.instance.previousKeys, 0, GLFW_KEY_LAST);
        }
    }

    public static void mousePositionCallback(long windowID, double xPosition, double yPosition)
    {
        Input.instance.currentXPosition = (float) xPosition;
        Input.instance.currentYPosition = (float) yPosition;

        Input.instance.dragging = Input.instance.mouseButtons[0] ||
                Input.instance.mouseButtons[1] || Input.instance.mouseButtons[2];
    }

    public static void mouseButtonCallback(long windowID, int button, int action, int mods)
    {
        if(action == GLFW_PRESS)
            Input.instance.mouseButtons[button] = true;
        else if(action == GLFW_RELEASE)
        {
            Input.instance.mouseButtons[button] = false;
            Input.instance.dragging = false;
        }
    }

    public static void mouseScrollCallback(long windowID, double xOffset, double yOffset)
    {
        Input.instance.scrolls[0] = (float) xOffset;
        Input.instance.scrolls[1] = (float) yOffset;
    }

    public static float getCurrentX()
    {
        return Input.instance.currentXPosition;
    }

    public static float getCurrentY()
    {
        return Input.instance.currentYPosition;
    }

    public static float getPreviousX()
    {
        return Input.instance.previousXPosition;
    }

    public static float getPreviousY()
    {
        return Input.instance.previousYPosition;
    }

    public static float getScrollX()
    {
        return Input.instance.scrolls[0];
    }

    public static float getScrollY()
    {
        return Input.instance.scrolls[1];
    }

    public static boolean isDragging()
    {
        return Input.instance.dragging;
    }

    public static boolean isButtonPressed(int button)
    {
        return Input.instance.mouseButtons[button];
    }

    public static float[] getScreenCoordinates()
    {
        if(Input.instance.previousXPosition != Input.instance.currentXPosition ||
                Input.instance.previousYPosition != Input.instance.currentYPosition)
            calculateScreenCoordinates();
        return Input.instance.screenCoordinates;
    }

    private static void calculateScreenCoordinates()
    {
        Input.instance.screenCoordinates[0] = Input.instance.currentXPosition - Input.instance.viewportPosition.x;
        Input.instance.screenCoordinates[0] = (Graphics.getWindowWidth() / Input.instance.viewportSize.x)
                * Input.instance.screenCoordinates[0];
        Input.instance.screenCoordinates[1] = Input.instance.currentYPosition - Input.instance.viewportPosition.y;
        Input.instance.screenCoordinates[1] = (Graphics.getWindowHeight() / Input.instance.viewportSize.y)
                * Input.instance.screenCoordinates[1];
    }

    public static void input()
    {
        Vector2f displayVector = Input.instance.displayVector;
        displayVector.set(0.0f, 0.0f);
        if(Input.instance.previousXPosition < 0 && Input.instance.previousYPosition < 0)
        {
            double x = Input.instance.currentXPosition - Input.instance.previousXPosition;
            double y = Input.instance.currentYPosition - Input.instance.previousYPosition;

            boolean rotateX = x != 0;
            boolean rotateY = y != 0;

            if(rotateX)
                displayVector.y = (float) x;

            if(rotateY)
                displayVector.x = (float) y;
        }

        Input.instance.previousXPosition = Input.instance.currentXPosition;
        Input.instance.previousYPosition = Input.instance.currentYPosition;
    }

    public static Vector2f getDisplayVector()
    {
        return Input.instance.displayVector;
    }

    public static void setGameViewportPos(Vector2f position)
    {
        Input.instance.viewportPosition.set(position);
    }

    public static void setGameViewportSize(Vector2f size)
    {
        Input.instance.viewportSize.set(size);
    }
}
