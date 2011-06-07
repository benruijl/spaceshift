package spaceshift;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import shapes.Circle;
import engine.Physics;
import engine.Renderable;
import engine.ResourceManager;
import engine.SceneObject;
import engine.Shader;

public class Scene {
    private final Physics physics;

    private final Set<Renderable> renderables;
    private final Set<Shiftable> shiftables;

    private Vector2f cameraPos;

    private final SceneObject player;
    private final Rectangle box, lift; // test box
    private float shiftFactor;
    private float shrinkFactor;
    private static final float maximumShift = 0.9f;
    private static final float maximumShrink = 0.1f;

    public Scene() {
	initializeShaders();

	renderables = new HashSet<Renderable>();
	shiftables = new HashSet<Shiftable>();

	Rectangle floor = new Rectangle(true, new Vector2f(-10, -1));
	floor.getPointerWorldTransformation().scale(new Vector3f(100, 1, 1));
	renderables.add(floor);

	shiftFactor = (float) (0.5f * Math.PI);
	shrinkFactor = 1;
	box = new Rectangle(true, new Vector2f(0, 0));
	renderables.add(box);
	shiftables.add(box);

	lift = new Rectangle(true, new Vector2f(1.5f, 2));
	renderables.add(lift);
	shiftables.add(lift);

	Circle circle = new Circle(32);
	circle.getPointerWorldTransformation()
		.scale(new Vector3f(0.5f, 0.5f, 0.5f))
		.translate(new Vector2f(-2, 5));

	renderables.add(circle);

	physics = new Physics();
	physics.createDynamicCircleBody(circle);
	physics.createStaticRectBody(box);
	physics.createStaticRectBody(lift);
	physics.createStaticRectBody(floor);
	player = circle;

	cameraPos = new Vector2f();
	cameraPos.x = 0;
	cameraPos.y = -3;
    }

    private void initializeShaders() {
	ResourceManager.INSTANCE.addShader(Shaders.COLOR, new Shader(
		"res/shaders/color.vert", "res/shaders/color.frag"));
    }

    public void draw() {
	GL11.glLoadIdentity();
	GL11.glTranslatef(cameraPos.x, cameraPos.y, -10.0f);

	for (Renderable renderable : renderables) {
	    renderable.render();
	}
    }

    public void update(int delta) {
	shiftFactor += 0.01f;

	if (shiftFactor > 0.5f * Math.PI) {
	    shiftFactor = (float) (0.5f * Math.PI);
	}

	shrinkFactor += 0.01f;

	if (shrinkFactor > 1) {
	    shrinkFactor = 1;
	}

	physics.adjustRectFixture(box);
	physics.adjustRectFixture(lift);

	physics.doSimulation();

	for (Shiftable shiftable : shiftables) {
	    if (shiftable.doShift()) {
		shiftable.shift(shiftFactor);
		shiftable.shrink(shrinkFactor);
	    }
	}

	cameraPos.x = -player.getPointerWorldTransformation().m30;
    }

    public void doAction() {
	shiftFactor -= 0.03f;

	if (shiftFactor < maximumShift) {
	    shiftFactor = maximumShift;
	}
    }

    public void doShrink() {
	shrinkFactor -= 0.03f;

	if (shrinkFactor < maximumShrink) {
	    shrinkFactor = maximumShrink;
	}
    }

    public void Move(Vector2f dir) {
	physics.applyForce(player, dir);
    }

}
