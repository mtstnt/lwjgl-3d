package engine.graphics;

import engine.Scene;

import java.util.List;

public class Model {

    protected List<MeshObject> meshObjects;

    public Model(List<MeshObject> meshObjects) {
        this.meshObjects = meshObjects;
    }

    public void render(Scene scene) {
        for (MeshObject meshObject : meshObjects) {
            meshObject.render(scene);
        }
    }

}
