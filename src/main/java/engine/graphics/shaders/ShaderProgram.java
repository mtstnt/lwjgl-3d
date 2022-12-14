package engine.graphics.shaders;

import engine.Utils;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.io.FileNotFoundException;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL40.*;

public class ShaderProgram {
    private int programID;

    public static ShaderProgram createDefault() throws Exception {
        return ShaderProgram.fromPath(
                "assets/shaders/default/vertex.glsl",
                "assets/shaders/default/fragment.glsl"
        );
    }

    public static ShaderProgram fromPath(
            String vertex,
            String fragment
    ) throws Exception {
        return new ShaderProgram(
                new Shader(vertex, GL_VERTEX_SHADER),
                new Shader(fragment, GL_FRAGMENT_SHADER)
        );
    }

    public ShaderProgram(
            Shader vertexShader,
            Shader fragmentShader,
            Shader geometryShader
    ) throws Exception {
        this.programID = glCreateProgram();

        glAttachShader(this.programID, vertexShader.getId());
        glAttachShader(this.programID, fragmentShader.getId());
        glAttachShader(this.programID, geometryShader.getId());

        glLinkProgram(this.programID);

        if (0 == glGetProgrami(this.programID, GL_LINK_STATUS)) {
            String log = glGetProgramInfoLog(this.programID);
            throw new Exception("Error linking program: " + log);
        }
    }

    public ShaderProgram(
            Shader vertexShader,
            Shader fragmentShader
    ) throws Exception {
        this.programID = glCreateProgram();

        glAttachShader(this.programID, vertexShader.getId());
        glAttachShader(this.programID, fragmentShader.getId());

        glLinkProgram(this.programID);

        if (0 == glGetProgrami(this.programID, GL_LINK_STATUS)) {
            String log = glGetProgramInfoLog(this.programID);
            throw new Exception("Error linking program: " + log);
        }
    }

    public void bind() {
        glUseProgram(this.programID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void setUniformMat4f(String name, Matrix4f val) {
        glUniformMatrix4fv(getLocation(name), false, val.get(new float[16]));
    }
    public void setUniformInt(String name, int val) {
        glUniform1i(getLocation(name), val);
    }
    public void setUniformFloat(String name, float val) {
        glUniform1f(getLocation(name), val);
    }
    public void setUniformVec3f(String name, Vector3f val) {
        glUniform3f(getLocation(name), val.x, val.y, val.z);
    }
    public void setUniformVec4f(String name, Vector4f val) {
        glUniform4f(getLocation(name), val.x, val.y, val.z, val.w);
    }

    private int getLocation(String name) {
        return glGetUniformLocation(this.programID, name);
    }
}
