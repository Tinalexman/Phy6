package dream.renderer;

import dream.camera.Camera;
import dream.components.Component;
import dream.debug.Debug;
import dream.graphics.Graphics;
import dream.io.output.BufferFactory;
import dream.io.output.FrameBuffer;
import dream.nodes.Node;
import dream.scene.EditorScene;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class Renderer
{
    public static final int nosScene = -1;

    // Comgigurations
    protected int framebufferTextureID;

    private final NodeRenderer nodeRenderer;
    private final PickingRenderer pickingRenderer;
    private final Vector4f viewPortClearColor;

    private Camera camera;
    private FrameBuffer frameBuffer;

    private EditorScene editorScene;
    private final boolean[] useTargetRatio;
    private boolean availableDebugLines;

    public Renderer()
    {
        this.nodeRenderer = new NodeRenderer();
        this.pickingRenderer = new PickingRenderer();
        this.useTargetRatio = new boolean[] {false};
        this.viewPortClearColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.availableDebugLines = false;
    }

    public void initialize()
    {
        this.pickingRenderer.initialize();
        this.nodeRenderer.initialize();
        int[] size = Graphics.getMaxWindowSize();
        this.frameBuffer = BufferFactory.createFrameBuffer(size[0], size[1]);
        this.framebufferTextureID = this.frameBuffer.getTextureID();
        glViewport(0, 0, size[0], size[1]);
    }

    public void editScene(EditorScene editorScene)
    {
        this.editorScene = editorScene;
    }

    public List<Node> getRuntimeNodes()
    {
        if(this.editorScene != null)
            return this.editorScene.getSceneObjects(false);
        return null;
    }

    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }

    public int readPixelAt(int xPosition, int yPosition)
    {
        return this.pickingRenderer.getPickingTexture().readPixel(xPosition, yPosition);
    }

    public void cleanUp()
    {
        this.pickingRenderer.cleanUp();
        this.frameBuffer.destroy();
    }

    public void setAvailableDebugLines(boolean debugLines)
    {
        this.availableDebugLines = debugLines;
    }

    public void prepare()
    {
        this.frameBuffer.bindFrameBuffer();
        glClearColor(this.viewPortClearColor.x, this.viewPortClearColor.y, this.viewPortClearColor.z, this.viewPortClearColor.w);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(boolean renderEditorScene)
    {
        if(this.editorScene == null)
            return;

        List<Node> nodes = this.editorScene.getSceneObjects(renderEditorScene);
        this.pickingRenderer.render(this.camera, nodes);

        prepare();
        this.nodeRenderer.render(this.camera, nodes);
        if(this.availableDebugLines)
            Debug.drawLines();

        postRender(this.camera, nodes);
    }

    private void postRender(Camera camera, List<Node> nodes)
    {
        this.frameBuffer.unbindFrameBuffer();

        camera.reset();
        for(Node node : nodes)
        {
            List<Component> components = node.components;
            for(Component component : components)
                component.setChanged();
        }
    }

    public int getCurrentViewportID()
    {
        return this.editorScene != null ? this.framebufferTextureID : Renderer.nosScene;
    }

    public List<String> formatNodes(List<Node> nodes)
    {
        if(this.editorScene == null)
            return new ArrayList<>();
        return this.editorScene.formatNodes(nodes);
    }

    public Node getSelectedNode(int ID)
    {
        if(this.editorScene == null)
            return null;
        return this.editorScene.getNode(ID);
    }

    public boolean[] shouldUseTargetRatio()
    {
        return this.useTargetRatio;
    }

    public Vector4f getViewPortClearColor()
    {
        return this.viewPortClearColor;
    }

    public EditorScene getScene()
    {
        return this.editorScene;
    }
}
