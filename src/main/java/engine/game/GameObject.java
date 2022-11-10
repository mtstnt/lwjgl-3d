package engine.game;

import engine.Scene;
import engine.graphics.Mesh;

public abstract class GameObject {
    protected Mesh mesh;

    public void render(Scene scene) {
        this.mesh.render(scene.getCamera());
    }

    public Mesh getMesh() {
        return mesh;
    }
}
