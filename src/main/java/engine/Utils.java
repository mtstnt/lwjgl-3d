package engine;

import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    public static float[] flatten3f(List<Vector3f> vertices) {
        float[] v = new float[vertices.size() * 3];
        for (int i = 0; i < vertices.size(); i++) {
            v[i * 3] = vertices.get(i).x;
            v[(i + 1) * 3] = vertices.get(i).y;
            v[(i + 2) * 3] = vertices.get(i).z;
        }
        return v;
    }

    public static int[] flatten3i(List<Vector3i> indices) {
        int[] v = new int[indices.size() * 3];
        for (int i = 0; i < indices.size(); i++) {
            v[i * 3] = indices.get(i).x;
            v[(i + 1) * 3] = indices.get(i).x;
            v[(i + 2) * 3] = indices.get(i).x;
        }
        return v;
    }

}
