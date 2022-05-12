package dream.renderer;

import dream.camera.Camera;
import dream.components.general.GeneralComponent;
import dream.components.graphics.GraphicComponent;
import dream.components.graphics.Mesh;
import dream.graphics.Graphics;
import dream.io.output.BufferFactory;
import dream.io.output.PickingTexture;
import dream.nodes.Node;
import dream.shader.Shader;
import dream.shader.ShaderConstants;
import dream.tools.OpenGlUtils;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class PickingRenderer
{
    protected final Shader pickingShader;
    protected PickingTexture pickingTexture;
    protected boolean blendEnabled;

    public PickingRenderer()
    {
        this.pickingShader = new Shader("pickingShader.glsl");
    }

    public void initialize()
    {
        try
        {
            this.pickingShader.createShaderObject();
            this.pickingShader.storeUniforms(ShaderConstants.modelMatrix, ShaderConstants.viewMatrix,
                    ShaderConstants.projectionMatrix, ShaderConstants.drawIndex, ShaderConstants.objectIndex);
            this.pickingTexture = BufferFactory.createPickingTexture();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void loadMVPMatrix(Camera camera, Shader shader, GeneralComponent generalComponent)
    {
        if(generalComponent.isChanged() || camera.hasChanged()[0])
        {
            shader.updateMVPMatrices(generalComponent.transform.getTransformationMatrix(),
                    camera.getViewMatrix(), camera.getProjectionMatrix());
        }
    }

    public void render(Camera camera, List<Node> nodes)
    {
        this.blendEnabled = glIsEnabled(GL_BLEND);
        if(this.blendEnabled)
            OpenGlUtils.disableBlending();

        this.pickingTexture.enableWriting();
        glViewport(0, 0, Graphics.getWindowWidth(), Graphics.getWindowHeight());
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        this.pickingShader.startProgram();

        for(int pos = 0; pos < nodes.size(); ++pos)
        {
            Node node = nodes.get(pos);
            loadMVPMatrix(camera, pickingShader, node.getComponent(GeneralComponent.class));
            this.pickingShader.updateObjectInfo(pos, (node.ID + 1));

            Mesh mesh = node.getComponent(GraphicComponent.class).mesh;
            if(mesh == null)
                continue;

            mesh.enable();
            if(mesh.indices != null)
                glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
            else
                glDrawArrays(GL_TRIANGLES, 0, mesh.getVertexCount());
            mesh.disable();
        }

        this.pickingShader.stopProgram();
        this.pickingTexture.disableWriting();
        if(this.blendEnabled)
            OpenGlUtils.enableAdditiveBlending();
    }

    public PickingTexture getPickingTexture()
    {
        return this.pickingTexture;
    }

    public void cleanUp()
    {
        this.pickingTexture.destroy();
        this.pickingShader.destroy();
    }

}
