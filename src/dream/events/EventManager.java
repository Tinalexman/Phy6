package dream.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager
{
    private static final Map<EventType, List<Handler>> eventHandlers = new HashMap<>();
    private static final List<Event> eventsSubmitted = new ArrayList<>();

    public static void registerHandler(EventType event, Handler handler)
    {
        List<Handler> eventHandlers = EventManager.eventHandlers.computeIfAbsent(event, k -> new ArrayList<>());
        eventHandlers.add(handler);
    }

    public static void unregisterHandler(EventType event, Handler handler)
    {
        List<Handler> eventHandlers = EventManager.eventHandlers.get(event);
        eventHandlers.remove(handler);
    }

    public static void pushEvent(Event event)
    {
        eventsSubmitted.add(event);
    }

    public static void notifyAllHandlers()
    {
        for(Event event : eventsSubmitted)
        {
            List<Handler> handlers = eventHandlers.get(event.type);
            if(handlers == null)
                continue;

            for(Handler handler : handlers)
            {
                if(handler != null)
                    handler.respond(event.type);
            }
        }
        eventsSubmitted.clear();
    }
}
