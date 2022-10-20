package engine.graphics.buffers;

import static org.lwjgl.opengl.GL15.*;

public class IndexBuffer {
    private int ibo;
    private int[] indices;

    public IndexBuffer(int[] indices) {
        this.ibo = glGenBuffers();
        this.indices = indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public void bind() {
        this.ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }

    public int getCount() {
        return indices.length;
    }
}
