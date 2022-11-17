package project.loader;

import engine.Camera;
import engine.Engine;
import engine.Scene;
import engine.graphics.Mesh;
import engine.graphics.shaders.ShaderProgram;
import engine.window.Window;

import static org.lwjgl.opengl.GL40.*;

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

        sp = ShaderProgram.fromPath(
                "assets/shaders/default/vertex.glsl",
                "assets/shaders/default/fragment.glsl"
        );

        mesh = Mesh.fromObj("assets/models/box.obj");
        mesh.getTransform().translate(0, 0, -5);
        mesh.setShader(sp);
    }

    @Override
    public void update(float delta) {
        mesh.getTransform()
            .rotateX((float) Math.toRadians(40) * delta);

        mesh.render(this, GL_TRIANGLE_STRIP);
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
