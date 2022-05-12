package dream.io.output;

import dream.graphics.Graphics;

public class BufferFactory
{
    public static FrameBuffer createFrameBuffer()
    {
        int[] maxSize = Graphics.getMaxWindowSize();
        return createFrameBuffer(maxSize[0], maxSize[1]);
    }

    public static FrameBuffer createFrameBuffer(int width, int height)
    {
        return new FrameBuffer(width, height);
    }

    public static PickingTexture createPickingTexture()
    {
        int[] maxSize = Graphics.getMaxWindowSize();
        return createPickingTexture(maxSize[0], maxSize[1]);
    }

    public static PickingTexture createPickingTexture(int width, int height)
    {
        return new PickingTexture(width, height);
    }

}
