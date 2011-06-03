package spaceshift;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import engine.Shader;
import engine.ShaderUtil;
import engine.VBO;

public class Box {
    private Shader shifter;
    private VBO vbo;
    private float phi = 0.5f * (float) Math.PI;

    public void createBox() {
        float vert[] = { 0.0f, 2.0f, 1.0f, 2.0f, 2.0f, 1.0f, 2.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f };
        float col[] = { 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 0.5f };
        int ind[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };

        FloatBuffer vertices = BufferUtils.createFloatBuffer(vert.length);
        vertices.put(vert);
        vertices.flip();
        FloatBuffer colors = BufferUtils.createFloatBuffer(col.length);
        colors.put(col);
        colors.flip();
        IntBuffer index = BufferUtils.createIntBuffer(ind.length);
        index.put(ind);
        index.flip();

        vbo = new VBO(vertices, colors, index);
    }

    public Box() {
        shifter = new Shader("res/shaders/screen.vert",
                "res/shaders/screen.frag");

        createBox();
    }
    
    public void shift(float shiftFactor) {
        phi = shiftFactor;
    }

    public void draw() {
        shifter.useShader();

        ShaderUtil.setVertexAttribute(shifter, "phi", phi);

        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -10.0f);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);

        vbo.render();

        Shader.disableShaders();
    }

}
