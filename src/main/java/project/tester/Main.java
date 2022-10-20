package project.tester;

import engine.Camera;
import engine.Engine;
import engine.Scene;
import engine.graphics.Mesh;
import engine.graphics.shaders.ShaderProgram;
import engine.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Main extends Scene {
    private Mesh mesh;
    private ShaderProgram sp;

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
        input.handleKeyEvent(GLFW_KEY_A, GLFW_PRESS, () -> mesh.getTransform().translate(new Vector3f(1, 0, 0)));

        sp = ShaderProgram.fromPath(
                "assets/shaders/default/vertex.glsl",
                "assets/shaders/default/fragment.glsl"
        );

        mesh = new Mesh();
        mesh.setVertices(new float[] {
                -0.5f, -0.5f, -0.5f,
                 0.5f, -0.5f, -0.5f,
                 0.5f,  0.5f, -0.5f,
                 0.5f,  0.5f, -0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f,  0.5f,
                 0.5f, -0.5f,  0.5f,
                 0.5f,  0.5f,  0.5f,
                 0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                 0.5f,  0.5f,  0.5f,
                 0.5f,  0.5f, -0.5f,
                 0.5f, -0.5f, -0.5f,
                 0.5f, -0.5f, -0.5f,
                 0.5f, -0.5f,  0.5f,
                 0.5f,  0.5f,  0.5f,
                -0.5f, -0.5f, -0.5f,
                 0.5f, -0.5f, -0.5f,
                 0.5f, -0.5f,  0.5f,
                 0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f,  0.5f, -0.5f,
                 0.5f,  0.5f, -0.5f,
                 0.5f,  0.5f,  0.5f,
                 0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f
        });

//        mesh.getTransform().translate(new Vector3f(0, 0, 0));
        mesh.setShader(sp);
    }

    @Override
    public void update(float delta) {
        mesh.getTransform()
            .rotateX((float) Math.toRadians(40) * delta)
            .rotateY((float) Math.toRadians(40) * delta)
            .rotateZ((float) Math.toRadians(40) * delta);

        mesh.render(camera);
    }

    @Override
    public void dispose() {

    }

    public static void main(String[] args) {
        try {
            Window window = new Window(640, 480, "Testing Window");
            Engine engine = new Engine(window);
            Main mainScene = new Main();
            engine.run(mainScene);
        }
        catch (IllegalStateException e) {
            System.out.println("OPENGL ERROR:");
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
