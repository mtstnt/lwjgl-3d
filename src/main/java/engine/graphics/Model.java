package engine.graphics;

import engine.Scene;
import engine.interfaces.Renderable;
import org.joml.Matrix4f;

import java.util.List;

public class Model implements Renderable {

    protected List<MeshObject> meshObjects;

    public Model(List<MeshObject> meshObjects) {
        this.meshObjects = meshObjects;
    }

    public void render(Scene scene) {
        for (MeshObject meshObject : meshObjects) {
            meshObject.render(scene);
        }
    }

    public void mulTransform(Matrix4f transformation) {
        for (var m : meshObjects) {
            m.getTransform().mul(transformation);
        }
    }
}
