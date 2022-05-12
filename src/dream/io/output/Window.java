package dream.io.output;

import dream.events.Event;
import dream.events.EventManager;
import dream.events.EventType;
import dream.io.input.Input;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    private final String windowTitle;
    private final int[] maxWindowSize;
    private final int[] windowSize;
    private final float[] aspectRatio;
    private long windowID;
    private boolean closeRequested;

    public Window(String windowTitle, int windowWidth, int windowHeight)
    {
        this.maxWindowSize = new int[2];
        this.windowSize = new int[2];
        this.aspectRatio = new float[1];


        this.windowTitle = windowTitle;
        this.windowSize[0] = windowWidth;
        this.windowSize[1] = windowHeight;

        this.closeRequested = false;
    }

    public float[] getAspectRatio()
    {
        return this.aspectRatio;
    }

    public void initialize()
    {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW!");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        this.windowID = glfwCreateWindow(this.windowSize[0], this.windowSize[1], this.windowTitle, NULL, NULL);
        if (this.windowID == NULL)
            throw new RuntimeException("Failed to create the GLFW Window!");

        glfwSetCursorPosCallback(this.windowID, Input::mousePositionCallback);
        glfwSetMouseButtonCallback(this.windowID, Input::mouseButtonCallback);
        glfwSetScrollCallback(this.windowID, Input::mouseScrollCallback);
        glfwSetKeyCallback(this.windowID, Input::keyCallback);
        glfwSetWindowCloseCallback(this.windowID, window -> glfwSetWindowShouldClose(window, true));

        try (MemoryStack stack = stackPush())
        {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(this.windowID, pWidth, pHeight);

            final GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            this.maxWindowSize[0] = vidMode.width();
            int windowBorderHeight = 23;
            this.maxWindowSize[1] = vidMode.height() - windowBorderHeight;

            int calculatedWidth = (vidMode.width() - pWidth.get(0)) / 2, calculatedHeight = (vidMode.height() - pHeight.get(0)) / 2;
            glfwSetWindowPos(this.windowID, calculatedWidth, calculatedHeight);

            this.aspectRatio[0] = (float) this.windowSize[0] / this.windowSize[1];

            glfwSetWindowSizeCallback(this.windowID, (window, width, height) ->
            {
                this.windowSize[0] = width;
                this.windowSize[1] = height;
                float ratio = (float) width / height;
                this.aspectRatio[0] = ratio;
                glViewport(0, 0, width, height);
            });
            glfwSetWindowCloseCallback(this.windowID, window -> this.closeRequested = true);
            glfwMakeContextCurrent(this.windowID);
        }
        GL.createCapabilities();
        glfwShowWindow(this.windowID);
    }

    public void startFrame()
    {
        glClearColor(0.5f,  0.f, 0.5f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        EventManager.pushEvent(new Event(EventType.StartWindowFrame));
    }

    public boolean isCloseRequested()
    {
        return this.closeRequested;
    }

    public void endFrame()
    {
        glfwSwapBuffers(this.windowID);
        glfwPollEvents();
        EventManager.pushEvent(new Event(EventType.EndWindowFrame));
    }

    public long getWindowId()
    {
        return this.windowID;
    }

    public void cleanUp()
    {
        glfwFreeCallbacks(this.windowID);
        glfwDestroyWindow(this.windowID);
    }

    public int getWindowWidth()
    {
        return this.windowSize[0];
    }

    public int getWindowHeight()
    {
        return this.windowSize[1];
    }

    public int[] getMaxWindowSize()
    {
        return this.maxWindowSize;
    }

    public void closeWindow()
    {
        glfwSetWindowShouldClose(this.windowID, true);
    }

    public int[] getWindowSize()
    {
        return this.windowSize;
    }
}
