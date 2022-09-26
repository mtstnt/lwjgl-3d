package engine.graphics.buffers;

import static org.lwjgl.opengl.GL40.*;

public class VertexBuffer {
    public enum Type {
        POSITION,
        NORMALS,
        TEXCOORDS
    };

    private int vbo;
    private int index;
    private float[] vertices;
    private boolean isBoundAlready = false;

    public VertexBuffer(Type index, float[] vertices) {
        this.vbo = glGenBuffers();
        this.vertices = vertices;
        this.applyIndex(index);
    }

    private void applyIndex(Type index) {
        switch (index) {
            case POSITION: this.index = 0; break;
            case NORMALS: this.index = 1; break;
            case TEXCOORDS: this.index = 2; break;
        }
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public void bind() {
        if (!this.isBoundAlready) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
            glVertexAttribPointer(index, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(index);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            this.isBoundAlready = true;
        }
        else {
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
    }

    public int getCount() {
        return vertices.length / 3;
    }
}
