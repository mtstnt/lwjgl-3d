package project.quadric;

import engine.Camera;
import engine.Scene;
import engine.Utils;
import engine.graphics.Mesh;
import engine.graphics.shaders.ShaderProgram;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL40.*;

public class MainScene extends Scene {

    private Mesh ellipsoid;
    private Mesh ellipticCone;
    private Mesh ellipticParaboloid;
    private Mesh hyperboloid1Sheet;
    @Override
    public void start() throws Exception {
        this.camera = new Camera();
        this.camera.usePerspective(
                (float) Math.toRadians(45.f),
                640.f / 480.f,
                0.01f,
                100.f
        );

//        cone = new Cone(new Vector3f(-8, 0, 0), 10.f, 5.f, ShaderProgram.createDefault());
//        cyllinder = new Cyllinder(new Vector3f(8, 0, 0), 5.f, 3.f, 1.f, ShaderProgram.createDefault());
        {
            var vertices = Utils.createEllipsoid(
                    new Vector3f(0, 0, 0),
                    new Vector3f(5.f, 5.f, 5.f),
                    24, 24
            );

            var indices = Utils.createEllipsoidIndices(24, 24);

            ellipsoid = new Mesh();
            ellipsoid.setVertices(Utils.flatten3f(vertices));
            ellipsoid.setIndices(Utils.flatten3i(indices));
            ellipsoid.setShader(ShaderProgram.createDefault());
            ellipsoid.translate(new Vector3f(0, 0, -10));
        }
        {
//            var vertices = Utils.createHyperboloidOneSheet(
//                    new Vector3f(-20, 0, 0),
//                    new Vector3f(5f, 5f, 5f),
//                    32, 32
//            );
//
//            var indices = Utils.createEllipsoidIndices(32, 32);
//
//            hyperboloid1Sheet = new Mesh();
//            hyperboloid1Sheet.setVertices(Utils.flatten3f(vertices));
//            hyperboloid1Sheet.setIndices(Utils.flatten3i(indices));
//            hyperboloid1Sheet.setShader(ShaderProgram.createDefault());
//            hyperboloid1Sheet.translate(new Vector3f(0, 0, -50));
        }
        {
            var vertices = Utils.createEllipticCone(
                    new Vector3f(0, 0, 0),
                    new Vector3f(5.f, 5.f, 5.f),
                    32, 32
            );

            var indices = Utils.createEllipsoidIndices(32, 32);

            ellipticCone = new Mesh();
            ellipticCone.setVertices(Utils.flatten3f(vertices));
            ellipticCone.setIndices(Utils.flatten3i(indices));
            ellipticCone.setShader(ShaderProgram.createDefault());
            ellipticCone.translate(new Vector3f(0, 0, -50));
        }
        {
            var vertices = Utils.createEllipticParaboloid(
                    new Vector3f(20, 0, 0),
                    new Vector3f(5.f, 5.f, 5.f),
                    32, 32
            );

            var indices = Utils.createEllipsoidIndices(32, 32);

            ellipticParaboloid = new Mesh();
            ellipticParaboloid.setVertices(Utils.flatten3f(vertices));
            ellipticParaboloid.setIndices(Utils.flatten3i(indices));
            ellipticParaboloid.setShader(ShaderProgram.createDefault());
            ellipticParaboloid.translate(new Vector3f(0, 0, -50));
        }
    }

    @Override
    public void update(float delta) {
//        cone.rotate((float) Math.toRadians(40.f) * delta, new Vector3f(1, 0, 0));
//
//        cone.render(camera);
//        cyllinder.render(camera);
//        hyperboloid1Sheet.rotate((float) Math.toRadians(40.f) * delta, new Vector3f(1, 0, 0));
//        ellipticCone.rotate((float) Math.toRadians(40.f) * delta, new Vector3f(1, 0, 0));
//        ellipticParaboloid.rotate((float) Math.toRadians(40.f) * delta, new Vector3f(1, 0, 0));

//        hyperboloid1Sheet.render(camera, GL_LINE_STRIP);
        ellipsoid.render(this, GL_LINE_STRIP);
//        ellipticCone.render(camera, GL_LINE_STRIP);
//        ellipticParaboloid.render(camera, GL_LINE_STRIP);
    }

    @Override
    public void dispose() {

    }
}
