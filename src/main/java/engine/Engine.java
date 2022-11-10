package engine;

import engine.window.Input;
import engine.window.Window;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Stack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL40.*;

public class Engine {

    private final Window window;

    // TODO: Separate this into other class.
    private Stack<Scene> scenes;

    public Engine(Window window) throws Exception {
        this.window = window;
        this.scenes = new Stack<>();

        // Initalize GLFW
        if (! glfwInit()) {
            throw new IllegalStateException("GLFW Initialization failed!");
        }

        // Error printer
        GLFWErrorCallback.createPrint().set();

        window.initDefault();
        window.setHint(GLFW_VISIBLE, GLFW_TRUE);
        window.setHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Check if window is created
        if (! window.isCreated()) {
            throw new IllegalStateException("Window is not created while initializing OpenGL");
        }
        window.setAsCurrentContext();

        // Initialize OpenGL functions.
        GL.createCapabilities();

        // OpenGL enable
        glEnable(GL_DEPTH_TEST);

        // Should I render wireframe?
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        glClearColor(0f, 0f, 0f, 1f);

        // Set default resize and viewport update.
        window.enableDefaultHandlers();
    }

    public void pushScene(Scene scene) throws Exception {
        scenes.push(scene);
        scene.setWindow(window);
        scene.start();
    }

    public void run(Scene scene) throws Exception {
        this.pushScene(scene);
        this.run();
    }

    public void run() throws Exception {
        long windowHandle = window.getHandle();
        float delta;
        double previousTime = glfwGetTime();

        Scene scene = this.scenes.firstElement();

        while (! glfwWindowShouldClose(windowHandle)) {
            // Calculate delta
            double currentTime = glfwGetTime();
            delta = (float) (currentTime - previousTime);
            previousTime = currentTime;

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfwPollEvents();

            // Run scene operations
            scene.update(delta);
            // Done?

            glfwSwapBuffers(windowHandle);
        }

        scene.dispose();
    }
}
