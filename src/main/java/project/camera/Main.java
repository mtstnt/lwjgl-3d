package project.camera;

import engine.Camera;
import engine.Engine;
import engine.Scene;
import engine.graphics.Mesh;
import engine.graphics.shaders.ShaderProgram;
import engine.window.Window;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

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

        mesh = Mesh.fromObj("assets/models/box.obj");
        mesh.getTransform().translate(0, 0, -5);
        mesh.setShader(ShaderProgram.createDefault());

        input.setCursorMode(GLFW.GLFW_CURSOR_DISABLED);

        input.handleKeyEvent(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_PRESS, () -> input.setCursorMode(GLFW.GLFW_CURSOR_NORMAL));

        input.handleCursorChange((newX, newY) -> {
            Vector2d cursorOffset = input.getCursorChangeOffset();
            camera.rotate(new Vector3f((float) cursorOffset.x, (float) cursorOffset.y, 0));
        });

        input.handleScrollChange((newX, newY) -> camera.zoom((float) newY * 0.1f));
    }

    @Override
    public void update(float delta) {
        if (input.isKeyPressed(GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            camera.moveAlongDirection(delta * 10.f);
        } else if (input.isKeyPressed(GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            camera.moveAlongDirection(delta * -10.f);
        }

        if (input.isKeyPressed(GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            camera.moveAlongCrossDirection(delta * -10.f);
        } else if (input.isKeyPressed(GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            camera.moveAlongCrossDirection(delta * 10.f);
        }

        camera.update();

        mesh.getTransform().rotateY((float) Math.toRadians(40) * delta);
        mesh.render(this, GL_TRIANGLE_STRIP);
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
