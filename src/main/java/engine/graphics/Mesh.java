package engine.graphics;

import engine.Camera;
import engine.LightSource;
import engine.Scene;
import engine.Utils;
import engine.graphics.buffers.IndexBuffer;
import engine.graphics.buffers.VertexArray;
import engine.graphics.buffers.VertexBuffer;
import engine.graphics.shaders.ShaderProgram;
import engine.interfaces.Renderable;
import org.joml.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.lwjgl.opengl.GL40.*;

public class Mesh implements Renderable {

    protected final VertexArray vertexArray;
    protected VertexBuffer positions;
    protected VertexBuffer normals;
    protected VertexBuffer texCoords;
    protected IndexBuffer indices;
    protected Texture texture;

    // TODO: Someday pindahin ke class sendiri.
    protected ShaderProgram shaderProgram;
    protected Matrix4f transform;

    public static Mesh fromObj(String path) throws Exception {
        FileReader fr = new FileReader(path);
        Scanner scanner = new Scanner(fr);

        ArrayList<Vector3f> vertices = new ArrayList<>();
        ArrayList<Vector3i> indices = new ArrayList<>();
        ArrayList<Vector3f> normals = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String[] words = scanner.nextLine().split(" ");

            if (words[0].equals("#")) {
                continue;
            }

            if (words[0].equals("v")) {
                vertices.add(new Vector3f(
                        Float.parseFloat(words[1]),
                        Float.parseFloat(words[2]),
                        Float.parseFloat(words[3]))
                );
            } else if (words[0].equals("vn")) {
                normals.add(new Vector3f(
                        Float.parseFloat(words[1]),
                        Float.parseFloat(words[2]),
                        Float.parseFloat(words[3]))
                );
            } else if (words[0].equals("f")) {
                var v = new Vector3i();
                String[] l;

                l = words[1].split("/");
                v.x = Integer.parseInt(l[0]) - 1;

                l = words[2].split("/");
                v.y = Integer.parseInt(l[0]) - 1;

                l = words[3].split("/");
                v.z = Integer.parseInt(l[0]) - 1;

                indices.add(v);
            }
        }

        Mesh mesh = new Mesh();
        mesh.setVertices(Utils.flatten3f(vertices));
        mesh.setIndices(Utils.flatten3i(indices));
        mesh.setNormals(Utils.flatten3f(normals));

        return mesh;
    }

    public Mesh() {
        vertexArray = new VertexArray();
        transform = new Matrix4f();
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
        vertexArray.use(() -> this.texCoords.bind(2));
    }

    public void setIndices(int[] indices) {
        if (this.indices == null) {
            this.indices = new IndexBuffer(indices);
        } else {
            this.indices.setIndices(indices);
        }
        vertexArray.use(() -> this.indices.bind());
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setShader(ShaderProgram shader) {
        this.shaderProgram = shader;
    }

    public void rotate(float angle, Vector3f up) {
        this.transform.mul(new Matrix4f().rotate(angle, up));
    }

    public void translate(Vector3f v) {
        this.transform.mul(new Matrix4f().translate(v));
    }

    public Matrix4f getTransform() {
        return transform;
    }

    public void render(Scene scene, int type) {
        Camera camera = scene.getCamera();

        vertexArray.bind();
        shaderProgram.bind();

        shaderProgram.setUniformMat4f("u_projection", camera.getProjection());
        shaderProgram.setUniformMat4f("u_view", camera.getView());
        shaderProgram.setUniformMat4f("u_model", transform);
        shaderProgram.setUniformVec4f("u_color", new Vector4f(1.0f, 0.5f, 1.0f, 1.0f));
        shaderProgram.setUniformVec3f("u_cameraPosition", camera.getPosition());

        shaderProgram.setUniformInt("u_depthMapSampler", 1);
        shaderProgram.setUniformInt("u_skyboxSampler", 2);
        shaderProgram.setUniformFloat("u_farPlane", ShadowMap.FAR_PLANE);

        List<LightSource> lightSources = scene.getLightSources();
        if (lightSources != null) {
            for (int i = 0; i < lightSources.size(); i++) {
                shaderProgram.setUniformVec3f("u_lightSources[" + i + "].position", lightSources.get(i).getPosition());
                shaderProgram.setUniformVec4f("u_lightSources[" + i + "].color", lightSources.get(i).getColor());
            }
        }

        if (texture != null) {
            shaderProgram.setUniformInt("u_texture", 0);
            texture.bindToActiveTexture();
        }

        if (indices == null) {
            glDrawArrays(type, 0, positions.getCount());
        } else {
            glDrawElements(type, indices.getCount(), GL_UNSIGNED_INT, 0);
        }

        shaderProgram.unbind();
        vertexArray.unbind();
    }

    public void render(Scene scene) {
        render(scene, GL_TRIANGLES);
    }

    @Override
    public void render(Scene scene, ShaderProgram customShader) {
        Camera camera = scene.getCamera();

        vertexArray.bind();
        customShader.bind();

        customShader.setUniformMat4f("u_projection", camera.getProjection());
        customShader.setUniformMat4f("u_view", camera.getView());
        customShader.setUniformMat4f("u_model", transform);
        customShader.setUniformVec4f("u_color", new Vector4f(1.0f, 0.5f, 1.0f, 1.0f));
        customShader.setUniformVec3f("u_cameraPosition", camera.getPosition());

        customShader.setUniformInt("u_depthMapSampler", 1);
        customShader.setUniformInt("u_skyboxSampler", 2);
        customShader.setUniformFloat("u_farPlane", ShadowMap.FAR_PLANE);

        List<LightSource> lightSources = scene.getLightSources();
        if (lightSources != null) {
            for (int i = 0; i < lightSources.size(); i++) {
                customShader.setUniformVec3f("u_lightSources[" + i + "].position", lightSources.get(i).getPosition());
                customShader.setUniformVec4f("u_lightSources[" + i + "].color", lightSources.get(i).getColor());
            }
        }

        if (texture != null) {
            customShader.setUniformInt("u_textureSampler", 0);
            texture.bindToActiveTexture();
        }

        if (indices == null) {
            glDrawArrays(GL_TRIANGLES, 0, positions.getCount());
        } else {
            glDrawElements(GL_TRIANGLES, indices.getCount(), GL_UNSIGNED_INT, 0);
        }

        shaderProgram.unbind();
        vertexArray.unbind();
    }
}
