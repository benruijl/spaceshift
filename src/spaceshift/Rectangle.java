package spaceshift;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.Renderable;
import engine.ResourceManager;
import engine.SceneObject;
import engine.Shader;
import engine.ShaderUtil;
import engine.VBO;

public class Rectangle extends SceneObject implements Renderable, Shiftable {
    private VBO vbo;
    private float phi = 0.5f * (float) Math.PI;
    private boolean shift;

    private Vector2f realPos;

    public Rectangle(boolean doShift, Vector2f realPos) {
	createBox();
	this.shift = doShift;
	this.realPos = realPos;

	getPointerWorldTransformation().m30 = realPos.x;
	getPointerWorldTransformation().m31 = realPos.y;
    }

    private void createBox() {
	float vert[] = { 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
		1.0f, 0.0f, 0.0f };

	FloatBuffer vertices = BufferUtils.createFloatBuffer(vert.length);
	vertices.put(vert);
	vertices.flip();

	vbo = new VBO.Builder(vertices).build();
    }

    @Override
    public void shift(float shiftFactor) {
	if (!doShift()) {
	    return;
	}
	
	phi = shiftFactor;

	getPointerWorldTransformation().m10 = (float) Math.cos(phi);
	getPointerWorldTransformation().m11 = (float) Math.sin(phi);
	getPointerWorldTransformation().m30 = realPos.x + realPos.y
		* (float) Math.cos(phi);
	getPointerWorldTransformation().m31 = realPos.y * (float) Math.sin(phi);
    }

    @Override
    public void render() {
	Shader shifter = ResourceManager.INSTANCE.getShader(Shaders.COLOR);
	shifter.useShader();

	ShaderUtil.setUniform(shifter, "worldtrans", getWorldTransformation());
	vbo.render();

	Shader.disableShaders();
    }

    @Override
    public boolean doShift() {
	return shift;
    }

    @Override
    public void shrink(float shrinkFactor) {
	if (!doShift()) {
	    return;
	}
	
	getPointerWorldTransformation().scale(new Vector3f(1, shrinkFactor, 1));
	getPointerWorldTransformation().m31 *= shrinkFactor;
    }

}
