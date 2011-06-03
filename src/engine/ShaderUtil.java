package engine;

import org.lwjgl.opengl.GL20;

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
}
