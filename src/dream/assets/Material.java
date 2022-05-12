package dream.assets;

import dream.constants.MaterialConstants;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class Material
{
    public final Vector4f ambientColor;
    public final Vector4f diffuseColor;
    public final Vector4f specularColor;
    public Texture texture;
    public float reflectance;

    public boolean changed;

    public Material()
    {
        this(MaterialConstants.DEFAULT);
        this.changed = true;
    }

    public Material(Material material)
    {
        this.ambientColor = new Vector4f(material.ambientColor);
        this.diffuseColor= new Vector4f(material.diffuseColor);
        this.specularColor = new Vector4f(material.specularColor);
        this.reflectance = material.reflectance;
        this.changed = true;
    }

    public Material(Vector4f ambient, Vector4f diffuseColor, Vector4f specularColor, float reflectance)
    {
        this.ambientColor = ambient;
        this.diffuseColor= diffuseColor;
        this.specularColor = specularColor;
        this.reflectance = reflectance;
        this.changed = true;
    }

    public Material(Vector4f color, float reflectance)
    {
        this(color, color, color, reflectance);
    }

    public Vector4f getAmbientColor()
    {
        return this.ambientColor;
    }

    public Vector4f getDiffuseColor()
    {
        return this.diffuseColor;
    }

    public Vector4f getSpecularColor()
    {
        return this.specularColor;
    }

    public float getReflectance()
    {
        return this.reflectance;
    }

    public void setAmbientColor(Vector4f ambientColor)
    {
        if(!this.ambientColor.equals(ambientColor))
        {
            this.changed = true;
            this.ambientColor.set(ambientColor);
        }
    }

    public void setDiffuseColor(Vector4f diffuseColor)
    {
        if(!this.diffuseColor.equals(diffuseColor))
        {
            this.changed = true;
            this.diffuseColor.set(diffuseColor);
        }
    }

    public void setSpecularColor(Vector4f specularColor)
    {
        if(!this.specularColor.equals(specularColor))
        {
            this.changed = true;
            this.specularColor.set(specularColor);
        }
    }

    public void setReflectance(float reflectance)
    {
        if(this.reflectance != reflectance)
        {
            this.changed = true;
            this.reflectance = reflectance;
        }
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Material material))
            return false;
        return material.diffuseColor.equals(this.diffuseColor)
                && material.ambientColor.equals(this.ambientColor)
                && material.specularColor.equals(this.specularColor)
                && material.reflectance == this.reflectance;
    }

    public boolean hasChanged()
    {
        return this.changed;
    }

    public void resetChange()
    {
        this.changed = false;
    }

    public Texture getTexture()
    {
        return this.texture;
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    public boolean hasTexture()
    {
        return !this.texture.getFilePath().equals("");
    }
}
