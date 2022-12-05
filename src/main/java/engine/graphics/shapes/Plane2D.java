package engine.graphics.shapes;

import engine.Scene;
import engine.graphics.Mesh;
import engine.graphics.MeshObject;
import engine.graphics.Texture;
import engine.graphics.shaders.ShaderProgram;
import engine.interfaces.Renderable;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Plane2D implements Renderable {
    private MeshObject meshObject;
    public Plane2D(
            Vector3f a, Vector3f b,
            Vector3f c, Vector3f d,
            Texture texture,
            ShaderProgram shaderProgram
    ) {

        meshObject = new MeshObject();
        meshObject.setVertices(new float[] {
            a.x, a.y, a.z,
        });
        meshObject.setIndices(new int[] {
                0, 2, 3,
                1, 2, 0
        });
//        mesh.setTexCoords(new float[] {
//                0,5,
//                0,0,
//                5,0,
//                5,0,
//                5,5,
//                0,5
//        });
//        mesh.setNormals(new float[]{
//                0.f, 1.f, 0.f, // 2
//                0.f, 1.f, 0.f, // 2
//                0.f, 1.f, 0.f, // 2
//                0.f, 1.f, 0.f, // 2
//                0.f, 1.f, 0.f, // 2
//                0.f, 1.f, 0.f // 2
//        });
//        mesh.setTexture(texture);
//        mesh.setShader(shaderProgram);
    }

    @Override
    public void render(Scene scene) {
        meshObject.render(scene);
    }

    public Matrix4f getTransform() {
        return meshObject.getTransform();
    }
}
