package dream.physics.registry;

import dream.components.physics.PhysicsComponent;
import dream.nodes.Node;
import dream.physics.generators.ParticleForceGenerator;

import java.util.ArrayList;
import java.util.List;

public class ParticleForceRegistry
{
    static class ParticleForceRegistration
    {
        public PhysicsComponent component;
        public ParticleForceGenerator generator;
    }


    private static final List<ParticleForceRegistration> registrations = new ArrayList<>();

    public static void add(PhysicsComponent particle, ParticleForceGenerator forceGenerator)
    {
        ParticleForceRegistration registration = new ParticleForceRegistration();
        registration.component = particle;
        registration.generator = forceGenerator;
        registrations.add(registration);
    }

    public static void remove(PhysicsComponent particle, ParticleForceGenerator forceGenerator)
    {
        ParticleForceRegistration registration = new ParticleForceRegistration();
        registration.component = particle;
        registration.generator = forceGenerator;
        registrations.remove(registration);
    }

    public static void clear()
    {

    }

    public static void updateForces(float duration)
    {
        for(ParticleForceRegistration registration : registrations)
            registration.generator.updateForce(registration.component, duration);
    }
}
