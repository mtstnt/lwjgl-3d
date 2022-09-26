package project.tester;

import engine.Camera;
import engine.Scene;
import engine.graphics.Mesh;
import engine.graphics.shaders.ShaderProgram;

import static org.lwjgl.glfw.GLFW.*;

public class MainScene extends Scene {

    Mesh mesh;
    ShaderProgram sp;
    @Override
    public void start() throws Exception {
        this.camera = new Camera();
        this.camera.usePerspective(
                (float) Math.toRadians(45.f),
                640.f / 480.f,
                0.01f,
                100.f
        );
//        System.out.println("Projection dari main: " + this.camera.getProjection().toString());

        input.handleButtonEvent(GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS, () -> System.out.println("Mouse left is pressed"));
        input.handleButtonEvent(GLFW_MOUSE_BUTTON_RIGHT, GLFW_PRESS, () -> System.out.println("Mouse right is pressed"));
        input.handleKeyEvent(GLFW_KEY_A, GLFW_PRESS, () -> System.out.println("Key A is pressed"));

        sp = ShaderProgram.fromPath(
                "assets/shaders/default/vertex.glsl",
                "assets/shaders/default/fragment.glsl"
        );

        mesh = new Mesh();
        mesh.setVertices(new float[] {
                -1.0f,-1.0f,-1.0f, // triangle 1 : begin
                -1.0f,-1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f, // triangle 1 : end
                1.0f, 1.0f,-1.0f, // triangle 2 : begin
                -1.0f,-1.0f,-1.0f,
                -1.0f, 1.0f,-1.0f, // triangle 2 : end
                1.0f,-1.0f, 1.0f,
                -1.0f,-1.0f,-1.0f,
                1.0f,-1.0f,-1.0f,

                1.0f, 1.0f,-1.0f,
                1.0f,-1.0f,-1.0f,
                -1.0f,-1.0f,-1.0f,

                -1.0f,-1.0f,-1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f,-1.0f,

                1.0f,-1.0f, 1.0f,
                -1.0f,-1.0f, 1.0f,
                -1.0f,-1.0f,-1.0f,

                -1.0f, 1.0f, 1.0f,
                -1.0f,-1.0f, 1.0f,
                1.0f,-1.0f, 1.0f,

                1.0f, 1.0f, 1.0f,
                1.0f,-1.0f,-1.0f,
                1.0f, 1.0f,-1.0f,

                1.0f,-1.0f,-1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f,-1.0f, 1.0f,

                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f,-1.0f,
                -1.0f, 1.0f,-1.0f,

                1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f,-1.0f,
                -1.0f, 1.0f, 1.0f,

                1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,
                1.0f,-1.0f, 1.0f
        });
        mesh.setShader(sp);
    }

    @Override
    public void update(float delta) {
        mesh.render(camera);
    }

    @Override
    public void dispose() {

    }
}
