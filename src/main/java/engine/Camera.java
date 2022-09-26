package engine;

import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private Vector3f position;

    private Matrix4f projection;
    private Matrix4f view;

    public Camera() {
        position = new Vector3f();
        view = new Matrix4f().translate(0, 0, -5f);
    }

    public void usePerspective(float fov, float aspect, float zNear, float zFar) {
        projection = new Matrix4f().perspective(fov, aspect, zNear, zFar);
    }

    public void useOrthogonal(float width, float height, float zNear, float zFar) {
        projection = new Matrix4f().ortho(0, width, 0, height, zNear, zFar);
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public Matrix4f getView() {
        return view;
    }
}
