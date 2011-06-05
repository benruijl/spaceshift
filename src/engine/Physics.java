package engine;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

public class Physics {
    private final World world;
    private static final Vec2 gravity = new Vec2(0, -0.1f);
    private final float timeStep = 1.0f / 60.0f;
    private final int velocityIterations = 6;
    private final int positionIterations = 2;

    public Physics() {
	world = new World(gravity, true);

	/* Create a ground */
	BodyDef groundDef = new BodyDef();
	groundDef.position.set(0, -4);
	Body groundBody = world.createBody(groundDef);
	PolygonShape boxShape = new PolygonShape();
	boxShape.setAsBox(50, 1);
	groundBody.createFixture(boxShape, 1);
    }

    public void createDynamicBody(SceneObject sceneObject) {
	BodyDef bodyDef = new BodyDef();
	bodyDef.position.set(sceneObject.getWorldTransformation().m30,
		sceneObject.getWorldTransformation().m31);
	Body body = world.createBody(bodyDef);
	body.setType(BodyType.DYNAMIC);
	body.setUserData(sceneObject);

	CircleShape circleShape = new CircleShape();
	circleShape.m_radius = 1.0f;

	FixtureDef fixture = new FixtureDef();
	fixture.shape = circleShape;
	fixture.density = 1;
	body.createFixture(fixture);
    }

    public void doSimulation() {
	world.step(timeStep, velocityIterations, positionIterations);
	world.clearForces();

	/* Update scene objects */
	Body body = world.getBodyList();
	while (body != null) {
	    if (body.getUserData() != null) {
		SceneObject sceneObject = ((SceneObject) body.getUserData());
		// FIXME: do somewhere else
		sceneObject.setWorldTransformation(new Matrix4f()
			.translate(new Vector2f(body.getPosition().x, body
				.getPosition().y)));
	    }

	    body = body.getNext();
	}
    }
}
