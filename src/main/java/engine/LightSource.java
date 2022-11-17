package engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class LightSource {
    protected Vector3f position;
    protected Vector4f color;

    public LightSource(Vector3f position, Vector4f color) {
        this.position = position;
        this.color = color;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector4f getColor() {
        return color;
    }
}
