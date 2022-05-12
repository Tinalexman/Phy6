package dream.physics.generators.gravity;

import dream.components.physics.PhysicsComponent;
import dream.physics.generators.ParticleForceGenerator;
import org.joml.Vector3f;

public class GravityGenerator implements ParticleForceGenerator
{
    private final Vector3f gravitationalAcceleration;

    public GravityGenerator()
    {
        this(new Vector3f());
    }

    public GravityGenerator(Vector3f gravity)
    {
        this.gravitationalAcceleration = gravity;
    }

    @Override
    public Vector3f updateForce(PhysicsComponent component, float duration)
    {
        float particleMass = 1 / component.inverseMass;
        if(particleMass <= 0.0f)
            return null;
        Vector3f force = new Vector3f();
        this.gravitationalAcceleration.mul(particleMass, force);
        return force;
    }
}
