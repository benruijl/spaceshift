package shapes;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import spaceshift.Shaders;
import engine.Renderable;
import engine.ResourceManager;
import engine.SceneObject;
import engine.Shader;
import engine.ShaderUtil;
import engine.VBO;

public class Circle extends SceneObject implements Renderable {
    private VBO vbo;

    public Circle(int steps) {
	FloatBuffer vertices;
	IntBuffer index;

	vertices = BufferUtils.createFloatBuffer((steps + 1) * 2);
	index = BufferUtils.createIntBuffer(6 * steps);

	vertices.put(new float[] { 0, 0 });
	for (int i = 0; i < steps; i++) {
	    vertices.put(new float[] {
		    (float) Math.cos(2 * Math.PI * i / (float) steps),
		    (float) Math.sin(2 * Math.PI * i / (float) steps) });

	    if (i == steps - 1) {
		index.put(new int[] { 0, 1, i + 1 });
	    } else {
		index.put(new int[] { 0, i + 2, i + 1 });
	    }
	}

	vertices.flip();
	index.flip();

	vbo = new VBO.Builder(vertices).indices(index).build();
    }

    @Override
    public void render() {
	Shader shader = ResourceManager.INSTANCE.getShader(Shaders.COLOR);
	shader.useShader();
	ShaderUtil.setUniform(shader, "worldtrans", getWorldTransformation());

	vbo.render();
	Shader.disableShaders();
    }

}
