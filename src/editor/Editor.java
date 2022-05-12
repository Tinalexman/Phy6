package editor;

import dream.DreamEngine;
import dream.camera.Camera;
import dream.components.general.GeneralComponent;
import dream.components.graphics.Color;
import dream.components.graphics.GraphicComponent;
import dream.components.graphics.Mesh;
import dream.debug.Debug;
import dream.events.EventManager;
import dream.events.EventType;
import dream.events.Handler;
import dream.graphics.Graphics;
import dream.io.input.Input;
import dream.nodes.Node;
import dream.nodes.NodeType;
import dream.renderer.Renderer;
import dream.scene.EditorScene;
import imgui.ImGui;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Editor implements Handler
{
    // EDITOR VIEWPORT CONFIGS
    private boolean renderEditorScene;

    // EDITOR THEME
    protected Vector4f editorSelectColor;

    // EDITOR MOUSE CONFIGURATIONS
    private final float[] previousScrolls;
    protected float[] sensitivity = {0.05f};
    private final Vector3f clickOrigin = new Vector3f();
    private final float[] dragFrameDelay = {0.032f};

    // EDITOR KEY CONFIGURATIONS
    private final Vector3f editorCameraIncrement = new Vector3f();
    private final float[] lerpTime = {0.0f};
    private final boolean[] resetOrigin = {false};

    // EDITOR CONSTANTS
    public static final float minimumCameraPitch = -89.0F;
    public static final float maximumCameraPitch = 89.0F;
    public static final float minimumCameraZoom = 1.0F;
    public static final float maximumCameraZoom = 45.0F;

    public static final float mouseSensitivity = 0.2F;
    public static final float editorSpecularPower = 10.0f;
    public static final Vector3f editorAmbientLight = new Vector3f(1.0f, 1.0f, 1.0f);

    private static final float[] cameraMoveSpeed = {0.05f};
    private static final float[] editorCameraNearPlane = {0.01f};
    private static final float[] editorCameraFarPlane = {1000.0f};
    private static final float[] editorCameraZoom = {(Editor.minimumCameraZoom + Editor.maximumCameraZoom) * 0.5f};

    // MAIN EDITOR WINDOWS
    private final AssetsWindow assetsWindow;
    private final ResourcesWindow resourcesWindow;
    private final Settings settings;
    private final World world;
    private final InspectorWindow inspectorWindow;
    private final SceneGraphWindow sceneGraphWindow;
    private final Viewport viewport;
    private final DebugWindow debugWindow;
    private final FileUtility fileUtility;

    // EDITOR CAMERA
    protected final Camera editorCamera;

    // EDITOR RENDERER
    private final Renderer renderer;

    public Editor()
    {
        this.assetsWindow = new AssetsWindow();
        this.resourcesWindow = new ResourcesWindow();
        this.inspectorWindow = new InspectorWindow();
        this.sceneGraphWindow = new SceneGraphWindow();
        this.viewport = new Viewport();
        this.world = new World();
        this.settings = new Settings();
        this.debugWindow = new DebugWindow();
        this.fileUtility = new FileUtility();

        this.editorCamera = new Camera();
        this.renderer = new Renderer();

        this.renderEditorScene = true;

        this.editorSelectColor = new Vector4f(0.8f);

        this.previousScrolls = new float[2];
    }

    public void initializeEditor()
    {
        EventManager.registerHandler(EventType.EndWindowFrame, this);
        EventManager.registerHandler(EventType.StartViewportRuntime, this);
        EventManager.registerHandler(EventType.EndViewportRuntime, this);


        this.editorCamera.farPlane = Editor.editorCameraFarPlane;
        this.editorCamera.nearPlane = Editor.editorCameraNearPlane;
        this.editorCamera.cameraZoom = Editor.editorCameraZoom;
        this.editorCamera.aspectRatio = Graphics.getAspectRatio();

        this.settings.cameraNearPlane = Editor.editorCameraNearPlane;
        this.settings.cameraFarPlane = Editor.editorCameraFarPlane;
        this.settings.cameraZoom = Editor.editorCameraZoom;
        this.settings.cameraPosition = this.editorCamera.getPosition();
        this.settings.cameraRotation = this.editorCamera.getCameraRotation();
        this.settings.cameraChange = this.editorCamera.hasChanged();
        this.settings.selectionColor = this.editorSelectColor;
        this.settings.cameraSpeed = Editor.cameraMoveSpeed;

        this.editorCamera.requestActivation();

        this.renderer.initialize();
        this.renderer.setCamera(this.editorCamera);

        this.viewport.useTargetRatio = this.renderer.shouldUseTargetRatio();
        this.settings.useTargetRatio = this.renderer.shouldUseTargetRatio();

        this.sceneGraphWindow.setInspectorWindow(this.inspectorWindow);

        this.debugWindow.maximumLines = Debug.getMaximumLines();
        this.settings.viewportClearColor = this.renderer.getViewPortClearColor();

        this.settings.cameraPosition.set(0.0f, -0.2f, 0.5f);

        //TODO: Remove this line as soon as assets are drawn and can create new scene
        editScene(new EditorScene());
    }

    @Override
    public void respond(EventType eventType)
    {
        if(eventType == EventType.EndWindowFrame)
        {
            this.previousScrolls[0] = Input.getScrollX();
            this.previousScrolls[1] = Input.getScrollY();
        }
        else if(eventType == EventType.StartViewportRuntime)
        {
            List<Node> nodes = this.renderer.getRuntimeNodes();
            if(nodes != null)
            {
                DreamEngine.submitPhysicsList(this.renderer.getRuntimeNodes());
                renderEditorScene = false;
            }
        }
        else if(eventType == EventType.EndViewportRuntime)
            renderEditorScene = true;
    }

    public void checkInputs()
    {
        this.editorCameraIncrement.set(0.0f, 0.0f, 0.0f);

        if(this.viewport.getWantCaptureMouse())
        {
            if(this.previousScrolls[1] != 0.0f)
            {
                this.editorCamera.incrementCameraZoom(this.previousScrolls[1] * this.sensitivity[0]);
                float cameraZoom = this.editorCamera.getCameraZoom()[0];
                if(cameraZoom < minimumCameraZoom)
                    this.editorCamera.setCameraZoom(minimumCameraZoom);
                if(cameraZoom > maximumCameraZoom)
                    this.editorCamera.setCameraZoom(maximumCameraZoom);
            }

            if(Input.isButtonPressed(GLFW_MOUSE_BUTTON_RIGHT))
            {
                // Pan around scene origin;
                System.out.println("Right Button Pressed!");
            }

            if(Input.isButtonPressed(GLFW_MOUSE_BUTTON_LEFT))
            {
                float[] screenCoordinates = Input.getScreenCoordinates();
                int x = (int) screenCoordinates[0];
                int y = (int) screenCoordinates[1];

                int ID = this.renderer.readPixelAt(x, y);
                Node selectedNode = this.renderer.getSelectedNode(ID);
                this.inspectorWindow.setSelectedNode(selectedNode);
            }

            if(Input.isKeyPressed(GLFW_KEY_W))
                this.editorCameraIncrement.z = -1.0f;
            if(Input.isKeyPressed(GLFW_KEY_S))
                this.editorCameraIncrement.z = 1.0f;

            if(Input.isKeyPressed(GLFW_KEY_LEFT))
                this.editorCameraIncrement.x = -1.0f;
            if(Input.isKeyPressed(GLFW_KEY_RIGHT))
                this.editorCameraIncrement.x = 1.0f;

            if(Input.isKeyPressed(GLFW_KEY_UP))
                this.editorCameraIncrement.y = -1.0f;
            if(Input.isKeyPressed(GLFW_KEY_DOWN))
                this.editorCameraIncrement.y = 1.0f;
        }

        if(Input.isKeyPressed(GLFW_KEY_LEFT_CONTROL) || Input.isKeyPressed(GLFW_KEY_RIGHT_CONTROL))
        {
            if(Input.isKeyTyped(GLFW_KEY_S))
            {
                EditorScene currentScene = this.renderer.getScene();
                if(currentScene != null)
                {
                    if(!currentScene.isSaved())
                    {
                        currentScene.setSaved(true);
//                        String filePath = DreamEngine.homeDirectory + "\\scenes\\" + scene.name;
//                        SceneManager.saveScene(scene, filePath);
                    }
                }
            }

            if(Input.isKeyTyped(GLFW_KEY_N))
            {
                String newSceneName = this.fileUtility.showNewSceneDialog();
                if(!newSceneName.isEmpty())
                    editScene(new EditorScene(newSceneName));
            }
        }

        this.editorCamera.moveCamera(this.editorCameraIncrement.mul(cameraMoveSpeed[0]));
    }

    private void saveCurrentScene(EditorScene scene)
    {

    }

    public void editScene(EditorScene editorScene)
    {
        editorScene.loaded();
        this.viewport.sceneName = editorScene.name;
        this.viewport.savedScene = editorScene.getSaved();
        this.sceneGraphWindow.setScene(editorScene);

        Node node = new Node("Test Node", 1);
        node.nodeType = NodeType.Plane;
        GeneralComponent comp2 = new GeneralComponent();
        GraphicComponent comp = new GraphicComponent();

        comp.mesh = new Mesh(node.nodeType, 0.2f);
        comp.mesh.load();
        comp.color = new Color(1.0f, 0.5f, 0.3f, 1.0f);

        comp2.componentName = node.name;

        node.addComponent(comp2);
        node.addComponent(comp);
        editorScene.addSceneObject(node);

        this.renderer.editScene(editorScene);
    }

    public void drawEditor()
    {
        Debug.beginFrame();
        this.renderer.setAvailableDebugLines(Debug.hasLines());
        this.renderer.render(this.renderEditorScene);

        this.sceneGraphWindow.drawImGui();
        this.inspectorWindow.drawImGui();
        this.assetsWindow.drawImGui();
        this.settings.drawImGui();
        this.resourcesWindow.drawImGui();
        this.world.drawImGui();
        this.debugWindow.drawImGui();

        this.viewport.renderViewPort(this.renderer.getCurrentViewportID());
        ImGui.showDemoWindow();
    }

    public void cleanUp()
    {
        this.renderer.cleanUp();
    }

    public List<String> formatNodes(List<Node> nodes)
    {
        return this.renderer.formatNodes(nodes);
    }

    public Camera getCamera()
    {
        return this.editorCamera;
    }
}
