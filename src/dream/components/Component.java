package dream.components;

public abstract class Component
{
    protected static final int noParent = -1;

    protected boolean changed;
    public int parentID;
    public String name;

    public Component()
    {
        this.parentID = noParent;
        this.name = "Component";
        this.changed = true;
    }

    public abstract void onStart();
    public abstract void onDestroyRequested();
    public abstract void drawImGui();
    public abstract void onChanged();

    public boolean isChanged()
    {
        return this.changed;
    }
    public void setChanged()
    {
        this.changed = false;
    }
}


