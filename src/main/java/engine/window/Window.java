package engine.window;

import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL40.*;

public class Window {

    private long handle = 0;
    private int width, height;
    private String title;

    private Input inputHandler;

    public Window(int width, int height, String title) throws Exception {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void initDefault() {
        handle = glfwCreateWindow(width, height, title, 0, 0);

        if (handle == 0) {
            throw new IllegalStateException("Failed to create window");
        }

        glfwDefaultWindowHints();

        inputHandler = new Input(this);
    }

    public void setHint(int hintType, int value) {
        glfwWindowHint(hintType, value);
    }

    public boolean isCreated() {
        return handle > 0;
    }

    // Returns cursor position as float.
    public Vector2f getCursorPosition() {
        double[] xPos = new double[1];
        double[] yPos = new double[1];
        glfwGetCursorPos(handle, xPos, yPos);

        return new Vector2f((float)xPos[0], (float)yPos[0]);
    }

    public void setAsCurrentContext() {
        glfwMakeContextCurrent(handle);
    }

    public void enableDefaultHandlers() {
        glfwSetWindowSizeCallback(handle, (w, width, height) -> glViewport(0, 0, width, height));
    }

    public void setWidth(int width) {
        this.width = width;
        updateWindowSettings();
    }

    public void setHeight(int height) {
        this.height = height;
        updateWindowSettings();
    }

    public void setTitle(String title) {
        this.title = title;
        updateWindowSettings();
    }

    public void show() {
        glfwShowWindow(handle);
    }

    public void hide() {
        glfwHideWindow(handle);
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getHandle() {
        return handle;
    }

    public Input getInputHandler() {
        return inputHandler;
    }

    private void updateWindowSettings() {
        glfwSetWindowSize(handle, this.width, this.height);
        glfwSetWindowTitle(handle, title);
    }
}
