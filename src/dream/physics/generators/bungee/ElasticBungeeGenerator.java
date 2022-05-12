package dream.physics.generators.bungee;

import dream.components.physics.PhysicsComponent;
import dream.physics.generators.ParticleForceGenerator;
import org.joml.Vector3f;

public class ElasticBungeeGenerator implements ParticleForceGenerator
{
    public PhysicsComponent otherPhysicsComponent;

    public float springConstant;
    public float originalLength;

    public ElasticBungeeGenerator()
    {
        this(null, 1.0f, 1.0f);
    }

    public ElasticBungeeGenerator(PhysicsComponent otherPhysicsComponent, float springConstant, float originalLength)
    {
        this.otherPhysicsComponent = otherPhysicsComponent;
        this.springConstant = springConstant;
        this.originalLength = originalLength;
    }

    @Override
    public Vector3f updateForce(PhysicsComponent component, float duration)
    {
        // CALCULATE THE VECTOR OF THE SPRING
        Vector3f force = new Vector3f(component.transform.position);
        force.sub(otherPhysicsComponent.transform.position);

        // CHECK IF THE BUNGEE IS COMPRESSED
        float magnitude = force.length();
        if(magnitude <= this.originalLength)
            return new Vector3f();

        magnitude = this.springConstant * (this.originalLength - magnitude);

        // CALCULATE THE FINAL FORCE AND APPLY IT
        force.normalize();
        force.mul(-magnitude);
        return force;
    }
}
