package engine.graphics.buffers;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class IndexBuffer {
    private int vbo;
    private int[] indices;
    private boolean isBoundAlready;

    public IndexBuffer(int[] indices) {
        this.vbo = glGenBuffers();
        this.indices = indices;
        this.isBoundAlready = false;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public void bind() {
        if (! this.isBoundAlready) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            this.isBoundAlready = true;
        } else {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
            glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, indices);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        }

    }

    public int getCount() {
        return indices.length;
    }
}
