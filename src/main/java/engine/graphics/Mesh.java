package engine.graphics;

import engine.Camera;
import engine.graphics.buffers.IndexBuffer;
import engine.graphics.buffers.VertexArray;
import engine.graphics.buffers.VertexBuffer;
import engine.graphics.shaders.Shader;
import engine.graphics.shaders.ShaderProgram;
import org.joml.*;

import java.lang.Math;
import java.nio.Buffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL40.*;

public class Mesh {

    protected final VertexArray vertexArray;
    protected VertexBuffer positions;
    protected VertexBuffer normals;
    protected VertexBuffer texCoords;
    protected IndexBuffer indices;

    // TODO: Someday pindahin ke class sendiri.
    protected ShaderProgram shaderProgram;
    protected Matrix4f transform;

    public Mesh() {
        vertexArray = new VertexArray();
        transform = new Matrix4f().rotate((float) Math.toRadians(30.f), new Vector3f(0, 1, 0));
    }

    public void setVertices(float[] vertices) {
        if (this.positions == null) {
            this.positions = new VertexBuffer(VertexBuffer.Type.POSITION, vertices);
        } else {
            this.positions.setVertices(vertices);
        }
        vertexArray.use(() -> this.positions.bind());
    }

    public void setNormals(float[] normals) {
        if (this.normals == null) {
            this.normals = new VertexBuffer(VertexBuffer.Type.NORMALS, normals);
        } else {
            this.normals.setVertices(normals);
        }
        vertexArray.use(() -> this.normals.bind());
    }

    public void setTexCoords(float[] texCoords) {
        if (this.texCoords == null) {
            this.texCoords = new VertexBuffer(VertexBuffer.Type.TEXCOORDS, texCoords);
        } else {
            this.texCoords.setVertices(texCoords);
        }
        vertexArray.use(() -> this.texCoords.bind());
    }

    public void setIndices(int[] indices) {
        if (this.indices == null) {
            this.indices = new IndexBuffer(indices);
        } else {
            this.indices.setIndices(indices);
        }
        vertexArray.use(() -> this.indices.bind());
    }

    public void setShader(ShaderProgram shader) {
        this.shaderProgram = shader;
    }

    public void render(Camera camera, int type) {
        vertexArray.bind();
        shaderProgram.bind();

        shaderProgram.setUniformMat4f("u_projection", camera.getProjection());
        shaderProgram.setUniformMat4f("u_view", camera.getView());
        shaderProgram.setUniformMat4f("u_model", transform);
        shaderProgram.setUniformVec4f("u_color", new Vector4f(0.0f, 0.0f, 1.0f, 1.0f));

        if (indices == null) {
           glDrawArrays(type, 0, positions.getCount());
        } else {
           glDrawElements(type, indices.getCount(), GL_UNSIGNED_INT, 0);
        }

        shaderProgram.unbind();
        vertexArray.unbind();
    }

    public void render(Camera camera) {
        render(camera, GL_TRIANGLES);
    }

    //    protected ShaderProgram shaderProgram;
//
//    public Mesh(VertexBuffer vertexBuffer, IndexBuffer indexBuffer) {
//        this.vao = glGenVertexArrays();
//
//        this.vertexBuffer = vertexBuffer;
//        this.indexBuffer = indexBuffer;
//    }
//
//    public void render() {
//        render(GL_TRIANGLES);
//    }
//
//    public void render(int type) {
//        glBindVertexArray(this.vao);
//        shaderProgram.bind();
//        if (indexBuffer == null) {
//            glDrawArrays(type, 0, vertexBuffer.getCount());
//        } else {
//            glDrawElements(type, indexBuffer.getCount(), GL_UNSIGNED_INT, 0);
//        }
//        shaderProgram.unbind();
//        glBindVertexArray(0);
//    }
//
//    public void setShader(ShaderProgram shaderProgram) {
//        this.shaderProgram = shaderProgram;
//    }
//
//    public ShaderProgram getShader() {
//        return this.shaderProgram;
//    }
}
