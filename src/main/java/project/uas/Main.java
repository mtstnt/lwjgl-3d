package project.uas;

import engine.*;
import engine.graphics.Mesh;
import engine.graphics.Model;
import engine.graphics.ShadowMap;
import engine.graphics.shaders.ShaderProgram;
import engine.interfaces.Renderable;
import engine.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import project.uas.objects.Room;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.opengl.GL40.*;

public class Main extends Scene {
    private Skybox skybox;
    private boolean isActive = true;

    private List<Renderable> renderObjects;
    private List<Renderable> shadowCasters;
    private ShadowMap shadowMap;

    private LightSource lightSource;

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
        Model sofa = Model.load("assets/models/sofa_01_2k/sofa_01_2k.obj");
        sofa.mulTransform(
                new Matrix4f()
                        .translate(1f, 0.f, 0f)
                        .rotateY((float) Math.toRadians(180.f))
        );
        renderObjects.add(sofa);

        Model bed = Model.load("assets/models/GothicBed_01_2k/GothicBed_01_2k.obj");
        bed.mulTransform(
                new Matrix4f()
                        .translate(0.9f, 0.f, 1.2f)
                        .rotateY((float) Math.toRadians(270.f))
        );
        renderObjects.add(bed);

        Mesh tv = Mesh.fromObj("assets/models/tv/tv.obj");
        tv.setShader(ShaderProgram.fromPath(
            "assets/shaders/lighting/vertex.glsl",
            "assets/shaders/lighting/fragment.glsl"
        ));
        tv.getTransform().mul(new Matrix4f()
            .translate(-1.93f, 1.5f, 1.2f)
            .rotateY((float) Math.toRadians(90))
            .scale(0.6f));
        renderObjects.add(tv);

        Model console = Model.load("assets/models/ClassicConsole_01_2k/ClassicConsole_01_2k.obj");
        console.mulTransform(new Matrix4f()
            .translate(-1.65f, 0f, 1.2f)
            .rotateY((float) Math.toRadians(90)));
        renderObjects.add(console);

        Model table = Model.load("assets/models/wooden_table_02_2k/wooden_table_02_2k.obj");
        table.mulTransform(new Matrix4f().translate(0.3f, 0.0f, -1.6f));
        renderObjects.add(table);

        Model wardrobe = Model.load("assets/models/old_wardrobe/old_wardrobe.obj");
        wardrobe.mulTransform(new Matrix4f()
            .translate(1.45f, 0, -1.6f)
            .rotateY((float) Math.toRadians(90.f)));
        renderObjects.add(wardrobe);

        Model ac = Model.load("assets/models/air_conditioner/air_conditioner.obj");
        ac.mulTransform(new Matrix4f()
            .translate(0.0f, 2f, 1.78f)
            .rotateY((float) Math.toRadians(180f)));
        renderObjects.add(ac);

        Model lamp = Model.load("assets/models/modern_ceiling_lamp_01_2k/modern_ceiling_lamp_01_2k.obj");
        lamp.mulTransform(new Matrix4f().translate(0, 1.8f, 0));
        renderObjects.add(lamp);

        Model computer = Model.load("assets/models/office_computer/office_computer.obj");
        computer.mulTransform(new Matrix4f().translate(-1.7f, 0.1f, -1.1f));
        renderObjects.add(computer);

        lightSource = new LightSource(
            new Vector3f(0, 2f, 0.f),
            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)
        );
        lightSources.add(lightSource);

        skybox = new Skybox(new String[]{
            "assets/skybox/sky/posx.png",
            "assets/skybox/sky/negx.png",
            "assets/skybox/sky/posy.png",
            "assets/skybox/sky/negy.png",
            "assets/skybox/sky/posz.png",
            "assets/skybox/sky/negz.png",
        });

        Room room = new Room(new Vector3f(0, 0, 0), 2.f, 3.f, 2f);
        renderObjects.add(room);

        applyInputHandlers();

        shadowMap = new ShadowMap(getWindow());
        shadowCasters = renderObjects;
    }

    @Override
    public void update(float delta) {
        handleCameraMovements(delta);
        shadowMap.renderShadows(this, shadowCasters);
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
            camera.moveAlongDirection(delta * 5);
        } else if (input.isKeyPressed(GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            camera.moveAlongDirection(delta * -5);
        }

        if (input.isKeyPressed(GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            camera.moveAlongCrossDirection(delta * -5);
        } else if (input.isKeyPressed(GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            camera.moveAlongCrossDirection(delta * 5);
        }
    }

    public static void main(String[] args) {
        try {
            Window window = new Window(1920, 1080, "Testing Window");
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