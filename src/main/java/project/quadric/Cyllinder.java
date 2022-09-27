package project.quadric;

import engine.Camera;
import engine.Utils;
import engine.graphics.Mesh;
import engine.graphics.shaders.ShaderProgram;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL40.*;

public class Cyllinder {
    private Mesh baseMesh, topMesh, sideMesh;
    private Vector3f baseCenter;
    private float radiusTop, radiusBottom, height;

    public Cyllinder(Vector3f baseCenter, float radiusTop, float radiusBottom, float height, ShaderProgram shader) {
        this.baseCenter = baseCenter;
        this.height = height;
        this.radiusBottom = radiusBottom;
        this.radiusTop = radiusTop;

        // Bot circle
        this.baseMesh = new Mesh();
        this.baseMesh.setVertices(Utils.flatten3f(this.genCircleVertices(baseCenter, radiusBottom)));
        this.baseMesh.setShader(shader);
        this.baseMesh.translate(new Vector3f(0, 0, -20));


        // Top circle
        this.topMesh = new Mesh();
        this.topMesh.setVertices(Utils.flatten3f(this.genCircleVertices(
                new Vector3f(baseCenter.x, baseCenter.y + height, baseCenter.z),
                radiusTop
        )));
        this.topMesh.setShader(shader);
        this.topMesh.translate(new Vector3f(0, 0, -50));
    }

    private ArrayList<Vector3f> genCircleVertices(Vector3f center, float radius) {
        int startAngle = 0, endAngle = 360, step = 1;

        var vertices = new ArrayList<Vector3f>();
        vertices.add(center);
        for (int i = startAngle; i <= endAngle; i += step) {
            vertices.add(new Vector3f(
                    center.x + radius * (float) Math.cos(Math.toRadians(i)),
                    center.y + radius * (float) Math.sin(Math.toRadians(i)),
                    center.z
            ));
        }
        return vertices;
    }

    public void render(Camera camera) {
        this.baseMesh.render(camera, GL_TRIANGLE_FAN);
        this.topMesh.render(camera, GL_TRIANGLE_FAN);
    }
}
