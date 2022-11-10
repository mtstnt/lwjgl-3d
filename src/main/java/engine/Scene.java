package engine;

import engine.game.GameObject;
import engine.window.Input;
import engine.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Window window;
    protected Input input;
    protected Camera camera;

    protected List<GameObject> gameObjects;

    public Scene() {
        this.gameObjects = new ArrayList<>();
        this.window = null;
        this.input = null;
    }

    public abstract void start() throws Exception;
    public abstract void update(float delta) throws Exception;
    public abstract void dispose() throws Exception;

    public void setWindow(Window window) {
        this.window = window;
        this.input = window.getInputHandler();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }
}
