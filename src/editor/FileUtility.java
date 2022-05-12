package editor;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;

public class FileUtility
{

    public String showNewSceneDialog()
    {
        String sceneName = "";
        //ImGui.openPopup("New Scene");

        ImVec2 center = ImGui.getMainViewport().getCenter();
        ImGui.setNextWindowPos(center.x, center.y, ImGuiCond.Appearing, 0.5f, 0.5f);

        if(ImGui.beginPopupModal("New Scene", new ImBoolean(true), ImGuiWindowFlags.AlwaysAutoResize))
        {
            ImGui.text("Add a new scene");
            ImGui.separator();
            ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 0.0f, 0.0f);
            ImString name = new ImString();
            ImGui.inputText("##sceneName", name);
            ImGui.popStyleVar();

            if(ImGui.button("Create", 120.0f, 0.0f))
                sceneName = name.get();
            ImGui.setItemDefaultFocus();
            ImGui.sameLine();
            if(ImGui.button("Cancel", 120.0f, 0.0f))
                ImGui.closeCurrentPopup();

            ImGui.endPopup();
        }
        return sceneName;
    }

}
