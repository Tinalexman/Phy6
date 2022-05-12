package dream.physics.generators.spring;

import dream.components.general.GeneralComponent;
import dream.components.physics.PhysicsComponent;
import org.joml.Vector3f;
import dream.physics.generators.ParticleForceGenerator;

public class AnchoredSpringGenerator implements ParticleForceGenerator
{
    public Vector3f anchorLocation;

    public float springConstant;
    public float originalLength;

    public AnchoredSpringGenerator()
    {
        this(new Vector3f(1.0f), 1.0f, 1.0f);
    }

    public AnchoredSpringGenerator(Vector3f anchor, float springConstant, float originalLength)
    {
        this.anchorLocation = anchor;
        this.springConstant = springConstant;
        this.originalLength = originalLength;
    }

    @Override
    public Vector3f updateForce(PhysicsComponent component, float duration)
    {
        Vector3f force = new Vector3f(component.transform.position);
        force.sub(this.anchorLocation);

        // CALCULATE THE MAGNITUDE OF THE FORCE
        float magnitude = force.length();
        magnitude = Math.abs(magnitude - this.originalLength);
        magnitude *= this.springConstant;

        // CALCULATE THE FINAL FORCE AND APPLY IT
        force.normalize();
        force.mul(-magnitude);
        return force;
    }
}
