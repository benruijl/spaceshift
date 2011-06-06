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

    private final SceneObject player;
    private final Rectangle box; // test box
    private float shiftFactor;

    public Scene() {
	initializeShaders();

	renderables = new HashSet<Renderable>();
	shiftables = new HashSet<Shiftable>();

	shiftFactor = (float) (0.5f * Math.PI);
	box = new Rectangle(true);
	box.getPointerWorldTransformation().translate(new Vector2f(0, -3));
	renderables.add(box);
	shiftables.add(box);

	Circle circle = new Circle(32);
	circle.getPointerWorldTransformation()
		.scale(new Vector3f(0.5f, 0.5f, 0.5f))
		.translate(new Vector2f(-2, 0));

	renderables.add(circle);

	physics = new Physics();
	physics.createDynamicCircleBody(circle);
	physics.createStaticRectBody(box);
	player = circle;
    }

    private void initializeShaders() {
	ResourceManager.INSTANCE.addShader(Shaders.COLOR, new Shader(
		"res/shaders/color.vert", "res/shaders/color.frag"));
	ResourceManager.INSTANCE.addShader(Shaders.SHIFTER, new Shader(
		"res/shaders/screen.vert", "res/shaders/screen.frag"));
    }

    public void draw() {
	GL11.glLoadIdentity();
	GL11.glTranslatef(0.0f, 0.0f, -10.0f);

	for (Renderable renderable : renderables) {
	    renderable.render();
	}
    }

    public void update(int delta) {
	physics.doSimulation();

	for (Shiftable shiftable : shiftables) {
	    if (shiftable.doShift()) {
		shiftable.shift(shiftFactor);
	    }
	}
    }

    public void doAction() {
	shiftFactor -= 0.01f;

	if (shiftFactor < 0) {
	    shiftFactor = (float) (0.5f * Math.PI);
	}
	
	physics.adjustRectFixture(box);
    }

    public void Move(Vector2f dir) {
	physics.applyForce(player, dir);
    }

}
