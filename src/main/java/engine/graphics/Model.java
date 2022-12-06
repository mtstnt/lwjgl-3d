package engine.graphics;

import engine.AssimpLoader;
import engine.Scene;
import engine.graphics.shaders.ShaderProgram;
import engine.interfaces.Renderable;
import org.joml.Matrix4f;

import java.io.File;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class Model implements Renderable {

    protected List<MeshObject> meshObjects;

    public static Model load(String objPath) throws Exception {
        return AssimpLoader.loadModel(
                objPath,
                new File(objPath).getParent(),
                aiProcess_Triangulate | aiProcess_GenSmoothNormals | aiProcess_FixInfacingNormals
        );
    }

    public Model(List<MeshObject> meshObjects) {
        this.meshObjects = meshObjects;
    }

    public void render(Scene scene) {
        for (MeshObject meshObject : meshObjects) {
            meshObject.render(scene);
        }
    }

    @Override
    public void render(Scene scene, ShaderProgram customShader) {
        for (MeshObject meshObject : meshObjects) {
            meshObject.render(scene, customShader);
        }
    }

    public void mulTransform(Matrix4f transformation) {
        for (var m : meshObjects) {
            m.getTransform().mul(transformation);
        }
    }
}
