package dream;

import dream.debug.Debug;
import dream.events.EventManager;
import dream.graphics.Graphics;
import dream.nodes.Node;
import dream.physics.Physics;

import java.io.File;
import java.util.List;


public class DreamEngine
{
    public static final String projectPath;
    public static final String resourcePath;
    public static final String homeDirectory;

    public static final double nanoSeconds = 0.000000001;
    public static final long inverseNanoseconds = 1000000000L;
    public static float frameRate = 1000F;
    public static final float frameTime = 1 / frameRate;
    public static int framesPerSecond;
    public static int frames = 0;

    public static volatile boolean isRunning = true;

    static
    {
        projectPath = System.getProperty("user.dir");
        resourcePath = projectPath + "\\res";
        homeDirectory = System.getProperty("user.home") + "\\Dream World";
    }

    private static void checkHomeDirectory()
    {
        File homeDirectory = new File(DreamEngine.homeDirectory);
        if(!homeDirectory.exists())
        {
            homeDirectory.mkdir();
            new File(DreamEngine.homeDirectory + "\\scenes").mkdir();
        }
    }

//    private static ExecutorService service;
//    private static int NUM_OF_THREADS;
//    public static ArrayBlockingQueue<Runnable> taskQueue;
//
//    static
//    {
//        NUM_OF_THREADS = Runtime.getRuntime().availableProcessors();
//        service = Executors.newFixedThreadPool(NUM_OF_THREADS);
//        taskQueue = new ArrayBlockingQueue<>(NUM_OF_THREADS);
//    }

    public static boolean initializeEngines()
    {
        checkHomeDirectory();
        Graphics.initialize();
        Physics.initialize();
        return true;
    }

    public static String getErrorMessage()
    {
        return "Error: ";
    }

    public static void startEngines()
    {
        Graphics.showSplashScreen();
        runEngineLoop();
        postEngineLoop();
        cleanUp();
    }

    private static void runEngineLoop()
    {
        isRunning = true;

        boolean render = false;
        long lastTime = System.nanoTime();
        long frameCounter = 0;
        double unprocessedTime = 0.0;

        while(isRunning)
        {
            long startTime = System.nanoTime();
            long delta = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += (delta * nanoSeconds);
            frameCounter += delta;

            handleInput();

            while(unprocessedTime > frameTime)
            {
                render = true;
                unprocessedTime -= frameTime;

                if(!Graphics.isRunning())
                    return;

                if(frameCounter >= inverseNanoseconds)
                {
                    DreamEngine.framesPerSecond = frames;
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render)
            {
                EventManager.notifyAllHandlers();
                updateState((float) (delta * nanoSeconds));
                frames++;
            }
        }
    }

    private static void postEngineLoop()
    {
        if(Graphics.onDestroyRequested())
        {
            Graphics.closeWindow();
            isRunning = false;
        }
    }

    private static void handleInput()
    {
        Graphics.handleInput();
    }

    private static void updateState(float delta)
    {
        Physics.simulate(delta);
        Graphics.update();
    }

    private static void cleanUp()
    {
        Graphics.cleanUp();
    }

    public static int getFramesPerSecond()
    {
        return framesPerSecond;
    }

    public static float getFrameRate()
    {
        return frameRate;
    }

    public static void setFrameRate(float frameRate)
    {
        DreamEngine.frameRate = frameRate;
    }

    public static void submitPhysicsList(List<Node> nodes)
    {
        Physics.setComponents(nodes);
    }

    public static void createNewScene()
    {
        Graphics.createNewScene();
    }

    public static List<String> formatNodes(List<Node> nodes)
    {
        return Graphics.formatNodes(nodes);
    }
}
