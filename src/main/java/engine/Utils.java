package engine;

import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {

    public static String readFileAsString(String path) throws FileNotFoundException {
        String result = "";
        FileReader reader = new FileReader(path);
        Scanner scanner = new Scanner(reader);
        while (scanner.hasNextLine()) {
            result += scanner.nextLine() + "\n";
        }
        return result;
    }

    public static float[] flatten3f(List<Vector3f> s) {
        float[] v = new float[s.size() * 3];
        for (int i = 0; i < s.size(); i++) {
            v[i * 3] = s.get(i).x;
            v[i * 3 + 1] = s.get(i).y;
            v[i * 3 + 2] = s.get(i).z;
        }
        return v;
    }

    public static int[] flatten3i(List<Vector3i> s) {
        int[] v = new int[s.size() * 3];
        for (int i = 0; i < s.size(); i++) {
            v[i * 3] = s.get(i).x;
            v[i * 3 + 1] = s.get(i).y;
            v[i * 3 + 2] = s.get(i).z;
        }
        return v;
    }

    public static ArrayList<Vector3f> createCircle(
            Vector3f center,
            float radius,
            float step,
            float startAngle,
            float endAngle
    ) {
        var vertices = new ArrayList<Vector3f>();
        vertices.add(new Vector3f(center.x, center.y, center.z));
        for (float i = startAngle; i <= endAngle; i += step) {
            vertices.add(new Vector3f(
                    center.x + radius * (float) Math.cos(Math.toRadians(i)),
                    center.y + radius * (float) Math.sin(Math.toRadians(i)),
                    center.z
            ));
        }
        return vertices;
    }

    public static ArrayList<Vector3f> createCone(
            Vector3f base,
            float height,
            float radius
        ) {
        ArrayList<Vector3f> results = new ArrayList<>();
        results.add(new Vector3f(base.x, base.y, base.z + height));
        results.addAll(Utils.createCircle(base, radius, 2.f, 0.f, 360.f));
        return results;
    }

    public static ArrayList<Vector3f> createEllipticCone(
            Vector3f position,
            Vector3f radius,
            int stackCount,
            int sectorCount
    ) {
        double sectorStep = 2 * Math.PI / sectorCount;
        double stackStep = Math.PI / stackCount;

        var vertices = new ArrayList<Vector3f>();
        double x, y, z;

        for (int i = 0; i <= stackCount; i++) {
            double stackAngle = Math.PI / 2 - i * stackStep;

            x = radius.x * stackAngle;
            y = radius.y * stackAngle;
            z = radius.z * stackAngle;

            for (int j = 0; j <= sectorCount; j++) {
                double v = j * sectorStep;

                var vertex = new Vector3f();

                vertex.x = (float) (position.x + x * Math.cos(v));
                vertex.y = (float) (position.y + y);
                vertex.z = (float) (position.z + z * Math.sin(v));

                vertices.add(vertex);
            }
        }
        return vertices;
    }

    public static ArrayList<Vector3f> createEllipsoid(
            Vector3f position,
            Vector3f radius,
            int stackCount,
            int sectorCount
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
        return vertices;
    }

    public static ArrayList<Vector3i> createEllipsoidIndices(
            int sectorCount, int stackCount
    ) {
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
        return indices;
    }

    public static ArrayList<Vector3f> createHyperboloidOneSheet(
            Vector3f position,
            Vector3f radius,
            int stackCount,
            int sectorCount
    ) {
        double sectorStep = 2 * Math.PI / sectorCount;
        double stackStep = Math.PI / stackCount;

        var vertices = new ArrayList<Vector3f>();
        double x, y, z;

        for (int i = 0; i <= stackCount; i++) {
            double stackAngle = Math.PI / 2 - i * stackStep;

            x = radius.x * (1.0 / Math.cos(stackAngle));
            y = radius.y * Math.tan(stackAngle);
            z = radius.z * (1.0 / Math.cos(stackAngle));

            for (int j = 0; j <= sectorCount; j++) {
                double sectorAngle = j * sectorStep;

                var vertex = new Vector3f();

                vertex.x = (float) (position.x + x * Math.cos(sectorAngle));
                vertex.y = (float) (position.y + y);
                vertex.z = (float) (position.z + z * Math.sin(sectorAngle));

                vertices.add(vertex);
            }
        }
        return vertices;
    }

    public static ArrayList<Vector3f> createHyperboloidParaboloid(
            Vector3f position,
            Vector3f radius,
            int stackCount,
            int sectorCount
    ) {
        double sectorStep = 2 * Math.PI / sectorCount;
        double stackStep = Math.PI / stackCount;

        var vertices = new ArrayList<Vector3f>();
        double x, y, z;

        for (int i = 0; i <= stackCount; i++) {
            double stackAngle = Math.PI / 2 - i * stackStep;

            x = radius.x * stackAngle;
            y = radius.y * stackAngle * stackAngle;
            z = radius.z * stackAngle;

            for (int j = 0; j <= sectorCount; j++) {
                double sectorAngle = j * sectorStep;

                var vertex = new Vector3f();

                vertex.x = (float) (position.x + x * Math.tan(sectorAngle));
                vertex.y = (float) (position.y + y);
                vertex.z = (float) (position.z + z * Math.acos(sectorAngle));

                vertices.add(vertex);
            }
        }
        return vertices;
    }

    public static ArrayList<Vector3f> createEllipticParaboloid(
            Vector3f position,
            Vector3f radius,
            int stackCount,
            int sectorCount
    ) {
        double sectorStep = 2 * Math.PI / sectorCount;
        double stackStep = Math.PI / stackCount;

        var vertices = new ArrayList<Vector3f>();
        double x, y, z;

        for (int i = 0; i <= stackCount; i++) {
            double stackAngle = Math.PI / 2 - i * stackStep;

            x = radius.x * stackAngle;
            y = radius.y * stackAngle * stackAngle;
            z = radius.z * stackAngle;

            for (int j = 0; j <= sectorCount; j++) {
                double sectorAngle = j * sectorStep;

                var vertex = new Vector3f();

                vertex.x = (float) (position.x + x * Math.cos(sectorAngle));
                vertex.y = (float) (position.y + y);
                vertex.z = (float) (position.z + z * Math.sin(sectorAngle));

                vertices.add(vertex);
            }
        }
        return vertices;
    }
}
