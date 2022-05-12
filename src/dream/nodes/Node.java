package dream.nodes;

import dream.components.Component;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public class Node
{
    protected static int numberOfNodes = 0;

    public String name;
    public NodeType nodeType;
    public int ID;
    public List<Component> components;

    public Node(String name, int ID)
    {
        this.ID = ID;
        this.name = name;
        this.components = new ArrayList<>();
    }

    public void drawImGui()
    {
        for(Component component : components)
        {
            if(ImGui.collapsingHeader(component.name))
                component.drawImGui();
        }
    }

    public <T extends Component> T getComponent(Class<T> componentClass)
    {
        if(this.components.isEmpty())
                return null;

        for(Component component : components)
        {
            if(componentClass.isAssignableFrom(component.getClass()))
            {
                try
                {
                    return componentClass.cast(component);
                }
                catch (ClassCastException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public void start()
    {
        for(Component component : this.components)
            component.onStart();
    }

    public void addComponent(Component component)
    {
        component.parentID = this.ID;
        this.components.add(component);
    }

    public void addComponentIfAbsent(Component component)
    {
        if(!components.isEmpty())
        {
            for(Component comp : components)
            {
                if(component.getClass().isAssignableFrom(comp.getClass()))
                    return;
            }
        }
        addComponent(component);
    }

    public <T extends Component> boolean removeComponent(Class<T> componentClass)
    {
        if(components.isEmpty())
            return false;

        for(Component component : this.components)
        {
            if(componentClass.isAssignableFrom(component.getClass()))
            {
                try
                {
                    component.onDestroyRequested();
                    components.remove(component);
                    return true;
                }
                catch (ClassCastException ignored)
                {

                }
            }
        }
        return false;
    }

    public void removeAllComponents()
    {
        this.components.clear();
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Node node))
            return false;
        return this.name.equals(node.name) && this.ID == node.ID;
    }

}
