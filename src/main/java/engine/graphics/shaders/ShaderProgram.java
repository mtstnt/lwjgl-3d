package engine.graphics.shaders;

import engine.Utils;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
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

    public void setUniformMat4d(String name, Matrix4d val) {
        System.out.println("Name: " + name + ": " + getLocation(name));
        double[] n = new double[16];
        n = val.get(n);
        glUniformMatrix4dv(getLocation(name), false, n);
    }

    public void setUniformVec4f(String name, Vector4f val) {
        glUniform4f(getLocation(name), val.x, val.y, val.z, val.w);
    }

    private int getLocation(String name) {
        return glGetUniformLocation(this.programID, name);
    }

}
