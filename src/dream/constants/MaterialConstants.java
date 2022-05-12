package dream.constants;

import dream.assets.Material;
import org.joml.Vector4f;

public class MaterialConstants
{
    public static final Material CARAMEL = new Material(new Vector4f(1.0f, 0.5f, 0.31f, 1.0f),
            new Vector4f(1.0f, 0.8f, 0.31f, 1.0f), new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 32.0f);

    public static final Material DEFAULT = new Material(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),
            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 32.0f);

    public static final Material RED = new Material(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),
            new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 32.0f);

    public static final Material GREEN = new Material(new Vector4f(0.0f, 1.0f, 0.0f, 1.0f),
            new Vector4f(0.0f, 1.0f, 0.0f, 1.0f), new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 32.0f);

    public static final Material BLUE = new Material(new Vector4f(0.0f, 0.0f, 1.0f, 1.0f),
            new Vector4f(0.0f, 0.0f, 1.0f, 1.0f), new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 32.0f);

    public static final Material PURPLE = new Material(new Vector4f(1.0f, 0.0f, 1.0f, 1.0f),
            new Vector4f(1.0f, 0.0f, 1.0f, 1.0f), new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 32.0f);

    public static final Material CYAN = new Material(new Vector4f(0.0f, 1.0f, 1.0f, 1.0f),
            new Vector4f(0.0f, 1.0f, 1.0f, 1.0f), new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 32.0f);

    public static final Material YELLOW = new Material(new Vector4f(1.0f, 1.0f, 0.0f, 1.0f),
            new Vector4f(1.0f, 1.0f, 0.0f, 1.0f), new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 32.0f);

    public static final Material ORANGE = new Material(new Vector4f(1.0f, 0.5f, 0.0f, 1.0f),
            new Vector4f(1.0f, 0.5f, 0.0f, 1.0f), new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 32.0f);

}
