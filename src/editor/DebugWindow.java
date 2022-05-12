package editor;

import imgui.ImGui;
import imgui.flag.ImGuiStyleVar;

public class DebugWindow
{
    protected int[] maximumLines;

    public void drawImGui()
    {
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 1.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1.0f);
        ImGui.begin("Debug", Controls.defaultWindowFlags);

        Controls.drawIntControl("Max Lines", maximumLines[0], false);

        ImGui.popStyleVar(3);
        ImGui.end();
    }
}
