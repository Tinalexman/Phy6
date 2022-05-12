package editor;

import dream.assets.Texture;
import dream.events.Event;
import dream.events.EventManager;
import dream.events.EventType;
import dream.graphics.Graphics;
import dream.io.input.Input;
import dream.renderer.Renderer;
import dream.tools.AssetPool;
import dream.tools.SystemIcon;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

public class Viewport
{
    private float previousWindowWidth;
    private float previousWindowHeight;
    private float previousWindowX;
    private float previousWindowY;

    protected String sceneName;
    protected boolean[] savedScene;

    private boolean isPlaying;
    protected boolean[] useTargetRatio;

    public Viewport()
    {
        this.isPlaying = false;
        this.sceneName = "";
    }

    public void renderViewPort(int framebufferTextureID)
    {
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 1.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1.0f);

        int windowFlags = ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse
                | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoMove;
        ImGui.begin("##viewport", windowFlags);

        if(this.savedScene != null)
            renderMenubar();

        ImVec2 framebufferSizeInWindow = getLargestSizeForViewPort();
        ImVec2 framebufferPositionInWindow = getCenteredPositionForViewPort(framebufferSizeInWindow);

        ImVec2 actualWindowPosition = ImGui.getWindowPos();
        ImVec2 actualWindowSize = ImGui.getWindowSize();

        ImGui.setCursorPos(framebufferPositionInWindow.x, framebufferPositionInWindow.y);

        if(framebufferTextureID != Renderer.nosScene)
            ImGui.image(framebufferTextureID, framebufferSizeInWindow.x, framebufferSizeInWindow.y, 0, 1, 1, 0);

        if(hasChanged(actualWindowSize, actualWindowPosition))
        {
            float leftX = actualWindowPosition.x + framebufferPositionInWindow.x;
            float topY = actualWindowPosition.y + framebufferPositionInWindow.y;

            Input.setGameViewportPos(new Vector2f(leftX, topY));
            Input.setGameViewportSize(new Vector2f(framebufferSizeInWindow.x, framebufferSizeInWindow.y));

            this.previousWindowWidth = actualWindowSize.x;
            this.previousWindowHeight = actualWindowSize.y;

            this.previousWindowX = actualWindowPosition.x;
            this.previousWindowY = actualWindowPosition.y;
        }

        ImGui.popStyleVar(3);
        ImGui.end();
    }

    private void renderMenubar()
    {
        ImVec2 vec2 = new ImVec2();
        ImGui.getWindowSize(vec2);

        ImGui.beginMenuBar();
        float sceneWidth = vec2.x;

        ImGui.text(this.sceneName + (this.savedScene[0] ? "" : "*"));
        ImGui.sameLine(sceneWidth);

        Texture playBtn = AssetPool.getTexture(SystemIcon.play, true);
        Texture stopBtn = AssetPool.getTexture(SystemIcon.stop, true);

        float scaledSize = playBtn.getWidth() * 0.18f;

        if(ImGui.imageButton(playBtn.getTextureID(), scaledSize, scaledSize, 0, 1, 1, 0))
        {
            if(!this.isPlaying)
                EventManager.pushEvent(new Event(EventType.StartViewportRuntime));
            this.isPlaying = true;
        }

        if(ImGui.imageButton(stopBtn.getTextureID(), scaledSize, scaledSize, 0, 1, 1, 0))
        {
            if(this.isPlaying)
                EventManager.pushEvent(new Event(EventType.EndViewportRuntime));
            this.isPlaying = false;
        }

        ImGui.endMenuBar();
    }

    private ImVec2 getCenteredPositionForViewPort(ImVec2 aspectSize)
    {
        ImVec2 windowSize = setup();
        float viewportX = (windowSize.x * 0.5f) - (aspectSize.x * 0.5f);
        float viewportY = (windowSize.y * 0.5f) - (aspectSize.y * 0.5f);
        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

    private ImVec2 getLargestSizeForViewPort()
    {
        if(this.useTargetRatio[0])
        {
            ImVec2 windowSize = setup();
            float aspectWidth = windowSize.x;
            float aspectHeight = aspectWidth / Graphics.getAspectRatio()[0];
            if(aspectHeight > windowSize.y)
            {
                aspectHeight = windowSize.y;
                aspectWidth = aspectHeight * Graphics.getAspectRatio()[0];
            }
            return new ImVec2(aspectWidth, aspectHeight);
        }
        else
            return setup();
    }

    private ImVec2 setup()
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();
        return windowSize;
    }

    public boolean getWantCaptureMouse()
    {
        float[] screenCoordinates = Input.getScreenCoordinates();
        return screenCoordinates[0] >= 0.0f && screenCoordinates[0] <= Graphics.getWindowWidth()
                && screenCoordinates[1] >= 0.0f && screenCoordinates[1] <= Graphics.getWindowHeight();
    }

    private boolean hasChanged(ImVec2 newWindowSize, ImVec2 newWindowPos)
    {
        boolean equalSize = (this.previousWindowWidth == newWindowSize.x) && (this.previousWindowHeight == newWindowSize.y);
        boolean samePosition = (this.previousWindowX == newWindowPos.x) && (this.previousWindowY == newWindowPos.y);
        return !(equalSize && samePosition);
    }

}