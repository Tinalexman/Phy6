package dream.physics.generators.drag;

import dream.components.physics.PhysicsComponent;
import dream.physics.generators.ParticleForceGenerator;
import org.joml.Vector3f;

public class DragGenerator implements ParticleForceGenerator
{
    private final float dragCoefficient;
    private final float dragCoefficientSquared;

    public DragGenerator()
    {
        this(1.0f, 1.0f);
    }

    public DragGenerator(float dragCoefficient, float dragCoefficientSquared)
    {
        this.dragCoefficient = dragCoefficient;
        this.dragCoefficientSquared = dragCoefficientSquared;
    }


    @Override
    public Vector3f updateForce(PhysicsComponent component, float duration)
    {
        Vector3f force = new Vector3f(component.velocity);

        // CALCULATE THE DRAG COEFFICIENT
        float dragMagnitude = force.length();
        dragMagnitude = (dragCoefficient * dragMagnitude) + (dragCoefficientSquared * dragMagnitude * dragMagnitude);

        // CALCULATE FINAL FORCE
        force.normalize();
        force.mul(dragMagnitude);
        return force;
    }
}
