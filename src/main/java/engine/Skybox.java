package engine;

import engine.graphics.Texture;
import engine.graphics.buffers.VertexArray;
import engine.graphics.buffers.VertexBuffer;
import engine.graphics.shaders.Shader;
import engine.graphics.shaders.ShaderProgram;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL40.*;

public class Skybox {

    public static final int FACE_COUNT = 6;
    public static final String SKYBOX_VERTEX_SHADER_PATH = "assets/shaders/skybox/vertex.glsl";
    public static final String SKYBOX_FRAGMENT_SHADER_PATH = "assets/shaders/skybox/fragment.glsl";

    private VertexArray vao;
    private VertexBuffer vbo;
    private int textureID;
    private ShaderProgram shader;

    private float[] skyboxVertices = {
            // positions
            -1.0f,  1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,

            -1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,

            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            -1.0f, -1.0f,  1.0f,
            -1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,

            -1.0f,  1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f, -1.0f,

            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f,  1.0f
    };

    public Skybox(String[] faces) throws Exception {
        vao = new VertexArray();
        vao.bind();

        vbo = new VertexBuffer(VertexBuffer.Type.POSITION, skyboxVertices);
        vbo.bind();

        textureID = glGenTextures();
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);

        if (faces.length != FACE_COUNT) {
            throw new Exception("Faces length must be 6!");
        }

        for (int i = 0; i < FACE_COUNT; i++) {
            int[] width = new int[1];
            int[] height = new int[1];
            int[] channels = new int[1];

            STBImage.stbi_set_flip_vertically_on_load(false);
            ByteBuffer buffer = STBImage.stbi_load(faces[i], width, height, channels, 0);

            if (buffer == null) {
                throw new Exception("Failed to load image from stbi_load");
            }

            int channelCount = GL_RGB;
            if (channels[0] == 4) {
                channelCount = GL_RGBA;
            }

            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, channelCount, width[0], height[0], 0, channelCount, GL_UNSIGNED_BYTE, buffer);
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

        shader = new ShaderProgram(
                new Shader(SKYBOX_VERTEX_SHADER_PATH, GL_VERTEX_SHADER),
                new Shader(SKYBOX_FRAGMENT_SHADER_PATH, GL_FRAGMENT_SHADER)
        );
        glActiveTexture(GL_TEXTURE0);
    }

    public void render(Scene scene) {
        Camera camera = scene.getCamera();
        glDepthMask(false);
        glDepthFunc(GL_LEQUAL);
        shader.bind();

        shader.setUniformInt("u_skybox", 2);
        shader.setUniformMat4f("u_projection", camera.getProjection());
        shader.setUniformMat4f("u_model", new Matrix4f().scale(200.0f, 200.0f, 200.0f));
        // Remove translations
        shader.setUniformMat4f("u_view", new Matrix4f(new Matrix3f(camera.getView())));

        vao.bind();

        glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);
        glDrawArrays(GL_TRIANGLES, 0, 36);
        glDepthFunc(GL_LESS);
        glDepthMask(true);
    }
}
