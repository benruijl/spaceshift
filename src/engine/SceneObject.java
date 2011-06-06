package engine;

import org.lwjgl.util.vector.Matrix4f;

public class SceneObject {
	private Matrix4f worldTransformation;

	public SceneObject() {
		worldTransformation = new Matrix4f();
	}

	public Matrix4f getWorldTransformation() {
		/* Defensive copy */
		return new Matrix4f().load(worldTransformation);
	}
	
	public Matrix4f getPointerWorldTransformation() {
		return worldTransformation;
	}

	public void setWorldTransformation(Matrix4f worldTransformation) {
		this.worldTransformation = worldTransformation;
	}
}
