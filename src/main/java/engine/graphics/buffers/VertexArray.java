package engine.graphics.buffers;

import static org.lwjgl.opengl.GL40.*;

public class VertexArray {
    private final int vao;

    public VertexArray() {
        this.vao = glGenVertexArrays();
    }

    public void bind() {
        glBindVertexArray(vao);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void use(Runnable fn) {
        bind();
        fn.run();
        unbind();
    }
}
