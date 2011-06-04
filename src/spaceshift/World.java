package spaceshift;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.opengl.GL11;
import shapes.Circle;

import engine.Renderable;
import engine.ResourceManager;
import engine.Shader;

public class World {
    private final Set<Renderable> renderables;
    private final Set<Shiftable> shiftables;

    private float shiftFactor;

    public World() {
        initializeShaders();

        renderables = new HashSet<Renderable>();
        shiftables = new HashSet<Shiftable>();

        shiftFactor = (float) (0.5f * Math.PI);
        Rectangle box = new Rectangle(true);
        renderables.add(box);
        shiftables.add(box);

        renderables.add(new Circle(32));
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
    }

}
