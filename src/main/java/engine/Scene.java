package engine;

import engine.window.Input;
import engine.window.Window;

public abstract class Scene {
    protected Window window;
    protected Input input;
    protected Camera camera;

    public abstract void start() throws Exception;
    public abstract void update(float delta) throws Exception;
    public abstract void dispose() throws Exception;

    public void setWindow(Window window) {
        this.window = window;
        this.input = window.getInputHandler();
    }

    public void useCamera(Camera camera) {
        this.camera = camera;
    }
}
