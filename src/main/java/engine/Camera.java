package engine;

import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;

public class Camera {

    private Vector3f position;
    private final Vector3f up;

    private float yaw, pitch, roll;

    private float fov, aspectRatio, zNear, zFar;

    private Vector3f direction;

    private Matrix4f projection;
    private Matrix4f view;

    public static Camera createPerspective(float fov, float aspect, float zNear, float zFar) {
        Camera camera = new Camera();
        camera.projection = new Matrix4f().perspective(fov, aspect, zNear, zFar);
        camera.fov = fov;
        camera.aspectRatio = aspect;
        camera.zNear = zNear;
        camera.zFar = zFar;
        return camera;
    }

    public Camera() {
        position = new Vector3f(0, 0, 0f);
        up = new Vector3f(0, 1, 0);
        pitch = 0;
        yaw = -90.f;
        view = new Matrix4f();
    }

    public void zoom(float offset) {
        fov -= offset;
        usePerspective(fov, aspectRatio, zNear, zFar);
    }

    public void move(Vector3f offset) {
        position.add(offset);
    }

    public void moveAlongDirection(float multiplier) {
        position.add(new Vector3f().add(direction).mul(multiplier));
    }

    public void moveAlongCrossDirection(float multiplier) {
        Vector3f crossAxisDirection = new Vector3f().add(direction).cross(up);
        position.add(new Vector3f().add(crossAxisDirection).mul(multiplier));
    }

    public void setViewMatrix(Matrix4f matrix) {
        this.view = matrix;
    }

    public void rotate(Vector3f offset) {
        float sensitivity = 0.1f;
        yaw += (offset.x * sensitivity);
        pitch += (offset.y * sensitivity);

        if (pitch > 89.0f) {
            pitch = 89.f;
        } else if (pitch < -89.f) {
            pitch = -89.f;
        }
    }

    public void update() {
        direction = new Vector3f(
            (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch))),
            (float) Math.sin(Math.toRadians(pitch)),
            (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)))
        ).normalize();

        view = new Matrix4f().lookAt(
                position,
                new Vector3f().add(position).add(direction),
                up
        );
    }

    public void usePerspective(float fov, float aspect, float zNear, float zFar) {
        projection = new Matrix4f().perspective(fov, aspect, zNear, zFar);
        this.fov = fov;
        this.aspectRatio = aspect;
        this.zNear = zNear;
        this.zFar = zFar;
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public Matrix4f getView() {
        return view;
    }
}
