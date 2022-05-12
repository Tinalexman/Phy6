package dream.physics.generators;

import dream.components.physics.PhysicsComponent;
import org.joml.Vector3f;

public interface ParticleForceGenerator
{
    Vector3f updateForce(PhysicsComponent physicsComponent, float duration);
}
