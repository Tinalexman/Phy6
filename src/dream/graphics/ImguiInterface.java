package dream.graphics;

import dream.DreamEngine;
import dream.graphics.Graphics;
import dream.io.input.Input;
import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class ImguiInterface
{
    private final ImGuiImplGl3 imGuiGl3;
    private final ImGuiImplGlfw imGuiGlfw;

    public ImguiInterface()
    {
        this.imGuiGl3 = new ImGuiImplGl3();
        this.imGuiGlfw = new ImGuiImplGlfw();
    }

    public void swapToLight()
    {
        ImGui.styleColorsLight();
    }

    public void swapToDark()
    {
        ImGui.styleColorsDark();
    }

    public void initImGui(long windowID)
    {
        ImGui.createContext();
        swapToLight();

        final ImGuiIO io = ImGui.getIO();
        io.setIniFilename("imgui.ini");
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        //io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.setBackendPlatformName("imgui_java_impl_glfw"); // For clarity reasons

        // ------------------------------------------------------------
        // Here goes GLFW callbacks to update user input in Dear ImGui

        glfwSetKeyCallback(windowID, (w, key, scancode, action, mods) ->
        {
            if (action == GLFW_PRESS)
                io.setKeysDown(key, true);
            else if (action == GLFW_RELEASE)
                io.setKeysDown(key, false);

            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));

            Input.keyCallback(w, key, scancode, action, mods);
        });

        glfwSetCharCallback(windowID, (w, c) ->
        {
            if (c != GLFW_KEY_DELETE)
                io.addInputCharacter(c);
        });

        glfwSetMouseButtonCallback(windowID, (w, button, action, mods) ->
        {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1])
                ImGui.setWindowFocus(null);

            Input.mouseButtonCallback(w, button, action, mods);
        });

        glfwSetScrollCallback(windowID, (w, xOffset, yOffset) ->
        {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);

            Input.mouseScrollCallback(w, xOffset, yOffset);
        });

        io.setSetClipboardTextFn(new ImStrConsumer()
        {
            @Override
            public void accept(final String s)
            {
                glfwSetClipboardString(windowID, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier()
        {
            @Override
            public String get()
            {
                String clipboardString = glfwGetClipboardString(windowID);
                return (clipboardString != null) ? clipboardString : "";
            }
        });

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());
        fontConfig.setRasterizerMultiply(1.5f);

        fontAtlas.addFontFromFileTTF("res/fonts/segoeuil.ttf", 16, fontConfig);
        fontConfig.destroy();

        imGuiGlfw.init(windowID, false);
        imGuiGl3.init("#version 330 core");
    }

    public void cleanUp()
    {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    public void drawDockSpace()
    {
//        ImGuiViewport mainViewport = ImGui.getMainViewport();
//        ImGui.setNextWindowPos(mainViewport.getWorkPosX(), mainViewport.getWorkPosY());
//        ImGui.setNextWindowSize(mainViewport.getWorkSizeX(), mainViewport.getWorkSizeY());
//        ImGui.setNextWindowViewport(mainViewport.getID());

        ImGui.setNextWindowPos(0.0f, 0.0f);
        ImGui.setNextWindowSize(Graphics.getWindowWidth(), Graphics.getWindowHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 1.0f);

        int windowFlags = ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoTitleBar
                | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove
                | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("DockSpace Window", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);
        ImGui.dockSpace(ImGui.getID("DockSpace"));

        if(ImGui.beginMenuBar())
        {
            if(ImGui.beginMenu("Home"))
            {
                ImGui.menuItem("New Scene", "Ctrl + N");
                ImGui.menuItem("Open Scene", "Ctrl + O");
                ImGui.separator();
                ImGui.menuItem("Save Scene", "Ctrl + S");
                ImGui.menuItem("Save Scene As", "Ctrl + Shift + S");
                ImGui.separator();
                ImGui.menuItem("Quit");
            }
        }
        ImGui.endMenuBar();

        ImGui.end();
    }

    public void startRenderFrame()
    {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    public void endRenderFrames()
    {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

//        long backupWindowPtr = glfwGetCurrentContext();
//        ImGui.updatePlatformWindows();
//        ImGui.renderPlatformWindowsDefault();
//        glfwMakeContextCurrent(backupWindowPtr);
    }


}
