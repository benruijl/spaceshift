package engine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;

public class VBO {
    private final int vertexBufferID, colourBufferID, indexBufferID;
    private final int vertexCount;

    public static class Builder {
        private final int vertexBufferID;
        private int colourBufferID;
        private int indexBufferID;
        private final int vertexCount;

        public Builder(FloatBuffer vertices) {
            vertexBufferID = createVBOID();
            colourBufferID = -1;
            indexBufferID = -1;
            bufferData(vertexBufferID, vertices);

            // direct buffer, capacity should be length
            vertexCount = vertices.capacity();
        }

        public Builder color(FloatBuffer colors) {
            colourBufferID = createVBOID();
            bufferData(colourBufferID, colors);
            return this;
        }

        public Builder indices(IntBuffer indices) {
            indexBufferID = createVBOID();
            bufferElementData(indexBufferID, indices);
            return this;
        }

        public VBO build() {
            return new VBO(vertexBufferID, colourBufferID, indexBufferID,
                    vertexCount);
        }

    }

    private VBO(int vertexBufferID, int colourBufferID, int indexBufferID,
            int vertexCount) {
        super();
        this.vertexBufferID = vertexBufferID;
        this.colourBufferID = colourBufferID;
        this.indexBufferID = indexBufferID;
        this.vertexCount = vertexCount;
    }

    public void render() {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        ARBBufferObject.glBindBufferARB(
                ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertexBufferID);
        GL11.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);

        if (colourBufferID > -1) {
            GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
            ARBBufferObject.glBindBufferARB(
                    ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, colourBufferID);
            GL11.glColorPointer(4, GL11.GL_FLOAT, 0, 0);
        }

        if (indexBufferID > -1) {
            ARBBufferObject.glBindBufferARB(
                    ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB,
                    indexBufferID);
            GL12.glDrawRangeElements(GL11.GL_QUADS, 0, vertexCount,
                    vertexCount / 2, GL11.GL_UNSIGNED_INT, 0);
        } else {
            GL11.glDrawArrays(GL11.GL_QUADS, 0, vertexCount / 2);
        }

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
    }

    public static int createVBOID() {
        if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
            IntBuffer buffer = BufferUtils.createIntBuffer(1);
            ARBBufferObject.glGenBuffersARB(buffer);
            return buffer.get(0);
        }
        return 0;
    }

    public static void bufferData(int id, FloatBuffer buffer) {
        if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
            ARBBufferObject.glBindBufferARB(
                    ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
            ARBBufferObject.glBufferDataARB(
                    ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer,
                    ARBBufferObject.GL_STATIC_DRAW_ARB);
        }
    }

    public static void bufferElementData(int id, IntBuffer buffer) {
        if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
            ARBBufferObject.glBindBufferARB(
                    ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
            ARBBufferObject.glBufferDataARB(
                    ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, buffer,
                    ARBBufferObject.GL_STATIC_DRAW_ARB);
        }
    }

}
