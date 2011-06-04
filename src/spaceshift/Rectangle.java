package spaceshift;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import engine.Renderable;
import engine.ResourceManager;
import engine.Shader;
import engine.ShaderUtil;
import engine.VBO;

public class Rectangle implements Renderable, Shiftable {
    private VBO vbo;
    private float phi = 0.5f * (float) Math.PI;
    private boolean shift;

    public Rectangle(boolean doShift) {
        createBox();
        this.shift = doShift;
    }

    private void createBox() {
        float vert[] = { 0.0f, 2.0f, 2.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f };

        FloatBuffer vertices = BufferUtils.createFloatBuffer(vert.length);
        vertices.put(vert);
        vertices.flip();

        vbo = new VBO.Builder(vertices).build();
    }

    @Override
    public void shift(float shiftFactor) {
        phi = shiftFactor;
    }

    @Override
    public void render() {
        Shader shifter = ResourceManager.INSTANCE.getShader(Shaders.SHIFTER);
        shifter.useShader();

        ShaderUtil.setVertexAttribute(shifter, "phi", phi);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);

        vbo.render();

        Shader.disableShaders();
    }

    @Override
    public boolean doShift() {
        return shift;
    }

}