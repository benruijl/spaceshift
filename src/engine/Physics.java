package engine;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

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
		groundDef.position.set(0, -5);
		Body groundBody = new Body(groundDef, world);
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(50, 10);
		groundBody.createFixture(boxShape, 1);
	}

	public void createDynamicBody(SceneObject sceneObject) {
		BodyDef groundDef = new BodyDef();
		groundDef.position.set(0, -5);
		Body body = new Body(groundDef, world);
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
			SceneObject sceneObject = ((SceneObject) body.getUserData());

			body = body.getNext();
		}
	}
}
