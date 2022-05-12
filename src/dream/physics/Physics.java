package dream.physics;

import dream.components.physics.PhysicsComponent;
import dream.events.Event;
import dream.events.EventManager;
import dream.events.EventType;
import dream.events.Handler;
import dream.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public class Physics implements Handler
{
    private static int targetFramesPerSecond;
    private static float frameRate;
    private static float physicsTime;

    private static Physics engine;
    private static boolean shouldSimulate = false;

    private final List<PhysicsComponent> components;

    public Physics()
    {
        this.components = new ArrayList<>();
        EventManager.registerHandler(EventType.StartViewportRuntime, this);
        EventManager.registerHandler(EventType.EndViewportRuntime, this);
    }

    public static void setComponents(List<Node> components)
    {
        Physics.engine.components.clear();
        for (Node node : components)
        {
            PhysicsComponent component = node.getComponent(PhysicsComponent.class);
            if (component != null)
                Physics.engine.components.add(component);
        }
    }

    public static void simulate(float delta)
    {
        if(Physics.shouldSimulate)
        {
            Physics.physicsTime += delta;
            if(Physics.physicsTime >= 0.0f)
            {
                Physics.physicsTime -= Physics.frameRate;
                EventManager.pushEvent(new Event(EventType.StartPhysicsSimulation));
                for(PhysicsComponent component : engine.components)
                    component.integrate(Physics.physicsTime);
                EventManager.pushEvent(new Event(EventType.EndPhysicsSimulation));
            }
        }
    }

    public static void setTargetFramesPerSecond(int targetFPS)
    {
        Physics.targetFramesPerSecond = targetFPS;
        Physics.frameRate = 1.0f / targetFPS;
    }

    public static void initialize()
    {
        Physics.engine = new Physics();
        Physics.targetFramesPerSecond = 60;
        Physics.frameRate = 1.0f / 60;
        Physics.physicsTime = 0.0f;
    }

    public static int[] getFrameRanges()
    {
        return new int[] {1, 120};
    }

    public static int getTargetFramesPerSecond()
    {
        return targetFramesPerSecond;
    }

    @Override
    public void respond(EventType eventType)
    {
        if(eventType == EventType.StartViewportRuntime)
            shouldSimulate = true;
        else if(eventType == EventType.EndViewportRuntime)
            shouldSimulate = false;
    }
}
