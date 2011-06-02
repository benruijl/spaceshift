package spaceshift;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;

public class Box {
	private boolean useShader = true;
	private int shader = 0;
	private int vertShader = 0;
	private int fragShader = 0;
	
	private float phi = 0.5f * (float)Math.PI;

	private int vertexBufferID, colourBufferID, indexBufferID, maxIndex,
			indexBufferSize;

	public static int createVBOID() {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			IntBuffer buffer = BufferUtils.createIntBuffer(1);
			ARBVertexBufferObject.glGenBuffersARB(buffer);
			return buffer.get(0);
		}
		return 0;
	}

	public static void bufferData(int id, FloatBuffer buffer) {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			ARBVertexBufferObject.glBindBufferARB(
					ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
			ARBVertexBufferObject.glBufferDataARB(
					ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer,
					ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		}
	}

	public static void bufferElementData(int id, IntBuffer buffer) {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			ARBVertexBufferObject.glBindBufferARB(
					ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
			ARBVertexBufferObject.glBufferDataARB(
					ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, buffer,
					ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		}
	}

	public void render() {
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		ARBVertexBufferObject.glBindBufferARB(
				ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertexBufferID);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		ARBVertexBufferObject.glBindBufferARB(
				ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, colourBufferID);
		GL11.glColorPointer(4, GL11.GL_FLOAT, 0, 0);

		ARBVertexBufferObject.glBindBufferARB(
				ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB,
				indexBufferID);
		GL12.glDrawRangeElements(GL11.GL_QUADS, 0, maxIndex, indexBufferSize,
				GL11.GL_UNSIGNED_INT, 0);

		// GL11.glDrawArrays(GL11.GL_QUADS, 0, maxIndex);

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
	}

	public void createBox() {
		// First simple object
		float vert[] = { 0.0f, 2.0f, 1.0f, 2.0f, 2.0f, 1.0f, 2.0f, 0.0f,
				1.0f, 0.0f, 0.0f, 1.0f }; // vertex array
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

		vertexBufferID = createVBOID();
		colourBufferID = createVBOID();
		indexBufferID = createVBOID();
		bufferData(vertexBufferID, vertices);
		bufferData(colourBufferID, colors);
		bufferElementData(indexBufferID, index);
		maxIndex = 11;
		indexBufferSize = 4;
	}

	public Box() {

		/*
		 * create the shader program. If OK, create vertex and fragment shaders
		 */
		shader = ARBShaderObjects.glCreateProgramObjectARB();

		if (shader != 0) {
			vertShader = createVertShader("res/shaders/screen.vert");
			fragShader = createFragShader("res/shaders/screen.frag");
		} else
			useShader = false;

		/*
		 * if the vertex and fragment shaders setup sucessfully, attach them to
		 * the shader program, link the sahder program (into the GL context I
		 * suppose), and validate
		 */
		if (vertShader != 0 && fragShader != 0) {
			ARBShaderObjects.glAttachObjectARB(shader, vertShader);
			ARBShaderObjects.glAttachObjectARB(shader, fragShader);
			ARBShaderObjects.glLinkProgramARB(shader);
			ARBShaderObjects.glValidateProgramARB(shader);
			printLogInfo(shader);
		} else {
			System.out.println("shader fail");
			useShader = false;
		}

		createBox();
	}

	/*
	 * If the shader was setup succesfully, we use the shader. Otherwise we run
	 * normal drawing code.
	 */
	public void draw() {
		if (useShader) {
			ARBShaderObjects.glUseProgramObjectARB(shader);
		}
		
		phi-=0.01f;
		
		if (phi < 0) {
			phi = (float) (0.5f * Math.PI);
		}
				
		int phi_loc = GL20.glGetAttribLocation(shader, "phi");
		System.out.println(phi_loc);
		GL20.glVertexAttrib1f(phi_loc, phi);   
	    
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0f, 0.0f, -10.0f);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);// white

		render();

		// release the shader
		ARBShaderObjects.glUseProgramObjectARB(0);

	}

	/*
	 * With the exception of syntax, setting up vertex and fragment shaders is
	 * the same.
	 * 
	 * @param the name and path to the vertex shader
	 */
	private int createVertShader(String filename) {
		// vertShader will be non zero if succefully created

		vertShader = ARBShaderObjects
				.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
		// if created, convert the vertex shader code to a String
		if (vertShader == 0) {
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
		ARBShaderObjects.glShaderSourceARB(vertShader, vertexCode);
		ARBShaderObjects.glCompileShaderARB(vertShader);

		System.out.println(vertShader);

		// if there was a problem compiling, reset vertShader to zero
		printLogInfo(vertShader);
		// if zero we won't be using the shader
		return vertShader;
	}

	// same as per the vertex shader except for method syntax
	private int createFragShader(String filename) {

		fragShader = ARBShaderObjects
				.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		if (fragShader == 0) {
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
		ARBShaderObjects.glShaderSourceARB(fragShader, fragCode);
		ARBShaderObjects.glCompileShaderARB(fragShader);
		printLogInfo(fragShader);

		return fragShader;
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
