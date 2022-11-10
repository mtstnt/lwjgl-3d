package project.loader3;

import engine.Camera;
import engine.Engine;
import engine.Scene;
import engine.game.GameObject;
import engine.graphics.Mesh;
import engine.graphics.shaders.ShaderProgram;
import engine.graphics.shapes.Ellipsoid;
import engine.window.Window;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL40.GL_TRIANGLE_STRIP;

public class Main extends Scene {

    private static final float CAMERA_POS_STEP = 0.05f;
    private static final float MOUSE_SENSITIVITY = 0.1f;

    private Train train;
    private Ellipsoid ellipsoid1, ellipsoid2;

    @Override
    public void start() throws Exception {
        this.camera = new Camera();
        this.camera.usePerspective(
                (float) Math.toRadians(45.f),
                640.f / 480.f,
                0.01f,
                100.f
        );

        train = new Train();
        train.setPosition(new Vector3f(0, 0, -20));

        ellipsoid1 = new Ellipsoid(
                new Vector3f(-10.f, 0, 0),
                new Vector3f(3), 24, 24,
                ShaderProgram.createDefault());

        ellipsoid2 = new Ellipsoid(
                new Vector3f(10.f, 0, 0),
                new Vector3f(3), 24, 24,
                ShaderProgram.createDefault());

        camera.getView().translate(0, 0, -20);

        this.gameObjects.addAll(List.of(train, ellipsoid1, ellipsoid2));
    }

    @Override
    public void update(float delta) {
        train
                .getMesh()
                .getTransform()
                .rotateY((float) Math.toRadians(30.f) * delta);

        ellipsoid1
                .getMesh()
                .getTransform()
                .rotateLocalY((float) Math.toRadians(30.f) * delta);

        ellipsoid2
                .getMesh()
                .getTransform()
                .rotateLocalY((float) Math.toRadians(30.f) * delta);

        for (GameObject object : this.gameObjects) {
            object.render(this);
        }
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
        } catch (IllegalStateException e) {
            System.out.println("OPENGL ERROR:");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
