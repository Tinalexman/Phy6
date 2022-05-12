import dream.DreamEngine;

public final class Launcher
{
    public static void main(String[] args)
    {
        boolean engineInitialized = DreamEngine.initializeEngines();
        if(engineInitialized)
            DreamEngine.startEngines();
        else
            System.err.println(DreamEngine.getErrorMessage());
    }
}
