package engine;

import java.io.BufferedReader;
import java.io.FileReader;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;

public class Shader {
    private final int shader;
    private int vertexShader;
    private int fragmentShader;
    private boolean enabled;

    public Shader(String vertexShaderFile, String fragmentShaderFile) {
        shader = ARBShaderObjects.glCreateProgramObjectARB();

        if (shader != 0) {
            vertexShader = createVertShader(vertexShaderFile);
            fragmentShader = createFragShader(fragmentShaderFile);
        } else {
            enabled = false;
            return;
        }

        /*
         * if the vertex and fragment shaders setup sucessfully, attach them to
         * the shader program, link the sahder program (into the GL context I
         * suppose), and validate
         */
        if (vertexShader != 0 && fragmentShader != 0) {
            ARBShaderObjects.glAttachObjectARB(shader, vertexShader);
            ARBShaderObjects.glAttachObjectARB(shader, fragmentShader);
            ARBShaderObjects.glLinkProgramARB(shader);
            ARBShaderObjects.glValidateProgramARB(shader);
            enabled = true;
        } else {
            System.out.println("Shader failed to load.");
        }

        printLogInfo(shader);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void disable() {
        enabled = false;
    }

    public void useShader() {
        if (!enabled) {
            return;
        }

        ARBShaderObjects.glUseProgramObjectARB(shader);
    }

    public int getShader() {
        return shader;
    }

    public static void disableShaders() {
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    private int createVertShader(String filename) {
        // vertShader will be non zero if succefully created

        vertexShader = ARBShaderObjects
                .glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
        // if created, convert the vertex shader code to a String
        if (vertexShader == 0) {
            return 0;
        }
        String vertexCode = "";
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while ((line = reader.readLine()) != null) {
                vertexCode += line + "\n";
            }
        } catch (Exception e) {
            System.out.println("Fail reading vertex shading code");
            return 0;
        }
        /*
         * associate the vertex code String with the created vertex shader and
         * compile
         */
        ARBShaderObjects.glShaderSourceARB(vertexShader, vertexCode);
        ARBShaderObjects.glCompileShaderARB(vertexShader);
        printLogInfo(vertexShader);

        return vertexShader;
    }

    private int createFragShader(String filename) {

        fragmentShader = ARBShaderObjects
                .glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        if (fragmentShader == 0) {
            return 0;
        }
        String fragCode = "";
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while ((line = reader.readLine()) != null) {
                fragCode += line + "\n";
            }
        } catch (Exception e) {
            System.out.println("Fail reading fragment shading code");
            return 0;
        }
        ARBShaderObjects.glShaderSourceARB(fragmentShader, fragCode);
        ARBShaderObjects.glCompileShaderARB(fragmentShader);
        printLogInfo(fragmentShader);

        return fragmentShader;
    }

    private static void printLogInfo(int obj) {
        final int logLength = ARBShaderObjects.glGetObjectParameteriARB(obj,
                ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB);
        if (logLength <= 1)
            return;

        System.out.println("\nShader Program Info Log: ");
        System.out.println("--------------------------");
        System.out.println(ARBShaderObjects.glGetInfoLogARB(obj, logLength));
    }
}
