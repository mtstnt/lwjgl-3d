package engine.graphics.shaders;

import engine.Utils;

import java.io.FileNotFoundException;

import static org.lwjgl.opengl.GL40.*;

public class Shader {

    private int id;

    public Shader(String path, int type) throws Exception {
        id = glCreateShader(type);

        String source = Utils.readFileAsString(path);

        glShaderSource(id, source);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == 0) {
            String log = glGetShaderInfoLog(id);
            System.err.println(log);
            throw new Exception("Error processing shader: " + log);
        }
    }

    public int getId() {
        return id;
    }
}
