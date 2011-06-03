package spaceshift;

import java.util.HashSet;
import java.util.Set;

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

        Box box = new Box(true);
        renderables.add(box);
        shiftables.add(box);
    }

    private void initializeShaders() {
        ResourceManager.INSTANCE.addShader(Shaders.SHIFTER, new Shader(
                "res/shaders/screen.vert", "res/shaders/screen.frag"));
    }

    public void draw() {
        for (Renderable renderable : renderables) {
            renderable.draw();
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
