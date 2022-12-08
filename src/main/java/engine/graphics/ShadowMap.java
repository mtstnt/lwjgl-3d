package engine.graphics;

import engine.Scene;
import engine.graphics.shaders.Shader;
import engine.graphics.shaders.ShaderProgram;
import engine.interfaces.Renderable;
import engine.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL40.*;

public class ShadowMap {

    private final int depthMapFbo;
    private final int depthCubeMap;
    private final ShaderProgram depthShader;
    private final Window window;

    private final int SHADOW_WIDTH = 1024;
    private final int SHADOW_HEIGHT = 1024;

    public static final float NEAR_PLANE = 1.f;
    public static final float FAR_PLANE = 25.f;

    public ShadowMap(Window window) throws Exception {
        this.window = window;

        depthMapFbo = glGenFramebuffers();
        depthCubeMap = glGenTextures();
        depthShader = new ShaderProgram(
                new Shader("assets/shaders/shadow/vertex.glsl", GL_VERTEX_SHADER),
                new Shader("assets/shaders/shadow/fragment.glsl", GL_FRAGMENT_SHADER),
                new Shader("assets/shaders/shadow/geometry.glsl", GL_GEOMETRY_SHADER)
        );

        glBindTexture(GL_TEXTURE_CUBE_MAP, depthCubeMap);

        for (int i = 0; i < 6; ++i) {
            glTexImage2D(
                    GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0,
                    GL_DEPTH_COMPONENT, SHADOW_WIDTH, SHADOW_HEIGHT, 0,
                    GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null
            );
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFbo);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthCubeMap, 0);
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void renderShadows(Scene scene, List<Renderable> shadowCasters) {
        Matrix4f shadowProj = new Matrix4f().perspective(
                (float) Math.toRadians(90),
                (float) SHADOW_WIDTH / (float) SHADOW_HEIGHT,
                NEAR_PLANE, FAR_PLANE
        );

        var lightSources = scene.getLightSources();
        if (lightSources.size() == 0) {
            return;
        }

        Vector3f lightPos = lightSources.get(0).getPosition();

        // All cubemap directions.
        ArrayList<Matrix4f> shadowTransforms = new ArrayList<>(List.of(
                new Matrix4f(shadowProj).mul(new Matrix4f().lookAt(
                        lightPos,
                        new Vector3f(lightPos.x + 1, lightPos.y + 0, lightPos.z + 0),
                        new Vector3f(0, -1, 0))),
                new Matrix4f(shadowProj).mul(new Matrix4f().lookAt(
                        lightPos,
                        new Vector3f(lightPos.x - 1, lightPos.y + 0, lightPos.z + 0),
                        new Vector3f(0, -1, 0))),
                new Matrix4f(shadowProj).mul(new Matrix4f().lookAt(
                        lightPos,
                        new Vector3f(lightPos.x + 0, lightPos.y + 1, lightPos.z + 0),
                        new Vector3f(0, 0, 1))),
                new Matrix4f(shadowProj).mul(new Matrix4f().lookAt(
                        lightPos,
                        new Vector3f(lightPos.x + 0, lightPos.y - 1, lightPos.z + 0),
                        new Vector3f(0, 0, -1))),
                new Matrix4f(shadowProj).mul(new Matrix4f().lookAt(
                        lightPos,
                        new Vector3f(lightPos.x + 0, lightPos.y + 0, lightPos.z + 1),
                        new Vector3f(0, -1, 0))),
                new Matrix4f(shadowProj).mul(new Matrix4f().lookAt(
                        lightPos,
                        new Vector3f(lightPos.x + 0, lightPos.y + 0, lightPos.z - 1),
                        new Vector3f(0, -1, 0)))
        ));

        glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFbo);
        glClear(GL_DEPTH_BUFFER_BIT);

        depthShader.bind();
        for (int i = 0; i < 6; ++i)
            depthShader.setUniformMat4f("u_shadowMatrices[" + i + "]", shadowTransforms.get(i));

        depthShader.setUniformFloat("u_farPlane", FAR_PLANE);
        depthShader.setUniformVec3f("u_lightPos", lightPos);

        shadowCasters.forEach(item -> item.render(scene, depthShader));

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, window.getWidth(), window.getHeight());

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_CUBE_MAP, depthCubeMap);

        glActiveTexture(GL_TEXTURE0);
    }
}
