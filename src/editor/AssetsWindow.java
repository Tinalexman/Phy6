package editor;

import imgui.ImGui;
import imgui.flag.ImGuiStyleVar;

public class AssetsWindow
{

    public void drawImGui()
    {
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 1.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1.0f);
        ImGui.begin("Assets", Controls.defaultWindowFlags);



        ImGui.popStyleVar(3);
        ImGui.end();
    }

}
