package engine;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex {

    private Vector3f vertex;
    private Vector3f normals;
    private Vector2f texCoords;

    public Vertex(Vector3f vertex, Vector3f normals, Vector2f texCoords) {
        this.vertex = vertex;
        this.normals = normals;
        this.texCoords = texCoords;
    }

    public float[] getJoinedBuffer() {
        return new float[] {
                vertex.x,
                vertex.y,
                vertex.z,
                normals.x,
                normals.y,
                normals.z,
                texCoords.x,
                texCoords.y
        };
    }
}
