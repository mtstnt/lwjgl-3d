package engine.graphics.buffers;

import static org.lwjgl.opengl.GL40.*;

// Sequential Buffer: Vertex buffer namun penyimpanan dilakukan secara sequential ->
// Structure: vx, vy, vz, nx, vny, vnz, vtu, vtv
public class SequentialBuffer {

    public static final int STRIDE = (3 + 3 + 2) * 4;

    private int ibo;
    private int vbo;

    private float[] buffer;

    public SequentialBuffer(float[] buffer) {
        vbo = glGenBuffers();
        this.buffer = buffer;
    }

    public float[] getBuffer() {
        return this.buffer;
    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, this.buffer, GL_STATIC_DRAW);

        int floatSize = 4;

        glVertexAttribPointer(0, 3, GL_FLOAT, false, STRIDE, 0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, STRIDE, 3 * floatSize);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, STRIDE, 6 * floatSize);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }


}
