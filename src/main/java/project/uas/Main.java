package project.uas;

import engine.*;
import engine.graphics.Model;
import engine.window.Window;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.assimp.Assimp.*;

public class Main extends Scene {
    private Model model;
    private Skybox skybox;
    private boolean isActive = true;

    @Override
    public void start() throws Exception {
        camera = Camera.createPerspective(
                (float) Math.toRadians(45.f),
                640.f / 480.f,
                0.01f,
                100.f
        );

        camera.move(new Vector3f(0, 0, 5));

        model = AssimpLoader.loadModel(
                "assets/models/jug_01_2k/jug_01_2k.obj",
                "assets/models/jug_01_2k",
                aiProcess_Triangulate | aiProcess_GenSmoothNormals | aiProcess_FixInfacingNormals
        );


        this.lightSources.add(
                new LightSource(
                        new Vector3f(0.f, 5.f, 5.f),
                        new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)
                )
        );

        this.skybox = new Skybox(new String[]{
                "assets/skybox/yokohama/posx.jpg",
                "assets/skybox/yokohama/negx.jpg",
                "assets/skybox/yokohama/posy.jpg",
                "assets/skybox/yokohama/negy.jpg",
                "assets/skybox/yokohama/posz.jpg",
                "assets/skybox/yokohama/negz.jpg",
        });

        input.setCursorMode(GLFW.GLFW_CURSOR_DISABLED);

        input.handleKeyEvent(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_PRESS, () -> {
            isActive = !isActive;

            if (isActive) {
                input.setCursorMode(GLFW.GLFW_CURSOR_DISABLED);
            } else {
                input.setCursorMode(GLFW.GLFW_CURSOR_NORMAL);
            }
        });

        input.handleKeyEvent(GLFW.GLFW_KEY_R, GLFW.GLFW_PRESS, () -> {
            Vector3f pos = camera.getPosition();
            camera.move(new Vector3f(-1 * pos.x, -1 * pos.y,-1 * pos.z));
        });

        input.handleCursorChange((newX, newY) -> {
            if (!isActive)
                return;

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

        model.render(this);

        skybox.render(this);
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
        } catch (IllegalStateException e) {
            System.out.println("OPENGL ERROR:");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}