package engine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;

public class VBO {
    private final int vertexBufferID, colourBufferID, indexBufferID;
    private final int vertexCount;

    public VBO(FloatBuffer vertices, FloatBuffer colors, IntBuffer indices) {
        vertexBufferID = createVBOID();
        colourBufferID = createVBOID();
        indexBufferID = createVBOID();
        bufferData(vertexBufferID, vertices);
        bufferData(colourBufferID, colors);
        bufferElementData(indexBufferID, indices);
        vertexCount = vertices.capacity(); // direct buffer, should be length
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
        GL12.glDrawRangeElements(GL11.GL_QUADS, 0, vertexCount, vertexCount / 3,
                GL11.GL_UNSIGNED_INT, 0);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    }

    public int createVBOID() {
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

}
