package project.quadric;

import engine.Camera;
import engine.Utils;
import engine.graphics.Mesh;
import engine.graphics.shaders.ShaderProgram;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL40.*;

public class Cone {

    private Mesh base, cone;
    private ShaderProgram shader;

    public Cone(Mesh base, Mesh cone, ShaderProgram shader) {
        this.shader = shader;
        this.base = base;
        this.cone = cone;
    }

    public Cone(Vector3f baseCenter, float height, float radius, ShaderProgram sp) {
        cone = new Mesh();
        cone.setVertices(Utils.flatten3f(Utils.createCone(baseCenter, height, radius)));
        cone.translate(new Vector3f(0, 0, -20));
        cone.rotate((float) Math.toRadians(-45.f), new Vector3f(1, 0, 0));
        cone.setShader(sp);

        base = new Mesh();
        base.setVertices(Utils.flatten3f(Utils.createCircle(baseCenter, radius, 2.f, 0.f, 360.f)));
        base.translate(new Vector3f(0, 0, -20));
        base.rotate((float) Math.toRadians(-45.f), new Vector3f(1, 0, 0));
        base.setShader(sp);
    }

    public void render(Camera camera) {
        cone.render(camera, GL_TRIANGLE_FAN);
        base.render(camera, GL_TRIANGLE_FAN);
    }

    public void rotate(float deg, Vector3f up) {
        cone.rotate(deg, up);
        base.rotate(deg, up);
    }
}
