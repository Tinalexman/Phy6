package editor;

import dream.camera.Camera;
import dream.graphics.Graphics;
import dream.tools.Theme;
import imgui.ImGui;
import imgui.flag.ImGuiStyleVar;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Settings
{
    protected float[] cameraNearPlane;
    protected float[] cameraFarPlane;
    protected float[] cameraZoom;
    protected float[] cameraSpeed;
    protected Vector3f cameraPosition;
    protected Vector3f cameraRotation;
    protected boolean[] cameraChange;

    protected Vector4f selectionColor;

    protected boolean[] useTargetRatio;

    protected Vector4f viewportClearColor;

    public Settings()
    {

    }

    public void drawImGui()
    {
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 1.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1.0f);

        ImGui.begin("Editor Settings", Controls.defaultWindowFlags);

        if(ImGui.collapsingHeader("Theme"))
        {
            Theme currentTheme = Graphics.getTheme();
            if(ImGui.radioButton("Light Theme", (currentTheme == Theme.Light)))
                Graphics.changeTheme(Theme.Light);
            ImGui.sameLine();
            if(ImGui.radioButton("Dark Theme", (currentTheme == Theme.Dark)))
                Graphics.changeTheme(Theme.Dark);
        }

        if(ImGui.collapsingHeader("Camera Settings"))
        {
            Controls.setSensitivity(0.01f);
            boolean posChange = Controls.drawVector3Control("Position:", this.cameraPosition, true);
            boolean rotChange = Controls.drawVector3Control("Rotation:", this.cameraRotation, true);

            Controls.setSensitivity(0.001f);
            float cameraNearPlane = this.cameraNearPlane[0];
            float change = Controls.drawFloatControl("Near Plane:", cameraNearPlane, true);
            boolean nearChange = change != cameraNearPlane;
            if (change != cameraNearPlane)
                this.cameraNearPlane[0] = Math.max(change, Camera.minimumCameraNearPlane);

            Controls.setSensitivity(1.0f);
            float cameraFarPlane = this.cameraFarPlane[0];
            change = Controls.drawFloatControl("Far Plane:", cameraFarPlane, true);
            boolean farChange = change != cameraFarPlane;
            if (change != cameraFarPlane)
                this.cameraFarPlane[0] = Math.min(change, Camera.maximumCameraFarPlane);

            Controls.setSensitivity(0.01f);
            float zoom = this.cameraZoom[0];
            change = Controls.drawFloatControl("Zoom:", zoom, true);
            boolean zoomChange = zoom != change;
            if (change < Editor.minimumCameraZoom)
                this.cameraZoom[0] = Editor.minimumCameraZoom;
            else
                this.cameraZoom[0] = Math.min(change, Editor.maximumCameraZoom);

            Controls.setSensitivity(0.1f);
            Controls.drawFloatControl("Camera Speed", this.cameraSpeed[0], true);

            this.cameraChange[0] = posChange || rotChange || nearChange || farChange || zoomChange;
            Controls.resetSensitivity();
        }

        if(ImGui.collapsingHeader("Selection"))
        {
            Controls.drawColorPicker4("Selection Color:", this.selectionColor);
        }

        if(ImGui.collapsingHeader("Viewport Settings"))
        {
            boolean change = Controls.drawBooleanControl("Use Ratio", this.useTargetRatio[0]);
            ImGui.sameLine();
            Controls.drawHelpMarker("This swaps the aspect ratio of the viewport from " +
                    "the available monitor's aspect ratio to the ratio of the viewport window. This change " +
                    "is reflected when the viewport window is resized");
            this.useTargetRatio[0] = change;

            Controls.drawColorPicker4("Viewport Color", this.viewportClearColor);
        }

        ImGui.popStyleVar(3);
        ImGui.end();
    }
}
