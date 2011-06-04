package shapes;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import engine.Renderable;

import engine.VBO;

public class Circle implements Renderable {
    private FloatBuffer vertices;
    private VBO vbo;

    public Circle(int steps) {
        vertices = BufferUtils.createFloatBuffer(6 * steps);

        for (int i = 0; i < steps; i++) {
            vertices.put(new float[] { 0, 0 });
            vertices.put(new float[] {
                    (float) Math.cos(2 * Math.PI * i / steps),
                    (float) Math.sin(2 * Math.PI * i / steps) });
            vertices.put(new float[] {
                    (float) Math.cos(2 * Math.PI * (i + 1) / steps),
                    (float) Math.sin(2 * Math.PI * (i + 1) / steps) });
        }

        vertices.flip();

        vbo = new VBO.Builder(vertices).build();
    }

    @Override
    public void render() {
        vbo.render();
    }

}
