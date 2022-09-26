package engine.window;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    public enum ButtonState {
        RELEASED,
        PRESSED
    }

    public enum ButtonType {
        MOUSE_LEFT,
        MOUSE_RIGHT
    }

    private Window window;
    public HashMap<List<Integer>, Runnable> mouseEventHandler;
    private HashMap<List<Integer>, Runnable> keyEventHandler;

    public Input(Window window) {
        this.window = window;
        this.mouseEventHandler = new HashMap<>();
        this.keyEventHandler = new HashMap<>();
    }

    private void _update() {
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

    public ButtonState isKeyPressed(int key) {
        int result = glfwGetKey(window.getHandle(), key);
        switch (result) {
            case 1: return ButtonState.PRESSED;

            default:
            case 0: return ButtonState.RELEASED;
        }
    }

    public void handleButtonEvent(int button, int type, Runnable fn) {
        this.mouseEventHandler.put(List.of(button, type), fn);
        _update();
    }

    public void handleKeyEvent(int key, int type, Runnable fn) {
        this.keyEventHandler.put(List.of(key, type), fn);
        _update();
    }

}
