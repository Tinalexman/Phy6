package editor;

import dream.physics.Physics;
import imgui.ImGui;
import imgui.flag.ImGuiStyleVar;
import org.joml.Vector3f;

public class World
{
    public Vector3f gravitationalAcceleration;

    public World()
    {
        this.gravitationalAcceleration = new Vector3f(0.0f, -9.8f, 0.0f);
    }

    public void drawImGui()
    {
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 1.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1.0f);

        ImGui.begin("Physics World", Controls.defaultWindowFlags);

        if(ImGui.collapsingHeader("Physics Settings"))
        {
            int targetFPS = Physics.getTargetFramesPerSecond();
            int[] ranges = Physics.getFrameRanges();
            int res = Controls.drawIntControl("Target FPS:", targetFPS, false);
            if(res < ranges[0])
                Physics.setTargetFramesPerSecond(ranges[0]);
            else
                Physics.setTargetFramesPerSecond(Math.min(res, ranges[1]));
        }
        if(ImGui.collapsingHeader("Physics Variables"))
        {
            Controls.drawVector3Control("Gravity:", this.gravitationalAcceleration, false);
        }
        ImGui.popStyleVar(3);
        ImGui.end();
    }

}
