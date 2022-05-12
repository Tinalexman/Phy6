package dream.shader;

import dream.DreamEngine;
import org.joml.*;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL30.*;

public class Shader
{
    protected static final int notFound = -1;

    protected transient int programID;

    private final String shaderFilePath;
    protected transient Map<String, Integer> uniformVariables;

    public Shader(String shaderFileName)
    {
        this.shaderFilePath = DreamEngine.resourcePath + "\\shaders\\" + shaderFileName;
    }

    public void destroy()
    {
        stopProgram();
        if(this.programID != 0)
            glDeleteProgram(this.programID);
    }

    public void createShaderObject()
    {
        String[] shaders = processShader(this.shaderFilePath);
        this.programID = glCreateProgram();
        if(programID == 0)
            throw new RuntimeException("Cannot create shader program!");

        int vertexShader = createShader(GL_VERTEX_SHADER, shaders[0]);
        int fragmentShader = createShader(GL_FRAGMENT_SHADER, shaders[1]);

        linkProgram(vertexShader, fragmentShader);
        this.uniformVariables = new HashMap<>();
    }

    private int createShader(int shaderType, String shaderSource)
    {
        int shaderID = glCreateShader(shaderType);
        if(shaderID == 0)
            throw new IllegalStateException("Cannot create shader!");

        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0)
            throw new IllegalStateException("Cannot create shader: " + glGetShaderInfoLog(shaderID, 1024));

        glAttachShader(this.programID, shaderID);
        return shaderID;
    }

    private String[] processShader(String shaderFilePath)
    {
        String[] shaders = new String[2];
        try
        {
            String source = new String(Files.readAllBytes(Paths.get(shaderFilePath)));
            String[] splitShaders = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if(firstPattern.equalsIgnoreCase("vertex"))
                shaders[0] = splitShaders[1];
            else if(firstPattern.equalsIgnoreCase("fragment"))
                shaders[1] = splitShaders[1];
            else
                throw new RuntimeException("Illegal Shader Type: '" + firstPattern + "'");

            if(secondPattern.equalsIgnoreCase("vertex"))
                shaders[0] = splitShaders[2];
            else if(secondPattern.equalsIgnoreCase("fragment"))
                shaders[1] = splitShaders[2];
            else
                throw new RuntimeException("Illegal Shader Type: '" + secondPattern + "'");
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Cannot load shader: '" + shaderFilePath + "' due to " + ex.getMessage());
        }
        return shaders;
    }

    private void linkProgram(int vertexShader, int fragmentShader)
    {
        glLinkProgram(this.programID);
        if(glGetProgrami(this.programID, GL_LINK_STATUS) == 0)
        {
            String errorMessage = "Cannot link program: " + glGetProgramInfoLog(this.programID, 1024);
            throw new IllegalStateException(errorMessage);
        }

        glValidateProgram(this.programID);
        if(glGetProgrami(this.programID, GL_VALIDATE_STATUS) == 0)
            System.err.println("Warning! Shader code validation " + glGetProgramInfoLog(this.programID, 1024));

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void startProgram()
    {
        glUseProgram(this.programID);
    }

    public void stopProgram()
    {
        glUseProgram(0);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Shader shader))
            return false;
        return this.shaderFilePath.equals(shader.shaderFilePath);
    }

    private int uniformLocation(String name)
    {
        return glGetUniformLocation(this.programID, name);
    }

    public void storeUniforms(String... uniforms) throws Exception
    {
        for(String uniform : uniforms)
        {
            int location = uniformLocation(uniform);
            if(location == notFound)
            {
                throw new Exception("Uniform " + uniform + " could not be located in the shader! " +
                        "Check whether it was used in the shader code or perhaps it was spelt wrongly");
            }
            this.uniformVariables.put(uniform, location);
        }
    }

    protected void loadUniform(int location, int value)
    {
        glUniform1i(location, value);
    }

    protected void loadUnsignedUniform(int location, int value)
    {
        glUniform1ui(location, value);
    }

    protected void loadUniform(int location, float value)
    {
        glUniform1f(location, value);
    }

    protected void loadUniform(int location, boolean value)
    {
        glUniform1i(location, value ? 1 : 0);
    }

    protected void loadUniform(int location, Vector2f value)
    {
        glUniform2f(location, value.x, value.y);
    }

    protected void loadUniform(int location, Vector3f value)
    {
        glUniform3f(location, value.x, value.y, value.z);
    }

    protected void loadUniform(int location, Vector4f value)
    {
        glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    protected void loadUniform(int location, Matrix3f value)
    {
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            glUniformMatrix3fv(location, false, value.get(stack.mallocFloat(9)));
        }
    }

    protected void loadUniform(int location, Matrix4f value)
    {
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            glUniformMatrix4fv(location, false, value.get(stack.mallocFloat(16)));
        }
    }

    public void updateMVPMatrices(Matrix4f modelMatrix, Matrix4f viewMatrix, Matrix4f projectionMatrix)
    {
        updateTransform(ShaderConstants.modelMatrix, modelMatrix);
        updateTransform(ShaderConstants.projectionMatrix, projectionMatrix);
        updateTransform(ShaderConstants.viewMatrix, viewMatrix);
    }

    public void updateVPMatrices(Matrix4f viewMatrix, Matrix4f projectionMatrix)
    {
        updateTransform(ShaderConstants.projectionMatrix, projectionMatrix);
        updateTransform(ShaderConstants.viewMatrix, viewMatrix);
    }

    public void updateTransform(String transformName, Matrix4f matrix)
    {
        int location = this.uniformVariables.get(transformName);
        loadUniform(location, matrix);
    }

    public void updateObjectInfo(int drawIndex, int objectIndex)
    {
        int location = this.uniformVariables.get(ShaderConstants.drawIndex);
        loadUniform(location, drawIndex);

        location = this.uniformVariables.get(ShaderConstants.objectIndex);
        loadUniform(location, objectIndex);
    }

    public void updateColor(Vector4f color)
    {
        int location = this.uniformVariables.get(ShaderConstants.color);
        loadUniform(location, color);
    }
}
