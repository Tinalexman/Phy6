package dream.renderer;

import dream.camera.Camera;
import dream.components.general.GeneralComponent;
import dream.components.graphics.GraphicComponent;
import dream.components.graphics.Mesh;
import dream.nodes.Node;
import dream.shader.Shader;
import dream.shader.ShaderConstants;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class NodeRenderer
{
    private final Shader shader;

    public NodeRenderer()
    {
        this.shader = new Shader("nodeShader.glsl");
    }

    public void initialize()
    {
        try
        {
            this.shader.createShaderObject();
            this.shader.storeUniforms(ShaderConstants.modelMatrix, ShaderConstants.viewMatrix,
                    ShaderConstants.projectionMatrix, ShaderConstants.color);
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
        glEnable(GL_DEPTH_TEST);

        shader.startProgram();
        for(Node node : nodes)
        {
            try
            {
                GraphicComponent graphicComponent = node.getComponent(GraphicComponent.class);
                GeneralComponent generalComponent = node.getComponent(GeneralComponent.class);

                loadMVPMatrix(camera, shader, generalComponent);
                if(graphicComponent.isChanged())
                    shader.updateColor(graphicComponent.color.getRGBA());

                Mesh mesh = graphicComponent.mesh;
                if(mesh == null)
                    continue;
                mesh.enable();

                if(mesh.indices != null)
                    glDrawElements(GL_TRIANGLES, nodes.size(), GL_UNSIGNED_INT, 0);
                else
                    glDrawArrays(GL_TRIANGLES, 0, mesh.getVertexCount());

                mesh.disable();
            }
            catch (Exception ex)
            {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        shader.stopProgram();

        glDisable(GL_DEPTH_TEST);
    }
}
