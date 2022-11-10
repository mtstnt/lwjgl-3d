package engine.window;

import engine.interfaces.MouseCursorCallbackFn;
import org.joml.Vector2d;

import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private Window window;
    private HashMap<List<Integer>, Runnable> mouseEventHandler;
    private HashMap<List<Integer>, Runnable> keyEventHandler;

    private Vector2d previousMousePosition;
    private boolean isMouseMoved = false;

    public Input(Window window) {
        this.window = window;
        this.mouseEventHandler = new HashMap<>();
        this.keyEventHandler = new HashMap<>();
        this.previousMousePosition = new Vector2d();
    }

    public void setCursorMode(int cursorMode) {
        glfwSetInputMode(window.getHandle(), GLFW_CURSOR, cursorMode);
    }

    private void resetCallbacks() {
        glfwSetMouseButtonCallback(window.getHandle(), (w, btn, action, mods) -> {
            List<Integer> key = List.of(btn, action);
            if (this.mouseEventHandler.containsKey(key)) {
                Runnable fnHandler = this.mouseEventHandler.get(key);
                fnHandler.run();
            }
        });

        glfwSetKeyCallback(window.getHandle(), (w, keycode, scancode, action, mods) -> {
            List<Integer> key = List.of(keycode, action);
            if (this.keyEventHandler.containsKey(key)) {
                Runnable fnHandler = this.keyEventHandler.get(key);
                fnHandler.run();
            }
        });
    }

    public int isKeyPressed(int key) {
        return glfwGetKey(window.getHandle(), key);
    }

    public void handleButtonEvent(int button, int type, Runnable fn) {
        this.mouseEventHandler.put(List.of(button, type), fn);
        resetCallbacks();
    }

    public void handleKeyEvent(int key, int type, Runnable fn) {
        this.keyEventHandler.put(List.of(key, type), fn);
        resetCallbacks();
    }

    public void handleCursorChange(MouseCursorCallbackFn fn) {
        glfwSetCursorPosCallback(window.getHandle(), (win, x, y) -> fn.run(x, y));
    }

    public void handleScrollChange(MouseCursorCallbackFn fn) {
        glfwSetScrollCallback(window.getHandle(), (win, x, y) -> fn.run(x, y));
    }

    public Vector2d getCursorChangeOffset() {
        Vector2d cursorPos = getCursorPosition();
        if (!isMouseMoved) {
            previousMousePosition.x = cursorPos.x;
            previousMousePosition.y = cursorPos.y;
            isMouseMoved = true;
        }

        Vector2d v = new Vector2d(
                (float) (cursorPos.x - previousMousePosition.x),
                (float) (previousMousePosition.y - cursorPos.y)
        );

        previousMousePosition.x = cursorPos.x;
        previousMousePosition.y = cursorPos.y;

        return v;
    }

    private Vector2d getCursorPosition() {
        double[] xPos = new double[1];
        double[] yPos = new double[1];

        glfwGetCursorPos(window.getHandle(), xPos, yPos);

        return new Vector2d(xPos[0], yPos[0]);
    }
}
