package dream.physics.generators.buoyancy;

import dream.components.general.GeneralComponent;
import dream.components.physics.PhysicsComponent;
import dream.physics.generators.ParticleForceGenerator;
import org.joml.Vector3f;

public class BuoyancyGenerator implements ParticleForceGenerator
{
    public float maximumDepth;
    public float liquidVolume;
    public float liquidHeight;
    public float liquidDensity;

    public BuoyancyGenerator()
    {
        this(1.0f, 1.0f, 1.0f,1000.0f);
    }

    public BuoyancyGenerator(float maximumDepth, float liquidVolume, float liquidHeight)
    {
        this(maximumDepth, liquidVolume, liquidHeight, 1000.0f);
    }

    public BuoyancyGenerator(float maximumDepth, float liquidVolume, float liquidHeight, float liquidDensity)
    {
        this.maximumDepth = maximumDepth;
        this.liquidVolume = liquidVolume;
        this.liquidHeight = liquidHeight;
        this.liquidDensity = liquidDensity;
    }

    @Override
    public Vector3f updateForce(PhysicsComponent component, float duration)
    {
        // CALCULATE THE PARTICLE DEPTH
        float depth = component.transform.position.y;

        Vector3f force = new Vector3f();

        // CHECK IF WE ARE OUT OF THE LIQUID
        if(depth >= (this.liquidHeight + this.maximumDepth))
            return force;

        // CHECK IF WE ARE AT MAXIMUM DEPTH
        if(depth >= (this.liquidHeight - this.maximumDepth))
        {
            force.y = this.liquidDensity * liquidVolume;
            return force;
        }

        // OTHERWISE, WE ARE PARTLY SUBMERGED
        force.y = this.liquidDensity * liquidVolume * (depth - this.maximumDepth - this.liquidHeight) * this.maximumDepth * 0.5f;
        return force;
    }
}
