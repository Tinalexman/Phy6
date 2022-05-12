package dream.graphics;

import dream.DreamEngine;
import dream.debug.Debug;
import dream.io.input.Input;
import dream.io.output.Window;
import dream.nodes.Node;
import dream.nodes.factory.Factory;
import dream.scene.EditorScene;
import dream.scene.SceneManager;
import dream.tools.AssetPool;
import editor.Editor;
import dream.tools.Theme;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class Graphics
{
    private static final Graphics engine;

    private Window window;
    private Editor editor;
    private ImguiInterface imguiInterface;
    private Theme theme;
    private Factory cubeFactory, planeFactory;

    static
    {
        engine = new Graphics();
    }

    public static boolean initialize()
    {
        Graphics.engine.theme = Theme.Light;

        // Responsible for loading scenes and saving them too
        SceneManager.initialize();

        Graphics.engine.window = new Window("Dream Engine", 1280, 768);
        Graphics.engine.window.initialize();

        Graphics.engine.imguiInterface = new ImguiInterface();
        Graphics.engine.imguiInterface.initImGui(engine.window.getWindowId());

        Graphics.engine.editor = new Editor();
        Graphics.engine.editor.initializeEditor();

        Input.initialize();

//        Graphics.engine.cubeFactory = new CubeFactory();
//        Graphics.engine.cubeFactory.initialize();
//
//        Graphics.engine.planeFactory = new PlaneFactory();
//        Graphics.engine.planeFactory.initialize();
        Debug.initialize(Graphics.engine.editor.getCamera());

        String[] files = new File(DreamEngine.resourcePath + "\\textures\\system").list();
        if(files != null)
        {
            for(String file : files)
                AssetPool.addTexture("\\system\\" + file);
        }
        else
            System.err.println("The required editor textures are absent!");

        return true;
    }

    private Graphics()
    {

    }

    public static boolean onDestroyRequested()
    {
        // this is the part that checks if everything is alright. Like saving the current scene and all that. To be properly implemented
        return true;
    }

    public static void processNodes(List<Node> nodes)
    {
        Graphics.engine.planeFactory.prepareNodes(nodes.size());
    }

    public static void enableFactory()
    {
        Graphics.engine.planeFactory.enable();
    }

    public static void disableFactory()
    {
        Graphics.engine.planeFactory.disable();
    }

    public static void showSplashScreen()
    {

    }

    public static float[] getAspectRatio()
    {
        return engine.window.getAspectRatio();
    }

    public static int getWindowWidth()
    {
        return engine.window.getWindowWidth();
    }

    public static int getWindowHeight()
    {
        return engine.window.getWindowWidth();
    }

    public static int[] getMaxWindowSize()
    {
        return engine.window.getMaxWindowSize();
    }

    public static int[] getWindowSize()
    {
        return engine.window.getWindowSize();
    }

    public static boolean isRunning()
    {
        return !engine.window.isCloseRequested();
    }

    public static void update()
    {
        Graphics.engine.window.startFrame();

        Graphics.engine.imguiInterface.startRenderFrame();
        Graphics.engine.imguiInterface.drawDockSpace();

        Graphics.engine.editor.drawEditor();

        Graphics.engine.imguiInterface.endRenderFrames();

        Graphics.engine.window.endFrame();
    }

    public static void changeTheme(Theme theme)
    {
        if (theme == Theme.Dark)
            Graphics.engine.imguiInterface.swapToDark();
        else if (theme == Theme.Light)
            Graphics.engine.imguiInterface.swapToLight();
        Graphics.engine.theme = theme;
    }

    public static Theme getTheme()
    {
        return Graphics.engine.theme;
    }

    public static void handleInput()
    {
        engine.editor.checkInputs();
    }

    public static void cleanUp()
    {
        Graphics.engine.editor.cleanUp();
        Graphics.engine.imguiInterface.cleanUp();
        Graphics.engine.window.cleanUp();

        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
        glfwTerminate();
    }

    public static void createNewScene()
    {
        Graphics.engine.editor.editScene(new EditorScene());
    }

    public static List<String> formatNodes(List<Node> nodes)
    {
        return Graphics.engine.editor.formatNodes(nodes);
    }

    public static void closeWindow()
    {
        Graphics.engine.window.closeWindow();
    }
}
