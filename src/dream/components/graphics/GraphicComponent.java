package dream.components.graphics;

import dream.assets.Material;
import dream.components.Component;
import dream.shader.Shader;
import editor.Controls;
import imgui.ImGui;

public class GraphicComponent extends Component
{
    public Mesh mesh;
    public Color color;
    public Material material;
    public Shader shader;

    public GraphicComponent()
    {
        this.name = "Graphics";
        this.color = new Color();
    }

    @Override
    public void onStart()
    {
        if(this.mesh != null)
            this.mesh.onStart();
    }

    @Override
    public void onDestroyRequested()
    {
        if(this.mesh != null)
            this.mesh.onDestroyRequested();

        if(this.shader != null)
            this.shader.destroy();
    }

    @Override
    public void drawImGui()
    {
        boolean res = Controls.drawColorPicker4("Color", this.color.getRGBA());
        if(res)
            this.changed = true;
        ImGui.separator();
    }

    @Override
    public void onChanged()
    {

    }

}
