package editor;

import dream.nodes.Node;
import dream.scene.EditorScene;
import imgui.ImGui;
import imgui.flag.ImGuiStyleVar;


public class SceneGraphWindow
{
    private EditorScene scene;
    private InspectorWindow inspectorWindow;

    public SceneGraphWindow()
    {

    }

    public void setScene(EditorScene scene)
    {
        this.scene = scene;
    }

    public void setInspectorWindow(InspectorWindow window)
    {
        this.inspectorWindow = window;
    }

    public void drawImGui()
    {
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 1.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1.0f);
        ImGui.begin("Scene Graph", Controls.defaultWindowFlags);

        if(ImGui.beginPopupContextWindow("Node Adder"))
        {
            if(ImGui.beginMenu("Add New Node"))
            {
                if(ImGui.menuItem("Cube"))
                {
//                    if(this.scene != null)
//                        this.scene.addSceneObject(Factory.createCube());
                }

                ImGui.endMenu();
            }

            ImGui.endPopup();
        }

        if(this.scene != null)
        {
            int index = 0;
            for(Node node : this.scene.getSceneObjects(true))
            {
                ImGui.pushID(index);
                ImGui.bullet();
                ImGui.sameLine();
                if(ImGui.selectable(node.name))
                    this.inspectorWindow.setSelectedNode(node);

                ImGui.popID();
                ++index;
            }
        }
        ImGui.popStyleVar(3);
        ImGui.end();
    }


}
