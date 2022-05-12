package editor;

import dream.nodes.Node;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiStyleVar;

import java.util.ArrayList;
import java.util.List;


public class InspectorWindow
{
    private static final float defaultColumnWidth = 80.0f;
    private Node selectedNode;
    private final List<Node> selectedNodes;

    public InspectorWindow()
    {
        this.selectedNodes = new ArrayList<>();
    }

    public void setSelectedNode(Node node)
    {
        this.selectedNode = node;
    }

    public void drawImGui()
    {
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 1.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1.0f);
        ImGui.begin("Inspector", Controls.defaultWindowFlags);

        ImVec2 tempVector = new ImVec2(), windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        if(this.selectedNode == null)
        {
            ImGui.calcTextSize(tempVector, "No Selected Node");
            float textPos = (windowSize.x - tempVector.x) * 0.5f;
            ImGui.sameLine(textPos);
            ImGui.setCursorPos(textPos, windowSize.y * 0.25f);
            ImGui.text("No Selected Node");
        }
        else
        {
            ImGui.calcTextSize(tempVector, selectedNode.name);
            float textPos = (windowSize.x - tempVector.x) * 0.5f;
            ImGui.sameLine(textPos);
            ImGui.text(this.selectedNode.name);
            ImGui.separator();

            this.selectedNode.drawImGui();
        }
        ImGui.popStyleVar(3);
        ImGui.end();
    }
}
