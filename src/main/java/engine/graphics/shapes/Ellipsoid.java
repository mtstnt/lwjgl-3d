package engine.graphics.shapes;

import engine.Scene;
import engine.Utils;
import engine.game.GameObject;
import engine.graphics.Mesh;
import engine.graphics.shaders.ShaderProgram;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;

public class Ellipsoid extends GameObject {

    public Ellipsoid(
            Vector3f position,
            Vector3f radius,
            int stackCount,
            int sectorCount,
            ShaderProgram shader
    ) {
        double sectorStep = 2 * Math.PI / sectorCount;
        double stackStep = Math.PI / stackCount;

        var vertices = new ArrayList<Vector3f>();
        double x, y, z;

        for (int i = 0; i <= stackCount; i++) {
            double stackAngle = Math.PI / 2 - i * stackStep;

            x = radius.x * Math.cos(stackAngle);
            y = radius.y * Math.sin(stackAngle);
            z = radius.z * Math.cos(stackAngle);

            for (int j = 0; j <= sectorCount; j++) {
                double v = j * sectorStep;

                var vertex = new Vector3f();

                vertex.x = (float) (position.x + x * Math.cos(v));
                vertex.y = (float) (position.y + y);
                vertex.z = (float) (position.z + z * Math.sin(v));

                vertices.add(vertex);
            }
        }

        var indices = new ArrayList<Vector3i>();
        int k1, k2;
        for (int i = 0; i < stackCount; i++) {
            k1 = i * (sectorCount - 1);
            k2 = k1 + sectorCount + 1;
            for (int j = 0; j < sectorCount; j++, k1++, k2++) {
                if (i != 0) {
                    indices.add(new Vector3i(k1, k2, k1 + 1));
                }
                if (i != stackCount - 1) {
                    indices.add(new Vector3i(k1 + 1, k2, k2 + 1));
                }
            }
        }

        this.mesh = new Mesh();
        this.mesh.setVertices(Utils.flatten3f(vertices));
        this.mesh.setIndices(Utils.flatten3i(indices));
        this.mesh.setShader(shader);
    }
}
