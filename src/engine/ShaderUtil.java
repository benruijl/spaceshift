package engine;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public final class ShaderUtil {
	private ShaderUtil() {

	}

	public static void setVertexAttribute(Shader shader,
			CharSequence attribute, float value) {
		int attribLoc = GL20.glGetAttribLocation(shader.getShader(), attribute);

		if (attribLoc == -1) {
			System.out.println("Could not find attribute " + attribute);
			return;
		}

		GL20.glVertexAttrib1f(attribLoc, value);
	}

	public static void setUniform(Shader shader, CharSequence attribute,
			Matrix4f value) {
		int location = GL20.glGetUniformLocation(shader.getShader(), attribute);

		if (location == -1) {
			System.out.println("Could not find uniform " + attribute);
			return;
		}

		FloatBuffer floatvalue = BufferUtils.createFloatBuffer(16);
		value.store(floatvalue);
		floatvalue.flip();
		GL20.glUniformMatrix4(location, false, floatvalue);
	}
}
