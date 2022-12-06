package engine.graphics;

import engine.*;
import engine.graphics.buffers.IndexBuffer;
import engine.graphics.buffers.SequentialBuffer;
import engine.graphics.buffers.VertexArray;
import engine.graphics.shaders.ShaderProgram;
import engine.interfaces.Renderable;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class MeshObject implements Renderable {

    protected final VertexArray vertexArray;
    protected SequentialBuffer vertexBuffer;
    protected IndexBuffer indexBuffer;

    protected ArrayList<Texture> textures;
    protected Material material;

    protected ShaderProgram shaderProgram;
    protected Matrix4f transform;

    public MeshObject() {
        vertexArray = new VertexArray();
        transform = new Matrix4f();
    }

    public void setVertices(float[] vertices) {
        vertexBuffer = new SequentialBuffer(vertices);
    }

    public void setIndices(int[] indices) {
        indexBuffer = new IndexBuffer(indices);
    }

    public void setTextures(ArrayList<Texture> textures) {
        this.textures = textures;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setShaderProgram(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public void bind() {
        vertexArray.use(() -> {
            vertexBuffer.bind();
            indexBuffer.bind();
        });
    }

    public void render(Scene scene) {
        render(scene, shaderProgram);
    }

    @Override
    public void render(Scene scene, ShaderProgram customShader) {
        Camera camera = scene.getCamera();

        vertexArray.bind();
        customShader.bind();

        // TODO: Bind texture OR material.
        material.bindToShader(customShader);

        customShader.setUniformMat4f("u_projection", camera.getProjection());
        customShader.setUniformMat4f("u_view", camera.getView());
        customShader.setUniformMat4f("u_model", transform);
        customShader.setUniformVec3f("u_cameraPosition", camera.getPosition());

        // Shadow sampler
        customShader.setUniformInt("u_depthMapSampler", 1);
        customShader.setUniformFloat("u_farPlane", ShadowMap.FAR_PLANE);

        List<LightSource> lightSources = scene.getLightSources();

        if (lightSources != null) {
            for (int i = 0; i < lightSources.size(); i++) {
                customShader.setUniformVec3f("u_lightSources[" + i + "].position", lightSources.get(i).getPosition());
                customShader.setUniformVec4f("u_lightSources[" + i + "].color", lightSources.get(i).getColor());
            }
        }

        glDrawElements(GL_TRIANGLES, indexBuffer.getCount(), GL_UNSIGNED_INT, 0);

        customShader.unbind();
        vertexArray.unbind();
    }

    public Matrix4f getTransform() {
        return transform;
    }
}
