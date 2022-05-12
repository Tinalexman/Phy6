package dream.components.physics;

import dream.components.Component;
import dream.components.general.Transform;
import editor.Controls;
import org.joml.Vector3f;

public class PhysicsComponent extends Component
{
    public Vector3f velocity;
    public Vector3f forceAccumulated;

    public float damping;
    public float inverseMass;
    public float mass;
    public Transform transform;

    public PhysicsComponent()
    {
        this.name = "Physics";
        this.velocity = new Vector3f();
        this.forceAccumulated = new Vector3f();

        this.damping = 1.0f;
        this.inverseMass = 1.0f;
        this.mass = 1.0f;
    }

    @Override
    public void onStart()
    {

    }

    @Override
    public void onDestroyRequested()
    {

    }

    public void integrate(float duration)
    {
        if(duration <= 0.0f)
            return;

        Vector3f tempVector = new Vector3f();

        // UPDATE THE LINEAR POSITION
        this.velocity.mul(duration, tempVector);
        this.transform.position.add(tempVector);

        //CALCULATE ACCELERATION FROM FORCES
        Vector3f acceleration = new Vector3f();
        tempVector.set(acceleration);
        this.forceAccumulated.mul(this.inverseMass);
        tempVector.add(this.forceAccumulated);

        // UPDATE THE LINEAR VELOCITY
        this.velocity.add(tempVector.mul(duration));

        // IMPOSING DRAG
        this.velocity.mul((float) Math.pow(this.damping, duration));

        // CLEARING THE ACCUMULATED FORCE
        this.forceAccumulated.set(0.0f);
    }

    public void addForce(Vector3f force)
    {
        this.forceAccumulated.add(force);
    }

    @Override
    public void drawImGui()
    {
        Controls.drawVector3Control("Velocity:", this.velocity, false);
        float msChange = Controls.drawFloatControl("Mass:", this.mass, false);
        float dgChange = Controls.drawFloatControl("Damping:", this.damping, false);

        if(this.mass != msChange)
        {
            this.mass = msChange;
            this.inverseMass = 1 / msChange;
            this.changed = true;
        }

        if(this.damping != dgChange)
        {
            this.damping = dgChange;
            this.changed = true;
        }
    }

    @Override
    public void onChanged()
    {

    }

}
