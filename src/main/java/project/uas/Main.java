package project.uas;

import engine.*;
import engine.graphics.Mesh;
import engine.graphics.Model;
import engine.graphics.Texture;
import engine.graphics.shaders.Shader;
import engine.graphics.shaders.ShaderProgram;
import engine.graphics.shapes.Plane2D;
import engine.interfaces.Renderable;
import engine.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import project.uas.objects.LightBox;
import project.uas.objects.Room;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class Main extends Scene {
    private Skybox skybox;
    private boolean isActive = true;

    private List<Renderable> renderObjects;

    @Override
    public void start() throws Exception {
        renderObjects = new ArrayList<>();

        camera = Camera.createPerspective(
                (float) Math.toRadians(45.f),
                640.f / 480.f,
                0.01f,
                10000.f
        );

        camera.move(new Vector3f(0, 2, 0));

        Model sofa = AssimpLoader.loadModel(
                "assets/models/sofa_01_2k/sofa_01_2k.obj",
                "assets/models/sofa_01_2k/",
                aiProcess_Triangulate | aiProcess_GenSmoothNormals | aiProcess_FixInfacingNormals
        );
        sofa.mulTransform(
                new Matrix4f()
                        .translate(0f, 0.f, 1.5f)
        );

        Model bed = AssimpLoader.loadModel(
                "assets/models/GothicBed_01_2k/GothicBed_01_2k.obj",
                "assets/models/GothicBed_01_2k/",
                aiProcess_Triangulate | aiProcess_GenSmoothNormals | aiProcess_FixInfacingNormals
        );
        bed.mulTransform(
                new Matrix4f()
                        .translate(1.9f, 0.f, 2.f)
                        .rotateY((float) Math.toRadians(270.f))
        );

        Model jug = AssimpLoader.loadModel(
                "assets/models/jug_01_2k/jug_01_2k.mtl.obj",
                "assets/models/jug_01_2k/",
                aiProcess_Triangulate | aiProcess_GenSmoothNormals | aiProcess_FixInfacingNormals
        );
        jug.mulTransform(
                new Matrix4f()
                        .translate(0.0f, 0.8f, 2.5f)
        );
        Model tv = AssimpLoader.loadModel(
                "assets/models/Television_01_2k/Television_01_2k.obj",
                "assets/models/Television_01_2k",
                aiProcess_Triangulate | aiProcess_GenSmoothNormals | aiProcess_FixInfacingNormals
        );
        tv.mulTransform(
                new Matrix4f()
                        .translate(0.0f, 0.8f, 2.5f)
                        .rotateY((float) Math.toRadians(180f))
        );
        Model table = AssimpLoader.loadModel(
                "assets/models/wooden_table_02_2k/wooden_table_02_2k.obj",
                "assets/models/wooden_table_02_2k",
                aiProcess_Triangulate | aiProcess_GenSmoothNormals | aiProcess_FixInfacingNormals
        );
        table.mulTransform(
                new Matrix4f()
                        .translate(0.0f, 0.0f, 2.5f)
        );

        LightBox light = new LightBox();
        light.getTransform()
                .translate(0, 2.8f, 0)
                .scale(0.1f);
        lightSources.add(
                new LightSource(
                        new Vector3f(0, 2.8f, 0),
                        new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)
                )
        );

        skybox = new Skybox(new String[]{
                "assets/skybox/sky/posx.png",
                "assets/skybox/sky/negx.png",
                "assets/skybox/sky/posy.png",
                "assets/skybox/sky/negy.png",
                "assets/skybox/sky/posz.png",
                "assets/skybox/sky/negz.png",
        });

        Room room = new Room(new Vector3f(0, 0, 0), 3.f, 3.f, 3f);

        renderObjects.add(light);
        renderObjects.add(room);

        renderObjects.add(jug);
        renderObjects.add(sofa);
        renderObjects.add(bed);
        renderObjects.add(table);
        renderObjects.add(tv);

        applyInputHandlers();
    }

    @Override
    public void update(float delta) {
        handleCameraMovements(delta);

        camera.update();
        renderObjects.forEach(obj -> obj.render(this));
        skybox.render(this);
    }

    private void applyInputHandlers() {
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

    private void handleCameraMovements(float delta) {
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
    }

    public static void main(String[] args) {
        try {
            Window window = new Window(1280, 720, "Testing Window");
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