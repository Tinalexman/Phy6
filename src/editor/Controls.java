package editor;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Controls
{
    private static final float defaultColumnWidth = 80.0f;
    private static float sensitivity = 0.1f;
    public static final int defaultWindowFlags = ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize;

    public static void setSensitivity(float sensitivity)
    {
        Controls.sensitivity = sensitivity;
    }

    public static void resetSensitivity()
    {
        Controls.sensitivity = 0.1f;
    }

    public static boolean drawVector2Control(String label, Vector2f values, boolean drag, float resetValue, float columnWidth)
    {
        boolean res = false;
        float[] vecValuesX = new float[1];
        float[] vecValuesY = new float[1];

        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) * 0.5f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.15f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.15f, 0.15f, 1.0f);
        if(ImGui.button("X", buttonSize.x, buttonSize.y))
        {
            values.x = resetValue;
            res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();

        if(drag)
        {
            vecValuesX[0] = values.x;
            ImGui.dragFloat("##x", vecValuesX, Controls.sensitivity);
        }
        else
        {
            ImFloat imFloat = new ImFloat(values.x);
            ImGui.inputFloat("##inputFloat", imFloat);
        }
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.15f, 0.8f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.9f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.15f, 0.8f, 0.15f, 1.0f);
        if(ImGui.button("Y", buttonSize.x, buttonSize.y))
        {
            values.y = resetValue;
            res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();

        if(drag)
        {
            vecValuesY[0] = values.y;
            ImGui.dragFloat("##y", vecValuesY, Controls.sensitivity);
        }
        else
        {
            ImFloat imFloat = new ImFloat(values.y);
            ImGui.inputFloat("##inputFloat", imFloat);
        }
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.popItemWidth();
        ImGui.sameLine();
        ImGui.nextColumn();


        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();


        if(values.x != vecValuesX[0] || values.y != vecValuesY[0])
        {
            values.x = vecValuesX[0];
            values.y = vecValuesY[0];
            res = true;
        }

        return res;
    }

    public static boolean drawVector2Control(String label, Vector2f values, boolean drag, float resetValue)
    {
        return drawVector2Control(label, values, drag, resetValue, defaultColumnWidth);
    }

    public static boolean drawVector2Control(String label, Vector2f values, boolean drag)
    {
        return drawVector2Control(label, values, drag, 0.0f, defaultColumnWidth);
    }

    public static boolean drawVector3Control(String label, Vector3f values, boolean drag, float resetValue, float columnWidth)
    {
        boolean res = false;

        float[] vecValuesX = new float[1];
        float[] vecValuesY = new float[1];
        float[] vecValuesZ = new float[1];


        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);

        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) * 0.5f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.15f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.15f, 0.15f, 1.0f);
        if(ImGui.button("X", buttonSize.x, buttonSize.y))
        {
            values.x = resetValue;
            res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();

        if(drag)
        {
            vecValuesX[0] = values.x;
            ImGui.dragFloat("##x", vecValuesX, Controls.sensitivity);
        }
        else
        {
            ImFloat imFloat = new ImFloat(values.x);
            ImGui.inputFloat("##inputFloat", imFloat);
        }

        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.15f, 0.8f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.9f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.15f, 0.8f, 0.15f, 1.0f);
        if(ImGui.button("Y", buttonSize.x, buttonSize.y))
        {
            values.y = resetValue;
            res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();

        if(drag)
        {
            vecValuesY[0] = values.y;
            ImGui.dragFloat("##y", vecValuesY, Controls.sensitivity);
        }
        else
        {
            ImFloat imFloat = new ImFloat(values.y);
            ImGui.inputFloat("##inputFloat", imFloat);
        }

        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.15f, 0.15f, 0.8f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.2f, 0.9f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.15f, 0.15f, 0.8f, 1.0f);
        if(ImGui.button("Z", buttonSize.x, buttonSize.y))
        {
            values.z = resetValue;
            res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();

        if(drag)
        {
            vecValuesZ[0] = values.z;
            ImGui.dragFloat("##z", vecValuesZ, Controls.sensitivity);
        }
        else
        {
            ImFloat imFloat = new ImFloat(values.z);
            ImGui.inputFloat("##inputFloat", imFloat);
        }

        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();

        if(values.x != vecValuesX[0] || values.y != vecValuesY[0] || values.z != vecValuesZ[0])
        {
            values.x = vecValuesX[0];
            values.y = vecValuesY[0];
            values.z = vecValuesZ[0];
            res = true;
        }

        return res;
    }

    public static boolean drawVector3Control(String label, Vector3f values, boolean drag, float resetValue)
    {
        return drawVector3Control(label, values, drag, resetValue, defaultColumnWidth);
    }

    public static boolean drawVector3Control(String label, Vector3f values, boolean drag)
    {
       return drawVector3Control(label, values, drag, 0.0f, defaultColumnWidth);
    }

    public static boolean drawVector4Control(String label, Vector4f values, boolean drag, float resetValue, float columnWidth)
    {
        boolean res = false;

        float[] vecValuesX = new float[1];
        float[] vecValuesY = new float[1];
        float[] vecValuesZ = new float[1];
        float[] vecValuesW = new float[1];

        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);

        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) * 0.5f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.15f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.15f, 0.15f, 1.0f);
        if(ImGui.button("X", buttonSize.x, buttonSize.y))
        {
            values.x = resetValue;
            res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();

        if(drag)
        {
            vecValuesX[0] = values.x;
            ImGui.dragFloat("##x", vecValuesX, Controls.sensitivity);
        }
        else
        {
            ImFloat imFloat = new ImFloat(values.x);
            ImGui.inputFloat("##inputFloat", imFloat);
        }

        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.15f, 0.8f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.9f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.15f, 0.8f, 0.15f, 1.0f);
        if(ImGui.button("Y", buttonSize.x, buttonSize.y))
        {
            values.y = resetValue;
            res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();

        if(drag)
        {
            vecValuesY[0] = values.y;
            ImGui.dragFloat("##y", vecValuesY, Controls.sensitivity);
        }
        else
        {
            ImFloat imFloat = new ImFloat(values.y);
            ImGui.inputFloat("##inputFloat", imFloat);
        }

        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.15f, 0.15f, 0.8f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.2f, 0.9f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.15f, 0.15f, 0.8f, 1.0f);
        if(ImGui.button("Z", buttonSize.x, buttonSize.y))
        {
            values.z = resetValue;
            res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();

        if(drag)
        {
            vecValuesZ[0] = values.z;
            ImGui.dragFloat("##z", vecValuesZ, Controls.sensitivity);
        }
        else
        {
            ImFloat imFloat = new ImFloat(values.z);
            ImGui.inputFloat("##inputFloat", imFloat);
        }

        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.15f, 0.15f, 0.8f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.2f, 0.9f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.15f, 0.15f, 0.8f, 1.0f);
        if(ImGui.button("W", buttonSize.x, buttonSize.y))
        {
            values.w = resetValue;
            res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();

        if(drag)
        {
            vecValuesW[0] = values.w;
            ImGui.dragFloat("##w", vecValuesW, Controls.sensitivity);
        }
        else
        {
            ImFloat imFloat = new ImFloat(values.w);
            ImGui.inputFloat("##inputFloat", imFloat);
        }

        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();

        if(values.x != vecValuesX[0] || values.y != vecValuesY[0] || values.z != vecValuesZ[0] || values.w != vecValuesW[0])
        {
            values.x = vecValuesX[0];
            values.y = vecValuesY[0];
            values.z = vecValuesZ[0];
            values.w = vecValuesW[0];
            res = true;
        }

        return res;
    }

    public static boolean drawVector4Control(String label, Vector4f values, boolean drag, float resetValue)
    {
        return drawVector4Control(label, values, drag, resetValue, defaultColumnWidth);
    }

    public static boolean drawVector4Control(String label, Vector4f values, boolean drag)
    {
        return drawVector4Control(label, values, drag, 0.0f, defaultColumnWidth);
    }

    public static int drawIntControl(String label, int value, boolean drag)
    {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        int result;

        if(drag)
        {
            int[] valueArr = new int[] {value};
            ImGui.dragInt("##dragInt", valueArr, 1);
            result = valueArr[0];
        }
        else
        {
            ImInt imInt = new ImInt(value);
            ImGui.inputInt("##inputInt", imInt);
            result = imInt.get();
        }

        ImGui.columns(1);
        ImGui.popID();

        return result;
    }

    public static int drawRangedIntControl(String label, int value, int min, int max)
    {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        int[] valueArr = new int[] {value};
        ImGui.dragIntRange2("##dragInt", valueArr, new int[] {min, max}, 1);
        ImGui.columns(1);
        ImGui.popID();

        return valueArr[0];
    }


    public static float drawFloatControl(String label, float value, boolean drag)
    {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float result;

        if(drag)
        {
            float[] valueArr = new float[] {value};
            ImGui.dragFloat("##dragFloat", valueArr, Controls.sensitivity);
            result = valueArr[0];
        }
        else
        {
            ImFloat imFloat = new ImFloat(value);
            ImGui.inputFloat("##inputFloat", imFloat);
            result = imFloat.get();
        }

        ImGui.columns(1);
        ImGui.popID();

        return result;
    }

    public static float drawRangedFloatControl(String label, float value, boolean drag, float min, float max)
    {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valueArr = new float[] {value};
        ImGui.dragFloatRange2("##dragFloat", valueArr, new float[] {min, max}, Controls.sensitivity);

        ImGui.columns(1);
        ImGui.popID();

        return valueArr[0];
    }

    public static boolean drawBooleanControl(String label, boolean value)
    {
        ImGui.pushID(label);
        boolean res = value;
        if(ImGui.checkbox(label, value))
            res = !value;
        ImGui.popID();
        return res;
    }

    public static boolean drawColorPicker4(String label, Vector4f color)
    {
        boolean res = false;
        // Push ID if not working
        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorEdit4(label, imColor))
        {
            color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            res = true;
        }
        return res;
    }

    public static void drawHelpMarker(String description)
    {
        ImGui.textDisabled("(?)");
        if (ImGui.isItemHovered())
        {
            ImGui.beginTooltip();
            ImGui.pushTextWrapPos(ImGui.getFontSize() * 35.0f);
            ImGui.textUnformatted(description);
            ImGui.popTextWrapPos();
            ImGui.endTooltip();
        }
    }


}
