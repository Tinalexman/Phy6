package dream.components.general;

import dream.components.Component;
import editor.Controls;
import imgui.ImGui;

public class GeneralComponent extends Component
{
    public Transform transform;
    public String componentName;

    public GeneralComponent()
    {
        this.name = "General";
        this.transform = new Transform();
    }

    @Override
    public void onStart()
    {

    }

    @Override
    public void onDestroyRequested()
    {

    }

    @Override
    public void drawImGui()
    {
        ImGui.text("Transform:");
        Controls.setSensitivity(0.01f);
        boolean posChange = Controls.drawVector3Control( "Position:", transform.position, true);
        boolean rotChange = Controls.drawVector3Control( "Rotation:", transform.rotation, true);
        boolean scChange = Controls.drawVector3Control( "Scale:", transform.scale, true, 1.0f);
        int indChange = Controls.drawIntControl("Z Index:", transform.zIndex, false);

        if(posChange || rotChange || scChange || (transform.zIndex != indChange))
        {
            transform.zIndex = Math.max(indChange, 0);
            transform.recalculateMatrix();
            this.changed = true;
        }

        ImGui.separator();
        Controls.resetSensitivity();
    }

    @Override
    public void onChanged()
    {

    }

}
