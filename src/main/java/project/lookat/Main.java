package project.lookat;

import engine.Camera;
import engine.Engine;
import engine.Scene;
import engine.graphics.Mesh;
import engine.graphics.shaders.ShaderProgram;
import engine.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL40.GL_TRIANGLE_STRIP;

public class Main extends Scene {
    private Mesh mesh;

    @Override
    public void start() throws Exception {
        camera = Camera.createPerspective(
                (float) Math.toRadians(45.f),
                640.f / 480.f,
                0.01f,
                100.f
        );

        camera.move(new Vector3f(0, 0, 5));

        mesh = Mesh.fromObj("assets/models/toy_train.obj");
        mesh.getTransform().translate(0, 0, 0);
        mesh.setShader(ShaderProgram.createDefault());

        input.handleCursorChange((newX, newY) -> {
            Vector2d cursorOffset = input.getCursorChangeOffset();
            camera.rotate(new Vector3f((float) cursorOffset.x, (float) cursorOffset.y, 0));
        });

        input.handleScrollChange((newX, newY) -> camera.zoom((float) newY * 0.1f));
    }

    @Override
    public void update(float delta) {
        float speed = 10f;
        float radius = 15f;
        float camX = (float) Math.sin(Math.toRadians(GLFW.glfwGetTime() * speed)) * radius;
        float camZ = (float) Math.cos(Math.toRadians(GLFW.glfwGetTime() * speed)) * radius;

        camera.setViewMatrix(new Matrix4f().lookAt(
                new Vector3f(camX, 0, camZ),
                new Vector3f().zero(),
                new Vector3f(0, 1, 0)
        ));

//        camera.update();

        mesh.render(this, GL_TRIANGLES);
    }

    @Override
    public void dispose() {

    }

    public static void main(String[] args) {
        try {
            Window window = new Window(640, 480, "Testing Window");
            Engine engine = new Engine(window);
            Main mainScene = new Main();
            engine.pushScene(mainScene);
            engine.run();
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
