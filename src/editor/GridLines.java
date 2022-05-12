package editor;

import dream.camera.Camera;
import dream.debug.Debug;
import dream.graphics.Graphics;
import org.joml.Vector3f;

public class GridLines
{
    protected static final int[] gridWidth = {32};
    protected static final int[] gridHeight = {32};
    protected static final boolean[] drawGridLines = {false};
    protected static final boolean[] snapToGrid = {false};
    protected static Vector3f gridColor;

    protected static Camera camera;

    public static void initialize(Camera camera)
    {
        GridLines.camera = camera;
        GridLines.gridColor = new Vector3f(0.2f);
    }

    public static void drawGrid()
    {
        Vector3f cameraPosition = camera.cameraPosition;
        int windowWidth = Graphics.getWindowSize()[0], windowHeight = Graphics.getWindowSize()[1];

        int firstX = ((int) (cameraPosition.x / GridLines.gridWidth[0]) - 1) * GridLines.gridWidth[0];
        int firstY = ((int) (cameraPosition.y / GridLines.gridHeight[0]) - 1) * GridLines.gridHeight[0];

        int numVerticalLines = (windowWidth / GridLines.gridWidth[0]) + 2;
        int numHorizontalLines = (windowHeight / GridLines.gridHeight[0]) + 2;

        int width = (GridLines.gridWidth[0] * 2) + windowWidth;
        int height = (GridLines.gridHeight[0] * 2) + windowHeight;

        int maxLines = Math.max(numVerticalLines, numHorizontalLines);
        for (int i = 0; i < maxLines; i++)
        {
            int x = firstX + (GridLines.gridWidth[0] * i);
            int y = firstY + (GridLines.gridHeight[0] * i);

            // Change this to take the z position into account too
            if(i < numVerticalLines)
            {
                Debug.addLine(new Vector3f(x, firstY, 0.0f),
                        new Vector3f(x, firstY + height, 0.0f), GridLines.gridColor);
            }

            if(i < numHorizontalLines)
            {
                Debug.addLine(new Vector3f(firstX, y, 0.0f),
                        new Vector3f(firstX + width, y, 0.0f), GridLines.gridColor);
            }
        }

    }

}
