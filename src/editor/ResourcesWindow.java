package editor;

import dream.DreamEngine;
import dream.assets.Texture;
import dream.graphics.Graphics;
import dream.tools.AssetPool;
import dream.tools.SystemIcon;
import dream.tools.Theme;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiStyleVar;

import java.io.File;

public class ResourcesWindow
{
    protected static final File scenePath = new File(DreamEngine.homeDirectory + "\\scenes\\");

    public ResourcesWindow()
    {

    }

    public void drawImGui()
    {
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 1.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1.0f);
        ImGui.begin("Resources", Controls.defaultWindowFlags);

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowPos);
        ImVec2 itemsSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemsSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        boolean useDarkTexture = Graphics.getTheme() == Theme.Light;
        Texture newFile = AssetPool.getTexture(SystemIcon.newFile, useDarkTexture);
        Texture document = AssetPool.getTexture(SystemIcon.document, useDarkTexture);

        if(ImGui.imageButton(newFile.getTextureID(), newFile.getWidth(), newFile.getHeight(), 0, 1, 1, 0))
        {
            System.out.println("New File Button Clicked");
        }

//        int numberOfItems = scenePath.list().length;
//        for(int i = 0; i < numberOfItems; i++)
//        {
//            if(ImGui.imageButton(document.getTextureID(), document.getWidth(), document.getHeight(), 0, 1, 1, 0))
//            {
//                System.out.println("Document Button Clicked");
//            }
//        }

        ImGui.popStyleVar(3);
        ImGui.end();
    }
}
