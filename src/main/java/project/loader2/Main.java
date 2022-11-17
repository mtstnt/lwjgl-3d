package project.loader2;

import engine.Camera;
import engine.Engine;
import engine.Scene;
import engine.graphics.Mesh;
import engine.graphics.shaders.ShaderProgram;
import engine.window.Window;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.opengl.GL40.GL_TRIANGLE_STRIP;

public class Main extends Scene {
    private Mesh mesh;
    private ShaderProgram sp;

    private Vector3f cameraInc;
    private static final float CAMERA_POS_STEP = 0.05f;
    private static final float MOUSE_SENSITIVITY = 0.1f;

    @Override
    public void start() throws Exception {
        this.camera = new Camera();
        this.camera.usePerspective(
                (float) Math.toRadians(45.f),
                640.f / 480.f,
                0.01f,
                100.f
        );
        this.cameraInc = new Vector3f();

        sp = ShaderProgram.fromPath(
                "assets/shaders/default/vertex.glsl",
                "assets/shaders/default/fragment.glsl"
        );

        mesh = Mesh.fromObj("assets/models/box.obj");
        mesh.getTransform().translate(3.5f, 0, -10);
        camera.getView().translate(0, 0, -20);
        mesh.setShader(sp);
    }

    @Override
    public void update(float delta) {
        cameraInc.zero();

        if (GLFW.glfwGetKey(window.getHandle(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            cameraInc.x = -1;
        } else if (GLFW.glfwGetKey(window.getHandle(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            cameraInc.x = 1;
        }

        if (GLFW.glfwGetKey(window.getHandle(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            cameraInc.z = -1;
        } else if (GLFW.glfwGetKey(window.getHandle(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            cameraInc.z = 1;
        }

        if (GLFW.glfwGetKey(window.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
            cameraInc.y = -1;
        } else if (GLFW.glfwGetKey(window.getHandle(), GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            cameraInc.y = 1;
        }

        mesh.getTransform()
                .rotateLocalY((float) Math.toRadians(40) * delta);

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
