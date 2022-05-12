package dream.physics.generators.spring;

import dream.components.general.GeneralComponent;
import dream.components.physics.PhysicsComponent;
import dream.physics.generators.ParticleForceGenerator;
import org.joml.Vector3f;

public class FreeSpringGenerator implements ParticleForceGenerator
{
    public PhysicsComponent otherComponent;

    public float springConstant;
    public float originalLength;

    public FreeSpringGenerator()
    {
        this(null, 1.0f, 1.0f);
    }

    public FreeSpringGenerator(PhysicsComponent otherComponent, float springConstant, float originalLength)
    {
        this.otherComponent = otherComponent;
        this.springConstant = springConstant;
        this.originalLength = originalLength;
    }

    @Override
    public Vector3f updateForce(PhysicsComponent component, float duration)
    {
        Vector3f force = new Vector3f(component.transform.position);
        force.sub(otherComponent.transform.position);

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
