package engine;

import java.util.HashMap;
import java.util.Map;

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
import org.lwjgl.util.vector.Vector4f;

public class Physics {
    private final World world;
    private static final Vec2 gravity = new Vec2(0, -10.0f);
    private final float timeStep = 1.0f / 60.0f;
    private final int velocityIterations = 6;
    private final int positionIterations = 2;
    private final Map<SceneObject, Body> bodyMap;

    public Physics() {
	bodyMap = new HashMap<SceneObject, Body>();

	world = new World(gravity, true);

	/* Create a ground */
	BodyDef groundDef = new BodyDef();
	groundDef.position.set(0, -4);
	Body groundBody = world.createBody(groundDef);
	PolygonShape boxShape = new PolygonShape();
	boxShape.setAsBox(50, 1);
	groundBody.createFixture(boxShape, 1);
    }

    public void createDynamicCircleBody(SceneObject sceneObject) {
	BodyDef bodyDef = new BodyDef();
	bodyDef.position.set(sceneObject.getWorldTransformation().m30,
		sceneObject.getWorldTransformation().m31);
	Body body = world.createBody(bodyDef);
	body.setType(BodyType.DYNAMIC);
	body.setUserData(sceneObject);

	CircleShape circleShape = new CircleShape();
	circleShape.m_radius = sceneObject.getWorldTransformation().m00;

	FixtureDef fixture = new FixtureDef();
	fixture.shape = circleShape;
	fixture.density = 2;
	body.createFixture(fixture);

	bodyMap.put(sceneObject, body);
    }

    public void adjustRectFixture(SceneObject sceneObject) {
	PolygonShape polygonShape = new PolygonShape();

	Vec2 vertices[] = new Vec2[4];

	Vector4f temp = new Vector4f();
	Matrix4f.transform(sceneObject.getPointerWorldTransformation(),
		new Vector4f(0, 0, 0, 0), temp);
	vertices[0] = new Vec2(temp.x, temp.y);

	Matrix4f.transform(sceneObject.getPointerWorldTransformation(),
		new Vector4f(1, 0, 0, 0), temp);
	vertices[1] = new Vec2(temp.x, temp.y);

	Matrix4f.transform(sceneObject.getPointerWorldTransformation(),
		new Vector4f(1, 1, 0, 0), temp);
	vertices[2] = new Vec2(temp.x, temp.y);

	Matrix4f.transform(sceneObject.getPointerWorldTransformation(),
		new Vector4f(0, 1, 0, 0), temp);
	vertices[3] = new Vec2(temp.x, temp.y);

	polygonShape.set(vertices, 4);

	Body body = bodyMap.get(sceneObject);

	if (body.getFixtureList() != null) {
	    body.destroyFixture(body.getFixtureList());
	}

	body.createFixture(polygonShape, 1);
    }

    public void createStaticRectBody(SceneObject sceneObject) {
	BodyDef bodyDef = new BodyDef();
	bodyDef.position.set(sceneObject.getWorldTransformation().m30,
		sceneObject.getWorldTransformation().m31);
	Body body = world.createBody(bodyDef);
	body.setType(BodyType.STATIC);
	body.setUserData(sceneObject);
	bodyMap.put(sceneObject, body);

	adjustRectFixture(sceneObject);
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
		sceneObject.getPointerWorldTransformation().m30 = body
			.getPosition().x;
		sceneObject.getPointerWorldTransformation().m31 = body
			.getPosition().y;
		// sceneObject.setWorldTransformation(new Matrix4f()
		// .translate(new Vector2f(body.getPosition().x, body
		// .getPosition().y)));
	    }

	    body = body.getNext();
	}
    }

    public void applyForce(SceneObject sceneObject, Vector2f force) {
	if (!bodyMap.containsKey(sceneObject)) {
	    System.out.println("No body for scene object found.");
	    return;
	}

	Body body = bodyMap.get(sceneObject);
	body.applyLinearImpulse(new Vec2(force.x, force.y),
		body.getWorldCenter());
    }
}
